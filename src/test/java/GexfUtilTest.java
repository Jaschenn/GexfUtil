import com.jasmine.util.GexfUtil;
import org.junit.Test;

import java.io.File;

public class GexfUtilTest {
    @Test
    public void testGenerateFiles(){
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
