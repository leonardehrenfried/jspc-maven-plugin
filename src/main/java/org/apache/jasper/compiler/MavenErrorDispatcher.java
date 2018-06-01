package org.apache.jasper.compiler;

import java.io.File;

import org.apache.jasper.JasperException;
import org.apache.jasper.compiler.ErrorDispatcher;
import org.apache.jasper.compiler.JavacErrorDetail;
import org.apache.jasper.compiler.Mark;
import org.apache.jasper.compiler.Node;
import org.apache.maven.plugin.logging.Log;
import org.sonatype.plexus.build.incremental.BuildContext;

public class MavenErrorDispatcher extends ErrorDispatcher {

	private final BuildContext buildContext;
	private final Log log;
	
	private boolean errorOccurred;
	
	public MavenErrorDispatcher(BuildContext buildContext, Log log) {
		super(true);
		this.buildContext = buildContext;
		this.log = log;
	}


    /**
     * Dispatches the given JSP parse error to the configured error handler.
     *
     * The given error code is localized. If it is not found in the
     * resource bundle for localized error messages, it is used as the error
     * message.
     *
     * @param errCode Error code
     * @param args Arguments for parametric replacement
     * @throws JasperException An error occurred
     */
    public void jspError(String errCode, String... args) throws JasperException {
        dispatch(null, errCode, args, null);
    }

    /**
     * Dispatches the given JSP parse error to the configured error handler.
     *
     * The given error code is localized. If it is not found in the
     * resource bundle for localized error messages, it is used as the error
     * message.
     *
     * @param where Error location
     * @param errCode Error code
     * @param args Arguments for parametric replacement
     * @throws JasperException An error occurred
     */
    public void jspError(Mark where, String errCode, String... args)
            throws JasperException {
        dispatch(where, errCode, args, null);
    }

    /**
     * Dispatches the given JSP parse error to the configured error handler.
     *
     * The given error code is localized. If it is not found in the
     * resource bundle for localized error messages, it is used as the error
     * message.
     *
     * @param n Node that caused the error
     * @param errCode Error code
     * @param args Arguments for parametric replacement
     * @throws JasperException An error occurred
     */
    public void jspError(Node n, String errCode, String... args)
            throws JasperException {
        dispatch(n.getStart(), errCode, args, null);
    }

    /**
     * Dispatches the given parsing exception to the configured error handler.
     *
     * @param e Parsing exception
     * @throws JasperException An error occurred
     */
    public void jspError(Exception e) throws JasperException {
        dispatch(null, null, null, e);
    }

    /**
     * Dispatches the given JSP parse error to the configured error handler.
     *
     * The given error code is localized. If it is not found in the
     * resource bundle for localized error messages, it is used as the error
     * message.
     *
     * @param errCode Error code
     * @param args Arguments for parametric replacement
     * @param e Parsing exception
     * @throws JasperException An error occurred
     */
    public void jspError(Exception e, String errCode, String... args)
                throws JasperException {
        dispatch(null, errCode, args, e);
    }

    /**
     * Dispatches the given JSP parse error to the configured error handler.
     *
     * The given error code is localized. If it is not found in the
     * resource bundle for localized error messages, it is used as the error
     * message.
     *
     * @param where Error location
     * @param e Parsing exception
     * @param errCode Error code
     * @param args Arguments for parametric replacement
     * @throws JasperException An error occurred
     */
    public void jspError(Mark where, Exception e, String errCode, String... args)
                throws JasperException {
        dispatch(where, errCode, args, e);
    }

    /**
     * Dispatches the given JSP parse error to the configured error handler.
     *
     * The given error code is localized. If it is not found in the
     * resource bundle for localized error messages, it is used as the error
     * message.
     *
     * @param n Node that caused the error
     * @param e Parsing exception
     * @param errCode Error code
     * @param args Arguments for parametric replacement
     * @throws JasperException An error occurred
     */
    public void jspError(Node n, Exception e, String errCode, String... args)
                throws JasperException {
        dispatch(n.getStart(), errCode, args, e);
    }

    /**
     * Dispatches the given javac compilation errors to the configured error
     * handler.
     *
     * @param javacErrors Array of javac compilation errors
     * @throws JasperException An error occurred
     */
    public void javacError(JavacErrorDetail[] javacErrors)
            throws JasperException {
    	errorOccurred = true;
		for (JavacErrorDetail jed : javacErrors) {
			buildContext.addMessage(new File(jed.getJspFileName()), jed.getJspBeginLineNumber(), 0,
					jed.getErrorMessage(), BuildContext.SEVERITY_ERROR, null);
		}
    }


    /**
     * Dispatches the given compilation error report and exception to the
     * configured error handler.
     *
     * @param errorReport Compilation error report
     * @param e Compilation exception
     * @throws JasperException An error occurred
     */
    public void javacError(String errorReport, Exception e)
                throws JasperException {
    	errorOccurred = true;
    	log.error(errorReport, e);
    }
    /**
     * Dispatches the given JSP parse error to the configured error handler.
     *
     * The given error code is localized. If it is not found in the
     * resource bundle for localized error messages, it is used as the error
     * message.
     *
     * @param where Error location
     * @param errCode Error code
     * @param args Arguments for parametric replacement
     * @param e Parsing exception
     * @throws JasperException An error occurred
     */
    private void dispatch(Mark where, String errCode, Object[] args,
                          Exception e) throws JasperException {
    	errorOccurred = true;
    	buildContext.addMessage(new File(where.getFile()), where.getLineNumber(), where.getColumnNumber(), errCode, BuildContext.SEVERITY_ERROR, e);
    }


	public boolean isErrorOccurred() {
		return errorOccurred;
	}

}
