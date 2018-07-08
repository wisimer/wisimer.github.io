---
date: 2014-05-27
title: 自定义View-IndexSideBar
layout: default
category: ANDROID
---

在一些联系人以及国家列表中经常会使用侧边栏字母导航来进行快速索引

如何自定义一个侧边栏字母导航（IndexSideBar）哩？

<!--more-->

####在继承View实现一个子类IndexSideBar

**1.定义用于导航的字母数组**

    String[] index = { "#", "A", "B", "C", "E", "F", "G", "H", "I","J", "K", "L", "M" };//省略剩下的字母

**2.重写onDraw(Canvas cavas)方法，在里面绘制侧边栏导航条**

```java
public void onDraw(Canvas canvas)
{
    //获取IndexSideBar的宽度和高度
    int width=getWidth();
    int height=getHeight();

    //获取单个字母所占的高度
    int singleHeight=height/index.length;
    //循环绘制每一个字母
    for(int i=0;i<index.length;i++)
    {
      if(i==choose)
      {
       paint.setFakeBoldText(true);//如果某个字母是被选中则字体加粗
      }
      /*字母所在的位置的相对x坐标应该是IndexSideBar的宽度的一半减去字体宽度的一半*/
      float xPos=width/2-paint.measureText(index[i])/2;
      float yPos=singleHeight*(i+1);
      canvas.drawText(index[i],xPos,yPos,paint);
      paint.rest();
    }
}
```

![侧边栏字母导航分析](/src/imgs/1405/27_sidebar.png)


**3.重写dispatchTouchEvent实现监听事件的判断**

```java
public void dispatchTouchEvent(MotionEvent event){
      //获取当前点击时间的纵坐标
      float y =event.getY();
      //如果是滑动时，用于记录上次选中的位置
      int oldchoose=choose;
      //根据纵坐标的位置判断索引所在的次序
      int c=y/getHeight()*index.length;
      switch(event.getAction)
      {
        case:MOTIONEVENT.ACTION_UP:
              //如果是抬起状态则设置一个正常的背景色
              setBackgroud(normal_color);
              choose=-1；//每次抬起之后选中的位置都要归为未选状态，即-1
              invalidate();
        break;
        default:
          //如果是其他状态（点击或者滑动），则设置另一个背景色
          setBackground(another_color);
          if(oldchoose!=c)
          {
              /*如果选中位置发生了变化*/
              choose=c;
              invalidate();
          }
        break;
      }
}
```

**4.在IndexListView中定义回调接口**

```java
public interface OnTouchChangeListener{
    public void onTouchChange(String index);
}
public void setOnTouchChangeListener(OnTouchChangeListener onTouchChangeListener){
    this.onTouchChangeListerner = onTouchChangeListerner;
}
```

- - -

#### 使用IndexListView

直接在布局文件中加入标签`<com.wxp.custom.view.IndexSideBar />`

如果要监听Index的改变事件，在Activity中

```java
indexSideBar.setOnTouchChangeListener(new OnTouchChangeListener(){
   public void onTouchChange(String s){
      int index = adapter.getPosition(String.valueOf(s.toUpperCase().charAt(0)));
      listView.setSelection(index);
    }
});
```

或者可以为Activity实现`OnTouchChangeListener`接口;

- - -
THE END.
