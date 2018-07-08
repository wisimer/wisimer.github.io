---
title: Java 中的 IncompatibleClassChangeError 错误
layout: default
date: 2017-02-23
category: JAVA
---

最近发现了一个从来没遇到过的崩溃 `IncompatibleClassChangeError ` ,出现这种错误的原因可能是因为jar包的代码发生了一些无法执行 `二进制兼容` 的变化。最有可能的是将非静态的成员变量或者方法变成了静态的，还有将私有的成员变量和方法变成了共有的。并且引用jar包的客户端没有重新编译源码。

IncompatibleClassChangeError 这个错误只是二进制兼容问题其中的一种错误。

<!--more-->

#### 一、如下几种情况是Java支持的二进制兼容的变化：


- Reimplementing existing methods, constructors, and initializers to improve performance.

- Changing methods or constructors to return values on inputs for which they previously either threw exceptions that normally should not occur or failed by going into an infinite loop or causing a deadlock.

- Adding new fields, methods, or constructors to an existing class or interface.

- Deleting private fields, methods, or constructors of a class.

- When an entire package is updated, deleting default (package-only) access fields, methods, or constructors of classes and interfaces in the package.

- Reordering the fields, methods, or constructors in an existing type declaration.

- Moving a method upward in the class hierarchy.

- Reordering the list of direct superinterfaces of a class or interface.

- Inserting new class or interface types in the type hierarchy.

#### 二、可能不兼容的二进制变化

- 从非abstract类变为abstract类（注意从abstract类变为非abstract类不会引起二进制兼容问题）
- 从非final类变为final类 （注意从final类变为非final类不会引起二进制兼容问题）
- 从public类变为非public类 （注意从非public类变为public类不会引起二进制兼容问题）
- 改变类的类型参数的约束
- 删除一个类的非private构造函数或者非private成员变量
- 非static的函数或者成员变量变成static，反之亦然
- 其他还有好多种可以参考下面的文档。
- - -

参考文档 ： 
[http://docs.oracle.com/javase/specs/jls/se7/html/jls-13.html](http://docs.oracle.com/javase/specs/jls/se7/html/jls-13.html)

这里有一个工具可以检测二进制变化 ：
[https://github.com/lvc/japi-compliance-checker](https://github.com/lvc/japi-compliance-checker)

- - -
THE END.