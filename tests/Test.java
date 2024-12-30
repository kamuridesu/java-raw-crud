package tests;

import java.io.IOException;

import scan.Scan;

public class Test {
    public static void main(String[] args) throws IOException {

        var testClasses = Scan.search(scan.Test.class);
        testClasses.forEach(s -> {
            TestABC tc = (TestABC) s;
            tc.run();
        });

    }
}
