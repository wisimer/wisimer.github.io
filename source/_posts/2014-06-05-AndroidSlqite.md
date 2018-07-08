---
date: 2014-06-05
title: Android数据库 Sqlite 操作
category: ANDROID
layout: default
tags: Sqlite
---
数据库在Android应用开发中扮演重要的角色，对于一下小型配置数据可以直接用SharedPreference解决，但是如果涉及到大量复杂结构数据的持久化操作，那就离不开Sqlite了

- - -

#### 1. 继承SQLiteOpenHelper实现一个子类DBhelper

#### 2. 在类中定义要新建的数据库名、表名以及新建表的sql语句

```java
private static final String DB_NAME="timo.db";
private static final String TBL_NAME="users";
String CREATE_TBL=" create table users(_no integer primary key autoincrement,id text,name text,avatar text,atype txt) ";
```

<!--more-->

#### 3. 构造函数

```java
public DBhelper(Context c)
{
  super(c,DB_NAME,null,2);
}
```

#### 4. 重写onCreate方法，在其中执行新建表到sql语句

```java
public void onCreate(SQLiteDatabase db)
{
  this.db=db;
  db.execSQL(CREATE_TBL);
}
```


#### 5. 定义增删查改到方法

>插入数据

```java
public void insert(ContentValues values)
{
    SQLiteDatabase db=getWritableDatabase();
    db.insert(TBL_NAME,null,values);
    db.close
}
```

>查询数据

```java
public Cursor queryCursor(){
    SQLiteDatabase db = getWritableDatabase();
    Cursor c = db.query(TBL_NAME, null, null, null, null, null, null);
    return c;
}
```

>通常我们是将查询到的数据转换成List

```java
public List<BeanUser> queryList() {
       ArrayList<BeanUser> Records = new ArrayList<BeanUser>();
       Cursor c = queryCursor();
       if (c.moveToFirst()) {
          while (c.moveToNext()) {
            BeanUser Record = new BeanUser();
            Record.userId = c.getString(c.getColumnIndex("id"));
            Record.userName = c.getString(c.getColumnIndex("name"));
            Record.headUrl = c.getString(c.getColumnIndex("avatar"));
            Record.accountType = c.getString(c.getColumnIndex("atype"));
            Records.add(Record);
             }
         }
     c.close();
     return Records;
}
```

>删除数据

```java
public void delete(String id)
{
    if (db == null)
    db = getWritableDatabase();
    db.delete(TBL_NAME, "id=?", new String[] { String.valueOf(id) });
}
```

- - -
*附录:adb的常用命令*

1.获取手机的权限

    adb shell
    su

2.使用sqlite3

    sqlite3 timo.db

3.查看当前数据库的所有表

    .table

接着就可以使用sql语句操作数据了

- - -
THE END.
