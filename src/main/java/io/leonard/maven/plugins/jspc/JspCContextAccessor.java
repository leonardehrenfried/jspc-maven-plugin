package io.leonard.maven.plugins.jspc;

import java.io.IOException;
import java.util.Map;

import org.apache.jasper.*;
import org.apache.jasper.compiler.*;
import org.apache.jasper.servlet.JspCServletContext;
import org.eclipse.jdt.internal.compiler.env.NameEnvironmentAnswer;

public class JspCContextAccessor extends JspC {

  private Map<String, NameEnvironmentAnswer> resourcesCache;
  private String compilerClass;

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
}
