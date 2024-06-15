//login1
//pass2
//nick : zhenyavasilev
// admin :pass:123
package org.example;

import java.sql.*;
import java.util.Scanner;


public class authentication {
    final static String connectionURL = "jdbc:sqlite:C:\\Users\\vas228\\OneDrive\\Рабочий стол\\sqlite\\identifier";
    private static Connection connection;
    private static Statement stmt;



    public static void main(String[] args) {
        connectToDatabase();
        Scanner sc = new Scanner(System.in);
        System.out.println("userLogin:");
        String userLogin = sc.nextLine();
        System.out.println("userPassword:");
        String userPassword = sc.nextLine();

        String userNick = authenticate(userLogin, userPassword);

        if ("admin".equals(userLogin) && userNick != null) {
            boolean exit = false;
            while (!exit) {
                System.out.println("Enter command(/adduser, /deluser, /exit):");
                String adminCommand = sc.nextLine();

                switch (adminCommand) {
                    case "/adduser":
                        System.out.println("Enter login for new user:");
                        String newLogin = sc.nextLine();
                        System.out.println("Enter password for new user:");
                        String newPassword = sc.nextLine();
                        System.out.println("Enter nick for new user:");
                        String newNick = sc.nextLine();
                        addUser(newLogin, newPassword, newNick);
                        break;
                    case "/deluser":
                        System.out.println("Enter login of user to delete:");
                        String delLogin = sc.nextLine();
                        deleteUser(delLogin);
                        break;
                    case "/exit":
                        exit = true;
                        break;
                    default:
                        System.out.println("Unknown command.");
                        break;
                }
            }
        } else if (userNick != null) {
            System.out.println("User authenticated as " + userNick);
            System.out.println("Enter command(/changenick):");
            String command = sc.nextLine();
            if (command.startsWith("/changenick")) {
                System.out.println("Enter your new nick:");
                String newNick = sc.nextLine();
                changeNick(userLogin, newNick);
            }
        } else {
            System.out.println("Authentication failed.");
        }
        closeResources();



    }
    private static void connectToDatabase() {
        try {

            Class.forName("org.sqlite.JDBC");

            connection = DriverManager.getConnection(connectionURL);
            stmt = connection.createStatement();
            System.out.println("Connected");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static String authenticate(String login, String password) {
        String query = "SELECT nick FROM users WHERE login = ? AND pass = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, login);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String nick = rs.getString("nick");
                System.out.println("Authentication successful. Welcome, " + nick);
                return nick;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static void closeResources() {
        try {
            if (stmt != null) {
                stmt.close();
            }
            if (connection != null) {
                connection.close();
            }
            System.out.println("closed ");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static boolean changeNick(String userLogin, String newNick) {
        String updateQuery = "UPDATE users SET nick = ? WHERE login = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(updateQuery)) {
            pstmt.setString(1, newNick);
            pstmt.setString(2, userLogin);
            int updatedRows = pstmt.executeUpdate();

            if (updatedRows > 0) {
                System.out.println("Nickname changed");
                return true;
            } else {
                System.out.println("Could not change nickname for user " + userLogin);
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean addUser(String login, String password, String nick) {
        String insertQuery = "INSERT INTO users (login, pass, nick) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(insertQuery)) {
            pstmt.setString(1, login);
            pstmt.setString(2, password);
            pstmt.setString(3, nick);
            int updatedRows = pstmt.executeUpdate();

            if (updatedRows > 0) {
                System.out.println("User added successfully");
                return true;
            } else {
                System.out.println("Could not add user");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean deleteUser(String login) {
        String deleteQuery = "DELETE FROM users WHERE login = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(deleteQuery)) {
            pstmt.setString(1, login);
            int updatedRows = pstmt.executeUpdate();

            if (updatedRows > 0) {
                System.out.println("User deleted successfully");
                return true;
            } else {
                System.out.println("Could not delete user");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}
