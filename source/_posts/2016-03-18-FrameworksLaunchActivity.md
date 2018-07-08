---
date: 2016-03-18
title: 由主线程更新UI所想到的Window和RootViewImpl的创建过程
category: ANDROID
layout: default
---

之前在项目里写了一段错误的代码，是使用异步线程下载图片之后直接更新UI，导致应用崩溃。报错信息是 `Only the original thread that created a view hierarchy can touch its views.` ，意思就是只有主线程可以更新UI。大家肯定都遇到这个错误过，也符合我们的认识：子线程不能更新UI。但是实际上这个崩溃并不是必现的，不管是用户反馈还是我后来自己重现，都只有第一次进入界面的时候才会引起崩溃，后面都可以正常显示图片，所以带着疑问来一起看看这里面的具体分析吧。

<!--more-->

报错信息如下：

```java
android.view.ViewRootImpl$CalledFromWrongThreadException: Only the original thread that created a view hierarchy can touch its views.
    at android.view.ViewRootImpl.checkThread(ViewRootImpl.java:6556)
    at android.view.ViewRootImpl.invalidateChildInParent(ViewRootImpl.java:942)
    at android.view.ViewGroup.invalidateChild(ViewGroup.java:5081)
    at android.view.View.invalidateInternal(View.java:12717)
    at android.view.View.invalidate(View.java:12681)
    at android.view.View.invalidate(View.java:12665)
    at android.widget.TextView.checkForRelayout(TextView.java:7159)
    at android.widget.TextView.setText(TextView.java:4342)
    at android.widget.TextView.setText(TextView.java:4199)
    at android.widget.TextView.setText(TextView.java:4174)
    at com.wxp.uioperation.OpActivity$2.run(OpActivity.java:60)
    at java.lang.Thread.run(Thread.java:818)
```


### 一、先来分析一下上面的堆栈信息里面所显示出来的代码

看看TextView的setText()方法中的line:4342

```java
private void setText(CharSequence text, BufferType type , boolean notifyBefore, int oldlen) {
    ......

    if (mLayout != null) {
        checkForRelayout();
    }

    ......
}
```

checkForRelayout方法中里面又调用了invalidate()方法，接着又调用了ViewRootImpl中的invalidateChildInParent()方法：

```java
@Override
public ViewParent invalidateChildInParent(int[] location, Rect dirty) {

    checkThread();

    ......
}
```

可以看到invalidateChildInParent()方法的第一行就是checkThread()方法，也就是说我们每次setText()的时候都会检查当前线程是不是mThread这个线程：

```java
void checkThread() {
    if (mThread != Thread. currentThread()) {
        throw new CalledFromWrongThreadException(
                "Only the original thread that created a view hierarchy can touch its views." );
    }
}
```

而mThread变量是在ViewRootImpl的构造函数中初始化的。所以上面的checkThread()方法实际判断的是实例化ViewRootImpl对象所在的线程和当前操作UI，也就是执行setText()的线程是不是同一个线程。

```java
public ViewRootImpl(Context context, Display display) {
    mContext = context;
    mWindowSession = WindowManagerGlobal.getWindowSession();
    mDisplay = display;
    mBasePackageName = context.getBasePackageName();

    mDisplayAdjustments = display.getDisplayAdjustments();

    mThread = Thread.currentThread();

    ......
}
```

从上面的代码中也可以看出在ViewRootImpl中更新UI的时候会checkThread()检查线程。那ViewRootImpl是不是主线程呢？那就要分析一下RootViewImpl是怎么创建出来的了。先看看Activity的布局创建的过程。

- - -

### 二、Activity的布局加载过程以及Window的创建过程

#### 1.先来看看在Activity是怎么设置布局文件的

Activity.setContentView()里面的代码:

```java
public void setContentView(@LayoutRes int layoutResID) {
    getWindow().setContentView(layoutResID);
    initWindowDecorActionBar();
}
```

getWindow()实际得到的是一个Window ，所以Activity的setContentView实际是交给了mWindow的setContentView处理。

