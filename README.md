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

