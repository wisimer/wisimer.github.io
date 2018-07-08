---
date: 2014-06-15
layout: default
title: TextView实现圆角矩形背景
category: ANDROID
---

简单实现带圆角矩形背景图片的TextView

#### 1.在drawable中放置两张带有圆角的背景图片

一张是正常状态下的背景tv_bg_normal.9.png,另一张是按下时的背景图片tv_bg_pressed.9.png.

<!--more-->

#### 2.在drawable新建一个文件pre_bg.xml

```java
<selector
  xmlns:android="http://schemas.android.com/apk/res/android">
  <item
    android:state_enabled="true"
    android:state_selected="true"
    android:drawable="@drawable/tv_bg_pressed" />

  <item
    android:state_enabled="true"
    android:state_pressed="true"
    android:drawable="@drawable/tv_bg_pressed"/>

  <item
    android:drawable="@drawable/tv_bg_normal"/>
</selector>
```

#### 3.设置TextView的属性

```java
android:background="@drawable/pre_bg"
android:clickable="true"
```

- - -
THE END.
