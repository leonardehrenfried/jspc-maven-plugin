## JSPC Maven Plugin

[![Build Status](https://travis-ci.org/leonardehrenfried/jspc-maven-plugin.svg?branch=master)](https://travis-ci.org/leonardehrenfried/jspc-maven-plugin)
[![Maven Central](https://img.shields.io/maven-central/v/io.leonard.maven.plugins/jspc-maven-plugin.svg)](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22io.leonard.maven.plugins%22%20AND%20a%3A%22jspc-maven-plugin%22)

This plugin precompiles JSPs. It is a fork of jetty-jspc-maven-plugin and configured as follows:

```xml
<build>
....
<plugin>
 <groupId>io.leonard.maven.plugins</groupId>
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
```

It has the following improvements compared to jetty-jspc-maven-plugin:

* Faster: can be configured to run multi-threaded. For a speed comparison read the blog post below.
* More descriptive error messages: Under Maven 3 this plugin shows a clear indication of what caused the error and which file it is in
* Indication of the progress of the compilation by showing which JSP is currently being compiled

The compiler used in this plugin is [Apache Jasper 8.5.8](http://search.maven.org/#artifactdetails%7Corg.apache.tomcat%7Ctomcat-jasper%7C8.5.8%7Cjar).

Full documentation of the goal is available at http://leonardehrenfried.github.com/jspc-maven-plugin/compile-mojo.html

## Comparison of precompiler plugins

[https://tcollignon.github.io/2016/12/04/How-to-compile-JSP-with-Tomcat-and-Maven-faster.html](https://tcollignon.github.io/2016/12/04/How-to-compile-JSP-with-Tomcat-and-Maven-faster.html)

## Use another JSP compiler

By default this plugin use org.apache.jasper.compiler.JDTCompiler (see compilerClass goal option). But if the number of threads is over 2, 
this compiler has too much synchronization overheas and hence is no very not very performant. 

To improve this is possible to user another compilerClass `org.apache.jasper.compiler.ParallelJDTCompiler`, which reduces synchronization.

## Release process

1. `make release`
1. `make commit-site`
