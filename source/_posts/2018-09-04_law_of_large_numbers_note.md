---
title: 大数定理随笔
date: 2018-09-04
categories: MATH
tags: [大数定理]
---

在数学与统计学中，大数定律又称大数法则、大数律，是描述相当多次数重复实验的结果的定律。根据这个定律知道，样本数量越多，则其平均就越趋近期望值。

<!--more-->

大数定律主要有两种表现形式：弱大数定律和强大数定律。定律的两种形式都肯定无疑地表明，样本均值：

$\overline{X}_n=\frac{1}{n}(X_1+X_2+...+X_n)$

收敛于期望值：

$\overline{X}_n \rightarrow u , n\rightarrow\infty$

其中 $X_1, X_2, ...$ 是独立同分布、期望值 $E(X_1)=E(X_2)=...=u$ ,且皆勒贝格可积的随机变量构成的无穷序列。$X_j$ 的勒贝格可积性意味着期望值 $E(X_j)$ 存在且有限。

方差 $Var(X_1)=Var(X_2)=...=\sigma^2<\infty$ 有限的假设是非必要的。很大或者无穷大的方差会使其收敛得缓慢一些，但大数定律仍然成立。通常采用这个假设来使证明更加简洁。

强（大数定理）和弱（大数定理）之间的差别在所断言的收敛的方式。

#### 弱大数定律

弱大数定律也称为辛钦定理，陈述为：样本均值`依概率收敛`于期望值

${ {\overline {X}}_{n}\ {\xrightarrow {P}}\ \mu \quad {\textrm {as}}\quad n\to \infty }$
也就是说对于任意正数 $\varepsilon$ 有${ \lim _{n\to \infty }P\left(\,|{\overline {X}}_{n}-\mu |>\varepsilon \,\right)=0}$

#### 强大数定律

强大数定律指出，样本均值`以概率1收敛`于期望值。

${ {\overline {X}}_{n}\ {\xrightarrow {\text{a.s.}}}\ \mu \quad {\textrm {as}}\quad n\to \infty }$,即 ${ P\left(\lim _{n\to \infty }{\overline {X}}_{n}=\mu \right)=1}$

#### 切比雪夫定理的特殊情况

设 ${a_{1},\ a_{2},\ \dots \ ,\ a_{n},\ \dots }$ 为相互独立的随机变量，其数学期望为： ${E(a_{i})=\mu \quad (i=1,\ 2,\ \dots )}$，方差为：${Var (a_{i})=\sigma ^{2}\quad (i=1,\ 2,\ \dots )}$

则序列 ${\overline {a}}={\frac {1}{n}}\sum _{i=1}^{n}a_{i}$ 依概率收敛于 $\mu$（即收敛于此数列的数学期望 $E(a_{i})$。

> 换言之，在定理条件下，当 n 无限变大时，n 个随机变量的算术平均将变成一个常数。

#### 伯努利大数定律

设在 n 次独立重复伯努利试验中，事件 X 发生的次数为 $n_{x}$ 。事件 X 在每次试验中发生的母体机率为 p。
${\frac {n_{x}}{n}}$ 代表样本发生事件 X 的频率。

大数定律可用机率极限值定义: 则对任意正数 $\varepsilon >0$，下式成立：

$\lim _{n\to \infty }{P{\left\{\left|{\frac {n_{x}}{n}}-p\right|<\varepsilon \right\}}}=1$

定理表明事件发生的频率依机率收敛于事件的母体机率。
定理以严格的数学形式表达了频率的稳定性。
就是说当 n 很大时，事件发生的频率于母体机率有较大偏差的可能性很小。

- - -
THE END.