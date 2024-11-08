#! /bin/bash

echo "Hello World! Let's begin!"

echo "Checking dependencies..."

[[ ! -f ./target/sqlite-jdbc-3.46.0.jar ]] && curl --output sqlite-jdbc-3.46.0.jar https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.46.0.0/sqlite-jdbc-3.46.0.0.jar -SsL
[[ ! -f ./target/slf4j-api-2.0.13.jar ]] && curl --output slf4j-api-2.0.13.jar https://repo1.maven.org/maven2/org/slf4j/slf4j-api/2.0.13/slf4j-api-2.0.13.jar -SsL

echo "Dependencies checked!"

echo "Building the project..."

. ./build.sh

echo "Project built successfully!"

echo "Running tests..."

. ./test.sh || exit 1

echo "All tests passed!"

java -cp "target:target/*" Main
