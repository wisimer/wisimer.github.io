---
date: 2016-08-12
title: LeetCode:381. Insert Delete GetRandom O(1)
tags: LEETCODE
category: ALGORITHM
layout: default
---

### [LeetCode:381. Insert Delete GetRandom O(1) - Duplicates allowed](https://leetcode.com/problems/insert-delete-getrandom-o1-duplicates-allowed/)

>Design a data structure that supports all following operations in average O(1) time.
Note: Duplicate elements are allowed.
insert(val): Inserts an item val to the collection.
remove(val): Removes an item val from the collection if present.
getRandom: Returns a random element from current collection of elements. The probability of each element being returned is linearly related to the number of same value the collection contains.

<!--more-->

题目的意思是要你设计一个数据结构，使得它的插入和删除时间复杂度都是O(1)，并且能够随机返回一个元素，而返回这个元素的概率也是和这个元素的个数是线性相关的。允许插入相同的元素。

说实话一看到这个题目我是没有思路的。我想这总不至于让你设计一个和HashMap一样的数据结构吧。看了一下别人的解法，确实不至于。要说的是这个数据结构内部可以使用HashMap，而不必自己从0开始写一个。

来说一下大概的思路吧，这个数据结构暂且就叫 `RandomizedCollection` ，它有一个 Map<Integer, List<Integer>> 类型的成员变量 `data` ，用于保存当前已经插入的元素。每次插入元素val，首先判断data里是否存在val这个key，如果不存在，则首先插入key为val，值为ArrayList对象，再将val保存到对应的ArrayList中去，这样就保证来可以插入重复的元素，并且时间复杂度为O(1)。同理，如果要删除一个元素val，则首先找到以val为key的ArrayList，再从其中移除一个val。

而对于随机返回一个元素，我们可以用一个List<Integer>类型的成员变量 `list` 来保存每个val，用mSize来保存所有元素的总个数，生成一个介于0到mSize之间的随机数 random 。由于List可以保存重复的元素，所以可以直接通过list.get(random)来返回一个随机元素。看一下Java代码实现：

```java
public class RandomizedCollection {
    /**
    * data用来保存实际的数据，key是插入的值，value是和key相同的值所组成的list
    */
    private Map<Integer, List<Integer>> data = new HashMap<Integer, List<Integer>>();
    /**
    * list 用来保存所有的元素，在返回随机元素时减少时间。
    */
    private List<Integer> list = new ArrayList<Integer>();
    private Random mRandom = new Random();
    private int mSize = 0;

    public RandomizedCollection() {
    }

    public boolean insert(int val) {
        if (!data.containsKey(val)) {
            data.put(val, new ArrayList<Integer>());
        }
        data.get(val).add(val);
        list.add(val);
        mSize++;
        return true;
    }

    public boolean remove(int val) {
        if (data.containsKey(val)) {
            if (data.get(val).size() > 0) {
                data.get(val).remove(Integer.valueOf(val));
                list.remove(Integer.valueOf(val));
                mSize--;
                return true;
            }
        }
        return false;
    }

    public int getRandom() {
        int random = mRandom.nextInt(mSize);
        return list.get(random);
    }
}
```

- - -
THE END.
