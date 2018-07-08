---
date: 2015-03-14
layout: default
category: PYTHON
tags: Django
title: Django 视图Views和URL配置
---

> Django 中 views 里面的代码就是一个一个函数逻辑，处理客户端(浏览器)发送的 HTTPRequest，然后返回 HTTPResponse。

### 编写第一个视图

** 首先在/DjangoApp/LV/views.py中导入HttpResponse模块，再创建一个函数并返回HTTPResponse。： **

```python
from django.shortcuts import render
from django.http import HttpResponse

# Create your views here.
def hello(request):
    return HttpResponse("Hello World, Django")
```

** 接着在/DjangoApp/DjangoApp/urls.py中设置这个视图对应的url： **

```python
urlpatterns = patterns('',
    url(r'^admin/', include(admin.site.urls)),
    url(r'^$', 'LV.views.hello'),
)
```

<!--more-->

这里的匹配规则是：

    ^ ： 表示开始匹配字符串
    $ ： 表示结束匹配字符串

也就是字符串为空，所以我们最终匹配的地址应该还是`127.0.0.1:8000`

** 然后在浏览器中打开127.0.0.1:8000，即可看页面上到输出`Hello World, Django` **

- url()函数有四个参数, 两个是必须的:regex和view, 两个可选的:kwargs和name
- 第一个参数regex也就是正则匹配，Django将浏览器请求的URL从上倒下一次匹配，直到匹配出一个为止，另外即使有多个匹配，也知会匹配第一个符合条件的。
- 第二个参数view，如果上面的过程完毕之后成功匹配到一个URL，就会调用这个view中指定的函数，比如上面执行了hello函数。
- 第三个参数kwargs任意关键字参数可传一个字典至目标view

- - -

### 带参数的视图

** 在/DjangoApp/LV/views.py中创建一个新的带参数的函数： **

```python
def answerme(request,name):
    return HttpResponse("Hello Django, i am %s"%name)
```

** 在/DjangoApp/DjangoApp/urls.py中添加url： **

```python
url(r'^(?P<your_name>\D+)/$','LV.views.anserme',name='anserme'),
```

> ^(?P<my_args>\d+)/$这个正则表达式的意思是将传入的非数字字符作为参数传递到views中的`answerme`作为参数, 其中?P<my_args>定义名称用于标识匹配的内容

这里的匹配规则是：

    \D ： 匹配任意非数字的字符
    + ： 表示重复一次或更多次

在浏览器中打开`127.0.0.1:8000/your_name`，就会显示`Hello Django, i am your_name`了，这里的`your_name`可以随便输入一个不为数字的字符串。

### 当然同样也可以定义另外一个函数，用于接受年龄
** 在/DjangoApp/LV/views.py中创建一个新的带参数的函数：**

```python
def answerme_age(request,age):
    return HttpResponse("Hello Django, i am %s years old."%age)
```

** 不过这时候的匹配规则就要变化了，在/DjangoApp/DjangoApp/urls.py中添加url： **

```python
url(r'^(?P<age>\d+)/$','LV.views.answerme_age',name='answerme_age'),
```

在浏览器中打开`127.0.0.1:8000/24`，就会显示`Hello Django, i am 24 years old`了，这里的`24`就是我输入的年纪了。

- - -

### 传入参数访问数据库

之前已经在管理页面添加了Article的model了，所以我们在管理页面[http://127.0.0.1:8000/admin/LV/article/](http://127.0.0.1:8000/admin/LV/article/)就可以直接操作Article了，
Django会自动将我们的操作同步到数据库

** 这里我们可以直接插入一条记录 **

![14_edit_article.png](/src/imgs/1503/14_edit_article.png)

** 同样，在/DjangoApp/LV/views.py中创建一个新的带参数的函数，记得要导入Article：**

```python
from LV.models import Article
def detail(request, my_args):
    post = Article.objects.all()[int(my_args)]
    str = ("title = %s, category = %s, content = %s"%(post.title,post.category,post.content))
    return HttpResponse(str)
```

** 在/DjangoApp/DjangoApp/urls.py中添加url： **
```python
url(r'^(?P<my_args>\d+)/$', 'LV.views.detail', name='detail'),
```

在浏览器中打开`127.0.0.1:8000/0`，就会显示刚刚插入的那条记录的内容了

> title = wxp, category = blog, content = Hello Django i am wxp

- - -
THE END.
