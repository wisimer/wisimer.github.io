---
date: 2017-05-24
title: 使用 UI Automator 来实现Android自动化测试
category: ANDROID
layout: default
---

#### 1. 配置
直接添加 `com.android.support.test` 相关依赖的话会报如下冲突：
```
Error:Conflict with dependency 'com.android.support:support-annotations'. Resolved versions for app (25.3.1) and test app (23.0.1) differ. See http://g.co/androidstudio/app-test-app-conflict for details.
```
所以需要把 support 包剔除：
```
androidTestCompile ('com.android.support.test:runner:0.5') {
    exclude group: 'com.android.support'
}
androidTestCompile ('com.android.support.test:rules:0.5') {
    exclude group: 'com.android.support'
}
androidTestCompile 'com.android.support.test.uiautomator:uiautomator-v18:2.1.2'
```

<!--more-->

#### 2. 编写业务代码

直接新建一个Activity，并修改他的布局文件，在里面添加一个 EditText :
```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/id_container"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    tools:context="weex.wisim.com.weexdemo.MainDemoActivity">

    <EditText
        android:id="@+id/id_input"
        android:layout_width="match_parent"
        android:layout_height="60dp" />
</LinearLayout>

```
#### 3. 编写测试用例

这里我们来测试一下 EditText 里面的内容跟我们需要的是不是一致，直接新建一个测试用例 ：

```java
@Test
public void testEdit() {
    UiDevice uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    try {
        UiObject allAppsButton =  uiDevice.findObject(new UiSelector().description("MY"));//new UiObject(new UiSelector().description("MY"));
        allAppsButton.clickAndWaitForNewWindow();

        UiObject et1 = uiDevice.findObject(new UiSelector().resourceId("weex.wisim.com.weexdemo:id/id_input"));

        et1.setText("2");
        assertEquals(12, Integer.parseInt(et1.getText().toString()));
        
    } catch (Exception e) {
        e.printStackTrace();
    }
}
```

运行之后发现测试用例未通过。

#### 4. 说明

##### (1) UiDevice 这个对象可以完成一些针对设备的动作

UiDevice 对象可以通过如下方式获得：

```java
UiDevice uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
```

-  click(int x, int y) : 在(x,y)表示的像素地方点击
- pressBack() : 点击返回键
- pressDelete() : 点击删除键
- pressEnter() : 点击Enter键
- pressHome() : 点击Home键
- pressMenu() : 点击Menu键
- pressSearch() : 点击Search键
- wakeUp() : 当手机处于灭屏状态时，唤醒屏幕，并解锁。
- swipe(startX, startY, endX, endY, steps) : 在手机上滑动，从(startX,startY)到(endX,endY)。steps表示滑动的这个距离分为几步完成，数目越少，滑动幅度越大。
- setOrientationLeft() / setOrientationRight(): 将手机向相应方向旋转。
- setOrientationNatural() : 将手机旋转状态回归正常。

##### (2) UiObject对象,这个对象可以理解为控件的对象。 

一般一个UiObject对象可以通过一下形式得到，也就是配合一个UiSelector就可以得到一个控件。：

```java
UiObject allAppsButton =  uiDevice.findObject(new UiSelector().description("MY"));
```
 
- click() : 点击控件
- clickAndWaitForNewWindow() : 点击某个控件，并等待窗口刷新
- longClick() : 长按
- clearTextField() : -清除文本，主要针对编辑框
- getChildCount() : 获取子控件个数。这个方法可以看出，其实UiObject也可以是一个控件的集合
- getPackageName() : 得到控件的包名
- getSelector() : 得到当前控件的选择条件
- getText() : 得到控件上的Text
- isCheckable() : 是否可以被check
- isChecked() : 是否处于checked状态
- isClickable() : 是否可点击
- isLongClickable() : 是否可长按
- isScrollable() : 是否可滑动
- isSelected() : 是否可选中

##### (3) UiSelector对象,这个对象可以理解为一种条件对象，描述的是一种条件

UiSelector经常配合UiObject使用，可以得到某个（某些）符合条件的控件对象。

- checked(boolean val) : 描述一种check状态为val的关系。
- className(className) : 描述一种类名为className的对象关系
- clickable(boolean val) : 与checked类似，描述clickable状态为val的关系
- description(desc) : 文案描述
- descriptionContains(desc) : 与description类似
- focusable(boolean val) : 与checked类似
- index(index) : 用当前对象在父对象集中的索引作为描述
- packageName(String name) : 用包名作为条件描述
- selected(val) : 描述一种选择关系
- text(text) : 最为常用的一种关系，用控件上的文本即可找到当前控件，需要注意，所有使用text属性找到的控件，必须是英文的。也就是说，不支持通过中文查找控件！
- textContains(text) : 与text类似
- textStartsWith(text) : 与text类似
    
##### (4) UiCollection对象，这个对象可以理解为一个对象的集合。

因为UiSelector描述后得到的有可能是多个满足条件的控件集合，因此可以用来生成UiCollection:

```java
UiCollection mUiCollection = new UiCollection(new UiSelector().text("Settings"));
```
- getChild(selector) : 从集合中再次通过UiSelector选择一个UiObject对象
- getChildByDescription(childPattern, text) : 从一个匹配模式中再次以text为条件选择UiObject
- getChildByText(childPattern, text) : 与上面类似。
- getChildCount() : 得到当前集合中控件的个数
    
##### (5) UiScrollable可以生成一个滚动动作的对象，其最大的作用就是可以实现滚动的查找某个元素。

比如在“设置”菜单中，“语言和输入法”这个菜单比较靠下，需要滚动后才可以看到（找到），因此就用上了UiScrollable：

```java
UiScrollable settingItems = new UiScrollable( new UiSelector().scrollable(true));  
UiObject languageAndInputItem = settingItems.getChildByText(  
new UiSelector().text("Language & input"), "Language & input",  
true);  
```
上面的形式就可以在滚动中查找显示有“Language & input”的控件，也就是“语言和输入法”的设置项。

##### (6) 添加Log的方法也可以通过Java标准的println来实现

System.out.println("This used to print some log!!!" + setLanItem.getText());

- - -
THE END.