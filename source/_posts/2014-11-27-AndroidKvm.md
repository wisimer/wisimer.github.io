---
layout: default
date: 2014-11-27
category: ANDROID
title: Android5.0神速模拟器配置
---

### 前言 ###
创建模拟器时，如果选择 ARM 类型，模拟器的运行时就会极其卡顿。如果选择 x86 类型，启动时会提示缺少 KVM。KVM是个很炫酷的东西，不过有坑。

#### 1.准备工作：检查CPU是否支持 KVM ####

```bash
$ egrep -o '(vmx|svm)' /proc/cpuinfo
```

输出下面的信息说明该CPU支持安装KVM：

```bash
vmx
```

<!--more-->

#### 2.安装软件：####

```bash
$ sudo apt-get install qemu-kvm libvirt-bin virt-manager bridge-utils
```

#### 3.检查是否安装成功： ####

```bash
$ kvm-ok
```

输出如下结果，表示安装成功：

```bash
INFO: /dev/kvm exists
KVM acceleration can be used
```

如果提示信息为：

```bash
INFO: KVM (vmx) is disabled by your BIOS(KVM [vmx]被你的BIOS禁用)
HINT: Enter your BIOS setup and enable Virtualization Technology (VT)
```

则需要进入的BIOS设置界面，启用虚拟化技术[VT]，设置步骤为：


#### 4. 进入BIOS后，选择ADVANCED，然后至 PROCESSOR CONFIGURATION进去找到 ####
>INTEL (R) VIRTUALIZATION  TECHNOLOGY ，设置成ENABLE，保存退出 。

验证KVM内核是否加载成功：

```bash
lsmod | grep kvm
```

输出为：

```bash
kvm_intel             137899  3
kvm                   455932  1 kvm_intel
```

#### 5.设置模拟器 ####

如图：

![27_avd_comfig.png](/src/imgs/1411/27_avd_comfig.png)

#### 6.到这里貌似都结束了，不过开头说了，有坑。看图说话####

![27_genymotion_error.png](/src/imgs/1411/27_genymotion_error.png)

神器genymotion扑街！并且VM也不能使用了。

那么问题来了，如何解决这个冲突呢。

>那就是如果要使用VM或者genymotion，就先关闭Android模拟器。。。

- - -
THE END.
