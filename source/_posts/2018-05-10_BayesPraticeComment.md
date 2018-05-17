---
title: 朴素贝叶斯分类算法实践-言论过滤器
date: 2018-05-10
categories: 机器学习
tags: [朴素贝叶斯分类]
---

#### 一、前言
以在线社区留言为例。为了不影响社区的发展，我们要屏蔽侮辱性的言论，所以要构建一个快速过滤器，如果某条留言使用了负面或者侮辱性的语言，那么就将该留言标志为内容不当。过滤这类内容是一个很常见的需求。对此问题建立两个类型：侮辱类和非侮辱类，使用1和0分别表示。

<!--more-->

#### 二、实现

##### 1.准备工作：加载数据

我们把文本看成单词向量或者词条向量，也就是说将句子转换为向量。考虑出现所有文档中的单词，再决定将哪些单词纳入词汇表或者说所要的词汇集合，然后必须要将每一篇文档转换为词汇表上的向量。简单起见，我们先假设已经将本文切分完毕，存放到列表中，并对词汇向量进行分类标注。编写代码如下：

```python
# -*- coding: UTF-8 -*-
def loadDataSet():
    postingList=[['my', 'dog', 'has', 'flea', 'problems', 'help', 'please'],                #切分的词条
                 ['maybe', 'not', 'take', 'him', 'to', 'dog', 'park', 'stupid'],
                 ['my', 'dalmation', 'is', 'so', 'cute', 'I', 'love', 'him'],
                 ['stop', 'posting', 'stupid', 'worthless', 'garbage'],
                 ['mr', 'licks', 'ate', 'my', 'steak', 'how', 'to', 'stop', 'him'],
                 ['quit', 'buying', 'worthless', 'dog', 'food', 'stupid']]
    classVec = [0,1,0,1,0,1]                                                               #类别标签向量，1代表侮辱性词汇，0代表不是
    return postingList,classVec

if __name__ == '__main__':
    postingLIst, classVec = loadDataSet()
    for each in postingLIst:
        print(each)
    print(classVec)
```

我们已经知道贝叶斯分类属于监督学习算法，所以这里的训练数据是6个词条和对应的类别。

