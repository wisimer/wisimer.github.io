---
date: 2015-10-13
layout: default
category: ANDROID
title: Android依赖注入框架 Dagger1 深入了解
---

### 一、前言

之前已经介绍了Dagger的简单使用

接下来看看Dagger中还有一些别的常用的注解如何使用

<!--more-->

### 二、@Provides注解

#### 1.对构造方法进行注解是很好用的实现依赖的途径，然而它并不适用于所有情况。

1. 接口（Interface）是没有构造方法的
2. 第三方库提供的类，它们的构造方法不能被注解
3. 有些类需要灵活选择初始化的配置，而不是使用一个单一的构造方法

所以除了直接@Inject构造函数，也可以通过@Provides注解来获取到对象。Dagger要求所有的@Provides必须属于一个Module。

```java
@Module(injects = MainActivity.class)
public class MsgMoudle {
    @Provides
    DraftsBox provideDraftsBox() {
        return new DraftsBox();
    }
}
```

而Module仅仅是一个使用了@Module注解的类。

Module的作用是提供信息，让ObjectGraph知道应该怎样注入所有的依赖。例如，上面这段代码中声明了可注入对象的信息：MainActivity.class

#### 2.限定符注解 @Qualifier

有些时候，单纯类型（指这些基本的@Inject....等等）是不能够满足指定依赖的需求的。

在这种情况下，我们可以添加限定符注释来自定义一个注解， 这种注释本身有一个@Qualifier注释

```java
@Module(injects = MainActivity.class,library = true)
public class MsgMoudle {

    @Provides
    @MyInt(1)
    public int providesMyInt() {
        return 1;
    }
}
```

使用的时候在@Inject下面加上@MsgMoudle.MyInt(1)即可

```java
@Inject
@MsgMoudle.MyInt(1)
int mLength;
```

#### 3.有些情况下， 你需要多个对象实例， 而不是仅仅注入一个对象实例。

这时你可以利用Provider实现， 每次调用Provider的get()函数将返回新的<T>的对象实例。

```java
public class DraftsBox{

	@Inject  Provider<NewMsg> providerNewMsg;
	public void edit(){
		providerNewMsg.get();	//得到对象1
		providerNewMsg.get();	//得到对象2
		//对象1 和 对象2 是两个不同的对象.
	}

}
```

### 二、Dagger中其他一些注解的使用

#### 1.当我们希望不管多少个地方注入DraftsBox这个类，我们只希望拥有一份“DraftsBox”的实例对象（单例）。

那么我们可以用到注解@Singleton 加在 @Provides注解的后面即可

```java
@Provides @Singleton DraftsBox provideDraftsBox(){
	return new DraftsBox();
}
```

@Singleton 注释对Dagger有效， 也只在一个ObjectGraph中生效。 若是有多个ObjectGraph， 则有多个相应的@Singleton对象。

#### 2.延迟注入 Lazy :(即：懒加载, 等到调用的时候才注入)

```java
public class Account{

	@Inject  Lazy<DraftsBox> lazyDraftsBox;
	public void save(){
		lazyDraftsBox.get();//这样就能得到一个DraftsBox对象
	}
}
```


### 三、@Module的一些说明

1. 在@Module中加入一个参数 complete=false， 标记说明该Module为不完整的Module。因为不完整的Module允许缺少对象实例。

2.如果在@Module中已经加入参数injects (即所谓的：注入对象列表绑定),若是这个Module提供的对象绑定， 可能被injects列表中以外的类使用， 可以将改Module标记为library, 以避免出错。

如：

```java
@Module(injects = DraftsBox.class,library = true)
public class DraftsBoxModule{

	@Provides NewMsg provideNewMsg(){
		return new NewMsg();
	}

	@Provides Others provideOthers(){
		return  new Others;
	}

}
```

分析：由于DraftsBox只用到了一个NewMsg的类，而injects列表中也只写了DraftsBox.class。
这样的话，NewMsg这个类提供的其他方法有可能被除了DraftsBox之外的类所用，那么避免报错就要在@Module加上参数library=true

- - -
THE END.
