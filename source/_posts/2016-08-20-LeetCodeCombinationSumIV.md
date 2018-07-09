---
date: 2016-08-20
title: LeetCode:377. Combination Sum IV
tags: Leetcode
category: ALGORITHM
layout: default
---

### [LeetCode:377. Combination Sum IV](https://Leetcode.com/problems/combination-sum-iv/)

```
Given an integer array with all positive numbers and no duplicates, find the number of possible combinations that add up to a positive integer target.

Example:

nums = [1, 2, 3]
target = 4

The possible combination ways are:
(1, 1, 1, 1)
(1, 1, 2)
(1, 2, 1)
(1, 3)
(2, 1, 1)
(2, 2)
(3, 1)

Note that different sequences are counted as different combinations.

Therefore the output is 7.
```

<!--more-->

题目的意思就是给你一个正整数数组，里面的元素不重复（未排序）。再给你一个目标数值，要你用数组里面的数组合起来的和是目标数值，求出有多少种可能。

这道题目有点像那个[爬梯子](https://Leetcode.com/problems/climbing-stairs/)的题目。同时也可以看看这篇文章[ClimbingStairs](http://wisim.me/Leetcode/2016/08/21/LeetCode_ClimbingStairs.html)。

两道题目有异曲同工之妙，我们来思考一下这道题目。如果我要求和为4的所有可能性，首先要求(4-1),(4-2),(4-3)的可能性，它们的和就是4的可能性。来看一下Java代码实现：

```java
public class Solution {
    public int combinationSum4(int[] nums, int target) {
        Arrays.sort(nums);
        int[] res = new int[target+1];
        for (int i = 1 ; i < target+1;i++) {
            for(int num : nums) {
                if(i == num) {
                    res[i]+=1;
                } else if(i > num) {
                    res[i]+= res[i-num];
                } else {
                    break;
                }
            }
        }
        return res[target];
    }
}
```
- - -
THE END
