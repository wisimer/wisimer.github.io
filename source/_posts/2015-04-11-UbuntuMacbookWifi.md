---
date: 2015-04-11
layout: default
category: ANDROID
tags: Ubuntu
title: MacbookPro(2011 early)安装 Ubuntu12.04之后无线网不能使用
---

今天闲着无聊把电脑装类个Ubuntu,装完之后发现无线网用不了，真炫酷。然后想起来前几天在Ubuntu上折腾小米wifi的时候，可以将小米Wifi当外置wifi接收器使用。

> 见帖[http://bbs.xiaomi.cn/thread-10424927-1-1.html](http://bbs.xiaomi.cn/thread-10424927-1-1.html)

然后就顺利成功上网。

<!--more-->

但是如何使用电脑自带的无线接收器呢，于是展开一系列大搜索。终于在这里发现了

> [https://help.ubuntu.com/community/MacBookPro8-2](https://help.ubuntu.com/community/MacBookPro8-2)

首先要确保现在的电脑可以上网（真矛盾），幸好刚刚装了小米Wifi，跟着它的步骤走：

#### Step1 :在终端执行如下命令

    sudo add-apt-repository ppa:mpodroid/mactel
    sudo apt-get update
    sudo apt-get install b43-fwcutter firmware-b43-installer

#### Step2 ：For Precise, the Compat module should also be installed

    sudo apt-get install linux-backports-modules-cw-3.3-precise-generic

#### Step3 :打开文件 /etc/modprobe.d/blacklist.conf ，在末尾添加

    blacklist ndiswrapper

#### Step4 :创建或者编辑文件 /etc/pm/config.d/modules 修改里面的内容

    SUSPEND_MODULES="b43 bcma"

文章里面说重启之后就可以生效类，但是我试了一下还要在 `系统设置-附加驱动` 里面激活刚刚安装的驱动。然后重启就可以。

- - -
THE END.
