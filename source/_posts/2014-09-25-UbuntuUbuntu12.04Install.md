---
date: 2014-09-25
title: Ubuntu12.04系统安装和Anroid系统源码编译环境配置
layout: default
category: ANDROID
tags: Ubuntu
---

## 准备工作： ##

* 1.备份当前系统的文件
* 2.导出xp虚拟机的虚拟介质，以便安装12.04之后直接恢复

- - -

## 一：USB启动盘制作 ##

- 1.拷贝服务器上的安装镜像ubuntu-12.04-desktop-amd64.iso到本地
- 2.插入Ｕ盘
- 3.Ubuntu系统中打开　系统－系统管理－启动盘制作器
- 4.选择第１步中拷贝的镜像，并选择第２步中插入的Ｕ盘
- 5.格式化掉U盘后，点击制作就可以了

<!--more-->
- - -

## 二：系统安装 ##

- 1.插入Ｕ盘开机，在开机界面按F12进入启动菜单，选择U盘启动
- 2.进入安装界面，如果选择自定义的话，可以参考如下磁盘分配

>- (1)./分区，50G,方便以后重装系统
>- (2).SWAP分区，物理内存的2倍就行。可以先分一个分区出来，在文件系统选项里面选择swap
>- (3).剩下的单独做一个分区，待系统安装完成后，格式化并配置自动挂载


- 3.安装完成后，拔掉U盘重启

- - -

## 三：环境配置 ##

 (1)激活root账户

- A.在终端中，执行　`sudo passwd root`
- B.根据提示，输入密码，以及root账户的密码，可以一样，也可以不同
- C.(12.04需要此步骤开启root账户登录)

    sudo gedit /etc/lightdm/lightdm.conf

*在文件中添加下面这两行*

    greeter-show-manual-login=true
    allow-guest=false

*另外还要检查系统设置中的自动登录，关闭自动登录选项*


 (2)点击屏幕右上角的电源图标，选择注销，并以root帐户登录

 (3)JDK

- 拷贝jdk-6u32-linux-x64.bin到/usr/lib/jvm分区中
- 赋予可执行权限　`chmod u+x jdk-6u32-linux-x64.bin`
- 在终端中执行　`./jdk-6u32-linux-x64.bin`
- 将JDK相关的命令配置到环境变量中

  A.打开/etc/environment,
    gedit /etc/environment

  B.在Path末尾添加jdk的路径
  >PATH="/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin:/usr/games:/usr/lib/jvm/jdk1.6.0_32/bin"

  C.保存并退出

  D.在终端执行
      source /etc/environment

  E.然后就是要告诉系统，我们使用的sun的JDK，而非OpenJDK了：

      $ sudo update-alternatives --install /usr/bin/java java /usr/lib/jvm/jdk1.6.0_32/bin/java 300
      $ sudo update-alternatives --install /usr/bin/javac javac /usr/lib/jvm/jdk1.6.0_32/bin/javac 300
      $ sudo update-alternatives --config java


  E.敲入`java -version`看是否有相应输出

 (4)交叉编译器

- 拷贝arm-eabi-4.4.3到/opt分区中
- 给该文件夹中的相关文件赋权限

		chmod u+x -R arm-eabi-4.4.3/

- 将arm 相关的命令配置到环境变量中

     A.gedit /etc/environment

     B.Path末尾添加arm-eabi的路径
    >PATH="/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin:/usr/games:/usr/lib/jvm/jdk1.6.0_32/bin:/opt/arm-eabi-4.4.3/bin:/opt/arm-eabi-4.4.3/arm-eabi/bin"

     C.保存并退出

     D.在终端执行　
    source /etc/environment

     E.这一步需要重启，可以稍后安装完成之后再重启

 (5).gawk的安装(82以后的需要)

- A.拷贝gawk-4.1.0.tar.7z到/opt目录
- B.chmod u+x -R gawk-4.1.0/
- C.右键选择后，解压到此处
- D.进入解压后的目录，依次执行如下命令

```
    ./configure
    make
    make install
```

