---
date: 2015-09-07
layout: default
category: ANDROID
title: 自定义ViewGroup实现竖向引导界面 Part3
---

#### 最终的目标是实现上下滑动切换页面，同时可以根据手指初始化速度来实现自行滚动。

前面已经控制控件可以随着手指滑动上下滚动,并且滑动超过一定距离之后松开手指，可以自动滚动到上一屏或者下一屏

还有一些工作需要完善，比如加速度检测以及边界检测

<!--more-->

#### 一、首先来实现边界检测

也就是滑动到顶部之后不能再继续向下滑动，滑动到底部之后也不能继续向上滑动

在onTouchEvent中作判断

```java
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (mIsScrolling) {
            return super.onTouchEvent(event);
        }
        int y = (int) event.getY();
        obtainVelocity(event);
        switch (event.getAction()) {

            ......

            case MotionEvent.ACTION_MOVE:

                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                int dy = mLastY - y;

                /**
                 * Point1 ：
                 * (getScrollY() + dy) < 0表示已经到达控件顶部
                 * dy < 0表示还想要向下滑动
                 */
                if ((getScrollY() + dy) < 0 && dy < 0) {
                    return super.onTouchEvent(event);
                }

                /**
                 * Point2 ：
                 * (getScrollY() + dy) > (getHeight() - mScreenHeight)表示已经滑动到达控件底部了（控件的总的高度减去最后一个子View的高度则是最后一个控件的顶部位置）
                 * dy > 0表示还想要向上滑动
                 */
                if ((getScrollY() + dy) > (getHeight() - mScreenHeight) && dy > 0) {
                    return super.onTouchEvent(event);
                }

                scrollBy(0, dy);
                mLastY = y;
                break;

                ......
        }

        return true;
    }
```

这样不管滑动到顶部还是底部都不能再滑过边界了

#### 二、实现加速度检测

定义如下几个方法分别用于获取加速度检测器以及释放资源，还有获取Y轴的加速度

```java
/**
 * Point3 ：初始化加速度检测器
 *
 * @param event
 */
public void obtainVelocity(MotionEvent event) {

    if (mVelocityTracker == null) {
        mVelocityTracker = VelocityTracker.obtain();
    }

    mVelocityTracker.addMovement(event);
}

/**
 * Point4 ：释放加速度检测器
 */
public void recycleVelocity() {

    if (mVelocityTracker != null) {
        mVelocityTracker.recycle();
        mVelocityTracker = null;
    }
}

/**
 * Point5 ：获取Y轴方向加速度
 *
 * @return
 */
public int getYVelocity() {

    mVelocityTracker.computeCurrentVelocity(1000);
    return (int) mVelocityTracker.getYVelocity();
}

```

#### 三、然后在onTouchEvent中调用obtainVelocity方法以初始化加速度检测器

```java
@Override
public boolean onTouchEvent(MotionEvent event) {

    ......

    obtainVelocity(event);

    ......

}
```

#### 四、每次抬起手指后，都要释放加速度检测器资源

```java
@Override
public boolean onTouchEvent(MotionEvent event) {

    ......

    switch (event.getAction()) {
        case MotionEvent.ACTION_UP:

        ......
        recycleVelocity();
        break;
    }

    ......

}
```

#### 五、在判断能否自动滚动的方法中加上加速度判断

```java
/**
 * Point6 ： 当加速度大于600时，也判定可以滚动
 *
 * @return
 */
public boolean ableScroll() {
    return Math.abs(mScrollEndY - mScrollStartY) > mScreenHeight / 2 || Math.abs(getYVelocity()) > 600;
}
```

#### 六、Part3 最终效果

![0907_verticalsplash3.gif](/src/imgs/1509/0907_verticalsplash3.gif)

- - -

参考网址：
[http://blog.csdn.net/lmj623565791/article/details/23692439](http://blog.csdn.net/lmj623565791/article/details/23692439)

源码github ：
[https://github.com/whisper92/CustomViewDays/blob/master/app/src/main/java/views/Day12View.java](https://github.com/whisper92/CustomViewDays/blob/master/app/src/main/java/views/Day12View.java)

- - -
THE END.
