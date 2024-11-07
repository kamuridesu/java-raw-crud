package sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLite {
    Connection connection;


    public SQLite(String db) {
        try {
            this.connection = DriverManager.getConnection("jdbc:sqlite:" + db);
            this.createUsersDB();
        } catch (SQLException e) {
            System.err.println(e.getClass().getName());
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }

    public void close() {
        try {
            this.connection.close();
        } catch (SQLException e) {
            System.err.println(e.getClass().getName());
            System.exit(1);
        }
    }

    void createUsersDB() throws SQLException {
        var query = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"+
                    "name VARCHAR(50) NOT NULL UNIQUE,"+
                    "age INTEGER DEFAULT 0,"+
                    "skills TEXT DEFAULT '',"+
                    "salary DECIMAL(10, 2) DEFAULT 0.0"+
                    ")";
        executeUpdate(query);
    }

    void executeUpdate(String query) throws SQLException {
        var stmt = this.connection.createStatement();
        stmt.executeUpdate(query);
        stmt.close();
    }

    public void insertUser(User user) throws SQLException {
        var query = this.connection.prepareStatement("INSERT INTO users (name, age, skills, salary) VALUES (?, ?, ?, ?)");
        query.setString(1, user.name);
        query.setInt(2, user.age);
        query.setString(3, user.skills);
        query.setDouble(4, user.salary);
        query.executeUpdate();
        query.close();

    }

    ResultSet executeQuery(String query) throws SQLException {
        var stmt = this.connection.createStatement();
        var result = stmt.executeQuery(query);
        stmt.close();
        return result;
    }

    public List<User> selectAllUsers() throws SQLException {
        var query = "SELECT * FROM users";
        List<User> allUsers = new ArrayList<>();
        var result = executeQuery(query);
        while (result.next()) {
            allUsers.add(new User(result.getInt("id"), result.getString("name"), result.getInt("age"), result.getString("skills"), result.getDouble("salary")));
        }
        return allUsers;
    }

    public User selectUser(String name) throws SQLException {
        var query = this.connection.prepareStatement("SELECT * FROM users WHERE name=?");
        User u;
        query.setString(1, name);
        var result = query.executeQuery();
        u = new User(result.getInt("id"), result.getString("name"), result.getInt("age"), result.getString("skills"), result.getDouble("salary"));
        query.close();
        return u;
    }

    public void deleteUser(String name) throws SQLException {
        var query = this.connection.prepareStatement("DELETE FROM users WHERE name=?");
        query.setString(1, name);
        query.executeUpdate();
        query.close();
    }

    public void updateUser(User user) throws SQLException {
        var query = this.connection.prepareStatement("UPDATE users SET name=?, age=?, skills=?, salary=? WHERE id=?");
        query.setString(1, user.name);
        query.setInt(2, user.age);
        query.setString(3, user.skills);
        query.setDouble(4, user.salary);
        query.setInt(5, user.id);
        query.executeUpdate();
        query.close();
    }
}

