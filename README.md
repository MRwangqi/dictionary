# dictionary
![这里写图片描述](img/c.gif)
翻译词霸功能： 
一、主页每天更新100个单词练习 
二、单词随机练习，从数据库里面随机取100个数据 
三、顺序练习，通过xRecycleView分页加载单词，总共1万3的单词，分页每次加载10个单词，这样体验比起全部加载会比较流畅些。 
四、网络单词查找，用的是有道的API，翻译还是挺不错的。 
五、单词在线查询用的是扇贝的API，扇贝的API算是最有良心的，不仅返回单词的解释，还有例句、发音，还有英文解释，大大的良心，更大的良心是，这些json数据里面有一个url，打开后居然是一个H5页面，我的天哪，这个h5里面什么都有，发音到词句，大大的良心，所以，页面在线查询我直接访问API获取了url，直接用webview去加载url，体验真的是一个单词来形容—perfect
