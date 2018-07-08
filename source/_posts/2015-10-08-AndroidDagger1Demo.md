---
date: 2015-10-08
layout: default
category: ANDROID
title: Android依赖注入框架 Dagger1
---


### 一、前言

#### 1. 依赖

如果在 Class A 中，有 Class B 的实例，则称 Class A 对 Class B 有一个依赖。例如下面类 Human 中用到一个 Father 对象，我们就说类 Human 对类 Father 有一个依赖。

```java
public class Human {
    ...
    Father father;
    ...
    public Human() {
        father = new Father();
    }
}
```

<!--more-->

仔细看这段代码我们会发现存在一些问题：

(1). 如果现在要改变 father 生成方式，如需要用new Father(String name)初始化 father，需要修改 Human 代码；

(2). 如果想测试不同 Father 对象对 Human 的影响很困难，因为 father 的初始化被写死在了 Human 的构造函数中；

(3). 如果new Father()过程非常缓慢，单测时我们希望用已经初始化好的 father 对象 Mock 掉这个过程也很困难。

#### 2. 依赖注入

上面将依赖在构造函数中直接初始化是一种 Hard init 方式，弊端在于两个类不够独立，不方便测试。我们还有另外一种 Init 方式，如下：

```java
public class Human {
    ...
    Father father;
    ...
    public Human(Father father) {
        this.father = father;
    }
}
```

上面代码中，我们将 father 对象作为构造函数的一个参数传入。在调用 Human 的构造方法之前外部就已经初始化好了 Father 对象。像这种非自己主动初始化依赖，而通过外部来传入依赖的方式，我们就称为依赖注入。
现在我们发现上面 1 中存在的两个问题都很好解决了，简单的说依赖注入主要有两个好处：

(1). 解耦，将依赖之间解耦。

(2). 因为已经解耦，所以方便做单元测试，尤其是 Mock 测试。

- - -

在代码中，许多对象在使用前都需要初始化，如果都是手写依赖注入的代码有时候会比较麻烦。

> Dagger 的用途就是：让你不需要手动初始化对象，完全自动注入对象。

换句话说，任何对象声明完了就能直接用。

Dagger是通过ObjectGraph(对象图表)来管理或者组织依赖关系的。所以要记得创建ObjectGraph类并执行inject()方法并将当前所在的类作为参数传入

### 二、Dagger的基本使用

#### 1.初始化对象

通过在NewMsg的构造函数之前添加一个 `@Inject` 注解。Dagger就会在需要的时候找到这个标记的构造函数，并获取它的对象。

```java
public class NewMsg {

    public String title;
    public String content;
    @Inject
    public NewMsg(){

    }

    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{").append("\"title\":\"").append(title).append("\",").append("\"content\":\"").append(content).append("\"}");
        return sb.toString();
    }
}
```

(1). 如果你@Inject fields却没有@Inject构造函数，Dagger就会使用一个存在的无参构造函数，若没有@Inject构造函数，就会出错。

(2). 需要注意的是，如果构造函数含有参数，Dagger会在构造对象的时候先去获取这些参数，所以你要保证这些参数的构造方法也有@Inject标记，或者能够通过@Provides注解来获取到。

#### 2.@Inject 注入

将一个 NewMsg 对象注入到MainActivity

```java
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ObjectGraph objectGraph;

    @Inject
    NewMsg newMsg;
    @Inject
    DraftsBox mDraftsBox;

	......
}
```

#### 3.ObjectGraph初始化

创建ObjectGraph类并执行inject()方法并将当前MainActivity作为参数传入， NewMsg 的对象就被注入到了MainActivity中。

```java
......

@Inject
NewMsg newMsg;

@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);
	Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
	setSupportActionBar(toolbar);

	if (objectGraph == null) {
		objectGraph = ObjectGraph.create(new MsgMoudle());
		objectGraph.inject(this);
	}
}

......
```

这样，就成功使用Dagger将一个NewMsg对象注入到MainActivity中并可正常使用了。

- - -
THE END.
