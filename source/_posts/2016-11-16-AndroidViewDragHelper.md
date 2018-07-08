---
date: 2016-11-16
title: Android ViewDragHelper
category: ANDROID
layout: default
---

ViewDragHelper 作为处理手势滑动，实现复杂滑动效果很有一套。一起来看看它的实现原理。

<!--more-->

### 一、先来看看 ViewDragHelper 的 shouldInterceptTouchEvent 方法：

#### (1) 从 MotionEvent.ACTION_DOWN 开始：

```java
switch (action) {
    case MotionEvent.ACTION_DOWN: {
        final float x = ev.getX();
        final float y = ev.getY();
        final int pointerId = MotionEventCompat.getPointerId(ev, 0);
        saveInitialMotion(x, y, pointerId);

        final View toCapture = findTopChildUnder((int) x, (int) y);

        // Catch a settling view if possible.
        if (toCapture == mCapturedView && mDragState == STATE_SETTLING) {
            tryCaptureViewForDrag(toCapture, pointerId);
        }

        final int edgesTouched = mInitialEdgesTouched[pointerId];
        if ((edgesTouched & mTrackingEdges) != 0) {
            mCallback.onEdgeTouched(edgesTouched & mTrackingEdges, pointerId);
        }
        break;
    }
    ......
}
```

调用 findTopChildUnder 方法，根据 x,y 的值找到当前坐标下的第一个子View。

```java
if (toCapture == mCapturedView && mDragState == STATE_SETTLING) {
    tryCaptureViewForDrag(toCapture, pointerId);
}
```

先是判断如果刚刚拿到的子View就是想要拖拽的子View，并且当前的状态是自然滚动状态，则调用 tryCaptureViewForDrag ：

```java
boolean tryCaptureViewForDrag(View toCapture, int pointerId) {
    if (toCapture == mCapturedView && mActivePointerId == pointerId) {
        // Already done!
        return true;
    }
    if (toCapture != null && mCallback.tryCaptureView(toCapture, pointerId)) {
        mActivePointerId = pointerId;
        captureChildView(toCapture, pointerId);
        return true;
    }
    return false;
}
```
如果当前拖拽的View已经是mCapturedView，并且 mActivePointerId == pointerId 则表明已经获取了需要拖拽的View，直接返回true。否则调用  mCallback.tryCaptureView(toCapture, pointerId) 主动判断，再调用 captureChildView 方法：

```java
public void captureChildView(View childView, int activePointerId) {
    if (childView.getParent() != mParentView) {
        throw new IllegalArgumentException("captureChildView: parameter must be a descendant " +
                "of the ViewDragHelper's tracked parent view (" + mParentView + ")");
    }

    mCapturedView = childView;
    mActivePointerId = activePointerId;
    mCallback.onViewCaptured(childView, activePointerId);
    setDragState(STATE_DRAGGING);
}
```

这里就是赋值一下内部的成员变量以及更新拖动的状态为 STATE_DRAGGING。

#### (2) 再看看 MotionEvent.ACTION_MOVE 里面：
```java
case MotionEvent.ACTION_MOVE: {
    // First to cross a touch slop over a draggable view wins. Also report edge drags.
    final int pointerCount = MotionEventCompat.getPointerCount(ev);
    for (int i = 0; i < pointerCount; i++) {
        final int pointerId = MotionEventCompat.getPointerId(ev, i);
        final float x = MotionEventCompat.getX(ev, i);
        final float y = MotionEventCompat.getY(ev, i);
        final float dx = x - mInitialMotionX[pointerId];
        final float dy = y - mInitialMotionY[pointerId];

        final View toCapture = findTopChildUnder((int) x, (int) y);
        final boolean pastSlop = toCapture != null && checkTouchSlop(toCapture, dx, dy);
        if (pastSlop) {
            final int oldLeft = toCapture.getLeft();
            final int targetLeft = oldLeft + (int) dx;
            final int newLeft = mCallback.clampViewPositionHorizontal(toCapture,
                    targetLeft, (int) dx);
            final int oldTop = toCapture.getTop();
            final int targetTop = oldTop + (int) dy;
            final int newTop = mCallback.clampViewPositionVertical(toCapture, targetTop,
                    (int) dy);
            final int horizontalDragRange = mCallback.getViewHorizontalDragRange(
                    toCapture);
            final int verticalDragRange = mCallback.getViewVerticalDragRange(toCapture);
            if ((horizontalDragRange == 0 || horizontalDragRange > 0
                    && newLeft == oldLeft) && (verticalDragRange == 0
                    || verticalDragRange > 0 && newTop == oldTop)) {
                break;
            }
        }
        reportNewEdgeDrags(dx, dy, pointerId);
        if (mDragState == STATE_DRAGGING) {
            break;
        }

        if (pastSlop && tryCaptureViewForDrag(toCapture, pointerId)) {
            break;
        }
    }
    saveLastMotion(ev);
    break;
}
```

mCallback.clampViewPositionHorizontal 是用来获取拖拽的子View toCapture 在竖直方向上的 top 值。在实现 mCallback 的 clampViewPositionVertical 方法时，可以根据 targetTop 值以及你的需求，返回一个范围内的值。

接着看：

```java
if (pastSlop && tryCaptureViewForDrag(toCapture, pointerId)) {
    break;
}
```

同样是调用 tryCaptureViewForDrag 方法，就不赘述了。

- - -

### 题外话：关于使用位运算的技巧：

ViewDragHelper 内部定义了几个静态成员变量：

```java
public static final int EDGE_LEFT = 1 << 0;   //0001
public static final int EDGE_RIGHT = 1 << 1;  //0010
public static final int EDGE_TOP = 1 << 2;    //0100
public static final int EDGE_BOTTOM = 1 << 3; //1000
```

在 saveInitialMotion 保存位置信息的时候会调用 getEdgesTouched 方法：

```java
private int getEdgesTouched(int x, int y) {
    int result = 0;
    if (x < mParentView.getLeft() + mEdgeSize) result |= EDGE_LEFT;
    if (y < mParentView.getTop() + mEdgeSize) result |= EDGE_TOP;
    if (x > mParentView.getRight() - mEdgeSize) result |= EDGE_RIGHT;
    if (y > mParentView.getBottom() - mEdgeSize) result |= EDGE_BOTTOM;
    return result;
}
```

这样使用 | 和 & 位操作可以让 mInitialEdgesTouched 数组里面的某一位保存多个边界状态。这种思路很有趣。比如说我一个手势，既在左边界又在上边界，这个时候就会执行：

```java
int result = 0;
if (x < mParentView.getLeft() + mEdgeSize) result |= EDGE_LEFT;
if (y < mParentView.getTop() + mEdgeSize) result |= EDGE_TOP;
```

result 执行两次 | 操作之后会得到 0101

如果我要判断这个收拾是否在上边界，可以直接和 EDGE_TOP 执行 & 操作：

```java
int code = result & EDGE_TOP;
if (code == EDGE_TOP) {
    ......
}
```

这样就可以判断收拾是否在上边界了，同理也可以判断是否在左边界。

- - -
THE END.
