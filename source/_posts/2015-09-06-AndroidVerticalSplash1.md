---
date: 2015-09-06
layout: default
category: ANDROID
title: 自定义ViewGroup实现竖向引导界面 Part1
---

#### 最终的目标是实现上下滑动切换页面，同时可以根据手指初始化速度来实现自行滚动。

第一步来实现控制控件可以随着手指滑动上下滚动

> 原理就是通过在手指滑动时，在onTouchEvent中调用scrollBy(0, dy)方法实现滚动效果。

<!--more-->

#### 一、继承ViewGroup实现该控件

在构造方法中获取屏幕高度

```java
public class Day10View extends ViewGroup {
    public Day10View(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        /**
         * Point1 ：获取屏幕高度
         */
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        mScreenHeight = metrics.heightPixels;
    }
    ......
}
```

#### 二、重写onMeasure方法

```java
@Override
protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    int childCount = getChildCount();
    /**
     * Point2 ： 根据高度计算每个子View布局
     */
    for (int i = 0; i < childCount; i++) {
        View childView = getChildAt(i);
        measureChild(childView, widthMeasureSpec, mScreenHeight);
    }
}
```

#### 三、重写onLayout方法，确定控件以及子View的布局

```java
@Override
protected void onLayout(boolean changed, int l, int t, int r, int b) {
    int childCount = getChildCount();
    /**
     * Point3 ： 设置控件的高度为 屏幕的高度*子View的个数
     */
    MarginLayoutParams marginLayoutParams = (MarginLayoutParams) getLayoutParams();
    marginLayoutParams.height = mScreenHeight * childCount;
    setLayoutParams(marginLayoutParams);

    /**
     *Point4 ： 确定每个子View的位置
     */
    for (int i = 0; i < childCount; i++) {
        View childView = getChildAt(i);
        childView.layout(l, i * mScreenHeight, r, (i + 1) * mScreenHeight);
    }
}
```

#### 四、重写onTouchEvent方法，处理滑动事件

```java
/**
 * 移动时不断更新的Y值
 */
int mLastY = 0;
@Override
public boolean onTouchEvent(MotionEvent event) {

    /**
     * Point5：获取当前手指位置的Y坐标
     */
    int y = (int) event.getY();
    switch (event.getAction()) {

        case MotionEvent.ACTION_DOWN:
            /**
             *Point6 ：手指按下时获取Y轴滚动的距离。
             * getScrollY()是View的方法，如果为正表示内容向上滑动
             */
            mLastY = y;
            break;

        case MotionEvent.ACTION_MOVE:

            /**
             * Point7 : 计算Y轴滚动的差值
             */
            int dy = mLastY - y;

            /**
             * Point8 : 关键代码!!!调用scrollBy执行滚动
             */
            scrollBy(0, dy);

            /**
             * Point9 ： 这一步很重要，在手指尚未抬起时，要将y赋值给mLastY，接着继续执行Point7计算差值
             */
            mLastY = y;
            break;

        case MotionEvent.ACTION_UP:
            /*暂时这里还不需要进行操作*/
            break;
    }
    return true;
}
```

#### 五、Part1 最终效果：

![0906_verticalsplash1.gif](/src/imgs/1509/0906_verticalsplash1.gif)

- - -

参考网址：
[http://blog.csdn.net/lmj623565791/article/details/23692439](http://blog.csdn.net/lmj623565791/article/details/23692439)


源码github ：
[https://github.com/whisper92/CustomViewDays/blob/master/app/src/main/java/views/Day10View.java](https://github.com/whisper92/CustomViewDays/blob/master/app/src/main/java/views/Day10View.java)

- - -
THE END.
