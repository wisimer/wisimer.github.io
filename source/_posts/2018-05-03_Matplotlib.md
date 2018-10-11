---
title: Matplotlib
date: 2018-05-03
categories: ML
tags: [Matplotlib]
---

#### 一、2D 图形
##### 1. 画一个简单的图形

```python
import matplotlib.pyplot as plt
import numpy as np
%matplotlib inline

x = np.linspace(0, 2 * np.pi, 50) #生成一个包含 50 个元素的数组，这 50 个元素均匀的分布在 [0, 2pi] 的区间上。
plt.plot(x, np.sin(x)) # 如果没有第一个参数 x，图形的 x 坐标默认为数组的索引
plt.plot(x, np.sin(x),
         x, np.sin(2 * x)) #绘制两个数据集
plt.show() # 显示图形
```

![0503_simple](/src/imgs/1805/0503_simple.png)

<!--more-->

##### 2. 自定义图形的外观

```python
x = np.linspace(0, 2 * np.pi, 50)
plt.plot(x, np.sin(x), 'r-o',
         x, np.cos(x), 'g--') #'r' 和 'g' 代表线条的颜色，'-o' 代表包含实心点标记的实线，'--' 代表虚线
plt.show()
```

![0503_style](/src/imgs/1805/0503_style.png)

##### 3. 使用子图，可以在一个窗口绘制多张图。

```python
x = np.linspace(0, 2 * np.pi, 50)
plt.subplot(2, 1, 1) # （行，列，活跃区）
plt.plot(x, np.sin(x), 'r')
plt.subplot(2, 1, 2) #在调用 plot() 函数之前需要先调用 subplot() 函数。
plt.plot(x, np.cos(x), 'g')
plt.show()
```

![0503_child](/src/imgs/1805/0503_child.png)

##### 4. 简单散点图

```python
x = np.linspace(0, 2 * np.pi, 50)
y = np.sin(x)
plt.scatter(x,y) #通过 plot 命令并将线的样式设置为 'bo' 也可以实现同样的效果。实际设置'o'就可以，'bo'表示蓝色的点。
plt.show()
```

![0503_sandian](/src/imgs/1805/0503_sandian.png)

##### 5. 直方图

```python
x = np.random.randn(1000)
plt.hist(x, 50)
plt.show()
```

![0503_zhifang](/src/imgs/1805/0503_zhifang.png)

##### 6. 彩色映射散点图

```python
x = np.random.rand(1000)
y = np.random.rand(1000)
size = np.random.rand(1000) * 50
colour = np.random.rand(1000)
plt.scatter(x, y, size, colour)
plt.colorbar() #加了一个颜色栏。
plt.show()
```

![0503_color_sandian](/src/imgs/1805/0503_color_sandian.png)

##### 7. 添加标题，坐标轴标记和图例

```python
x = np.linspace(0, 2 * np.pi, 50)
plt.plot(x, np.sin(x), 'r-x', label='Sin(x)')
plt.plot(x, np.cos(x), 'g-^', label='Cos(x)')
plt.legend() # 应用label，展示图例
plt.xlabel('Rads') # 给 x 轴添加标签
plt.ylabel('Amplitude') # 给 y 轴添加标签
plt.title('Sin and Cos Waves') # 添加图形标题
plt.show()
```

![0503_label](/src/imgs/1805/0503_label.png)

##### 8. 饼图
```python
slices = [7,2,2,13]
activities = ['sleeping','eating','working','playing']
cols = ['c','m','r','b']

plt.pie(slices,
        labels=activities,
        colors=cols,
        startangle=90,
        shadow= True,
        explode=(0,0.1,0,0),
        autopct='%1.1f%%')

plt.title('Interesting Graph\nCheck it out')
plt.show()
```

![0503_pie](/src/imgs/1805/0503_pie.png)


##### 9. 从文件读取数据

```python
import numpy as np
import matplotlib.pyplot as plt
import random
import csv

x = []
y = []

with open('example.txt','r') as csvfile:
    plots = csv.reader(csvfile, delimiter=',')
    for row in plots:
        x.append(int(row[0]))
        y.append(int(row[1]))

plt.plot(x,y, label='Loaded from file!')
plt.xlabel('x')
plt.ylabel('y')
plt.title('Interesting Graph\nCheck it out')
plt.legend()
plt.show()
```

![0503_read_file_data](/src/imgs/1805/0503_read_file_data.png)

#### 二、3D 图形

```
import numpy as np
import matplotlib.pyplot as plt
from mpl_toolkits.mplot3d import Axes3D

fig = plt.figure()
ax = Axes3D(fig)

# X, Y value
X = np.arange(-4, 4, 0.25)
print(X)
Y = np.arange(-4, 4, 0.25)
X, Y = np.meshgrid(X, Y)    # x-y 平面的网格,这一步不可少，表示(x，y)两个变量共建两个坐标轴

R = np.sqrt(X ** 2 + Y ** 2)
# height value
Z = np.sin(R)

ax.plot_surface(X, Y, Z, rstride=1, cstride=1, cmap=plt.get_cmap('rainbow'))
```

![matplot_3d](/src/imgs/1805/0503_matplot_3d.png)

- - -

附：
[code](/raw/code/matplotlib/matplotlibdemo.ipynb)

- - -
THE END.
