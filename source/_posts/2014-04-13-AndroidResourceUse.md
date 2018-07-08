---
date: 2014-04-13
layout: default
category: ANDROID
title: res资源的使用
---

#### 属性资源：attrs.xml

```java
<attr name=”属性名字” format=“属性值限定的字符串“>
    <定义全局的属性>
</attr>
<declare-styleable name=”组件名称”
	<attr name=”属性名字” format=“属性值限定的字符串“/>
	< attr name=”全局定义的属性名称”/>
</ declare-styleable >
```

值得注意的是定义属性 需要定义自己的命名空间 原则上命名空间可以随便取但是 也有一定规则 命名空间必须以http://schemas.android.com/apk/res/ 开头 后面是所要引用的R文件所在的路径 也就是包名   属性值限定的字符串的意思是属性的取值限定的类型 如reference 引用资源id类型 string float 等等 最后要在所要引用该属性的布局文件中加上自己的命名空间

<!--more-->

- 使用方式﻿-CustomAttributes﻿：﻿﻿

```java
AttributeSet的一个对象先有 假设是attrs
//获得属性数组对象
TypedArray typedArray=Context.obtainStyledAttributes（atts，R.styleable.组件名称）;
//获得<declare-styleable>标签下的属性值
resourceid= typedArray.getResourceId（R.styleable.组件名称_属性名称,default_value）;
```
(布局)在视图的布局文件 加入命名空间 如app 那么引用的就是app：属性名称=“属性值”

- - -

####菜单资源：/menu目录
```java
<menu  xmlns:android="http://schemas.android.com/apk/res/android" >
   <item  android:属性   />
   <item  android:属性   />
     <group id>
        <item  android:属性   />
        <item  android:属性   />
            <menu>
               <item  android:属性   />
               <item  android:属性   />
                  <group id>
                    <item  android:属性   />
                    <item  android:属性   />
                  </group>
     </group>
</menu>
```

上面是一个完整的菜单方式 根据要求可以删减
比如说不需要设置子菜单项中间那个 menu的标签就不需要的  有的需要组 也可以删去 但是item内不可以有item
值得注意的是 命名空间 可以是安卓默认的 也可以是自己定义的 设置的属性 主要是菜单的属性 与在布局文件定义类似
Menu 标签没有任何属性 除了第一个跟标签需要命名空间 其他的不要 当然 菜单资源的文件 要以``<menu>`` 为root标签 不是以``<resource>``

- item的属性如下
<pre>
 <code>
id 菜单项的id
menuCategory 菜单项的种类 如设置成system 表示系统菜单 放在其他的后面
orderInCategory 同种类菜单排列顺序
title 菜单项的显示文本
titleCondensed 菜单项的短标题 如果菜单项文本太长 会显示该值
icon 菜单项图片的id
alphabeticShortCut 菜单项的字母快捷键
numericShortCut 菜单项数字快捷键
checkable 菜单项是否带复选框
checked 如果菜单项带复选框 表示该复选框是否被默认选中
visible 菜单项是否可见
enabled 菜单项是否可用
 </code>
</pre>

- group的属性如下
<pre>
 <code>
id 菜单组的id
menuCategory 与item相同 只是作用域在菜单组
orderInCategory  与item相同 只是作用域在菜单组
visible 菜单组里的所有菜单项是否可见
enable 菜单组里所有菜单是否可用
CheckableBehavior 设置该菜单组上显示的选择组件 如果为all 显示checkbox
如果为single 显示Radio Button 如果为none 正常显示菜单 不会显示选择组件（check    box  Radio Button）
 </code>
</pre>

- 使用方式：
如果是在onCreateOptionsMenu(Menu menu) 或者 onCreteContextMenu（）的回调方法里装载资源文件
>MenuInflater menuinflater=getMenuInflater（）
>menuinflater.inflate（R.menu.菜单资源文件的名称）

如果是onCreteContextMenu里 要在onCreate（）方法里将上下文菜单注册到某个组件上
>registerForContextMenu（某个组件的名称）

- - -

#### 数组资源：arrays.xml
- 定义形式：

```java
<数组类型-array name=”array_name”>
	<item>
		array_value1
	</item>
	<item>
		array_value2
	</item>
</数组类型-array>
```

- 使用方式:

		String []abc=getResource().getStringArray(R.array.array_name)﻿﻿

eg:

```java
<?xml version="1.0" encoding="utf-8"?>
<resources>
	<string-array name="countries">
	    <item >中国</item>
	    <item >美国</item>
	</string-array>
    <integer-array name="numbs">
        <item >1</item>
        <item >2</item>
    </integer-array>
</resources>
```

#### xml资源:使用方式

```java
XmlResourceParser xml=getResources().getXml(R.xml.xml_file_name);
```

#### raw资源:使用方式

```java
InputStream read= getResources().openRawResource（R.raw.file_name）;

OutputStream write= getResources().openRawResource（R.raw.file_name）;
```

#### assets资源:使用方式

```java
InputStream read=getAssets（）.open（“文件的名称“）
//如果assets下还有个文件目录 也要把目录名写上用 / 隔开 如abc/a.txt
OutputStream write=getAssets（）.open（“文件的名称“）
```

- - -
THE END.
