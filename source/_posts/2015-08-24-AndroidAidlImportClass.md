---
date: 2015-08-24
layout: default
category: ANDROID
title: Android 使用aidl实现进程间通信-传递自定义的类
---

之前使用aidl传递的都是基本的数据类型比如int 、boolean之类的还有就是String类型的参数，并没有传递自己定义的class。
最近在开发的过程中重构代码时遇到了这个问题，网上也有文章提供了解决的办法，不过不太全面。我东拼西凑才把问题给解决了。这里记录一下。

<!--more-->

#### 一、直接在aidl中的方法参数传递一个自定义类参数。

看一下我们的aidl文件

```
// IJDMAService.aidl
package com.jingdong.jdlogsys;

import com.jingdong.jdlogsys.model.CommonParamInfo;

interface IJDFileLogService {

    void setCommonParamInfo(CommonParamInfo info);
    void changeUser(String uid,String uuid,String pin);

}

```

这里的setCommonParamInfo方法的参数就是一个我们自己定义的类CommonParamInfo，虽然上面import了这个类，但是编译项目时会报如下的错误信息（使用Android Studio）。

```
Error:Execution failed for task ':JDLogSys:compileReleaseAidl'.
> com.android.ide.common.process.ProcessException: org.gradle.process.internal.ExecException: Process 'command 'D:\Android\sdk\build-tools\19.1.0\aidl.exe'' finished with non-zero exit value 1
Error:(5) couldn't find import for class com.jingdong.jdlogsys.model.CommonParamInfo
F:\JD_GIT\JDLogSys\JDLogSys\res\com\jingdong\jdlogsys\IJDFileLogService.aidl
```


看到这么一行信息`Error:(5) couldn't find import for class com.jingdong.jdlogsys.model.CommonParamInfo`。意思就是找不到这个类。

#### 二、网上提供了一个解决这个问题的方法

首先新建一个CommonParamInfo.aidl文件，文件的内容如下：

```
package com.jingdong.jdlogsys.model;

parcelable CommonParamInfo;
```

要注意的是这里的package要和原本定义的类的包名一样，其次是下面的`parcelable CommonParamInfo;`这行代码。

当然最重要的一点是你自己定义的类要实现Parcelable接口并按照规范重写一系列方法。这里我就不做介绍了。

接着重新编译，是的不再报上面那个错误信息，但是报了另外一个错误：

```
:JDLogSys:compileReleaseAidl
F:\JD_GIT\JDLogSys\JDLogSys\res\com\jingdong\jdlogsys\IJDFileLogService.aidl:14 parameter 1: 'CommonParamInfo info' can be an out parameter, so you must declare it as in, out or inout.
Error:Execution failed for task ':JDLogSys:compileReleaseAidl'.
> com.android.ide.common.process.ProcessException: org.gradle.process.internal.ExecException: Process 'command 'D:\Android\sdk\build-tools\19.1.0\aidl.exe'' finished with non-zero exit value 1
```

看到这么一行信息`IJDFileLogService.aidl:14 parameter 1: 'CommonParamInfo info' can be an out parameter, so you must declare it as in, out or inout.
`

意思就是我们需要把这个参数声明成in或者inout类型的，

#### 三、于是我们修改一下aidl中的setCommonParamInfo方法，一开始我是设置的inout：

```
void setCommonParamInfo(inout CommonParamInfo info);
```

于是，不出意外接着报错：

```
Error:(144, 5) 错误: 找不到符号
符号:   方法 readFromParcel(Parcel)
位置: 类型为 CommonParamInfo 的变量 info
注: 有关详细信息, 请使用 -Xlint:deprecation 重新编译。
Error:Execution failed for task ':JDLogSys:compileReleaseJava'.
> Compilation failed; see the compiler error output for details.
1 个错误
注: 某些输入文件使用或覆盖了已过时的 API。
F:\JD_GIT\JDLogSys\JDLogSys\build\generated\source\aidl\release\com\jingdong\jdlogsys\IJDFileLogService.java
```

啥状况呢，就是说找不到readFromParcel方法。我看了一下CommonParamInfo类，确实没有这个方法，但是之前该重写的方法都重写了啊，之前一直也是这么做的。
于是我又接着搜索，找到了解决这个问题的方法，就是在CommonParamInfo类中自己添加一个readFromParcel方法

#### 四、修改CommonParamInfo类，添加readFromParcel方法

```java
package com.jingdong.jdlogsys.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhengqing on 2014/12/18.
 */
public class CommonParamInfo implements Parcelable {
    public static final Parcelable.Creator<CommonParamInfo> CREATOR = new Creator<CommonParamInfo>() {
        @Override
        public CommonParamInfo[] newArray(int size) {
            return new CommonParamInfo[size];
        }

        @Override
        public CommonParamInfo createFromParcel(Parcel in) {
            return new CommonParamInfo(in);
        }
    };
    public String strUid;           //用户id
    public String strUuid;          //用户唯一标识，根据设备串号等唯一编码

    public CommonParamInfo() {
        // TODO Auto-generated constructor stub
    }
    @SuppressWarnings("unchecked")
    public CommonParamInfo(Parcel in) {
        // TODO Auto-generated constructor stub
        strUid = in.readString();
        strUuid = in.readString();
    }
    public void readFromParcel(Parcel in){
        strUid = in.readString();
        strUuid = in.readString();
    }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(strUid)
          .append(strUuid);
        return sb.toString();
    }
    public String getStrUid() {
        return strUid;
    }
    public void setStrUid(String strUid) {
        this.strUid = strUid;
    }
    public String getStrUuid() {
        return strUuid;
    }
    public void setStrUuid(String strUuid) {
        this.strUuid = strUuid;
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(strUid);
        out.writeString(strUuid);
    }
}
```

其实readFromParcel方法的实现和构造函数中的内容是一样的。这样我们再次编译运行，发现没有任何问题了。

#### 五、接着我又把第三步中的inout修改成了in

发现并没有报找不到readFromParcel方法的错误。

- - -

参考文章：

[http://blog.csdn.net/jackyu613/article/details/6011606](http://blog.csdn.net/jackyu613/article/details/6011606)

[http://blog.csdn.net/flowingflying/article/details/22276821](http://blog.csdn.net/flowingflying/article/details/22276821)

- - -
THE END.
