# SQLite

To use SQLite with Java, we need to download the JDBC driver for SQLite. Just run the `setup.sh` file and two new .jar files will be downloaded, one contains the SQLite driver, the other contains a dependency for the driver.

After that, just run the Main class with the command:

```
java -cp ".:sqlite-jdbc-3.46.0.jar:slf4j-api-2.0.13.jar" Main.java
```
