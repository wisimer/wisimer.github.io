---
title: 集成学习 实践
date: 2018-09-16
categories: ML
tags: [ML]
---

我们使用 `集成学习` 方法来对乳腺癌预测，这里的例子使用的数据集是 [Wisconsin Breast Cancer dataset](https://raw.githubusercontent.com/dataspelunking/MLwR/master/Machine%20Learning%20with%20R%20(2nd%20Ed.)/Chapter%2003/wisc_bc_data.csv) 。这个数据集的 `diagnosis` 列为每一个记录的标签，有 B 和 M两个值，而其他列均是特征。下面我们来一步一步处理。

<!--more-->

#### 一、数据预处理

##### 1. 数据导入

```
import pandas as pd
import numpy as np
from sklearn.preprocessing import Imputer
from sklearn.preprocessing import MinMaxScaler

data = pd.read_csv('/Users/mac/Downloads/cancer.csv')
data.head()
```

看一下输出：

![cancer data set](/src/imgs/1809/0916_cancer_data_set.png)

看一下第一列 "id" ，对我们的训练和预测没有什么用，直接去掉即可：

```
data.drop(['id'],axis = 1, inplace = True)
```

去掉之后再看一下数据集的详细信息：

```
data.describe()
```

![cancer data set describe](/src/imgs/1809/0916_cancer_data_set_describe.png)

```
data.info()
```

![cancer data set info](/src/imgs/1809/0916_cancer_data_set_info.png)

##### 2. 数据缺失值处理

如果数据集中有缺失的话，我们可以使用 `data.replace('?',0, inplace=True)` 来填补缺失值。同样，我们看一下 `diagnosis` 这一列，也就是诊断结果。由于现在是字符串类型 `B` or `M`，需要变为整型，直接用 replace 替换为：`B(0)`，`M(1)`：

```
data.replace('B',0, inplace=True)
data.replace('M',1, inplace=True)
```

由于这里的 `data` 还是一个 `DataFrame <class pandas.core.frame.DataFrame>` 类型的对象，现在要转换为 Numpy 里面的数 `ndarray<class  numpy.ndarray>` ：

```
values = data.values
print(values)

# 输出替换之后的数据
[[ 0.      12.32    12.39    ...  0.09391  0.2827   0.06771]
 [ 0.      10.6     18.95    ...  0.07926  0.294    0.07587]
 [ 0.      11.04    16.83    ...  0.07431  0.2998   0.07881]
 ...
 [ 1.      15.28    22.41    ...  0.1226   0.3175   0.09772]
 [ 0.      14.53    13.98    ...  0.1069   0.2606   0.0781 ]
 [ 1.      21.37    15.1     ...  0.1966   0.273    0.08666]]
```

如果是在训练集上用规则处理好数据，同时想要应用到测试集上，可以使用 `Imputer.fit_transform()`，见注[3]。

##### 3. 数据归一化

上面缺失值处理好之后发现即使全部类型都变为整型，但是每一列的数据范围还是比较大，这样会导致特征的微小变化不会生效。下面对数据进行归一化处理，保证它们的范围都在0-1之间：

```
scaler = MinMaxScaler(feature_range=(0, 1))
normalizedData = scaler.fit_transform(values)
print(normalizedData)

# 输出归一化之后的数据
[[0.         0.25268588 0.0906324  ... 0.32271478 0.24876799 0.08310376]
 [0.         0.17128118 0.31247886 ... 0.27237113 0.27104278 0.136626  ]
 [0.         0.19210564 0.24078458 ... 0.25536082 0.28247585 0.15590975]
 ...
 [1.         0.3927777  0.42948935 ... 0.42130584 0.31736645 0.27994228]
 [0.         0.35728146 0.14440311 ... 0.36735395 0.20520402 0.15125279]
 [1.         0.68100715 0.18227934 ... 0.67560137 0.22964715 0.20739866]]
```

#### 二、使用 sklearn 实现集成学习实例

##### 1. Bagging 集成学习

先回顾一下 `Bagging` 算法。Bagging 是并行集成学习方法的代表。我们使用 `sklearn.ensemble.BaggingClassifier` 来实现 Bagging 集成学习：

文档：

```
class sklearn.ensemble.BaggingClassifier(base_estimator=None, n_estimators=10, max_samples=1.0, max_features=1.0, bootstrap=True, bootstrap_features=False, oob_score=False, warm_start=False, n_jobs=1, random_state=None, verbose=0)

A Bagging classifier is an ensemble meta-estimator that fits base classifiers each on random subsets of the original dataset and then aggregate their individual predictions (either by voting or by averaging) to form a final prediction. 
Such a meta-estimator can typically be used as a way to reduce the variance of a black-box estimator (e.g., a decision tree), by introducing randomization into its construction procedure and then making an ensemble out of it.
```

BaggingClassifier 有如下几个参数：

- `base_estimator` ： 基学习器（可以是多个不同）
- `n_estimators` ： base_estimator 的数量
- `max_samples` ： 从数据集中取出训练每个学习器的数据最大个数
- `max_features` ： max_samples 的最大特征数
- `bootstrap` ： 取出样本后是否放回，默认 True
- `bootstrap_features` ： 样本特征取出后是否替换
- `oob_score` ： 是否使用 `out-of-bag` 方法
- `warm_start` ： 是否复用之前的模型
- `n_jobs` ： 工作 CPU 的个数
- `random_state` ： 随机种子
- `verbose` ： The verbosity level

代码：

```
from sklearn import model_selection
from sklearn.ensemble import BaggingClassifier
from sklearn.tree import DecisionTreeClassifier

# 取特征值
X = values[:,1:31]
# 取标签
Y = values[:,0]

# 使用 K 折交叉验证
kfold = model_selection.KFold(n_splits=10, random_state=7)
# 使用决策树分类
cart = DecisionTreeClassifier()
num_trees = 100
# 使用 Bagging 集成学习
model = BaggingClassifier(base_estimator=cart, n_estimators=num_trees, random_state=7)

# 执行交叉验证
results = model_selection.cross_val_score(model, X, Y, cv=kfold)

# 打印 10 次验证结果的平均值
print(results.mean())
# 0.9561090225563911
```

当然这里我们也可以横向对比一下， BaggingClassifier 的 `n_estimators` 参数取不同值的时候，模型的性能如何：

```
k_range = range(10, 100, 5)
k_scores = []

for k in k_range:
    model = BaggingClassifier(base_estimator=cart, n_estimators=k, random_state=7)
    results = model_selection.cross_val_score(model, X, Y, cv=kfold)
    #print(results)
    k_scores.append(results.mean())
    
plt.plot(k_range, k_scores)
plt.xlabel('Value of n_estimators for BaggingClassifier')
plt.ylabel('Cross-Validated Accuracy')
plt.show()
```

![baggings](/src/imgs/1809/0916_bagging_compare.png)

从图中可以看到 n_estimators 不是越好，在50-60之间的时候性能较高一点点。

##### 2. AdaBoost 集成学习

先回顾一下 `Boosting` 算法族是一组串行序列化集成学习方法。可以将弱学习器转化为强学习器。我们这里使用 `sklearn.ensemble.AdaBoostClassifier` 来实现 Boosting 集成学习方法。

文档：

```
class sklearn.ensemble.AdaBoostClassifier(base_estimator=None, n_estimators=50, learning_rate=1.0, algorithm=’SAMME.R’, random_state=None)

An AdaBoost classifier is a meta-estimator that begins by fitting a classifier on the original dataset and then fits additional copies of the classifier on the same dataset but where the weights of incorrectly classified instances are adjusted such that subsequent classifiers focus more on difficult cases.
```

AdaBoostClassifier 有如下几个参数：

- `base_estimator` ： 基础模型
- `n_estimators` ： 模型数量
- `learning_rate` ： 学习速率，通过修改 learning_rate 控制每个模型的贡献值
- `algorithm` ： 具体 boosting 算法，{‘SAMME’, ‘SAMME.R’}二选一
- `random_state` ： 随机种子

代码：

```
from sklearn.ensemble import AdaBoostClassifier
seed = 7
num_trees = 70
kfold = model_selection.KFold(n_splits=10, random_state=seed)

# 使用 AdaBoost 集成学习
model = AdaBoostClassifier(n_estimators=num_trees, random_state=seed)
results = model_selection.cross_val_score(model, X, Y, cv=kfold)

# 打印 10 次验证结果的平均值
print(results.mean())
# 0.9649122807017545
```

##### 3. 基于投票法的集成学习

这里将多个分类模型集合起来，对他们的分类结果采取投票法，最终得到结果。这里使用 `sklearn.ensemble.VotingClassifier` 来实现。

文档：

```
class sklearn.ensemble.VotingClassifier(estimators, voting=’hard’, weights=None, n_jobs=1, flatten_transform=None)

Soft Voting/Majority Rule classifier for unfitted estimators.
```

VotingClassifier 有如下几个参数：

- `estimators` ： 模型
- `voting` ： {‘hard’, ‘soft’} 二选一，hard就是单纯使用标签投票，soft则是预测可能性最大的标签。默认hard
- `weights` ： 每个方法预先的权值，默认各方法权值相同
- `n_jobs` ： 工作 CPU 数量
- `flatten_transform` ： 平滑变形，配合voting参数为soft时使用。如果voting ='soft'且flatten_transform = True，变换方法返回具有形状的矩阵（n_samples，n_classifiers * n_classes），则仅影响变换输出的形状。 如果flatten_transform = False，它将返回（n_classifiers，n_samples，n_classes）。

代码：

```
from sklearn.linear_model import LogisticRegression
from sklearn.tree import DecisionTreeClassifier
from sklearn.svm import SVC
from sklearn.ensemble import VotingClassifier

kfold = model_selection.KFold(n_splits=10, random_state=seed)

# 模型集合
estimators = []
model1 = LogisticRegression()
estimators.append(('logistic', model1))
model2 = DecisionTreeClassifier()
estimators.append(('cart', model2))
model3 = SVC()
estimators.append(('svm', model3))

# Voting 作为结合策略的集成学习
ensemble = VotingClassifier(estimators)
results = model_selection.cross_val_score(ensemble, X, Y, cv=kfold)
print(results.mean())
# 0.950814536340852
```

- - -

注：

[1]. Imputer : sklearn 里面用于处理缺失值。

[2]. MinMaxScaler : sklearn 里面用于处理数据归一化。

[3]. [fit_transform](https://datascience.stackexchange.com/questions/12321/difference-between-fit-and-fit-transform-in-scikit-learn-models)


- - -
THE END.