```java
public Window getWindow() {
    return mWindow;
}
```

那么Window和Activity还有Activity的布局有什么关系呢？来看看下面这张图：

![0318_activity_window](/src/imgs/1603/0318_activity_window.png)

Activity还有Dialog,Toast以及PopupWindow都对应着一个Window。而它们的视图是不能单独存在的，必须依附在Window上。也就是说Activity的布局会被添加到它对应的Window里面。

#### 2.再来看看Window是怎么创建出来的

在Activity的启动过程中会通过ActivityThread这个类的handleLaunchActivity方法来启动Activity：

```java
private void handleLaunchActivity(ActivityClientRecord r, Intent customIntent) {
    unscheduleGcIdler();
    mSomeActivitiesChanged = true;
    if (r.profilerInfo != null) {
        mProfiler.setProfiler(r.profilerInfo);
        mProfiler.startProfiling();
    }
    handleConfigurationChanged(null, null);
    WindowManagerGlobal.initialize();

    Activity a = performLaunchActivity(r, customIntent);

    if (a != null) {
        r.createdConfig = new Configuration(mConfiguration);
        Bundle oldState = r.state;
        handleResumeActivity(r.token, false, r.isForward,!r.activity.mFinished && !r.startsNotResumed);

        ......

    } else {
        ......
    }
}

```

其中performLaunchActivity()这个方法比较重要，完成了Activity对象的创建，并且调用了Activity的attach()方法，为它关联了上下文Context。handleResumeActivity()会在下面说到。来看看performLaunchActivity：

```java
private Activity performLaunchActivity(ActivityClientRecord r, Intent customIntent) {

    ......

    Activity activity = null;
    try {
        java.lang.ClassLoader cl = r.packageInfo.getClassLoader();
        activity = mInstrumentation.newActivity(cl, component.getClassName(), r.intent);
        StrictMode.incrementExpectedActivityCount(activity.getClass());
        r.intent.setExtrasClassLoader(cl);
        r.intent.prepareToEnterProcess();
        if (r.state != null) {
            r.state.setClassLoader(cl);
        }
    } catch (Exception e) {
        ......
    }

    try {
        Application app = r.packageInfo.makeApplication(false, mInstrumentation);

        if (activity != null) {
            Context appContext = createBaseContextForActivity(r, activity);
            CharSequence title = r.activityInfo.loadLabel(appContext.getPackageManager());
            Configuration config = new Configuration(mCompatConfiguration);

            activity.attach(appContext, this, getInstrumentation(), r.token,
                    r.ident, app, r.intent, r.activityInfo, title, r.parent,
                    r.embeddedID, r.lastNonConfigurationInstances, config,
                    r.referrer, r.voiceInteractor);

            ......

            if (r.isPersistable()) {
                mInstrumentation.callActivityOnCreate(activity, r.state, r.persistentState);
            } else {
                mInstrumentation.callActivityOnCreate(activity, r.state);
            }

            ......
       }
    } catch (SuperNotCalledException e) {
        throw e;

    } catch (Exception e) {
        ......
    }

    return activity;
}
```

可以看到这里使用类加载器创建了一个Activity对象，并通过调用createBaseContextForActivity() 方法创建了一个上下文。
注意到后面的mInstrumentation.callActivityOnCreate()方法，其实它是在Activity创建完成之后调用了Activity的onCreate()方法。

再看看attach()方法:

```java
final void attach(Context context, ActivityThread aThread,
        Instrumentation instr, IBinder token, int ident,
        Application application, Intent intent, ActivityInfo info,
        CharSequence title, Activity parent, String id,
        NonConfigurationInstances lastNonConfigurationInstances,
        Configuration config, String referrer, IVoiceInteractor voiceInteractor) {
    attachBaseContext(context);

    mFragments.attachHost(null /*parent*/);

    mWindow = new PhoneWindow(this);
    mWindow.setCallback(this);
    mWindow.setOnWindowDismissedCallback(this);
    mWindow.getLayoutInflater().setPrivateFactory(this);
    if (info.softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED) {
        mWindow.setSoftInputMode(info.softInputMode);
    }
    if (info.uiOptions != 0) {
        mWindow.setUiOptions(info.uiOptions);
    }
    mUiThread = Thread.currentThread();

    mMainThread = aThread;
    mInstrumentation = instr;
    ......
}
```

