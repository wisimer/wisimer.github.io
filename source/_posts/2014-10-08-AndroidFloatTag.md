---
date: 2014-10-08
title: 自定义View-浮动标签
layout: default
category: ANDROID
---

#### 1.继承LinearLayout自定义一个FloatTag类

有如下几个成员变量

```java
Context mContext;

/*
 * 放置内容，可以滑动的父布局
 */
ScrollView mScrollView;

/*
 * 显示和隐藏的视图
 */
View mFloatTagView;

/*
 * 控制显示或隐藏 mFloatTagView 的回调接口
 */
StayViewListener mStayViewListener;

/*
 * 首次进入页面，应该是向上滑动，直到到达 mFloatTagView 的位置
 * 设置一个标记，未到达时mScrollUp为true，mFloatTagView 是隐藏状态，
 * 到达之后mScrollUp为false， mFloatTagView 是可见状态
 */  
boolean mScrollUp = true;  
```

<!--more-->

#### 2.进行一些初始化工作

```java
  //在构造方法中调用init()方法
public void init(ScrollView scrollView,View floatTagView,FloatTagStateListener listener){
  this.mScrollView=scrollView;
  this.mFloatTagView=floatTagView;
  this.mFloatTagStateListener=listener;
  setOrientation(LinearLayout.VERTICAL);
}
```



#### 3.重写computeScroll()方法

  其中mScrollView.getScrollY()得到的数值是mScrollView的纵坐标距离初始原点的纵坐标（也就是0）的距离。所以，如果是网上滑动的话，这个数值会一直增大；
如果向下滑动的话，这个数值会减小。而mFloatTagView.getTop()的到的是布局中固定的标签的上边缘的纵坐标，这个纵坐标是一个固定值，也就是mFloatTagView距离mScrollView顶部的距离。
所以，只要mScrollView向上滑动的距离等于mFloatTagView.getTop()这个值的时候，就是mFloatTagView滑动到屏幕顶部的时候，这个时候就要把布局文件中隐藏的标签头显示出来。

```java
@Override  
public void computeScroll() {  
    if(mFloatTagView!=null&&mScrollView!=null&&mStayViewListener!=null){  
        int y = mScrollView.getScrollY();  
        if(up){  
            int top = mFloatTagView.getTop();  
            if(y>=top){
              //如果向上滑动到mFloatTagView的顶端则调用mStayViewListener的显示浮动标签的方法
            	mStayViewListener.onStayViewShow();  
                up = false;  
            }  
        }  
        if(!up){  
            int bottom = mFloatTagView.getBottom();  
            if(y<=mFloatTagView.getTop()){
              //如果向下滑动到stayView的顶端则调用mStayViewListener的隐藏浮动标签的方法
            	mStayViewListener.onStayViewGone();  
                up = true;  
            }  
        }  
    }  
}  
```

#### 4.定义一个接口供外部使用OrderView时显示或隐藏浮动标签

```java
public interface FloatTagStateListener{
      public void onFloatTagShow();
      public void onFloatTagHide();
}
```

#### 5.使用

*5.1 定义主布局文件中activity_main.xml*

布局文件中id为id_btn_tag的View是一直显示的，就是通过得到他的上边缘坐标与mScrollView滑动距离相比较，来控制id为id_btn_float_tag的View的隐藏和显示。

```java
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity" >
    <com.wxp.floattag.FloatTag
        android:id="@+id/id_ft_floattag"
        android:layout_width="match_parent"
        android:layout_height="match_parent" >
        <ScrollView
            android:id="@+id/id_sv_mainscrolleview"
            android:layout_width="match_parent"
            android:layout_height="match_parent" >
            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="vertical" >
                <ImageView
                    android:layout_width="match_parent"
                    android:layout_height="200px"
                    android:src="@drawable/ic_launcher" />
                <Button
                    android:id="@+id/id_btn_tag"
                    android:background="#000000"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:text="float_tag" />
                <TextView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="@string/hello_world" />
                <ImageView
                    android:layout_width="match_parent"
                    android:layout_height="2000px"
                    android:src="@drawable/ic_launcher" />
            </LinearLayout>
        </ScrollView>
    </com.wxp.floattag.FloatTag>
    <Button
        android:id="@+id/id_btn_float_tag"
        android:visibility="gone"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:background="#000000"
        android:text="float_tag"
         />

</RelativeLayout>
```

*5.2 MainActivity.java*

要在FloatTagStateListener接口的两个回调方法中控制布局文件中标签头的隐藏和显示

```java
/*
 * 注意：这里的第二个参数的View是固定显示的，而下面控制隐藏和显示的是另一个id为id_btn_float_tag的View
 */
mFloatTag.init(mScrollView, findViewById(R.id.id_btn_tag), new FloatTagStateListener() {

@Override
public void onFloatTagShow() {
   findViewById(R.id.id_btn_float_tag).setVisibility(View.VISIBLE);
}

@Override
public void onFloatTagHide() {
   findViewById(R.id.id_btn_float_tag).setVisibility(View.GONE);
}
});  
```

效果图:

![floatlabel.gif](/src/imgs/1410/08_floatlabel.gif)


>github：[https://github.com/whisper92/FloatTag.git](https://github.com/whisper92/FloatTag.git)

- - -
THE END.
