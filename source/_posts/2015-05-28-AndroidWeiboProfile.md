---
date: 2015-05-28
layout: default
category: ANDROID
title: ScrollView与ListView嵌套仿微博个人主页
---

#### 前言

之前写过一篇文章是关于浮动标签头的实现方式，但是内容只有一个ScrollView,里面并无ListView

下面要通过ScrollView与ListView嵌套来微博个人主页的效果，实现原理和上面这个还是一样的

<!--more-->

#### 这是微博里的个人主页的效果图

![0528_weiboprofile.gif](/src/imgs/1505/0528_weiboprofile.gif)

可以看到顶部有一个head区域，包含头像和姓名之类的信息。中间是一条TabBar，点击不同的tab可以切换显示不同的内容。下面是不同Tab对应的内容。向上滑动时，当TabBar到达屏幕顶部之后便会固定在屏幕顶部，而下拉后则再次随者内容滑动。

- - -

## 代码实现

#### 一、继承ScrollView实现它一个子类ObservableScrollView

由于Android原生的ScrollView并未提供滑动监听的接口，所以要为ObservableScrollView添加一个滑动监听接口，这样才能判断它的滑动距离。

```java
public class ObservableScrollView extends ScrollView {

    public ObservableScrollView(Context context) {
        super(context);
    }
    public ObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public ObservableScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    public interface OnScrollChangedListener {
        public void onScrollChanged(int x, int y, int oldX, int oldY);
    }
    private OnScrollChangedListener onScrollChangedListener;

    public void setOnScrollListener(OnScrollChangedListener onScrollChangedListener) {
        this.onScrollChangedListener = onScrollChangedListener;
    }
    @Override
    protected void onScrollChanged(int x, int y, int oldX, int oldY) {
        super.onScrollChanged(x, y, oldX, oldY);
        if (onScrollChangedListener != null) {
            onScrollChangedListener.onScrollChanged(x, y, oldX, oldY);
        }
    }
}
```

#### 二、由于ListView嵌套在ScrollView中会导致显示内容不全，所以这里也实现一个自定义的ListView

```java
/***
 * 自定义ListView子类，继承ListView
 * @author Administrator
 *
 */
public class FullyListView extends ListView {

	public FullyListView(Context context) {
		super(context);
	}

	public FullyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FullyListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
```

#### 三、接着在Activity中控制TabBar的隐藏与显示

值得注意的是下面的300是Head区域的高度，也就是当ScrollView向上滑动超过300dp后，隐藏的TabBar便会显示，而ScrollView向上滑动不足300dp时则隐藏，这样就形成了一种滑动到顶部便固定的效果

```java
mScrollView.setOnScrollListener(new ObservableScrollView.OnScrollChangedListener() {

		@Override
		public void onScrollChanged(int x, int y, int oldX, int oldY) {
			Log.e("wxpsc", " x = " +x+" y = " +y+" oldX = " +oldX+" oldY = " +oldY);
			if (y >= dpToPx(300)) {				
				mTabTop.setVisibility(View.VISIBLE);
			} else {
				mTabTop.setVisibility(View.GONE);
			}
		}
});
```

最后看一下这种方式实现的效果图：

![0528_weiboprofile_demo.gif](/src/imgs/1505/0528_weiboprofile_demo.gif)

> 实例源码：[https://github.com/whisper92/WeiboProfile](https://github.com/whisper92/WeiboProfile)

- - -
THE END.
