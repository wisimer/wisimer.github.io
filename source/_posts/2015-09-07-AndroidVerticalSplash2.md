---
date: 2015-09-07
layout: default
category: ANDROID
title: 自定义ViewGroup实现竖向引导界面 Part2
---

#### 最终的目标是实现上下滑动切换页面，同时可以根据手指初始化速度来实现自行滚动。

前面已经控制控件可以随着手指滑动上下滚动

第二步通过Scroller这个类来实现滑动超过一定距离之后松开手指，可以自动滚动到上一屏或者下一屏

> 原理就是在松开手指后，调用Scroller.startScroll方法来实现自动滚动

<!--more-->

#### 一、在构造函数中实例化一个Scroller对象

```java
mScroller = new Scroller(context);
```

因为布局无需变化，所以onMeasure()方法以及onLayout()方法中的内容不变。

#### 二、在手指抬起时，判断是否需要自动滚动，重写onTouchEvent()方法

```java
@Override
public boolean onTouchEvent(MotionEvent event) {

    /**
     * Point5 ：如果正在滚动则直接返回父类的onTouchEvent方法
     */
    if (mIsScrolling) {
        return super.onTouchEvent(event);
    }

    /**
     * Point6  ： 获取当前手指位置的Y坐标,这个值在滑动时是不断更新的
     */
    int y = (int) event.getY();
    switch (event.getAction()) {

        ......

        case MotionEvent.ACTION_UP:

            mScrollEndY = getScrollY();

            /**
             * Point11 ： 计算抬起时和按下时的差值
             */
            int dScrollY = mScrollEndY - mScrollStartY;

            if (wantScrollNext()) {
                if (ableScroll()) {
                    /**
                     * Point12 ： 判断用户想要滑到下一屏并且已经滑动的距离超过屏幕高度的1/2，则松开手指后控件自动继续向上滚动，从而滑动到下一屏
                     */
                    mScroller.startScroll(0, mScrollEndY, 0, mScreenHeight - dScrollY);
                } else {
                    /**
                     * Point13 ：否则，则松开手指后控件自动滚会原来的位置
                     */
                    mScroller.startScroll(0, mScrollEndY, 0, -dScrollY);
                }
            }

            if (wantScrollPre()) {
                if (ableScroll()) {
                    mScroller.startScroll(0, mScrollEndY, 0, -mScreenHeight - dScrollY);
                } else {
                    mScroller.startScroll(0, mScrollEndY, 0, -dScrollY);
                }
            }

            /**
             * 下面这一行很重要，表示抬起后，继续滑动。
             * 如果没有这一行，松开手指后，自动滑动过程中点击控件会停止滑动
             */
            mIsScrolling = true;
            postInvalidate();
            break;
    }
    return true;
}
```

#### 三、此外，上面调用的mScroller.startScroll方法并不会实际产生滚动效果，还需要重写View的computeScroll方法

```java
@Override
public void computeScroll() {
    super.computeScroll();
    /**
     * Point16 ： 判断是否滚动结束
     */
    if (mScroller.computeScrollOffset()) {
        scrollTo(0, mScroller.getCurrY());
        postInvalidate();
    } else {
        mIsScrolling = false;
    }
}
```

#### 四、Part2 最终效果

![0907_verticalsplash2.gif](/src/imgs/1509/0907_verticalsplash2.gif)

- - -

参考网址：
[http://blog.csdn.net/lmj623565791/article/details/23692439](http://blog.csdn.net/lmj623565791/article/details/23692439)

源码github ：
[https://github.com/whisper92/CustomViewDays/blob/master/app/src/main/java/views/Day11View.java](https://github.com/whisper92/CustomViewDays/blob/master/app/src/main/java/views/Day11View.java)

- - -
THE END.
