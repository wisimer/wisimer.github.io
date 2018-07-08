---
date: 2014-04-21
title: hp pavilion 15 notebook笔记本 解决RTL8188EE无线网卡问题
category: FAQ
layout: default
tags: Ubuntu
---

笔记本装了Ubuntu12.04之后发现无线网用不了，折腾了好久才搞定 --！
解决方法：

(1):下载驱动文件[linux_mac80211_0012.0207.2013.tar.bz2](http://67.216.200.63:10241/share/7yQUTMdV)（大小为12799K).

(2):右击压缩包->提取到此处.解压后的目录名称为:`rtl_92ce_92se_92de_8723ae_88ee_linux_mac80211_001 2.0207.2013` .将该目录复制到root目录下.

(3):打开终端，输入下面的命令:

    cd ~/rtl_92ce_92se_92de_8723ae_88ee_linux_mac80211_0012.0207.2013

<!--more-->

进入该目录，后键入：

    make

可能会出现如下错误:

```java
/home/wxp/rtl_92ce_92se_92de_8723ae_88ee_linux_mac80211_0012.0207.2013/pci.h:247:15: 错误： expected ‘=’, ‘,’, ‘;’, ‘asm’ or ‘__attribute__’ before ‘rtl_pci_probe’
make[2]: *** [/home/wxp/rtl_92ce_92se_92de_8723ae_88ee_linux_mac80211_0012.0207.2013/base.o] 错误 1
make[1]: *** [_module_/home/wxp/rtl_92ce_92se_92de_8723ae_88ee_linux_mac80211_0012.0207.2013] 错误 2
make[1]:正在离开目录 `/usr/src/linux-headers-3.8.0-35-generic'
make: *** [all] 错误 2
```

解决方法:
在rtl_92ce_92se_92de_8723ae_88ee_linux_mac80211_0012.0207.2013目录下的pci.h的文件内容中

```java
#ifndef __RTL_PCI_H__
#define __RTL_PCI_H__
```

的后面加上：

```java
#ifndef __devinit
#define __devinit
#define __devinitdata
#endif
```

重新执行:

    make
    sudo make install
    sudo modprobe -v rtl8188ee

> 参考资料:[http://ubuntuforums.org/showthread.php?t=2162026](http://ubuntuforums.org/showthread.php?t=2162026)

- - -
THE END.
