---
date: 2016-08-13
title: LeetCode:378. Kth Smallest Element in a Sorted Matrix
tags: LeetCode
category: Alogrithm
layout: default
---

### [LeetCode:378. Kth Smallest Element in a Sorted Matrix](https://leetcode.com/problems/kth-smallest-element-in-a-sorted-matrix/)

```
Given a n x n matrix where each of the rows and columns are sorted in ascending order, find the kth smallest element in the matrix.
Note that it is the kth smallest element in the sorted order, not the kth distinct element.

Example:
matrix = [
   [ 1,  5,  9],
   [10, 11, 13],
   [12, 13, 15]
],
k = 8,
return 13.

Note:You may assume k is always valid, 1 ≤ k ≤ n2.
```
<!--more-->

题目的意思呢就是给你一个n x n的矩阵，而这个矩阵的每一行以及每一列的元素大小都是递增（也可以相等）。让你找到第k小的那个元素。值得注意的是第k小的意思并不是绝对第k小。怎么讲呢，比如上面的example中的两个13，它们分别是第7小和第8小。也就是想等的元素也各自算一个k。

拿到手第一个想到的动态规划，不过后来发现题目理解错了，那就快排呗，不过这也达不到hard的level啊。构造一个搜索二叉树？嗯也是可以的，不过代价还是有点大。看了讨论之后发现居然直接可以使用 `PriorityQueue` 来解决。我真是越来越不懂这套路了。

1. 优先队列PriorityQueue的本质是最小堆，每次插入和删除时都会动态更新堆中元素的位置。这就给我们提供了方便的解法了。

2. 后来转念一想，干嘛不直接放到List里面，然后一顿排序。岂不快哉。

看一下Java代码：

```java
public class KthSmallest {
    public int kthSmallest(int[][] matrix, int k) {
        Comparator<Integer> comparator = new IntegerLengthComparator();
        PriorityQueue<Integer> queue =
            new PriorityQueue<Integer>(10, comparator);
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                queue.add(matrix[i][j]);
            }
        }
        int pos=0;
        while (queue.size() > 0)
        {
            pos++;
            if (pos == k) {
                return queue.poll();
            }
            queue.poll();
        }
        return -1;
    }

    public int kthSmallestList(int[][] matrix, int k) {
        Comparator<Integer> comparator = new IntegerLengthComparator();
        List<Integer> queue = new ArrayList<Integer>();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                queue.add(matrix[i][j]);
            }
        }
        Collections.sort(queue,comparator);
        return queue.get(k-1);
    }

    public static class IntegerLengthComparator implements Comparator<Integer>
    {
        @Override
        public int compare(Integer x, Integer y)
        {
            if (x < y)
            {
                return -1;
            }
            if (x > y)
            {
                return 1;
            }
            return 0;
        }
    }
}

```
- - -
THE END.
