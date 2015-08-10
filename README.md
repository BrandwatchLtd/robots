# What Is This? [![Build Status](https://travis-ci.org/BrandwatchLtd/async-mixpanel-client.svg)](https://travis-ci.org/BrandwatchLtd/async-mixpanel-client)
This repo hosts a wrapper around the official Mixpanel Java client.

# Why?
The official Mixpanel client falls down in a few ways. First it relies a lot on null parameters. When something is optional you just don't pass it in. However this can be confusing and can make code more complicated than is required so this wrapper provides full methods for every valid combination of parameters.

Second the official client is synchronous adding delay to your code. We live in a multicore world these days so this wrapper offloads that sensing delay to another thread and deals with queueing and scheduling for you.

# How?
To add the dependency to Maven:

```xml
<dependency>
  <groupId>com.brandwatch.mixpanel</groupId>
  <artifactId>mixpanel-client</artifactId>
  <version>${mixpanel.version}</version>
</dependency>
```

You can find the latest version number on [Nexus](https://nexus.brandwatch.com/index.html#nexus-search;quick~mixpanel-client).


```java
ClientConfig config = new ClientConfigBuilder()
                          .maxBatchSize(1000)
                          .maxBatchTimeInSeconds(10)
                          .projectToken("aaabbbccc111222333")
                          .build();

MixpanelClient client = new MixpanelClient(config);

client.event("my-event-key");
```

# Requirements
Java 6 or above

# Building The Project
`mvn clean package`
