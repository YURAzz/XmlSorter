import java.io.IOException;
import java.util.HashMap;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

//only used if checking a nodeList content 

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import org.w3c.dom.DOMException;
import java.io.StringWriter;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

public class App {

    public static void main(String[] args) throws Exception {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            org.w3c.dom.Document doc = db
                    .parse(new File("EPG.xml"));
                    eventSort(doc);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

    }

    public static NodeList eventSort(Document doc) {
 
        //nodeList containing all epgListItems 
        XPath xPath = XPathFactory.newInstance().newXPath();
        String path = "//list[@name='eventList']/value/epgListItem";
        NodeList nodelist = null;

        try {
            nodelist = (NodeList) xPath.compile(path).evaluate(doc, XPathConstants.NODESET);

        } catch (XPathExpressionException e) {
            e.printStackTrace();
        } 

        //HashMap creation
        HashMap hashmap/*String, Node*/ = new HashMap();
        String k = null;
        String var = "/content/group/definition/list/value/epgListItem/group/definition/group/definition/text[@name='startTime']/value";
        String currentEvent = null;
        NodeList nodelist2 = null;
        StringWriter sw = new StringWriter();
        String mela = null;
        
        try{
            

            Transformer serializer;
            serializer = TransformerFactory.newInstance().newTransformer();

            //getting epg nodes
            nodelist2 = (NodeList) xPath.compile(var).evaluate(doc, XPathConstants.NODESET);

            for(int i = 0; i<nodelist.getLength();i++){

                    //nodes to text
                    serializer.transform(new DOMSource(nodelist.item(i)), new StreamResult(sw));
                    currentEvent = sw.toString();
                    k = nodelist2.item(i).getTextContent();

                hashmap.put(k,currentEvent);

            }


            File file = new File("HashMapTest.xml");
  
            BufferedWriter bf = null;
                bf = new BufferedWriter(new FileWriter(file));

                bf.write(currentEvent);

                bf.flush();
            }
            catch (IOException | XPathExpressionException |TransformerException |TransformerFactoryConfigurationError e) {
                e.printStackTrace();
            }





            /*
            node count
            System.out.println(nodelist.getLength()); 

            //node to string, change nodelist.item
            
            try{
            StringWriter sw = new StringWriter();
            Transformer serializer = TransformerFactory.newInstance().newTransformer();
            serializer.transform(new DOMSource(nodelist.item(*)), new StreamResult(sw));
            String result = sw.toString();

            System.out.println(result);
            }catch{TransformerException e}

            //something
            
            FileWriter myWriter= null;
            try {
                myWriter = new FileWriter("HashMapTest.txt");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            for(int u=0; u<hashmap.size(); u++){
            try {
                String str = hashmap.toString();
                System.out.println(str);
                myWriter.write(str);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            }

             */
    
    return nodelist;
        }
}
