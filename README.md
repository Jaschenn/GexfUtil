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