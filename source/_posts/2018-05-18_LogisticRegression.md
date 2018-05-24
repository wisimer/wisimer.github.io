---
title: Logistic回归算法
date: 2018-05-18
categories: 机器学习
tags: [Logistic回归]
---

#### 一、前言

Logistic回归虽然名字上是叫回归，但其实它是一种分类算法。Logistic回归也在一些文献中也称为logit回归、最大熵分类(MaxEnt)或对数线性分类器。

“回归”的意思就是要找到最佳拟合参数，其中涉及的数学原理和步骤如下：

1. 需要一个合适的分类函数来实现分类。可以使用单位阶跃函数或者Sigmoid函数。
2. 用 `代价函数` 来表示 `预测值h(x)` 与 `实际值y` 的偏差 `(h−y)`。要使得回归最佳拟合，那么偏差要尽可能小（偏差求和或取均值）。
3. 记J(w,b)表示回归系数取w时的偏差，那么求最佳回归参数w,b就转换成了求J(w,b)的最小值。可以使用梯度下降法求回归参数w,b。

所以，接下来就围绕这几个步骤进行展开。

<!--more-->

#### 二、分类函数

1.假设要实现二分类，那么可以找一个函数，根据不同的特征变量，输出0和1，并且只输出0和1，这种函数在某个点直接从0跳跃到1，如图：

![classifyfunction](/src/imgs/1805/0515_classifyfunction.png)

但是这种函数处理起来，稍微有点麻烦。

2.我们选择另外一个连续可导的函数，也就是Sigmoid函数,函数的公式如下：

$\widehat{y}=h(z)=\frac{1}{1+e^{-z}} （\widehat{y}就表示预测值也就是分类结果）$


这个函数的特点是，当z=0时，h(z)=0.5，而z越大，h(z)越接近1，z越小，h(z)越接近0。这个函数很像阶跃函数，当z>0，就可以将数据分入1类；当z < 0，就可以将数据分入0类。函数图如下：

![sigmoid](/src/imgs/1805/0515_sigmidfunction.png)

确定了分类函数，接下来，我们将Sigmoid函数的输入记为z，那么输入特征变量为：

$z=w_0x_0+w_1x_1+...+w_nx_n+b=w^Tx+b$，w是特征向量，b是一个值。

则分类函数：$\widehat{y}=h(z)=\frac{1}{1+e^{-z}}= \frac{1}{1+e^{-(w^Tx+b)}}$

向量x是特征变量，是输入数据，向量w,b是回归系数是特征。之后的事情就是如何确定最佳回归系数w(w0,w1,w2,...,wn,b)。

采用Sigmoid函数实际上就是将原始的输入x向量和某一合适的参数w向量相乘，得出的值作为sigmoid函数的输入，最终是的输出保持在0和1这两个值中的一个，从而达到分类的要求。

#### 三、损失函数和代价函数

1.损失函数（Loss function）

首先要知道损失函数是应用于单独一个训练样本的。而下面要说的代价函数则是针对整个训练集。

为了更好的进行凸优化，我们这里直接给出一个适合Logistic函数的损失函数：

$L(\widehat{y},y) = -[ylog\widehat{y}+(1-y)log(1-\widehat{y})]$

来看一下这个损失函数的特点：

- 当y=1时，$ylog\widehat{y}$ 这个部分的值是$log\widehat{y}，\widehat{y}表示预测分类$，$(1-y)log(1-\widehat{y})$ 这个部分的值是0。所以最终的损失函数是：$L(\widehat{y},y)=-log\widehat{y}$，而我们要求损失函数越小越好，所以也就是要求 $-(log\widehat{y})$ 越小越好，也就是要求 $\widehat{y}$ 越大越好。而 $\widehat{y}$和y一样，范围都是在0-1之间，所以 $\widehat{y}$ 越接近于1（y=1），损失函数越小。
- 当y=0时，$ylog\widehat{y}$ 这个部分的值是$0，\widehat{y}表示预测分类$，$(1-y)log(1-\widehat{y})$ 这个部分的值是 $log(1-\widehat{y})$。所以最终的损失函数是：$L(\widehat{y},y)=-(1-y)log(1-\widehat{y})$，而我们要求损失函数越小越好，所以也就是要求 $-[(1-y)log(1-\widehat{y})]$ 越小越好，也就是要求 $\widehat{y}$ 越小越好，所以 $\widehat{y}$ 越接近于0（y=0），损失函数越小。

