---
date: 2017-11-22
layout: default
category: Alogrithm
tags: DP
title: 动态规划-数对之差最大值
---

### 题目描述

> 在数组中，某个数字减去它右边的数字得到一个数对之差。求所有数对之差的最大值。例如，在数组{2,4,1,16,7,5,11,9}中，数对之差的最大值是11，是16减去5的结果。

<!--more-->

### 分析

> 定义D[i]是以数组中第i个数字为减数的所有数对之差的最大值。根据题意，用N[i]表示数组的第i个元素，就是说对于任意h<i，D[i]>=N[h]-N[i]。所以D[i]的最大值就是整个数组的最大的数对之差。来手写求一下最大数对之差的过程。


```
M[0] = 2;
D[1] = M[0] - N[1] = 2 - 4 = -2;
maxDiff = 2;

M[1] = max{M[0],N[1]} = max{2,4} = 4;
D[2] = M[1] - N[2] = 4 - 1 = 3;
maxDiff = max{maxDiff,D[2]} = 3;

M[2] = max{M[1],N[2]} = max{4,1} = 4;
D[3] = M[2] - N[3] = -12;
maxDiff = max{maxDiff,D[3]} = 3;

M[3] =16;
D[4] = 9;
maxDiff = 9;

M[4] = 16;
D[5] = 11;
maxDiff = 11;

M[5] = 16;
D[6] = 5;
maxDiff = 11;

M[6] = 16;
D[7] = 7;
maxDiff = 11;
```


> 这里的 M[i-1] 表示的是当前第i个元素之前的i-1个元素中的最大值。而maxDiff则是i之前包括位置i在内的最大数对之差的值。

### C代码实现

```
int MaxDiff(int numbers[], int length) {
    if(numbers == NULL || length < 2)
        return 0;
    int max = numbers[0];
    int maxDiff = max - numbers[1];
    for(int i=2;i<length;++i) {
        if(numbers[i] < max) {
            max = numbers[i];
        }
        int curDiff = max - numbers[i];
        if(curDiff > maxDiff) {
            maxDiff = curDiff;
        }
    }
    return maxDiff;
}
```

- - -
THE END.
