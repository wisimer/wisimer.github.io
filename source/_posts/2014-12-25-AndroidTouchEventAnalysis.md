---
layout: default
date: 2014-12-25
category: ANDROID
title: Android触摸事件分发流程详细分析

---

### 触摸事件的分发主要涉及到的就是dispatchTouchEvent,onInterceptTouchEvent,onTouchEvent这三个方法。

下面通过Log来详细分析一下触摸事件的分发流程,为了更直观的现实事件的分发路径，这里我们将每个方法中都加入Log

#### 1. 首先继承LinearLayout自定义一个 MyChildLayout

然后重写它的三个方法 dispatchTouchEvent,onInterceptTouchEvent,onTouchEvent

> 值得注意的是，onInterceptTouchEvent方法是ViewGroup才有而View没有的

为了详细分析每一步，这里将所有的触摸事件以及相应的方法的返回值全部打印出来

<!--more-->

```java
@Override
public boolean dispatchTouchEvent(MotionEvent ev) {
    switch (ev.getAction()) {
        case MotionEvent.ACTION_DOWN:
            Log.e("wxp", "mylayout- dispatchTouchEvent : ACTION_DOWN");
            break;
        case MotionEvent.ACTION_MOVE:
            Log.e("wxp", "mylayout- dispatchTouchEvent : ACTION_MOVE");
            break;
        case MotionEvent.ACTION_UP:
            Log.e("wxp", "mylayout- dispatchTouchEvent : ACTION_UP");
            break;
        case MotionEvent.ACTION_CANCEL:
            Log.e("wxp", "mylayout- dispatchTouchEvent : ACTION_CANCEL");
            break;
        default:
            break;
    }
    //这里默认返回值为 false,也就是事件不分发给自己而分发给子View
    return super.dispatchTouchEvent(ev);
}

@Override
public boolean onInterceptTouchEvent(MotionEvent ev) {
    boolean f = false;
    Log.e("wxp", "mylayout- onInterceptTouchEvent : " +f);
    switch (ev.getAction()) {
        case MotionEvent.ACTION_DOWN:
            Log.e("wxp", "mylayout- onInterceptTouchEvent : ACTION_DOWN");
            break;
        case MotionEvent.ACTION_MOVE:
            Log.e("wxp", "mylayout- onInterceptTouchEvent : ACTION_MOVE");
            break;
        case MotionEvent.ACTION_UP:
            Log.e("wxp", "mylayout- onInterceptTouchEvent : ACTION_UP");
            break;
        case MotionEvent.ACTION_CANCEL:
            Log.e("wxp", "mylayout- onInterceptTouchEvent : ACTION_CANCEL");
            break;
        default:
            break;
    }
    return f;
}

@Override
public boolean onTouchEvent(MotionEvent event) {
    boolean f = false;
    Log.e("wxp", "mylayout- onTouchEvent : true");
    switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            Log.e("wxp", "mylayout- onTouchEvent : ACTION_DOWN");
            break;
        case MotionEvent.ACTION_MOVE:
            Log.e("wxp", "mylayout- onTouchEvent : ACTION_MOVE");
            break;
        case MotionEvent.ACTION_UP:
            Log.e("wxp", "mylayout- onTouchEvent : ACTION_UP");
            break;
        case MotionEvent.ACTION_CANCEL:
            Log.e("wxp", "mylayout- onTouchEvent : ACTION_CANCEL");
            break;
        default:
            break;
    }
    return f;
}
```

#### 2. 然后继承Butoon自定义一个 MyButton,并重写它的两个个方法 dispatchTouchEvent,onTouchEvent

MyButton这两个方法内部的实现和 MyChildLayout 一样，接着只要改变各个方法的返回值，来观察触摸事件的传导路线即可。

#### 3.  布局文件如下

```java
<custom.MyLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/id_mylayout"
    android:background="#056f00"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <custom.MyChildLayout
        android:id="@+id/id_childlayout"
        android:background="#e00032"
        android:orientation="vertical"
        android:layout_width="match_parent"
        android:layout_height="400dp">

        <custom.MyButton
            android:background="#4d69ff"
            android:layout_width="match_parent"
            android:layout_height="200dp"
            android:id="@+id/id_btn"
            android:text="Button" />
    </custom.MyChildLayout>
</custom.MyLayout>
```

