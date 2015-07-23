package kpp.jqtx.core;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

import kpp.jqtx.core.QueryList.Query;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Executer
{
  private Element xml;
  
  private QueryList queries;
  
  /** Wykonanie */
  public void execute() throws SQLException
  {
    if (xml == null || queries == null)
      return;
    
    findRepeatee(xml);
  }
  
  private void findRepeatee(Node parent) throws SQLException
  {
    for (Node child : XmlUtils.getChildrenElements(parent))
    {
      Node attr = child.getAttributes().getNamedItem("q:repeat");
      if (attr == null)
        continue;
      
      String qrepeat = attr.getNodeValue();
      repeat(qrepeat, (Element)child);
    }
  }

  private void repeat(String queryId, Element ele) throws SQLException
  {
    QueryList.Query qry = QueryList.getById(queryId);
    ResultSet rs = loadData(qry);
    iterate(rs, ele, queryId);
    
    ele.getParentNode().removeChild(ele);
  }
  
  private ResultSet loadData(Query qry) throws SQLException
  {
    String sqlQuery = findAndReplaceVarsInText(qry.Value);
    PreparedStatement ps = ConnectionManager.get().prepareStatement(sqlQuery);
    return ps.executeQuery();
  }

  private void iterate(ResultSet rs, Element ele, String queryId) throws SQLException
  {
    while (rs.next())
    {
      Element clone = XmlUtils.cloneNode(ele);
      
      Variables.update(rs, queryId);
      findAndReplaceVarsInElement(clone);
      
      findRepeatee(clone);
      
      prepareNode(clone);
      ele.getParentNode().appendChild(clone);
    }
  }

  private void prepareNode(Node node)
  {
    NamedNodeMap attrs = node.getAttributes();
    if (attrs != null && attrs.getLength() > 0)
    {
      Node qrepeat = attrs.getNamedItem("q:repeat");
      if (qrepeat != null)
        node.getAttributes().removeNamedItem(qrepeat.getNodeName());
    }
    
    Node child = node.getFirstChild();
    if (child == null)
      return;
    
    do
    {
      if (XmlUtils.isElement(child))
        prepareNode(child);
      else
        node.removeChild(child);
    } while ((child = child.getNextSibling()) != null);
  }
  
  private String findAndReplaceVarsInText(String text)
  {
    Set<String> vars = Variables.find(text);
    for (String varName : vars)
    {
      text = text.replaceAll(String.format("\\$\\{%s\\}", varName), String.valueOf(Variables.get(varName)));
    }
    return text;
  }
  
  /** */
  private void findAndReplaceVarsInElement(Element ele)
  {
    NamedNodeMap attrs = ele.getAttributes();
    for (int i = 0; i < attrs.getLength(); i++) 
    {
      Node attr = attrs.item(i);
      attr.setNodeValue(findAndReplaceVarsInText(attr.getNodeValue()));
    }
  }
  
  /** Pobranie wyników w postaci tekstowej */
  public String getResults() throws Exception
  {
    return XmlUtils.toString(xml);
  }
  
  /** Ustawienie xmla, który będzie główną gałęzią root przekształceń */
  public void setXml(Element item)
  {
    xml = item;
  }
  
  /** Ustawienie listy zapytań */
  public void setQueries(NodeList queriesList)
  {
    queries = QueryList.fromNodeList(queriesList); 
  }

}