---
title: 基本概念
date: 1970-01-04
categories: ML
tags: [QA]
---

#### 环境相关

1. [python](https://app.yinxiang.com/shard/s13/nl/621958455/54409a58-c91d-4d22-b457-6e5fbb721a3c/)

2. [Anaconda](https://www.continuum.io/downloads/)
> Anaconda 本质上是一个软件发行版 ，包含了 conda、Python 等 180 多个科学包及其依赖项。

2. [pydot](https://pypi.python.org/pypi/pydot)
> pydot是用python实现的绘制graphviz的接口。

3. [graphviz](http://www.graphviz.org/)
> Graphviz的是AT&T Labs Research开发的图形绘制工具软件
如果运行过程中报出 `Exception: "dot" not found in path.` 则需要安装 `graphviz`，如果已经安装了Anacoda，直接执行 `conda install graphviz`。

<!--more-->

#### 假设空间

假设空间是指所有可能的能满足样本输入和输出的`假设函数h(x)的集合`。
注意，假设函数一定是一个无穷大的集合。也就是说，如果样本是一串有穷的离散点（xi，yi），i属于1到N，那么能够拟合这这些点的无穷多个函数都是可能的假设函数。

那么，怎样的假设函数是最合理呢？这就涉及到`归纳偏好`的概念了。
归纳偏好是一个能挑选最佳假设函数的基准。以韦小宝的7个老婆为例，这7个老婆均满足小宝的要求，因此构成了大小为7的假设空间。（实际上，假设空间的大小一定是无穷大的。为了说明问题，我们暂时以7为大小）。那么，如何衡量哪一个假设空间中哪一个假设函数（老婆）最好呢？如果以温柔体贴为偏好来选，当然是小双；如果以小宝的迷恋为偏好来讲，假设函数就是阿珂。说白了，归纳偏好就是一个用于挑选假设函数的基准。

一般情况下，我们都使用“奥卡姆剃刀”原则，也就是选择最简单的假设函数。也就是变量最少，变量的幂指数最小的函数。也就是说，一次函数能拟合时就不选二次函数作为假设函数。

#### 监督学习

1. 广义线性模型(Generalized Linear Models)
2. 线性和二次判别分析(Linear and Quadratic Discriminant Analysis)
3. 核岭回归(Kernel ridge regression)
4. SVM(Support Vector Machines)
5. 相关向量机（RVM）
6. 随机梯度下降法(Stochastic Gradient Descent)
7. 最邻近算法(Nearest Neighbors)
8. 高斯过程(Gaussian Processes)
9. 交叉分解(Cross decomposition)
10. 朴素贝叶斯(Naive Bayes)
11. 决策树(Decision Trees)
12. 集成方法(Ensemble methods)
13. 多分类和多标签算法(Multiclass and multilabel algorithms)
14. 特征选择(Feature selection)
15. 半监督(Semi-Supervised)
16. 序回归(Isotonic regression)
17. Probability calibration
18. 有监督神经网络模型(Neural network models (supervised))
19. 分类 ： 输出值如果是不连续的学习算法称为分类器（classifier）。
20. 回归 ： 输出值连续的学习算法称为回归函数（regression function）。

#### 无监督学习

1. 高斯混合模型(Gaussian mixture models)
2. 流形学习(Manifold learning)
3. 聚类(Clustering)
4. 双聚类(Biclustering)
5. Decomposing signals in components (matrix factorization problems)
6. 协方差估计(Covariance estimation)
7. Novelty and Outlier Detection
8. 密度估计(Density Estimation)
9. 无监督神经网络模型(Neural network models (unsupervised))

#### 降维

- 因子分析
- CCA
- ICA
- LDA
- NMF
- PCA
- LASSO
- t-SNE
  
#### 损失函数

对于给定的输入X和假设空间中选择的决策函数模型f，由f(X)给出相应的输入Y，这个输出的预没值f(X)与真实值Y可能一致，也可能不一致，用一个损失函数或代价函数来度量预测的错误程度。损失函数是f(x)和Y的非负实值函数，记作L(Y,f(X))

几种常用的损失函数：

1. 0-1损失函数(0-1 loss function)

 $L(Y,f(X)) = \begin{cases}1, &Y\neq f(X) \\ 0, & Y=f(X)\end{cases}$

2. 平方损失函数（quadratic loss function）

 $L(Y,f(X)) = (Y – f(X))^2$

3. 绝对损失函数（absolute loss function）

 $L(Y,f(X)) = |Y-f(X)|$

4. 对数损失函数（logarithmic loss function）或对数似然损失函数

 $L(Y,P(Y|X)) = –logP(Y|X)$

#### 风险函数

损失函数值越小，模型就越好。由于模型的输入、输出(X,Y)是随机变量，遵循联合分布P(X,Y)，所以损失函数的期望是 :

$R_{exp}(f)=E_P[L(Y,f(X))]=\int_{\mathcal{X}\times\mathcal{Y}}L(y,f(x))P(x,y)dxdy$

这是理论上模型f(X)关于联合分布P(X,Y)的平均意义下的损失，称为风险函数(risk function)或期望损失(expected loss)。

学习的目标就是选择期望风险最小的模型。由于联合分布P(X,Y)是所有样本所遵循的统计规律，它是未知的，所以Rexp(f)不能直接计算。实际上如果知道了联合分布，那么可以直接计算出P(Y|X)=∫P(x,y)dx，也就不需要学习了。
所以用上面那种方式定义风险函数是不行的，那样的话监督学习变成了一个病态问题。

对于给定的训练数据集 T=(x1,y1),(x2,y2),…,(xN,yN),模型f(X)关于训练数据集的平均损失称为经验风险（empirical risk）或经验损失（empirical loss），记作Remp：

$R_{emp}(f) = \frac{1}{N}\sum_{i=1}^NL(y_i,f(x_i))$

期望风险Rexp(f)是模型关于联合分布的期望损失，经验风险Remp(f)是模型关于训练样本集的平均损失。根据大数定律，当样本容量N趋于无穷时，经验风险Remp(f)趋向于期望风险Rexp(f)。

所以，一个很自然的想法是用经验风险估计期望风险。但是，由于现实中训练样本数目很有限，所以用经验风险估计期望风险常常不理想，要对经验风险进行一定的矫正。这就关系到监督学习的两个基本策略：经验风险最小化和结构风险最小化。

#### 经验风险最小化

在假设空间、损失函数以及训练数据集确定的情况下，经验风险函数式就可以确定。经验风险最小化的策略认为，经验风险最小的模型就是最优的模型。根据这一策略，按照经验风险最小化求最佳模型就是求解最优化问题：

$\min_{f\in\mathcal{F}}\frac{1}{N}\sum_{i=1}^NL(y_i,f(x_i))$

其中F是假设空间。

当样本容量足够大时，经验风险最小化能保证有很好的学习效果，在现实中广泛采用。比如极大似然估计就是经验风险最小化的一个例子。当模型是条件概率分布，损失函数是对数损失函数时，经验风险最小化就等价于极大似然估计。
但是当样本容量很小时，经验风险最小化学习效果就未必很好，会产生“过拟合(over-fitting)”现象。

#### 结构风险最小化

结构风险最小化（structural risk minimization SRM）是为了防止过拟合而提出来的策略。结构风险最小化等价于正则化。结构风险在经验风险上加上表示模型复杂度的正则化项或罚项。在假设空间，损失函数以及训练样本集确定的情况下，结构风险的定义是

$R_{srm}(f) = \frac{1}{N}\sum_{i=1}^NL(y_i,f(x_i))+\lambda J(f)$

其中J(f)为模型的复杂度，是定义在假设空间F上的泛函。模型f越复杂，复杂度J(f)就越大；反之，模型f越简单，复杂度J(f)就越小。也就是说复杂度表示了对复杂模型的惩罚。λ≥0是系数，用以权衡经验风险和模型的复杂度。结构风险小需要经验风险与模型复杂度同时小。结构风险小的模型往往对训练数据以及未知的测试数据都有较好的预测。
比如，贝叶斯估计中的最大后验概率估计（maximum posterior probability estimation,MAP）就是结构风险最小化的例子。当模型是条件概率分布、损失函数就是对数损失函数、模型复杂度由模型的先验概率表示时，结构风险最小化就等价于最大后验概率估计。

结构风险最小化的策略认为结构风险最小的模型是最优的模型。所以求最优化模型时，就是求解最优化问题：

$\min_{f\in\mathcal{F}}\frac{1}{N}L(y_i,f(x_i))+\lambda J(f)$

这样，监督学习问题就变成了经验风险或结构风险函数的最优化问题。这时经验或结构风险函数是最优化的目标函数。

#### 理论

- 偏差/方差偏置(Bias-Variance Tradeoff)：一般而言高偏差意味着欠拟合，高方差意味着过拟合
- 计算学习理论
- 经验风险最小化
- PAC学习
- 统计学习
- VC理论



#### 极大似然估计

极大似然估计是根据样本(x1,x2，…，xi，…，xn)的值来估计样本模型中参数（θ1,θ2，…，θi，…，θn）的。是一种参数估计方法。

其中，似然是似然函数的简称。可由L(x1,x2，…，xi，…，xn,θ1,θ2，…，θi，…，θn )来表示。实际上，样本一旦确定，人们一般会假设一个可拟合这些样本的模型，即参数个数是确定的，待求的是参数的具体值。比如，基于班上同学的身高数据，一般会假设身高是符合正态分布(μ,σ)，待求的是这两个参数具体的值。

那么，如何基于已有的样本数据来求得模型中参数得具体值呢？

极大似然估计的意思是说，你只要能使L(x1,x2，…，xi，…，xn,θ1,θ2，…，θi，…，θn )最大，你的参数θ1,θ2，…，θi，…，θn 就是有效的。
以身高样本为例（假定身高样本是独立同分布的）。即当L(x1,x2，…，xi，…，xn,μ,σ )最大时，(μ,σ)被估计的是准确的。

求极大似然函数估计值的一般步骤：

1. 写出似然函数；
2. 对似然函数取对数，并整理；
3. 求导数 ；
4. 导数为零时，似然函数取得极大值 。

- - -
线性回归

-  代价函数 ：

$$J(\theta)=\frac{1}{2m}\sum_{i=1}^m{({h_\theta }({x^{(i)}})-{y^{(i)}})^{2}}$$

- 向量化实现 : 

$$J(\theta)=\frac{1}{2m}(X*\theta-\vec{y})^{T} (X*\theta-\vec{y})$$

-  归一化 :

$$y_{i}=\frac{x_{i}-\overline{x}}{s(x)}$$

-  梯度下降算法 :

代价函数对  $\theta_j$ 求偏导得到 :

$$\frac{\partial{J(\theta)}}{\partial{\theta_j}} = \frac{1}{m}\sum\limits_{i = 1}^m {[({h_\theta }({x^{(i)}})-{y^{(i)}})x_j^{(i)}]}$$

所以对 $\theta$ 的更新可以写为 : 

$${\theta_j}={\theta_j}-\alpha\frac{1}{m}\sum\limits_{i=1}^m {[({h_\theta }({x^{(i)}})-{y^{(i)}})x_j^{(i)}]}$$

向量化实现 : 

$${\theta_j} = {\theta_j}-{\alpha\frac{1}{m}(X^T(X*{\theta- \vec{y}}))}$$


- - -

`生成子空间（Span）`：一组向量的生成子空间是原始向量线性组合后所能抵达的点得集合。举个例子：设有一组向量 ${a_1,a_2,...,a_n}$，则它的生成子空间为 

$$Span(a_1,a_2,...,a_n)=\{k_1a_1+k_2a_2+...+k_na_n\},k_i=1,2,...,n$$

`范数（norm）`：是讲向量映射到非负值的函数。$L^p$ 范数定义如下：

$$||x||_p=(\sum_i|x_i|^p)^{\frac{1}{p}}$$

`奇异矩阵（singular matrix）`：列向量线性相关的矩阵。

`正交矩阵（orthogonal matrix）`：行向量或者列向量分别`标准正交（不仅正交，而且标准正交）`。$A^TA=AA^T=I,A^T=A^{-1}$。

`正定（positive definite）`：所有特征值都是正数的矩阵是正定矩阵。

`半正定（positive semidefinite）` ：所有特征值都是非负数的矩阵是半正定矩阵。

`对称举证`：$A^T=A$，实对称矩阵都可以分解为实特征向量和实特征值：$A=Q \Lambda Q^T$，Q 是特征向量组成的`正交矩阵`，$\Lambda$ 是特征值组成的对角矩阵。

实对称矩阵的特征分解可用于`优化二次方程` $f(x)=x^T A x$，其中 $||x||_2=1$，当x是A的特征向量时，f(x)是对应的特征值。

- - -
THE END.
