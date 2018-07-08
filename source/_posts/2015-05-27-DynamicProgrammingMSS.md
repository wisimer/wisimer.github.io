---
date: 2015-05-27
layout: default
category: ALGORITHM
tags: DynamicProgramming
title: 动态规划-最大子数组和问题
---

### 题目描述

> 有整型数组int a[]={1,-2,3,10,-4,7,2,-5}，求这个数组的最大子数组和

<!--more-->

### 分析

来手写一下求取最大子数组和序列的过程，用M[i]表示第i个数所对应的最大子数组和

```
S[0] = 1 ;
M[0] = 1 ;

S[1] = max{S[0]+a[1],a[1]} = -1 ;
M[1] = max{S[1],M[0]} = 1 ; 

S[2] = max{S[1]+a[2],a[2]} = 3 ;
M[2] = max{S[2],M[1]} = 3 ; 

S[3] = max{S[2]+a[3],a[3]} = 13 ;
M[3] = max{S[3],M[2]} = 13 ; 

S[4] = max{S[3]+a[4],a[4]} = 9 ;
M[4] = max{S[4],M[3]} = 13 ; 

S[5] = max{S[4]+a[5],a[5]} = 16 ;
M[5] = max{S[5],M[4]} = 16 ; 

S[6] = max{S[5]+a[6],a[6]} = 18 ;
M[6] = max{S[6],M[5]} = 18 ; 

S[7] = max{S[6]+a[7],a[7]} = 13 ;
M[7] = max{S[7],M[6]} = 18 ;
```

> 我们求解的问题也就是求M[7]的值，M[i]就是这个问题的状态，可以得到状态转移方程为：M[i] = max{max{S[i-1]+a[i],a[i]},M[i-1]}。其中M[i]表示当前位置i之前所有数的最大子数组和，S[i]则用于保存最大子数组的起始位置到当前位置之间所有元素的和。

### Java代码实现

```java
public class MaxSubSum {
    public static void main(String[] args) {
        int a[]={1,-2,3,10,-4,7,2,-5};
        maxSubSum(a);
    }

    public static void maxSubSum(int[] a) {

        int M = 1;
        int S = 1;
        for (int i = 1; i < a.length ; i++ ) {
            S = maxOf(S+a[i],a[i]);
            System.out.printf("S[%d] = max{S[%d]+a[%d],a[%d]} = %d ;\n",i,i-1,i,i,S);
            M = maxOf(M,S);
            System.out.printf("M[%d] = max{S[%d],M[%d]} = %d ; \n",i,i,i-1,M);
            System.out.println("");
        }
        System.out.println("MAX FINAL : "+M);
      
    }

    public static int maxOf(int a,int b) {
         System.out.println(a+" | "+b);
         if(a>b || a==b) return a;
         else return b;
    }
}
```

- - -
THE END.
