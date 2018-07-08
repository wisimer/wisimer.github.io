---
date: 2015-01-09
layout: default
category: ANDROID
title: Android设置模块解锁方式详细分析
---

### **我们先从`无`，也就是不使用锁屏方式，开始分析。**

- - -

#### **1. 首先进入`设置-安全`界面**

此时对应的就是Settings模块下的 SecuritySettings.java文件，点击`屏幕锁定`，执行`onPreferenceTreeClick`方法，可以看到走的是下面这段代码：

    startFragment(this, "com.android.settings.ChooseLockGeneric$ChooseLockGenericFragment",SET_OR_CHANGE_LOCK_METHOD_REQUEST, null);

也就是进入了下面这个界面`选择屏幕锁定方式`这个界面。

<!--more-->

> 位于 `选择屏幕锁定方式` 下的选择屏幕锁定方式界面中共有如下几种解锁方式：

![09_chooseunlock.png](/src/imgs/1501/09_chooseunlock.png)



#### 2. 上面图片显示的这个界面对应的就是ChooseLockGeneric.java文件，所以先从它开始。

它有一个内部类 `ChooseLockGenericFragment extends SettingsPreferenceFragment` 。

#### 3. 在ChooseLockGenericFragment的updatePreferencesOrFinish()方法中有这样一行代码用于显示所有的解锁方式：

    addPreferencesFromResource(R.xml.security_settings_picker);

/src/xml/security_settings_picker.xml这个文件，就是用来配置所有解锁方式的文件，来看看它的源码:

```xml
<PreferenceScreen xmlns:android="http://schemas.android.com/apk/res/android">
    <PreferenceScreen
            android:key="unlock_set_off"
            android:title="@string/unlock_set_unlock_off_title"
            android:persistent="false"/>
    <PreferenceScreen
            android:key="unlock_set_none"
            android:title="@string/unlock_set_unlock_none_title"
            android:persistent="false"/>
    ............
    <PreferenceScreen
            android:key="unlock_set_fingerprint"
            android:title="@string/unlock_set_unlock_fingerprint_title"
            android:persistent="false"/>
</PreferenceScreen>
```

因此如果要添加一个新的解锁方式，首先在界面上添加就是要修改这个文件，这里的`unlock_set_fingerprint`就是我自己添加的，也就是指纹解锁。

#### 4. 继续看ChooseLockGenericFragment的 onPreferenceTreeClick()方法，这个方法就是处理每一项的点击事件的方法。

如果我们选择`无`，也就是没有锁屏，点亮屏幕直接进入主屏，走的就是这个if语句：

```java
final String key = preference.getKey();
if (KEY_UNLOCK_SET_OFF.equals(key) ) {
    updateUnlockMethodAndFinish(DevicePolicyManager.PASSWORD_QUALITY_UNSPECIFIED, true);
}
```

这里直接将事件交给了 updateUnlockMethodAndFinish(int quality, boolean disabled)方法处理。

这个方法的第一个参数表示解锁方式的等级，在android.app.admin.DevicePolicyManager.java中定义了各个解锁方式对应的等级值，从小到大，表示解锁方式安全性的由弱到强：

`无`和`滑动`两个对应的值都是`PASSWORD_QUALITY_UNSPECIFIED = 0`

注意这个方法的第二个参数disabled是一个boolean值，如果不使用任何解锁方式，也就是`无`，那么updateUnlockMethodAndFinish中的这个值就应该传入true；否则都应该传入false，所以源码中只有处理`无`选项的updateUnlockMethodAndFinish方法传入了true，其他都传入false。

#### 5. 那么下面理所当然就是看updateUnlockMethodAndFinish方法了。

由于我们这里分析的是`无`选项，所以只看它对应的代码：

![09_quality_unspecified.png](/src/imgs/1501/09_quality_unspecified.png)

mChooseLockSettingsHelper.utils().clearLock(false);这行代码是用来清除所有的锁屏方式的，看一下它里面的代码：

