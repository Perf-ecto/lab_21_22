package org.example;

import java.sql.*;

public class dbConnection{
    final static String connectionURL = "jdbc:sqlite:C:\\Users\\vas228\\OneDrive\\Рабочий стол\\sqlite\\identifier";
    private static Connection connection;
    private static Statement stmt;
    public static boolean authenticate(String login, String password) {
        String query = "SELECT * FROM users WHERE login = ? AND pass = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, login);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                System.out.println("Authentication successful for user: " + login);
                return true;
            } else {
                System.out.println("Authentication failed for user: " + login);
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[]args){
        try{
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(connectionURL);
            stmt = connection.createStatement();
            System.out.println("Connected ");
            String loginOne = "login1";
            String passOne = "pass1";
            String query = "SELECT *FROM users Where login = 'login1'AND pass = 'pass2'";
            String nickReturn = " ";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()){
                nickReturn = rs.getString("nick");
                System.out.println(rs.getString("nick"));

            }
            if (nickReturn==" ")System.out.println("Ничего не найдено ");


        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }
}