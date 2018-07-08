---
date: 2015-09-20
layout: default
category: JAVA
title: Java泛型小结
---

#### 一、普通的泛型

比如我们经常使用的List<String> data = new ArrayList<String>;
这里我们直接指定List中存储的是String类型的数据，避免在get的时候还要强制转换。

1.来定义一个简单的泛型：

```java
public class MessageQueue<T> {
    private T mMsg = null;
    public T getMsg() {
        return mMsg;
    }
    public void addMsg(T msg) {
        this.mMsg = msg;
    }
    public void printMsg() {
        System.out.println(this.mMsg);
    }
}
```

<!--more-->

2.测试代码：

```java
public class TypeTest {
    public static void main(String[] args) {
        System.out.println("= = = = = = = = MessageQueue | 基本泛型 = = = = = = = = = ");
        MessageQueue<String> mq= new MessageQueue<String>();
        mq.addMsg("MQ");
        System.out.println(mq.getMsg());
        mq.printMsg();
        MessageQueue<Integer> mqInt= new MessageQueue<Integer>();
        mqInt.addMsg(1);
        System.out.println(mqInt.getMsg());
        mqInt.printMsg();
    }
}
```

3.可以看到运行的结果是：

```
= = = = = = = = MessageQueue | 基本泛型 = = = = = = = = =
MQ
MQ
1
1
```

- - -

#### 二、两个或者多个泛型参数

1.还是来定义含有两个参数的泛型类

```java
public class UserInfo<K,V> {
  private K key;
  private V value;
  public void put(K key,V value) {
    this.key = key;
    this.value = value;
  }

  public K getKey() {
    return key;
  }

  public V getValue() {
    return value;
  }

  public String toString() {
      return this.key.toString() + " | " + value.toString();
  }
}
```

2.测试代码：

```java
public class TypeTest {
    public static void main(String[] args) {
        System.out.println("= = = = = = = = UserInfo | 两个泛型参数 = = = = = = = = = ");
        UserInfo<Integer,String> userInfo = new UserInfo<Integer,String>();
        userInfo.put(2,"wxp");
        System.out.println(userInfo.toString());
    }
}
```

3.看一下运行的结果：

```
= = = = = = = = UserInfo | 两个泛型参数 = = = = = = = = =
2 | wxp
```

第一个参数就是key，第二个参数就是value

- - -

#### 三、泛型通配符

1.定义一个简单的泛型，或者直接使用上面的MessageQueue类，都是可以的

```java
public class MsgModel<T> {
    private T mMsg = null;
    public T getMsg() {
      return mMsg;
    }
    public void addMsg(T msg) {
      this.mMsg = msg;
    }
    @Override
    public String toString() {
      return this.mMsg.toString();
    }
}
```

2.接着定义一个printStringMsgModel方法，这个方法的参数时一个MsgModel类型，但是它的泛型参数只能只能接收String或Object（String的父类）类型的泛型，这个是受限通配符，用<? extends String>表示。

```java
    //受限通配符，限制泛型参数只能接收String或Object类型的泛型
    public static void printStringMsgModel(MsgModel<? extends String> model) {
        System.out.println("model = "+model.toString());
    }
```

3.测试代码：

```java
public class TypeTest {
    public static void main(String[] args) {
        System.out.println("= = = = = = = = MsgModel | 通配符 = = = = = = = = = ");
        MsgModel<String> msgModel = new MsgModel<String>();
        msgModel.addMsg("www");
        printStringMsgModel(msgModel);
    }
}
```

4.运行的结果是：

```
= = = = = = = = MsgModel | 通配符 = = = = = = = = =
model = www
```

- <? extends UserInfo> 表示上边界通配符，参数类型只能是UserInfo或者其子类
- <? super UserInfo> 表示下边界通配符，参数类型只能是UserInfo或者其父类

5.或者也可以这样定义一个printUserInfoMsgModel方法，表示只能接受UserInfo或者其子类的泛型参数

