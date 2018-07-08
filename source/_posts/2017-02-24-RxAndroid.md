---
title: RxAndroid
layout: default
date: 2017-02-24
tags: Rx
category: ANDROID
---

#### Observer 

创建Observer :
```java
Observer observer = new Observer<Model>() {
    @Override
    public void onComplete() {
        ......
    }

	@Ovrride
	public void onError(Throwable e) {
		......
	}

	@Override
	public void onNext(Model model) {
		......
	}
};
```

<!--more-->

#### Observable

创建Observable  : 
```java
Observable observable = Observable.create(new Observable.OnSubscribe<Model>() {
	@Override
	public void call(Subcriber<? extends Model> subcriber) {
		......
		subcriber.onComplete();
	}
});
```

其他方式创建Observable :
```java
Observable.from(model...);
Observable.just(model);
```

OnSubscribe :
```java
public interface OnSubscribe<T> extends Action1<Subscriber<? super T>> {
    // cover for generics insanity
}
```

#### subcribe()

```java
observable.subcribe(obsever);
```

#### Action0 and Action1

自定义Subcriber : 
```java
Action1<Model> nextAction= new Action1<Model>() {
	@Override
	public void call(Model model) {
		......
	}
}

Action0 completeAction= new Action0() {
	@Override
	public void call() {
		......
	}
}

observable.subscribe(nextAction);
//observable.susbcribe(nextAction,errorAction);
//observable.subscribe(nextAction,errorAction,completeAction);
```

#### Scheduler

线程控制
```java
Observable
	.from(new String[]{"HE", "LLO"})
	.subscribeOn(Schedulers.io())
	.observeOn(AndroidSchedulers.mainThread())
	.subscribe(new Action1<String>() {
	    @Override
	    public void call(String s) {
	        android.util.Log.d(TAG, "loadNewProductData action1 : " + s);
	    }
	});
```

> Observable 通过 `subscribeOn` 来指定事件产生的线程，也就是指定创建Observable的线程，用 `observeOn` 指定 Subscriber 所运行的线程，也就是指定回调函数执行的线程。 

- Schedulers.immediate(): 直接在当前线程运行，相当于不指定线程。这是默认的 Scheduler。
- Schedulers.newThread(): 总是启用新线程，并在新线程执行操作。
- Schedulers.io(): I/O 操作（读写文件、读写数据库、网络信息交互等）所使用的 Scheduler。行为模式和 newThread() 差不多，区别在于 io() 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率。不要把计算工作放在 io() 中，可以避免创建不必要的线程。
- Schedulers.computation(): 计算所使用的 Scheduler。这个计算指的是 CPU 密集型计算，即不会被 I/O 等操作限制性能的操作，例如图形的计算。这个 Scheduler 使用的固定的线程池，大小为 CPU 核数。不要把 I/O 操作放在 computation() 中，否则 I/O 操作的等待时间会浪费 CPU。
- 另外， Android 还有一个专用的 AndroidSchedulers.mainThread()，它指定的操作将在 Android 主线程运行。

#### 变换

>所谓变换，就是将事件序列中的对象或整个序列进行加工处理，转换成不同的事件或事件序列

##### map()
```java
Observable
	.just(-1)
	.map(new Func1<Integer, String>() {
	    @Override
	    public String call(Integer integer) {
	        ......
	        return String.valueOf(integer);
	    }
	})
	.subscribeOn(Schedulers.computation())
	.observeOn(AndroidSchedulers.mainThread())
	.subscribe(new Action1<String>() {
	    @Override
	    public void call(String s) {
            ......
	        android.util.Log.d(TAG, "loadNewProductData action1 : " + s);
	    }
	});
```

可以看到这里把传入的Integer对象转换成了String对象，并在Observer的回调函数中接收了String对象，这就是上面说的变换。

> 注意这里 `map()` 里面的时间还是发生在当前线程，如果是在主线程的话可能会导致线程阻塞。需要使用上面的 `Schedulers` 来指定运行的线程。


##### flatMap()

```java
Observable
	.from(new String[]{"HE", "LLO"})
	.flatMap(new Func1<String, Observable<Character>>() {
	    @Override
	    public Observable<Character> call(String str) {
	
	        Character[] characters = new Character[str.length()];
	        for (int i = 0; i < str.length(); i++) {
	            characters[i] = str.charAt(i);
	        }
	        return Observable.from(characters);
	    }
	})
	.subscribe(new Action1<Character>() {
	    @Override
	    public void call(Character c) {
	        android.util.Log.d(TAG, "subscribeAction1 : " + c);
	    }
	});
```

这里的需求是把String数组变换成String,在把String变换成Character

> flatMap()方法参数Func1的返回值是一个Observable，相当于用String数组里的每个String再构造了一个Observable，然后传递给Subscriber里的Action，这里就接收到拆开之后的每个Character了。

`map()` 方法相当于一对一的转换，而 `flatMap()` 有点类似于多（一）对多的转换。

参考：[http://gank.io/post/560e15be2dca930e00da1083](http://gank.io/post/560e15be2dca930e00da1083)

- - -
THE END.