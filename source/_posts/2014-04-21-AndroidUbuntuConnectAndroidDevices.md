---
date: 2014-04-21
title: Ubuntu 连接Android真机（华为C8813Q)
category: ANDROID
layout: default
tags: Ubuntu
---

Android手机连接ubuntu之后,若已在环境变量中配置了adb,直接在终端中输入

    adb devices

会显示一串????????
解决方法如下：

<!--more-->

(1):手机连接上笔记本后，在手机中的usb调试一定要开启，然后在终端中键入`lsusb`

出现如下内容:

```bash
wxp@wxp:~$ lsusb
Bus 001 Device 002: ID 8087:8008 Intel Corp.
Bus 002 Device 002: ID 8087:8000 Intel Corp.
Bus 003 Device 002: ID 046d:c52f Logiblog, Inc. Wireless Mouse M305
Bus 003 Device 011: ID 12d1:1038 Huawei Technologies Co., Ltd. Ideos (debug mode)
Bus 003 Device 003: ID 04f2:b40d Chicony Electronics Co., Ltd
Bus 001 Device 001: ID 1d6b:0002 Linux Foundation 2.0 root hub
Bus 002 Device 001: ID 1d6b:0002 Linux Foundation 2.0 root hub
Bus 003 Device 001: ID 1d6b:0002 Linux Foundation 2.0 root hub
Bus 004 Device 001: ID 1d6b:0003 Linux Foundation 3.0 root hub
```

找到你的手机对应的ID，(我的是`Huawei`的那行)

(2):接着在终端中输入

    cd /etc/udev/rules.d/

进入到此目录下，新建文档“90-android.rules”

    touch 90-android.rules
    sudo gedit 90-android.rules

进入文档后，输入以下两行内容

>- SUBSYSTEM =="usb" , SYSFS{ "Huawei Technologies Co." } =="12d1" , MODE ="0666"
>- SUBSYSTEM=="usb", ENV{DEVTYPE}=="usb_device", MODE="0666"

注意:SYSFS后面就是填你自己的ID，我的手机是12d1

文档内容完成后赋予所有人所有权限

    sudo chmod 777 90-android.rules



(3):在终端中输入

    sudo /etc/init.d/udev restart

对usb设备重启连接。

    sudo adb kill-server

注意:如果不行则进入你的Android sdk目录下的platform-tools目录中再尝试一次。

继续：

    sudo adb start-server
    sudo adb devices



至此应该可以正常显示了，如下

```bash
adb devices
List of devices attached
A49947CBC4AEdevice
```


然而华为手机可能仍不能正常显示

1. 打开你的手机设置，找到附加设置（我的手机已经刷成MIUI了）
2. 选择工程模式
3. 接着选择后台设置
4. 接着选择 USB 端口配置，默认是Normal模式
5. 选择 Google 模式


选择好后,重新启动手机.

在ubuntu终端中输入

    adb devices

- - -
THE END.
