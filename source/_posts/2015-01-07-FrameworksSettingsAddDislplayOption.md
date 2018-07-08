---
date: 2015-01-07
title: Android Settings模块浅析-添加新的显示选项
category: ANDROID
layout: default
des: 总结一下修改设置模块经常用到的方法
---

### 向Display中添加新的设置选项：

- mDisplayPerCategory 是加入到`PERSONLIZE`部分

```java
CheckBoxPreference  PersonalNewSet = (CheckBoxPreference) createPreference(TYPE_CHECKBOX,
    R.string.display_personalize, KEY_LOCK_SCREEN_NOTIFICATIONS, mDisplayPerCategory);
```

- mDisplayDefCategory 是加入到`DISPLAY`部分

```java
CheckBoxPreference  DisplayNewSet = (CheckBoxPreference) createPreference(TYPE_CHECKBOX,
    R.string.display_default, KEY_LOCK_SCREEN_NOTIFICATIONS,mDisplayDefCategory);
```

<!--more-->
看一下效果图，黑色边框圈出的地方就是新加的两个选项：

![07_SettingsDisplay](/src/imgs/1501/07_SettingsDisplay.png)

- - -
THE END.