我们最终要判断某一个测试词条是否属于侮辱分类是根据之前文章[朴素贝叶斯分类算法原理](http://ai.wisim.me/2018/03/27/2018-03-17_Bayes/)中的贝叶斯公式得到结果，也就是如下公式：

(1).$P(词条属于侮辱类|测试词条中每个词汇的分类) = \frac{P(词条中每个词汇的分类|词条属于侮辱类)}{P(词条中每个词汇的分类)}P(词条属于侮辱类)$

(2).$P(词条属于非侮辱类|测试词条中每个词汇的分类) = \frac{P(词条中每个词汇的分类|词条属于非侮辱类)}{P(词条中每个词汇的分类)}P(词条属于非侮辱类)$

拿（1）公式举例，P(词条属于侮辱类|测试词条中每个词汇的分类)是要求的结果，P(词条中每个词汇的分类|词条属于侮辱类)可以计算出来，P(词条中每个词汇的分类)是提前计算好的，P(词条属于侮辱类)也是已知的0.5。最终计算结果，比较两个结果大小，值更大就表示测试词条属于该类。

##### 2.再创建一个词汇表，并将切分好的词条转换为词条向量。

```Python
"""
根据原始词条创建一个包含所有词汇且不重复的词汇表
"""
def createVocabList(dataSet):
    vocabSet = set([])                      #创建一个空的不重复列表
    for document in dataSet:               
        vocabSet = vocabSet | set(document) #取并集
    return list(vocabSet)

"""
创建一个和词汇表相同大小的列表，标记出输入词条inputSet中的词在词汇表中的位置。如果存在就是1，不存在就是0。
"""
def setOfWords2Vec(vocabList, inputSet):
    returnVec = [0] * len(vocabList)                                     #创建一个其中所含元素都为0的向量
    for word in inputSet:                                                #遍历每个词条
        if word in vocabList:                                            #如果词条存在于词汇表中，则置1
            returnVec[vocabList.index(word)] = 1
        else: print("the word: %s is not in my Vocabulary!" % word)
    return returnVec                                                    #返回文档向量

if __name__ == '__main__':
    #1. 加载数据
    print('postingList:')
    postingList, classVec = loadDataSet()
    for each in postingList:
        print(each)
    print('classVec:\n',classVec)
    #2. 创建词汇表
    myVocabList = createVocabList(postingList)
    print('myVocabList:\n',myVocabList)
    #3. 创建词条向量
    trainMat = []
    for postinDoc in postingList:
        trainMat.append(setOfWords2Vec(myVocabList, postinDoc))
    print('trainMat:\n', trainMat)
```

运行结果：

```
postingList:
['my', 'dog', 'has', 'flea', 'problems', 'help', 'please']
['maybe', 'not', 'take', 'him', 'to', 'dog', 'park', 'stupid']
['my', 'dalmation', 'is', 'so', 'cute', 'I', 'love', 'him']
['stop', 'posting', 'stupid', 'worthless', 'garbage']
['mr', 'licks', 'ate', 'my', 'steak', 'how', 'to', 'stop', 'him']
['quit', 'buying', 'worthless', 'dog', 'food', 'stupid']
classVec:
 [0, 1, 0, 1, 0, 1]

myVocabList:
 ['food', 'has', 'to', 'quit', 'stupid', 'help', 'park', 'so', 'how', 'love', 'not', 'I', 'ate', 'my', 'licks', 'dalmation', 'flea', 'him', 'worthless', 'take', 'steak', 'cute', 'buying', 'stop', 'mr', 'garbage', 'dog', 'is', 'posting', 'problems', 'maybe', 'please']

trainMat:
 [[0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 1],
  [0, 0, 1, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0],
  [0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0],
  [0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 1, 0, 0, 0],
  [0, 0, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0],
  [1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0]]
```

- 从运行结果可以看出，postingList 是原始的词条列表。
- myVocabList 是词汇表。 myVocabList 是所有单词出现的集合，没有重复的元素。
`词汇表` 是用来干什么的？没错，它是用来将词条 `向量化` 的，myVocabList中的一个位置上的单词在某一个原始词条中出现过一次，那么就在相应位置记作1，如果没有出现就在相应位置记作0。这样就构成了一个词条向量。
- trainMat 是所有的词条向量组成的列表。它里面存放的是根据myVocabList向量化的词条向量。

我们已经得到了词条向量。接下来，我们就可以通过词条向量训练朴素贝叶斯分类器。

##### 3.训练数据

```python
"""
根据原始词条和对应分类结果训练贝叶斯分类
"""
def trainNB0(trainMatrix,trainCategory):
    numTrainDocs = len(trainMatrix)                            #计算训练的文档数目
    numWords = len(trainMatrix[0])                             #计算每篇文档的词条数
    pAbusive = sum(trainCategory)/float(numTrainDocs)          #文档属于侮辱类的概率
    p0Num = np.zeros(numWords); p1Num = np.zeros(numWords)     #创建numpy.zeros数组,词条出现数初始化为0
    p0Denom = 0.0; p1Denom = 0.0                               #分母初始化为0
    for i in range(numTrainDocs):
        if trainCategory[i] == 1:                              #统计属于侮辱类的条件概率所需的数据，即P(w0|1),P(w1|1),P(w2|1)···
            p1Num += trainMatrix[i]
            p1Denom += sum(trainMatrix[i])
        else:                                                  #统计属于非侮辱类的条件概率所需的数据，即P(w0|0),P(w1|0),P(w2|0)···
            p0Num += trainMatrix[i]
            p0Denom += sum(trainMatrix[i])
    p1Vect = p1Num/p1Denom                                      
    p0Vect = p0Num/p0Denom         
    return p0Vect,p1Vect,pAbusive                              #返回属于非侮辱类的条件概率数组p0Vect，属于侮辱类的条件概率数组p1Vect，词条属于侮辱类的概率pAbusive


if __name__ == '__main__':
    print('postingList:')
    postingList, classVec = loadDataSet()
    for each in postingList:
        print(each)
    print('classVec:\n',classVec)

    myVocabList = createVocabList(postingList)
    print('myVocabList:\n',myVocabList)

    trainMat = []
    for postinDoc in postingList:
        trainMat.append(setOfWords2Vec(myVocabList, postinDoc))
    print('trainMat:\n', trainMat)

    p0V, p1V, pAb = trainNB0(trainMat, classVec)
    print('p0V:\n', p0V)            #p0V: 属于非侮辱类的条件概率数组
    print('p1V:\n', p1V)            #p1V: 属于侮辱类的条件概率数组
    print('classVec:\n', classVec)  #classVec: 原始词条分类结果
    print('pAb:\n', pAb)            #pAb: 文档属于侮辱类的概率
```

运行结果如下:

```
p0V:
 [0.         0.04166667 0.04166667 0.         0.         0.04166667
  0.         0.04166667 0.04166667 0.04166667 0.         0.04166667
  0.04166667 0.125      0.04166667 0.04166667 0.04166667 0.08333333
  0.         0.         0.04166667 0.04166667 0.         0.04166667
  0.04166667 0.         0.04166667 0.04166667 0.         0.04166667
  0.         0.04166667]
p1V:
 [0.05263158 0.         0.05263158 0.05263158 0.15789474 0.
  0.05263158 0.         0.         0.         0.05263158 0.
  0.         0.         0.         0.         0.         0.05263158
  0.10526316 0.05263158 0.         0.         0.05263158 0.05263158
  0.         0.05263158 0.10526316 0.         0.05263158 0.
  0.05263158 0.        ]
classVec:
 [0, 1, 0, 1, 0, 1]
pAb:
 0.5
```

- p0V存放的是每个单词属于类别0，也就是非侮辱类词汇的概率。比如p0V的倒数第6个概率，就是stupid这个单词属于非侮辱类的概率为0。
- 同理，p1V的倒数第6个概率，就是[stupid](https://translate.google.cn/#en/zh-CN/stupid)这个单词属于侮辱类的概率为0.15789474，也就是约等于15.79%的概率。显而易见，这个单词属于侮辱类。
- pAb是所有侮辱类的样本占所有样本的概率，从classVec中可以看出，共有3个侮辱类，3个非侮辱类。所以侮辱类的概率是0.5。

因此p0V存放的就是P(him|非侮辱类) = 0.0833、P(is|非侮辱类) = 0.0417，一直到P(dog|非侮辱类) = 0.0417，这些单词的条件概率。表示词条是非侮辱类的前提下，这些单词是属于非侮辱类的概率。

同理，p1V存放的就是在词条属于侮辱类的前提下，各个单词在属于侮辱类的条件概率。

pAb就是先验概率。

已经训练好分类器，接下来，使用分类器进行分类。

##### 4.使用贝叶斯分类器

```python
def classifyNB(vec2Classify, p0Vec, p1Vec, pClass1):
    p1 = reduce(lambda x,y:x*y, vec2Classify * p1Vec) * pClass1                #对应元素相乘
    p0 = reduce(lambda x,y:x*y, vec2Classify * p0Vec) * (1.0 - pClass1)
    print('p0:',p0)
    print('p1:',p1)
    if p1 > p0:
        return 1
    else:
        return 0

def testingNB():
    listOPosts,listClasses = loadDataSet()                              #创建实验样本
    myVocabList = createVocabList(listOPosts)                           #创建词汇表
    trainMat=[]
    for postinDoc in listOPosts:
        trainMat.append(setOfWords2Vec(myVocabList, postinDoc))         #将实验样本向量化
    p0V,p1V,pAb = trainNB0(np.array(trainMat),np.array(listClasses))    #训练朴素贝叶斯分类器

    testEntry = ['love', 'my', 'dalmation']                             #测试样本1
    thisDoc = np.array(setOfWords2Vec(myVocabList, testEntry))          #测试样本向量化
    if classifyNB(thisDoc,p0V,p1V,pAb):
        print(testEntry,'属于侮辱类')                                     #执行分类并打印分类结果
    else:
        print(testEntry,'属于非侮辱类')                                   #执行分类并打印分类结果

    testEntry = ['stupid', 'garbage']                                   #测试样本2
    thisDoc = np.array(setOfWords2Vec(myVocabList, testEntry))          #测试样本向量化
    if classifyNB(thisDoc,p0V,p1V,pAb):
        print(testEntry,'属于侮辱类')                                     #执行分类并打印分类结果
    else:
        print(testEntry,'属于非侮辱类')                                   #执行分类并打印分类结果

if __name__ == '__main__':
   testingNB()
```

这里的`classifyNB`函数还是根据下面两个公式得出的，由于只要比较大小，两个公式中 `P(词条中每个词汇的分类)` 值相同，所以只要计算比较P(词条中每个词汇的分类|词条属于侮辱类)P(词条属于侮辱类)和P(词条中每个词汇的分类|词条属于非侮辱类)P(词条属于非侮辱类)的大小。

(1).$P(词条属于侮辱类|测试词条中每个词汇的分类) = \frac{P(词条中每个词汇的分类|词条属于侮辱类)}{P(词条中每个词汇的分类)}P(词条属于侮辱类)$

(2).$P(词条属于非侮辱类|测试词条中每个词汇的分类) = \frac{P(词条中每个词汇的分类|词条属于非侮辱类)}{P(词条中每个词汇的分类)}P(词条属于非侮辱类)$

计算的结果：
```
p0: 0.0
p1: 0.0
['love', 'my', 'dalmation'] 属于非侮辱类
p0: 0.0
p1: 0.0
['stupid', 'garbage'] 属于非侮辱类
```

但是这样写的算法无法进行分类，p0和p1的计算结果都是0，最终分类都是非侮辱类，显然结果错误。这是为什么呢？之前已经提过，利用贝叶斯分类器对文档进行分类时，要计算多个概率的乘积以获得文档属于某个类别的概率，即计算 p(w_{0}|1)p(w_{1}|1)p(w_{2}|1)...p(w_{n}|1)  。如果其中有一个概率值为0，那么最后的计算结果也为0。

##### 5.平滑处理

为了降低这种影响，可以将所有词的出现数初始化为1，并将分母初始化为2。这种做法就叫做拉普拉斯平滑(Laplace Smoothing)又被称为加1平滑，是比较常用的平滑方法，它就是为了解决0概率问题。

除此之外，另外一个遇到的问题就是下溢出，这是由于太多很小的数相乘造成的。学过数学的人都知道，两个小数相乘，越乘越小，这样就造成了下溢出。在程序中，在相应小数位置进行四舍五入，计算结果可能就变成0了。为了解决这个问题，对乘积结果取自然对数。通过求对数可以避免下溢出或者浮点数舍入导致的错误。同时，采用自然对数进行处理不会有任何损失。

根据上面这两种改进方法，改进之后的trainNB0方法和classifyNB方法：

```python
def trainNB0(trainMatrix,trainCategory):
    numTrainDocs = len(trainMatrix)                         #计算训练的文档数目
    numWords = len(trainMatrix[0])                          #计算每篇文档的词条数
    pAbusive = sum(trainCategory)/float(numTrainDocs)       #文档属于侮辱类的概率
    p0Num = np.ones(numWords); p1Num = np.ones(numWords)    #创建numpy.ones数组,词条出现数初始化为1，拉普拉斯平滑
    p0Denom = 2.0; p1Denom = 2.0                            #分母初始化为2,拉普拉斯平滑
    for i in range(numTrainDocs):
        if trainCategory[i] == 1:                           #统计属于侮辱类的条件概率所需的数据，即P(w0|1),P(w1|1),P(w2|1)···
            p1Num += trainMatrix[i]
            p1Denom += sum(trainMatrix[i])
        else:                                               #统计属于非侮辱类的条件概率所需的数据，即P(w0|0),P(w1|0),P(w2|0)···
            p0Num += trainMatrix[i]
            p0Denom += sum(trainMatrix[i])
    p1Vect = np.log(p1Num/p1Denom)                          #取对数，防止下溢出         
    p0Vect = np.log(p0Num/p0Denom)         
    return p0Vect,p1Vect,pAbusive                           #返回属于侮辱类的条件概率数组，属于非侮辱类的条件概率数组，文档属于侮辱类的概率

def classifyNB(vec2Classify, p0Vec, p1Vec, pClass1):
    p1 = sum(vec2Classify * p1Vec) + np.log(pClass1)        #对应元素相乘。logA * B = logA + logB，所以这里加上log(pClass1)
    p0 = sum(vec2Classify * p0Vec) + np.log(1.0 - pClass1)
    print('p0:',p0)
    print('p1:',p1)
    if p1 > p0:
        return 1
    else:
        return 0
```

最终计算出来的分类结果：

```
p0: -7.694848072384611
p1: -9.826714493730215
['love', 'my', 'dalmation'] 属于非侮辱类
p0: -7.20934025660291
p1: -4.702750514326955
['stupid', 'garbage'] 属于侮辱类
```

- - -
参考：

1. [Python3《机器学习实战》学习笔记（四）：朴素贝叶斯基础篇之言论过滤器](https://zhuanlan.zhihu.com/p/28719332)
2. [Python3《机器学习实战》学习笔记（五）：朴素贝叶斯实战篇之新浪新闻分类](https://zhuanlan.zhihu.com/p/28720393)

附：

[code](/raw/code/BayesClassify/BayesClassify_CommentClassify.ipynb.ipynb)

- - -
THE END.
