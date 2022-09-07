import java.io.File;
import org.w3c.dom.Node;
import java.util.TreeMap;
import java.util.ArrayList;
import java.io.IOException;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import java.io.StringWriter;
import org.xml.sax.SAXException;
import org.apache.xpath.CachedXPathAPI;
import javax.xml.transform.Transformer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.TransformerException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerFactoryConfigurationError;

public class App {

    public static void main(String[] args){

        // parse doc
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            org.w3c.dom.Document doc = db.parse(new File("EPG.xml"));
            eventSort(doc);

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    public static Document eventSort(Document doc) {

        int i = 0;
        Element value = null;
        Node wholeList = null;
        String startTime = null;
        Node changedNode = null;
        Node currentEvent = null;
        NodeList epgNodeList = null;
        Transformer serializer = null;
        NodeList startTimeList = null;
        TreeMap treeMap = new TreeMap();
        StringWriter sw = new StringWriter();
        ArrayList arrayKeys = new ArrayList();
        CachedXPathAPI v = new CachedXPathAPI();
        String epgNodeVar = "//list[@name='eventList']/value/epgNodeListItem";
        String startTimeVar = "/content/group/definition/list/value/epgNodeListItem/group/definition/group/definition/text[@name='startTime']/value";
        String valueVar = "//list[@name='eventList']/value";

        try {

            epgNodeList = v.selectNodeList(doc, epgNodeVar);
            startTimeList = v.selectNodeList(doc, startTimeVar);
            wholeList = v.selectSingleNode(doc, valueVar);
            
            // treeMap population
            for (i = 0; i < epgNodeList.getLength(); i++) {
                
                serializer = TransformerFactory.newInstance().newTransformer();
                serializer.transform(new DOMSource(epgNodeList.item(i)), new StreamResult(sw));
                currentEvent = epgNodeList.item(i);
                startTime = startTimeList.item(i).getTextContent();
                arrayKeys.add(startTime);
                treeMap.put(startTime, currentEvent);

                currentEvent = null;
                sw = new StringWriter();
            }
        } catch (TransformerException | TransformerFactoryConfigurationError e) {
            e.printStackTrace();
        }

        arrayKeys.sort(null);

        // changing id's and replacing
        for (i = 0; i < startTimeList.getLength(); i++) {
            changedNode = (Node) treeMap.get(arrayKeys.get(i));
            value = (Element) changedNode;
            value.setAttribute("id", String.valueOf(i));
            wholeList.replaceChild((Node) changedNode, epgNodeList.item(i));
        }
        return doc;
    }
}