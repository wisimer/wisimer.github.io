---
title: Logistic回归算法
date: 2018-05-18
categories: 机器学习
tags: [Logistic回归]
---

#### 一、前言

Logistic回归虽然名字上是叫回归，但其实它是一种分类算法。Logistic回归也在一些文献中也称为logit回归、最大熵分类(MaxEnt)或对数线性分类器。

“回归”的意思就是要找到最佳拟合参数，其中涉及的数学原理和步骤如下：

1. 需要一个合适的分类函数来实现分类。可以使用单位阶跃函数或者Sigmoid函数。
2. 用 `代价函数` 来表示 `预测值h(x)` 与 `实际值y` 的偏差 `(h−y)`。要使得回归最佳拟合，那么偏差要尽可能小（偏差求和或取均值）。
3. 记J(ω)表示回归系数取ω时的偏差，那么求最佳回归参数ω就转换成了求J(ω)的最小值。可以使用梯度下降法求回归参数ω。

所以，接下来就围绕这几个步骤进行展开。

<!--more-->

#### 二、分类函数

1.假设要实现二分类，那么可以找一个函数，根据不同的特征变量，输出0和1，并且只输出0和1，这种函数在某个点直接从0跳跃到1，如图：

![classifyfunction](/src/imgs/1805/0515_classifyfunction.png)

但是这种函数处理起来，稍微有点麻烦。

2.我们选择另外一个连续可导的函数，也就是Sigmoid函数,函数的公式如下：

$\widehat{y}=h(z)=\frac{1}{1+e^{-z}} （\widehat{y}就表示预测值也就是分类结果）$


这个函数的特点是，当z=0时，h(z)=0.5，而z越大，h(z)越接近1，z越小，h(z)越接近0。这个函数很像阶跃函数，当z>0，就可以将数据分入1类；当z<0，就可以将数据分入0类。函数图如下：

![sigmoid](/src/imgs/1805/0515_sigmidfunction.png)

确定了分类函数，接下来，我们将Sigmoid函数的输入记为z，那么输入特征变量为：

$z=w_0x_0+w_1x_1+...+w_nx_n+b=w^Tx+b$

则分类函数：$\widehat{y}=h(z)=\frac{1}{1+e^{-z}}= \frac{1}{1+e^{-w^Tx+b}}$

向量x是特征变量，是输入数据，向量w,b是回归系数是特征。之后的事情就是如何确定最佳回归系数ω(w0,w1,w2,...,wn,b)。

采用Sigmoid函数实际上就是将原始的输入x向量和某一合适的参数w向量相乘，得出的值作为sigmoid函数的输入，最终是的输出保持在0和1这两个值中的一个，从而达到分类的要求。

#### 三、损失函数和代价函数

1.损失函数（Loss function）

首先要知道损失函数是应用于单独一个训练样本的。而下面要说的代价函数则是针对整个训练集。

为了更好的进行凸优化，我们这里直接给出一个适合Logistic函数的损失函数：

$L(\widehat{y},y) = -[ylog\widehat{y}+(1-y)log(1-\widehat{y})]$

来看一下这个损失函数的特点：

- 当y=1时，`$ylog\widehat{y}$` 这个部分的值是$log\widehat{y}，\widehat{y}表示预测分类$，`$(1-y)log(1-\widehat{y})$` 这个部分的值是0。所以最终的损失函数是：`$L(\widehat{y},y)=-log\widehat{y}$`，而我们要求损失函数越小越好，所以也就是要求 $-(log\widehat{y})$ 越小越好，也就是要求 $\widehat{y}$ 越大越好。而 $\widehat{y}$和y一样，范围都是在0-1之间，所以 $\widehat{y}$ 越接近于1（y=1），损失函数越小。
- 当y=0时，`$ylog\widehat{y}$` 这个部分的值是$0，\widehat{y}表示预测分类$，`$(1-y)log(1-\widehat{y})$` 这个部分的值是 $log(1-\widehat{y})$。所以最终的损失函数是：`$L(\widehat{y},y)=-(1-y)log(1-\widehat{y})$`，而我们要求损失函数越小越好，所以也就是要求 $-[(1-y)log(1-\widehat{y})]$ 越小越好，也就是要求 $\widehat{y}$ 越小越好，所以 $\widehat{y}$ 越接近于0（y=0），损失函数越小。

2.代价函数（Cost function）或者叫 `成本函数`。

根据上面的损失函数可以得到整个训练集的代价函数：

$J(w,b)=\frac{1}{n}\sum_{i=1}^{n}{L({\widehat{y}}^i,y^i)}=-\frac{1}{n}\sum_{i=1}^{n}{[y^ilog{\widehat{y}}^i+(1-y^i)log(1-{\widehat{y}}^i)]}$

由于损失函数越小，预测效果越好。所以代价函数也是越小越好。由于代价函数是一个凸函数，可以采用梯度下降法求到使得代价函数最小的时候的参数值(w,b)。

#### 四、梯度上升/梯度下降

要使得代价函数越小越好就是要 $\frac{1}{n}\sum_{i=1}^{n}{[y^ilog{\widehat{y}}^i+(1-y^i)log(1-{\widehat{y}}^i)]}$ 越大越好。为求最大值，这里采用梯度上升的方法。

代价函数J(w,b)的梯度是：$\nabla J(w,b) = (\frac{\partial J(w,b)}{\partial w}, \frac{\partial J(w,b)}{\partial b}) \: (1)$

梯度上升法中，梯度算子沿着函数增长最快的方向移动（移动方向），如果移动大小为α（步长），那么梯度上升法的迭代公式是:

$w:=w+\alpha \nabla_w J(w,b)$

再将(1)式带入上式得：

$$

- - -

参考：

1. [广义线性模型(Generalized Linear Models)](http://scikit-learn.org/stable/modules/linear_model.html)
2. [Python3《机器学习实战》学习笔记（六）：Logistic回归基础篇之梯度上升算法](https://zhuanlan.zhihu.com/p/28922957)
3. [机器学习之Logistic回归与Python实现](https://blog.csdn.net/moxigandashu/article/details/72779856)
4. [机器学习实战笔记5(logistic回归)](https://blog.csdn.net/lu597203933/article/details/38468303)
5. [Logistic 回归损失函数](https://github.com/Kivy-CN/dlai-notes/blob/master/1.nndl/2.3.md)
6. [Logistic分类函数](https://juejin.im/post/5a4e21b46fb9a01ca267474d)

- - -
THE END.
