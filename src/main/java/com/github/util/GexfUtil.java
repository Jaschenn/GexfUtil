package com.github.util;
/**
 * @author jaschen
 * @version 0.1.1
 *
 *
 * */
import it.uniroma1.dis.wsngroup.gexf4j.core.*;
import it.uniroma1.dis.wsngroup.gexf4j.core.data.Attribute;
import it.uniroma1.dis.wsngroup.gexf4j.core.data.AttributeClass;
import it.uniroma1.dis.wsngroup.gexf4j.core.data.AttributeList;
import it.uniroma1.dis.wsngroup.gexf4j.core.data.AttributeType;
import it.uniroma1.dis.wsngroup.gexf4j.core.impl.GexfImpl;
import it.uniroma1.dis.wsngroup.gexf4j.core.impl.StaxGraphWriter;
import it.uniroma1.dis.wsngroup.gexf4j.core.impl.data.AttributeListImpl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class GexfUtil {
    private static Gexf gexf = new GexfImpl();
    private static File file;
    private static FileWriter fileWriter;
    private static StaxGraphWriter staxGraphWriter = new StaxGraphWriter();
    private static Graph graph = gexf.getGraph();
    private static HashMap nodesMap = new HashMap();
    private static Attribute modularity_class;
    static {

        gexf.setVisualization(true);//设置viz可见性
        gexf.getMetadata().setCreator("GexfUtil_Create_By_Jasc");
        graph.setDefaultEdgeType(EdgeType.UNDIRECTED).setMode(Mode.STATIC);
        AttributeList attributes = new AttributeListImpl(AttributeClass.NODE);
        graph.getAttributeLists().add(attributes);
        modularity_class = attributes.createAttribute("modularity_class",AttributeType.INTEGER,"Modularity Class");//类别为整形

    }

    public static HashMap getNodesMap() {
        return nodesMap;
    }

    /**
     * @param file 要生成的文件，默认目录为项目根目录
     *
     * @param isappend 是否覆盖文件内容，通常情况下建议选择false，重写文件内容
     **/
    public static void setFile(File file,Boolean isappend) {
        GexfUtil.file = file;
        try {
            fileWriter = new FileWriter(file,isappend);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("文件路径错误");
        }

    }

    /**
     * @param id 表示节点的id，注意不可以重复，类行为string
     * @param label 节点的标签，可以用来在关系图中显示节点的信息
     * @param size 节点的大小，建议在1-100内
     * @param categories 节点的类别，在这里是通过设置一个属性modularity_class完成的
     * */
    public static void addNode(String id,String label,String categories,int size){
        if(!nodesMap.containsKey(id)) {
            Node node = graph.createNode(id);
            node.setLabel(label);
            node.setSize(size);
            node.getAttributeValues().addValue(modularity_class, categories);
            nodesMap.put(id, node);

        }else {
            GexfUtil.updateNode(id,label,categories,size);
        }
    }
    public static void updateNode(String id,String label,String categories,int size){

        if(nodesMap.containsKey(id)) {
            Node node = (Node) nodesMap.get(id);
            if (!label.equals("")) {
                node.setLabel(label);
            }
            if (!categories.equals("")) {
                node.getAttributeValues().addValue(modularity_class, categories);
            }
            if (!(size == 0)) {
                node.setSize(size);
            }
            nodesMap.put(id, node);
        }
    }
    public static void addNode(String id,String lable,String categories){
        if(!nodesMap.containsKey(id)) {
            Node node = graph.createNode(id);
            node.setLabel(lable);
            node.getAttributeValues().addValue(modularity_class, categories);
            nodesMap.put(id, node);
        }
    }
    /**
     * @param sourceid 边的源节点地址，这里需要填入的是边的id
     * @param distid 边的目的节点地址，这里需要填入的是边的id
     * */
    public static void addEdge(String sourceid,String distid){
       if(!nodesMap.containsKey(sourceid)) {
        GexfUtil.addNode(sourceid,"temp","999");
       }
       if(!nodesMap.containsKey(distid)) {
            GexfUtil.addNode(distid,"temp","999");
        }

        ((Node)nodesMap.get(sourceid)).connectTo((Node)nodesMap.get(distid));
    }
    /**
     * @Description 该方法将之前输入的节点和边写入到文件中，因此在写入边和节点完成之后，请务必调用该方法。
     * */
    public static void flush(){
        try {
            int NSIZE = 0;
            List<Node> nodes = graph.getNodes();
            for (Node n :
                    nodes) {
                List list = n.getEdges();
                if(n.getLabel().equals("temp")){
                    n.setSize(-1);//如果存在临时添加的节点，那么就把它的大小设置为不可见
                }
            }
            staxGraphWriter.writeToStream(gexf,fileWriter,"UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("IO异常！");
        }
    }
}
