---
date: 2014-08-24
title: 桌面插件AppWidget深入理解-基于系统音乐播放器的桌面音乐插件
category: ANDROID
layout: default
---

光有一个空壳不行，桌面插件必须要有后台的应用程序为它提供内容和服务，这样才能真正让桌面插件发挥它的优势。

#### 1.首先来分析AppWidgetProvider中关于插件生命周期的几个方法

> 值得注意的是只有在onReceive(Context context, Intent intent)使用父类的onReceive方法：

```java
public void onReceive(Context context,Intent intent) {
   super.onReceive(context,intent);
}
```

<!--more-->

之后如下所述的几个方法才能被成功调用，否则无效 。

* public void onEnable(Context context):第一个插件添加到桌面时会调用此方法。
* public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds):每向桌面添加一个插件都会调用此方法。
* public void onDeleted(Context context, int[] appWidgetIds):每删除一个插件，都会调用此方法。
* public void onDisabled(Context context):最后一个插件删除之后会调用此方法。

#### 2. 另外，从Manifest.xml文件中对于WppWidgetProvider的注册就知道它本质上是一个广播接收器：

```java
<receiver android:name="com.wxp.made.MediaProvider">
    <intent-filter >
        <action android:name="android.appwidget.action.APPWIDGET_UPDATE"/>
        <action android:name="com.android.music.playstabloganged"/>
        <action android:name="com.android.music.metachanged"/>
    </intent-filter>
    <meta-data android:name="android.appwidget.provider" android:resource="@xml/appwidget_info"/>
 </receiver>
```

所以对于音乐插件的基本思路就有了：后台播放服务每当播放或者暂停音乐时都会发送一个广播，这个广播包含了当前音乐的所有信息：播放状态（播放or暂停），作者，歌曲名，专辑名等等。只要在AppWidgetProvider中接收这个广播就可以相应的更新桌面插件的界面。如上两个<action />就是音乐播放器放送的广播的Action，playstabloganged表示音乐播发状态的变化，metachangde表示歌曲信息的变化。

在onReceive方法中实现接收到广播之后立即更新界面的功能：

```java
@Override
public void onReceive(Context context, Intent intent) {
  super.onReceive(context, intent);
  playData=(ApplicationData)context.getApplicationContext();
  if(intent.getExtras()!=null){
      if (intent.getExtras().getLong("id")!=0) {
        bundle =intent.getExtras();
        playData.setBundle(bundle);
        updateViews(context, bundle);
      }
  }
}
```

代码中playData.setBundle(bundle);用于保存当前播放音乐的信息，以便用户多次添加插件时能够正确更新桌面插件。因此在更新方法中可以直接调用已经保存了的Bundle

```java
@Override
public void onUpdate(Context context, AppWidgetManager appWidgetManager,
  int[] appWidgetIds) {
  playData=(ApplicationData)context.getApplicationContext();
  if(playData.getBundle()!=null){
    updateViews(context, playData.getBundle());
  }
}
```

#### 3.因为只有在播放或者暂停音乐后台服务才会发送广播，所以仅靠接收广播来判断是否有音乐在播放还不能完全解决问题。比如正在播放音乐时，用户向桌面添加了第一个插件，由于此时并未触发播放/暂停事件，也就不会发送广播，所以插件并不会接收到广播而更新界面。因此需要在onEnabled方法中开启一个新的服务来调用远程服务以获取当前音乐的状态。

```java
Intent intent=new Intent(context, EnableService.class);
context.startService(intent);
```

在EnableService中会使用AIDL来获取后台播放服务的一些接口

关于AIDL的使用不再赘述,可参照另一篇文章:Service初探-AIDL简单实现

在ServiceConnection的onServiceConnected方法中获取到音乐信息在再封装成bundle并发送一个广播以便插件更新界面。

```java
try {
    boolean mplaying = mIMediaPlaybackService.isPlaying();
    Long mauid=mIMediaPlaybackService.getAudioId();
    String mtrack=mIMediaPlaybackService.getTrackName();
    String malbum=mIMediaPlaybackService.getAlbumName();
    String martist=mIMediaPlaybackService.getArtistName();

    Bundle bundle=new Bundle();
    bundle.putLong("id", mauid);
    bundle.putBoolean("playing", mplaying);
    bundle.putString("track", mtrack);
    bundle.putString("album", malbum);
    bundle.putString("artist", martist);

    Intent intent=new Intent("com.android.music.metachanged");
    intent.putExtras(bundle);

    sendBroadcast(intent);
} catch (RemoteException e) {
    e.printStackTrace();
}
```

#### 4.而桌面插件触发音乐播放或者暂停事件的功能则需要通过RemoteView和PendingIntent来实现,代码如下：

```java
public static final String TOGGLEPAUSE_ACTION = "com.android.music.musicservicecommand.togglepause";
ComponentName serviceName=new ComponentName("com.android.music","com.android.music.MediaPlaybackService");
Intent intent= new Intent(TOGGLEPAUSE_ACTION);
intent.setComponent(serviceName);
PendingIntent pendingIntent= PendingIntent.getService(context, 0 /* no requestCode */, intent, 0 /* no flag */);
RemoteViews views.setOnClickPendingIntent(R.id.control_play, pendingIntent);
```

- - -
THE END.
