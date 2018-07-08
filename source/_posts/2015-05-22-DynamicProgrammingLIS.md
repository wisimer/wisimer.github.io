---
date: 2015-05-22
layout: default
category: ALGORITHM
tags: DynamicProgramming
title: 动态规划-最长非降子序列
---

### 题目描述

> 有序列5，3，4，8，6，7，求它的最长非降子序列的长度

<!--more-->

### 分析

来手写一下求取最长非降子序列的过程，用MAX[i]表示 `第i个数所对应的最长非降子序列的长度`

```
MAX[1] = 1;//5是第一个数，所以只有5这一个数,MAX[1]=1
MAX[2] = max{1} = 1;//因为3之前的所有数都大于3，所以只有3这一个数
MAX[3] = max{1,MAX[2]+1} = 2;//因为4前面有3小于4，所以MAX[3]=2
MAX[4] = max{1,MAX[1]+1,MAX[2]+1,MAX[3]+1} = max{1,2,2,3} = 3;//8之前的最长非降序列为{3,4}，所以加上8就是MAX[4]=3
MAX[5] = max{1,MAX[1]+1,MAX[2]+1,MAX[3]+1} = max{1,2,2,3} = 3;
MAX[6] = max{1,MAX[1]+1,MAX[2]+1,MAX[3]+1,MAX[5]+1} = max{1,2,3,4} = 4;
```

> 我们求解的问题也就是求MAX[6]的值，MAX[i]就是这个问题的状态，可以得到状态转移方程为：MAX[i] = max{1,MAX[j]+1}，其中j<i,且a[j]<a[i]，也就是说j这个位置的元素小于i位置的元素

### Java代码实现

```java
public class LIS {

    public static void lis(int[] a) {

        int[] MAX = new int[a.length];
        MAX[0] = 1;
        for (int i = 1;i<a.length;i++) {
            MAX[i] = 1;
            for (int j = 0;j<i;j++) {
              if (a[j]<a[i] && MAX[j]+1>MAX[i]) {
                  MAX[i] = MAX[j]+1;
                  System.out.println("Temp Max["+i+"] = "+MAX[i]);
              }
            }

            System.out.println("Final -->  Max["+i+"] = "+MAX[i]);
        }

    }

    public static void main(String[] args) {

        int[] a = new int[]{5,3,4,8,6,7};
        lis(a);
    }
}
```

- - -
THE END.
