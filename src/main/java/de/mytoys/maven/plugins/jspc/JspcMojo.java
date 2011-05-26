//========================================================================
//$Id: JspcMojo.java 6430 2011-03-15 17:52:17Z joakime $
//Copyright 2006 Mort Bay Consulting Pty. Ltd.
//------------------------------------------------------------------------
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at 
//http://www.apache.org/licenses/LICENSE-2.0
//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.
//========================================================================

package de.mytoys.maven.plugins.jspc;

import org.apache.jasper.JspC;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.eclipse.jetty.util.IO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.codehaus.plexus.util.DirectoryScanner;
import org.codehaus.plexus.util.StringUtils;

/**
 * <p>
 * This goal will compile jsps for a webapp so that they can be included in a
 * war.
 * </p>
 * <p>
 * It is a fork of <a href="http://docs.codehaus.org/display/JETTY/Maven+Jetty+Jspc+Plugin">jetty-jspc-maven-plugin</a>
 * but has the following improvements:
 * </p>
 * <ul>
 *  <li>Faster: on my test project I was able to cut down the compilation 
 *      time by about 40%
 *  </li>
 *  <li>Indication of the progress of the compilation by showing which JSP
 *      is currently being compiled
 *  </li>
 * </ul>
 * <p>
 * The compiler used in this plugin the Apache Jasper 6.0.32.
 * </p>
 * 
 * @author janb
 * @author <a href="mailto:leonard.ehrenfrie@web.de">Leonard Ehrenfried</a>
 * 
 * @goal compile
 * @phase process-classes
 * @requiresDependencyResolution compile
 * @description Runs jspc compiler to produce .java and .class files
 */
public class JspcMojo extends AbstractMojo
{
    public static final String END_OF_WEBAPP = "</web-app>";


    /**
     * The maven project.
     * 
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    /**
     * File into which to generate the &lt;servlet&gt; and
     * &lt;servlet-mapping&gt; tags for the compiled jsps
     * 
     * @parameter default-value="${basedir}/target/webfrag.xml"
     */
    private String webXmlFragment;

    /**
     * Optional. A marker string in the src web.xml file which indicates where
     * to merge in the generated web.xml fragment. Note that the marker string
     * will NOT be preserved during the insertion. Can be left blank, in which
     * case the generated fragment is inserted just before the &lt;/web-app&gt;
     * line
     * 
     * @parameter
     */
    private String insertionMarker;

    /**
     * Merge the generated fragment file with the web.xml from
     * webAppSourceDirectory. The merged file will go into the same directory as
     * the webXmlFragment.
     * 
     * @parameter default-value="true"
     */
    private boolean mergeFragment;

    /**
     * The destination directory into which to put the compiled jsps.
     * 
     * @parameter default-value="${project.build.outputDirectory}"
     */
    private String generatedClasses;

    /**
     * Controls whether or not .java files generated during compilation will be
     * preserved.
     * 
     * @parameter default-value="false"
     */
    private boolean keepSources;

    /**
     * Default root package for all generated classes
     * 
     * @parameter default-value="jsp"
     */
    private String packageRoot;

    /**
     * Root directory for all html/jsp etc files
     * 
     * @parameter default-value="${basedir}/src/main/webapp"
     * 
     */
    private String webAppSourceDirectory;
    
   
    
    /**
     * Location of web.xml. Defaults to src/main/webapp/web.xml.
     * @parameter default-value="${basedir}/src/main/webapp/WEB-INF/web.xml"
     */
    private String webXml;


    /**
     * The comma separated list of patterns for file extensions to be processed. By default
     * will include all .jsp and .jspx files.
     * 
     * @parameter default-value="**\/*.jsp, **\/*.jspx,  **\/*.jspf"
     */
    private String[] includes;

    /**
     * The comma separated list of file name patters to exclude from compilation.
     * 
     * @parameter default_value="**\/.svn\/**";
     */
    private String[] excludes;

    /**
     * The location of the compiled classes for the webapp
     * 
     * @parameter expression="${project.build.outputDirectory}"
     */
    private File classesDirectory;

    /**
     * Whether or not to output more verbose messages during compilation.
     * 
     * @parameter default-value="false";
     */
    private boolean verbose;

    /**
     * If true, validates tlds when parsing.
     * 
     * @parameter default-value="false";
     */
    private boolean validateXml;

    /**
     * The encoding scheme to use.
     * 
     * @parameter default-value="UTF-8"
     */
    private String javaEncoding;

    /**
     * Whether or not to generate JSR45 compliant debug info
     * 
     * @parameter default-value="true";
     */
    private boolean suppressSmap;

    /**
     * Whether or not to ignore precompilation errors caused by jsp fragments.
     * 
     * @parameter default-value="false"
     */
    private boolean ignoreJspFragmentErrors;

    /**
     * Allows a prefix to be appended to the standard schema locations so that
     * they can be loaded from elsewhere.
     * 
     * @parameter
     */
    private String schemaResourcePrefix;
    

