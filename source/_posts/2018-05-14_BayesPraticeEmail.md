---
title: 朴素贝叶斯分类算法实践-垃圾邮件过滤器
date: 2018-05-14
categories: 机器学习
tags: [朴素贝叶斯分类]
---

#### 一、前言

这个例子中，我们将了解朴素贝叶斯的一个最著名的应用：电子邮件垃圾过滤。

首先看一下使用朴素贝叶斯对电子邮件进行分类的步骤：

1. 收集数据：提供文本文件。
2. 准备数据：将文本文件解析成词条向量。
3. 分析数据：检查词条确保解析的正确性。
4. 训练算法：使用我们之前建立的trainNB0()函数。
5. 测试算法：使用classifyNB()，并构建一个新的测试函数来计算文档集的错误率。
6. 使用算法：构建一个完整的程序对一组文档进行分类，将错分的文档输出到屏幕上。

<!--more-->

#### 二、实现

##### 1. 收集数据
数据我已经为大家准备好了，可以在[这里](/raw/code/BayesClassify/email.zip)下载：

有两个文件夹ham和spam，ham文件夹下是正常邮件，spam文件夹下的txt文件为垃圾邮件。

##### 2. 准备数据
对于英文文本，我们可以以非字母、非数字作为符号进行切分，使用split函数即可。编写代码如下：

```python
import re

"""
接收一个文本里的字符串并将其解析为字符串列表
"""
def textParse(bigString):                                                   #将字符串转换为字符列表
    listOfTokens = re.split(r'\W*', bigString)                              #将特殊符号作为切分标志进行字符串切分，即非字母、非数字
    return [tok.lower() for tok in listOfTokens if len(tok) > 2]            #除了单个字母，例如大写的I，其它单词变成小写

"""
将切分的实验样本词条整理成不重复的词条列表，也就是词汇表
"""
def createVocabList(dataSet):
    vocabSet = set([])                      #创建一个空的不重复列表
    for document in dataSet:               
        vocabSet = vocabSet | set(document) #取并集
    return list(vocabSet)

if __name__ == '__main__':
    docList = []; classList = []
    for i in range(1, 26):                                                  #遍历25个txt文件
        wordList = textParse(open('email/spam/%d.txt' % i, 'r', encoding = "ISO-8859-1").read())     #读取每个垃圾邮件，并字符串转换成字符串列表
        docList.append(wordList)
        classList.append(1)                                                 #标记垃圾邮件，1表示垃圾文件
        wordList = textParse(open('email/ham/%d.txt' % i, 'r', encoding = "ISO-8859-1").read())      #读取每个非垃圾邮件，并字符串转换成字符串列表
        docList.append(wordList)
        classList.append(0)                                                 #标记非垃圾邮件，1表示垃圾文件   
    vocabList = createVocabList(docList)                                    #创建词汇表，不重复
    print(vocabList)
```

这里注意要加上 `encoding` 参数，才能正确读取文本内容，否则会报错。运行之后就得到所有邮件文本的词汇表，这里就不展示里。

##### 3. 构建词向量模型/词袋模型

```python
"""
词向量模：根据vocabList词汇表，型创建词向量
"""
def setOfWords2Vec(vocabList, inputSet):
    returnVec = [0] * len(vocabList)                #创建一个其中所含元素都为0的向量
    for word in inputSet:                           #遍历每个词条
        if word in vocabList:                       #如果词条存在于词汇表中，则置1
            returnVec[vocabList.index(word)] = 1
    return returnVec                                #返回文档向量

"""
词袋量模：根据vocabList词汇表，型创建词袋模型
"""
def bagOfWords2VecMN(vocabList, inputSet):
    returnVec = [0]*len(vocabList)                   #创建一个其中所含元素都为0的向量
    for word in inputSet:                            #遍历每个词条
        if word in vocabList:                        #如果词条存在于词汇表中，则计数加一
            returnVec[vocabList.index(word)] += 1
    return returnVec  
```

两种模型里面选择一种即可，这里选择的是词向量模型

##### 4. 训练分类器

