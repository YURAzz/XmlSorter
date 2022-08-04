import java.io.IOException;
import java.util.HashMap;
import java.io.File;
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
            nodelist.getLength();

        } catch (XPathExpressionException e) {
            e.printStackTrace();
        } 

        //HashMap creation
        HashMap hashmap/*String, Node*/ = new HashMap();
        String k = null;
        String var = "/content/group/definition/list/value/epgListItem/group/definition/group/definition/text[@name='startTime']/value";
        NodeList nodelist2 = null;
        String currentEvent = null;
    
     //node to string, change nodelist.item
            
     try{
        








            int j = 0;
            nodelist2 = (NodeList) xPath.compile(var).evaluate(doc, XPathConstants.NODESET);
            for(int i = 0; i<nodelist2.getLength();i++){



                StringWriter sw = new StringWriter();
                Transformer serializer = TransformerFactory.newInstance().newTransformer();
                try {
                    serializer.transform(new DOMSource(nodelist.item(i)), new StreamResult(sw));
                } catch (TransformerException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                currentEvent = sw.toString();
        




                k = nodelist2.item(i).getTextContent();
                hashmap.put(k,currentEvent);
                System.out.println(hashmap);
                j++;
            }

        } catch (XPathExpressionException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TransformerFactoryConfigurationError e) {
            // TODO Auto-generated catch block
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


             */
    
    return nodelist;
        }
}
