---
date: 2016-08-10
title: LeetCode:382. Linked List Random Node
tags: [LEETCODE,List]
category: ALGORITHM
layout: default
---

### [LeetCode:382. Linked List Random Node](https://leetcode.com/problems/linked-list-random-node/)

> Given a singly linked list, return a random node's value from the linked list. Each node must have the same probability of being chosen.
Follow up:
What if the linked list is extremely large and its length is unknown to you? Could you solve this efficiently without using extra space?

<!--more-->

题目的意思就是给你一个链表，要你从链表中取出一个值，并且保证取到每个元素的概率都是相等的。还要思考如果链表长度极其大，咋办。能不能不使用额外的空间就能达到目的。

一开始我的想法就是直接遍历一次拿到链表的长度，再生成一个随机值，拿到这个值对应位置的元素即可。但是我觉得这种解法完全达不到Medium的难度啊。看了讨论学又到一个新词：`蓄水池抽样算法`。

蓄水池抽样算法的结论就是：在N（N不确定）个元素中取出K个元素的概率就是 K/N。这里我们令K等于1，就可以用来解上面的这道题目了。Java代码实现：

```java
import java.util.Random;
public class Solution {

    private Random mRandom;
    private ListNode head;
    public Solution(ListNode head) {
        this.head=head;
    }

    /**
     *蓄水池抽样算法
     */
    public int getRandom() {
        mRandom=new Random();
        int pos = 1;
        ListNode node = head;
        ListNode res = head;
         while(node != null) {
            //一直取最末尾的一个数
            if (mRandom.nextInt(pos) == (pos-1)) {
                res = node;//此处不可直接return。取第一个值的时候概率是百分之百，直接返回。
            }
            pos = pos+1;
            node = node.next;
        }

        return res == null ? head.val : res.val;
    }

    /**
     *先获取长度再取随机值的方法
     */
    public int getRandomXX() {
        int randomVaule = mRandom.nextInt(this.getLength());
        int pos = 0;
        ListNode res = head;
        while(res != null) {
            if(pos == randomVaule) {
                return res.val;
            }
             res = res.next;
            pos++;
        }
        return res.val;
    }

    public int getLength() {
        int len = 0;
        ListNode node = head;
        while(node != null) {
            len++;
            node = node.next;
        }
        return len;
    }

}
```

那么为什么蓄水池抽样算法会保证每个元素被取到的概率是一样的呢？让我们一步一步来验证一下。

我们这里来考察选择最后一个元素的概率。

1. 首先如果只有一个元素的时候，概率是1；

2. 如果有两个元素，取到第二个元素的概率是1/2；

3. 如果有三个元素，要保证每个元素取到的概率都是一样，有两种情况，第一种就是取第三个元素，取到的概率是1/3。但是如果不是取到第三个元素，而是取到第二个呢？那就是下面这种情况 ： 取不到第三个元素的概率 x 取到第二个元素的概率 = (2/3) x (1/2) = 1/3

可以用数学归纳法来证明一下：

假设已经读取n个数，现在保留的数是An，取到An的概率为(1/n)。

```
对于第n+1个数An+1，以1/(n+1)的概率取An+1，否则仍然取An。依次类推，可以保证取到数据的随机性。

数学归纳法证明如下：

    当n=1时，显然，取A1。取A1的概率为1/1。

    假设当n=Max时，取到的数据AMax。取AMax的概率为1/Max。

    当n=Max+1时，以1/(Max+1)的概率取AMax+1，否则仍然取AMax。

　　　　(1)如果取AMax+1，则概率为1/(Max+1)；

　　　　(2)如果不取AMax+1而仍然取AMax，则概率为(1/Max)*(Max/(Max+1))=1/(Max+1)

所以，对于之后的第n+1个数An+1，以1/(n+1)的概率取An+1，否则仍然取An。依次类推，可以保证取到数据的随机性。
```

- - -
THE END.
