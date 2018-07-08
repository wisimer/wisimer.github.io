---
title: Android ANR 分析的几个要点
layout: default
date: 2017-02-22
category: ANDROID
---

#### 一、找到trace.txt中的ANR

```
----- pid 23835 at 2017-02-21 20:11:48 -----
Cmd line: com.wisim.example

......

```

trace.txt中,上面这种形式就是一个ANR日志的开始标志了。一个trace.txt中会有好多ANR，有的可能在界面上弹出提示框，有的没有。

<!--more-->

#### 二、ANR类型

##### 1. CPU满负荷

```
...
CPU usage from 3330ms to 814ms ago:
6% 178/system_server: 3.5% user + 1.4% kernel / faults: 86 minor 20 major
4.6% 2976/com.anly.githubapp: 0.7% user + 3.7% kernel /faults: 52 minor 19 major
0.9% 252/com.android.systemui: 0.9% user + 0% kernel
...

100%TOTAL: 5.9% user + 4.1% kernel + 89% iowait
```

看到最后一行表明：
1. 当是CPU占用100%, 满负荷了.
2. 其中绝大数是被iowait即I/O操作占用了.

此时分析方法调用栈, 一般来说会发现是方法中有频繁的文件读写或是数据库读写操作放在主线程来做了.

##### 2. 内存原因

```
......
DALVIK THREADS:
"main"prio=5 tid=3 VMWAIT
|group="main" sCount=1 dsCount=0 s=N obj=0x40026240self=0xbda8
| sysTid=1815 nice=0 sched=0/0 cgrp=unknownhandle=-1344001376
atdalvik.system.VMRuntime.trackExternalAllocation(NativeMethod)
atandroid.graphics.Bitmap.nativeCreate(Native Method)
atandroid.graphics.Bitmap.createBitmap(Bitmap.java:468)
atandroid.view.View.buildDrawingCache(View.java:6324)
atandroid.view.View.getDrawingCache(View.java:6178)
......

MEMINFO in pid 1360 [android.process.acore] **

           native  dalvik  other  total
size:      17036   23111   N/A    40147
allocated: 16484   20675   N/A    37159
free:      296     2436    N/A    2732
```

free那行就是剩余的内存，可以看到free的内存已所剩无几.当然这种情况可能更多的是会产生OOM的异常.

查看内存信息也可以搜搜看如下代码
```
RAM: 2932124K total, 603160K free, 19100K buffers, 382832K cached, 3496K shmem, 77900K slab\
```

##### 3. 线程死锁

```
"main" prio=5 tid=1 Native
  | group="main" sCount=1 dsCount=0 obj=0x763ac390 self=0xf48a4500
  ......
  at com.tencent.android.tpush.common.p.a(ProGuard:96)
  at com.tencent.android.tpush.common.n.a(ProGuard:107)
  at com.tencent.android.tpush.service.channel.b.m(ProGuard:808)
  at com.tencent.android.tpush.service.channel.b.k(ProGuard:609)
  - locked <0x0e28719c> (a com.tencent.android.tpush.service.channel.b)
  at com.tencent.android.tpush.service.channel.b.f(ProGuard:75)
  at com.tencent.android.tpush.service.channel.j.onReceive(ProGuard:791)
  at 
  ......
```

```
"TpnsClient" prio=5 tid=32 Blocked
  | group="main" sCount=1 dsCount=0 obj=0x32ee3160 self=0xee818200
  | sysTid=5469 nice=0 cgrp=bg_non_interactive sched=0/0 handle=0xdd5fa930
  | state=S schedstat=( 0 0 0 ) utm=5 stm=1 core=1 HZ=100
  | stack=0xdd4f8000-0xdd4fa000 stackSize=1038KB
  | held mutexes=
  at com.tencent.android.tpush.service.channel.b.a(ProGuard:912)
  - waiting to lock <0x0e28719c> (a com.tencent.android.tpush.service.channel.b) held by thread 1
  at com.tencent.android.tpush.service.channel.a.a.b(ProGuard:121)
  at com.tencent.android.tpush.service.channel.a.a.run(ProGuard:161)
```

这里tid=1的线程locked <0x0e28719c>这个锁，而tid=32的线程又在waiting to lock <0x0e28719c>

还有循环等待的情况，也会导致死锁发生ANR。

##### 4. 其他原因

当然其他异常也会导致出现ANR。这个要在实践中积累。最主要的还是要耐心分析。


- - -
THE END.