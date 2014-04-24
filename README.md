This is a citation exporting web service, based on the following excellent open-source
tools:

* [citeproc-java](https://github.com/michel-kraemer/citeproc-java).
* [citeproc-js](http://gsl-nagoya-u.net/http/pub/citeproc-doc.html)


## Quick start

Clone the repository, and cd to it

```
git clone https://github.com/Klortho/citeproc-java-demo.git
cd citeproc-java-demo
```

This depends on a library, [kitty-cache](https://code.google.com/p/kitty-cache/),
that is not in Maven central, so you'll need to build
and install that first:

```
svn checkout http://kitty-cache.googlecode.com/svn/trunk/ kitty-cache-read-only
cd kitty-cache-read-only
mvn install
cd ..
```

The service also depends on one library XSLT that is pulled from the
[DtdAnalyzer](https://github.com/ncbi/DtdAnalyzer).  To get it:

```
wget https://github.com/ncbi/DtdAnalyzer/raw/master/xslt/xml2json-2.0.xsl \
    -O src/main/webapp/xslt/xml2json-2.0.xsl
```

Then build and run this web service:

```
mvn jetty:run
```

Point your browser to [http://localhost:11999](http://localhost:11999).


## Running as executable jar with embedded Jetty

```
mvn package
mkdir jetty-temp-dir
java -Djava.io.tmpdir=./jetty-temp-dir \
  -jar target/pmc-citation-exporter-0.1-SNAPSHOT.jar
```




## Configuration parameters

Configuration is controlled with system properties.
Set these on the run command line, for example:

```
mvn jetty:run -Djetty.port=9876 -Dcache_aiids=true -Daiid_cache_ttl=8
```

Here are the parameters that are defined:

* `jetty.port`
* `item_source` - string specifying which ItemSource to use.  Default is "test".
  Other values are "stcache".
* `id_converter_url` - URL of the PMC ID converter API.  Default is
  "http://web.pubmedcentral.nih.gov/utils/idconv/v1.1/".
* `id_converter_params` - Query string parameters to send to the the PMC ID
  converter API.  Default is "showaiid=yes&format=json".
* `cache_aiids` - either "true" or "false".  Default is "false".
* `aiid_cache_ttl` - time-to-live for each of the IDs in the ID cache, in seconds.
  Default is 86400.
* `json_from_pmfu` - when "true", the citeproc-json format is retrieved by transforming
  the PMFU XML on the fly.  Default is "true".
* `stcache_pmfu_image` - location of the stcache image file for PMFU records.
* `xml.catalog.files` - used by the Apache commons CatalogResolver; this is the pathname
  of the OASIS catalog file to use when parsing XML files.
* `log` - location of the log files.  Defaults to the *log* subdirectory of the directory
  from which the app is run.


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

## Logging

The location of log files is controlled by the system paramter `log`, which is usually set to
the value "log" using `-Dlog=log` command-line switch.

Logging is controlled by properties set in the *src/main/resources/log4j.properties*
file. The log level is controlled by the `log4j.rootLogger` property, and can be set to
one of TRACE, DEBUG, INFO, WARN, ERROR or FATAL.




## Development

### Test item provider

When the value of *item_source" is "test", the citation data is mock data from the
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

Below is a list of some of the notable dependencies, along with links to documentation,
useful when doing development.


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

* [Home page](http://wiki.fasterxml.com/JacksonHome)
* [Data binding](https://github.com/FasterXML/jackson-databind) - includes tutorial
  on how to use it.
* [Javadocs](http://fasterxml.github.io/jackson-databind/javadoc/2.3.0/)
* [Jackson annotations](https://github.com/FasterXML/jackson-annotations) - how to
  annotate the classes that map to JSON objects

**kitty-cache**

This library, [kitty-cache](https://code.google.com/p/kitty-cache/), is not in
Maven Central. It is declared in the *pom.xml*, but needs to be built and installed to your
local maven repository.  For example:

```
svn checkout http://kitty-cache.googlecode.com/svn/trunk/ kitty-cache-read-only
cd kitty-cache-read-only
mvn install
```

**DtdAnalyzer**

Some of the XSLT conversions of XML to JSON import a library XSLT from the
[DtdAnalyzer](https://github.com/ncbi/DtdAnalyzer).  The library XSLT is
[xml2json-2.0.xsl](https://github.com/ncbi/DtdAnalyzer/blob/master/xslt/xml2json-2.0.xsl).


### XSLT conversions

You can try out any given XSLT from the command line, using Saxon Home Edition.

For example, nxml2json.xsl converts PMC NXML into citeproc-json format.  To try it out,
first set an alias to point to your Saxon jar file, and the catalog
file to use to resolve the DTDs.  For example:

```
alias saxon95='java -cp /path/to/saxon9.5.1.4/saxon9he.jar:/pmc/JAVA/saxon9.5.1.4/xml-commons-resolver-1.2/resolver.jar \
  net.sf.saxon.Transform -catalog:/path/to/catalog.xml '
```

Then, run the transformations:

```
cd test
saxon95 -s:aiid/3362639.nxml -xsl:../xslt/nxml2json.xsl pmcid=PMC3362639
saxon95 -s:aiid/3352855.nxml -xsl:../xslt/nxml2json.xsl pmid=22615544 pmcid=PMC3352855
```

