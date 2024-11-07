#! /bin/bash

javac -d . -sourcepath . Main.java
java -cp ".:sqlite-jdbc-3.46.0.jar:slf4j-api-2.0.13.jar" Main
