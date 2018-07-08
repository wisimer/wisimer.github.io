---
layout: default
title: RecyclerView Preview
date: 2015-08-12
category: ANDROID
---

#### RecyclerView的配置与基本使用

1.添加依赖

```
dependencies {
    compile fileTree(dir: 'libs', include: ['*.jar'])
    compile 'com.android.support:appcompat-v7:22.2.0'
    compile 'com.android.support:recyclerview-v7:21.0.+'
}
```

<!--more-->

2.定义每个item的布局

```xml
<? xml version="1.0" encoding= "utf-8"?>
<LinearLayout xmlns:android= "http://schemas.android.com/apk/res/android"
    xmlns: tools="http://schemas.android.com/tools"
    android :layout_width="match_parent"
    android :layout_height="48dp"
    android :background="#ffff7299"
    android :gravity="center_vertical"
    android :orientation="vertical"
    android :padding="6dp"
    tools :context=".MyActivity">

    <TextView
        android :id="@+id/id_item_name"
        android :layout_width="wrap_content"
        android :layout_height="match_parent"
        android :background="#303339"
        android :padding="6dp"
        android :text="Horizontal"
        android :textSize="18sp" />
</LinearLayout>
```

3.定义一个MyApapter

```java
public class MainAdapter<T> extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {

    private List<T> mData = null;
    public MainAdapter(List< T> data) {
        mData = data;
    }
    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent , int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout. main_recydemo_item , null);
        MainViewHolder mainViewHolder = new MainViewHolder(view);
        return mainViewHolder ;

    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, final int position) {
        holder.mDemoNameTv .setText(mData.get(position).toString()) ;
        holder. mDemoNameTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnRecyclerItemClickListener.onClick(v, mData.get(position ));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MainViewHolder extends RecyclerView.ViewHolder {
        public TextView mDemoNameTv;
        public MainViewHolder(View itemView) {
            super (itemView);
            mDemoNameTv = (TextView) itemView.findViewById(R.id. id_item_name);

        }
    }

    private OnRecyclerItemClickListener< T> mOnRecyclerItemClickListener = null;
    public void setOnRecyclerItemClickListener (OnRecyclerItemClickListener listener) {
        this .mOnRecyclerItemClickListener = listener;
    }
    public interface OnRecyclerItemClickListener< T> {
        void onClick(View view , T item);
    }
}
```

4.在Activity中使用

```java
RecyclerView recyclerView = (RecyclerView) findViewById(R.id. id_main_recy);
// 创建一个线性布局管理器
LinearLayoutManager layoutManager = new LinearLayoutManager(this );

layoutManager.setOrientation(LinearLayoutManager. HORIZONTAL);
// 设置布局管理器
recyclerView.setLayoutManager(layoutManager) ;

List<String> data = new ArrayList<>();
for (String it : mRecyDemos) {
    data.add(it);
}
MainAdapter<String> mainAdapter = new MainAdapter<>(data) ;
// 设置Adapter
recyclerView.setAdapter(mainAdapter) ;
mainAdapter.setOnRecyclerItemClickListener( new MainAdapter.OnRecyclerItemClickListener<String>() {
    @Override
    public void onClick(View view, String item) {
        Toast.makeText(MainActivity. this,"Click : "+item ,Toast.LENGTH_SHORT).show() ;
    }
});
```

5.点击事件是通过定义一个回调接口来实现的

```java
private OnRecyclerItemClickListener< T > mOnRecyclerItemClickListener = null;
public void setOnRecyclerItemClickListener (OnRecyclerItemClickListener listener) {
    this . mOnRecyclerItemClickListener = listener;
}
public interface OnRecyclerItemClickListener< T > {
    void onClick (View view , T item);
}
```


- - -
THE END.
