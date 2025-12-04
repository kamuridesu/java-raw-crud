package tests;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.atomic.AtomicBoolean;
import scan.Scan;

public class Test {
    public static void main(String[] args) throws IOException {

        var hasTestFailed = new AtomicBoolean(false);
        var testClasses = Scan.searchClass(scan.Test.class, Object.class);
        testClasses.forEach(s -> {
            Class<?> clazz = s.getClass();
            var beforeEach = Scan.searchForAnnotatedMethods(clazz, scan.BeforeEach.class);
            var afterEach = Scan.searchForAnnotatedMethods(clazz, scan.AfterEach.class);
            var afterAll = Scan.searchForAnnotatedMethods(clazz, scan.AfterAll.class);
            var beforeAll = Scan.searchForAnnotatedMethods(clazz, scan.BeforeAll.class);
            var tests = Scan.searchForAnnotatedMethods(clazz, scan.Test.class);

            try {
                for (var m : beforeAll)
                    m.invoke(s);

                System.out.println("- Running tests for " + clazz.getName());

                for (var m : tests) {
                    try {
                        for (var be : beforeEach)
                            be.invoke(s);

                        m.invoke(s);
                        System.out.println("  [OK] " + m.getName());

                    } catch (InvocationTargetException e) {
                        hasTestFailed.set(true);
                        var cause = e.getCause();
                        System.err.println("  [FAIL] " + m.getName() + " -> " + cause.getMessage());
                    } catch (Exception e) {
                        System.err.println("  [ERROR] Reflection error in " + m.getName());
                        e.printStackTrace();
                    } finally {
                        try {
                            for (var ae : afterEach)
                                ae.invoke(s);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                for (var m : afterAll)
                    m.invoke(s);

            } catch (Exception e) {
                e.printStackTrace();
            }

            if (hasTestFailed.get()) {
                System.exit(1);
            }
        });
    }
}
