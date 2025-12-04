package tests;

import scan.Test;
import scan.BeforeEach;
import scan.AfterEach;
import scan.AfterAll;
import sqlite.SQLite;
import sqlite.User;

@Test
public class TestSQLite {

    SQLite sqlite;

    @BeforeEach
    void initDB() {
        this.sqlite = new SQLite("test.db");
    }

    @BeforeEach
    void setupUser() {
        var user = new User("John Doe", 30, "Java, Python", 1000.0);
        try {
            sqlite.insertUser(user);
        } catch (Exception e) {
            throw new AssertionError("Error inserting user: " + e.getMessage());
        }
    }

    @AfterEach
    void dropTable() {
        try {
            sqlite.dropTable();
        } catch (Exception e) {
            throw new AssertionError("Error dropping table: " + e.getMessage());
        }
    }

    @AfterAll
    void deleteTestDBFileIfExists() {
        var file = new java.io.File("test.db");
        if (file.exists()) {
            file.delete();
        }
        sqlite.close();
    }

    @Test
    void testInsertUser() {
        var user = new User("Admin", 30, "Rest, Kubernetes", 100.0);
        try {
            sqlite.insertUser(user);
        } catch (Exception e) {
            throw new AssertionError("Error inserting user: " + e.getMessage());
        }
    }

    @Test
    void testSelectAllUsers() {
        try {
            var users = sqlite.selectAllUsers();
            assert users.size() > 0 : "No users found";
        } catch (Exception e) {
            throw new AssertionError("Error selecting users: " + e.getMessage());
        }
    }

    @Test
    void testSelectJohnDoe() {
        try {
            var user = sqlite.selectUser("John Doe");
            assert user.isValid() : "Invalid user";
        } catch (Exception e) {
            throw new AssertionError("Error selecting users: " + e.getMessage());
        }
    }

    @Test
    void testDeleteJohnDoe() {
        try {
            sqlite.deleteUser("John Doe");
        } catch (Exception e) {
            throw new AssertionError("Error deleting user: " + e.getMessage());
        }
    }

}
