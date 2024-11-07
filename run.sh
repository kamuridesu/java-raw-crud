#! /bin/bash

[[ ! -f ./target/sqlite-jdbc-3.46.0.jar ]] && curl --output sqlite-jdbc-3.46.0.jar https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.46.0.0/sqlite-jdbc-3.46.0.0.jar -SsL
[[ ! -f ./target/slf4j-api-2.0.13.jar ]] && curl --output slf4j-api-2.0.13.jar https://repo1.maven.org/maven2/org/slf4j/slf4j-api/2.0.13/slf4j-api-2.0.13.jar -SsL

. ./build.sh

java -cp "target:target/*" Main
