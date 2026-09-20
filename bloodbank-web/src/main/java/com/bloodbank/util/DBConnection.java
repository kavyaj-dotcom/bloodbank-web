
package com.bloodbank.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String DB_URL =
            "jdbc:mysql://localhost:3306/blood_bank_db?useSSL=false&serverTimezone=UTC";

    private static final String DB_USER = "root";

    private static final String DB_PASSWORD =
            System.getenv("DB_PASSWORD");

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(
                    "MySQL JDBC driver not found in classpath.", e);
        }
    }

    private DBConnection() {
    }

    public static Connection getConnection() throws SQLException {

        if (DB_PASSWORD == null || DB_PASSWORD.isBlank()) {
            throw new SQLException(
                    "DB_PASSWORD environment variable is not set.");
        }

        return DriverManager.getConnection(
                DB_URL, DB_USER, DB_PASSWORD);
    }
}