    public void execute() throws MojoExecutionException, MojoFailureException
    {
        if (getLog().isDebugEnabled())
        {
            getLog().info("verbose=" + verbose);
            getLog().info("webAppSourceDirectory=" + webAppSourceDirectory);
            getLog().info("generatedClasses=" + generatedClasses);
            getLog().info("webXmlFragment=" + webXmlFragment);
            getLog().info("webXml="+webXml);
            getLog().info("validateXml=" + validateXml);
            getLog().info("packageRoot=" + packageRoot);
            getLog().info("javaEncoding=" + javaEncoding);
            getLog().info("insertionMarker="+ (insertionMarker == null || insertionMarker.equals("") ? END_OF_WEBAPP : insertionMarker));
            getLog().info("keepSources=" + keepSources);
            getLog().info("mergeFragment=" + mergeFragment);
            getLog().info("suppressSmap=" + suppressSmap);
            getLog().info("ignoreJspFragmentErrors=" + ignoreJspFragmentErrors);
            getLog().info("schemaResourcePrefix=" + schemaResourcePrefix);
        }
        try{
            long start=System.currentTimeMillis();
						
						prepare();
            compile();
            cleanupSrcs();
            mergeWebXml();
            
						long finish=System.currentTimeMillis();
						long millis = finish - start;
						String time = String.format("%d min, %d sec",
									TimeUnit.MILLISECONDS.toMinutes(millis),
									TimeUnit.MILLISECONDS.toSeconds(millis)
									- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
						
						getLog().info("Compilation completed in "+time);
        }
        catch (Exception e)
        {
            throw new MojoExecutionException("Failure processing jsps", e);
        }
    }

    public void compile() throws Exception
    {
        ClassLoader currentClassLoader = Thread.currentThread()
        .getContextClassLoader();

        ArrayList urls = new ArrayList();
        setUpClassPath(urls);
        URLClassLoader ucl = new URLClassLoader((URL[]) urls.toArray(new URL[0]), currentClassLoader);
        StringBuffer classpathStr = new StringBuffer();

        for (int i = 0; i < urls.size(); i++)
        {
            if (getLog().isDebugEnabled())
                getLog().debug("webappclassloader contains: " + urls.get(i));
            classpathStr.append(((URL) urls.get(i)).getFile());
            if (getLog().isDebugEnabled())
                getLog().debug(
                        "added to classpath: " + ((URL) urls.get(i)).getFile());
            classpathStr.append(System.getProperty("path.separator"));
        }

        Thread.currentThread().setContextClassLoader(ucl);

        JspC jspc = new JspC();
        jspc.setWebXmlFragment(webXmlFragment);
        jspc.setUriroot(webAppSourceDirectory);
        jspc.setPackage(packageRoot);
        jspc.setOutputDir(generatedClasses);
        jspc.setValidateXml(validateXml);
        jspc.setClassPath(classpathStr.toString());
        jspc.setCompile(true);
        jspc.setSmapSuppressed(suppressSmap);
        jspc.setSmapDumped(!suppressSmap);
        jspc.setJavaEncoding(javaEncoding);

        // JspC#setExtensions() does not exist, so 
        // always set concrete list of files that will be processed.
        String[] jspFiles = getJspFiles(webAppSourceDirectory);
        getLog().info("Includes="+StringUtils.join(includes, ","));
        if(excludes!=null){
					getLog().info("Excludes="+StringUtils.join(excludes, ","));
				}
				
        if (verbose)
        {
            getLog().info("Files selected to precompile: " + jspFiles);
        }
        
        if (verbose)
            jspc.setVerbose(99);
        else
            jspc.setVerbose(0);
				
				for(String fileName:jspFiles){
					jspc.setJspFiles(fileName);
					getLog().info("Compiling "+fileName);
					jspc.execute();
				}
				

        Thread.currentThread().setContextClassLoader(currentClassLoader);
    }

    private String[] getJspFiles(String webAppSourceDirectory)
    throws Exception
    {
        DirectoryScanner scanner = new DirectoryScanner();
				scanner.setBasedir(new File(webAppSourceDirectory));
        if ((excludes != null) && (excludes.length != 0)) {
            scanner.setExcludes(excludes);
        }
        scanner.addDefaultExcludes();
				scanner.setIncludes(includes);
				scanner.setCaseSensitive(false);
				scanner.scan();
				
				return scanner.getIncludedFiles();
    }

    /**
     * Until Jasper supports the option to generate the srcs in a different dir
     * than the classes, this is the best we can do.
     * 
     * @throws Exception
     */
    public void cleanupSrcs() throws Exception
    {
        // delete the .java files - depending on keepGenerated setting
        if (!keepSources)
        {
            File generatedClassesDir = new File(generatedClasses);

            if(generatedClassesDir.exists() && generatedClassesDir.isDirectory())
            {
                delete(generatedClassesDir, new FileFilter()
                {
                    public boolean accept(File f)
                    {
                        return f.isDirectory() || f.getName().endsWith(".java");
                    }                
                });
            }
        }
    }
    
    static void delete(File dir, FileFilter filter)
    {
        File[] files = dir.listFiles(filter);
        for(int i=0; i<files.length; i++)
        {
            File f = files[i];
            if(f.isDirectory())
                delete(f, filter);
            else
                f.delete();
        }
    }

    /**
     * Take the web fragment and put it inside a copy of the web.xml.
     * 
     * You can specify the insertion point by specifying the string in the
     * insertionMarker configuration entry.
     * 
     * If you dont specify the insertionMarker, then the fragment will be
     * inserted at the end of the file just before the &lt;/webapp&gt;
     * 
     * @throws Exception
     */
    public void mergeWebXml() throws Exception
    {
        if (mergeFragment)
        {
            // open the src web.xml
            File webXml = getWebXmlFile();
           
            if (!webXml.exists())
            {
                getLog().info(webXml.toString() + " does not exist, cannot merge with generated fragment");
                return;
            }

            File fragmentWebXml = new File(webXmlFragment);
            if (!fragmentWebXml.exists())
            {
                getLog().info("No fragment web.xml file generated");
            }
            File mergedWebXml = new File(fragmentWebXml.getParentFile(),
            "web.xml");
            BufferedReader webXmlReader = new BufferedReader(new FileReader(
                    webXml));
            PrintWriter mergedWebXmlWriter = new PrintWriter(new FileWriter(
                    mergedWebXml));

            // read up to the insertion marker or the </webapp> if there is no
            // marker
            boolean atInsertPoint = false;
            boolean atEOF = false;
            String marker = (insertionMarker == null
                    || insertionMarker.equals("") ? END_OF_WEBAPP : insertionMarker);
            while (!atInsertPoint && !atEOF)
            {
                String line = webXmlReader.readLine();
                if (line == null)
                    atEOF = true;
                else if (line.indexOf(marker) >= 0)
                {
                    atInsertPoint = true;
                }
                else
                {
                    mergedWebXmlWriter.println(line);
                }
            }

            // put in the generated fragment
            BufferedReader fragmentWebXmlReader = new BufferedReader(
                    new FileReader(fragmentWebXml));
            IO.copy(fragmentWebXmlReader, mergedWebXmlWriter);

            // if we inserted just before the </web-app>, put it back in
            if (marker.equals(END_OF_WEBAPP))
                mergedWebXmlWriter.println(END_OF_WEBAPP);

            // copy in the rest of the original web.xml file
            IO.copy(webXmlReader, mergedWebXmlWriter);

            webXmlReader.close();
            mergedWebXmlWriter.close();
            fragmentWebXmlReader.close();
        }
    }

    private void prepare() throws Exception
    {
        // For some reason JspC doesn't like it if the dir doesn't
        // already exist and refuses to create the web.xml fragment
        File generatedSourceDirectoryFile = new File(generatedClasses);
        if (!generatedSourceDirectoryFile.exists())
				{
            generatedSourceDirectoryFile.mkdirs();	
				}
    }

    /**
     * Set up the execution classpath for Jasper.
     * 
     * Put everything in the classesDirectory and all of the dependencies on the
     * classpath.
     * 
     * @param urls a list to which to add the urls of the dependencies
     * @throws Exception
     */
    private void setUpClassPath(List urls) throws Exception
    {
        String classesDir = classesDirectory.getCanonicalPath();
        classesDir = classesDir
        + (classesDir.endsWith(File.pathSeparator) ? "" : File.separator);
        urls.add(new File(classesDir).toURL());

        if (getLog().isDebugEnabled())
            getLog().debug("Adding to classpath classes dir: " + classesDir);

        for (Iterator iter = project.getArtifacts().iterator(); iter.hasNext();)
        {
            Artifact artifact = (Artifact) iter.next();

            // Include runtime and compile time libraries
            if (!Artifact.SCOPE_TEST.equals(artifact.getScope()))
            {
                String filePath = artifact.getFile().getCanonicalPath();
                if (getLog().isDebugEnabled())
                    getLog().debug(
                            "Adding to classpath dependency file: " + filePath);

                urls.add(artifact.getFile().toURL());
            }
        }
    }
    
    private File getWebXmlFile ()
    throws IOException
    {
        File file = null;
        File baseDir = project.getBasedir().getCanonicalFile();
        File defaultWebAppSrcDir = new File (baseDir, "src/main/webapp").getCanonicalFile();
        File webAppSrcDir = new File (webAppSourceDirectory).getCanonicalFile();
        File defaultWebXml = new File (defaultWebAppSrcDir, "web.xml").getCanonicalFile();
        
        //If the web.xml has been changed from the default, try that
        File webXmlFile = new File (webXml).getCanonicalFile();
        if (webXmlFile.compareTo(defaultWebXml) != 0)
        {
            file = new File (webXml);
            return file;
        }
        
        //If the web app src directory has not been changed from the default, use whatever
        //is set for the web.xml location
        file = new File (webAppSrcDir, "web.xml");
        return file;
    }
}
