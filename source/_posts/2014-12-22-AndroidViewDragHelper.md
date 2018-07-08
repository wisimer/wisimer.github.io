---
layout: default
title: ViewDragHelper Preview
date: 2014-12-22
category: ANDROID
---

ViewDragHelper对于处理复杂的手势操作有着很大的帮助，这里通过ViewDragHelper来实现一个简单的拖动效果

### 通常是在自定义ViewGroup中使用ViewDragHelper ####

#### 1. 这里自定义一个LinearLayout

```java
public class MyLayout extends LinearLayout {...}
```

#### 2.如果想要拖动某个子view，可以在onFinishInflate()方法中通过findViewById来引用这个子view ####

```java
mDragView = (View) findViewById(R.id.id_view);
```

<!--more-->

#### 3.若要实例化一个ViewDragHelper，需要调用ViewDragHelper.create(viewgroup,sensitivity,callback)方法，这里传入的第三个参数就是一个回调接口

```java
ViewDragHelper mDragHeleper = ViewDragHelper.create(this,1.0f,new WxpCallback());
```

#### 4.所以要继承ViewDragHelper.Callback实现一个简单的自定义接口WxpCallback ####

```java
class WxpCallback extends ViewDragHelper.Callback {

  public boolean tryCaptureView(View view,int arg) {
    //这里默认返回false，但是只有返回true时，才能拖动
    return true;
  }

  public onEdgeDragStarted(int edgeFlags,int pointerId) {
    //mDragView就是要拖动的View
    mDragHelper.captureChildView(mDragView,pointerId);
  }

  /*
   *实现垂直拖动
   */
  public int clampViewPositionVertical(View child,int top,int dy) {
    final int topBound = getPaddingTop();
    final int bottomBound = getHeight()-mDragView.getHeight();
    final newTop = Math.min(Math.max(top,topBound),bottomBound);
    return newTop;
  }

  /*
   *实现水平拖动
   */
  public int clampViewPositionHorizontal(View child,int left,int dx) {
    final int leftBound = getPaddingLeft();
    final int rightBound = getWidth() - mDragView.getWidth();
    final newLeft = Math.min(Math.min(left,leftBound)rightBound);
    return newLeft;
  }
}
```

#### 5.另外还要重写ViewGrounp的onTouchEvent(event)以及onInterceptTouchEvent(event)方法 ####

```java
public boolean onInterceptTouchEvent(MotionEvent event) {
  return mDragHelper.shouldInterceptTouchEvent(event);
}

public boolean onTouchEvent(MotionEvent event) {
  mDragHelper.processToucEvent(event);
  return true;
}
```

- - -

### 使用

在布局文件中，将要拖动的view放在 `MyLayout` 中

```java
<com.wxp.drag.MyLayout
 android:layout_width = "match_parent"
 android:layout_height = "match_parent"
 >
 <View android:id = "@+id/id_view"
       android:layout_width = "40dp"
       android:layout_height = "40dp"
       android:background = "#6a6a6a" />
</com.wxp.drag.MyLayout>
```

- - -
THE END.
