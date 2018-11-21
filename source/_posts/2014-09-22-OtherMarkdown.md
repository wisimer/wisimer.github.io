---
date: 2014-09-22
layout: default
title: Markdown Priview
category: QA
tags: Markdown
---

Markdown语法简要介绍以及SublimText的一些快捷方式

我最近已经转投 [Atom](https://atom.io/)了哈哈，因为Atom支持实时预览Markdown。

## Markdown Summary

#### Title

- use `#` to mark the title,eg:`# First Page`
>#First Page

#### List

- use `-` to mark the ul,eg:`- item`
>- item1
>- item2

- use directly `1.` to mark the ol,eg:`1.first item`
>1. first item
>2. secondary item
>3. third item

#### Code
<!--more-->

- use `<code>` to mark a line code,eg:`<code>$ git status</code>`
><code>$ git status</code>

- use `` ` `` to mark a few words,eg:`` `i am a monkey` ``
>`i am a monkey`

- In Addition
if you want to insert a `` ` `` at someplace,you can use:

		like this `` ` ``

#### Reference

- use `>` to to mark a reference line,eg:`>i love u`
>i love u

#### Link
eg:`[whisper](whisper92.github.io)`
>[whisper](www.sianper.com)

#### Image

eg:`![Image](http://whisperlog.qiniudn.com/w.png)`

>![Image](http://whisperlog.qiniudn.com/w.png)

## Sublime Text

- set shortcut key using preiview .md in browser
>Preferences->Key Bindings-User

    	{ "keys": ["ctrl+m"], "command": "markdown_preview", "args": { "target": "browser"} }

- goto line
>Ctrl+G

- goto a file
>Ctrl+P

- goto a function
>Ctrl+P @

主要快捷键列表：

<pre>
	<code>
Ctrl+L 选择整行（按住-继续选择下行）
Ctrl+KK 从光标处删除至行尾
Ctrl+Shift+K 删除整行
Ctrl+Shift+D 复制光标所在整行，插入在该行之前
Ctrl+J 合并行（已选择需要合并的多行时）
Ctrl+KU 改为大写
Ctrl+KL 改为小写
Ctrl+D 选词 （按住-继续选择下个相同的字符串）
Ctrl+M 光标移动至括号内开始或结束的位置
Ctrl+Shift+M 选择括号内的内容（按住-继续选择父括号）
Ctrl+/ 注释整行（如已选择内容，同“Ctrl+Shift+/”效果）
Ctrl+Shift+/ 注释已选择内容
Ctrl+Z 撤销
Ctrl+Y 恢复撤销
Ctrl+M 光标跳至对应的括号
Alt+. 闭合当前标签
Ctrl+Shift+A 选择光标位置父标签对儿
Ctrl+Shift+[ 折叠代码
Ctrl+Shift+] 展开代码
Ctrl+KT 折叠属性
Ctrl+K0 展开所有
Ctrl+U 软撤销
Ctrl+T 词互换
Tab 缩进 自动完成
Shift+Tab 去除缩进
Ctrl+Shift+↑ 与上行互换
Ctrl+Shift+↓ 与下行互换
Ctrl+K Backspace 从光标处删除至行首
Ctrl+Enter 光标后插入行
Ctrl+Shift+Enter 光标前插入行
	</code>
</pre>

- - -
THE END.
