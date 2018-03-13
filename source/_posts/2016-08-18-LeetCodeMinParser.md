---
date: 2016-08-18
title: LeetCode:385. Mini Parser
tags: LeetCode
category: leetcode
layout: default
---

### [LeetCode:385. Mini Parser](https://leetcode.com/problems/mini-parser/)

```
Given a nested list of integers represented as a string, implement a parser to deserialize it.

Each element is either an integer, or a list -- whose elements may also be integers or other lists.

Note: You may assume that the string is well-formed:

String is non-empty.
String does not contain white spaces.
String contains only digits 0-9, [, - ,, ].
```

<!--more-->

题目的意思就是给你一个由好多int值嵌套在一起的字符串。让你写一个解析器，把里面的元素解析出来。每个元素呢，要么是一个int值，要么是一个列表，而这个列表的元素又可以是int或者其他列表，如此嵌套而成。
几个注意点：给定的字符创不为空且不含空格，只包含0-9以及[]还有,和-。

#### 讲讲思路先

1. 遇到'['字符肯定是要产生一个新的 `NestedInteger` 对象的。
2. 遇到']'字符则表明上一个元素可以结束了，此时要处理这里面的整型字符串，将其解析成int值再传给当前的NestedInteger对象。并且呢，由于当前元素已经结束解析，还需要将它传给它的父NestedInteger。
3. 遇到','字符要分情况了，如果它的前一个字符是']'则表明在步骤2种已经做了处理了，否则的话说明之前的整型字符串还没有解析。
4. 如果遇到了0到9还有－，则暂时不作处理，将其拼接到一个StringBuilder里面。

看看Java代码实现：

```java
public class Solution {
    public NestedInteger deserialize(String s) {
        if (s.isEmpty())
            return null;
        if (s.charAt(0) != '[') // ERROR: special case
            return new NestedInteger(Integer.valueOf(s));

        //stack 用于保存上一个 NI 对象
        Stack<NestedInteger> stack = new Stack<>();
        //curNi用于保存当前 NI 对象
        NestedInteger curNi = null;
        StringBuilder sb = new StringBuilder();
        for(int i = 0 ; i < s.length() ; i++ ) {
            char c = s.charAt(i);
            if(c == '[') {
                //如果当前 curNi 不为空，则 curNi 临时push进 stack
                if(curNi != null) {
                   stack.push(curNi);
                }
                //并且遇到'['之后要实例化新的 curNi
                curNi = new NestedInteger();
            } else if (c == ']') {
                //遇到']'之后，就要把之前的字符串解析成int了
                if(sb.length() >0) {
                    curNi.add(new NestedInteger(Integer.parseInt(sb.toString())));
                    sb.setLength(0);
                }
                //如果 stack 不为空则把 curNi 添加到 stack的顶层NI 中。
                if(!stack.empty()) {
                    NestedInteger popNi = stack.pop();
                    popNi.add(curNi);
                    curNi = popNi;
                }
            } else if (c == ','){
                //如果前一个元素是]，则已经处理；如果不是，则要把前面的元素解析成int
                if(s.charAt(i-1) != ']') {
                    curNi.add(new NestedInteger(Integer.parseInt(sb.toString())));
                    sb.setLength(0);
                }
            } else {
                sb.append(c);
            }
        }
        return curNi;
    }
}
```

#### 我们这里再来拿一个字符串来讨论看看，对于字符串"[-1,[123],[[3]]]"

1. 首先遇到'['产生一个NestedInteger，对应着最外层的NestedInteger， 记作NI1，并赋值给curNi(NI1)；
2. 接着向后遍历，直到遇到了第一个','，此时要为前面的整型值'-1'实例化一个NestedInteger对象，并插入到最外层的curNi(NI1)；
3. 继续向后遍历，遇到第二个'['，先将curNi(NI1)压入stack中，再实例化一个新的NestedInteger对象，记作NI2，且令赋值给curNi(NI2)；
4. 向后遍历，遇到第二个'['所对应的']'，为前面的整型值'123'实例化一个NestedInteger对象，add进curNI(NI2)中。再弹出stack中的NI1对象，将curNI(NI2)add到NI中，再令curNi ＝ NI1，注意此时stack中已空；
5. 继续，遇到第二个','但是发现它的前一个字符是']'，不作处理；
6. 继续遍历，遇到第三个'['，先将curNI(NI1)压入stack中。再实例化一个新的NestedInteger对象，记作NI3，令curNI = NI3；
7. 继续遍历，遇到第四个'['，先将curNI(NI3)压入stack中。再实例化一个新的NestedInteger对象，记作NI4，令curNI = NI4；
8. 继续遍历，遇到第四个'['所对应的']'，为'3'实例化一个NestedInteger对象，插入到curNI(NI4)中。从stack中弹出NI3，将curNI(NI4)插入到NI3中，且令curNI = NI3；
9. 继续遍历，遇到第三个'['所对应的']'，前面没有未处理的整型字符串。此时stack里面还有一个NI1，弹出NI1，将curNI(NI3)add给NI1，且令curNI = NI1；
10. 到了最后一个']'，也对应了第一个']'，此时stack为空，且没有未处理的字符串了。此时，curNI就对应了最外层的那个NestedInteger，是不是很神奇。结束。

- - -
THE END.