在attach方法中也可以看到Activity的成员变量mWindow其实是一个PhoneWindow对象。到这里PhoneWindow已经有了。那我们是如何将自己的布局添加到Window中的？其实在Activity创建的时候会为它的Window添加一个DecorView作为Activity的根布局。DecorView是一个“装饰（Decoration）过的View”，比如说它还包含标题栏以及最重要的内容区域，而我们的布局文件则会添加到这个DecorView的内容区域里面。

#### 3.DecorView的创建

先来看看Window的setContentView()的具体实现：

```java
@Override
public void setContentView(int layoutResID) {

    if (mContentParent == null) {
        installDecor();
    } else if (!hasFeature(FEATURE_CONTENT_TRANSITIONS)) {
        mContentParent.removeAllViews();
    }

    if (hasFeature(FEATURE_CONTENT_TRANSITIONS)) {
        final Scene newScene = Scene.getSceneForLayout(mContentParent, layoutResID,
                getContext());
        transitionTo(newScene);
    } else {
        mLayoutInflater.inflate(layoutResID, mContentParent);
    }
    mContentParent.requestApplyInsets();
    final Callback cb = getCallback();
    if (cb != null && !isDestroyed()) {
        cb.onContentChanged();
    }
}
```

首先就是通过installDecor()方法来生成Window的根布局DecorView：

```java
private void installDecor() {
    if (mDecor == null) {
        mDecor = generateDecor();
        mDecor.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        mDecor.setIsRootNamespace(true);
        if (!mInvalidatePanelMenuPosted && mInvalidatePanelMenuFeatures != 0) {
            mDecor.postOnAnimation(mInvalidatePanelMenuRunnable);
        }
    }
    if (mContentParent == null) {
        mContentParent = generateLayout(mDecor);
        ......
    }
}
```

mDecor作为PhoneWindow的一个成员变量，如果mDecor为空，则调用generateDecor()直接返回一个DecorView对象：

```java
protected DecorView generateDecor() {
    return new DecorView(getContext(), -1);
}
```

那这个mContentParent是什么呢？看看generateLayout()方法：

```java
protected ViewGroup generateLayout(DecorView decor) {
    ......
    View in = mLayoutInflater.inflate(layoutResource, null);
    decor.addView(in, new ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT));
    mContentRoot = (ViewGroup) in;

    ViewGroup contentParent = (ViewGroup)findViewById(ID_ANDROID_CONTENT);
    ......

    return contentParent;
}
```

这里的layoutResource是上文（省略）根据Window的属性从系统中选择出的一个布局文件，而这些布局文件有一个共同的特征：都含有一个id为`android.R.id.content`的FrameLayout。然后把这个布局文件添加到DecorView中，并且找到android.R.id.content这个FrameLayout赋值给contentParent。

而上面Window中的setContentView()方法中有这段代码：

> mLayoutInflater.inflate(layoutResID, mContentParent);

很明显，是将我们设置的布局添加到mContentPartent中。

到这里我们已经知道了Window是如何产生的，还有我们的布局是如何添加到DecorView中的。那么DecorView又是如何添加到Window中的呢？那就通过下面要说的ViewRootImpl来实现了。

- - -

### 三、ViewRootImpl的创建

上文在Activity的初始化过程中的performLaunchActivity()方法中已经把Window和DecorView创建好了，但是并没有看到ViewRootImpl的身影。我们往下看，在ActivityThread的handleResumeActivity()方法中直接获取到Activity中的Window所包含的DecorView:

> r.window.getDecorView();

接着通过ViewManager的addView方法将DecorView添加到Window中，代码如下：