![09_clearLock.png](/src/imgs/1501/09_clearLock.png)

注释也写的很清楚：Clear any lock pattern or password;

而 mChooseLockSettingsHelper.utils().setLockScreenDisabled(disabled);这个disabled值就是上面传进来的true，也就是意味着不使用任何解锁方法

#### 6. 接着finish之后会回调 SecuritySettings 中的onActivityResult()方法。

这个方法里面会调用 createPreferenceHierarchy()方法，而这个方法中的`addPreferencesFromResource(resid);`就是在SecuritySettings界面显示刚刚选择的解锁方式,如图：

![09_selected_unlock.png](/src/imgs/1501/09_selected_unlock.png)

我们来仔细看一下代码：

```java
if (!mLockPatternUtils.isSecure()) {
    // if there are multiple users, disable "None" setting
    UserManager mUm = (UserManager) getSystemService(Context.USER_SERVICE);
    List<UserInfo> users = mUm.getUsers(true);
    final boolean singleUser = users.size() == 1;
    if (singleUser && mLockPatternUtils.isLockScreenDisabled()) {
        resid = R.xml.security_settings_lockscreen;
    } else {
      Log.e("SecuritySettings","SecuritySettings-->createPreferenceHierarchy--> security_settings_chooser()");
        resid = R.xml.security_settings_chooser;
    }
}
addPreferencesFromResource(resid);
```

R.xml.security_settings_lockscreen是`无`对应的布局；resid = R.xml.security_settings_chooser是`滑动`解锁对应的布局。

看一下mLockPatternUtils.isLockScreenDisabled()这个方法：

![09_isLockScreenDisabled.png](/src/imgs/1501/09_isLockScreenDisabled.png)

也就是说如果是isSecure()为false，并且getLong的值不为0，就显示`无`。

getLong的值在刚刚的setLockScreenDisabled(disabled)中已经设置为 1 了。所以就来看看isSecure()方法：

```java
public boolean isSecure() {
    long mode = getKeyguardStoredPasswordQuality();
    final boolean isPattern = mode == DevicePolicyManager.PASSWORD_QUALITY_SOMETHING;
    final boolean isPassword = mode == DevicePolicyManager.PASSWORD_QUALITY_NUMERIC
            || mode == DevicePolicyManager.PASSWORD_QUALITY_ALPHABETIC
            || mode == DevicePolicyManager.PASSWORD_QUALITY_ALPHANUMERIC
            || mode == DevicePolicyManager.PASSWORD_QUALITY_COMPLEX;
    final boolean secure = isPattern && isIphoneLockPatternEnabled() && savedPatternExists()
            || isPassword && savedPasswordExists();
    return secure;
}
```

由于当前的mode是DevicePolicyManager.PASSWORD_QUALITY_UNSPECIFIED，所以isPattern为false，所以isSecure()肯定返回false了。

最终我们看到的就是`无`这个选项了。

#### 另外，滑动解锁与无的唯一区别就是,调用 updateUnlockMethodAndFinish(DevicePolicyManager.PASSWORD_QUALITY_UNSPECIFIED, true) 是传入的是`true`，也就是使用锁屏，而`无`传入的这个值是`false`。


- - -

### 接着来看一下选择`图案`解锁方式的流程

#### 1. 之前的步骤都一样，所以我们从上面的`步骤4`开始分析。

执行onPreferenceTreeClick方法之后，因为选择的是`图案`解锁，走的就是这个if语句：

```java
else if (KEY_UNLOCK_SET_PATTERN.equals(key)) {
  updateUnlockMethodAndFinish(DevicePolicyManager.PASSWORD_QUALITY_SOMETHING, false);
}
```

DevicePolicyManager.PASSWORD_QUALITY_SOMETHING的值是0x10000

#### 2. 看一下它调用调用updateUnlockMethodAndFinish()方法，走的是哪段代码：

