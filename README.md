Demo web application to wrap a RESTful API around
[citeproc-java](https://github.com/michel-kraemer/citeproc-java).


# Running in Jetty

From the root directory of the project, run

```
mvn clean compile jetty:run -Djetty.port=9999
```

Then, point your browser to [http://localhost:9999](http://localhost:9999).

Running under Jetty is enabled by the following code in the *pom.xml* file:

```xml
...
<properties>
  ...
  <jettyVersion>9.1.2.v20140210</jettyVersion>
</properties>
...
<dependencies>
  ...
  <dependency>
    <groupId>org.eclipse.jetty.orbit</groupId>
    <artifactId>javax.servlet</artifactId>
    <version>3.0.0.v201112011016</version>
    <scope>provided</scope>
  </dependency>
</dependencies>
...
<build>
  <plugins>
    ...
    <plugin>
      <groupId>org.eclipse.jetty</groupId>
      <artifactId>jetty-maven-plugin</artifactId>
      <version>${jettyVersion}</version>
    </plugin>
  </plugins>
</build>
```

Note that by default, it uses test/mockup sources for the data.  To configure this for running
under Jetty in production, first create an XML configuration file that will override the web.xml.
Put this in the root directory of the app, named jetty-override-web.xml.  Use the following as a
template, and change the parameter values as needed.

```xml
<web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xmlns="http://java.sun.com/xml/ns/javaee"
         xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd"
         metadata-complete="false"
         version="3.0">

  <context-param>
    <description>URL of the backend service that provides item data</description>
    <param-name>backend_url</param-name>
    <param-value>test</param-value>
  </context-param>
</web-app>
```

Then, run Jetty with the `-DjettyOverrideXml=jetty-override-web.xml` command-line switch.

# Configuration parameters (system properties)

Set these on the run command line, for example:

```
mvn jetty:run -Djetty.port=11999 -Dcache_aiids=true -Daiid_cache_ttl=8
```

* `jetty.port`
* `cache_aiids` - either "true" or "false".  Defaults to "false".
* `aiid_cache_ttl` - time-to-live for each of the IDs in the ID cache, in seconds.

# Developing in Eclipse

To run the application from Eclipse, right-click on the project, and select
*Run As* -> *Run on server*.  Depending on your workspace server configuration
*server.xml*, you should then be able to point your browser to
http://locahost:8080/pmc-citation-service/.

# Backend address

This service accesses a backend to get its citation data from, and the URL of that
backend should be given in a context variable called `backend_url`.  The default value
is "test", and is given in the web deployment descriptor *web.xml*, as follows:

```xml
<context-param>
  <description>URL of the backend service that provides item data</description>
  <param-name>backend_url</param-name>
 <param-value>test</param-value>
</context-param>
```

When the value is `test`, the citation data is mock data derived from a hard-coded JSON
string.

In a production deployment, you should override this context variable with the real URL
of the backend.  For example, in Tomcat, you can specify it in the *server.xml* file as
follows.  The "override='false'" attribute ensures that this value overrides the one given
in the web deployment descriptor.

```xml
<Context docBase="pmc-citation-service"
         path="/pmc-citation-service"
         reloadable="true"
         source="org.eclipse.jst.jee.server:pmc-citation-service">
  <Parameter name='backend_url'
             value='http://domain.com/backend/url'
             override='false'/>
</Context>
```

# API

## Parameters:

* **ids** - the types and expected patterns of the values given here are the same as for
  the [PMC ID converter API](https://www.ncbi.nlm.nih.gov/pmc/tools/id-converter-api/).
  The type can either be specified explicitly with the idtype parameter, or can be inferred.
  IDs are always resolved to one of `aiid` or `pmid`.
* **idtype** - specifies the type of the IDs given in the ids parameter.
  Any of these types is allowed:
    * aiid
    * pmcid - includes versioned ids
    * pmid
    * mid
    * doi


# Non-maven dependencies

This depends on [kitty-cache](https://code.google.com/p/kitty-cache/), which is not on
Maven Central.  To build it and install it to your local maven repository,
from any temporary directory:

```
svn checkout http://kitty-cache.googlecode.com/svn/trunk/ kitty-cache-read-only
cd kitty-cache-read-only
mvn install
```

# References / see also

**Java**

Using Java 1.6.

* Platform / library [Javadocs](http://docs.oracle.com/javase/6/docs/api/)

**Saxon**

For XSLT tranformations

* [Javadocs](http://www.saxonica.com/documentation/Javadoc/index.html)

**citeproc-java**

* [Javadocs](http://michel-kraemer.github.io/citeproc-java/api/latest/)

**citeproc-js**

* [Manual](http://gsl-nagoya-u.net/http/pub/citeproc-doc.html)

**PMC ID Converter API**

* [Documentation](https://www.ncbi.nlm.nih.gov/pmc/tools/id-converter-api/)

**Jackson**

We're using the Jackson library to read JSON objects:

* [Home page](https://github.com/FasterXML/jackson-databind)
* [Javadocs](http://fasterxml.github.io/jackson-databind/javadoc/2.3.0/)



