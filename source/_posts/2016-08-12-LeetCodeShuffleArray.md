---
date: 2016-08-12
title: LeetCode:384. Shuffle an Array
tags: LEETCODE
category: ALGORITHM
layout: default
---

### [LeetCode:384. Shuffle an Array](https://leetcode.com/problems/shuffle-an-array/)

> Shuffle a set of numbers without duplicates.

<!--more-->

洗牌算法，题目的意思就是交换数组元素的位置，并且保证每种情况出现的概率是一样大。

一开始的思路呢，就是生成一个随机数(0到数组长度之间)，把这个随机数对应位置元素插入到一个等长的新数组中，如果生成的随机数已经出现过，则继续生成。直到新的数组放满。

后来呢，发现重复生成随机数会导致Time Limit Exceeded。值得再考虑考虑了。怎样才能保证生成的随机数即使重复了也有效呢。

> 首先在数组末尾设置一个标兵。每次生成的随机数（0到标兵位置之间）,把对应位置的元素和数组的标兵元素交换，标兵再向前移动一位。

这样就保证了即使随机数重复，对应位置的元素由于之前已经交换过，所以实际位置的元素并不会重复。而标兵位置之后的元素都是已经出现过的不会再出现了。看一下Java代码实现：

```java
import java.util.Random;
public class Solution {

    private int[] mNums;
    public Solution(int[] nums) {
        mNums = nums;
    }

    public int[] reset() {

        return mNums;
    }

    public int[] shuffle() {
        int len = mNums.length;
        int pos = len-1;
        int[] arr = new int[len];
        for(int i = 0 ; i < len;i++) {
            arr[i] = mNums[i];
        }
        Random random = new Random();
        while(pos > 0) {
             int ranNum =  random.nextInt(pos+1);
             int temp = arr[pos];
             arr[pos] = arr[ranNum];
             arr[ranNum] = temp;
             pos--;
        }
        return arr;
    }
}

```
- - -
THE END.
