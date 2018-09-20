---
title: 感知机 简介
date: 2018-02-19
categories: ML
tags: [Perceptron]
---

#### 一、感知机模型

感知机（perceptron）是一种`二分类的线性分类模型`，它是一种监督式学习算法。感知机的输入为实例的特征向量，输出为实例的类别（取+1和-1）。感知机对应于输入空间中将实例划分为两类的分离超平面 S。感知机旨在求出该超平面 S，为求得超平面导入了基于误分类的损失函数，利用梯度下降法对损失函数进行最优化（最优化）。

感知机也是 `Neural Networks（神经网络）` 和 `SVM（支持向量机）` 的基础，它是一个单层神经网络。

`感知机的模型`如下：

$$f(x)=sign(w \cdot x+b)$$

其中 w 叫做权值（weight）或权值向量（weight vector），b 叫做偏置（bias），sign 是`符号函数`，即：

$$\operatorname{sign}(x) = \begin{cases} +1 & x \geq 0 \\ -1 & x < 0 \end{cases}.$$

而分割超平面则为：

$$w \cdot x+b = 0$$

<!--more-->

#### 二、感知机的损失函数

##### 1.这个损失函数怎么得到呢？

我们先考虑一下，正常最直接的想法就是损失函数嘛，直接用误分类的点的个数来表示不就好了吗，这样误分类的点越少，损失函数的值不就越小嘛。但是这样的函数显然是离散的，不是参数w和b的可导函数，不易优化。

然后再考虑一下所有误分类的点到分割超平面的距离之和作为损失函数，这样的函数貌似是连续可导的。那可行吗？实际上，确实感知机也是采用的这种损失函数。

（1）为此，我们先考察任意某一点到超平面的距离：

$$\frac{1}{||w||}|w \cdot x_0+b| \tag{2.1}$$

这里的 $||w||$ 是 w 的 $L_2$ 范式，$|w \cdot x_0+b|$表示绝对值。

（2）其次，对于任意一个误分类的点 $(x_i,y_i)$ 来说，

$$-y_i(w \cdot x_i+b) < 0$$

（3）因此，误分类的点 $x_i$ 到超平面的距离可以写作：

$$-\frac{1}{||w||}y_i(w \cdot x_i+b)$$

这样就把公式2.1的绝对值去掉了，而 $y_i$ 的取值只有 +1 或 -1，也不影响最终结果大小。

（4）最终，设误分类点得集合为 M ，则所有误分类的点到超平面的距离之和为：

$$-\frac{1}{||w||} \sum_{x_i \in M}{y_i(w \cdot x_i+b)}$$

不考虑 $-\frac{1}{||w||} $ ，就可以得到感知机的损失函数了：

$$L(w,b)=-\sum_{i=1}^{M} {y_i(w*x_i+b)}$$

显然，损失函数L(w,b)是非负的。如果没有误分类点，那么L(w,b)为0，误分类点数越少，L(w,b)值越小。一个特定的损失函数：在误分类时是参数w,b的线性函数，在正确分类时，是0。


##### 2. 感知机学习算法

感知机学习的过程也就是它的损失函数不断优化达到最小值的过程。具体就是采用`随机梯度下降法(SGD)`。损失函数L(w,b)的梯度为：

$\nabla_w L(w,b)=-\sum_{i=1}^{M}y_i\:x_i$

$\nabla_b L(w,b)=-\sum_{i=1}^{M}y_i$

随机选出一个误分类点 $(x_i,y_i)$，对参数 w，b进行更新：

$$w+ \eta y_i x_i \rightarrow w$$
$$b+ \eta y_i \rightarrow b$$

式中η（0≤η≤1）是步长，在统计学是中成为学习速率。步长越大，梯度下降的速度越快，更能接近极小点。如果步长过大，有可能导致跨过极小点，导致函数发散；如果步长过小，有可能会耗很长时间才能达到极小点。

具体算法过程如下：

输入训练集 T，学习率 $\eta$。输出 w,b 和 感知机模型 $f(x)=sign(w \cdot x+b)$。

（1）首先任选一个超平面，参数分别为 $w_0,b_0$；

（2）在训练集中任选一条数据 $(x_i,y_i)$；

（3）如果 $y_i(w \cdot x_i+b) \leq 0$，则执行：

$$w+ \eta y_i x_i \rightarrow w$$
$$b+ \eta y_i \rightarrow b$$

（4）转至（2），直至训练集中没有误分类的点。

这种方法直观地解释就是：不断调整参数 w,b ，将超平面向当前选择的误分类点得方向移动（`一个点可能会更新多次，最终使得误分类点分类正确为止`），以减少误分类点，最终所有误分类点都被正确分类。

