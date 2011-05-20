#JSPC Maven Plugin

This plugin precompiles JSPs. It is a fork of jetty-jspc-maven-plugin configured as follows:
    
    <build>
    ....
    <plugin>
     <groupId>de.mytoys.maven.plugins</groupId>
      <artifactId>jspc-maven-plugin</artifactId>
      <version>${ENTER_VERSION_HERE}</version>
       <executions>
        <execution>
         <id>jspc</id>
         <goals>
         <goal>compile</goal>
        </goals>
        <configuration>
        </configuration>
      </execution>
     </executions>
    </plugin>
    ...
    </build>

It is a fork of jetty-jspc-maven-plugin but has the following improvements:

* Faster: on my test project I was able to cut down the compilation time by about 40%
* More descriptive error messages: Under Maven 3 this plugin shows a clear indication of what caused the error and which file it is in
* Indication of the progress of the compilation by showing which JSP is currently being compiled

The compiler used in this plugin the Apache Jasper 6.0.32.

Full documentation of the goal is available at http://lenniboy.github.com/jspc-maven-plugin/compile-mojo.html