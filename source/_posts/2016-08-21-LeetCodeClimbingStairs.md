---
date: 2016-08-21
title: LeetCode:70. Climbing Stairs
tags: LeetCode
category: Alogrithm
layout: default
---

### [LeetCode:70. Climbing Stairs](https://leetcode.com/problems/climbing-stairs/)

```
You are climbing a stair case. It takes n steps to reach to the top.
Each time you can either climb 1 or 2 steps. In how many distinct ways can you climb to the top?
```
<!--more-->

题目的意思就是让你爬n阶提子，但是每次只能爬一个阶梯或者两个阶梯。让你求出所有的可能性。

想想看，由于只能爬1或者2，所以我们爬一个阶梯只有一种可能性，但是爬两个阶梯就有两种可能，也就是两个1或者一个2。

这样举个例子，思考一下，因为只能爬1或者2个阶梯，所以爬到4只有两个可能，就是从3爬一步爬到4或者从2爬两步爬到4。因此爬到4的可能性也就确定了，也就是从0爬到3的可能性以及从0爬到2的可能性之和。再往前看，爬到3有几种可能：从1爬两步爬到3，以及从2爬一步爬到3。爬到2的可能性：从1爬一步到2或者从0爬两步到2。这样就可以算到从0爬到4的可能性了。来看一下Java代码实现 ：

```java
public class Solution {
    public int climbStairs(int n) {
        //将1，2两种步伐抽象成一个数组，这样下面的代码对其他的步伐也适用
        int[] arr = new int[]{1,2};
        //每个阶梯的可能的情况都放在res数组对应的位置
        int[] res = new int[n+1];
        if(n < arr[0]) {
            return 0;
        }
        for(int i = 1 ; i < n+1 ; i++) {
            //循环每种步伐
            for(int j = 0 ; j < arr.length;j++) {
                if(i == arr[j]) {
                    //如果等于步伐中的某一步，则该位置的结果增加自身这一种可能性
                    res[i]+=1;
                } else if(i > arr[j]){
                    //如果大于步伐，则减去该步伐所得位置的可能性要加在当前位置的可能性里面。
                    res[i]+=res[i-arr[j]];
                } else {
                    break;
                }
            }
            //System.out.println(i+ " : " +res[i]);
        }
        return res[n];
    }
}
```
- - -
THE END.
