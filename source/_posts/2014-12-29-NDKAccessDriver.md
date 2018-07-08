---
layout: default
date: 2014-12-29
category: ANDROID
title: Android应用访问硬件驱动程序
---

### 在应用层访问驱动程序有两种实现架构：

- (1).直接在使用JNI访问底层驱动，然后在Java代码中调用JNI的方法来实现最终访问驱动的目的。
- (2).通过硬件抽象层来访问硬件，然后在Framework层实现访问硬件抽象层的JNI方法。

不管那种方法，都要添加新的系统级硬件访问服务。

<!--more-->

#### 1. 对于第一种架构，先来看一个简单的例子：JNI访问重力感应驱动`libgsensor_jni.cpp` ####

    #define LOG_TAG "libproximity_jni"

    #include <stdio.h>
    #include <fcntl.h>
    #include <errno.h>
    #include <sys/ioctl.h>
    #include <utils/misc.h>
    #include <utils/Log.h>

    #include "jni.h"
    #include "JNIHelp.h"
    #include <linux/ioctl.h>

    #define GSENSOR 0x85
    #define GSENSOR_IOCTL_SET_CALIBARION _IO(GSENSOR, 0x0A)

    #ifdef __cplusplus
        extern "C" {
    #endif

    static jboolean gcalibrateSensor(JNIEnv *env, jobject clazz) {
        ALOGI("[Proximity] gcalibrateSensor\n");
          	int fd = open("/dev/gsensor", O_RDWR, 0);
     	  ALOGE("[Proximity] callgcalibrateSensor, fd:%d\n", fd);
        if (fd >= 0) {
            if (ioctl(fd, GSENSOR_IOCTL_SET_CALIBARION) == -1) {
                ALOGE("[Proximity] gcalibrateSensor 1111failed\n");
                return JNI_FALSE;
            }
            close(fd);
        } else {
            ALOGE("[Proximity] gcalibrateSensor 1111 open als_ps failed\n");
            return JNI_FALSE;
        }
        return JNI_TRUE;
    }

    // --------------------------------------------------------------------------

    static JNINativeMethod gsensorNotify[] = {
        { "gcalibrateSensor", "()Z", (void*)gcalibrateSensor },
    };

    jint JNI_OnLoad(JavaVM* vm, void* reserved) {
        JNIEnv* env = NULL;
        jint result = -1;
        if (vm->GetEnv((void**) &env, JNI_VERSION_1_4) != JNI_OK) {
            ALOGE("GetEnv failed!");
            return result;
        }

        int ret = jniRegisterNativeMethods(
            env, "com/bird/gsensor/GSensorNative", gsensorNotify, NELEM(gsensorNotify));

        if (ret) {
            ALOGE("[Proximity] call jniRegisterNativeMethods() failed, ret:%d\n", ret);
        }

        return JNI_VERSION_1_4;
    }

    #ifdef __cplusplus
        }
    #endif



#### 2. 由于上面这个.cpp文件会在编译的时候生成一个动态链接库，接着定义一个Java类来加载这个动态库 ####


    public class GSensorNative {
    	static {
    	System.loadLibrary("gsensorjni");
    	}
    	public native boolean gcalibrateSensor();
    }


#### 3. 最后如果要访问重力感应硬件，只要调用实例化一个GSensorNative对象，再调用它的gcalibrateSensor()即可 ####

    mGCali.gcalibrateSensor();

- - -

### 如果只是对于单纯某个应用要访问硬件，我们只要采用第一种架构。但是现实开发中，通常是要将访问硬件的服务添加到系统服务中去，以便系统里所有应用都可以通过调用系统服务的方式来访问硬件。
由于硬件抽象层并不在本文的涉及范围之内，我们这里只讨论如何使用JNI访问硬件抽象层模块的接口。

#### 0. /framework/base/下面新增一个包的话，要在build/core/pathmap.mk下面去注册,在FRAMEWORKS_BASE_SUBDIRS后添加新增的目录名

#### 1. 定义硬件访问服务/frameworks/base/core/java/android/os/IFingerprintService.aidl ####

    package android.os;
    interface IFingerprintService {
        boolean recordNewFingerprint();
        boolean compareFingerprints();
    }

- 注意：要在/framework/base/Android.mk中添加，修改LOCAL_SRC_FILES变量的值：

```
LOCAL_SRC_FILES += \
...
core/java/android/os/IFingerprintService.aidl
...
```

接口中有两个方法，一个是用于录取指纹，另一个用于比对指纹。

