This is a citation exporting web service, based on the following excellent open-source
tools:

* [citeproc-java](https://github.com/michel-kraemer/citeproc-java).
* [citeproc-js](http://gsl-nagoya-u.net/http/pub/citeproc-doc.html)


## Running

Clone the repository, and cd to it

```
git clone https://github.com/Klortho/citeproc-java-demo.git
cd citeproc-java-demo
```

This depends on one library, [kitty-cache](https://code.google.com/p/kitty-cache/),
that is not in Maven central, so you'll need to build
and install that first:

```
svn checkout http://kitty-cache.googlecode.com/svn/trunk/ kitty-cache-read-only
cd kitty-cache-read-only
mvn install
cd ..
```

Then build and run this web service:

```
mvn jetty:run -Djetty.port=9999
```

Point your browser to [http://localhost:9999](http://localhost:9999).


## Configuration parameters

Configuration is controlled with system properties.
Set these on the run command line, for example:

```
mvn jetty:run -Djetty.port=9999 -Dcache_aiids=true -Daiid_cache_ttl=8
```

Here are the parameters that are defined:

* `jetty.port`
* `item_provider` - string specifying which item provider to use.  Default is "test".
* `id_converter_url` - URL of the PMC ID converter API.  Default is
  "http://web.pubmedcentral.nih.gov/utils/idconv/v1.1/".
* `id_converter_params` - Query string parameters to send to the the PMC ID
  converter API.  Default is "showaiid=yes&format=json".
* `cache_aiids` - either "true" or "false".  Default is "false".
* `aiid_cache_ttl` - time-to-live for each of the IDs in the ID cache, in seconds.
  Default is 86400.


## API

### Parameters:

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


## Development info

### Test item provider

When the value of *item_provider" is "test", the citation data is mock data from the
*src/main/webapp/test* directory.

### Eclipse / Tomcat configuration

To run the application from Eclipse, right-click on the project, and select
*Run As* -> *Run on server*.  Depending on your workspace server configuration
*server.xml*, you should then be able to point your browser to
http://locahost:12006/pmc-citation-service/.

### Jetty configuration

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

### Dependencies

Dependencies are declared in the *pom.xml* file, and most are resolved automatically by Maven.

One library, however, [kitty-cache](https://code.google.com/p/kitty-cache/), is not in
Maven Central. It is declared in the *pom.xml*, but needs to be built and installed to your
local maven repository.  For example:

```
svn checkout http://kitty-cache.googlecode.com/svn/trunk/ kitty-cache-read-only
cd kitty-cache-read-only
mvn install
```

### References / see also

Here are links to sources of info useful when doing development.

**Java**

This app is currently configured to use Java 1.6.

* Platform / library [Javadocs](http://docs.oracle.com/javase/6/docs/api/)

**Saxon**

It uses Saxon for XSLT tranformations.

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


