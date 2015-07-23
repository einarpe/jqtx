package kpp.jqtx.core;

import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class QueryList extends ArrayList<QueryList.Query>
{
  private static final long serialVersionUID = 7422823311421917376L;
  
  private static final HashMap<String, QueryList.Query> idsToQueries = new HashMap<>();
  
  public static QueryList fromNodeList(NodeList list)
  {
    QueryList result = new QueryList();
    for (int i = 0; i < list.getLength(); i++)
    {
      Element item = (Element)list.item(i);
      
      QueryList.Query qry = new Query(); 
      qry.Value = item.getTextContent();
      qry.Id = item.getAttribute("id");
      
      result.add(qry);
      idsToQueries.put(qry.Id, qry);
    }
    return result;
  }
  
  public static Query getById(String id)
  {
    return idsToQueries.get(id);
  }
  
  public static class Query
  {
    public String Value;
    
    public String Id;
    
    public String toString()
    {
      return String.format("%s(%s)", Id, Value);
    }
  }
}