2.代价函数（Cost function）或者叫 `成本函数`。

根据上面的损失函数可以得到整个训练集的代价函数：

$J(w,b)=\frac{1}{n}\sum_{i=1}^{n}{L({\widehat{y}}^i,y^i)}=-\frac{1}{n}\sum_{i=1}^{n}{[y^ilog{\widehat{y}}^i+(1-y^i)log(1-{\widehat{y}}^i)]}$

由于损失函数越小，预测效果越好。所以代价函数也是越小越好。由于代价函数是一个凸函数，可以采用梯度下降法求到使得代价函数最小的时候的参数值(w,b)。

#### 四、梯度上升/梯度下降

要使得代价函数越小越好就是要 $\frac{1}{n}\sum_{i=1}^{n}{[y^ilog{\widehat{y}}^i+(1-y^i)log(1-{\widehat{y}}^i)]}$ 越大越好。为求最大值，这里采用梯度上升的方法。

##### 1. 先来看看损失函数的梯度：

首先还是要明确，损失函数是针对单独一个训练样本的。

由于 $L(\widehat{y},y)=-[ylog\widehat{y}+(1-y)log(1-\widehat{y})]$ ，所以损失函数 $L(\widehat{y},y)$ 对 $\widehat{y}$ 求导可得：

$$d\widehat{y}=\frac{dL(\widehat{y},y)}{d\widehat{y}}=-\frac{y}{\widehat{y}}+\frac{1-y}{1-\widehat{y}}$$

而 $\widehat{y}=h(z)=\frac{1}{1+e^{-z}}$，再利用链式求导法则，损失函数 $L(\widehat{y},y)$ 对 $z$ 求导可得：

$$dz=\frac{dL(\widehat{y},y)}{dz}=\frac{dL}{d\widehat{y}} \cdot  \frac{d\widehat{y}}{dz}=(-\frac{y}{\widehat{y}}+\frac{1-y}{1-\widehat{y}})\cdot[\widehat{y}(1-\widehat{y})]=\widehat{y}-y$$

amazing...

接着再求 $dw_j$，由于 $z=w^Tx+b$ ,利用链式法则可得：

$$dw_1=\frac{\partial L}{\partial w_1}=\frac{dL}{dz} \cdot \frac{dz}{dw_1}=x_1 \cdot dz=x_1 \cdot (\widehat{y}-y)$$

同理 $dw_2=x_2(\widehat{y}-y)...dw_n=x_n(\widehat{y}-y)$。

最后求db: $db=\frac{dL}{dz} \cdot \frac{dz}{db}=dz=\widehat{y}-y$

在训练过程中我们要我们要不断更新参数w和b以使得损失函数最小，也就是梯度更新，梯度更新的逻辑是：

- $w_j:=w_j-\alpha \cdot dw_j$
- $b:=b-\alpha \cdot db$
- $\alpha$ 在这里是学习速率

##### 2. 代价函数的梯度

上面已经得出了单个训练样本的梯度更新法，接着就看在整个训练集上如何使用梯度更新法。

由于 $\widehat{y} = \frac{1}{1+e^{-(w^Tx+b)}}$，所以 $dw_1^{(i)},dw_2^{(i)},...,dw_n^{(i)},d_b^{(i)}$ 分别表示第i个单个样本中对参数w向量中第j个分量$w_j$的导数以及对偏置b的导数。


令 $J(w,b)=\frac{1}{n}\sum_{i=1}^{n}{[y^ilog{\widehat{y}}^i+(1-y^i)log(1-{\widehat{y}}^i)]}$,则在整个训练集上,J(w,b)对 $w_j$的偏导数是：

