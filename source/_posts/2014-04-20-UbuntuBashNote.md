---
date: 2014-04-12
layout: default
title: bash note
category: QA
tags: Ubuntu
---

#### bash命令

+ 命令与文件补全功能： ([tab] 按键的好处)

使用 [tab] 按键的时机依据 [tab] 接在命令后或参数后而有所不同。
>[Tab] 接在一串命令的第一个字的后面，则为命令补全；
>[Tab] 接在一串命令的第二个字以后时，则为『文件补齐』！

所以说，如果我想要知道我的环境中，所有可以运行的命令有几个？ 就直接在 bash 的提示字符后面连续按两次 [tab] 按键就能够显示所有的可运行命令了。
那如果想要知道系统当中所有以 c 为开头的命令呢？就按下 `c[tab][tab]`就好啦！

<!--more-->

#### vim

```java
a 在光标后面插入；
i 直接在当前位置插入；
x 删除单个字符；
gg:跳转到文件首行
G:跳转到文件尾行
{:跳转到上一个空行处
}:跳转到下一个空行处
shift+o:在光标的上面插入新的一行
shift+O:在光标到下面插入新的一行
0:跳转到本行开头
$:跳转到本行结尾
yy:复制本行
p:在下行粘贴内容
P:在上面一行粘贴内容
```

#### bash脚本
+ 一个命令的运行成功与否，可以使用 $? 这个变量来观察～ 那么我们也可以利用 exit 这个命令来让程序中断，并且回传一个数值给系统。

```java
#!/bin/bash
#Program
echo -e "helloworld\a\n"
exit 0
```

上例使用 exit 0 ，这代表离开 script 并且回传一个 0 给系统， 所以我运行完这个 script 后，若接著下达 echo $? 则可得到 0 的值！ 利用这个 exit n (n 是数字) 的功能，我们还可以自订错误信息， 让这支程序变得更加的 smart 呢！

+ Ubuntu开机自动挂在分区

0.查看分区：

```java
sudo fdisk -l
vim mount_one.sh
```

1.新建一个mount_one.sh脚本文件，内容为：

> mount /dev/sdb1 /media/one

2.在rc.local文件中添加mount_one.sh的路径

```java
vim /etc/rc.local
```

> 在`exit 0`上面添加 `/root/mount_one.sh`

- - -
THE END.
