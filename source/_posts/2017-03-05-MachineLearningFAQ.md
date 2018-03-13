---
date: 2017-03-05
title: 学习MachineLearning过程中的一些问题
tags: MachineLearning
category: blog
layout: default
---

#### 环境相关

0. [python](https://app.yinxiang.com/shard/s13/nl/621958455/54409a58-c91d-4d22-b457-6e5fbb721a3c/)

1. [Anaconda](https://www.continuum.io/downloads/)
> Anaconda 本质上是一个软件发行版 ，包含了 conda、Python 等 180 多个科学包及其依赖项。

2. [pydot](https://pypi.python.org/pypi/pydot)
> pydot是用python实现的绘制graphviz的接口。

3. [graphviz](http://www.graphviz.org/)
> Graphviz的是AT&T Labs Research开发的图形绘制工具软件
如果运行过程中报出 `Exception: "dot" not found in path.` 则需要安装 `graphviz`，如果已经安装了Anacoda，直接执行 `conda install graphviz`。

<!--more-->

#### 概念

问题

- 分类
- 聚类
- 回归
- 异常检测
- 关联规则
- 强化学习
- 结构预测
- 特征学习
- 在线学习
- 半监督学习
- 语法归纳

 - - -

监督学习
- 分类 ： 输出值如果是不连续的监督学习算法称为分类器（classifier）。
- 回归 ： 输出值连续的监督学习算法称为回归函数（regression function）。

- - -
线性回归
-  代价函数 ：

> $$J(\theta)=\frac{1}{2m}\sum_{i=1}^m{({h_\theta }({x^{(i)}})-{y^{(i)}})^{2}}$$

> 向量化实现 : $$J(\theta)=\frac{1}{2m}(X*\theta-\vec{y})^{T} (X*\theta-\vec{y})$$

-  归一化 : 

> $$y_{i}=\frac{x_{i}-\overline{x}}{s(x)}$$

-  梯度下降算法 : 

> 代价函数对  $$\theta_j$$ 求偏导得到 : 
$$\frac{\partial{J(\theta)}}{\partial{\theta_j}} = \frac{1}{m}\sum\limits_{i = 1}^m {[({h_\theta }({x^{(i)}})-{y^{(i)}})x_j^{(i)}]}$$

> 所以对 $$\theta$$ 的更新可以写为 : $${\theta_j}={\theta_j}-\alpha\frac{1}{m}\sum\limits_{i=1}^m {[({h_\theta }({x^{(i)}})-{y^{(i)}})x_j^{(i)}]}$$

> 向量化实现 : $${\theta_j} = {\theta_j}-{\alpha\frac{1}{m}(X^T(X*{\theta- \vec{y}}))}$$

分类器
- 人工神经网络
- 支持向量机（support vector machine）
- 最邻近法
- 高斯混合模型
- 朴素贝叶斯法
- 决策树（decision tree）
- 径向量函数分类

解决步骤
1. 决定训练的样本类型
2. 搜集训练集
3. 决定学习函数的特征表示
4. 决定学习函数的结构以及对应的学习算法
5. 完成设计
6. 评价学习函数的准确性


- - -

- 决策树
- 表征（装袋, 提升，随机森林）
- k-NN
- 线性回归
- 朴素贝叶斯
- 神经网络
- 逻辑回归
- 感知器
- 支持向量机（SVM）
- 相关向量机（RVM）

聚类

- BIRCH
- 层次
- k平均
- 期望最大化（EM）

- DBSCAN
- OPTICS
- 均值飘移

降维
- 因子分析
- CCA
- ICA
- LDA
- NMF
- PCA
- LASSO
- t-SNE

结构预测
- 概率图模型（贝叶斯网络，CRF, HMM）

异常检测
- k-NN
- 局部离群因子

神经网络
- 自编码
- 深度学习
- 多层感知机
- RNN
- 受限玻尔兹曼机
- SOM
- CNN

理论
- 偏差/方差偏置(Bias-Variance Tradeoff)：一般而言高偏差意味着欠拟合，高方差意味着过拟合
- 计算学习理论
- 经验风险最小化
- PAC学习
- 统计学习
- VC理论




- - -
THE END.