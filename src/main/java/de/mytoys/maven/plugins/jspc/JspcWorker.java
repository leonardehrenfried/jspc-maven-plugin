package de.mytoys.maven.plugins.jspc;

import java.util.Iterator;
import org.apache.jasper.JasperException;
import org.apache.jasper.JspC;
import org.apache.maven.plugin.logging.Log;

public class JspcWorker extends Thread {

	private JspC compiler;
	private Iterator<String> iterator;
	private Log log;
	final Object lock;

	public JspcWorker(JspC compiler, Iterator<String> iterator, Log log, Object lock) {
		this.compiler = compiler;
		this.iterator = iterator;
		this.log = log;
		this.lock = lock;
	}

	@Override
	public void run() {
		synchronized (lock) {
			try {
				
				while (iterator.hasNext()) {
					String filename = iterator.next();
					compiler.setJspFiles(filename);
					log.info("[Thread "+getName()+"] Compiling " + filename);
					compiler.execute();
				}
				
				lock.notifyAll();

			} catch (JasperException ex) {
				log.error("JSP compilation error", ex);
				throw new RuntimeException("jsp", ex);
			}
		}
	}
}
