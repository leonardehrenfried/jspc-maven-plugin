# How to use

Add the plugin to the build as shown below:

```xml
<build>
...
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

## Overriding Jasper

To override the Jasper plugin used to compile the JSP files configure the plugin as shown below:

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
        <configuration />
      </execution>
    </executions>
    <dependencies>
      <dependency>
        <groupId>org.apache.tomcat</groupId>
        <artifactId>tomcat-jasper</artifactId>
        <version>${ENTER_JASPER_VERSION_HERE}</version>
      </dependency>
    </dependencies>
  </plugin>
  ...
</build>
```

## Overriding ECJ

To override the Eclipse compiler for Java used to compile the Java files that Jasper generates configure the plugin as shown below.
Newer versions of ECJ typically support newer JVMs and bytecode versions.

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
        <configuration />
      </execution>
    </executions>
    <dependencies>
      <dependency>
        <groupId>org.apache.tomcat</groupId>
        <artifactId>tomcat-jasper</artifactId>
        <version>${ENTER_JASPER_VERSION_HERE}</version>
        <exclusions>
          <exclusion>
            <groupId>org.eclipse.jdt</groupId>
            <artifactId>ecj</artifactId>
          </exclusion>
        </exclusions>
      </dependency>
      <dependency>
        <groupId>org.eclipse.jdt</groupId>
        <artifactId>ecj</artifactId>
        <version>3.36.0</version>
      </dependency>
    </dependencies>
  </plugin>
  ...
</build>
```
