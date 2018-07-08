---
layout: default
date: 2014-12-27
title: NDK开发起步
category: ANDROID
---

### Eclipse进行NDK开发HelloWorld步骤（无参数，返回String型）：

1.新建一个新的工程

2.在工程目录下新建一个`jni`文件夹

3.在`jni`目录下新建一个.c文件，例如我这里新建一个`hello.c`文件,以及一个.mk文件，内容如下

```java
LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE    := libhello
LOCAL_SRC_FILES := hello.c
include $(BUILD_SHARED_LIBRARY)
```

<!--more-->
4.在java代码中加载hello.c,我这里是在MainActivity中加入

```java
static {
  //参数就是刚刚新建的.c文件名
  System.loadLibrary("hello");
}
```

5.新建一个用于获取jni内容的方法,注意native关键字，可以通过这个方法在java文件中获取到jni提供的内容

```java
public native String getJniContent();
```

6.编写hello.c文件

```java
#include <string.h>
#include <jni.h>

jstring
Java_com_wxp_testjni_TestJni_getJniContent(JNIEnv* env, jobject thiz) {
	return (*env)->NewStringUTF(env, "Hello NDK !");
}
```

- 注意这里的jstring是返回String类型

- 方法名的命名规则是Java_packagename_classname_methodnameinjava

也就是Java_包名_使用到jni方法的类的名称_在java中定义的方法名，所有的.号都要用下划线代替。例如我这里是在MainActivity中使用到了getJniContent方法，那么.c文件中方法名应该是这样的：

>Java_com_wxp_testjni_MainActivity_getJniContent

7.在工程目录下执行命令`ndk-build`,提示生成了.so文件

![ndk-build.png](/src/imgs/1412/27_ndk-build.png)

再看一下工程目录下libs文件夹，确实生存了hello.so文件

![27_hello_so.png](/src/imgs/1412/27_hello_so.png)

8.最后看看用Eclipse运行之后打印出来的结果

![27_jni_result.png](/src/imgs/1412/27_jni_result.png)

- - -

### 上面是生成了一个动态库.so文件，接下来尝试一下生成静态库.a文件

- 首先在jni目录下新建一个wxpstatic.h文件，用以声明要实现的函数：

```java
extern int weclome();
```

- 接着新建一个wxpstatic.h文件，实现.h文件中声明的函数：

```java
#include "wxpstatic.h"
int weclome() {
	return 1024;
}
```

#### 对于我们自己编写的代码来说，如果要在JNI层的.c文件中调用wxpstatic.c中的函数，可以直接通过`#include "wxpstatic.h"`，然后编写Android.mk文件：

```java
LOCAL_PATH := $(call my-dir)  

include $(CLEAR_VARS)
LOCAL_MODULE    := libwxpstatic  
LOCAL_SRC_FILES := wxpstatic.c
include $(BUILD_STATIC_LIBRARY) #注意这里是 BUILD_STATIC_LIBRARY

include $(CLEAR_VARS)
LOCAL_MODULE    := wxpjni
LOCAL_SRC_FILES := wxpjni.c
LOCAL_STATIC_LIBRARIES := libwxpstatic  
include $(BUILD_SHARED_LIBRARY)
```

- 在jni目录下执行`ndk-build`

![08_generate_a.png](/src/imgs/1501/08_generate_a.png)

#### 如果是要使用第三方的.a文件，要将.a文件以及对应的.h文件拷贝到jni目录下。修改Android.mk文件：

```java
LOCAL_PATH := $(call my-dir)  

include $(CLEAR_VARS)
LOCAL_MODULE    := libwxpstatic
LOCAL_SRC_FILES := libwxpstatic.a  
include $(PREBUILT_STATIC_LIBRARY) #注意这里是 PREBUILT_STATIC_LIBRARY

include $(CLEAR_VARS)
LOCAL_MODULE    := wxpjni
LOCAL_SRC_FILES := wxpjni.c
LOCAL_STATIC_LIBRARIES := libwxpstatic  
include $(BUILD_SHARED_LIBRARY)
```

- 在jni目录下执行`ndk-build`

![08_use_a.png](/src/imgs/1501/08_use_a.png)

- 然后就可以在JNI层的代码中调用.a中的函数了。

- - -

## JNI中一些常用方法随笔

### 带int参数，返回int型

.java中的方法定义：

```java
public native int getSum(int a,int b);
```

传入参数给.c中的方法：

```java
jint
Java_com_wxp_testjni_TestJni_getSum(JNIEnv* env, jobject thiz,jint a,jint b) {
  return a+b;//返回两个参数的和
}
```

- - -

### 带int[]参数，返回int型

.java中的方法定义：

```java
public native int getSum(int[] a);
```

传入参数给.c中的方法：

```java
jint
Java_com_wxp_testjni_TestJni_getIntArraySum(JNIEnv* env, jobject thiz,jintArray a) {

  //获取参数中的int数组
  jint *b = (*env)->GetIntArrayElements(env, a, 0);
  //获取int数组的长度
  jsize a_len = (*env)->GetArrayLength(env, a);
  return a_len;
}
```
- - -

### String数组操作：

```java
  //获取String数组
  jobjectArray *b = (*env)->GetObjectArrayElement(env, a, 0);
  //获取String数组长度
  jsize a_len = (*env)->GetArrayLength(env, a);
```

- - -

### 注意使用for循环时：

- 如果写成这样：
```java
for(int i=0;i<10;i++){
  //......
}
```

会下面这个报错：

> error: 'for' loop initial declarations are only allowed in C99 mode

- 而要写成这种格式则不会：
```java
int i = 0;
for(i=0;i<10;i++){
  //......
}
```

- - -
THE END.
