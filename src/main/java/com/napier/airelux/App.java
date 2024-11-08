package com.napier.airelux;

import javax.swing.JOptionPane;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.*;
import java.io.IOException;

public class App {

    /**
     * Connection to MySQL database.
     */
    private Connection con = null;

    public static void main(String[] args) {
        // Create new Application
        App a = new App();

        if (args.length < 1) {
            // local
            a.connect("localhost:33060", 0);
        } else {
            // docker parameters passed from Dockerfile
            a.connect(args[0], Integer.parseInt(args[1]));
        }

        try {
            a.generateReportBasedOnUserSelection();
        } catch (IOException e) {
            System.out.println("Error while generating report: " + e.getMessage());
        }

        // Disconnect from database
        a.disconnect();
    }

    /**
     * Method to connect to the database
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
                // Connect to database
                con = DriverManager.getConnection("jdbc:mysql://" + conString
                        + "/world?allowPublicKeyRetrieval=true&useSSL=false", "root", "example");
                System.out.println("Successfully connected");
                break;
            } catch (SQLException sqle) {
                System.out.println("Failed to connect to database attempt " + i);
                System.out.println(sqle.getMessage());
            } catch (InterruptedException ie) {
                System.out.println("Thread interrupted? Should not happen.");
            }
        }
    }

    /**
     * Method to disconnect from the database
     */
    public void disconnect() {
        if (con != null) {
            try {
                // Close connection
                con.close();
            } catch (Exception e) {
                System.out.println("Error closing connection to database");
            }
        }
    }

    /**
     * Generate report based on user selection
     */
    public void generateReportBasedOnUserSelection() throws IOException {
        String reportCategoryNumber;
        int reportCategoryNum;
        String output;

        int leastEntry = 1;
        int mostEntry = 5;

        // Get report category selection from the user
        do {
            reportCategoryNumber = JOptionPane.showInputDialog("Please enter the number of the report category you wish to select:\n"
                    + "1. Country Reports\n"
                    + "2. City Reports\n"
                    + "3. Capital City Reports\n"
                    + "4. Population Reports\n"
                    + "5. Language Reports");

            reportCategoryNum = Integer.parseInt(reportCategoryNumber);

        } while (reportCategoryNum < leastEntry || reportCategoryNum > mostEntry);

        switch (reportCategoryNum) {
            case 1:
                output = "Country Report Option selected.";
                report2("country");
                break;
            case 2:
                output = "City Report Option selected.";
                report2("city");
                break;
            case 3:
                output = "Capital City Report Option selected.";
                report2("capital_city");
                break;
            case 4:
                output = "Population Report Option selected.";
                report2("population");
                break;
            case 5:
                output = "Language Report Option selected.";
                report2("language");
                break;
            default:
                output = "Invalid category selected.";
                break;
        }

        JOptionPane.showMessageDialog(null, output);
    }

    /**
     * Method to generate report based on SQL query results
     */
    public void report2(String reportType) throws IOException {
        StringBuilder sb = new StringBuilder();
        String sql = "";

        switch (reportType) {
            case "country":
                sql = "SELECT * FROM country";
                break;
            case "city":
                sql = "SELECT * FROM city";
                break;
            case "capital_city":
                sql = "SELECT * FROM capital_city";
                break;
            case "population":
                sql = "SELECT * FROM population";
                break;
            case "language":
                sql = "SELECT * FROM language";
                break;
            default:
                System.out.println("Invalid report type");
                return;
        }

        try {
            Statement stmt = con.createStatement();
            ResultSet rset = stmt.executeQuery(sql);
            while (rset.next()) {
                String name = rset.getString("name");
                int population = rset.getInt("population");
                sb.append(name).append("\t").append(population).append("\r\n");
            }
            new File("./output/").mkdir();
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File("./output/report_" + reportType + ".txt")));
            writer.write(sb.toString());
            writer.close();
            System.out.println(sb.toString());
        } catch (Exception e) {
            System.out.println("Failed to get details: " + e.getMessage());
        }
    }
}
