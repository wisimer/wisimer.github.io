---
date: 2014-04-25
title: AndroidListView加载不同布局的思路
layout: default
category: ANDROID
---

使用ListView时，对于不同的数据要使用不用的布局来展示，那如何创建并夹在这些不同的布局呢？

## 实现要素 ##

1.首先有多个对应不同Item的不同的布局文件

2.在Adapter中构造多个对应的Holder

3.注意在Adapter中要重写以下两个方法

<!--more-->

```java
public int getItemViewType(int position)
{
//此处根据positon获取item，再获取item的type，并返回
}
public int getViewTypeCount()
{
// 返回listview中的item个数
    return listview.getCount();
}
```

4.重写getView方法

```java
public View getView(int position, View convertView, ViewGroup viewgroup)
{
    UpdateStatusHolder holder_UpdateStatus = null;
    ShareStatusHolder holder_ShareStatus = null;
    if(convertView==null){
        //如果convertView为空
        if (type == 1 || type == 8){
            //根据不同的类型inflate不同的布局，并实例化对应的holder
            convertView = inflater.inflate(R.layout.updatestatus,  viewgroup, false);
            holder_UpdateStatus = new Holder_UpdateStatus();
            convertView.setTag(holder_UpdateStatus);
        } else {
            //其他的type....
            convertView = inflater.inflate(R.layout.sharestatus,  viewgroup, false);
            holder_ShareStatus = new Holder_ShareStatusHolder();
            convertView.setTag(holder_ShareStatus);
        }
    } else {
        //如果convertView不空
        if (type == 1 || type == 8){
            //也要根据type得到不同的holder
            holder_UpdateStatus =(Holder_UpdateStatus)convertView.getTag();
        } else {
            //其他type...
            holder_ShareStatus =(Holder_ShareStatus)convertView.getTag();
        }
    }

   if (type == 1 || type == 8){
   //根据type设置不同的holder的资源
     ...
   } else {...}

  return convertView;
}
```

- - -
THE END.
