package io.leonard.maven.plugins.jspc;

import java.io.IOException;
import java.util.Map;
import org.apache.jasper.JasperException;
import org.apache.jasper.JspC;
import org.apache.jasper.compiler.JspConfig;
import org.apache.jasper.compiler.TldCache;
import org.apache.jasper.servlet.JspCServletContext;
import org.apache.tomcat.JarScanner;
import org.apache.tomcat.util.scan.StandardJarScanFilter;
import org.apache.tomcat.util.scan.StandardJarScanner;
import org.eclipse.jdt.internal.compiler.env.NameEnvironmentAnswer;

public class JspCContextAccessor extends JspC {

  private Map<String, NameEnvironmentAnswer> resourcesCache;
  private String compilerClass;

  private String tldSkip;

  private String tldScan;

  private Boolean defaultTldScan;

  public JspCContextAccessor() {
    super();
  }

  protected void initServletContext() throws JasperException, IOException {
    initServletContext(this.loader);
  }

  @Override
  protected ClassLoader initClassLoader() throws IOException {
    return super.initClassLoader();
  }

  protected JspCServletContext getContext() {
    return super.context;
  }

  protected ClassLoader getLoader() {
    return this.loader;
  }

  public Map<String, NameEnvironmentAnswer> getResourcesCache() {
    return resourcesCache;
  }

  public void setResourcesCache(Map<String, NameEnvironmentAnswer> resourcesCache) {
    this.resourcesCache = resourcesCache;
  }

  public String getcompilerClass() {
    return compilerClass;
  }

  public void setcompilerClass(String compilerClass) {
    this.compilerClass = compilerClass;
  }

  public void setTldSkip(String tldSkip) {
    this.tldSkip = tldSkip;
  }

  public void setTldScan(String tldScan) {
    this.tldScan = tldScan;
  }

  public void setDefaultTldScan(Boolean defaultTldScan) {
    this.defaultTldScan = defaultTldScan;
  }

  protected void initContext(JspCContextAccessor topJspC) {
    this.context = topJspC.context;
    scanner = topJspC.scanner;

    initTldScanner(context, getLoader());
    tldCache = (TldCache) context.getAttribute(TldCache.SERVLET_CONTEXT_ATTRIBUTE_NAME);
    rctxt = topJspC.rctxt;
    jspConfig = new JspConfig(context);
    tagPluginManager = topJspC.tagPluginManager;
  }

  @Override
  public String getCompilerClassName() {
    return getcompilerClass();
  }

  @Override
  protected void initTldScanner(JspCServletContext context, ClassLoader classLoader) {
    if (tldSkip != null || tldScan != null || defaultTldScan != null) {
      JarScanner scanner = new StandardJarScanner();
      StandardJarScanFilter filter = new StandardJarScanFilter();
      if (tldSkip != null) {
        filter.setTldSkip(tldSkip);
      }
      if (tldScan != null) {
        filter.setTldScan(tldScan);
      }
      if (defaultTldScan != null) {
        filter.setDefaultTldScan(defaultTldScan);
      }
      scanner.setJarScanFilter(filter);
      // As seen in org.apache.jasper.compiler.JarScannerFactory.getJarScanner
      context.setAttribute(JarScanner.class.getName(), scanner);
    }
    super.initTldScanner(context, classLoader);
  }
}
