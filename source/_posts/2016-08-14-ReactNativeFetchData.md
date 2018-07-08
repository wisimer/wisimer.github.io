---
date: 2016-08-14
layout: default
category: ANDROID
tags: ReactNative
title: ReactNative从网络获取数据并显示
---

这里我们来实现一个极其简单的天气预报的小例子。获取网络接口天气预报的json数据，再显示在Text上。同时有个输入框，可以输入城市信息，再按确认，则会更新数据。同时还可以以列表形式展示未来几天的天气预报。

<!--more-->

#### 一、首先是单个元素，来布个局吧

这里我们用到了聚合API的天气预报接口 ：
[http://v.juhe.cn/weather/index?format=2&cityname=%E4%B8%8A%E6%B5%B7&key=71efbb690b9bc9a389ad90502306c8cd](http://v.juhe.cn/weather/index?format=2&cityname=%E4%B8%8A%E6%B5%B7&key=71efbb690b9bc9a389ad90502306c8cd)

```
class FetchData extends Component {
    constructor(props) {
        super(props);
        this.state = {weather: '天气'};
    }
    componentDidMount() {
        this.fetchWeatherData("上海");
    }
    render() {
        return(
            <View style={styles.containerStyle}>
            <TextInput
                style={styles.textInputStyle}
                onChangeText={(text) => this.handleTextChange(text)} >
            </TextInput>
            <TouchableHighlight
                style={styles.confirmStyle}
                onPress={() => this.fetchWeatherData(this.state.address)} >
                <Text >确认</Text>
            </TouchableHighlight>
            <Text>{this.state.weather}</Text>
            </View>
        );
    }
｝
```

这里的构造函数值得注意一下。一开始我还犯二，用的是ES5的格式写的，一直报错。后来网上查了一下[ES5和ES6](http://www.cnblogs.com/Mrs-cc/p/4969755.html)的差别。

`componentDidMount()` 表示组件加载好了第一次请求数据。

（1）最上面是一个 `TextInput` ，用来输入地名。这里为了能够让下面的确认按钮拿到TextInput里面的内容，要为它添加一个onChangeText监听函数handleTextChange:

```
handleTextChange(addressText) {
    this.setState({
        address:addressText
    });
}
```

这样每次TextInput的内容改变了都会更新到 `address` 属性中去。

（2）中间是一个确认按钮，这里要复习一下 `TouchableHighlight` 只能作为一个容器，里面放Text来实现按钮。TouchableHighlight的点击时间就是onPress函数了，为onPress绑定 `fetchWeatherData` 函数，这样每次点击确认按钮都会重新去请求天气数据：

```
fetchWeatherData(address) {
    var url = "http://v.juhe.cn/weather/index?format=2&cityname="+address+"&key=71efbb690b9bc9a389ad90502306c8cd";
    fetch(url)
    .then((response) => response.json())
    .then((responseData) => {
        this.setState({
            weather: !responseData.result ? "" : responseData.result.sk
        });
    })
    .done();
}
```

每次获取到json数据之后都会将sk字段更新到 `weather` 状态。

（3）最下面就是用来显示天气预报的 `Text` 了。直接显示 `this.state.weather` 即可。

看看效果图:

![0814_rn_fetch.gif](/src/imgs/1608/0814_rn_fetch.gif)

#### 二、下面顺理成章就是加载列表数据了，也来布个局吧

```
class WxpListView extends Component {
    constructor(props) {
        super(props);
        this.state = {
          preWeather: "",
          fecthedData: false
        };
    }

    componentDidMount() {
        this.fetchWeatherData("上海");
    }

    renderListRow(rowData) {
        return(
            <Text>{rowData.week}:{rowData.weather}</Text>
        );
    }

    render() {
        if(this.state.fecthedData) {
            return(
                <View style = {styles.containerStyle}>
                    <ListView
                        dataSource={this.state.preWeather}
                        renderRow={this.renderListRow.bind(this)}
                        >ListView</ListView>
                </View>
            )
        }
        return(<View style = {styles.containerStyle}>
            <Text>ListView</Text>
        </View>)
    }
}
```

（1）这里的`fecthedData`判断语句是用来保证未加载好数据时，页面不会展示ListView，因为如果ListView的 `dataSource` 属性为空的话是会报render函数错误滴。同时`renderRow`函数是用来绑定每个Item的布局的，也就是上面的`renderListRow`函数。

（2）来看一下比较重要的`fetchWeatherData`函数里面返回Json数据的代码。

```
fetchWeatherData(address) {
    var url = "http://v.juhe.cn/weather/index?format=2&cityname="+address+"&key=71efbb690b9bc9a389ad90502306c8cd";
    fetch(url)
    .then((response) => response.json())
    .then((responseData) => {
        var dataSource = new ListView.DataSource(
            {rowHasChanged: (r1, r2) => r1.date !== r2.date});
        this.setState({
            preWeather: !responseData.result ? "" : dataSource.cloneWithRows(responseData.result.future),
            fecthedData: true
        });
        console.log(responseData.result.future)
    })
    .done();
}
```

注意这里直接返回Json数据之后是要把Json转换成 `ListView.DataSource` 类型的对象的。否则的话ListView会报一个不能接受其他类型的数据的错误。解析完数据之后，将 `fecthedData` 置为true，ListView就可以展示数据了。如图：

![0814_RNListView.jpg](/src/imgs/1608/0814_RNListView.jpg)

- - -
THE END.
