# com.brandwatch:robots

This repository contains a stand alone library for the parsing of *robots.txt* files, and
application of the robots exclusion protocol.

# Specification

The `robots.txt` file format is part of the [Robots exclusion standard](http://en.wikipedia.org/wiki/Robots_exclusion_standard).
The protocol is a convention rather than formally agreed standard, and as such implementations
vary considerably. To formalise our approach we decided to adopt Google's interpretation of the
protocol, which is publicly available here: [Google Robots.txt Specifications](https://developers.google.com/webmasters/control-crawl-index/docs/robots_txt).


Example *robots.txt* file

```
User-Agent: *
Disallow: /harming/humans
Disallow: /ignoring/human/orders
Disallow: /harm/to/self
```

(from [http://www.last.fm/robots.txt](http://www.last.fm/robots.txt))

# Responsibilities

This module handles the entire *robots.txt* processing pipeline; from the identification and downloading
of a robots URI, to applying path exclusion directives. The following sub-sections offer a brief description
of all tasks handled by the module.

#### Identification

Given the URI of some resource we wish to crawl, identify the URI of the associated *robots.txt* file.
For example `http://example.com/path/to/resource.html` is associated with
`http://example.com:80/robots.txt`. Every resource has at-most one associated *robots.txt*, found
in the root path for the resources scheme, host, and port. See
[Google Robots.txt Specifications](https://developers.google.com/webmasters/control-crawl-index/docs/robots_txt) for further details.

#### Downloading

Once a *robots.txt* has been identified, the file is downloaded using an appropriate protocol. If an
I/O error occurs we interpret that as an *allow* directive for the resource being queried. We also
constrain the maximum download size.

#### Parsing

Resources are parsed using a *fast* LL1 top-down parser, built in JavaCC. The process is relatively
forgiving, allowing undefined field names, though can still fail if something entirely unexpected
happens.

#### Caching

For obvious reasons, we don't want to re-acquire the *robots.txt* file for every query, so we
cache results for pre-defined period (currently 2 days). The cache is also size limited so
memory usage is bounded.

#### Compiling wildcard expressions

The *robots.txt* file can contain user-agent and path expressions, containing wild-cards and other
non-literal syntax. The module translates these expressions to efficient regular expressions.

#### Agent group matching

The robots file can contain multiple agent groups, defined by one or more user-agent directives. The module
finds the group that best matches our crawlers user agent string. In the case that multiple groups match,
we choose the most precise match (longest matching expression).

#### Path directive matching

Once we have a group, we then match all it's path expressions to the queried resource URI. The matching
happens in the order presented in the file, and stops on the first match. If the first match is
an *allow* directive, the queried resource is allowed, otherwise the resource is disallowed. If no
path expressions match, then the resource is allowed.

#### Extraction of other directives

In addition to the standard exclusion rules, *robots.txt* sometimes contains non-standard directives.
These include site-map URIs, and crawler delay instructions. The module parses these directives
 and holds them in the internal model, but does nothing more. If we wish to make use of these directives
 in the future, it will be trivial to extend the module functionality to do so.

# Prerequisites

 1. Oracle JDK 1.6
 2. Maven 3

# Dependencies

Production Dependencies:

 1. Google Guava 14
 2. SLF4J 1.7

Testing Dependencies:

 1. JUnit 4.11
 2. Mockito 1.9.5
 2. Hamcrest 1.3

# Building

To build the artifact, simply run maven:

```sh
mvn clean install
```
 
Note that the library contains a JavaCC parser, which must be generated before
normal compilation. Maven will handle this auto-magically, but your chosen IDE
will most-likely flounder. Consult Google.

# Usage

Include the library dependency in your maven `pom.xml`:

```xml
<dependency>
    <groupId>com.brandwatch</groupId>
    <artifactId>robots</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

Configure the service using an instance of `RobotsConfig`, then instantiate the `RobotsService`
instance from the config object. `RobotsService` has exactly one method `isAllowed`, which
takes a crawler agent string, and a resource URI, returning true if the given crawler is
allowed to access the given resource.

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

