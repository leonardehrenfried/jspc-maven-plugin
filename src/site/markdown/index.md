# jspc-maven-plugin

This plugin precompiles JSPs.

It has the following improvements compared to jetty-jspc-maven-plugin:

* Faster: can be configured to run multi-threaded. For a speed comparison read the blog post below.
* More descriptive error messages: Under Maven 3 this plugin shows a clear indication of what caused the error and which file it is in.
* Not stop at the first error (depends on plugin configuration).

The compiler used by default in this plugin is Apache Jasper 10, but it can be swapped.

## Compatibility Matrix

* 4.X version of jspc-maven-plugin : needs Java >= 11, Jasper only 10.X
* 3.X version of jspc-maven-plugin : needs Java >= 1.8, Jasper only 9.X
* 2.X version of jspc-maven-plugin : needs Java >= 1.7, Jasper only 8.X
* 1.X version of jspc-maven-plugin : needs Java >= 1.5, Jasper only 6.X, 7.X

## Comparison of precompiler plugins

[https://tcollignon.github.io/2016/12/04/How-to-compile-JSP-with-Tomcat-and-Maven-faster.html](https://tcollignon.github.io/2016/12/04/How-to-compile-JSP-with-Tomcat-and-Maven-faster.html)
