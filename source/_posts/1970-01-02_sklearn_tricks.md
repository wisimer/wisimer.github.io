---
title: sklearn 技巧
date: 1970-01-02
categories: sklearn
tags: [sklearn,ML]
---

##### 1. 模型的保存和读取

使用 `joblib` 模块。

保存：

```
from sklearn.datasets import load_iris # iris数据集
from sklearn.model_selection import train_test_split # 分割数据模块
from sklearn.neighbors import KNeighborsClassifier # K最近邻(kNN，k-NearestNeighbor)分类算法
from sklearn.externals import joblib #jbolib模块

iris = load_iris()
X = iris.data
y = iris.target

# 分割数据并
X_train, X_test, y_train, y_test = train_test_split(X, y, random_state=4)

# 建立模型
knn = KNeighborsClassifier()

# 训练模型
knn.fit(X_train, y_train)

# 保存模型(注:保存模型所在的文件夹要预先建立，否则会报错)
joblib.dump(knn, '/Users/mac/Downloads/knn.pkl')
```

读取和使用：

```
knn = joblib.load('/Users/mac/Downloads/knn.pkl')
print(knn.predict(X_test[0:1]))
# [2]
```

<!--more-->

- - -
THE END.