package com.napier.airelux;

import java.io.*;
import java.sql.*;
import java.util.Scanner;

class countryreports {
    /**
     * Connection to MySQL database.
     */
    private static Connection con = null;

    public static void main(String[] args) {
        // Create an instance of ReportSelector
        countryreports app = new countryreports();

        // Connect to the database
        if (args.length < 1) {
            // Local
            app.connect("localhost:33060", 0);
        } else {
            // Docker parameters passed from Dockerfile
            app.connect(args[0], Integer.parseInt(args[1]));
        }

        // Show only the Country Population Reports submenu
        System.out.println("Country Population Reports Feature Branch");
        app.handleCountryReports(new Scanner(System.in));

        // Disconnect from the database
        app.disconnect();
    }

    /**
     * Handles the Country Population Reports submenu.
     */
    private void handleCountryReports(Scanner scanner) {
        while (true) {
            System.out.println("Country Population Reports:");
            System.out.println("1. Countries sorted by population.");
            System.out.println("2. Countries filtered by continent.");
            System.out.println("3. Countries filtered by region.");
            System.out.println("4. Top N countries by population.");
            System.out.println("5. Exit");
            System.out.println();

            System.out.print("Please choose a report to run: ");
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());

                switch (choice) {
                    case 1:
                        runReport(scanner, "Countries sorted by population",
                                "SELECT code, name, continent, region, population, capital FROM country ORDER BY population DESC");
                        break;
                    case 2:
                        filterCountriesByContinent(scanner);
                        break;
                    case 3:
                        filterCountriesByRegion(scanner);
                        break;
                    case 4:
                        displayTopNCountries(scanner);
                        break;
                    case 5:
                        System.out.println("Exiting the program. Goodbye!");
                        return;
                    default:
                        System.out.println("Invalid input. Please enter a number between 1 and 5.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    private void filterCountriesByRegion(Scanner scanner) {
        System.out.print("Please enter the region you'd like to filter by: ");
        String region = scanner.nextLine().trim();

        if (region.isEmpty()) {
            System.out.println("Region cannot be empty. Please try again.");
            return;
        }

        String query = "SELECT code, name, continent, region, population, capital " +
                "FROM country WHERE region = ? ORDER BY population DESC";

        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, region);

            System.out.println("\nRunning Report: Countries filtered by region: " + region);
            ResultSet rset;
            rset = pstmt.executeQuery();

            // Save user input to file
            saveToFile("user_input.txt", "Region Filter: " + region + "\n");
            printResultSet(rset); // Save the query results
        } catch (SQLException | IOException e) {
            System.out.println("Failed to execute query. Error: " + e.getMessage());
        }
    }

    private void filterCountriesByContinent(Scanner scanner) {
        System.out.println("Select a continent:");
        System.out.println("1. Africa");
        System.out.println("2. Asia");
        System.out.println("3. Europe");
        System.out.println("4. North America");
        System.out.println("5. Oceania");
        System.out.println("6. South America");
        System.out.print("Enter the number corresponding to your choice: ");

        try {
            int continentChoice = Integer.parseInt(scanner.nextLine().trim());
            String continent = switch (continentChoice) {
                case 1 -> "Africa";
                case 2 -> "Asia";
                case 3 -> "Europe";
                case 4 -> "North America";
                case 5 -> "Oceania";
                case 6 -> "South America";
                default -> null;
            };

            if (continent == null) {
                System.out.println("Invalid input. Please enter a number between 1 and 6.");
                return;
            }

            String query = "SELECT code, name, continent, region, population, capital " +
                    "FROM country WHERE continent = ? ORDER BY population DESC";

            try (PreparedStatement pstmt = con.prepareStatement(query)) {
                pstmt.setString(1, continent);

                System.out.println("\nRunning Report: Countries filtered by continent: " + continent);
                ResultSet rset = pstmt.executeQuery();

                // Save user input to file
                saveToFile("user_input.txt", "Continent Filter: " + continent + "\n");
                printResultSet(rset); // Save the query results
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
        } catch (SQLException | IOException e) {
            System.out.println("Failed to execute query. Error: " + e.getMessage());
        }
    }

    private void displayTopNCountries(Scanner scanner) {
        System.out.print("Enter the number of countries you want to display (N): ");
        try {
            int n = Integer.parseInt(scanner.nextLine().trim());
            if (n > 0) {
                String query = "SELECT name, population FROM country ORDER BY population DESC LIMIT ?";
                try (PreparedStatement pstmt = con.prepareStatement(query)) {
                    pstmt.setInt(1, n);

                    System.out.println("\nRunning Report: Top " + n + " countries by population");
                    ResultSet rset = pstmt.executeQuery();

                    // Save user input to file
                    saveToFile("user_input.txt", "Top N Countries: " + n + "\n");
                    printResultSet(rset); // Save the query results
                }
            } else {
                System.out.println("Please enter a positive number.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
        } catch (SQLException | IOException e) {
            System.out.println("Failed to execute query. Error: " + e.getMessage());
        }
    }

    private void runReport(Scanner scanner, String reportName, String query) {
        try {
            System.out.println("\nRunning Report: " + reportName);
            Statement stmt = con.createStatement();
            ResultSet rset = stmt.executeQuery(query);

            // Save user input to file
            saveToFile("user_input.txt", "Report Name: " + reportName + "\n");
            printResultSet(rset); // Save the query results
        } catch (SQLException | IOException e) {
            System.out.println("Failed to execute query. Error: " + e.getMessage());
        }
    }

    private void printResultSet(ResultSet rset) throws SQLException, IOException {
        StringBuilder sb = new StringBuilder();
        ResultSetMetaData metaData = rset.getMetaData();

        // Append column headers
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            sb.append(metaData.getColumnName(i)).append("\t");
        }
        sb.append("\n");

        // Append rows
        while (rset.next()) {
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                sb.append(rset.getString(i)).append("\t");
            }
            sb.append("\n");
        }

        // Print and save results
        String resultString = sb.length() > 0 ? sb.toString() : "No results found.";
        System.out.println(resultString);
        saveToFile("report.txt", resultString); // Save to file
    }

    private void saveToFile(String fileName, String content) throws IOException {
        File outputDir = new File("./output/");
        if (!outputDir.exists()) {
            outputDir.mkdir(); // Create the output directory if it doesn't exist
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(outputDir, fileName), true))) {
            writer.write(content);
        }
    }

    /**
     * Connect to the MySQL database.
     */
    public void connect(String conString, int delay) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://" + conString + "/world?allowPublicKeyRetrieval=true&useSSL=false", "root", "example");
            System.out.println("Successfully connected.");
        } catch (Exception e) {
            System.out.println("Failed to connect to the database: " + e.getMessage());
        }
    }

    /**
     * Disconnect from the MySQL database.
     */
    public void disconnect() {
        try {
            if (con != null) {
                con.close();
                System.out.println("Disconnected from the database.");
            }
        } catch (SQLException e) {
            System.out.println("Error closing connection to database: " + e.getMessage());
        }
    }
}
