package com.jasmine.util;

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
import java.io.Writer;
import java.util.Calendar;
import java.util.HashMap;

public class GexfUtil {
    private static Gexf gexf = new GexfImpl();
    private static File file;
    private static FileWriter fileWriter;
    private static StaxGraphWriter staxGraphWriter = new StaxGraphWriter();
    private static Graph graph = gexf.getGraph();
    private static HashMap hashMap = new HashMap();
    private static Attribute modularity_class;
    static {
        gexf.getMetadata().setCreator("GexfUtil_Create_By_Jasc");
        graph.setDefaultEdgeType(EdgeType.UNDIRECTED).setMode(Mode.STATIC);
        AttributeList attributes = new AttributeListImpl(AttributeClass.NODE);
        graph.getAttributeLists().add(attributes);
        modularity_class = attributes.createAttribute("modularity_class",AttributeType.INTEGER,"Modularity Class");//类别为整形

    }

    public static void setFile(File file,Boolean isappend) {
        GexfUtil.file = file;
        try {
            fileWriter = new FileWriter(file,isappend);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("文件路径错误");
        }

    }
    /*
    * 首先要设置文件
    * 然后就是增加节点和边
    * 最后flush到文件
    *
    *
    * */
    public static void addNode(String id,String lable,String categories,int size){

        Node node = graph.createNode(id);
        node.setLabel(lable);
        node.setSize(size);
        node.getAttributeValues().addValue(modularity_class,categories);
        hashMap.put(id,node);


    }
    public static void addNode(String id,String lable,String categories){
        Node node = graph.createNode(id);
        node.setLabel(lable);
        node.getAttributeValues().addValue(modularity_class,categories);
        hashMap.put(id,node);
    }

    public static void addEdge(String sourceid,String distid){
        ((Node)hashMap.get(sourceid)).connectTo((Node)hashMap.get(distid));
    }

    public static void flush(){
        try {
            staxGraphWriter.writeToStream(gexf,fileWriter,"UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("IO异常！");
        }
    }
}
