---
date: 2015-10-23
layout: default
category: ANDROID
title: ScrollView嵌套ListView处理事件冲突
---

#### 当ListView嵌套在ScrollView中时会有两个问题

1. 列表内容显示不全

2. 滑动事件冲突

第一个问题可以参考ScrollView与ListView嵌套仿微博个人主页中的解决方法。

第二个问题就是下面要讲的废话了。

<!--more-->

### 一、首先要实现的效果是

1. 手指在ListView中滑动时，滑动事件要交给ListView来处理，也就是说手指在ListView的内容区域中可以上下滑动。

2. ListView中的内容滑动到顶部后如果手指还是继续向下滑（自己模拟一下），此时的滑动事件应该交给ScrollView来处理，也就是说ScrollView可以继续滑动。

3. ListView中的内容滑动到底部后如果手指还是继续向上滑（自己模拟一下），此时的滑动事件应该交给ScrollView来处理，也就是说ScrollView可以继续滑动。

下面就来一个一个的实现上面所列出的效果

### 二、手指在ListView中滑动时，滑动事件要交给ListView来处理

#### 1.首先如果不做任何处理，ListView嵌套在ScrollView中时，默认滑动事件是被ScrollView处理掉的，效果是这样的：

![1203_scrollview_move](/src/imgs/1510/1203_scrollview_move.gif)

我们都知道ViewGroup默认是不拦截事件的，看一下ViewGroup的源码就知道：

```java
public boolean onInterceptTouchEvent(MotionEvent ev) {
    return false;
}
```

而ScrollView是继承自FrameLayout的，那为什么ScrollView会自己处理掉滑动事件呢，到ScrollView的源码里一搜，在onInterceptTouchEvent方法中居然有这么触目惊心的一段：

```java
@Override
public boolean onInterceptTouchEvent(MotionEvent ev) {

    final int action = ev.getAction();
    if ((action == MotionEvent.ACTION_MOVE) && (mIsBeingDragged)) {
        return true;
    }

    if (getScrollY() == 0 && !canScrollVertically(1)) {
        return false;
    }

    switch (action & MotionEvent.ACTION_MASK) {
        case MotionEvent.ACTION_MOVE: {

            final int activePointerId = mActivePointerId;
            if (activePointerId == INVALID_POINTER) {
                // If we don't have a valid id, the touch down wasn't on content.
                break;
            }

            final int pointerIndex = ev.findPointerIndex(activePointerId);
            if (pointerIndex == -1) {
                Log.e(TAG, "Invalid pointerId=" + activePointerId+ " in onInterceptTouchEvent");
                break;
            }

            final int y = (int) ev.getY(pointerIndex);
            final int yDiff = Math.abs(y - mLastMotionY);
            if (yDiff > mTouchSlop && (getNestedScrollAxes() & SCROLL_AXIS_VERTICAL) == 0) {
                mIsBeingDragged = true;
                mLastMotionY = y;
                initVelocityTrackerIfNotExists();
                mVelocityTracker.addMovement(ev);
                mNestedYOffset = 0;
                if (mScrollStrictSpan == null) {
                    mScrollStrictSpan = StrictMode.enterCriticalSpan("ScrollView-scroll");
                }
                final ViewParent parent = getParent();
                if (parent != null) {
                    parent.requestDisallowInterceptTouchEvent(true);
                }
            }
            break;
        }

        case MotionEvent.ACTION_DOWN: {
            final int y = (int) ev.getY();
            if (!inChild((int) ev.getX(), (int) y)) {
                mIsBeingDragged = false;
                recycleVelocityTracker();
                break;
            }

            /*
             * Remember location of down touch.
             * ACTION_DOWN always refers to pointer index 0.
             */
            mLastMotionY = y;
            mActivePointerId = ev.getPointerId(0);

            initOrResetVelocityTracker();
            mVelocityTracker.addMovement(ev);

            mIsBeingDragged = !mScroller.isFinished();
            if (mIsBeingDragged && mScrollStrictSpan == null) {
                mScrollStrictSpan = StrictMode.enterCriticalSpan("ScrollView-scroll");
            }
            startNestedScroll(SCROLL_AXIS_VERTICAL);
            break;
        }

        case MotionEvent.ACTION_CANCEL:
        case MotionEvent.ACTION_UP:
            /* Release the drag */
            mIsBeingDragged = false;
            mActivePointerId = INVALID_POINTER;
            recycleVelocityTracker();
            if (mScroller.springBack(mScrollX, mScrollY, 0, 0, 0, getScrollRange())) {
                postInvalidateOnAnimation();
            }
            stopNestedScroll();
            break;
        case MotionEvent.ACTION_POINTER_UP:
            onSecondaryPointerUp(ev);
            break;
    }

    return mIsBeingDragged;
}

```

(1). 首先看到的是如果ScrollView接收到MOVE事件，并且这个mIsBeingDragged为true，它的onInterceptTouchEvent方法直接就返回true了，也就是拦截了滑动事件，交给它自己处理了。

