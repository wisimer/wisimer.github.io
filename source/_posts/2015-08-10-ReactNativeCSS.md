---
date: 2015-08-10
layout: default
category: ANDROID
tags: ReactNative
title: ReactNative中的布局样式
---

#### 一、布局样式的声明与使用有如下几种方式

1. 首先用StyleSheet.create的方法来声明styles样式，然后引用styles对象即可。例如：

    ```
    //声明样式
    var styles = StyleSheet.create({
        container: {
            flex: 1,
            alignItems: 'center',
            justifyContent: 'center',
        },
    });
    //使用样式
    <View style={styles.container} />
    ```
<!--more-->

2. 也可以通过数组形式引用。例如：

    ```
    //声明样式
    var styles = StyleSheet.create({
        container: {
            flex: 1,
            alignItems: 'center',
            justifyContent: 'center',
        },
        tvcontainer: {
            margin: 2,
            width: 50,
            padding: 10,
            backgroundColor: '#303339',
            alignItems: 'center',
            justifyContent: 'center',
        },
    });
    //使用样式
    <View style={[styles.container,styles.tvcontainer]} />
    ```

3. 或者可以在style属性中直接使用CSS样式。例如：

    ```
    <View style={[styles.container,{marginTop: 80}]} />
    ```

#### 二、实现宽高的百分比设置

ReactNative的布局样式并不支持百分比，但是可以通过别的方式来实现同样的效果。

1. 首先我们要引入Dimensions模块

    ```
    var Dimensions = require('Dimensions');
    ```

2. 然后通过Dimensions.get('window')来获取设备的宽高，这里我们将一个View的宽高分别设置为设备宽高的1/2，来看看效果

    ```
    class DimensionsView extends Component {
      render() {
          var {width,height} = Dimensions.get('window');
        return (
          <View style={styles.container}>
            <Text style={styles.description}>window.width = {width}</Text>
            <Text style={styles.description}>window.height = {height}</Text>
            <View style={[styles.tvcontainer,{width: width/2 ,height: height/2}]}><Text style={styles.content}>Item</Text></View>
          </View>
        );
      }
    };
    ```

    ![0815_dimensions.png](/src/imgs/1508/0815_dimensions.png)

#### 三、关于绝对定位和相对定位

position属性有两个可选值：'absolute'和'relative'。absolute表示绝对定位，relative表示相对定位。来看一下具体的实例：

__这里我们在每个容器里面左上角都有一个50*50的方形，然后还有一个红色圆形。__

1. 首先看一下两个小球在设置position属性之前的布局。两个球和方形的位置都是这样的

    ![0815_positions_origin.png](/src/imgs/1508/0815_positions_origin.png)

2. 接着第一个圆形使用绝对定位，第二个圆形使用相对定位。每个圆形的left都设为70，top设为20。

    ```
    <View style={styles.container}>
      <View style={[{width: 200 ,height: 200,backgroundColor: '#6a6a6a',marginBottom:10}]}><View style={[{width: 50 ,height: 50,backgroundColor: '#a6a6a6'}]} /><View style={[styles.circle,{position:'absolute',left:70,top:20}]}></View></View>
      <View style={[{width: 200 ,height: 200,backgroundColor: '#a6a6a6'}]}><View style={[{width: 50 ,height: 50,backgroundColor: '#6a6a6a'}]} /><View style={[styles.circle,{position:'relative',left:70,top:20}]}></View></View>
    </View>
    ```

    ![0815_positions.png](/src/imgs/1508/0815_positions.png)

可以看到使用绝对定位时，圆形的位置与方形的位置无关。而使用相对定位时圆形的top位置是相对于方形的。

- - -
THE END.
