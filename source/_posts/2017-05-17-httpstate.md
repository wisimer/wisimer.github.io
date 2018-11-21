---
title: Http 协议见闻
date: 2017-05-17
layout: default
category: QA
tags: http
---

### Http 协议见闻

#### 一、HTTP协议的8种请求类型介绍

HTTP协议中共定义了八种方法或者叫“动作”来表明对Request-URI指定的资源的不同操作方式，具体介绍如下： 

- GET：向特定的资源发出请求。 
- POST：向指定资源提交数据进行处理请求（例如提交表单或者上传文件）。数据被包含在请求体中。POST请求可能会导致新的资源的创建和/或已有资源的修改。 
- PUT：向指定资源位置上传其最新内容。 
- DELETE：请求服务器删除Request-URI所标识的资源。 
- OPTIONS：返回服务器针对特定资源所支持的HTTP请求方法。也可以利用向Web服务器发送'*'的请求来测试服务器的功能性。 
- HEAD：向服务器索要与GET请求相一致的响应，只不过响应体将不会被返回。这一方法可以在不必传输整个响应内容的情况下，就可以获取包含在响应消息头中的元信息。 
- TRACE：回显服务器收到的请求，主要用于测试或诊断。 
- CONNECT：HTTP/1.1协议中预留给能够将连接改为管道方式的代理服务器。

虽然HTTP的请求方式有8种，但是我们在实际应用中常用的也就是get和post，其他请求方式也都可以通过这两种方式间接的来实现。
   
#### 二、Http请求返回状态码的简单说明

1. 客户方错误 
100 : 继续 
101 : 交换协议 
2. 成功 
200 : OK 
201 : 已创建 
202 : 接收 
203 : 非认证信息 
204 : 无内容 
205 : 重置内容 
206 : 部分内容 
3. 重定向 
300 : 多路选择 
301 : 永久转移 
302 :  暂时转移 
303 : 参见其它 
304 : 未修改（Not Modified） 
305 : 使用代理 
4. 客户方错误 
400 : 错误请求（Bad Request） 
401 : 未认证 
402 : 需要付费 
403 : 禁止（Forbidden） 
404 : 未找到（Not Found） 
405 : 方法不允许 
406 : 不接受 
407 : 需要代理认证 
408 : 请求超时 
409 : 冲突 
410 : 失败 
411 : 需要长度 
412 : 条件失败 
413 : 请求实体太大 
414 : 请求URI太长 
415 : 不支持媒体类型 
5. 服务器错误 
500 : 服务器内部错误 
501 : 未实现（Not Implemented） 
502 : 网关失败 
504 : 网关超时 
505 : HTTP版本不支持 

- - -

THE END.