效果图：

![layout_pic.png](/src/imgs/1412/03_TouchEvent/layout_pic.png)

- 绿色部分为顶层父布局,也就是MyLinearLayout，同样是继承LinearLayout，但是这里所有的事件传导都是按照系统的默认值流程执行，我们并不做任何修改。

```java
@Override
public boolean onTouchEvent(MotionEvent event) {
    Log.e("wxp", "MyLayout- onTouchEvent : false");
    return super.onTouchEvent(event);
}

@Override
public boolean onInterceptTouchEvent(MotionEvent ev) {
    Log.e("wxp", "MyLayout- onInterceptTouchEvent : false");
    return super.onInterceptTouchEvent(ev);
}

@Override
public boolean dispatchTouchEvent(MotionEvent ev) {
    Log.e("wxp", "MyLayout- dispatchTouchEvent : false");
    return super.dispatchTouchEvent(ev);
}
```

- 红色部分为子布局，也就是MyChildLayout,我们要在这里修改事件的传导过程以观察现象。
- 蓝色部分是位于子布局中一个MyButton。

- - -

### 准备工作已经完成，下面来实际操作一下

> 每次操作都是点击蓝色的Button区域

#### 1. 首先，我们将所有方法的返回值均设置成系统默认的返回值，也就是返回相应的父类方法返回值即可。看一下Log

![case_default.png](/src/imgs/1412/03_TouchEvent/case_default.png)

######## 这里我们借这个简单的Log来详细阐述一下长按Button这个操作所产生的触摸事件的分发流程。

- 首先，手指按下，ACTION_DOWN事件由顶层View也就是这里的MyLayout的dispatchTouchEvent的方法来分发，由于这里默认返回的是false，也就是不分发给自己而是继续向下分发。接着onInterceptTouchEvent方法想要拦截这个事件，但是由于系统默认的也是返回false，也就是不拦截。
- 于是这个ACTION_DOWN事件会分发给它的子View也就是MyChildLayout。由于ViewGroup的dispatchTouchEvent方法默认返回值是false，所以这里也和它的父控件一样，交由onInterceptTouchEvent方法来拦截。同样ViewGroup的onInterceptTouchEvent的默认返回值也是false。所以事件会继续向下分发。
- 知道分发到最底层的子View，也就是这里的MyButton，由于View的dispatchTouchEvent方法默认返回值为true，所以会将事件分发给自己处理，而不必向下分发。处理这个事件是在onTouchEvent中，而View的onTouchEvent默认返回值为true，也就是默认自己消费这个事件，不必向上传递。
- 由于我这里是长按Button，所以在ACTION_DOWN触发之后会执行Button的onLongClick方法。自此这个ACTION_DOWN事件传递完毕。
- 长按之后，将手指抬起，ACTION_UP事件也会按照上述的路线再次分发执行。
- 最终，ACTION_UP消费完成之后会执行Button的onClick方法。

> 从这个Log也可以清楚的表明，其实Android的`一个触摸事件`其实是一个单独的手指操作，例如ACTION_DOWN是一个手指按下事件，ACTION_UP是一个手指抬起事件，而并不像我们日常生活中所理解的点击一下是一个事件。

#### 2.  由于一开始我并没有调用super.dispatchTouchEvent，而是直接将dispatchTouchEvent方法返回true or false。结果产生了一些怪异的分发路径。

###### 2.1 不如来看看在MyChildLayout不调用super.dispatchTouchEvent方法的情况下（MyChildLayout中其方法以及其MyButton中的方法均返回默认值）:

- （1）MyChildLayout dispatchTouchEvent方法 return false;

![case_error_false.png](/src/imgs/1412/03_TouchEvent/case_error_false.png)

