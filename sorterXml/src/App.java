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

    public static void main(String[] args) throws Exception {

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
        String k = null;
        Node finish = null;
        Node newNode = null;
        Element value = null;
        Node wholeList = null;
        NodeList epgList = null;
        Node currentEvent = null;
        NodeList nodelist2 = null;
        TreeMap treemap = new TreeMap();
        ArrayList treemapKeys = new ArrayList();
        StringWriter sw = new StringWriter();
        CachedXPathAPI v = new CachedXPathAPI();
        String path = "//list[@name='eventList']/value/epgListItem";
        String var = "/content/group/definition/list/value/epgListItem/group/definition/group/definition/text[@name='startTime']/value";

        try {
            epgList = v.selectNodeList(doc, path);
            wholeList = v.selectSingleNode(doc, "//list[@name='eventList']/value");
            Transformer serializer;
            serializer = TransformerFactory.newInstance().newTransformer();
            nodelist2 = v.selectNodeList(doc, var);

            // treemap population
            for (i = 0; i < epgList.getLength(); i++) {

                serializer.transform(new DOMSource(epgList.item(i)), new StreamResult(sw));
                currentEvent = epgList.item(i);
                k = nodelist2.item(i).getTextContent();
                treemapKeys.add(k);
                treemap.put(k, currentEvent);

                sw = null;
                currentEvent = null;
                sw = new StringWriter();
            }
        } catch (TransformerException | TransformerFactoryConfigurationError e) {
            e.printStackTrace();
        }

        treemapKeys.sort(null);
        i = 0;

        // remove nodes
        for (i = 0; i < nodelist2.getLength(); i++) {
            wholeList.removeChild(epgList.item(i));
        }

        // changing id's and replacing
        for (i = 0; i < nodelist2.getLength(); i++) {
            newNode = (Node) treemap.get(treemapKeys.get(i));
            value = (Element) newNode;
            value.setAttribute("id", String.valueOf(i));
            finish = (Node) value;
            wholeList.appendChild(finish);
        }
        return doc;
    }
}