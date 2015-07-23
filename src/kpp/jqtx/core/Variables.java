package kpp.jqtx.core;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Variables 
{
  final static HashMap<String, Object> data = new HashMap<String, Object>();
  
  public static void put(String name, Object value)
  {
    data.put(name, value);
  }
  
  public static Object get(String name)
  {
    return data.get(name);
  }
  
  public static Set<String> list()
  {
    return data.keySet();
  }
  
  final static Pattern findVar = Pattern.compile("\\$\\{([a-zA-Z0-9\\._]+)\\}");
  
  public static Set<String> find(String text)
  {
    Matcher mtchr = findVar.matcher(text);
    Set<String> result = new HashSet<String>();
    while (mtchr.find())
    {
      result.add(mtchr.group(1));
    }
    return result;
  }

  public static void update(ResultSet rs, String queryId) throws SQLException
  {
    ResultSetMetaData rsmd = rs.getMetaData();
    int columnCount = rsmd.getColumnCount();
    for (int i = 1; i <= columnCount; i++)
    {
      String varName = String.format("%s.%s", queryId, rsmd.getColumnLabel(i));
      Object varValue = rs.getObject(i);
      
      put(varName, varValue);
    }
  }
}