#### 2. 实现硬件访问服务/framewoks/base/services/java/com/android/server/FingerprintService.java ####

    package com.android.server;

    import android.os.IFingerprintService;
    import android.util.Log;

    public class FingerprintService extends IFingerprintService.Stub {
        private static final String TAG = "FingerprintService";
        private int mPtr = 0;
        FingerprintService(){
            mPtr = init_native();
            if (mPtr == 0) {
                Log.e("TAG","init_fail");
            } else {
              Log.e("TAG","init_success");
            }
        }

        public boolean recordNewFingerprint(){
            if (mPtr == 0) {
                return false;
            }
            return recordNewFingerprint_native();
        }

        public boolean compareFingerprints(){
            if (mPtr == 0) {
                return false;
            }
            return compareFingerprints_native();
        }

        private static native int init_native();
        private static native boolean recordNewFingerprint_native();
        private static native boolean compareFingerprints_native();
    }


#### 3. 实现硬件访问服务的JNI方法/framewoks/base/services/jni/com_android_server_FingerprintService.cpp ###

    #define LOG_TAG "FingerprintServiceJNI"

    #include "jni.h"
    #include "JNIHelp.h"
    #include "android_runtime/AndroidRuntime.h"

    #include <utils/misc.h>
    #include <utils/Log.h>
    #include <hardware/hardware.h>
    #include <stdio.h>

    namespace android
    {
        static jboolean fingerprint_recordNewFingerprint(JNIEnv* env,jobject clazz){
            boolean result = false;
            /*录取指纹
             *......
             *result = recordNewFingerprint
             *......
             */
            return result;
        }

        static jboolean fingerprint_compareFingerprints(JNIEnv* env,jobject clazz){
            boolean result = false;
            /*比对指纹
             *......
             *result = compareFingerprints
             *......
             */
            return result;
        }

        static jint fingerprint_init(JNIEnv* env,jobject clazz){
            /*进行一些初始化工作
             *......
             *if(open_success) return (jint) device;
             *else return 0;
             *......
             */
            return 0;
        }

        static const JNINativeMethod method_table[] = {
            {"init_native","()I",(void*)fingerprint_init},
            {"recordNewFingerprint_native","()Z",(void*)fingerprint_recordNewFingerprint},
            {"compareFingerprints_native","()Z",(void*)fingerprint_compareFingerprints}
        };

        int register_android_server_FingerprintService (JNIEnv* env) {
            return jniRegisterNativeMethods(env,"com/android/server/FingerprintService",method_table,NELEM(method_table));
        }
    };


#### 4. 接着还要在/framewoks/base/services/jni/onload.cpp中增加register_android_server_FingerprintService函数的声明和调用，这样系统加载libandroid_servers模块时，就会调用和实现在onload.cpp文件中的JNI_OnLoad函数。这样，就可以将前面定义的三那个JNI方法注册到Java虚拟机中。 ####

    namespace android {
    ......
    int register_android_server_FingerprintService(JNIEnv* env);
    ......
    };

    using namespace android;

    extern "C" jint JNI_OnLoad(JavaVM* vm, void* reserved)
    {
        JNIEnv* env = NULL;
        jint result = -1;

        if (vm->GetEnv((void**) &env, JNI_VERSION_1_4) != JNI_OK) {
            ALOGE("GetEnv failed!");
            return result;
        }
        ALOG_ASSERT(env, "Could not retrieve the env!");
    ......
    register_android_server_FingerprintService(env);
    ......
        return JNI_VERSION_1_4;
    }


#### 5. 在系统中启用硬件访问服务，在/framewoks/base/services/java/com/android/server/SystemServer.java的initAndLoop方法中添加

    public void initAndLoop() {
        ......
        if (factoryTest != SystemServer.FACTORY_TEST_LOW_LEVEL) {
            //if (!disableNonCoreServices) { // TODO: View depends on these; mock them?
            if (true) {
                try {
                    Slog.i(TAG, "FingerprintService");
                    ServiceManager.addService("FingerprintService", new FingerprintService());
                } catch (Throwable e) {
                    reportWtf("starting FingerprintService", e);
                }

            }
        }
        ......
    }


#### 6. 系统层的服务已经添加完毕，接着先make update-api，然后要重新编译整个源码。

最终基于源码开发的应用调用这个服务。

    IFingerprintService mIFingerprintService = IFingerprintService.Stub.asInterface(ServiceManager.getService("FingerprintService"));
    mIFingerprintService.recordNewFingerprint()


- - -
THE END.
