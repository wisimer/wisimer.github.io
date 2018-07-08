---
title: Android Makefile Syntax
layout: default
date: 2014-12-30
category: ANDROID
---

You can build JNI Application by NDK.

In addition,if you have android source code environment,you can write a Android Makefile to build .so file and your application automatically.

<!--more-->

- - -

### Here is an example of Andoird.mk in HelloJni:

```java
LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)
LOCAL_STATIC_JAVA_LIBRARIES := android-support-v4
LOCAL_MODULE_TAGS := optional
LOCAL_SRC_FILES := $(call all-java-files-under, src)
LOCAL_JNI_SHARED_LIBRARIES := libhello
LOCAL_PACKAGE_NAME := HelloJni
include $(BUILD_PACKAGE)
```


#### **1.LOCAL_PATH:= $(call my-dir)**

- An Android makefile must begin with the definition of the LOCAL_PATH variable.
- It is used to locate source files in the development trees.
- The function `my-dir` ,provided by the build system,is used to return the path of the current directory.

#### **2.include $(CLEAR_VARS)**

The CLEAR_VARS vairable is provided by build system and points to a special GNU      Makefile thar will clear many LOCAL_XXX variable for you.

#### 3.LOCAL_STATIC_JAVA_LIBRARIES := android-support-v4

Use a static java library called `android-support-v4`

#### **4.LOCAL_MODULE_TAGS ：=optional**

- optional: this module will be compiled in all version
- user: this module will be compiled only in user version
- eng: only in eng version
- tests: only in tests version

#### **5.LOCAL_JNI_SHARED_LIBRARIES := libhellocas**

Use a shared library called `libhello`

#### **6.LOCAL_PACKAGE_NAME := HelloJni**

Name of the APK to build

#### **7.include $(BUILD_PACKAGE)**

Tell it to build an APK

Finally,this package will generate `HelloJni.apk`

- - -

#### Here is an example of Android.mk in jni folder:

```java
LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE := libhello
LOCAL_SRC_FILES := hello.c
include $(BUILD_SHARED_LIBRARY)
```

#### **1.LOCAL_MODULE := libhello**

- The LOCAL_MODULE variable must be defined to idenfy each module you describe in the Android.mk.
- The name must be unique and not contain any space.
- It determins the name of file which will genegrate.

#### **2.include $(BUILD_SHARED_LIBRARY)**

Tell it to generate a shared library

#### **3.LOCAL_SRC_FILES := hello.c**

The LOCAL_SRC_FILES variable must contain a list file of c and/or c++ source files that will be build and assembled into the module

Finally,this module will generate `libhello.so`

- - -

### If you want to use a third jar package,how to add compile command into Android.mk:

For example,there is a `nineoldandroids-2.4.0.jar` in the /libs,then we can compile it into the application by the Android.mk as follow.

```java
LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE_TAGS := optional
\#LOCAL_SDK_VERSION := current
LOCAL_SRC_FILES := $(call all-java-files-under,src)
LOCAL_PACKAGE_NAME := Todos
LOCAL_CERTIFICATE := platform
\#`nineo` is a tag we define below
LOCAL_STATIC_JAVA_LIBRARIES := nineo
include $(BUILD_PACKAGE)

include $(CLEAR_VARS)
\#`libs` is the position of jar package
LOCAL_PREBUILT_STATIC_JAVA_LIBRARIES := nineo:libs/nineoldandroids-2.4.0.jar
include $(BUILD_MULTI_PREBUILT)

\# Use the following include to make our test apk.
include $(call all-makefiles-under,$(LOCAL_PATH))
```

- - -
THE END.
