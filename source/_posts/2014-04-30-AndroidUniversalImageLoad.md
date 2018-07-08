---
date: 2014-04-30
title: Android-Universal-Image-Loader异步加载图片框架
layout: default
category: ANDROID
---

简单介绍 Android-Universal-Image-Loader 这个框架的配置及使用

#### 项目下载地址：
[Android-Universal-Image-Loader](https://github.com/nostra13/Android-Universal-Image-Loader)

#### 导入universal-image-loader-1.9.1.jar到你的项目

#### 基本配置

<!--more-->

在`AndroidManifest.xml`文件中加入访问权限

```java
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

继承Application实现一个子类MyApplication,并且在AndroidManif.xml的Application标签中使用MyApplication

```java
public class MyApplication extends Application {
    @Override
    public void onCreate() {
      super.onCreate();
      // This configuration tuning is custom. You can tune every option, you may tune some of them,
      // or you can create default configuration by
      //  ImageLoaderConfiguration.createDefault(this);
      // method.
      ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
      .threadPriority(Thread.NORM_PRIORITY - 2)
      .denyCacheImageMultipleSizesInMemory()
      .discCacheFileNameGenerator(new Md5FileNameGenerator())
      .tasksProcessingOrder(QueueProcessingType.LIFO)
      .enableLogging() // Not necessary in common
      .build();
      //Initialize ImageLoader with configuration
      ImageLoader.getInstance().init(config);
    }
}
```

#### 使用:加载单个图片

    ImageLoader.getInstance().displayImage(imgUrl,headImageView);

- - -
THE END.
