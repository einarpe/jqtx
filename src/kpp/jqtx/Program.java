package kpp.jqtx;

import kpp.jqtx.core.JQTX;

public final class Program
{

  public static final String NAMESPACE = "http://example.com";

  public static void main(String[] args)
  {
    if (args.length != 1)
    {
      printHelp();
      return;
    }
    
    try
    {
      JQTX.createInstance(args[0]).output(System.out);
    }
    catch (Throwable ex)
    {
      System.err.println(ex.getMessage());
    }
    
  }

  private static void printHelp()
  {
    System.out.println("Usage:");
    System.out.println("java -jar jqtx.jar query.xml");
    System.out.println("config.properties file is used for connecting to database.");
  }

}
