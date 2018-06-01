package io.leonard.maven.plugins.jspc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import org.apache.jasper.*;
import org.apache.jasper.compiler.*;
import org.apache.jasper.compiler.Compiler;
import org.apache.jasper.servlet.JspCServletContext;
import org.apache.maven.plugin.logging.Log;
import org.eclipse.jdt.internal.compiler.env.NameEnvironmentAnswer;
import org.sonatype.plexus.build.incremental.BuildContext;

public class JspCContextAccessor extends JspC {

  private Map<String, NameEnvironmentAnswer> resourcesCache;
  private final BuildContext buildContext;
  private final Log log;
  private final ErrorDispatcher errorDispatcher;

  public JspCContextAccessor(BuildContext buildContext, Log log, ErrorDispatcher errorDispatcher) {
    this.buildContext = buildContext;
    this.log = log;
    this.errorDispatcher = errorDispatcher;
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

  protected void initContext(JspCContextAccessor topJspC) {
    this.context = topJspC.context;
    scanner = topJspC.scanner;

    initTldScanner(context, getLoader());
    tldCache = (TldCache) context.getAttribute(TldCache.SERVLET_CONTEXT_ATTRIBUTE_NAME);
    rctxt = topJspC.rctxt;
    jspConfig = new JspConfig(context);
    tagPluginManager = topJspC.tagPluginManager;
  }
  


  protected void processFile(String file)
      throws JasperException
  {
      if (log.isDebugEnabled()) {
          log.debug("Processing file: " + file);
      }

      ClassLoader originalClassLoader = null;

      try {
          // set up a scratch/output dir if none is provided
          if (scratchDir == null) {
              String temp = System.getProperty("java.io.tmpdir");
              if (temp == null) {
                  temp = "";
              }
              scratchDir = new File(temp).getAbsoluteFile();
          }

          String jspUri=file.replace('\\','/');
          JspCompilationContext clctxt = new JspCompilationContext
              ( jspUri, this, context, null, rctxt );

          /* Override the defaults */
          if ((targetClassName != null) && (targetClassName.length() > 0)) {
              clctxt.setServletClassName(targetClassName);
              targetClassName = null;
          }
          if (targetPackage != null) {
              clctxt.setServletPackageName(targetPackage);
          }

          originalClassLoader = Thread.currentThread().getContextClassLoader();
          Thread.currentThread().setContextClassLoader(loader);

          clctxt.setClassLoader(loader);
          clctxt.setClassPath(classPath);

          Compiler clc = new ParallelJDTCompiler(log, buildContext, errorDispatcher);
          clc.init(clctxt, null);

          // If compile is set, generate both .java and .class, if
          // .jsp file is newer than .class file;
          // Otherwise only generate .java, if .jsp file is newer than
          // the .java file
          if( clc.isOutDated(compile) ) {
              if (log.isDebugEnabled()) {
                  log.debug(jspUri + " is out dated, compiling...");
              }

              clc.compile(compile, true);
          }

          // Generate mapping
          generateWebMapping( file, clctxt );
          if ( showSuccess ) {
              log.info( "Built File: " + file );
          }

      } catch (JasperException je) {
          Throwable rootCause = je;
          while (rootCause instanceof JasperException
                  && ((JasperException) rootCause).getRootCause() != null) {
              rootCause = ((JasperException) rootCause).getRootCause();
          }
          if (rootCause != je) {
              log.error(Localizer.getMessage("jspc.error.generalException",
                                             file),
                        rootCause);
          }

          // Bugzilla 35114.
          if(getFailOnError()) {
              throw je;
          } else {
              log.error(je.getMessage());
          }

      } catch (Exception e) {
          if ((e instanceof FileNotFoundException) && log.isWarnEnabled()) {
              log.warn(Localizer.getMessage("jspc.error.fileDoesNotExist",
                                            e.getMessage()));
          }
          throw new JasperException(e);
      } finally {
          if(originalClassLoader != null) {
              Thread.currentThread().setContextClassLoader(originalClassLoader);
          }
      }
  }
}