```java

final void handleResumeActivity(IBinder token, boolean clearHide, boolean isForward, boolean reallyResume) {
    ......

    if (r != null) {
        final Activity a = r.activity;
        ......
        if (r.window == null && !a.mFinished && willBeVisible) {
            r.window = r.activity.getWindow();
            View decor = r.window.getDecorView();
            decor.setVisibility(View.INVISIBLE);
            ViewManager wm = a.getWindowManager();
            WindowManager.LayoutParams l = r.window.getAttributes();
            a.mDecor = decor;
            l.type = WindowManager.LayoutParams.TYPE_BASE_APPLICATION;
            l.softInputMode |= forwardBit;
            if (a.mVisibleFromClient) {
                a.mWindowAdded = true;
                wm.addView(decor, l);
            }
        } else if (!willBeVisible) {
            r.hideForNow = true;
        }

        ......

        if (!r.activity.mFinished && willBeVisible
                && r.activity.mDecor != null && !r.hideForNow) {
            ......
            if (r.activity.mVisibleFromClient) {
                r.activity.makeVisible();
            }
        }

        ......

    } else {
        ......
    }
}

```

ViewManager是一个接口。它定义了Window对View几个基本的操作：

```java
public void addView(View view, ViewGroup.LayoutParams params);
public void updateViewLayout(View view, ViewGroup.LayoutParams params);
public void removeView(View view);
```

WindowManager也是一个接口，它继承自ViewManager接口。而WindowManagerImpl则是WindowManager的具体实现。
看到WindowManagerImpl的addView方法中，直接调用了mGlobal.addView()。

```java
@Override
public void addView(@NonNull View view, @NonNull ViewGroup.LayoutParams params) {
    applyDefaultToken(params);
    mGlobal.addView(view, params, mDisplay, mParentWindow);
}
```

mGlobal是一个WindowManagerGlobal对象，重点来了，看看它的addView()方法：

```java
public void addView(View view, ViewGroup.LayoutParams params ,
        Display display , Window parentWindow) {
    ......
    ViewRootImpl root;
    View panelParentView = null;

    synchronized ( mLock) {

        ......

        root = new ViewRootImpl(view.getContext() , display);

        view.setLayoutParams(wparams) ;

        mViews .add(view);
        mRoots .add(root);
        mParams .add(wparams) ;
    }

    // do this last because it fires off messages to start doing things
    try {
        root.setView(view, wparams, panelParentView) ;
    } catch (RuntimeException e) {
        ......
    }
}
```
在WindowManagerGlobal的addView方法中会实例化ViewRootImpl对象。然后将这个对象添加到mRoots这个集合中去。

```
mView存储的是所有Window对应的View
mRoots存储的是所有Window对应的ViewRootImpl
mParams存储的是所有Window对应的布局参数
```

也就是说WindowManagerGlobal作为一个单例会把应用程序中的所有Window保存起来统一管理。

接着看到最后的root.setView方法，就是将DecorView传递给RootViewImpl，看到这里我们可以知道RootViewImpl实际上是作为一个媒介来替Window管理的它的布局。所以在上面的报错信息里，更新布局的时候才会执行到ViewRootImpl.invalidateChildInParent()方法

- - -

从上面的分析可以看到ViewRootImpl所在的线程其实就是ActivityThread所在的线程，也就是主线程。所以上面说的checkThread()方法是判断ViewRootImpl对象所在的线程和操作UI的线程是不是同一个线程，确实就是每次操作UI的时候都要检查是否处在UI线程。

那在ViewRootImpl创建之前我们去更新UI呢？其实可以在任意线程更新UI，这也就是我一开始说的，为什么只有第一次进入界面才会报错，是因为从网络下载图片需要一定的时间，而下载完成之后RootViewImpl已经创建完成，这个时候再去更新UI必然会报错。而第二次进入界面的时候，由于本地已由图片缓存，在RootViewImpl创建之前直接加载图片更新UI，就没有报错了。

- - -
THE END.
