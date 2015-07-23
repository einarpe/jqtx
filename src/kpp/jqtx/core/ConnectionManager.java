package kpp.jqtx.core;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager
{
  private static Connection conn;
  private static String url;
  private static String driver;
  private static String user;
  private static String password;
  private static Properties props;
  
  public static void connect() throws Exception
  {
    url = props.getProperty("URL");
    driver = props.getProperty("DRIVER");
    user = props.getProperty("USER");
    password = props.getProperty("PASSWORD");
    
    
    Class.forName(driver);
    conn = DriverManager.getConnection(url, user, password);
  }
  
  public static Connection get() throws SQLException
  {
    if (conn == null || conn.isClosed())
      conn = DriverManager.getConnection(url, user, password);
    
    return conn;
  }

  public static void load() throws Exception
  {
    props = new Properties();
    props.load(new FileInputStream(new File("config.properties")));
    connect();
  }

}
