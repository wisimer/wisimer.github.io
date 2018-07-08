---
date: 2014-06-17
layout: default
category: ANDROID
title: Android 使用Viewpager实现主界面的不同页面之间的切换
---

一个主界面上可以通过左右滑动来切换不同的子页面这种布局在Android应用开发中还是经常碰到的。

下面就通过Viewpager来实现这种效果

<!--more-->

#### 1.主界面布局文件，底部是三个图片用于指示当前位置，主体部分是一个ViewPager ####

```java
<?xml version="1.0" enco
ding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/secxml"
    android:layout_width="fill_parent"
    android:layout_height="fill_parent"
    android:background="#eee"
    android:orientation="vertical" >
    <RelativeLayout
        android:id="@+id/sec_bottom"
        android:layout_width="match_parent"
        android:layout_height="55dp"
        android:layout_alignParentBottom="true"
        android:background="@drawable/bottom_bar"
        android:orientation="vertical" >
        <LinearLayout
            android:layout_width="fill_parent"
            android:layout_height="wrap_content"
            android:layout_alignParentBottom="true"
            android:paddingBottom="2dp" >
            <LinearLayout android:id="@+id/secshow1"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_weight="1"
                android:gravity="center_horizontal"
                android:orientation="vertical" >
                <ImageView
                    android:id="@+id/img_address"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:adjustViewBounds="false"
                    android:clickable="true"
                    android:scaleType="matrix"
                    android:src="@drawable/tab_address_pressed" />
                <TextView  
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_gravity="center_horizontal"
                    android:text="头条"
                    android:textColor="#fff"
                    android:textSize="10sp" />
            </LinearLayout>
           <!--第二张图片布局如上-->
           <!--第三张图片布局如上-->
        </LinearLayout>
    </RelativeLayout>
    <LinearLayout
        android:layout_width="fill_parent"
        android:layout_height="wrap_content"
        android:layout_above="@+id/sec_bottom"
        android:layout_alignParentTop="true"
        android:orientation="vertical" >
        <android.support.v4.view.ViewPager
            android:id="@+id/sectap"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="center" >
        </android.support.v4.view.ViewPager>
    </LinearLayout>
</RelativeLayout>
```

#### 2.还有另外三个用于不同页面的布局文件 ####

#### 3.为底部的指示图片添加监听事件，使得点击某一图片即可切换到对应的页面 ####

```java
public class MyOnClickListener implements View.OnClickListener {
  private int index = 0;
  public MyOnClickListener(int i) {
    index = i;
  }
  public void onClick(View v) {
    mTabPager.setCurrentItem(index);
  }
}
```

#### 4.通过LayoutInflater找到三个页面，并将他们存入一个ArrayList<View>中 ####

```java
final ArrayList<View> views = new ArrayList<View>();
views.add(view1);
views.add(view2);
views.add(view3);
```

#### 5.定义一个PagerAdapter 并设置主体部分ViewPager的Adapter ####

```java
PagerAdapter mPagerAdapter = new PagerAdapter() {
  public boolean isViewFromObject(View arg0, Object arg1) {
    return arg0 == arg1;
}

public int getCount() {
  return views.size();
}

public void destroyItem(View container, int position, Object object) {
  ((ViewPager) container).removeView(views.get(position));
}

public Object instantiateItem(View container, int position) {
  ((ViewPager) container).addView(views.get(position));
  return views.get(position);
}
};

mTabPager.setAdapter(mPagerAdapter);
```

#### 6.页面切换监听事件 ####

```java
mTabPager.setOnPageChangeListener(new MyOnPageChangeListener());
  //当滑动切换页面时，底部的指示图片会随之变化
  public class MyOnPageChangeListener implements OnPageChangeListener {
    public void onPageSelected(int arg0) {
      switch (arg0) {
        case 0:
            mTab1.setImageDrawable(getResources().getDrawable(R.drawable.tab_address_pressed));
            mTab2.setImageDrawable(getResources().getDrawable(R.drawable.tab_find_frd_normal));
            mTab3.setImageDrawable(getResources().getDrawable(R.drawable.tab_settings_normal));
        break;
        case 1:
            //第二张图片设为高亮
        break;
        case 2:
            //第三张图片设为高亮
        break;
      }
  }
  public void onPageScrolled(int arg0, float arg1, int arg2) {}

  public void onPageScrollStateChanged(int arg0) {}

}
```

#### 效果图如下： ####

![viewpager](/src/imgs/1406/17_viewpager.gif)

- - -
THE END.
