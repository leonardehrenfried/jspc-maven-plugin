## JSPC Maven Plugin

[![Build Status](https://travis-ci.org/leonardehrenfried/jspc-maven-plugin.svg?branch=master)](https://travis-ci.org/leonardehrenfried/jspc-maven-plugin)
[![Maven Central](https://img.shields.io/maven-central/v/io.leonard.maven.plugins/jspc-maven-plugin.svg)](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22io.leonard.maven.plugins%22%20AND%20a%3A%22jspc-maven-plugin%22)
[![Dependency Status](https://www.versioneye.com/user/projects/59f845ff0fb24f1f1f38c657/badge.svg)](https://www.versioneye.com/user/projects/59f845ff0fb24f1f1f38c657)

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
* Not stop at the first error (depends plugin configuration)

The compiler used by default in this plugin is [Apache Jasper 9.0.1](http://search.maven.org/#artifactdetails%7Corg.apache.tomcat%7Ctomcat-jasper%7C9.0.1%7Cjar).

If the Jasper compiler version needs to be overloaded, the plugin must be configured as follows:

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

 <dependencies>
  	<dependency>
  		<groupId>org.apache.tomcat</groupId>
  		<artifactId>tomcat-jasper</artifactId>
  		<version>${ENTER_JASPER_VERSION_HERE}</version>
  		<exclusions>
  			<exclusion>
  				<groupId>org.eclipse.jdt.core.compiler</groupId>
  				<artifactId>ecj</artifactId>
  			</exclusion>
  		</exclusions>
  	</dependency>

  	<dependency>
  		<groupId>org.eclipse.jdt.core.compiler</groupId>
  		<artifactId>ecj</artifactId>
  		<version>4.6.1</version>
  	</dependency>
  </dependencies>
</plugin>
...
</build>
```

Full documentation of the goal is available at http://leonardehrenfried.github.com/jspc-maven-plugin/compile-mojo.html

## Compatibility Matrix

* 3.X version of jspc-maven-plugin : needs Java >= 1.8, Jasper only 9.X
* 2.X version of jspc-maven-plugin : needs Java >= 1.7, Jasper only 8.X
* 1.X version of jspc-maven-plugin : needs Java >= 1.5, Jasper only 6.X, 7.X

## Comparison of precompiler plugins

[https://tcollignon.github.io/2016/12/04/How-to-compile-JSP-with-Tomcat-and-Maven-faster.html](https://tcollignon.github.io/2016/12/04/How-to-compile-JSP-with-Tomcat-and-Maven-faster.html)

## Release process

1. Set version in `pom.xml`
1. `mvn clean deploy`
1. `make commit-site`
