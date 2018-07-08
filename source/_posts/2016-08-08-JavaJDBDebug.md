---
date: 2016-08-08
title: 直接在终端对Java代码进行Debug
category: JAVA
layout: default
---

有时候写个小算法或者Demo时不想用IDE，直接打开Atom就可以写了。但是如果遇到bug，就只能在命令行终端调试了。下面看看如何在终端使用jdb调试Java代码。

<!--more-->

### 1. 编译准备

使用 `-g` 参数编译Java代码：

```
javac -g Test.java
```

### 2. jdb初始化

执行命令：

```
jdb Test
```

此时会显示：

>Sort wxp$ jdb Test
正在初始化jdb...


### 3. 设置断点

```
stop at Quick:22
```

表示在Quick类的22行设置一个断点。此时终端会提示：

>stop at Quick:22
正在延迟断点Quick:22。
将在加载类后设置。

接着就可以执行程序了，在终端输入：

```
run
```

### 4. 单步运行

通过 `next` 命令可以让程序执行到下一行，如果想查看变量的值可以调用 `print` 命令。如果要显示当前堆栈帧中的所有本地变量可以使用 `locals` 。

### 5. 跳转断点

如果要查看已经设置的所有断点，可以使用 `clear` 命令。
要跳转到下一个断点可以执行 `cont` 命令，如果下面没有断点了则直接执行到程序结束。此外 `step` 可以跳转进方法内部继续执行。

- - -
THE END.
