#!/bin/bash

# There's no point repeatedly updating coveralls, so do it after the jdk8 build only.
if [ "$TRAVIS_JDK_VERSION" == "oraclejdk8" ]; then
    echo "Running coveralls for JDK version: $TRAVIS_JDK_VERSION"
    mvn cobertura:cobertura org.eluder.coveralls:coveralls-maven-plugin:report
else
    echo "Skipping coveralls for JDK version: $TRAVIS_JDK_VERSION"
fi