$$\frac{\partial J(w,b)}{\partial w_j}=\frac{1}{n}\sum_{i=1}^{n}{\frac{\partial L(\widehat{y}^{(i)},y)}{\partial w_j}}$$

由于对每个$w_j$都要进行梯度更新，所以我们先得到下面的伪代码：

![0518_cost_function_gradient](/src/imgs/1805/0518_cost_function_gradient.jpeg)

> 注意这里$dw_j$没有上标，也训练集中的每个样本进行梯度运算的时候都是使用的一个全局的参数w。

在实际操作中，不建议使用for循环，可以进行向量化，使得运算更为迅速。

#### 五、实现

##### 1. 数据准备

[testSet.txt](/raw/code/LogisticRegression/testSet.txt)

数据格式如下：

```
-0.017612	14.053064	0
-1.395634	4.662541	1
-0.752157	6.538620	0
-1.322371	7.152853	0
......
```

数据没有实际意义。第一列是参数x1,第二列是参数x2，第三列是分类标签（0/1）。

假设Sigmoid函数的输入记为 z ，那么 $z=w_{0}+w_{1}x_{1} + w_{2}x_{2}$ ，其实$w_0$就是上面的参数b。为了方便将z表示成向量相乘的形式 $z=w^Tx$，可以添加一个值为1的变量$x_0$，于是前面的z就变形为： $z=w_{0}x_{0} +w_{1}x_{1} + w_{2}x_{2}$ 。下面代码中加载数据时手动插入一列1。


```python
"""
从 testSet.txt 中加载数据
"""
def loadDataSet():
    dataMat = []                                                        #创建数据列表
    labelMat = []                                                       #创建标签列表
    fr = open('testSet.txt')                                            #打开文件   
    for line in fr.readlines():                                         #逐行读取
        lineArr = line.strip().split()                                  #去回车，放入列表
        dataMat.append([1.0, float(lineArr[0]), float(lineArr[1])])          #添加数据(x,y)
        labelMat.append(int(lineArr[2]))                                #添加标签(分类结果)
    fr.close()                                                          #关闭文件
    return dataMat, labelMat                                            #返回

"""
绘制数据点图
"""
def plotDataSet():
    dataMat, labelMat = loadDataSet()                                   #加载数据集
    dataArr = np.array(dataMat)                                         #转换成numpy的array数组
    n = np.shape(dataMat)[0]                                            #数据个数
    xcord1 = []; ycord1 = []                                            #正样本
    xcord2 = []; ycord2 = []                                            #负样本
    for i in range(n):                                                  #根据数据集标签进行分类
        if int(labelMat[i]) == 1:
            xcord1.append(dataArr[i,1]); ycord1.append(dataArr[i,2])    #1为正样本
        else:
            xcord2.append(dataArr[i,1]); ycord2.append(dataArr[i,2])    #0为负样本
    fig = plt.figure()
    ax = fig.add_subplot(111)                                           #添加subplot
    ax.scatter(xcord1, ycord1, s = 20, c = 'red', marker = 's',alpha=.5,label='1') #绘制1样本
    ax.scatter(xcord2, ycord2, s = 20, c = 'green',alpha=.5,label='0')             #绘制0样本
    plt.legend()
    plt.title('DataSet')                                                #绘制title
    plt.xlabel('x1'); plt.ylabel('x2')                                  #绘制label
    plt.show()                                                          #显示

if __name__ == '__main__':
    plotDataSet()
```

绘制结果：
![0518_logistic_data_plot](/src/imgs/1805/0518_logistic_data_plot.png)

$z=w^Tx$ 这个方程未知的参数为 $w_{0},w_{1},w_{2}$ ，也就是我们需要求的回归系数(最优参数)。

##### 2. 训练Logistic回归算法

回顾一下上面得出的梯度更新公式：$w_j:=w_j-\alpha \cdot dw_j$，再对照伪代码，转化成python代码：

