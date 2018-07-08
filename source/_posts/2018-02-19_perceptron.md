---
title: 感知机
date: 2018-02-19
categories: ML
tags: [Perceptron]
---

#### 感知机

感知机（perceptron）是一种二分类的线性分类模型，输入为实例的特征向量，输出为实例的类别（取+1和-1）。感知机对应于输入空间中将实例划分为两类的分离超平面。感知机旨在求出该超平面，为求得超平面导入了基于误分类的损失函数，利用梯度下降法 对损失函数进行最优化（最优化）。

<!--more-->

#### 感知机的损失函数

$L(w,b)=-\sum_{i=1}^{M} {y_i(w*x_i+b)}$

显然，损失函数L(w,b)是非负的。如果没有误分类点，那么L(w,b)为0，误分类点数越少，L(w,b)值越小。一个特定的损失函数：在误分类时是参数w,b的线性函数，在正确分类时，是0。

损失函数L(w,b)的梯度：

$\nabla_w L(w,b)=-\sum_{i=1}^{M}y_i\:x_i$

$\nabla_b L(w,b)=-\sum_{i=1}^{M}y_i$

求解损失函数极小值的过程：

首先，任意选定w0、b0，然后用梯度下降法不断极小化目标损失函数，极小化的过程不是一次性的把M中的所有误分类点梯度下降，而是一次随机选取一个误分类点使其梯度下降：

$w=w+\eta y_i\:x_i$

$b=b+\eta y_i$

式中η（0≤η≤1）是步长，在统计学是中成为学习速率。步长越大，梯度下降的速度越快，更能接近极小点。如果步长过大，有可能导致跨过极小点，导致函数发散；如果步长过小，有可能会耗很长时间才能达到极小点。

#### 实现代码

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

疑问：感知机的对偶形式

参考：[http://www.cnblogs.com/kaituorensheng/p/3561091.html](http://www.cnblogs.com/kaituorensheng/p/3561091.html)
- - -
THE END.
