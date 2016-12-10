package de.mytoys.maven.plugins.jspc;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.logging.*;

import org.apache.jasper.JspC;

public class JspcWorker implements Callable<String> {

  private static final Logger logger = Logger.getLogger(JspC.class.getName());

  private JspC jspc;
  private List<String> jspFiles;
  private List<String> errors = new ArrayList<>();

  public JspcWorker(JspC jspc, List<String> jspFiles) {
    this.jspc = jspc;
    this.jspFiles = jspFiles;
    logger.addHandler(new TomcatLogHandler());
  }

  @Override
  public String call() throws Exception {
    jspc.setJspFiles(getAllCommaSeparatedJsp());
    try {
      jspc.execute();
    } catch (Exception e) {
      return e.getMessage();
    }

    if (!errors.isEmpty()) {
      return "Jspc fail with errors, see previous log";
    }
    return null;
  }

  private String getAllCommaSeparatedJsp() {
    String allJsp = "";
    for (String fileName : jspFiles) {
      allJsp += fileName + ",";
    }
    return allJsp;
  }

  /**
   * We need to know when Jspc raise SEVERE error when we set
   * "failOnError=false"
   */
  class TomcatLogHandler extends Handler {

    @Override
    public void publish(LogRecord record) {
      if (Level.SEVERE.equals(record.getLevel())) {
        errors.add(record.getMessage());
      }
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() throws SecurityException {
    }
  }
}