(6)虚拟机安装及配置

- A.拷贝VirtualBox-4.3.8-92456-Linux_amd64.run到/opt目录
- B.chmod u+x VirtualBox-4.3.8-92456-Linux_amd64.run
- C. ./VirtualBox-4.3.8-92456-Linux_amd64.run
- D.打开虚拟机软件

```
1.选择“新建”,根据提示输入该虚拟机的名字，内存大小，XP给512M就可以了，
2.选择硬盘时，选择已存在的硬盘，即已经拷贝过来的XP_NEW.vdi
3.之后就可以直接开机了
```

- E.Notes已经安装好，参照桌面中的文档完成配置即可

(7)编译相关

  **以下过程需要联网**

        apt-get install build-essential libc6-dev-i386 lib32ncurses5-dev ia32-libs x11proto-core-dev libx11-dev  lib32z-dev lib32readline-gplv2-dev gnupg tofrodos flex bison gperf  zip curl zlib1g-dev git-core gcc-4.4 g++-4.4 g++-4.4-multilib wine mingw32 libxml2-utils


(8)切换gcc版本

    ls /usr/bin/gcc*
    /usr/bin/gcc   /usr/bin/gcc-4.6    /usr/bin/gcc-4.4

**增加gcc4.6和gcc4.4的可选项**

    sudo update-alternatives --install /usr/bin/gcc gcc /usr/bin/gcc-4.6 46
    sudo update-alternatives --install /usr/bin/gcc gcc /usr/bin/gcc-4.4 44

如果没有第一句的话，也能行。但是系统默认的4.6版本的GCC就不能使用了。为了以后能用得着，还是加上第一句。

**切换版本到gcc-4.4**

     $ update-alternatives   --config   gcc
     There are 2 choices for the alternative gcc (providing /usr/bin/gcc).
      选择          路径               优先级      状态
      ------------------------------------------------------------
    * 0            /usr/bin/gcc-4.6   46        自动模式
      1            /usr/bin/gcc-4.4   44        手动模式
*输入1即可*

- - -
## 其他问题 ##
(1)修复root用户音量问题

    gedit /etc/default/pulseaudio

修改：
>PULSEAUDIO_SYSTEM_START=1

>DISALLOW_MODULE_LOADING=0

将root加到pulse和pulse-access组：

    sudo usermod -a -G pulse-access root
    gpasswd -a root pulse
    gpasswd -a root pulse-access

(2)编译如果报这个错的话：`/bin/bash: xmllint: 未找到命令`,终端输入：

    sudo apt-get install libxml2-utils

(3)访问nbtemp

打开一个文件夹并全屏，顶部栏->转到->位置
在文件位置处输入:
>smb://192.168.50.150/nbbsw_temp/

依次三个选项为：93218-->NBBD--> home

 (4)git账户配置

     git config --global user.name whisper
     git config --global user.email whisper@nbbsw.com
     git config --list

 (5)取消gedit创建备份文件

     菜单-编辑-首选项-编辑器：在保存前创建备份文件前面的勾去掉

 (6)修改文件的默认打开方式：

     gedit /etc/gnome/defaults.list
     gedit ~/.local/share/applications/mimeapps.list
     在这两个文件中修改默认打开方式

 (7)解决Ubuntu默认播放器无法播放视频的问题：

  下载这个文件[gstreamer0.10-ffmpeg_0.10.13-5_amd64.deb](/raw/attach/1410/31_gstreamer0.10-ffmpeg_0.10.13-5_amd64.deb)并手动安装即可。

 (8)Ubuntu Chrome不能以根用户启动

  在`/usr/share/applications/checkbox-qt.desktop`的快捷图标上右键，点击属性，修改命令属性，在后面加上 `-user-data-dir` 就OK了。

 (9)解决vim无法安装问题

  在/etc/apt/sources.list中添加`deb http://cz.archive.ubuntu.com/ubuntu lucid main`
  ```
  $ sudo apt-get update
  $ sudo apt-get install vim
  ```
- - -
THE END.
