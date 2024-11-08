package tests;

import java.util.ArrayList;
import java.util.List;

public abstract class TestABC {

    protected final List<Runnable> testList = new ArrayList<>();
    protected final List<Runnable> afterAll = new ArrayList<>();
    protected final List<Runnable> beforeAll = new ArrayList<>();
    protected final List<Runnable> afterEach = new ArrayList<>();
    protected final List<Runnable> beforeEach = new ArrayList<>();

    void addTest(Runnable test) {
        this.testList.add(test);
    }

    void hookAfterAll(Runnable afterTests) {
        this.afterAll.add(afterTests);
    }

    void hookBeforeAll(Runnable beforeTests) {
        this.beforeAll.add(beforeTests);
    }

    void hookAfterEach(Runnable afterEach) {
        this.afterEach.add(afterEach);
    }

    void hookBeforeEach(Runnable beforeEach) {
        this.beforeEach.add(beforeEach);
    }

    public void run() {
        System.out.println("- Running tests for " + this.getClass().getName());
        this.beforeAll.forEach(s -> s.run());
        this.testList.forEach(s -> {
            this.beforeEach.forEach(t -> t.run());
            s.run();
            this.afterEach.forEach(t -> t.run());
        });
        System.out.println("  " + this.getClass().getName() + " tests passed");
        this.afterAll.forEach(s -> s.run());
    }

}
