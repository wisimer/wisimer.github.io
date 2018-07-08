---
date: 2015-05-13
layout: default
category: ALGORITHM
tags: DynamicProgramming
title: 动态规划-最少硬币问题
---

### 题目描述

> 如果我们有面值为1元、3元和5元的硬币若干枚，如何用最少的硬币凑够11元？

<!--more-->

### 分析

来手写一下求取最小个数的过程，用MIN[i]表示要凑得i元所需的最少硬币个数

```
MIN[0] = 0
MIN[1] = min{MIN[1-1]+1} = 1
MIN[2] = min{MIN[2-1]+1} = 2
MIN[3] = min{MIN[3-1]+1,MIN[3-3]+1} = min{3,1} = 1
MIN[4] = min{MIN[4-1]+1,MIN[4-3]+1} = min{2,2} = 2
MIN[5] = min{MIN[5-1]+1,MIN[5-3]+1,MIN[5-5]+1} = min{3,3,1} = 1
MIN[6] = min{MIN[6-1]+1,MIN[6-3]+1,MIN[6-5]+1} = min{2,2,2} = 2
MIN[7] = min{MIN[7-1]+1,MIN[7-3]+1,MIN[7-5]+1} = min{3,3,3} = 3
MIN[8] = min{MIN[8-1]+1,MIN[8-3]+1,MIN[8-5]+1} = min{4,2,2} = 2
MIN[9] = min{MIN[9-1]+1,MIN[9-3]+1,MIN[9-5]+1} = min{3,3,3} = 3
MIN[10] = min{MIN[10-1]+1,MIN[10-3]+1,MIN[10-5]+1} = min{4,7,2} = 2
MIN[11] = min{MIN[11-1]+1,MIN[11-3]+1,MIN[11-5]+1} = min{3,3,3} = 3
```

> 我们求解的问题也就是求MIN[11]的值，MIN[i]就是这个问题的状态，可以得到状态转移方程为：MIN[i] = min{MIN[i-Vj]+1},其中i表示要凑i枚硬币，Vj表示第j枚硬币的面值，在这里Vj分别为1,3,5，且0<=i-Vj<i.

### Java代码实现

```java
public class MinCoins {
    public static void main(String[] args) {
        int[] a = new int[]{1,3,5};
        minCoins(a,11);
    }

    public static void minCoins(int[] a,int x) {

        int[] MIN = new int[x+1];

        MIN[0] = 0;

        for (int i = 0 ; i < MIN.length; i++) {
            //每次循环首先都要将MIN[i]设值i，也就置为最大值。
            MIN[i] = i;
            for (int j = 0;j<a.length;j++) {
                //接着要判断这次要凑的硬币是否大于最基本的三枚硬币的面值{1,3,5}，并且去除某个面值之后的硬币个数加1的总个数要小于刚刚设置的最大值
                if (i>=a[j] && (MIN[i-a[j]]+1)<MIN[i]) {
                    //要保存此次得到的最小值
                    MIN[i] = MIN[i-a[j]]+1;
                    System.out.println("Temp --> MIN["+i+"] = "+MIN[i]);
                }

            }

           System.out.println("MIN["+i+"] = "+MIN[i]);
        }
    }
}
```

- - -
THE END.
