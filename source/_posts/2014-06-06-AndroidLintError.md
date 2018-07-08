---
date: 2014-04-06
title: Android打包应用lint error
category: ANDROID
layout: default
---

Eclipse打包apk时出现的一个小错误...
有时候最终打包应用时会显示如下错误

![打包应用错误](/src/imgs/1406/06_linterror.png)

解决方法如下：

<!--more-->

**Step 1：**

Eclipse顶部菜单栏`Windows->Preferences->Android->Lint Error Checking`

原本Run full error check...前面是有勾的。把勾取消。

![错误解决](/src/imgs/1406/06_linterror_solve.png)

**Step 2：**

右击`工程->Android Tools->Clear Lint Markers`

**Step 3：**

接着再按正常步骤打包应用

- - -
THE END.
