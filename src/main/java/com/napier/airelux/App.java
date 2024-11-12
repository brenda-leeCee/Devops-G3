package com.napier.airelux;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.*;
import java.io.IOException;
import java.util.Scanner;

class ReportSelector {
    /**
     * Connection to MySQL database.
     */
    private static Connection con = null;

    public static void main(String[] args) {
        // Create an instance of ReportSelector
        ReportSelector app = new ReportSelector();

        // Connect to the database
        if (args.length < 1) {
            // Local
            app.connect("localhost:33060", 0);
        } else {
            // Docker parameters passed from Dockerfile
            app.connect(args[0], Integer.parseInt(args[1]));
        }

        // Welcome message and logo
        System.out.println("Welcome to Analytics ");
        System.out.println("     /\\        /\\");
        System.out.println("    /  \\      /  \\");
        System.out.println("   /____\\    /____\\");
        System.out.println("  /      \\  /      \\");
        System.out.println(" /        \\/        \\");
        System.out.println("AA - Always Ahead");
        System.out.println();

        System.out.println("Press Enter to proceed or type 'exit' to quit.");
        Scanner scanner = new Scanner(System.in);
        String userInput = scanner.nextLine().trim().toLowerCase();
        if (userInput.equals("exit")) {
            System.out.println("Exiting the program. Goodbye!");
            app.disconnect();
            return;
        }

        // Show report selection
        app.showReportSelection(scanner);

        // Disconnect from the database
        app.disconnect();
    }

    private void showReportSelection(Scanner scanner) {
        int reportCategoryNum = 0;

        // Constants for input validation
        int leastEntry = 1;
        int mostEntry = 6;
        boolean validInput = false;

        // Prompt user for report category
        do {
            System.out.println("1. Country Population Reports");
            System.out.println("2. City Population Reports");
            System.out.println("3. Capital City Reports");
            System.out.println("4. Population Analysis Reports");
            System.out.println("5. City Reports");
            System.out.println("6. Language Reports");
            System.out.println();
            System.out.print("Please enter the number of the report category you wish to select: ");

            try {
                reportCategoryNum = Integer.parseInt(scanner.nextLine().trim());

                if (reportCategoryNum >= leastEntry && reportCategoryNum <= mostEntry) {
                    validInput = true;
                } else {
                    System.out.println("Please enter a number between " + leastEntry + " and " + mostEntry + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        } while (!validInput);

        // Use switch to determine which report category was selected
        switch (reportCategoryNum) {
            case 1:
                System.out.println("You selected Country Population Reports.");
                System.out.println("1. Countries sorted by population.");
                System.out.println("2. Countries filtered by continent.");
                System.out.println("3. Countries filtered by region.");
                System.out.println("4. Top N countries by population.");
                System.out.println();

                int countrySubcategoryNum;
                do {
                    System.out.print("Please choose a report to run: ");
                    try {
                        countrySubcategoryNum = Integer.parseInt(scanner.nextLine().trim());
                        if (countrySubcategoryNum >= 1 && countrySubcategoryNum <= 4) {
                            switch (countrySubcategoryNum) {
                                case 1:
                                    System.out.println("You selected: Countries sorted by population.");
                                    runCountriesSortedByPopulation();
                                    break;
                                case 2:
                                    System.out.println("Feature not implemented yet.");
                                    break;
                                case 3:
                                    System.out.println("Feature not implemented yet.");
                                    break;
                                case 4:
                                    System.out.println("Feature not implemented yet.");
                                    break;
                            }
                            break;
                        } else {
                            System.out.println("Please enter a number between 1 and 4.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Please enter a valid number.");
                    }
                } while (true);
                break;

            default:
                System.out.println("Invalid category selected.");
                break;
        }
    }

    private void runCountriesSortedByPopulation() {
        StringBuilder sb = new StringBuilder();
        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String sql = "SELECT name, population FROM country ORDER BY population DESC";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(sql);
            // Process the results
            while (rset.next()) {
                String name = rset.getString("name");
                int population = rset.getInt("population");
                sb.append(name).append("\t").append(population).append("\n");
            }
            System.out.println(sb.toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get details.");
        }
    }

    /**
     * Connect to the MySQL database.
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
     * Disconnect from the MySQL database.
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
}
