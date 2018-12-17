[![Build Status](https://travis-ci.org/Jaschenn/GexfUtil.svg?branch=master](https://travis-ci.org/Jaschenn/GexfUtil.svg?branch=master)

# GexfUtil
GEXF (Graph Exchange XML Format) is a language for describing complex networks structures, their associated data and dynamics. GexfUtil is a tool to generate .gexf files 
## Dependence
This tool was developed on the basis of francesco-ficarola/gexf4j and therefore requires the dependency of gexf4j.
It is recommended to use maven here.
```
 <dependency>
        <groupId>it.uniroma1.dis.wsngroup.gexf4j</groupId>
        <artifactId>gexf4j</artifactId>
        <version>1.0.0</version>
    </dependency>
```

## Guide
This tool is usually used in the reduce phase of the MapReduce computing framework.

### Add a node
```
 GexfUtil.addNode(key.toString(), String.valueOf(sum), key.toString(), sum%100);
```
 The four parameters represent the id of the node, lable, categories, size
### Add a side

```
 GexfUtil.addEdge(key.toString(), lastKey);
```
### Samples
#### HTML
```html
<%--
  User: jaschen
  Date: 2018-11-25
  Time: 10:14
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>GexfUtil</title>
    <script src="https://upcdn.b0.upaiyun.com/libs/jquery/jquery-2.0.2.min.js"></script>
    <script src="lib/echarts.js"></script>
    <script src="lib/dataTool.js"></script>
</head>
<body>
<div id="main" style="width: 900px;height: 800px">

</div>
<script>
    var myChart =  echarts.init(document.getElementById('main'));
    myChart.showLoading();
    $.get('sample.gexf', function (xml) {
        //static_graph_sample
        myChart.hideLoading();
        var graph = echarts.dataTool.gexf.parse(xml);
        var categories = [];
        //设置类别，类别需要手动设置
        for (var i = 0; i < 3; i++) {
            categories[i] = {
                name: '列别' + i //change your code here
            };
        }
        graph.nodes.forEach(function (node) {
            node.itemStyle = null;
            node.value = node.symbolSize;
        //    node.symbolSize = 80; //统一指定节点的大小，默认按照创建时候的大小进行显示
            node.label = {
                normal: {
                    show: node.symbolSize > 0 //设置当大于多少的时候显示node的label
                }
            };
            node.category = node.attributes.modularity_class;//设置每一个node的类别，这里的modularity_class要与gexf文件中的一致
        });
        //开始设置表图的属性
        option = {
            title: {
                text: 'GexfUtil Samples',
                subtext: 'Created By Jaschen',
                top: 'bottom',
                left: 'right'
            },
            tooltip: {},
            legend: [{
                //selectedMode: 'single',//设置一个时间只显示一个列别
                data: categories.map(function (a) {
                    return a.name;
                })
            }],
            animationDuration: 1500,
            animationEasingUpdate: 'quinticInOut',
            series : [
                {
                    name: 'GexfUtil Samples',
                    type: 'graph',
                    layout: 'force',
                    data: graph.nodes,
                    links: graph.links,
                    categories: categories,
                    roam: true,//是否开启鼠标缩放
                    focusNodeAdjacency: true,//突出显示节点
                    force:{
                        edgeLength:360,
                        layoutAnimation:true,//是否显示添加node之后动画
                        repulsion:999 //斥力因子，数值越大，表示斥力越大
                    },
                    itemStyle: {
                        normal: {
                            borderColor: '#fbf6ff',//node的颜色
                            borderWidth: 1,
                            shadowBlur: 10,
                            shadowColor: 'rgba(0, 0, 0, 0.3)'
                        }
                    },
                    label: {
                        position: 'right',
                        formatter: '{b}'
                    },
                    lineStyle: {
                        color: 'source',
                        curveness: 0.3 //边的曲度，从0-1，数值越大，曲度越大。
                    },
                    emphasis: {
                        lineStyle: {
                            width: 10
                        }
                    }
                }
            ]
        };
        myChart.setOption(option);
    }, 'xml');

</script>
</body>
</html>

```
#### sample.gexf
```xml
<?xml version='1.0' encoding='UTF-8'?>
<gexf xmlns="http://www.gexf.net/1.2draft" xmlns:viz="http://www.gexf.net/1.2draft/viz" version="1.2">
 <meta>
  <creator>GexfUtil_Create_By_Jasc</creator>
 </meta>
 <graph defaultedgetype="undirected" idtype="string" mode="static">
  <attributes class="node" mode="static">
   <attribute id="modularity_class" title="Modularity Class" type="integer"/>
  </attributes>
  <nodes count="3">
   <node id="1" label="1">
    <attvalues>
     <attvalue for="modularity_class" value="0"/>
    </attvalues>
    <viz:size value="10.0"/>
   </node>
   <node id="2" label="2">
    <attvalues>
     <attvalue for="modularity_class" value="1"/>
    </attvalues>
    <viz:size value="10.0"/>
   </node>
   <node id="3" label="3">
    <attvalues>
     <attvalue for="modularity_class" value="2"/>
    </attvalues>
    <viz:size value="20.0"/>
   </node>
  </nodes>
  <edges count="3">
   <edge id="3a1e19e7-ef4b-4516-8dec-3c1edb397b6a" source="1" target="2" type="undirected"/>
   <edge id="a3e9999a-c532-4913-90eb-ba1949508102" source="2" target="3" type="undirected"/>
   <edge id="4ae5e2d6-a366-4aea-86fd-0576b3edba16" source="3" target="1" type="undirected"/>
  </edges>
 </graph>
</gexf>
```
#### Java code 
```java
public class Main {
      public static void main(String[] args)  throws IOException {
  GexfUtil.setFile(new File("web/sample.gexf"),false);
       GexfUtil.addNode("1","1","1",10);
       GexfUtil.addNode("2","2","2",10);
       GexfUtil.addNode("3","3","3",20);
       GexfUtil.addEdge("1","2");
       GexfUtil.addEdge("2","3");
       GexfUtil.addEdge("3","1");
       GexfUtil.flush();
       }
}
```