```java
    //受限通配符，限制泛型参数只能接收UserInfo或子类类型的泛型
    public static void printUserInfoMsgModel(MsgModel<? extends UserInfo> model) {
        System.out.println("model = "+model.getMsg().toString());
    }
```

测试一下：

```java
public class TypeTest {
    public static void main(String[] args) {
        UserInfo<Integer,String> userInfo1 = new UserInfo<Integer,String>();
        userInfo1.put(3,"wxp");
        MsgModel<UserInfo> msgModelUserInfo = new MsgModel<UserInfo>();
        msgModelUserInfo.addMsg(userInfo1);
        printUserInfoMsgModel(msgModelUserInfo);
    }
}
```

运行的结果：

```
model = 3 | wxp
```

6.如果是非受限通配符的话直接使用MsgModel<? >这样就可以了

- - -

#### 四.泛型接口

1.定义一个泛型接口

```java
public interface IUploadMsg<T> {
    public void upload(T msg);
}
```

2.定义一个UploadAction类，并实现IUploadMsg接口

```java
    //泛型接口
    public static class UploadAction<T> implements IUploadMsg<T> {
        private T mMsg;
        public void addMsg(T msg) {
            this.mMsg = msg;
            upload(msg);
        }

        @Override
        public void upload(T msg) {
            System.out.println("uploading msg..."+msg.toString());
        }
    }
```

3.测试代码：

```java
public class TypeTest {
    public static void main(String[] args) {
        System.out.println("= = = = = = = = IUploadMsg | 泛型接口 = = = = = = = = = ");
        UploadAction<String> uploadAction = new UploadAction<String>();
        uploadAction.addMsg("xxx");

        UploadIntAction uploadIntAction = new UploadIntAction();
        uploadIntAction.addMsg(4);
    }
}
```

4.看一下运行结果：

```
= = = = = = = = IUploadMsg | 泛型接口 = = = = = = = = =
uploading msg...xxx
uploading msg...4
```

- - -

#### 五、泛型方法

1.定义一个DownloadMsg类，它有一个download方法，可以传入一个泛型参数msg

```java
public class DownloadMsg {
    public static <T> void download(T msg) {
        System.out.println("Downloading msg..."+msg.toString());
    }
}
```

2.测试代码：

```java
public class TypeTest {
    public static void main(String[] args) {
        System.out.println("= = = = = = = = DownloadMsg | 泛型方法 = = = = = = = = = ");
        DownloadMsg.download("ppp");
        DownloadMsg.download(5.0);
    }
}
```

3.看一下运行结果：

```
= = = = = = = = DownloadMsg | 泛型方法 = = = = = = = = =
Downloading msg...ppp
Downloading msg...5.0
```

- - -

#### 六、泛型方法返回泛型实例

1.定义一个普通的泛型类

```java
public class GenerateInfo<T> {
    private T mInfo;
    public void setInfo(T info) {
        mInfo = info;
    }
    public T getInfo() {
        return mInfo;
    }
    public String toString() {
        System.out.println(this.mInfo.toString());
        return mInfo.toString();
    }
}
```

2.定义一个方法，传入泛型参数，返回一个泛型实例

```java
    public static <T> GenerateInfo<T> genInfo(T info) {
        GenerateInfo<T> temp = new GenerateInfo<T>();
        temp.setInfo(info);
        return temp;
    }
```

3.测试代码：

```java
public class TypeTest {
    public static void main(String[] args) {
        System.out.println("= = = = = = = = GenerateInfo | 泛型方法返回泛型实例 = = = = = = = = = ");
        GenerateInfo<String> info = genInfo("wwwxxxppp");
        info.toString();

        UserInfo<String,Integer> userInfo2 = genUserInfo("wxp",5);
        System.out.println(userInfo2.toString());
    }
}
```

4.运行结果：

```
= = = = = = = = GenerateInfo | 泛型方法返回泛型实例 = = = = = = = = =
wwwxxxppp
wxp | 5
```

- - -
THE END.
