---
date: 2014-06-30
layout: default
title: 自定义View-SwitchButton
category: ANDROID
---

系统自带的（5.0之前的）switch 实在是不好看，实际需求是要有一种直观的 开和关 的感受。下面就通过自定义view来实现一个炫酷的SwicthButton

#### 0. 继承View实现OnTouchListener接口

```java
public class Blog_SwitchButton extends View implements OnTouchListener {
  private Bitmap bg_on, bg_off, slipper_btn;
  // 按下时的x和当前的x
  private float downX, nowX;
  // 记录用户是否在滑动
  private boolean onSlip = false;
  // 当前的状态ON / OFF
  private boolean nowStatus = false;
  // 监听接口
  private OnChangedListener listener;
  // 两个构造函数
  public Blog_SwitchButton(Context context) {
  super(context);
  init();
}

public Blog_SwitchButton(Context context, AttributeSet attrs) {
  super(context, attrs);
  init();
}
```

<!--more-->
#### 1.初始化函数，用于解析图片，以及设置监听

```java
public void init() {
  // 载入图片资源
  bg_on = BitmapFactory.decodeResource(getResources(), R.drawable.on);
  bg_off = BitmapFactory.decodeResource(getResources(), R.drawable.off);
  slipper_btn = BitmapFactory.decodeResource(getResources(),R.drawable.btn);
  setOnTouchListener(this);
}
```


#### 2.重写onDraw方法，绘制视图

```java
protected void onDraw(Canvas canvas) {
  super.onDraw(canvas);
  Matrix matrix = new Matrix();
  Paint paint = new Paint();
  float x = 0;

  // 根据nowX设置背景，开或者关状态
  if (nowX < (bg_on.getWidth() / 2)) {
    canvas.drawBitmap(bg_off, matrix, paint);// 画出关闭时的背景
  } else {
    canvas.drawBitmap(bg_on, matrix, paint);// 画出打开时的背景
  }

  if (onSlip) {// 是否是在滑动状态,
    if (nowX >= bg_on.getWidth()){// 是否划出指定范围,不能让滑块跑到外头,必须做这个判断
      x = bg_on.getWidth() - slipper_btn.getWidth() / 2;// 减去滑块1/2的长度
    } else {
      x = nowX - slipper_btn.getWidth() / 2;
    }
  } else {
      if (nowStatus) {// 根据当前的状态设置滑块的x值
      x = bg_on.getWidth() - slipper_btn.getWidth();
  } else {
      x = 0;
  }
}

// 对滑块滑动进行异常处理，不能让滑块出界
if (x < 0) {
  x = 0;
} else if (x > bg_on.getWidth() - slipper_btn.getWidth()) {
  x = bg_on.getWidth() - slipper_btn.getWidth();
}
// 画出滑块
canvas.drawBitmap(slipper_btn, x, 0, paint);
}
```


#### 3.重写onTouch方法用于处理用户到触摸事件

```java
public boolean onTouch(View v, MotionEvent event) {
  switch (event.getAction()) {
    // 当用户按下时
    case MotionEvent.ACTION_DOWN: {
      if (event.getX() > bg_off.getWidth()|| event.getY() > bg_off.getHeight()) {
        //如果在控件的范围之外则不处理
        return false;
      } else {
        //如果在控件的范围之内，则设置为滑动状态
        onSlip = true;
        downX = event.getX();
        nowX = downX;
      }
    break;

    //当用户滑动按钮时
    case MotionEvent.ACTION_MOVE: {
      nowX = event.getX();
      break;
    }

    //当用户抬起手指时
    case MotionEvent.ACTION_UP: {
      onSlip = false;
      if (event.getX() >= (bg_on.getWidth() / 2)) {
        //如果按钮此时在右侧，则为ON状态把nowState设置为true，并且要把按钮归到右侧到位置
        nowStatus = true;
        nowX = bg_on.getWidth() - slipper_btn.getWidth();
      } else {
        //如果按钮此时在左侧，则为OFF状态，按钮的位置为0
        nowStatus = false;
        nowX = 0;
      }
      if (listener != null) {
        listener.OnChanged(Blog_SwitchButton.this, nowStatus);
      }
    break;
    }

    // 每次用户产生触摸事件之后都要刷新界面，重新绘制视图
    invalidate();
    return true;
}
```

#### 4.定义一个回调接口.为WiperSwitch设置一个监听，供外部调用的方法

```java
public interface OnChangedListener {
  //在外部实现这个方法，可以在这个方法内部处理一些逻辑业务
  public void OnChanged(Blog_SwitchButton wiperSwitch, boolean checkState);
}
public void setOnChangedListener(OnChangedListener listener) {
  this.listener = listener;
}
```

#### 5.设置滑动开关的初始状态，供外部调用

```java
public void setChecked(boolean checked) {
  if (checked) {
    nowX = bg_off.getWidth();
  } else {
    nowX = 0;
  }
  nowStatus = checked;
}
```


效果图如下:

![30_switchbutton.gif](/src/imgs/1406/30_switchbutton.gif)

- - -
THE END.
