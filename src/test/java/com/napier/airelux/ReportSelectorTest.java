package com.napier.airelux;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.Scanner;

class ReportSelector {

    /**
     * Connection to MySQL database.
     */
    private static Connection con = null;

    public static void main(String[] args) {
        ReportSelector app = new ReportSelector();

        // Connect to the database
        if (args.length < 1) {
            app.connect("localhost:33060", 0);
        } else {
            app.connect(args[0], Integer.parseInt(args[1]));
        }

        // Check connection status
        if (con != null) {
            System.out.println("Connected to the database successfully.");
            app.runReport("SELECT * FROM country LIMIT 5"); // Example query
        } else {
            System.out.println("Failed to connect to the database.");
        }

        // Disconnect from the database
        app.disconnect();
    }

    /**
     * Connect to the MySQL database.
     */
    public void connect(String conString, int delay) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Could not load SQL driver", e);
        }

        int retries = 10;
        for (int i = 0; i < retries; ++i) {
            try {
                Thread.sleep(delay);
                con = DriverManager.getConnection("jdbc:mysql://" + conString
                        + "/world?allowPublicKeyRetrieval=true&useSSL=false", "root", "example");
                System.out.println("Successfully connected");
                return; // Successful connection, exit method
            } catch (SQLException | InterruptedException e) {
                System.out.println("Failed to connect to database attempt " + i);
                if (i == retries - 1) {
                    throw new RuntimeException("Failed to connect to database after " + retries + " attempts", e);
                }
            }
        }
    }

    /**
     * Run a SQL report query and print the results.
     */
    public void runReport(String query) {
        if (query == null || query.isEmpty()) {
            throw new IllegalArgumentException("Query cannot be null or empty");
        }

        if (con == null) {
            throw new IllegalStateException("No active database connection");
        }

        try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Print column headers
            for (int i = 1; i <= columnCount; i++) {
                System.out.print(metaData.getColumnName(i) + "\t");
            }
            System.out.println();

            // Print rows
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    System.out.print(rs.getString(i) + "\t");
                }
                System.out.println();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error executing query", e);
        }
    }

    /**
     * Disconnect from the MySQL database.
     */
    public void disconnect() {
        if (con != null) {
            try {
                con.close();
                System.out.println("Disconnected from the database.");
            } catch (SQLException e) {
                System.out.println("Error closing connection to database: " + e.getMessage());
            } finally {
                con = null; // Ensure the connection is set to null
            }
        }
    }

    /**
     * Set the database connection (used for unit testing).
     */
    public void setConnection(Connection connection) {
        con = connection;
    }

    /**
     * Accessor for the connection (used for testing).
     */
    public Connection getConnection() {
        return con;
    }
}
