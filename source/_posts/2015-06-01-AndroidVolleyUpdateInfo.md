---
date: 2015-06-01
layout: default
category: ANDROID
title: 使用Volley获取fir.im服务器端应用的最新版本
---

> 儿童节快乐。

#### 一、首先将应用发布到fir.im上。可以得到他的APP ID，我这里的APP ID为`556c487d210234f16a000350`

在浏览器中输入[http://fir.im/api/v2/app/version/556c487d210234f16a000350](http://fir.im/api/v2/app/version/556c487d210234f16a000350),可以看到输出的应用信息

```
{"name":"Timo","version":"1","changelog":"beta0.1","versionShort":"1.0","installUrl":"http://fir.im/api/v2/app/install/556c487d210234f16a000350?token=f3b596a0085311e595c0e761ac19d16b4db1b3ff","update_url":"http://fir.im/timo"}
```

#### 二、在Android Studio中使用GsonFormat自动生成上面json字符串的实体类

> `GsonFormay`的主页： [https://github.com/zzz40500/GsonFormat](https://github.com/zzz40500/GsonFormat)

<!--more-->

最终生成的实体类UpdateInfo如下：

```java
    class UpdateInfo {
        /**
         * installUrl : http://fir.im/api/v2/app/install/556c487d210234f16a000350?token=f3b596a0085311e595c0e761ac19d16b4db1b3ff
         * update_url : http://fir.im/timo
         * name : Timo
         * changelog : beta0.1
         * versionShort : 1.0
         * version : 1
         */
        private String installUrl;
        private String update_url;
        private String name;
        private String changelog;
        private String versionShort;
        private String version;

        public void setInstallUrl(String installUrl) {
            this.installUrl = installUrl;
        }

        public void setUpdate_url(String update_url) {
            this.update_url = update_url;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setChangelog(String changelog) {
            this.changelog = changelog;
        }

        public void setVersionShort(String versionShort) {
            this.versionShort = versionShort;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getInstallUrl() {
            return installUrl;
        }

        public String getUpdate_url() {
            return update_url;
        }

        public String getName() {
            return name;
        }

        public String getChangelog() {
            return changelog;
        }

        public String getVersionShort() {
            return versionShort;
        }

        public String getVersion() {
            return version;
        }

    }
```

#### 三、接着开始编写自定义的GsonRequest

1. GsonRequest继承自Request类，并且同样提供了两个构造函数。

2. 在parseNetworkResponse()方法中，先是将服务器响应的数据解析出来，然后通过调用Gson的fromJson方法将数据组装成对象。

3. 在deliverResponse方法中将最终的数据进行回调。

```java
public class GsonRequest<T> extends Request<T> {
    private final Response.Listener<T> mListener;
    private Gson mGson;
    private Class<T> mClass;
    public GsonRequest(int method, String url, Class<T> clazz, Response.Listener<T> listener,Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mGson = new Gson();
        mClass = clazz;
        mListener = listener;
    }

    public GsonRequest(String url, Class<T> clazz, Response.Listener<T> listener,Response.ErrorListener errorListener) {
        this(Method.GET, url, clazz, listener, errorListener);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,HttpHeaderParser.parseCharset(response.headers));
            return Response.success(mGson.fromJson(jsonString, mClass),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            Log.e("wxpinfo","parseNetworkResponse error : "+error);
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);
    }
}

```

#### 四、接着就可以使用GsonRequest了

1. 首先要获取本地应用的版本：

```java
public static String getLocalVersionName(Context context) {
    String versionName = "";
    try {
        // 包名改为自己应用的包名即可
        versionName = context.getPackageManager().getPackageInfo(
                "timo.wxp.com.timoren", 1).versionName;
    } catch (PackageManager.NameNotFoundException e) {
        e.printStackTrace();
    }
    Log.e("wxpinfo", "loaclversionName" + versionName);

    return versionName;
}
```

2. 接着定义一个volleyGetUpdateInfo方法获将服务器最新应用的版本和本地版本进行对比：

```java
public static void volleyGetUpdateInfo(final Context context) {
    String APP_URL = "http://fir.im/api/v2/app/version/556c487d210234f16a000350";
    RequestQueue mQueue = Volley.newRequestQueue(context);
    GsonRequest<UpdateInfo> gsonRequest = new GsonRequest<UpdateInfo>(
            APP_URL, UpdateInfo.class,
            new Response.Listener<UpdateInfo>() {
                @Override
                public void onResponse(UpdateInfo updateInfo) {
                    if (Float.valueOf(getLocalVersionName(context))  < Float.valueOf(updateInfo.versionShort) ) {
                        Toast.makeText(context,"NEED UPDATE",Toast.LENGTH_SHORT).show();
                    } else if (Float.valueOf(getLocalVersionName(context))  > Float.valueOf(updateInfo.versionShort) ){
                        Toast.makeText(context,"本地大于服务器",Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context,"版本相同",Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("wxpinfo", "error --> " + error.getMessage(), error);
        }
    });
    mQueue.add(gsonRequest);
}
```

如果要判断本地应用和服务器最新应用的版本大小只要调用volleyGetUpdateInfo这个方法即可。

- - -
THE END.
