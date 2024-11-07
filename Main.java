import sqlite.SQLite;
import sqlite.User;
import httpserver.Request;
import httpserver.Response;
import httpserver.Server;
import jsonparser.JSONParser;

import java.io.IOException;
import java.net.InetAddress;
import java.sql.SQLException;
import java.util.HashMap;


public class Main {

    private static final SQLite db = new SQLite("users.db");

    @SuppressWarnings("unchecked")
    private static final User getUserFromJson(String json) {
        var parsedData = JSONParser.parseJson(json).getData();
        var personalInfo = (HashMap<Object, Object>) parsedData.get("personal_info");
        if (personalInfo == null) {
            throw new IllegalArgumentException("Invalid JSON format");
        }
        return new User(
            String.valueOf(parsedData.get("name")),
            Integer.valueOf(String.valueOf(personalInfo.get("age"))),
            String.valueOf(personalInfo.get("skills")),
            Double.valueOf(String.valueOf(personalInfo.get("salary")))
        );
    }

    public static void main(String[] args) throws IOException {

        var server = new Server(InetAddress.getByName("0.0.0.0"), 8081);
        server.registerRoute("/", (Request req) -> {
            var userQuery = req.getQueryParams();
            try {
                if (userQuery.size() < 1 || userQuery.get(0).get("username") == null) {
                    var users = db.selectAllUsers();
                    System.out.println(users);
                    return new Response(users.toString());
                }
                var username = userQuery.get(0).get("username");
            
                var user = db.selectUser(username);
                if (!user.isValid()) {
                    return new Response("User not found", 404);
                }
                return new Response(user.toString());
            } catch (SQLException e) {
                return new Response("SQL error: " + e.getMessage(), 500);
            }
        });

        server.registerRoute("/", "POST", (Request req) -> {
            var body = req.getBody();
            try {
                var user = getUserFromJson(body);
                var existingUser = db.selectUser(user.getName());
                if (existingUser.isValid()) {
                    return new Response("User already exists", 400);
                }
                db.insertUser(user);
                return new Response(user.toString());
            } catch (IllegalArgumentException e) {
                return new Response("Invalid JSON format", 400);
            } catch (SQLException e) {
                return new Response("SQL error: " + e.getMessage(), 500);
            }
        });

        server.registerRoute("/", "PUT", (Request req) -> {
            var body = req.getBody();
            try {
                var user = getUserFromJson(body);
                var existingUser = db.selectUser(user.getName());
                if (!existingUser.isValid()) {
                    return new Response("User not found", 404);
                }
                existingUser.update(user);
                db.updateUser(existingUser);
                return new Response(user.toString());
            } catch (IllegalArgumentException e) {
                return new Response("Invalid JSON format", 400);
            } catch (SQLException e) {
                return new Response("SQL error: " + e.getMessage(), 500);
            }
        });

        server.registerRoute("/", "DELETE", (Request req) -> {
            var userQuery = req.getQueryParams();
            if (userQuery.size() < 1 || userQuery.get(0).get("username") == null) {
                return new Response("Missing username param", 400);
            }
            var username = userQuery.get(0).get("username");
            try {
                db.deleteUser(username);
                return new Response("User deleted");
            } catch (SQLException e) {
                return new Response("SQL error: " + e.getMessage(), 500);
            }
        });

        server.start();
    }
}
