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

Full documentation of the goal is available at http://lenniboy.github.com/jspc-maven-plugin/compile-mojo.html
