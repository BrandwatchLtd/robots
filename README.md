# Robots.txt Processing Library

This repository contains a stand alone library for the parsing of *robots.txt* files, and application of the robots exclusion protocol. It handles the following keys tasks:

 * Identification the robots.txt location for some resource uri. 
 * Downloading of robots files.
 * Parsing robots.txt format.
 * Caching previously downloaded results.
 * Compiling agent and path expressions to efficient matchers.
 * Determining relevant agent group (matching agent expressions)
 * Determine resource exclusions (path directive matching)
 * Extraction of sitemap directives.
 * Extraction of other (non-standard) directives.
 
## Building

To build the artifact, simply run maven:

```sh
mvn package
```
 
Note that the lbrary contains a JavaCC parser, which must be generated before normal compilation. Maven will handle this automagically, but your chosen IDE will most-likely flownder. Consult google.

# Usage

Include the library dependency in you maven `pom.xml`:

```xml
<dependency>
    <groupId>com.brandwatch</groupId>
    <artifactId>robots</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

Configure the service using an instance of `RobotsConfig`, then instantiate the `RobotsService` instance from the config object. `RobotsService` has exactly one method `isAllowed`, which takes a crawler agent string, and a resource URI, returning true if the given crawler is allowed to access the given resource.

```java
RobotsConfig config = new RobotsConfig();
config.setCachedExpiresHours(48);
config.setCacheMaxSizeRecords(10000);
config.setMaxFileSizeBytes(192 * 1024);

RobotsService service = config.getService();
String crawlerAgent = "magpie-crawler/1.1 (U; Linux x86_64; en-GB; +http://www.brandwatch.net)";

...

URI resource = URI.create("https://example.com/path/to/some/file.html");
if(service.isAllowed(crawlerAgent, resource)) {
    // crawl away!
}
```

