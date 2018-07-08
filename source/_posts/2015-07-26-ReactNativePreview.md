---
date: 2015-07-26
layout: default
category: ANDROID
tags: ReactNative
title: ReactNative初探
---

ReactNative 计划在10月份以后就会支持Android了，它的宗旨是 `Learn once, write anywhere`，趁着周末来折腾一下。

ReactNative官方文档 ： [http://facebook.github.io/react-native/](http://facebook.github.io/react-native/)

<!--more-->

#### 一、环境搭建

1.首先安装 [Homebrew](http://brew.sh/),Homebrew时mac下的一个包管理应用，可以通过它来安装其他所需的软件

```ruby
ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"
```

接着通过`brew -v`这个命令来检查是否已经安装成功

2.安装node，安装node的同时也会安装npm

```
brew install node
```

3.安装watchman，watchman是用来检测文件变化的工具

```
brew install watchman
```

4.安装flow，flow是用来检测jsx语法的工具

```
brew install flow
```

5.接着通过npm安装react-native的命令行工具， －g表示安装到全局模式，这个安装过程我等了好久都没更新，最后还是翻墙之后才安装完成

```
npm install -g react-native-cli
```

#### 二、初始化一个ReactNative项目

之前已经安装好react-native的命令行工具，所以直接在终端执行如下命令，即可创建一个新的Xcode工程

```
react-native init HelloReactNative
```

完成之后看一下这个工程的目录结构

![0726_reactnative_files.png](/src/imgs/1507/0726_reactnative_files.png)

#### 三、双击`HelloReactNative.xcodeproj`在Xcode中打开项目

接着就可以在模拟器中直接运行这个应用了。

![0726_helloreactnative_app.png](/src/imgs/1507/0726_helloreactnative_app.png)

#### 四、打开项目根目录下的index.ios.js文件，这个文件就是用来显示我们上面看到的那个界面，看一下里面的代码

1.下面这行代码是用于开启 Strict Mode

```
'use strict';
```

2.下面这行代码是将 react-native 模块加载进来，并将它赋值给变量 React 的

> React Native 使用同 Node.js 相同的模块加载方式：require，这个概念可以等同于 java 中的 import 的概念。

```
var React = require('react-native');
```

3.批量定义组件

```
var {
  AppRegistry,
  StyleSheet,
  Text,
  View,
} = React;
```

这几行代码等同于:

```
var AppRegistry = React.AppRegistry;
var StyleSheet = React.StyleSheet;
var Text = React.Text;
var View = React.View;
```

4.定义视图的内容：

```
var HelloReactNative = React.createClass({
  render: function() {
    return (
      <View style={styles.container}>
        <Text style={styles.welcome}>
          Welcome to React Native!
        </Text>
        <Text style={styles.instructions}>
          To get started, edit index.ios.js
        </Text>
        <Text style={styles.instructions}>
          Press Cmd+R to reload,{'\n'}
          Cmd+D or shake for dev menu
        </Text>
      </View>
    );
  }
});
```

上面这段代码用语构建HelloReactNative的入口类，React提供了React.createClass的方法创建一个类。
里面的render方法就是渲染视图用的。return返回的是视图的模板代码。
其实上面这是JSX的模板语法。


5.除了上面提供的视图内容，还需要视图的样式。StyleSheet.create就是通过JS的自面量表达了css样式。

```
var styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5,
  },
});
```

那么如何引入css样式呢，可以看到在上面视图内容的代码中，render方法返回的视图模板里已经体现出来了
即style={styles.container}.其中style是视图的一个属性，styles是我们定义的样式表，container是样式表中的一个样式。

6.最后，注册应用入口

```
AppRegistry.registerComponent('HelloReactNative', () => HelloReactNative);
```

####  五、有几个文件是需要注意下的

1.在xcode项目代码中AppDelegate.m会标识入口文件，例如：

```
jsCodeLocation = [NSURL URLWithString:@"http://localhost:8081/index.ios.bundle"];
```

如果是网上下载别人的源码，注意此处的ip和端口是否有被修改。

2.闪屏界面在哪修改？在xcode项目中找到LaunchScreen.xib文件，点击，你会看到界面，这个就是启动界面，手动添加组件或者修改文本即可。

3.文本编辑器打开index.ios.js文件，是js代码的入口文件，所有的代码编写从这开始，可以定义自己的模块和引入第三方模块。

- - -
THE END.