```python
def sigmoid(inX):
    return 1.0 / (1 + np.exp(-inX))

def gradAscent(dataMatIn, classLabels):
    dataMatrix = np.mat(dataMatIn)                                       #变量转换成numpy的mat
    labelMat = np.mat(classLabels).transpose()                           #标签转换成numpy的mat,并进行转置
    m, n = np.shape(dataMatrix)                                          #返回dataMatrix的大小。m为行数,n为列数。
    alpha = 0.001                                                        #移动步长,也就是学习速率,控制更新的幅度。
    maxCycles = 500                                                      #最大迭代次数
    weights = np.ones((n,1))                                             #weights就是要求的特征系数w，全部初始化为1
    for k in range(maxCycles):
        h = sigmoid(dataMatrix * weights)                                #梯度上升矢量化公式
        dY = labelMat - h
        weights = weights + alpha * dataMatrix.transpose() * dY          #对w执行梯度更新
    return weights.getA()                                                #将矩阵转换为数组，返回权重数组

if __name__ == '__main__':
    dataMat, labelMat = loadDataSet()           
    print(gradAscent(dataMat, labelMat))
```

运行结果：

```
[[ 4.12414349]
 [ 0.48007329]
 [-0.6168482 ]]
```

也就是$w_0=4.12414349,w_1=0.48007329,w_2=-0.6168482$

由于决策边界 $w^Tx=0$，可得 $w_0+w_1x_1+w_2x_2=0 => x_2=(-w_0-w_1x_1)/w_2$ 。顺便根据得出的特征值绘制一下预测函数的图像：

```python
def plotBestFit(weights):
    dataMat, labelMat = loadDataSet()                                   #加载数据集
    dataArr = np.array(dataMat)                                         #转换成numpy的array数组
    n = np.shape(dataMat)[0]                                            #数据个数
    xcord1 = []; ycord1 = []                                            #正样本
    xcord2 = []; ycord2 = []                                            #负样本
    for i in range(n):                                                  #根据数据集标签进行分类
        if int(labelMat[i]) == 1:
            xcord1.append(dataArr[i,1]); ycord1.append(dataArr[i,2])    #1为正样本
        else:
            xcord2.append(dataArr[i,1]); ycord2.append(dataArr[i,2])    #0为负样本
    fig = plt.figure()
    ax = fig.add_subplot(111)                                           #添加subplot
    ax.scatter(xcord1, ycord1, s = 20, c = 'red', marker = 's',alpha=.5)#绘制正样本
    ax.scatter(xcord2, ycord2, s = 20, c = 'green',alpha=.5)            #绘制负样本
    x1 = np.arange(-3.0, 3.0, 0.1)
    x2 = (-weights[0] - weights[1] * x1) / weights[2]                   #w0+w1x1+w2x2=0 => x2=(-w0-w1x1)/w2
    ax.plot(x1, x2)
    plt.title('BestFit')                                                #绘制title
    plt.xlabel('x1'); plt.ylabel('x2')                                  #绘制label
    plt.show()       

if __name__ == '__main__':
    dataMat, labelMat = loadDataSet()           
    weights = gradAscent(dataMat, labelMat)
    plotBestFit(weights)
```

如图：

![0518_logistic_line](/src/imgs/1805/0518_logistic_line.png)

- - -

参考：

1. [广义线性模型(Generalized Linear Models)](http://scikit-learn.org/stable/modules/linear_model.html)
2. [Python3《机器学习实战》学习笔记（六）：Logistic回归基础篇之梯度上升算法](https://zhuanlan.zhihu.com/p/28922957)
3. [机器学习之Logistic回归与Python实现](https://blog.csdn.net/moxigandashu/article/details/72779856)
4. [机器学习实战笔记5(logistic回归)](https://blog.csdn.net/lu597203933/article/details/38468303)
5. [Logistic 回归损失函数](https://github.com/Kivy-CN/dlai-notes/blob/master/1.nndl/2.3.md)
6. [Logistic分类函数](https://juejin.im/post/5a4e21b46fb9a01ca267474d)

- - -
THE END.
