---
layout: default
date: 2014-11-26
category: QA
title: 翻墙教程[失效]
---

#### Windows 下翻墙 ####

下载翻墙软件：[自由门](/raw/attach/1411/fq_windows.exe)

如图所示，一键翻墙：

![26_fq_windows.png](/src/imgs/1411/26_fq_windows.png)

<!--more-->

#### Android 下翻墙 ####

下载翻墙软件：[fqrouter2](/raw/attach/1411/fq_android.apk])

如图所示，一键翻墙：

![26_fq_android.png](/src/imgs/1411/26_fq_android.png)

#### Ubuntu12.04 下翻墙 ####

参考文章：[https://github.com/goagent/goagent/blob/wiki/InstallGuide.md](https://github.com/goagent/goagent/blob/wiki/InstallGuide.md)

Step 1.按照上面的步骤申请 Google App Engine 并创建appid（Application Identifier）等操作。

Step 2.下载[goagent](https://github.com/goagent/goagent),解压到/opt中

step 3.运行gogent客户端,执行命令:

```
$ cd /opt/goagent/local
$ sudo apt-get insatll python-vte
$ sudo ./goagent-gtk.py
```

step 4.上传程序

```
$ python uploader.py
```

按提示输入appid,gamil以及passwd。可能会出现错误，再重新上传。

打开/goagent/local/中的proxy.ini文件，将其中的appid修改为自己申请的appid

step 5.配置浏览器

地址栏输入 Chrome://extensions/ 后按回车，打开扩展管理页，直接将 local 文件夹中的 SwitchySharp.crx 拖拽到该页面之后点击确定即可安装。接着导入SwitchySharp的配置文件SwitchyOptions.bak

在浏览器的Menu键旁选择GoAgent模式

step 6.解决SSL错误

下载 [CA证书](/raw/attach/1411/CA.crt)，并保存到/goagent/local/目录下。

在Chrome设置中找到管理证书选项:

![26_Setting_CA](/src/imgs/1411/26_Setting_CA.png)

导入刚刚下载的证书：

![26_Import_CA](/src/imgs/1411/26_Import_CA.png)

- - -
THE END.
