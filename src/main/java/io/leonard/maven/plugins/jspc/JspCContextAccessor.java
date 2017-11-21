package io.leonard.maven.plugins.jspc;

import java.io.IOException;

import org.apache.jasper.JasperException;
import org.apache.jasper.JspC;
import org.apache.jasper.compiler.JspConfig;
import org.apache.jasper.compiler.TldCache;
import org.apache.jasper.servlet.JspCServletContext;

public class JspCContextAccessor extends JspC {

	public JspCContextAccessor() {
		super();
	}
	protected void initServletContext() throws JasperException, IOException {
		initServletContext(this.loader);
	}
	
	@Override
	protected void initServletContext(ClassLoader classLoader)
			throws IOException, JasperException {
		super.initServletContext(classLoader);
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
	
	protected void initContext(JspCContextAccessor topJspC) throws IOException, JasperException {
		this.context = topJspC.context;
		

		scanner = topJspC.scanner;
        initTldScanner(context, getLoader());

       
        tldCache = (TldCache) context.getAttribute(TldCache.SERVLET_CONTEXT_ATTRIBUTE_NAME);
        rctxt = topJspC.rctxt;
        jspConfig = new JspConfig(context);
        tagPluginManager = topJspC.tagPluginManager;
	}
	

}
