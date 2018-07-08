---
date: 2014-04-28
title: PopupWindow概要
layout: default
category: ANDROID
---

#### PopupWindow常用来代替Dialog做一些提示性或供用户选择的操作，在开发中还是经常遇到的。####

###### 实现要素 ######

0.继承PopupWindow的一个子类

1.popwindow的布局文件

2.在MainActivity中使用

3.上下出入的动画

<!--more-->

- - -

####0.继承PopupWindow实现它的一个子类PopWindow####

```java
public class PopWindow extends PopupWindow{  
  View popView;
  public PopWindow(Context context,OnClickListener itemOnClickListener){
    super(context);
    LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    popView=inflater.inflate(R.layout.pop, null);
    this.setContentView(popView);

    //设置PopupWindow弹出窗体的宽
    this.setWidth(LayoutParams.FILL_PARENT);
    //设置PopupWindow弹出窗体的高
    this.setHeight(LayoutParams.WRAP_CONTENT);
    //设置PopupWindow弹出窗体可点击
    this.setFocusable(true);
    //设置PopupWindow的出入动画
    this.setAnimationStyle(R.style.PopupAnimation);
  }
}
```

####1.上下出入动画####

**在`values`文件夹下的`styles.xml`文件中定义一个包含进出动画效果的style**

```java
<resource>
  <style name="PopupAnimation" parent="android:Animation">
    <item name="android:windowEnterAnimation">@anim/push_bottom_in</item>
    <item name="android:windowExitAnimation">@anim/push_bottom_out</item>
  </style>
</resource>
```

**在`anim`文件夹下实现进出动画效果的xml文件**

>向上渐现滑入式：push_bottom_in.xml

```java
<?xml version="1.0" encoding="utf-8"?>
<set xmlns:android="http://schemas.android.com/apk/res/android" >
    <translate
      android:duration="200"
      android:fromYDelta="100%p"
      android:toYDelta="0"/>
    <alpha
      android:fromAlpha="0.0"
      android:toAlpha="1.0"
      android:duration="200"/>
</set>
```

>向下渐隐滑出式：push_bottom_out.xml

```java
<?xml version="1.0" encoding="utf-8"?>
<set xmlns:android="http://schemas.android.com/apk/res/android" >
    <translate
        android:duration="200"
        android:fromYDelta="0"
        android:toYDelta="50%p" />
    <alpha
        android:fromAlpha="1.0"
        android:toAlpha="0.0"
        android:duration="200"/>
</set>
```

####2.popwindow的布局文件####

>**注意高度使用wrap_content**

```java
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content" >
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical" >
        <Button
            android:id="@+id/confirm"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="confirm" />
        <Button
            android:id="@+id/cancle"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="cancle" />
    </LinearLayout>
</RelativeLayout>
```

####3.在MainActivity中实例化一个PopWindow并设置它的位置####

    popWindow.showAtLocation(MainActivity.this.findViewById(R.id.main), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);

- - -
THE END.
