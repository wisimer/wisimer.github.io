---
date: 2014-08-15
title: 桌面插件AppWidget初探
category: ANDROID
layout: default
---

解析一个最基本的桌面插件的构成

#### 1.Widget的配置文件，在/res/xml/目录下新建music_provider.xml文件

```java
<?xml version="1.0" encoding="utf-8"?>
<appwidget-provider xmlns:android="http://schemas.android.com/apk/res/android"
    android:minWidth="260dip"
    android:minHeight="40dip"
    android:initialLayout="@layout/music_layout">
</appwidget-provider>
```

<!--more-->

#### 2.Widget的布局文件

> /res/layout/music_layout.xml

#### 3.配置Manifest.xml

```java
<receiver
    android:name="com.wxp.widget.MusicWidgetProvider"
    android:label="@string/app_name">
      <intent-filter android:name="android.appwidget.action.APPWIDGET_UPDATE>
      </intent-filter>
      <mata-data android:name="android.appwidget.provider"
                 android:resource="@xml/music_provider" />
</receiver>
```

#### 4.主类MusicProvider继承AppWidgetProvider

- - -
THE END.