```python
"""
训练分类器
"""
def trainNB0(trainMatrix,trainCategory):
    numTrainDocs = len(trainMatrix)                         #计算训练的文档数目
    numWords = len(trainMatrix[0])                          #计算每篇文档的词条数
    pAbusive = sum(trainCategory)/float(numTrainDocs)       #文档属于侮辱类的概率
    p0Num = np.ones(numWords); p1Num = np.ones(numWords)    #创建numpy.ones数组,词条出现数初始化为1，拉普拉斯平滑
    p0Denom = 2.0; p1Denom = 2.0                            #分母初始化为2,拉普拉斯平滑
    for i in range(numTrainDocs):
        if trainCategory[i] == 1:            #统计属于垃圾邮件的条件概率所需的数据，即P(w0|1),P(w1|1),P(w2|1)···
            p1Num += trainMatrix[i]
            p1Denom += sum(trainMatrix[i])
        else:                                #统计属于正常邮件的条件概率所需的数据，即P(w0|0),P(w1|0),P(w2|0)···
            p0Num += trainMatrix[i]
            p0Denom += sum(trainMatrix[i])
    p1Vect = np.log(p1Num/p1Denom)           #取对数，防止下溢出         
    p0Vect = np.log(p0Num/p0Denom)         
    return p0Vect,p1Vect,pAbusive            #返回属于正常邮件的条件概率数组，属于垃圾邮件的条件概率数组，文档属于垃圾邮件的概率
```

##### 5. 测试分类器

```python
def classifyNB(vec2Classify, p0Vec, p1Vec, pClass1):
    p1 = sum(vec2Classify * p1Vec) + np.log(pClass1) #对应元素相乘。logA * B = logA + logB，所以这里加上log(pClass1)
    p0 = sum(vec2Classify * p0Vec) + np.log(1.0 - pClass1)
    if p1 > p0:
        return 1
    else:
        return 0
```

##### 6. 使用分类器

```python
def spamTest():
    docList = []; classList = []; fullText = []
    for i in range(1, 26):                                                  #遍历25个txt文件
        wordList = textParse(open('email/spam/%d.txt' % i, 'r').read())     #读取每个垃圾邮件，并字符串转换成字符串列表
        docList.append(wordList)
        fullText.append(wordList)
        classList.append(1)                                                 #标记垃圾邮件，1表示垃圾文件
        wordList = textParse(open('email/ham/%d.txt' % i, 'r').read())      #读取每个非垃圾邮件，并字符串转换成字符串列表
        docList.append(wordList)
        fullText.append(wordList)
        classList.append(0)                                                 #标记非垃圾邮件，1表示垃圾文件   
    vocabList = createVocabList(docList)                                    #创建词汇表，不重复
    trainingSet = list(range(50)); testSet = []                             #创建存储训练集的索引值的列表和测试集的索引值的列表                       
    for i in range(10):                                                     #从50个邮件中，随机挑选出40个作为训练集,10个做测试集
        randIndex = int(random.uniform(0, len(trainingSet)))                #随机选取索索引值
        testSet.append(trainingSet[randIndex])                              #添加测试集的索引值
        del(trainingSet[randIndex])                                         #在训练集列表中删除添加到测试集的索引值
    trainMat = []; trainClasses = []                                        #创建训练集矩阵和训练集类别标签系向量             
    for docIndex in trainingSet:                                            #遍历训练集
        trainMat.append(setOfWords2Vec(vocabList, docList[docIndex]))       #将生成的词集模型添加到训练矩阵中
        trainClasses.append(classList[docIndex])                            #将类别添加到训练集类别标签系向量中
    p0V, p1V, pSpam = trainNB0(np.array(trainMat), np.array(trainClasses))  #训练朴素贝叶斯模型
    errorCount = 0                                                          #错误分类计数
    for docIndex in testSet:                                                #遍历测试集
        wordVector = setOfWords2Vec(vocabList, docList[docIndex])           #测试集的词集模型
        if classifyNB(np.array(wordVector), p0V, p1V, pSpam) != classList[docIndex]:    #如果分类错误
            errorCount += 1                                                 #错误计数加1
            print("分类错误的测试集：",docList[docIndex])
    print('错误率：%.2f%%' % (float(errorCount) / len(testSet) * 100))


if __name__ == '__main__':
    spamTest()
```

运行结果：

```
分类错误的测试集： ['yeah', 'ready', 'may', 'not', 'here', 'because', 'jar', 'jar', 'has', 'plane', 'tickets', 'germany', 'for']
错误率：10.00%
/Users/wxp/anaconda3/lib/python3.6/re.py:212: FutureWarning: split() requires a non-empty pattern match.
  return _compile(pattern, flags).split(string, maxsplit)
```

由于测试数据是随机选的几个邮件文本，所以每次运行结果都可能不一样。

- - -

参考：

1. [Python3《机器学习实战》学习笔记（五）：朴素贝叶斯实战篇之新浪新闻分类](https://zhuanlan.zhihu.com/p/28720393)

附：

[code](/raw/code/BayesClassify/BayesClassify_EmailClassify.ipynb)

- - -
THE END.
