---
title: KNN 实践：识别手写数字
date: 2018-09-14
categories: ML
tags: [KNN]
---

#### 一、前言
先简单回顾一下 KNN 的原理：用`距离目标最近的 k 个样本数据的分类`来代表目标的分类。

<!--more-->

#### 二、实践

##### 1. 导入数据

sklearn 的 datasets 里面自带了一个关于手写数字的数据集，直接导入：

```
from sklearn import datasets
data = datasets.load_digits()
# 看一下里面有哪些信息
print(data.keys())
# dict_keys(['data', 'target', 'target_names', 'images', 'DESCR'])
```

- `data` ： 数据集
- `target` ： 数据集对应的标签
- `target_names` ： 所有标签的名字集合
- `images` ： 每个标签对应的手写图像
- `DESCR` ： 数据集描述

##### 2. 使用 KNeighborsClassifier 分类 

```
from sklearn.neighbors import KNeighborsClassifier
knn = KNeighborsClassifier() #也可以加一个int参数来设置 k 值

# 从0-1000的数据作为训练数据
knn.fit(data['data'][0:1000],data['target'][0:1000])

# 预测单个实例，这里主要要reshape一下
print(knn.predict(data['data'][1001].reshape(1,-1)))
# [1]

# 看看第1001个实例实际的标签
print(data['target'][1000])
# 1

# 也可以直接看一下测试集的分类准确率
print(knn.score(data['data'][1001:],data['target'][1001:]))
# 0.9560301507537688
```

##### 3. 当然也可以使用交叉验证来看看 knn 的分类效果，并选择合适的 k 值

```
# 交叉验证
from sklearn.cross_validation import cross_val_score
k_range = range(1, 31)
k_scores = []

for k in k_range:
    knn = KNeighborsClassifier(n_neighbors=k) # 这里通过改变 KNN 的 K 值来测试分类精度
    scores = cross_val_score(knn, data['data'], data['target'], cv=10, scoring='accuracy')
    k_scores.append(scores.mean())

#可视化数据
plot.plot(k_range, k_scores)
plot.xlabel('Value of K for KNN')
plot.ylabel('Cross-Validated Accuracy')
plot.show()
```

看一下效果：

![knn cross val](/src/imgs/1809/0914_knn_practice_cross.png)

```
print(scores)
# [0.90810811 0.96174863 0.97790055 0.92777778 0.96089385 0.96648045
 0.97765363 0.96629213 0.93785311 0.96022727]
```

看一下打印出来的各个k值的验证结果，和图中显示效果吻合。

- - -

附：

[1]. [KNN Pratice Code](/src/raw/code/knn/knn_handwriting.ipynb)


- - -
THE END.