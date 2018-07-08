---
date: 2015-01-13
layout: default
category: PYTHON
tags: Ubuntu
title: Ubuntu 12.04 下配置Django开发环境
---

### Apache安装：

第一步：安装Apache2。

    sudo apt-get install apache2

第二步：安装mod_wsgi。

    sudo apt-get install libapache2-mod-wsgi

<!--more-->

- - -

### MySQL安装：

第一步：安装MySQL server。

    sudo apt-get install mysql-server

> 注意这个过程会要求输入密码，之后登陆mysql都要输入这个密码

第二步：安装MySQL的python接口。

    sudo apt-get install python-mysqldb

- - -

### Django安装：

Ubuntu下安装最新的django可以直接打开终端执行：

    pip install  django

或者指定安装版本：

    pip install -v django==1.7.1

也可手动下载安装包再进行安装：

第一步：下载Django软件包。

可以从Django的官网下载[Django-1.7.6.tar.gz](https://www.djangoproject.com/m/releases/1.7/Django-1.7.6.tar.gz)

第二步：解压缩Django软件包。

第三步：安装Django软件包。

cd进入解压缩得到的文件夹，然后输入

    sudo python setup.py install

安装完成！

- - -

### 测试Django安装结果：

![13_django_install_result.png](/src/imgs/1503/13_django_install_result.png)

- - -

### 创建第一个Django项目：

    django-admin.py startproject DjangoApp
    cd DjangoApp
    python manage.py runserver

![13_run_first_django_app.png](/src/imgs/1503/13_run_first_django_app.png)

接着就在浏览器中打开[http://127.0.0.1:8000/](http://127.0.0.1:8000/),会出现Django的欢迎界面了：

![13_django_welcome.png](/src/imgs/1503/13_django_welcome.png)

- - -

### 创建一个Django程序：

在项目的根目录下执行：

    python manage.py startapp LV

接着就会发现目录下面新建了一个LV程序

> 还要记得在DjangoApp/DjangoApp/settings.py中找到INSTALLED_APPS，在其中添加`LV`

- - -

### 配置数据库：

第一步：登陆mysql。
    mysql -u root -p

接着再输入安装mysql时输入的密码即可

接着创建数据库，在终端继续执行：

    create database django_app

第二步：配置Django项目中的settings.py,在刚刚创建的DjangoApp/DjangoApp/目录下的settings.py文件中

```bash
DATABASES = {
    'default': {
        'ENGINE': 'django.db.backends.mysql', # Add 'postgresql_psycopg2', 'mysql', 'sqlite3' or 'oracle'.也就是告诉Django使用mysql数据库
        'NAME': 'django_app',                      # Or path to database file if using sqlite3.填写刚刚创建的数据库名称
        # The following settings are not used with sqlite3:
        'USER': 'root',                      #mysql用户名
        'PASSWORD': '1992',                  #msql密码
        'HOST': '127.0.0.1',                 # Empty for localhost through domain sockets or '127.0.0.1' for localhost through TCP.本机地址
        'PORT': '3306',                      # Set to empty string for default.端口号
    }
}
```

第三步：现在自己定义一个表，并在通过Django自动在数据库中创建这个表。

首先在/DjangoApp/LV/models.py中添加：

```python
from django.db import models

# Create your models here.
class Article(models.Model) :
    title = models.CharField(max_length = 100)  #title
    category = models.CharField(max_length = 50, blank = True)  #tags
    date_time = models.DateTimeField(auto_now_add = True)  #date
    content = models.TextField(blank = True, null = True)  #content

    def __unicode__(self) :
        return self.title

    class Meta:  #order by date desc
        ordering = ['-date_time']
```

- 每个Django Model都继承自 django.db.models.Model都相当于一个表，而model的每一个属性都相当于这个表的一个字段

- 通过 Django Model API 可以执行数据库的增删改查, 而不需要写一些数据库的查询语句

第四步：在DjangoApp根目录下执行：

        python manage.py syncdb

然后还会提示创建Django超级用户，依次输入相yes,root,邮箱,密码即可。

!![13_django_syncdb.png](/src/imgs/1503/13_django_syncdb.png)

接着就显示创建成功了。

进入数据库即可发现，出现了一张新的表 `LV_article`

** 以后如果要创建一个新表，就不用执行`python manage.py syncdb`了,需要做以下几个步骤：**

1. 修改 models.py 文件
2. 运行 python manage.py makemigrations 创建迁移语句
3. 运行 python manage.py migrate ，将模型的改变迁移到数据库中

- - -

### 打开Django自带的管理界面

首先在DjangoApp/DjangoApp/settings.py中INSTALLED_APPS部分添加

    'django.contrib.admin',  #add admin function

然后在DjangoApp/DjangoApp/urls.py中修改内容，原有的注释已被删除

```python
from django.conf.urls import patterns, include, url
from django.contrib import admin
admin.autodiscover()

urlpatterns = patterns('',
    url(r'^admin/', include(admin.site.urls)),  #enter admin background through url
)
```

接着在浏览器中打开http://127.0.0.1:8000/admin/，再输入创建Django超级用户时填写的用户名和密码即可成功进入管理页面：

![13_django_admin.png](/src/imgs/1503/13_django_admin.png)

> 另外可以通过安装Bootstrap来改善界面： pip install bootstrap-admin

### 可以看到管理页面只有Groups和Users，下面将之前创建的Article也加入到管理页面来

修改`DjangoApp/Lv/admin.py`内容，添加Article：

```python
from django.contrib import admin
from LV.models import Article
# Register your models here.
admin.site.register(Article)
```

之后就可以直接添加Article数据了。

- - -

### 参考资料：

- Django官网:[https://www.djangoproject.com/](https://www.djangoproject.com/)

- 如何创建一个Django网站:[http://blog.javachen.com/2014/01/11/how-to-create-a-django-site/](http://blog.javachen.com/2014/01/11/how-to-create-a-django-site/)

- The Django Book:[http://djangobook.py3k.cn/2.0/chapter02/](http://djangobook.py3k.cn/2.0/chapter02/)

- 开发环境和Django安装:[http://andrew-liu.gitbooks.io/django-blog/content/kai_fa_huan_jing_he_django_an_zhuang.html](http://andrew-liu.gitbooks.io/django-blog/content/kai_fa_huan_jing_he_django_an_zhuang.html)

- MySql命令行:[http://baike.baidu.com/link?url=U1jfiMYK86lqZL-iK1KK9F0RtcIMZD2-k2C4JCXys5B0y2RiaHUhFZzNj4purQeFm-ERDzgCgHFRAWcStD0bkgcByA5V1rNboc1_3R73253qfIsqZCDBIGFA394Zgyp3#1](http://baike.baidu.com/link?url=U1jfiMYK86lqZL-iK1KK9F0RtcIMZD2-k2C4JCXys5B0y2RiaHUhFZzNj4purQeFm-ERDzgCgHFRAWcStD0bkgcByA5V1rNboc1_3R73253qfIsqZCDBIGFA394Zgyp3#1)

- - -

### Windows下安装Python以及Django的步骤：

- [http://jingyan.baidu.com/article/466506580e7d29f549e5f8b6.html](http://jingyan.baidu.com/article/466506580e7d29f549e5f8b6.html)

> Windows如果要使用Mysql作为Django作为数据库，首先要安装这个包 [/raw/attach/1505/setuptools-15.1.zip](/raw/attach/1505/setuptools-15.1.zip)

- - -
THE END.