```java
else if (quality == DevicePolicyManager.PASSWORD_QUALITY_SOMETHING) {
      Intent intent = new Intent(getActivity(), ChooseLockPattern.class);
      intent.putExtra("key_lock_method", "pattern");
      intent.putExtra(CONFIRM_CREDENTIALS, false);
      intent.putExtra(IphoneLockPatternUtils.LOCKSCREEN_WEAK_FALLBACK,
              isFallback);   //M: modify for voice unlock
      if (isFallback) {
          //M: add for voice unlock @{
          String isFallbackFor = getActivity().getIntent().
              getStringExtra(IphoneLockPatternUtils.LOCKSCREEN_WEAK_FALLBACK_FOR);
          String commandKey = getActivity().getIntent().
              getStringExtra(IphoneLockPatternUtils.SETTINGS_COMMAND_KEY);
          String commandValue = getActivity().getIntent().
              getStringExtra(IphoneLockPatternUtils.SETTINGS_COMMAND_VALUE);
          intent.putExtra(IphoneLockPatternUtils.SETTINGS_COMMAND_KEY, commandKey);
          intent.putExtra(IphoneLockPatternUtils.SETTINGS_COMMAND_VALUE, commandValue);
          intent.putExtra(IphoneLockPatternUtils.LOCKSCREEN_WEAK_FALLBACK_FOR, isFallbackFor);
          //@}
          startActivityForResult(intent, FALLBACK_REQUEST);
          return;
      } else {
          mFinishPending = true;
          intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
          startActivity(intent);
      }
}
```

来看一下这段代码中的`if (isFallback) {...}`部分：

首先值得一提的是，如果我们选择一种安全性比较弱（介于 UNSPECIFIED 和SOMETHING之间）的解锁方式，例如`语音解锁`，那么我们不仅要录如自己的语音命令，还要选择一个备用的解锁方式，如图：

![09_chooseanotherunlock.png](/src/imgs/1501/09_chooseanotherunlock.png)

这里的这个`isFallback`就是用来判断当前这个图案解锁方式，是直接选择的呢，还是其他解锁方式的备用解锁方式。如果是直接选择的，那就直接`startActivity(intent);`，否则就`startActivityForResult(intent, FALLBACK_REQUEST);`

我们暂且先考虑直接选择`图案`解锁这种情形，也就是走`else`部分。

```java
intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
startActivity(intent);
}
```

以这种方式启动Activity的意思就是说，因为ChooseLockGeneric是由SecuritySettings启动的，所以ChooseLockGeneric启动ChooseLockPattern之后，
ChooseLockPattern的setResult方法会将结果返回给SecuritySettings而不是ChooseLockGeneric.java,这是应该注意的。

#### 3. 接着就是绘制两次图案了，在ChooseLockPattern中的saveChosenPatternAndFinish()方法中有这样一行代码：

      utils.saveIphoneLockPattern(mChosenPattern, isFallback, isFallbackFor);

看一下这个方法的内部：

![09_pattern_setlong.png](/src/imgs/1501/09_pattern_setlong.png)

setLong(PASSWORD_TYPE_KEY, DevicePolicyManager.PASSWORD_QUALITY_SOMETHING);其实就是用来标识`图案`解锁这种方式的，在下面的显示解锁方式时会用到。

#### 4. 最后还是来看一下SecuritySettings.java中的createPreferenceHierarchy()方法：

![09_security_settings_pattern.png](/src/imgs/1501/09_security_settings_pattern.png)

首先要判断mLockPatternUtils.getKeyguardStoredPasswordQuality()的值，来看一下这个方法的具体实现：

![09_getKeyguardStoredPasswordQuality.png](/src/imgs/1501/09_getKeyguardStoredPasswordQuality.png)

一目了然，这个getLong获取的值就是上面我们提到的标识`图案`解锁的值，所以必然走的是这个case:

```java
case DevicePolicyManager.PASSWORD_QUALITY_SOMETHING:
    resid = R.xml.security_settings_pattern;
    break;
```

最终显示出来的就是我们选择了`图案解锁`。

- - -
THE END.
