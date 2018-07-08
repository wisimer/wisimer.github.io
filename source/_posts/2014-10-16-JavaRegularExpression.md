---
date: 2014-10-16
layout: default
category: JAVA
tags: [Java,Regex]
title: Java正则表达式
---

Java正则表达式简要笔记

<!--more-->

```java
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.*;

/**
 * 正则表达式
 *
 * @param wxp
 */
public class RegularExpression {

	public static void p(Object object) {
		System.out.println(object.toString());
	}

	/**
	 * 匹配以某个字符串开头，任意字符串结尾的一个字符串
	 * 匹配规则^start.*
	 * 其中.号匹配除\r和\n之外的任何单个字符
	 * 其中*号匹配之前的表达式零次或多次
	 */
	public static void startWith() {
		boolean flag = false;
		Pattern pattern = Pattern.compile("^android.*");
		Matcher matcher = pattern.matcher("android hello");
		flag = matcher.matches();
		p(flag);
	}

	/**
	 * 以某个符号分割一串字符串
	 * 匹配规则[,|]
	 * 其中 , 可以换成别的符号
	 */
	public static void splitWith(){

		Pattern pattern=Pattern.compile("[,|]");
		String [] arrays=pattern.split("hello,android");
		p(arrays.length);
	}

	/**
	 * 替换首次出现的匹配的字符串
	 * 其中matcher的replaceFirst方法返回的是替换后的整个字符串
	 */
	public static void replaceFisrt(){
		Pattern pattern=Pattern.compile("hello");
		Matcher matcher=pattern.matcher("hello android");
		p(matcher.replaceFirst("i love"));
	}

	/**
	 * 替换所有匹配的字串
	 */
	public static void replaceAll(){
		Pattern pattern = Pattern.compile("正则表达式");
		Matcher matcher = pattern.matcher("正则表达式 Hello World,正则表达式 Hello World ");
		p(matcher.replaceAll("Java"));
	}

	/**
	 * 同上
	 */
	public static void replaceAllChar(){
		Pattern pattern = Pattern.compile("正则表达式");
		Matcher matcher = pattern.matcher("正则表达式 Hello World,正则表达式 Hello World ");
		StringBuffer sbr = new StringBuffer();
		while (matcher.find()) {
		    matcher.appendReplacement(sbr, "Java");
		}
		matcher.appendTail(sbr);
		System.out.println(sbr.toString());
	}

	/**
	*匹配IP地址
	*ip地址由四个字节构成,每个字节的值在0到255之间
	*其中\d等价于[0-9],匹配一个数字字符
	*/
	public static void matchIP(){
		Pattern pattern=Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");
		Matcher matcher=pattern.matcher("192.168.0.1");
		p(matcher.matches());

	}


	/*
	public static void fontAttri(){
		String fonfattr="<font size=\"8\">";
		Pattern fontP=Pattern.compile("<\\s*font\\s+([^>]*)\\s*>");
		Pattern attrP=Pattern.compile("([a-z]+)/s*=/s*\"([^\"]+)\"");
		Matcher matcher=fontP.matcher(fonfattr);
		p(matcher.matches());
	}
	*/

	/*
	*匹配邮箱
    *CASE_INSENSITIVE表示不分大小写
	*其中\w匹配任意字符包括下划线
	*/
	public static void matchEmail(){
		String mail="wxp2014android@163.com";
		Pattern pattern=Pattern.compile("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+",Pattern.CASE_INSENSITIVE);
        Matcher matcher=pattern.matcher(mail);
		p(matcher.matches());
	}
	public static void main(String[] args) {

		//matchIP();
        matchEmail();
	}

}
```
