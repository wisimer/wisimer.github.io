---
date: 2014-05-24
title: SAE笔记
layout: default
category: FAQ
tags: SAE
---

#### 连接数据库

###### 连主库

    link=mysql_connect(SAE_MYSQL_HOST_M.':'.SAE_MYSQL_PORT,SAE_MYSQL_USER,SAE_MYSQL_PASS);

###### 连从库

    link=mysql_connect(SAE_MYSQL_HOST_S.':'.SAE_MYSQL_PORT,SAE_MYSQL_USER,SAE_MYSQL_PASS);
    if($link)
    {
      mysql_select_db(SAE_MYSQL_DB,$link);
      //your code goes here
    }

<!--more-->

#### 查询操作

###### 验证用户名和密码

```java
    sql = "SELECT * FROM admin WHERE id = '$id' and pass='$pass'";
    res = mysql_query($sql);
    rows=mysql_num_rows($res);
    if($rows){
      header("refresh:0;url=/timo.html");//跳转页面，注意路径
      exit;
    }
```
###### 以json形式输出查询结果

```java
    $query = mysql_query($sql);
    $result = mysql_fetch_array($query) or die("");
    $rows=mysql_num_rows($query);
    if($rows){
      while($result = mysql_fetch_array($query)){
        $array = array(
            'id'=>$result['id'],
            'created'=>$result['created'],
            'description'=>$result['description'],
            'mime_type'=>$result['mime_type'],
            'name'=>$result['name'],
            'relative_path_cache'=>$result['relative_path_cache'],
            'relative_url_cache'=>$result['relative_url_cache'],
            'img_rl'=>$URL.$result['relative_path_cache']."&".$result['created']
        );
        echo json_encode($array);
      }

    }
```

###### 分页查询
    //起始行,要查询的行数
    $sql = "SELECT id,created,description,mime_type,name,relative_path_cache,relative_url_cache FROM g3_items LIMIT ".$q_start.",11";

#### 数据请求

###### POST

POST通常用于表单提交，参数以及参数值不会显示在url中。通过如下方式获取POST参数值：

    $q_rows = $_POST['rows'];

###### GET
GET通过url携带参数的方式请求数据。通过如下方式获取GET参数值：

    $q_name = $_GET['name'];
- - -
THE END.
