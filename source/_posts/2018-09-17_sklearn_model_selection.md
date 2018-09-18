---
title: sklearn.model_selection 交叉验证
date: 2018-09-17
categories: sklearn
tags: [sklearn,ML]
---

#### 前言

##### 1. 什么是交叉验证法？

它的基本思想就是将原始数据（dataset）进行分组，一部分做为训练集来训练模型，另一部分做为测试集来评价模型。

##### 2. 为什么用交叉验证法？

`交叉验证`用于评估模型的`预测性能`，尤其是训练好的模型在新数据上的表现。交叉验证本身只能用于评估，但是可以对比不同 Model 或者参数对结构准确度的影响。然后可以根据验证得出的数据进行调参，也可以在一定程度上减小过拟合。

<!--more-->

Sklearn 中的 Cross Validation (交叉验证)对于我们选择正确的 Model 和 Model 的参数是非常有帮助的， 有了他的帮助，我们能直观的看出不同 Model 或者参数对结构准确度的影响。

#### 一、基础验证

```
from sklearn.datasets import load_iris # iris数据集
from sklearn.model_selection import train_test_split # 分割数据模块
from sklearn.neighbors import KNeighborsClassifier # K最近邻(kNN，k-NearestNeighbor)分类算法

#加载iris数据集
iris = load_iris()
X = iris.data
y = iris.target

#分割数据并
X_train, X_test, y_train, y_test = train_test_split(X, y, random_state=4)

#建立模型
knn = KNeighborsClassifier()

#训练模型
knn.fit(X_train, y_train)

#将准确率打印出
print(knn.score(X_test, y_test))
# 0.973684210526
```

可以看到这里我们先使用 KNN 分类训练数据之后，再用测试集测试，最后直接调用 `knn.score`，通过 KNN 自带的基础验证的准确率为0.973684210526。

#### 二、Model 交叉验证法(Cross Validation) 

主要就是使用 `sklearn.cross_validation` 这个模块

##### 1. sklearn.model_selection.cross_val_score

文档：

```
sklearn.model_selection.cross_val_score(estimator, X, y=None, groups=None, scoring=None, cv=None, n_jobs=1, verbose=0, fit_params=None, pre_dispatch='2*n_jobs')

Evaluate a score by cross-validation
```

`cross_val_score` 是用来进行交叉验证的。它有如下参数：

- `estimator` ： 实现了 `fit` 接口的预测器，也就是我们的模型
- `X` ： 要拟合的数据
- `y` ： 标签
- `groups` ： 分割后的数据每一组的群组标签
- `cv` ： 交叉验证生成器，决定分割验证的策略。有四个可选值： None（默认使用 3 折叠交叉验证），integer（指定 StratifiedKFold 的k值大小），对象，迭代。
- `n_jobs` ： 用于计算的 CPU 的数量，默认1，如果是 -1 则表示用上所有 CPU
- `verbose` ： The verbosity level.
- `fit_params` ： 从预测器传递给拟合方法的参数
- `pre_dispatch` ： 任务的个数

例子：

```
from sklearn.cross_validation import cross_val_score # K折交叉验证模块

#使用K折交叉验证模块，参数 cv 为5，表示直接使用 5 折 StratifiedKFold 交叉验证
scores = cross_val_score(knn, X, y, cv=5, scoring='accuracy')

#将5次的预测准确率打印出
print(scores)
# [ 0.96666667  1.  0.93333333  0.96666667  1. ]

#将5次的预测准确平均率打印出
print(scores.mean())
# 0.973333333333
```

可以看到交叉验证的准确平均率为0.973333333333

##### 2. class sklearn.model_selection.KFold

此外我们可以使用 `KFold` 类自己构造一个 K 折交叉验证器。

文档：

```
class sklearn.model_selection.KFold(n_splits=3, shuffle=False, random_state=None)

K-Folds cross-validator

Provides train/test indices to split data in train/test sets. Split dataset into k consecutive folds (without shuffling by default).

Each fold is then used once as a validation while the k - 1 remaining folds form the training set.
```

很显然，`KFold`这个类是用来分离数据的。可以讲数据集分割成 k-1 个训练集和 1 个测试集。它有三个参数：

- `n_splits` ： 表示分为几个数据子集
- `shuffle` ： 要不要打乱数据顺序呢，默认为 False
- `random_state` ： 打乱数据顺序所用的种子，默认不使用

例子：

```
# 实例化一个 k 折叠分割器
kfold = model_selection.KFold(n_splits=10, random_state=7)
print(kfold)

# 验证模型
results = model_selection.cross_val_score(knn, X_test, y_test, cv=kfold)
print(results.mean())
# 0.9
```

可以看到10折交叉验证的准确平均率为0.9

#### 三、以准确率(accuracy)判断 

`上面验证只是得出某种参数情况下的模型的性能，如果想要横向对比不同参数时模型性能的话则需要多次验证再做对比。`

这里使用 KNN 分类器来举例，一般来说`准确率(accuracy)`会用于判断分类模型的好坏。

```
from sklearn import model_selection
import matplotlib.pyplot as plt

#加载iris数据集
iris = load_iris()
X = iris.data
y = iris.target

#建立测试参数集
k_range = range(1, 31)

k_scores = []

#通过迭代的方式来计算不同参数对模型的影响，并返回交叉验证后的平均准确率。
for k in k_range:
    knn = KNeighborsClassifier(n_neighbors=k) # 这里通过改变 KNN 的 K 值来测试分类精度
    scores = model_selection.cross_val_score(knn, X, y, cv=10, scoring='accuracy')
    k_scores.append(scores.mean())

#可视化数据
plt.plot(k_range, k_scores)
plt.xlabel('Value of K for KNN')
plt.ylabel('Cross-Validated Accuracy')
plt.show()
```

![KNN cross val](/src/imgs/1809/0917_knn_cross_val.png)

从图中可以得知，选择12~20的k值最好。高过20之后，准确率开始下降则是因为过拟合(Over fitting)的问题。

其实这里就是通过调整不同的 K值来调整 KNN 的分类性能，然后使用交叉验证来直观地显示分类的准确性，也就可以找出一个最佳 K 值使得 KNN 性能最高。

当然， `model_selection.cross_val_score` 方法的 `scoring` 参数也可以使用 `mean_squared_error(平均方差)`。得到的结果和使用 `accuracy` 相差不多。

- - -

参考：

[1]. [交叉验证](https://morvanzhou.github.io/tutorials/machine-learning/sklearn/3-2-cross-validation1/)

- - -
THE END.