package tests;

public class Test {

    private static final TestJsonParser testJsonParser = new TestJsonParser();
    private static final TestSQLite testSQLite = new TestSQLite();

    public static void main(String[] args) {
        testJsonParser.run();
        testSQLite.run();
    }
}
