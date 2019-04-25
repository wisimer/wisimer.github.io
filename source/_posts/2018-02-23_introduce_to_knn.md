---
title: KNN 简介
date: 2018-02-23
categories: ML
tags: [KNN]
---

#### 一、前言

k-近邻算法（kNN，k-NearestNeighbor），是一种`监督分类`算法，是最简单的机器学习分类算法之一。其核心思想在于用`距离目标最近的 k 个样本数据的分类`来代表目标的分类（这k个样本数据和目标数据最为相似）。kNN 是一种`惰性学习方法`。

k-近邻算法的缺点是对数据的`局部结构非常敏感`。

<!--more-->

#### 二、算法描述

##### 1. 具体算法过程：

（1）. 计算分类未知数据 x_new 与训练样本集数据 x 的欧氏距离 distance

（2）. 将 distance 递增排序

（3）. 选取 distance 的前 k 个点

（4）. 选取前 k 个点中，出现频率最高的类别 y 作为 x_new的分类

##### 2. k 值的选择

k 值的选择对 KNN 算法的性能有较大影响，选得太小容易过拟合，选得太大容易欠拟合。在实际应用中，一般选取一个区间，采用`交叉验证法`来选取最优的 k 值。

##### 3. KNN 分类决策规则

由于 KNN 属于惰性学习算法，所以并不存在事先的训练过程，决策都是在预测时直接根据决策规则做出的。`KNN 分类的决策规则往往是多数表决`，也就是由输入实例的 k 个邻近的训练实例中的多数决定的。那么为什么多数表决规则可以作为 KNN 的决策规则呢？具体解释如下：

首先设 KNN 分类算法的分类函数是 f(x) ，损失函数是 0-1 损失函数，即：
$$L(Y,f(X)) = \begin{cases}1, &Y\neq f(X) \\ 0, & Y=f(X)\end{cases}$$

那么误分类的概率是： $P(Y \neq f(X))=1-p(Y = f(X))$

对于某一个给定的实例 $x_i$，以及它的最邻近的 k 个训练实例构成集合 N。如果涵盖 N 的区域的类别是 $c_j$，那么误分类率是：

$$\frac{1}{k}\sum_{x_i \in N}I(y_i \neq c_j)=1-\frac{1}{k}\sum_{x_i \in N}I(y_i = c_j)$$

这里的 `函数 I` 是`指示函数`，它是定义在某集合X上的函数，表示其中有哪些元素属于某一子集A。

> 要使误分类率最小就是要使 $\sum_{x_i \in N}I(y_i = c_j)$ 最大，也就是在 N 集合中，属于类别 c 的实例个数越多越好。所以多数表决规则等价于经验风险最小化。

#### 三、代码实现

```python
import numpy as np

def createDataSet():
    group = np.array([[1, 1.1], [1, 1], [0, 0], [0, 0.1]])
    labels = ['A', 'A', 'B', 'B']
    return group, labels

"""
定义knn算法分类器函数
:param inX: 测试数据
:param dataSet: 训练数据
:param labels: 分类类别
:param k: k值
:return: 所属分类
"""
def classify(inX, dataSet, labels, k):
    # shape（m, n）m列n个特征
    dataSetSize = dataSet.shape[0]
    # 计算测试数据和训练数据每个特征的差值
    diffMat = np.tile(inX, (dataSetSize, 1)) - dataSet
    sqDiffMat = diffMat ** 2
    sqDistances = sqDiffMat.sum(axis=1)
    # 欧式距离
    distances = sqDistances ** 0.5  
    # 排序并返回index
    sortedDistIndicies = distances.argsort()  

    classCount = {}
    for i in range(k):
        voteIlabel = labels[sortedDistIndicies[i]]
        classCount[voteIlabel] = classCount.get(voteIlabel, 0) + 1 #default 0

    sortedClassCount = sorted(classCount.items(), key=lambda d:d[1], reverse=True)
    return sortedClassCount[0][0]
```

#### 测试

```python
if __name__ == '__main__':
    dataSet, labels = createDataSet()
    r = classify([0, 0.2], dataSet, labels, 3)
    print(r)
```

输出 `B`

#### 四、k 邻近法的特殊实现：kd 树（k-dimensional tree）

KNN 最简单地实现就是线性扫描，计算出训练集中每个点到输入实例的距离，再排序取前 k 个，采用多数表决判断输入实例的类别。但是当训练集很大时，这种方法的性能很差。为了提高 KNN 的性能，可以考虑使用特殊的存储结构，减少计算距离的次数，提高搜索效率。下面介绍一种 kd 树方法:

##### 1. kd 树的构造

