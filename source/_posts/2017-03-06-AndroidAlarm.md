---
date: 2017-03-06
title: Alarm实现机制分析
category: ANDROID
layout: default
---


alarm的使用这里就不说了，来看看alarm是怎么实现提醒的。

<!--more-->

当我们创建一个alarm的时候，是先加到一个 `batch` 中，系统中有一个 `batch` 列表，专门用于存储所有的alarm。
`AlarmManagerService` 中会启动一个 `AlarmThread` 线程，不停的遍历这个 `batch` 列表，一旦发现有alram的时间已经到达，就把它取出来，然后发送一个PendingIntent。

- - -

- `System.currentTimeMillis()`  系统时间，也就是日期时间，可以被系统设置修改，然后值就会发生跳变。

- `SystemClock.uptimeMillis()` 自开机后，经过的时间，不包括深度睡眠的时间

- `SystemClock.elapsedRealtime()` 自开机后，经过的时间，包括深度睡眠的时间

参考网址 :[http://blog.csdn.net/singwhatiwanna/article/details/18448997](http://blog.csdn.net/singwhatiwanna/article/details/18448997)
- - -
THE END.