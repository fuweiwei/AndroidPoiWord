# MyPoiWordDemo
Android 使用模板生成Word文档，支持手机直接查看word 

  最近在项目工作中，碰到一个很棘手的需求，说是要在手机端根据模板生成word文档，而且不借助第三方的软件可以查看word文档，一开始听这个需求差不多蒙了，这要怎么做，为什么不把生成word文档这个工作放在后台呢，抱怨归抱怨，但是面对需求只能硬着头皮做了，经过各种拷问度娘和谷哥，终于找了一个比较好用的方法。特此跟他家分享。

  Apache 公司推出的 Apache POI，我们来看下他的介绍：Apache POI 是用Java编写的免费开源的跨平台的 Java API，Apache POI提供API给Java程式对Microsoft Office格式档案读和写的功能。
  废话少说开始编码，首先我们要下Apache POI的开发jar包，下载地址：http://poi.apache.org/download.html，这里推荐不要下最新版本的，因为一开始我用最新版本的会出一下莫名其妙的问题，后面换旧的版本就OK了。这里我用的是3.9的还是比较稳定的、


  开发有2个包，有一点我就非常郁闷Apache居然没有提供api稳定，开发起来还是比较蛋疼的，可能是我自己没有找到把，如果有知道的筒子可以@我、嘿嘿。不过Apache还是提供了Demo大家可以参考。
  详情见博客：http://blog.csdn.net/u011916937/article/details/50085441
