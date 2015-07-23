package kpp.jqtx.core;

import java.io.StringWriter;
import java.util.ArrayList;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public final class XmlUtils
{
  private XmlUtils() { }
  
  public static ArrayList<Node> getChildrenElements(Node node)
  {
    ArrayList<Node> result = new ArrayList<Node>();
    
    NodeList list = node.getChildNodes();
    for (int i = 0; i < list.getLength(); i++)
    {
      Node child = list.item(i);
      if (isElement(child))
        result.add(child);
    }
    
    return result;
  }
  
  public static Element getFirstChild(Node node)
  {
    if (node == null)
      return null;
    
    Node child = node.getFirstChild();
    if (child == null)
      return null;
    
    while (!isElement(child))
      child = child.getNextSibling();
    
    return (Element) child;
  }

  public static boolean isElement(Node child)
  {
    return child != null && child.getNodeType() == Element.ELEMENT_NODE;
  }
  
  public static String toString(Node node) throws Exception
  {
    Transformer tr = TransformerFactory.newInstance().newTransformer();
    StringWriter sw = new StringWriter();
    StreamResult sr = new StreamResult(sw);
    tr.setOutputProperty(OutputKeys.INDENT, "yes");
    tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
    tr.transform(new DOMSource(node), sr);
    return sw.toString();
  }

  public static Element cloneNode(Element ele)
  {
    return (Element) ele.cloneNode(true);
  }
}