可以看出，这种情况下，事件虽然会分发，但是并不会执行到 MyChildLayout 的onInterceptTouchEvent方法。也不是分发给子View处理，而是会直接在Activity中的onTouchEvent中消化事件。对于Activity中的onTouchEvent方法来说，无论它的返回值是什么，这都是事件传导的终点。

- （2）MyChildLayout 的 dispatchTouchEvent方法 return true;

![case_error_true.png](/src/imgs/1412/03_TouchEvent/case_error_true.png)

可以看出，这种情况下，事件不会分发只会限制在 MyChildLayout 的 dispatchTouchEvent方法中直到消失，既不自己处理也不分给子View。Activity也不会处理到。

###### 2.2 再来看看在MyChildLayout调用super.dispatchTouchEvent方法的情况下（MyChildLayout中其方法以及其MyButton中的方法均返回默认值）:

- (1)比如：一开始我是这样写的

```java
public boolean dispatchTouchEvent(MotionEvent ev) {
    super.dispatchTouchEvent(ev);
    switch (ev.getAction()) { ... }
    return false;
}
```

结果发现打出的Log是这样的：

![case00_1.png](/src/imgs/1412/03_TouchEvent/case00_1.png)

注意图中选中的部分是MyButton- dispatchTouchEvent竟然位于MyLayout- onInterceptTouchEvent的下面

- (2)然后我又把super.dispatchTouchEvent(ev)的位置换了一下：

```java
public boolean dispatchTouchEvent(MotionEvent ev) {
    switch (ev.getAction()) { ... }
    super.dispatchTouchEvent(ev);
    return false;
}
```

此时我长按Button,打出的Log为：

![case00_2_1.png](/src/imgs/1412/03_TouchEvent/case00_2_1.png)

如果我点击Button，打出的Log为：

![case00_2_2.png](/src/imgs/1412/03_TouchEvent/case00_2_2.png)

可以看出不仅MyButton的onClick方法没有执行到,onLongClick的方法调用的顺序也混乱了。

其实上面这种写法也不能算是错误，但是这样会让事件传导路线发生混乱。如果想要事件按正确的分发路线分发一定要返回super.dispatchTouchEvent(event)，否则会产生一些奇怪的现象。

所以，虽然MyChildLayout的dispatchTouchEvent的返回值也是false，但是对比默认的分发路径，还是有很大的出入。

> 因此：如果想要拦截一个ViewGroup的事件，不要改变dispatchTouchEvent方法的返回值，可以直接修改onInterceptTouchEvent方法的返回值为true。

- - -

### 接下面我们试着来改变一下事件的分发路线

#### 1.CASE 02 :

- MyChildLayout- onInterceptTouchEvent return true;
- MyChildLayout- onTouchEvent return super.onTouchEvent(event);//false
- MyChildLayout中其方法以及其MyButton中的方法均返回默认值.

> 值得一提的是，和dispatchTouchEvent不同，在这两个方法里不管有没有调用相应的super方法，或者super方法的位置写在哪里，都不会影响事件的分发路线。

来看一下这种情况下的Log：

![case01_01.png](/src/imgs/1412/03_TouchEvent/case01_1.png)

- 可以看到MyChildLayout- onInterceptTouchEvent返回true，也就是拦截了事件，交给它自己的onTouchEvent来处理。。因此事件不会分发到MyButton,理所当然Log中也就看不到MyButton的影子了。
- 但是由于MyChildLayout- onTouchEvent默认返回false，也就是MyChildLayout自己不消化事件，而是向上传递到它的父控件MyLayout。
- 同样MyLayout默认也是不消化事件，继续向上传递到Activity中。由于Activity中的onTouchEvent默认返回true，所以最终会消化事件。
- 同时，Activity作为事件传递的顶层，如果返回值为true表示处理了事件；如果返回值为false，事件也不会继续向上传递了，而是自此`消失`。

#### 2.CASE 02 :

- MyChildLayout- onInterceptTouchEvent return true;
- MyChildLayout- onTouchEvent return true;
- MyChildLayout中其方法以及其MyButton中的方法均返回默认值.

来看一下这种情况下的Log：

![case01_02.png](/src/imgs/1412/03_TouchEvent/case01_2.png)

