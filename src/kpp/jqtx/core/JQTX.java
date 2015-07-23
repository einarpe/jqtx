package kpp.jqtx.core;

import java.io.File;
import java.io.PrintStream;
import java.sql.SQLException;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class JQTX
{
  private Executer executer;
  
  private Document document;
  
  public static JQTX createInstance(String xmlDefFilePath) throws Exception
  {
    JQTX result = new JQTX();
    result.loadConnectionData();
    result.loadXmlDefinition(xmlDefFilePath);
    result.transform();
    return result;
  }

  private void loadConnectionData() throws Exception
  {
    ConnectionManager.load();
  }

  private void loadXmlDefinition(String xmlDefFilePath) throws Exception
  {
    document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(xmlDefFilePath));
    executer = new Executer();
    loadConfig();
  }
  
  private void loadConfig() throws Exception
  {
    loadQueries();
    loadXml();
  }

  private void loadXml()
  {
    NodeList xmlToTransform = document.getElementsByTagName("q:xml");
    if (xmlToTransform.getLength() == 0)
      return;
    
    executer.setXml(XmlUtils.getFirstChild(xmlToTransform.item(0)));
  }

  private void loadQueries()
  {
    NodeList queries = document.getElementsByTagName("q:query");
    executer.setQueries(queries);
  }

  private void transform() throws SQLException
  {
    if (executer == null)
      return;
    
    executer.execute();
  }

  public void output(PrintStream outStream) throws Exception
  {
    if (outStream == null || executer == null)
      return;
    
    outStream.print(executer.getResults());
  }
}
