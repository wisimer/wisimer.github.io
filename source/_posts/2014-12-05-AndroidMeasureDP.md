---
layout: default
date: 2014-12-05
category: ANDROID
title: Android 屏幕 dp计算方法

---

/res/目录下有时会出现values-sw320dp-hdpi/这样的目录，其实这是为了适配不同分辨率以及PPI而创建的资源。

> 那目录名中对应的dp值该如何计算呢？

首先计算设备的PPI,公式如下：

![05_measuredp.png](/src/imgs/1412/05_measuredp.png)

<!--more-->

Android 根据手机ppi 分为  `160`  `240` `320` `480`

根据上述公式算得PPI之后从这几个值中向上取值,比如计算得到PPI为227，那就取240

最后计算设备的dp:

> dp= 160*(横向px)/ppi

假设计算得到的值为320，那就新建对应的资源目录`/res/values-sw320dp/`，另外如果是在320dp下：

854x480分辨率对应的应该是：`values-sw320dp-hdpi`

如果是在360dp下

960x540对应的是：`values-sw320dp-hdpi`
1280x720对应的是：`values-sw320dp-xhdpi`

这仅是目前常用的一些分辨率
- - -
THE END.
