---
layout: default
date: 2014-12-03
category: ANDROID
title: Android触摸事件分发流程图
---

- dispatchTouchEvent 表示分发事件，这里所说的分发其实是分发给这个View处理，而不是分发给子View

> 所以如果 dispatchTouchEvent 返回false表示不分发给这个View处理，需要拦截才能给这个View处理；返回true表示分发给这个View处理，而无需拦截。

- onInterceptTouchEvent 表示拦截事件

- onTouchEvent 是具体的处理事件的方法

<!--more-->

#### 一夜无话，按下不表 ####

![03_touchevent](/src/imgs/1412/03_touchevent.png)

- - -
THE END.
