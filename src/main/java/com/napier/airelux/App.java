package com.napier.airelux;

import java.io.*;
import java.sql.*;

public class App {
    private Connection con = null;

    public static void main(String[] args) {
        // Create new Application
        App a = new App();

        if (args.length < 1) {
            // Local connection
            a.connect("localhost:33060", 0);
        } else {
            // Docker parameters passed from Dockerfile
            a.connect(args[0], Integer.parseInt(args[1]));
        }

        try {
            a.report1();
        } catch (IOException e) {
            System.out.println("Error writing report: " + e.getMessage());
        }

        // Disconnect from database
        a.disconnect();
    }

    public void report1() throws IOException {
        StringBuilder sb = new StringBuilder();
        Statement stmt = null;
        ResultSet rset = null;

        try {
            // Create an SQL statement
            stmt = con.createStatement();
            // Create string for SQL statement
            String sql = "SELECT * FROM country";
            // Execute SQL statement
            rset = stmt.executeQuery(sql);

            // Process the results
            while (rset.next()) {
                String name = rset.getString("name");
                Integer population = rset.getInt("population");
                sb.append(name).append("\t").append(population).append("\r\n");
            }

            // Ensure the output directory exists
            File outputDir = new File("./output/");
            if (!outputDir.exists()) {
                outputDir.mkdir();
            }

            // Write to the file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File("./output/report1.txt")))) {
                writer.write(sb.toString());
            }
            System.out.println(sb.toString());

        } catch (SQLException e) {
            System.out.println("SQL error: " + e.getMessage());
            System.out.println("Failed to retrieve details.");
        } finally {
            // Close resources
            try {
                if (rset != null) rset.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }
    }

    /**
     * Connect to the MySQL database.
     *
     * @param conString Connection string, e.g., "db:3306" for docker or "localhost:33060" for local.
     * @param delay Connection retry delay in milliseconds.
     */
    public void connect(String conString, int delay) {
        try {
            // Load Database driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        int retries = 10;
        for (int i = 0; i < retries; ++i) {
            System.out.println("Connecting to database...");
            try {
                // Wait a bit for db to start
                Thread.sleep(delay);
                // Connect to database with allowPublicKeyRetrieval set for integration tests
                con = DriverManager.getConnection("jdbc:mysql://" + conString + "/world?allowPublicKeyRetrieval=true&useSSL=false", "root", "example");
                System.out.println("Successfully connected");
                break;
            } catch (SQLException sqle) {
                System.out.println("Failed to connect to database attempt " + (i + 1));
                System.out.println(sqle.getMessage());
            } catch (InterruptedException ie) {
                System.out.println("Thread interrupted? Should not happen.");
            }
        }
    }

    /**
     * Disconnect from the MySQL database.
     */
    public void disconnect() {
        if (con != null) {
            try {
                // Close connection
                con.close();
                System.out.println("Disconnected from database.");
            } catch (SQLException e) {
                System.out.println("Error closing connection to database: " + e.getMessage());
            }
        }
    }
}