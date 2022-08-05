import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
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

import org.apache.xpath.CachedXPathAPI;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import java.io.StringWriter;
import java.io.Writer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

public class App {

    public static void main(String[] args) throws Exception {

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            org.w3c.dom.Document doc = db.parse(new File("EPG.xml"));
            eventSort(doc);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void eventSort(Document doc) throws TransformerConfigurationException {

        // nodeList containing all epgListItems
        XPath xPath = XPathFactory.newInstance().newXPath();
        String path = "//list[@name='eventList']/value/epgListItem";
        NodeList nodelist3 = null;
        CachedXPathAPI v = new CachedXPathAPI();

        try {
            nodelist3 = v.selectNodeList(doc, path);
        } catch (TransformerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String k = null;
        String var = "/content/group/definition/list/value/epgListItem/group/definition/group/definition/text[@name='startTime']/value";
        Node currentEvent = null;
        NodeList nodelist2 = null;
        StringWriter sw = new StringWriter();
        TreeMap treemap = new TreeMap();
        Boolean u = true;
        ArrayList array = new ArrayList();

        try {

            Transformer serializer;
            serializer = TransformerFactory.newInstance().newTransformer();

            // getting epg nodes
            nodelist2 = v.selectNodeList(doc, var);

            for (int i = 0; i < nodelist3.getLength(); i++) {

                // nodes to text
                serializer.transform(new DOMSource(nodelist3.item(i)), new StreamResult(sw));
                currentEvent = nodelist3.item(i);
                k = nodelist2.item(i).getTextContent();
                array.add(k);
                treemap.put(k, currentEvent);
                System.out.println(treemap);

                currentEvent = null;
                sw = null;
                sw = new StringWriter();

            }
        } catch ( TransformerException | TransformerFactoryConfigurationError e) {
            e.printStackTrace();
        }

        File file = new File("temp.xml");
        BufferedWriter bf = null;
        try {
            bf = new BufferedWriter(new FileWriter(file));
            bf.flush();
            String treetext = null;
            treetext = "" + treemap.values();
            bf.write(treetext);
            bf.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Node finish = null;
        for (int i=0; i<nodelist2.getLength();i++){
            
        Node node1 = (Node) treemap.get(array.get(i));
        finish = doc.replaceChild(nodelist3.item(i), node1);

        
        i=0;
        for (i = 0; i < nodelist3.getLength(); i++) {

            Node filter = nodelist3.item(i);
            Element value = (Element) filter;
            value.setAttribute("id", String.valueOf(i));

            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer xformer = factory.newTransformer();
            xformer.setOutputProperty(OutputKeys.INDENT, "yes");
            Writer output = new StringWriter();
            try {
                xformer.transform(new DOMSource(doc), new StreamResult(output));
            } catch (TransformerException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println(output);
        }

        
        file = new File("sorted.xml");
        bf = null;
        try {
            bf = new BufferedWriter(new FileWriter(file));
            bf.flush();
            bf.write(finish.toString());
            bf.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        /*
         * node count
         * System.out.println(nodelist.getLength());
         * 
         * //node to string, change nodelist.item
         * 
         * try{
         * StringWriter sw = new StringWriter();
         * Transformer serializer = TransformerFactory.newInstance().newTransformer();
         * serializer.transform(new DOMSource(nodelist.item(*)), new StreamResult(sw));
         * String result = sw.toString();
         * 
         * System.out.println(result);
         * }catch{TransformerException e}
         * 
         * FileWriter myWriter= null;
         * try {
         * myWriter = new FileWriter("HashMapTest.txt");
         * } catch (IOException e) {
         * // TODO Auto-generated catch block
         * e.printStackTrace();
         * }
         * 
         * for(int u=0; u<hashmap.size(); u++){
         * try {
         * String str = hashmap.toString();
         * System.out.println(str);
         * myWriter.write(str);
         * } catch (IOException e) {
         * // TODO Auto-generated catch block
         * e.printStackTrace();
         * }
         * }
         * 
         */

    }
}
}