(2). 接着看switch里面`case MotionEvent.ACTION_DOWN`，如果按下的时候手指落在了子控件里面mIsBeingDragged置为false。

  里面还有这么一句`mIsBeingDragged = !mScroller.isFinished();`，mIsBeingDragged就表示当前ScrollView是否在滑动

  这mIsBeingDragged有啥卵用呢？联系上面1中所说，假如ScrollView还在滑动的时候，你想去触摸嵌在里面的ListView，没门，ScrollView滑动还没结束呢，继续直接return true。

  另外要说的是，不管咋样ScrollView并不会把ListView的点击事件给拦截掉。

(3). 再来看看switch里面`case MotionEvent.ACTION_MOVE`，如果y轴方向上的滑动距离大于最小滑动距离，则将mIsBeingDragged设置为true。结合上面第二点所说，啥情况呢？也就是说虽然我手指落在了子View里面，但是如果我要滑动的话，谁也拦不住老纸（Parent）！！

从上面几点来看，ScrollView确实默认会自己处理掉滑动事件。我们想想事件分发的流程，如果父控件拦截了事件，子控件就没办法接收到事件了。那如何才能让ListView来处理滑动事件呢，接着说。

#### 2.想要让ListView来处理滑动事件，首先要重写它的dispatchTouchEvent方法

我们继承ListView实现自己的一个MyListView,重写它的dispatchTouchEvent方法

```java
@Override
public boolean dispatchTouchEvent(MotionEvent ev) {
    Log.e(TAG, TAG + "dispatchTouchEvent");

    switch (ev.getAction()) {
        case MotionEvent.ACTION_DOWN:
            Log.e(TAG, TAG + "dispatchTouchEvent -> MotionEvent.ACTION_DOWN");
            downY = ev.getRawY();
            y = downY;
            getParent().requestDisallowInterceptTouchEvent(true);
            break;
        ......
    }
}

```

在它的 MotionEvent.ACTION_DOWN 事件中调用: `getParent().requestDisallowInterceptTouchEvent(true);`

这个'requestDisallowInterceptTouchEvent'真绕口，就是说

> 我[ListView]的父亲啊[getParent()]请求您[request]行行好别让[Disallow]您的onInterceptTouchEvent方法再拦截我的事件了啊[true]

因此如果你在ListView中调用了这个方法之后，父控件（ScrollView）就不会拦截ListView的滑动事件了。ListView的内容也就可以正常滑动了。

### 三、ListView中的内容滑动到顶部后以及滑动到底部后，事件应该交给ScrollView来处理

在MyListView中实现这两个方法：

```java
public boolean scrollToBottom() {
    int first = getFirstVisiblePosition();
    int last = getLastVisiblePosition();
    int visibleCoutn = getChildCount();
    int count = getCount();
    if ((first + visibleCoutn) == count) {
        return true;
    }
    return false;
}

public boolean scrollToTop() {
    int first = getFirstVisiblePosition();
    int last = getLastVisiblePosition();
    int visibleCoutn = getChildCount();
    int count = getCount();

    if (first == 0) {
        return true;
    }
    return false;
}
```

一个用于判断ListView是否滑动到底部，一个用于判断ListView是否滑动到顶部。

接着继续重写dispatchTouchEvent方法

```java
@Override
public boolean dispatchTouchEvent(MotionEvent ev) {
    Log.e(TAG, TAG + "dispatchTouchEvent");

    switch (ev.getAction()) {
        case MotionEvent.ACTION_DOWN:
            downY = ev.getRawY();
            y = downY;
            getParent().requestDisallowInterceptTouchEvent(true);
            break;

        case MotionEvent.ACTION_MOVE:
            y = ev.getRawY();
            if (scrollToTop()) {
                if (y - downY > mTouchSlop) {
                    /**
                     * Point 1 : 如果滑动到顶部，并且手指还想向下滑动，则事件交还给父控件，要求父控件可以拦截事件
                     */
                    getParent().requestDisallowInterceptTouchEvent(false);
                    return false;
                } else if (y - downY < -mTouchSlop) {
                    /**
                     * Point 2 : 如果滑动到顶部，并且手指正常向上滑动，则事件由自己处理，要求父控件不许拦截事件
                     */
                    getParent().requestDisallowInterceptTouchEvent(true);

                }

            }

            if (scrollToBottom()) {
                if (y - downY < -mTouchSlop) {
                    /**
                     * Point 3 : 如果滑动到底部，并且手指还想向上滑动，则事件交还给父控件，要求父控件可以拦截事件
                     */
                    getParent().requestDisallowInterceptTouchEvent(false);
                    return false;
                } else if (y - downY > mTouchSlop) {
                    /**
                     * Point 4 : 如果滑动到底部，并且手指正常向下滑动，则事件由自己处理，要求父控件不许拦截事件
                     */
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
            }

            break;

        case MotionEvent.ACTION_UP:
            break;

        default:
            break;
    }
    return super.dispatchTouchEvent(ev);
}
```

很简单，看注释，实现的效果：

![1203_scroll_lv](/src/imgs/1510/1203_scroll_lv.gif)

- - -
THE END.
