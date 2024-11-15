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

        String reportOptionNumber;
        int reportOptionNum;

        switch (reportCategoryNum) {
            case 1: // Country Reports
                reportOptionNumber = JOptionPane.showInputDialog("Country Reports:\n"
                        + "1. Countries sorted by population.\n"
                        + "2. Filter by continent and region.\n"
                        + "3. Top N countries based on population.");
                reportOptionNum = Integer.parseInt(reportOptionNumber);
                switch (reportOptionNum) {
                    case 1:
                        runCountryReport("sortedByPopulation");
                        break;
                    case 2:
                        runCountryReport("continentRegionFilter");
                        break;
                    case 3:
                        runCountryReport("topNCountries");
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Invalid option for Country Reports.");
                }
                break;
            case 2: // City Reports
                reportOptionNumber = JOptionPane.showInputDialog("City Reports:\n"
                        + "1. Cities sorted by population.\n"
                        + "2. Filter by continent, region, country, and district.\n"
                        + "3. Top N cities based on population.");
                reportOptionNum = Integer.parseInt(reportOptionNumber);
                switch (reportOptionNum) {
                    case 1:
                        runCityReport("sortedByPopulation");
                        break;
                    case 2:
                        runCityReport("filteredCities");
                        break;
                    case 3:
                        runCityReport("topNCities");
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Invalid option for City Reports.");
                }
                break;
            case 3: // Capital City Reports
                reportOptionNumber = JOptionPane.showInputDialog("Capital City Reports:\n"
                        + "1. Capital cities sorted by population.\n"
                        + "2. Filter by continent and region.\n"
                        + "3. Top N capital cities by population.");
                reportOptionNum = Integer.parseInt(reportOptionNumber);
                switch (reportOptionNum) {
                    case 1:
                        runCapitalCityReport("sortedByPopulation");
                        break;
                    case 2:
                        runCapitalCityReport("continentRegionFilter");
                        break;
                    case 3:
                        runCapitalCityReport("topNCapitalCities");
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Invalid option for Capital City Reports.");
                }
                break;
            case 4: // Population Reports
                reportOptionNumber = JOptionPane.showInputDialog("Population Reports:\n"
                        + "1. Total population and city populations by continents, regions, and countries.\n"
                        + "2. Percentages of populations within and outside cities.");
                reportOptionNum = Integer.parseInt(reportOptionNumber);
                switch (reportOptionNum) {
                    case 1:
                        runPopulationReport("totalAndCityPopulations");
                        break;
                    case 2:
                        runPopulationReport("urbanRuralPercentages");
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Invalid option for Population Reports.");
                }
                break;
            case 5: // Language Reports
                reportOptionNumber = JOptionPane.showInputDialog("Language Reports:\n"
                        + "1. Population speaking each specified language.\n"
                        + "2. Percentage of the world population speaking each language.");
                reportOptionNum = Integer.parseInt(reportOptionNumber);
                switch (reportOptionNum) {
                    case 1:
                        runLanguageReport("populationByLanguage");
                        break;
                    case 2:
                        runLanguageReport("languagePopulationPercentage");
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Invalid option for Language Reports.");
                }
                break;
            default:
                JOptionPane.showMessageDialog(null, "Invalid category selected.");
                break;
        }
    }

    // Sample methods to execute each report type
    public void runCountryReport(String reportType) {
        String sql = "";

        switch (reportType) {
            case "sortedByPopulation":
                sql = "SELECT code, name, continent, region, population, capital FROM country ORDER BY population DESC;";
                break;
            case "continentRegionFilter":
                // Add query for filtering by continent and region if needed
                break;
            case "topNCountries":
                // Add query for top N countries by population if needed
                break;
            default:
                System.out.println("Invalid report type for Country Reports.");
                return;
        }

        try {
            Statement stmt = con.createStatement();
            ResultSet rset = stmt.executeQuery(sql);
            StringBuilder sb = new StringBuilder();

            while (rset.next()) {
                String code = rset.getString("code");
                String name = rset.getString("name");
                String continent = rset.getString("continent");
                String region = rset.getString("region");
                int population = rset.getInt("population");
                int capital = rset.getInt("capital");

                sb.append(code).append("\t")
                        .append(name).append("\t")
                        .append(continent).append("\t")
                        .append(region).append("\t")
                        .append(population).append("\t")
                        .append(capital).append("\r\n");
            }

            new File("./output/").mkdir();
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File("./output/country_report_sortedByPopulation.txt")));
            writer.write(sb.toString());
            writer.close();
            System.out.println(sb.toString());

        } catch (SQLException | IOException e) {
            System.out.println("Failed to generate country report: " + e.getMessage());
        }
    }

    public void runCityReport(String reportType) {
        // Implement query logic here based on reportType
    }

    public void runCapitalCityReport(String reportType) {
        // Implement query logic here based on reportType
    }

    public void runPopulationReport(String reportType) {
        // Implement query logic here based on reportType
    }

    public void runLanguageReport(String reportType) {
        // Implement query logic here based on reportType
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
