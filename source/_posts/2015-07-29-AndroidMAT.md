---
date: 2015-07-29
layout: default
category: ANDROID
title: 使用MAT工具定位Android应用内存泄漏
---

> MAT工具下载地址：[https://www.eclipse.org/mat/downloads.php](https://www.eclipse.org/mat/downloads.php)

Android发生内存泄漏最普遍的一种情况就是长期持有对Context（特别是Activity）的引用，导致Context无法被销毁。
这也就意味着该Context中的所有成员变量都没有办法销毁。主要有如下两种情况会是得Activity无法被销毁：

* 1.某个`static变量`保持对Activity的引用

* 2.线程保持对Activity的引用

<!--more-->

> 使用MAT分析内存查找内存泄漏的根本思路，就是找到哪个类的对象的引用没有被释放，找到没有被释放的原因，也就可以很容易定位代码中的哪些片段的逻辑有问题了

#### 1.先来写一段会导致内存泄漏的代码，测试代码如下

```java
public class HeapTestActivity extends Activity implements View.OnClickListener{
   @Override
   protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.testuploadfile);
       findViewById(R.id.testupload).setOnClickListener(this);
   }

   public void onClick(View v){
       new MyThread().start();
   }

   class MyThread extends Thread{
       public void run(){
           try {
               Log.e("wxp", "wxp-HeapTest-run");
               Thread.sleep(60000);
           } catch (InterruptedException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
           }
       }
   }
}
```

程序截图：

![0729_heaptest_app.png](/src/imgs/1507/0729_heaptest_app.png)

点击testupload会启动一个新的线程，所以此时如果旋转屏幕的话，并没有做任何销毁操作，由于MyThread线程未被销毁，所以会一直持有原先的Activity。
多次旋转屏幕并点击testupload就会导致内存泄漏。

下面来具体操作一下如何定位内存泄漏

#### 2.打开DDMS

选中你要调适的应用，点击 `Update Heap` 按钮，再点击右边 `Heap` 栏下的 `Cause GC` 按钮，即可查看当前应用的堆情况

![0729_cause_gc.png](/src/imgs/1507/0729_cause_gc.png)

接着点击 `Update Threads` 按钮，再点击啊右边的 `Threads` 栏，即可看到当前应用的Thread运行情况

![0729_click_update_threads.png](/src/imgs/1507/0729_click_update_threads.png)

#### 3.导出hprof文件

接下来就要对应用进行一系列操作了，点击按钮，旋转屏幕，点击按钮，旋转屏幕。。。重复操作多次之后。
点击 `Dump HPROF file` 按钮，另存为 myheap.sample.hprof

![0729_dump_hprof.png](/src/imgs/1507/0729_dump_hprof.png)

由于我使用的是独立的MAT工具，所以这里要转换一下文件，才能被MAT读取。
Android已经自带了转换的工具，前提是配置了环境变量，在终端执行如下命令：

```
hprof-conv ~/Desktop/HEAP/myheap.sample.hprof ~/Desktop/HEAP/mymemoryleak.hprof
```

#### 4.分析mymemoryleak.hprof文件，定位内存泄漏

(1) 在MAT工具中打开刚刚转化之后的文件 `mymemoryleak.hprof`

![0729_actions_histogram.png](/src/imgs/1507/0729_actions_histogram.png)

(2) 点击 `Actions` 中的 `Histogram`,出现如下界面，这里就是按类名把所有的实例对象组织起来

在第一行的 `Class Name` 输入框中输入你要定位的类名。
我这里就是刚刚新建的 `HeapTestActivity`,直接输入 `Heap`,即可自动进行正则匹配，可以看到第一个类就是 HeapTestActivity：

![0729_classname_heap.png](/src/imgs/1507/0729_classname_heap.png)

(3) 右击HeapTestActivity，选择 `List objects->with incoming references`

![0729_with_incoming_refs.png](/src/imgs/1507/0729_with_incoming_refs.png)

(4) 接着就列出来该类的所有实例，展开某一个实例之后就可以显示对象间的引用关系。

如果想要快速找到某个实例没有被释放的原因，可以右击一个实例，选择 `Path To GC Roots->exclude all phantom/weak/soft etc. references`

![0729_path_to_gcroot.png](/src/imgs/1507/0729_path_to_gcroot.png)

(5) 沿着这个路线就可以找到仍旧引用着这个实例的对象，比如我们这里是一个Thread,也就是上面的一个MyThread对象

![0729_gc_root.png](/src/imgs/1507/0729_gc_root.png)

> 用这个方法可以快速找到某个对象的 GC Root,一个存在 GC Root的对象是不会被 GC回收掉的.

- - -

附：

Square推出的一款内存泄漏检测工具 `LeakCanary` 也很方便的就可以定位到内存泄漏

1.首先在build.gradle中添加依赖

```
dependencies {
   ......
   debugCompile 'com.squareup.leakcanary:leakcanary-android:1.3'
   releaseCompile 'com.squareup.leakcanary:leakcanary-android-no-op:1.3'
   ......
 }
```

2.在Application中注册一下就可以了

```
public class LeakCanaryApplication extends Application {
@Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
    }
}
```

3.编译安装应用之后，随着应用会在桌面出现另一个图标

![0729_leaks.png](/src/imgs/1507/0729_leaks.png)

4.当前应用发生内存泄漏之后，Leaks会在通知栏提醒，点击查看详情就会看到一步一步的引用流程

![0729_leakcanary_detal.png](/src/imgs/1507/0729_leakcanary_detal.png)

- - -
THE END.