- 同上：MyChildLayout- onInterceptTouchEvent返回true，拦截事件，
- 由于MyChildLayout- onTouchEvent返回true，也就是自己消化了事件，所以不会向再上传递到了。

#### 3.CASE 03 :

- MyButton- onTouchEvent return false;
- MyChildLayout- onTouchEvent return true;
- MyButton和MyChildLayout中的其他方法均返回默认值.

不如我们先来猜想一下ACTION_DOWN事件的分发路径：

- 首先从MyLayout中分发到MyChildLayout，再分发到MyButton，由于MyButton-onTouchEvent return false;也就是并未消化事件。
- 所以事件会向上传递到MyChildLayout的onTouchEvent的方法中，由于这个方法返回了true也就是消化了事件，所以不会再向上传递了。

来看一下这种情况下的Log：

![case_03.png](/src/imgs/1412/03_TouchEvent/case_03.png)

可以看出，ACTION_DOWN的分发路径和我们猜想的是一模一样的。

再来看看ACTION_UP事件。同样是从MyLayout分发下来。但是分发到MyChildLayout时，直接被它的dispatchTouchEvent分发给自己，并交由onTouchEvent处理了。

> 如果某个触摸事件传递到了某个View(or ViewGroup)的onToucEvent方法，但是它返回了false，那么对于这一次操作（点击或者长按之类的操作）而言，这之后所有的触摸事件都不会再分发给这个View了。

> 一次操作是从ACTION_DOWN事件开始的，所以每一ACTION_DOWN事件的发生相当于进行了一次初始化，因此onToucEvent方法返回false并不会影响这个View接受下一次的ACTION_DOWN事件。

所以我们再来看一下这个Log，ACTION_DOWN事件传递到了MyButton的onTouchEvent方法中，但是由于这个方法返回了false，所以ACTION_UP事件不会再分发给MyButton了。

#### 4.CASE 04 :

我们可以接着上面这个情况，再来进一步说明这种分发方式。

- MyButton- onTouchEvent return false;
- MyChildLayout- onTouchEvent return false;
- MyButton和MyChildLayout中的其他方法均返回默认值.

看一下这种情况的Log：

![case_04.png](/src/imgs/1412/03_TouchEvent/case_04.png)

- 选中部分及其上面是ACTION_DOWN的分发流程，下面是ACTION_UP的分发流程。
- 可以看出，由于MyButton- onTouchEvent return false;MyChildLayout- onTouchEvent return false;而MyLayout的onTouchEvent默认也是返回false。最终ACTION_DOWN事件被 ACtivity消化了。
- 并且，ACTION_UP事件直接交由Activity处理。

#### 5.CASE 05 :

来看看最后一种情况

- MyButton- onTouchEvent return false;
- MyChildLayout- onTouchEvent return false;
- Activity- onTouchEvent return false;
- MyButton和MyChildLayout中的其他方法均返回默认值.

这种情况的Log和CASE04的Log是一模一样的：

![case_05.png](/src/imgs/1412/03_TouchEvent/case_05.png)

> 也就是说，和View以及ViewGroup不同。即使Activity的onTouchEvent方法返回了false，它还是会接受到接下来的触摸事件。

- - -

### 如果MyButton中同时存在 onTouchEvent,onClick以及onLongClick三个方法，并且希望每个方法都能执行到的话

- 首先，如果已经重写了 dispatchTouchEvent方法，一定要 return super.dispatchTopuchEvent(event);千万不要直接返回 true。
- 其次，onTouchEvent 方法同样要 return super.onTouchEvent(event);也不能直接返回 true or false。
- 最后，onLongClick方法要返回false。

如下是长按Button之后的Log：

![touch_click_long.png](/src/imgs/1412/03_TouchEvent/touch_click_long.png)

在上文的分析中其实已经提到了：

- onLongClick是在ACTION_DOWN之后ACTION_UP之前触发的。
- 而onClick是在ACTION_UP之后触发的

> 此外,如果在Activity中为Button设置onClickListener，那么只会执行这里的onClick方法，而不会执行Button内部的onClick方法。

- - -
THE END.
