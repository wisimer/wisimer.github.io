---
layout: default
date: 2014-12-24
category: ANDROID
title: Scroller 实现滑动效果以及下拉操作
---

#### Scroller对于处理滑动事件有着很大的帮助，有时候可以通过Scoller来实现一些手势操作，并且可以在抬起手指之后仍可平滑的移动到指定位置。和之前介绍的ViewDragHelper类似，Scroller更多的是在自定义的组件当中发挥作用

 Scroller 中一些常用的方法

```java
mScroller.getCurrX() //获取mScroller当前水平滚动的位置  
mScroller.getCurrY() //获取mScroller当前竖直滚动的位置  
mScroller.getFinalX() //获取mScroller最终要停止的水平位置  
mScroller.getFinalY() //获取mScroller最终要停止的竖直位置  
mScroller.setFinalX(int newX) //设置mScroller最终停留的水平位置，没有动画效果，直接跳到目标位置  
mScroller.setFinalY(int newY) //设置mScroller最终停留的竖直位置，没有动画效果，直接跳到目标位置  

//滚动，startX, startY为开始滚动的位置，dx,dy为滚动的偏移量, duration为完成滚动的时间  
mScroller.startScroll(int startX, int startY, int dx, int dy) //使用默认完成时间250ms  
mScroller.startScroll(int startX, int startY, int dx, int dy, int duration)  
```

<!--more-->

> mScroller.computeScrollOffset() //返回值为boolean，true说明滚动尚未完成，false说明滚动已经完成。这是一个很重要的方法，通常放在View.computeScroll()中，用来判断是否滚动是否结束。


#### 另外值得注意的是

>scrollTo(int x,int y)以及scrollBy(int x,int y)是View中的方法，而不是Scroller的方法

- - -

- ## 实现滑动效果

#### 1. 首先我们继承 LinearLayout 实现一个ScrollerLinear类，并且实例化一个Scroller对象

```java
public class ScrollerLinear extends LienarLayout {
  private Scroller mScroller = null;

  public ScrollerLinear(Context context){
    this(context,null);
  }

  public ScrollerLinear(Context context,AttributeSet attrs) {
    super(context,attrs);
    init(context);
  }

  public void init (Context context) {
    mScroller = new Scroller(contex);
  }
}
```

#### 2. 定义一个方法smoothScrollTo方法供对象调用来实现滑动效果

```java
public void smoothScrollTo(int fx, int fy) {
  int dx = fx - mScroller.getFinalX();
  int dy = fy - mScroller.getFinalY();
  Log.e("wxp", "fx : " + fx + " | finalX : " + mScroller.getFinalX() + " | curX : " + mScroller.getCurrX());
  smoothScrollBy(dx, dy);

}

public void smoothScrollBy(int dx,int dy) {
  //如果要设置滑动持续的时间可以使用方法mScroller.startScroll(int startX, int startY, int dx, int dy, int duration)，设置第五个参数即可
  mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), dx, dy);

  invalidate();//这里必须调用invalidate()才能保证computeScroll()会被调用，否则不一定会刷新界面，看不到滚动效果
}
```

#### 3. 重写LinearLayout的computeScroll方法

```java
public void computeScroll() {
  if (mScroller.computeScrollOffset) {
    scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
  }
  //同样，这里也必须调用postInvalidate()方法
  postInvalidate();
  super.computeScroll();
}
```

#### 4. 在Activity中使用

```java
//注意这里滑动的是mScrollLinear内部的内容而不是他自己。
mScrollLinear.scrollTo(-100,-100);
```

- - -

- ## 实现下拉操作

> 这里要介绍另外一个类: GestureDetector 手势侦测器，借助这个类我们可以很方便的处理手势滑动，而不需要自己再去计算相关的位移之类的东西。

#### 首先实例化一个GestureDetector对象 mDetector = new GestureDetector(context,new WxpGestureListener());

#### 实现GestureDector.OnGestureLinener接口自定义一个 WxpGestureListener 类,重写它的onDown以及onScrolle方法用于处理滑动操作。

```java
public class WxpGestureListener implements GestureDector.OnGestureListener {
  @Override
  public boolean onDown (MotionEvent event) {
    //注意这个方法一定要返回true才能继续处理后续手势
    return true;
  }
  @Override
  public boolean onScroll (MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
    //就是要在这个方法里处理滑动操作
    smoothScrollBy(0,(int)distanceY);
    //默认返回false
    return false;
  }
}
```

#### 此外，还要重写 LinearLayout 的onTouchEvent方法，将滑动手势操作交由GestureDector处理

```java
public boolean onTouchEvent(MotionEvent event) {
  swicth(event.getAction()) {
    case MotionEvent.ACTION_Up:
      //抬起手指之后返回初始位置(0,0)
      smoothScrollTo(0,0);
      break;
    default: return mGestureDector.onTouchEvent(event);
  }

  return super.onTouchEvent(event);
}
```

#### 效果图如下:

![24_scroller.gif](https://raw.githubusercontent.com/whisper92/whisper92.github.io/master/src/imgs/1412/24_scroller.gif)

- - -
THE END.