kd 树是一种对 k 维空间中的实例进行存储以便快速检索的树形数据结构。kd 树是二叉树，表示对 k 维空间的不断划分。构造 kd 树相当于用垂直于坐标轴的超平面将 k 维空间划分为一系列 k 维超矩形区域。kd树的每一个节点对应于一个 k 维超矩形区域。kd 树的具体构造过程：

每次选出一个特征上的`中位数(median)`，将这个实例`构造为 kd 树的一个节点`，并对训练集进行切分。然后`依次换一个特征`，再取其中位数构造 kd 树节点。如此迭代，直到每个实例都被作为节点插入到 kd 树中。

之所要要选择中位数作为划分点，是因为这样构造出的 kd 树是平衡的。不过平衡的 kd 树搜索效率不一定是最高的。

**例：**
给定一个二维空间的数据集：T={(2,3),(5,4),(9,6),(4,7),(8,1),(7,2)}，构造一个平衡kd树。

![99f4bf30cea27185947ad02aae40d1ea.png](https://app.yinxiang.com/shard/s13/nl/2429525/cfb378ce-fd7d-43bf-8079-8eccaa25323a//res/775e1afc-0505-4cce-afa5-69ca1831d9a8.png?resizeSmall&width=832)

- 第一步，选择第一个特征对应的坐标轴，也就是横坐标x轴，取所有点中的x坐标的中位数7（点(4,7)），以平面x=7，也就是垂直于x轴的线将空间划分为左右两个子矩形（如上图中得红色直线），左边的x左边都小于7，右边的左边都大于7。
- 第二步，选择第二个特征对应的坐标轴，也就是纵坐标y轴，在两个子空间里分别选取里面的点的y坐标的中位数，将两个子空间再分别划分为两个子空间。左边的空间里的点的y坐标中位数为4，右边中位数为6，划分结果如上图的蓝色直线。
- 第三步，又循环到了x坐标轴，再在各个子空间里选择x左边的中位数，划分子空间，如图的黄色直线。

如此循环直到不能再划分子空间，最终得到的就是上图的特征划分空间。和下图所示的KD树：

![15b2e1a9883ac434f857fe7eab4d8498.png](https://app.yinxiang.com/shard/s13/nl/2429525/cfb378ce-fd7d-43bf-8079-8eccaa25323a//res/a6dfbe35-d092-4441-873b-f7e966afd78f/kd.png?resizeSmall&width=832)


##### 2. kd 树的搜索

对于 KNN 算法，我们要找的是前 k 个距离最近的实例。这里使用 kd 树，先寻找最邻近节点。对于给定一个输入实例，`先从 kd 树的根节点开始搜索`，比较第一个特征，如果输入实例第一个特征值小于根节点的第一个特征值，则向左子树继续搜索，否则向右子树继续搜索。而继续搜索子树的时候，则要`切换到下一个特征比较`。最终到达某一个叶节点，这个叶节点就是要找的`最邻近节点`。如果 k>1，则要`继续寻找剩下的 k-1 个邻近点`。这里比较重要了，继续寻找的时候，采用`回溯的方法`，从刚刚找到的那个节点回退到它的父节点，再寻找和输入实例最靠近的节点。如此迭代，直到找到 k 个邻近点结束。

如果训练样本是随进分布的，kd 树搜索的计算复杂度是 O(log N)。

**例：**
给定一个二维空间的数据集：T={(2,3),(5,4),(9,6),(4,7),(8,1),(7,2)}，找到点T(7.5,3)的最邻近点。

![026dda2dcb40f6fa1ba0191c379bf74c.png](https://app.yinxiang.com/shard/s13/nl/2429525/cfb378ce-fd7d-43bf-8079-8eccaa25323a//res/96869799-3937-4ff2-aa95-90ce721aa598.png?resizeSmall&width=832)

- 第一步，首先在最底层子空间内（黄色线分割的空间）找到点T。以点T 为圆心，过点F(8,1)的圆为范围，最邻近点一定是在这个圆内部。
- 第二步，我们设最短距离为点TF之间的距离。先找F的父节点C，C所在的蓝色空间与圆相交，在C的另一侧找最邻近点，另一侧没有点。
- 第三步，继续找到C的父节点A，A在园内，且TA距离小于TF，更新最短距离为TA。
- 第四步，A的另一侧B的子空间与圆也相交，在B内找最邻近点，发现左右子空间内的点和圆都不相交。
- 所以找到了最邻近点就是A。

- - -

参考：

[1]. 《统计学习方法（李航）》：第3章 k 邻近法

[2]. [k-d tree算法](https://www.cnblogs.com/eyeszjwang/articles/2429382.html)

[3]. [KD tree algorithm](https://www.youtube.com/watch?v=TLxWtXEbtFE)
- - -
THE END.
