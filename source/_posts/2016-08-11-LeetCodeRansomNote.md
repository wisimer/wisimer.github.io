---
date: 2016-08-11
title: LeetCode:383. Ransom Note
tags: LeetCode
category: Alogrithm
layout: default
---

### [eetCode:383. Ransom Note](https://leetcode.com/problems/ransom-note/)

> Given an arbitrary ransom note string and another string containing letters from all the magazines,write a function that will return true if the ransom note can be constructed from the magazines ;otherwise,it will return false.
Each letter in the magazine string can only be used once in your ransom note.
Note:
You may assume that both strings contain only lowercase letters.

<!--more-->

题目的意思就是给你两个字符串，判断第一个字符串ransom能不能由第二个字符串magazines里面的字符构成，第二个字符里的每个字符只能使用一次。（假设只包含小写字母）

1. 那么首先想到的就是穷举法了，两层遍历即可，也就是下面的 `canConstruct` 方法了。

2. 再思考一下，有个小技巧。这里只要判断两个字符串中每个字母出现的次数即可解决问题。对于某个字母来说，如果第ransom字符串中出现的次数比magazines中出现的次数要多，很显然就直接返回false了。

看一下 `canConstructBetter` 方法的实现：

```java
import java.util.List;
import java.util.ArrayList;
public class Solution {

    public boolean canConstructBetter(String ransomNote, String magazine) {
        if (ransomNote.equals(magazine) || "".equals(ransomNote)) {
            return true;
        }
        if ("".equals(magazine) && !"".equals(ransomNote)) {
            return false;
        }

        int[] ransomNoteCharCount = new int[26];
        int[] magazineCharCount = new int[26];

        for(int i = 0 ; i < ransomNote.length() ; i++) {
            ransomNoteCharCount[ransomNote.charAt(i)-'a']++;
        }

        for(int i = 0 ; i < magazine.length() ; i++) {
            magazineCharCount[magazine.charAt(i)-'a']++;
        }

        for(int i = 0 ; i < 26 ; i++) {
            if(ransomNoteCharCount[i] > magazineCharCount[i] ) {
                return false;
            }
        }
        return true;
    }

    public boolean canConstruct(String ransomNote, String magazine) {
        if (ransomNote.equals(magazine)) {
            return true;
        }
        if ("".equals(ransomNote)) {
            return true;
        }
        List<Character> ransomNodeChars = new ArrayList<>();
        List<Character> magazineChars = new ArrayList<>();
        for(int i = 0 ; i < ransomNote.length() ; i++) {
            ransomNodeChars.add(ransomNote.charAt(i));
        }
        this.printList(ransomNodeChars);
        for(int j = 0 ; j < magazine.length() ; j++) {
            magazineChars.add(magazine.charAt(j));
        }
        boolean flag = false;
        Character c = null;
        for(int m = 0 ; m < ransomNote.length() ; m++) {
            c = ransomNodeChars.get(m);
            flag = false;
            for(int n = 0 ; n < magazineChars.size() ; n++) {
                if(magazineChars.get(n) == c) {
                    flag = true;
                    magazineChars.remove(n);
                    break;
                }
            }
            if (flag == false) {
                break;
            }
        }
        return flag;
    }
}
```

- - -
THE END
