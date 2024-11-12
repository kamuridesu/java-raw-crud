package sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLite {
    private Connection connection;

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
                "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "name VARCHAR(50) NOT NULL UNIQUE," +
                "age INTEGER DEFAULT 0," +
                "skills TEXT DEFAULT ''," +
                "salary DECIMAL(10, 2) DEFAULT 0.0" +
                ")";
        var stmt = this.connection.createStatement();
        stmt.execute(query);
        stmt.close();
    }

    public void insertUser(User user) throws SQLException {
        var query = this.connection
                .prepareStatement("INSERT INTO users (name, age, skills, salary) VALUES (?, ?, ?, ?)");
        query.setString(1, user.getName());
        query.setInt(2, user.getAge());
        query.setString(3, user.getSkills());
        query.setDouble(4, user.getSalary());
        query.executeUpdate();
        query.close();

    }

    public List<User> selectAllUsers() throws SQLException {
        var query = "SELECT * FROM users";
        List<User> allUsers = new ArrayList<>();
        var stmt = this.connection.createStatement();
        var result = stmt.executeQuery(query);
        while (result.next()) {
            allUsers.add(new User(result.getInt("id"), result.getString("name"), result.getInt("age"),
                    result.getString("skills"), result.getDouble("salary")));
        }
        stmt.close();
        return allUsers;
    }

    public User selectUser(String name) throws SQLException {
        var query = this.connection.prepareStatement("SELECT * FROM users WHERE name=?");
        User u;
        query.setString(1, name);
        var result = query.executeQuery();
        u = new User(result.getInt("id"), result.getString("name"), result.getInt("age"), result.getString("skills"),
                result.getDouble("salary"));
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
        query.setString(1, user.getName());
        query.setInt(2, user.getAge());
        query.setString(3, user.getSkills());
        query.setDouble(4, user.getSalary());
        query.setInt(5, user.getId());
        query.executeUpdate();
        query.close();
    }

    public void dropTable() throws SQLException {
        var query = "DROP TABLE IF EXISTS users";
        var stmt = this.connection.createStatement();
        stmt.execute(query);
        stmt.close();
    }
}