此外，感知机学习算法是可能有很多解的，为了得到唯一的超平面，就要对超平面增加约束条件，也就是 SVM 的想法。

#### 三、实现代码

从输入参数得到训练文件和模型文件：

```python
n = float(sys.argv[1])
trainFile = open(sys.argv[2])
modelFile= open(sys.argv[3], 'w')
```

从训练文件中读取训练数据：

```python
for line in trainFile:
  chunk = line.strip().split(' ') #每行的数据
  lens = len(chunk) - 1 #最后一行是训练输出
  tmp_all = []
  tmp = []
  for i in range(1, lens+1):
    tmp.append(int(chunk[i]))
  tmp_all.append(tmp)
  tmp_all.append(int(chunk[0]))
  training_set.append(tmp_all)

trainFile.close()
```

训练数据：
```
1 3 3
1 4 3
-1 1 1
```

计算点到超平面的距离：
```python
def cal(item):
    global w, b
    res = 0
    for i in range(len(item[0])):
        res += item[0][i] * w[i] #w和xi的内积
    res += b
    res *= item[1]
    return res
```

判断是否是误分类点，如果是误分类点则更新参数:
```python
for item in training_set:
  if cal(item) <= 0:
    flag = True
    update(item)
```

使用随机梯度下降法更新参数：
```python
def update(item):
	global w, b, lens, n #n就是学习速率η
	for i in range(lens):
		w[i] = w[i] + n * item[1] * item[0][i]
	b = b + n * item[1]
```
  
#### 四、感知机学习算法的对偶形式

感知机学习算法的对偶形式的基本思想就是将参数 w 和偏置 b用实例 $x_i$ 和标签 $y_i$ 的`线性组合`来表示。通过求解这个`线性组合的系数`来求得 w 和 b。这里还是设初值 $w_0,b_0$ 均为0。对误分类的点也还是按如下规则更新：

$$w+ \eta y_i x_i \rightarrow w$$
$$b+ \eta y_i \rightarrow b$$

如果对于某个误分类点 $(x_i,y_i)$ ，我们通过上面这个规则更新了 $n_i$ 次参数 w 和 b。那么 w 和 b 关于 $(x_i,y_i)$ 的增量为：$a_iy_ix_i$ 和 $a_iy_i$，由于每个点更新了 $n_i$ 次，且学习率为 $\eta$，所以 $a_i=n_i \eta$ （`这里也就解释了下面具体算法过程中 a 每次只增加 η`）。通过这个学习过程，最终 w 和 b 为：

$$w=\sum_{i=1}^{N} a_iy_ix_i$$

$$b=\sum_{i=1}^{N} a_iy_i$$

由于 $a_i > 0,i=1,2,...,N$，所以当 $\eta=1$ 时，$a_i=n_i$ 实际上就是第 i 个误分类点更新到正确之后所用的次数。`某个误分类点更新次数越多，意味着它离超平面距离越近,也就越难分类正确`。这样的点对感知机学习算法影响最大。

具体算法过程如下：

输入训练集 T，学习率 $\eta$。输出 a，b 和感知机模型 $f(x)=sign(\sum_{j=1}^{N} a_jy_jx_j \cdot x+b)$。

（1） $a \leftarrow 0,b \leftarrow 0$

（2） 任选误分类点 $(x_i,y_i)$

（3） 如果 $y_i(\sum_{j=1}^{N} a_jy_jx_j \cdot x+b) \leq 0$，则更新：

$$a_i \leftarrow a_i+\eta$$
$$b_i \leftarrow b_i+\eta y_i$$

（4）转至（2），直至训练集中没有误分类的点。

另外，可以看第三步的判断条件，说明每次都要判断实例的内积 $(x_j \cdot x_i)$，所以可以预先将训练集的实例之间的内积算出来并以矩阵形式表示，这就是所谓的 Gram 矩阵:

$$G = [x_i \cdot x_j]_{N \times N}$$




- - -

注：

[1]. 线性可分：存在超平面能够将`所有`的数据点`完全划分`到两侧。

[2]. [验证单层感知机不能表示异或逻辑](https://blog.csdn.net/SmartDazhi/article/details/75745893)

[3]. 异或运算（XOR）：a⊕b = (¬a ∧ b) ∨ (a ∧¬b)，如果a、b两个值不相同，则异或结果为1。如果a、b两个值相同，异或结果为0。
- - -

参考：

[1]. [感知机（python实现）](http://www.cnblogs.com/kaituorensheng/p/3561091.html)

[2]. 《统计学习方法（李航）》：感知机
- - -
THE END.
