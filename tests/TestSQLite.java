package tests;

import sqlite.SQLite;
import sqlite.User;

public class TestSQLite extends TestABC {

    private final SQLite sqlite = new SQLite("test.db");

    public TestSQLite() {
        addTest(this::testInsertUser);
        addTest(this::testSelectAllUsers);
        addTest(this::testSelectJohnDoe);
        addTest(this::testDeleteJohnDoe);
        hookAfterAll(this::deleteTestDBFileIfExists);
    }

    private void deleteTestDBFileIfExists() {
        var file = new java.io.File("test.db");
        if (file.exists()) {
            file.delete();
        }
        sqlite.close();
    }

    void testInsertUser() {
        var user = new User("John Doe", 30, "Java, Python", 1000.0);
        try {
            sqlite.insertUser(user);
        } catch (Exception e) {
            throw new AssertionError("Error inserting user: " + e.getMessage());
        }
    }

    void testSelectAllUsers() {
        try {
            var users = sqlite.selectAllUsers();
            assert users.size() > 0 : "No users found";
        } catch (Exception e) {
            throw new AssertionError("Error selecting users: " + e.getMessage());
        }
    }

    void testSelectJohnDoe() {
        try {
            var user = sqlite.selectUser("John Doe");
            assert user.isValid() : "Invalid user";
        } catch (Exception e) {
            throw new AssertionError("Error selecting users: " + e.getMessage());
        }
    }

    void testDeleteJohnDoe() {
        try {
            sqlite.deleteUser("John Doe");
        } catch (Exception e) {
            throw new AssertionError("Error deleting user: " + e.getMessage());
        }
    }

    
}
