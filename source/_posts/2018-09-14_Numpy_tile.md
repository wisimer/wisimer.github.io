---
title: Numpy.tile 的作用
date: 2018-09-14
categories: ML
tags: [Numpy]
---

#### 文档

```
numpy.tile(A, reps):

  Construct an array by repeating A the number of times given by reps.

  If reps has length d, the result will have dimension of max(d, A.ndim).

  If A.ndim < d, A is promoted to be d-dimensional by prepending new axes. So a shape (3,) array is promoted to (1, 3) for 2-D replication, or shape (1, 1, 3) for 3-D replication. If this is not the desired behavior, promote A to d-dimensions manually before calling this function.

  If A.ndim > d, reps is promoted to A.ndim by pre-pending 1’s to it. Thus for an A of shape (2, 3, 4, 5), a reps of (2, 2) is treated as (1, 1, 2, 2).

  Note : Although tile may be used for broadcasting, it is strongly recommended to use numpy’s broadcasting operations and functions.
```

<!--more-->

#### 说明

`tile` 函数有两个参数 A 和 reps。

tile这个函数是用来构造一个数组，内容按`规则 reps (元组)` 来重复`参数 A (数组)`。

如果 reps 的长度是 d，那么生成结果的维度是 max(d,A.ndim)。A.ndim 表示参数 A 数组的维度。

1. 如果 A.ndim < d，那就在 A 的前面加上新的维度使其达到 d 维。

2. 如果 A.ndim > d，reps 就要在现有元素前面插入1，使它的长度达到 A.ndim。再对 A 进行重复。

> 注意！！！这里的执行顺序很重要：元组 reps 数字从左到右，数组 A 维度从最低（外层）维度到最高（内层）维度。见下面例子分析。


#### 例子

##### 例子 1

输入：

```
import numpy as np
a = np.array([0, 1, 2])
np.tile(a, 2)
```

输出：

```
array([0, 1, 2, 0, 1, 2])
```

分析：

> 这里的参数 A 就是 [0, 1, 2]，维度是 1。参数 reps 是 2，它的长度是1。A.ndim = d，不用改变维度，直接重复 A 两次得到[0, 1, 2, 0, 1, 2]。

##### 例子 2

输入：

```
a = np.array([0, 1, 2])
np.tile(a, (2, 2))
```

输出：

```
array([[0, 1, 2, 0, 1, 2],
       [0, 1, 2, 0, 1, 2]])
```

分析：

> 参数 A 还是 [0,1,2]，维度是 1。参数 reps 是 (2, 2)，它的长度是 2。A.ndim < d， max(d,A.ndim)=2，就表示构造的结果要升维变成 2 维了。那么具体是怎么重复A呢？首先第1个维度（最低维度）重复 A 两次（reps的第一个2），同时升为2维，变为 [[0,1,2,0,1,2]]；然后再将这个结果在第 2 个维度（高维度）重复两次（reps的第二个2）变为 [[0,1,2,0,1,2],[0,1,2,0,1,2]]。

##### 例子 3

输入：

```
a = np.array([0, 1, 2])
np.tile(a, (2, 1, 2))
```

输出：

```
array([[[0, 1, 2, 0, 1, 2]],
       [[0, 1, 2, 0, 1, 2]]])
```

分析：

> 参数 A 还是 [0,1,2]，维度是 1。参数 reps 是（2，1，2）它的长度是3。A.ndim < d，max(d,A.ndim)=3，所以构造的结果需要升维到 3。首先在第1维度（最低维度）重复2次且`升维`得[[[0,1,2,0,1,2]]]；再在第2维度重复1次，也就是它本身得[[[0,1,2,0,1,2]]]；最后在第3维度（最高）重复2次得[[[0,1,2,0,1,2]],[[0,1,2,0,1,2]]]。

##### 例子 4

输入：

```
b = np.array([[1, 2], [3, 4]])
np.tile(b, 2)
```

输出：

```
array([[1, 2, 1, 2],
       [3, 4, 3, 4]])
```

分析：

> 参数 A 是 [[1,2], [3,4]]，维度是 2。参数 reps 是 2，长度是1。A.ndim > d，max(d,A.ndim)=2，所以在 reps 前面插入 1 得 (1,2)，再对 A 重复。首先第1维（最低维度）重复1次得 [[1, 2], [3, 4]] ，再在高维度重复2次得 [[1,2,1,2],[3,4,3,4]] 。

##### 例子 5

输入：

```
b = np.array([[1, 2], [3, 4]])
np.tile(b, (2, 1))
```

输出：

```
array([[1, 2],
       [3, 4],
       [1, 2],
       [3, 4]])
```

分析：

> 参数 A 是 [[1, 2], [3, 4]]，维度是 2。参数 reps 是(2, 1)，长度是2。A.ndim = d，不用升维。首先低维度重复2次得 [[1, 2], [3, 4],[1, 2], [3, 4]]，再在高维度重复1次得  [[1, 2], [3, 4],[1, 2], [3, 4]]

##### 例子 6

输入：

```
c = np.array([1,2,3,4])
np.tile(c,(4,1))
```

输出：

```
array([[1, 2, 3, 4],
       [1, 2, 3, 4],
       [1, 2, 3, 4],
       [1, 2, 3, 4]])
```

分析：

> 参数 A 是 [1,2,3,4]，维度是 1。参数 reps 是 (4,1)，长度是 2。A.ndim < d，max(d,A.ndim)=2，结果需要升维到2。首先在低维度重复4次得 [[1,2,3,4],[1,2,3,4],[1,2,3,4],[1,2,3,4]]，再在高维度重复1次得 [[1,2,3,4],[1,2,3,4],[1,2,3,4],[1,2,3,4]]。

- - -
THE END.