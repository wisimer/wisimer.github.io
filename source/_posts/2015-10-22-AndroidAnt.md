---
date: 2015-10-22
layout: default
category: ANDROID
title: Ant 构建Android工程
---

前言：Eclipse时代的Android构建工具Ant，原本是用于跨平台构建Java工程的，虽然现在AndroidSrudio已经默认使用Gradle了，不过还是有学习的地方的。

### 1.下载

选择 [apache-ant-1.9.6-bin.zip](http://apache.fayea.com/ant/binaries/apache-ant-1.9.6-bin.zip)版本

<!--more-->

### 2.配置环境变量

2.1.新建 ANT_HOME 环境变量

![1022_ant_home.png](/src/imgs/1510/1022_ant_home.png)

2.2.添加到path

![1022_path.png](/src/imgs/1510/1022_path.png)

2.3.命令行检测 ： ant -version

![1022_ant-version.png](/src/imgs/1510/1022_ant-version.png)

### 3. 直接创建ant模板工程

终端执行 ： `android create project -k com.wxp.ant.test -a MainActivity -p ./AntTest -t 11`

也可以使用 update命令来更新一个已有的工程 ： `android update project -k com.wxp.ant.test -a MainActivity -p ./AntTest -t 11`

![1022_ant-help.png](/src/imgs/1510/1022_ant-help.png)

参数详情：

```
-n 项目名称
-k 新建工程的包名
-a 入口Activity
-p 路径
-t 目标平台 （可以使用 android list targets 查看可用平台）
```

此外还有直接创建gradle项目模板的命令

`android create project -k com.wxp.ant.test -a MainActivity -p ./GradleTest -t 11 -n GradleTest -g -v 1.2.3`

对比一下两个工程的目录结构

![1022_templates.png](/src/imgs/1510/1022_templates.png)

### 4.编译命令

```
ant debug : 生成一个debug包
ant release : 生成一个release包
ant isntalld
ant installr : 直接安装apk
ant uninstall : 直接卸载apk
ant clean : 清理工程
```

Android SDK中已经有了一个比较完整的build.xml文件了 ： sdk\tools\ant\build.xml

ant release 实际执行的是 :

```xml
<!-- This runs -package-release and -release-nosign first and then runs
     only if release-sign is true (set in -release-check,
     called by -release-no-sign)-->
<target name="release"
    depends="-set-release-mode, -release-obfuscation-check, -package, -post-package, -release-prompt-for-password, -release-nosign, -release-sign, -post-build"
    description="Builds the application in release mode.">
</target>
```

ant debug 实际执行的是 :

```xml
<!-- Builds debug output package -->
<target name="debug"
    depends="-set-debug-files, -do-debug, -post-build"
    description="Builds the application and signs it with a debug key.">
</target>
```

### 5.编译、打包的步骤：

```
5.1第一步 生成R.java类文件：
ant和命令行使用android SDK提供的aapt.exe程序生成R.java。

5.2第二步 将.aidl文件生成.java类文件：
ant和命令行使用android SDK提供的aidl.exe生成.java文件。

5.3第三步 编译.java类文件生成class文件：
ant和命令行使用jdk的javac编译java类文件生成class文件。

5.4第四步 将class文件打包生成classes.dex文件：
ant和命令行使用android SDK提供的dx.bat命令行脚本生成classes.dex文件。

5.5第五步 打包资源文件（包括res、assets、androidmanifest.xml等）：
ant和命令行使用Android SDK提供的aapt.exe生成资源包文件。

5.6第六步 生成未签名的apk安装文件：
ant和命令行使用android SDK提供的apkbuilder.bat命令脚本生成未签名的apk安装文件。

5.7第七步 对未签名的apk进行签名生成签名后的android文件：
ant和命令行使用jdk的jarsigner对未签名的包进行apk签名。
```

### 6.使用自定义的签名

在项目根目录下添加ant.properties（或者local.properties）文件,配置密钥的路径和别名

```
key.store=F:\\wxp.keystore
key.store.password=mima
key.alias=wxp
key.alias.password=mima
```

### 7.使用外部jar包或者library

jar包可以直接放到libs目录下面，打包的时候会自动打包进去

重点是使用已有的工程作为libiary

(1)首先将libiary拷贝到工程根目录，以slidingmenulibrary为例，进入slidingmenulibrary目录执行 ： `android update lib-project -t 11 -p ./`

或者直接在根目录下执行 ：`android update project -n AntTest -t 11 -p ./ --subprojects`

这样中方法会为目录下的每一个子工程生成构建ant所需要的文件

![1022_slidingmenu.png](/src/imgs/1510/1022_slidingmenu.png)

(2)添加相应的配置

在slidingmenulibrary目录下的project.properties文件加入 ： android.library=true

在AntTest目录下的project.properties文件中加入要使用的libiary的名称 ： android.library.reference.1=./slidingmenulibrary

(3)编译 ant debug install

使用slidingmenulibrary的效果图：

![1022_slidingmenu_use.png](/src/imgs/1510/1022_slidingmenu_use.png)

### 8.多渠道包打包

下载 多渠道所需的jar包 ： [ant-contrib-1.0b3.jar](http://sourceforge.net/projects/ant-contrib/files/ant-contrib/1.0b3/ant-contrib-1.0b3-bin.zip)，放到ant的lib目录下

(1)在AndroidManifest.xml中配置默认的渠道

![1022_manifest_channel.png](/src/imgs/1510/1022_manifest_channel.png)

(2)在\AntTest\ant.properties中配置多个渠道

![1022_multi_channels.png](/src/imgs/1510/1022_multi_channels.png)

(3)在AntTest目录下新建 一个 custom_rules.xml 文件,内容如下 :

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project name="custom_rules" >

    <taskdef resource="net/sf/antcontrib/antcontrib.properties" >
        <classpath>
            <pathelement location="lib/ant-contrib-1.0b3.jar" />
        </classpath>
    </taskdef>

    <target name="deploy" >
        <foreach
            delimiter=","
            list="${market_channels}"
            param="channel"
            target="modify_manifest" >
        </foreach>
    </target>

    <target name="modify_manifest" >
        <echo message="=============BEGIN ${channel} ==============" />
        <replaceregexp flags="g" byline="false">
           <!--正则匹配JD_CHANNEL字段 -->
           <regexp pattern="android:name=&quot;JD_CHANNEL&quot; android:value=&quot;(.*)&quot;" />
           <!--将匹配到的JD_CHANNEL字段的值替换成自定义的渠道 ${channel}  -->
           <substitution expression="android:name=&quot;JD_CHANNEL&quot; android:value=&quot;${channel}&quot;" />
           <fileset
                dir=""
                includes="AndroidManifest.xml" />
        </replaceregexp>
        <!--指定apk输出的位置 -->
        <mkdir dir="${channel}" />
        <property
            name="out.final.file"
            location="${channel}/${ant.project.name}_${channel}.apk" />
        <!-- antcall是修改之后需要继续执行的任务target。这里调用的是clean和debug. -->
        <antcall target="clean" />
        <antcall target="debug" />
        <echo message="=============END ${channel} ==============" />
    </target>
</project>
```

(4)直接用aapt查看AndroidManifest.xml文件的内容，看看有没有打包成功

`aapt l -a AntTest_JD.apk`

![1022_JD_channel.png](/src/imgs/1510/1022_JD_channel.png)

`aapt l -a AntTest_Xiaomi.apk`

![1022_Xiaomi_channel.png](/src/imgs/1510/1022_Xiaomi_channel.png)

- - -

参考文章：

1. [http://blog.csdn.net/xxdddail/article/details/21384995?utm_source=tuicool&utm_medium=referral](http://blog.csdn.net/xxdddail/article/details/21384995?utm_source=tuicool&utm_medium=referral)

2. [http://my.oschina.net/oppo4545/blog/346778#OSC_h6_5](http://my.oschina.net/oppo4545/blog/346778#OSC_h6_5)

3. [http://blog.csdn.net/sky_monkey/article/details/11882411](http://blog.csdn.net/sky_monkey/article/details/11882411)

> 说了这么多，其实然并卵

- - -
THE END.
