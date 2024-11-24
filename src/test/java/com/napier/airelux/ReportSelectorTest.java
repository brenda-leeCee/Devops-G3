package com.napier.airelux;

import java.sql.*;

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

        // Example functionality (can be removed if not needed for testing)
        if (con != null) {
            System.out.println("Connected to the database successfully.");
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
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        int retries = 10;
        for (int i = 0; i < retries; ++i) {
            System.out.println("Connecting to database...");
            try {
                Thread.sleep(delay);
                con = DriverManager.getConnection("jdbc:mysql://" + conString
                        + "/world?allowPublicKeyRetrieval=true&useSSL=false", "root", "example");
                System.out.println("Successfully connected");
                return;
            } catch (SQLException sqle) {
                System.out.println("Failed to connect to database attempt " + i);
                System.out.println(sqle.getMessage());
            } catch (InterruptedException ie) {
                System.out.println("Thread interrupted? Should not happen.");
            }
        }
        // Ensure the connection is set to null after all retries fail
        con = null;
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
                System.out.println("Error closing connection to database");
            }
        }
    }

    /**
     * For testing: Accessor to get the current connection.
     */
    public Connection getConnection() {
        return con;
    }
}
