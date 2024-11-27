package com.napier.airelux;

import java.sql.*;
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

    /**
     * Set the database connection (used for unit testing).
     */
    public void setConnection(Connection connection) {
        this.con = connection;
    }

    private void showReportSelection(Scanner scanner) {
        while (true) {
            System.out.println("Main Menu:");
            System.out.println("1. Country Population Reports");
            System.out.println("2. City Population Reports");
            System.out.println("3. Capital City Reports");
            System.out.println("4. Population Analysis Reports");
            System.out.println("5. City Reports");
            System.out.println("6. Language Reports");
            System.out.println("7. Exit");
            System.out.println();
            System.out.print("Please enter the number of the report category you wish to select: ");


            try {
                int reportCategoryNum = Integer.parseInt(scanner.nextLine().trim());

                switch (reportCategoryNum) {
                    case 1:
                        handleCountryReports(scanner);
                        break;
                    case 2:
                        handleCityPopulationReports(scanner);
                        break;
                    case 3:
                        handleCapitalCityReports(scanner);
                        break;
                    case 4:
                        handlePopulationReports(scanner);
                        break;
                    case 5:
                        handleCityReports(scanner);
                        break;
                    case 6:
                        handleLanguageReports(scanner);
                        break;
                    case 7:
                        System.out.println("Exiting the program. Goodbye!");
                        return;
                    default:
                        System.out.println("Invalid input. Please enter a number between 1 and 7.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    private void handleCountryReports(Scanner scanner) {
        while (true) {
            System.out.println("Country Population Reports:");
            System.out.println("1. Countries sorted by population.");
            System.out.println("2. Countries filtered by continent.");
            System.out.println("3. Countries filtered by region.");
            System.out.println("4. Top N countries by population.");
            System.out.println("5. Back to Report Category");
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
                        System.out.print("Enter the number of countries you want to display (N): ");
                        try {
                            int n = Integer.parseInt(scanner.nextLine().trim());
                            if (n > 0) {
                                runReport(scanner, "Top " + n + " countries by population",
                                        "SELECT name, population FROM country ORDER BY population DESC LIMIT ?");
                            } else {
                                System.out.println("Please enter a positive number.");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input. Please enter a valid number.");
                        }
                        break;
                    case 5:
                        return; // Back to main menu
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
                "FROM country " +
                "WHERE region = ? " +
                "ORDER BY population DESC";

        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, region);

            System.out.println("\nRunning Report: Countries filtered by region: " + region);
            ResultSet rset = pstmt.executeQuery();
            printResultSet(rset);
        } catch (SQLException e) {
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
                    "FROM country " +
                    "WHERE continent = ? " +
                    "ORDER BY population DESC";

            try (PreparedStatement pstmt = con.prepareStatement(query)) {
                pstmt.setString(1, continent);

                System.out.println("\nRunning Report: Countries filtered by continent: " + continent);
                ResultSet rset = pstmt.executeQuery();
                printResultSet(rset);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
        } catch (SQLException e) {
            System.out.println("Failed to execute query. Error: " + e.getMessage());
        }
    }

    private void printResultSet(ResultSet rset) throws SQLException {
        StringBuilder sb = new StringBuilder();
        while (rset.next()) {
            ResultSetMetaData metaData = rset.getMetaData();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                sb.append(rset.getString(i)).append("\t");
            }
            sb.append("\n");
        }

        if (sb.length() > 0) {
            System.out.println("Results:");
            System.out.println(sb.toString());
        } else {
            System.out.println("No results found.");
        }
    }


    private void handleCityPopulationReports(Scanner scanner) {
        while (true) {
            System.out.println("City Population Reports:");
            System.out.println("1. Population of cities by continent.");
            System.out.println("2. Population of cities by region.");
            System.out.println("3. Population of cities by country.");
            System.out.println("4. Back to Report Category");
            System.out.println();


            System.out.print("Please choose a report to run: ");
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());

                switch (choice) {
                    case 1: // Population of cities by continent
                        System.out.print("Enter the continent name: ");
                        String continent = scanner.nextLine().trim();
                        runReport(scanner, "Population of cities by continent: " + continent,
                                "SELECT city.Name, city.Population " +
                                        "FROM city " +
                                        "JOIN country ON city.CountryCode = country.Code " +
                                        "WHERE country.Continent = ? " +
                                        "ORDER BY city.Population DESC");
                        break;
                    case 2: // Population of cities by region
                        System.out.print("Enter the region name: ");
                        String region = scanner.nextLine().trim();
                        runReport(scanner, "Population of cities by region: " + region,
                                "SELECT city.Name, city.Population " +
                                        "FROM city " +
                                        "JOIN country ON city.CountryCode = country.Code " +
                                        "WHERE country.Region = ? " +
                                        "ORDER BY city.Population DESC");
                        break;
                    case 3: // Population of cities by country
                        System.out.print("Enter the country code: ");
                        String countryCode = scanner.nextLine().trim();
                        runReport(scanner, "Population of cities by country: " + countryCode,
                                "SELECT city.Name, city.Population " +
                                        "FROM city " +
                                        "WHERE city.CountryCode = ? " +
                                        "ORDER BY city.Population DESC");
                        break;
                    case 4: // Back to Report Category
                        return;
                    default:
                        System.out.println("Invalid input. Please enter a number between 1 and 4.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    private void handleCapitalCityReports(Scanner scanner) {
        while (true) {
            System.out.println("Capital City Reports:");
            System.out.println("1. Capital cities sorted by population.");
            System.out.println("2. Capital cities filtered by continent.");
            System.out.println("3. Capital cities filtered by region.");
            System.out.println("4. Top N capital cities by population.");
            System.out.println("5. Back to Report Category");
            System.out.println();

            System.out.print("Please choose a report to run: ");
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());

                switch (choice) {
                    case 1:
                        // Case 1: Capital cities sorted by population
                        runReport(scanner, "Capital cities sorted by population",
                                "SELECT city.Name, country.Name AS Country, city.Population " +
                                        "FROM city " +
                                        "JOIN country ON city.ID = country.Capital " +
                                        "ORDER BY city.Population DESC");
                        break;

                    case 2:
                        // Case 2: Capital cities filtered by continent
                        System.out.print("Enter the continent: ");
                        String continent = scanner.nextLine().trim();
                        runReport(scanner, "Capital cities filtered by continent: " + continent,
                                "SELECT city.Name, country.Name AS Country, city.Population " +
                                        "FROM city " +
                                        "JOIN country ON city.ID = country.Capital " +
                                        "WHERE country.Continent = ? " +
                                        "ORDER BY city.Population DESC");
                        break;

                    case 3:
                        // Case 3: Capital cities filtered by region
                        System.out.print("Enter the region: ");
                        String region = scanner.nextLine().trim();
                        runReport(scanner, "Capital cities filtered by region: " + region,
                                "SELECT city.Name, country.Name AS Country, city.Population " +
                                        "FROM city " +
                                        "JOIN country ON city.ID = country.Capital " +
                                        "WHERE country.Region = ? " +
                                        "ORDER BY city.Population DESC");
                        break;

                    case 4:
                        // Case 4: Top N capital cities by population
                        System.out.print("Enter the number of top capital cities to display (N): ");
                        try {
                            int n = Integer.parseInt(scanner.nextLine().trim());
                            if (n > 0) {
                                runReport(scanner, "Top " + n + " capital cities by population",
                                        "SELECT city.Name, country.Name AS Country, city.Population " +
                                                "FROM city " +
                                                "JOIN country ON city.ID = country.Capital " +
                                                "ORDER BY city.Population DESC " +
                                                "LIMIT ?");
                            } else {
                                System.out.println("Please enter a positive number.");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input. Please enter a valid number.");
                        }
                        break;

                    case 5:
                        // Case 5: Back to the main menu
                        return;

                    default:
                        System.out.println("Invalid input. Please enter a number between 1 and 5.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    private void handlePopulationReports(Scanner scanner) {
        while (true) {
            System.out.println("Population Analysis Reports:");
            System.out.println("1. Total population by continent.");
            System.out.println("2. Total population by region.");
            System.out.println("3. Total population by country.");
            System.out.println("4. Percentages within and outside cities.");
            System.out.println("5. Back to Report Category");
            System.out.println();

            System.out.print("Please choose a report to run: ");
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());

                switch (choice) {
                    case 1:
                        // Total population by continent
                        runReport(scanner, "Total population by continent",
                                "SELECT Continent, SUM(Population) AS TotalPopulation " +
                                        "FROM country " +
                                        "GROUP BY Continent " +
                                        "ORDER BY TotalPopulation DESC");
                        break;

                    case 2:
                        // Total population by region
                        runReport(scanner, "Total population by region",
                                "SELECT Region, SUM(Population) AS TotalPopulation " +
                                        "FROM country " +
                                        "GROUP BY Region " +
                                        "ORDER BY TotalPopulation DESC");
                        break;

                    case 3:
                        // Total population by country
                        runReport(scanner, "Total population by country",
                                "SELECT Name AS Country, Population " +
                                        "FROM country " +
                                        "ORDER BY Population DESC");
                        break;

                    case 4:
                        // Percentages within and outside cities
                        runReport(scanner, "Percentages within and outside cities",
                                "SELECT country.Continent, " +
                                        "SUM(country.Population) AS TotalPopulation, " +
                                        "SUM(city.Population) AS PopulationInCities, " +
                                        "(SUM(city.Population) / SUM(country.Population) * 100) AS PercentInCities, " +
                                        "(1 - (SUM(city.Population) / SUM(country.Population)) * 100) AS PercentOutsideCities " +
                                        "FROM country " +
                                        "LEFT JOIN city ON country.Code = city.CountryCode " +
                                        "GROUP BY country.Continent " +
                                        "ORDER BY TotalPopulation DESC");
                        break;

                    case 5:
                        // Back to the main menu
                        return;

                    default:
                        System.out.println("Invalid input. Please enter a number between 1 and 5.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }


    private void handleCityReports(Scanner scanner) {
        while (true) {
            System.out.println("City Reports:");
            System.out.println("1. Cities sorted by population.");
            System.out.println("2. Cities filtered by continent.");
            System.out.println("3. Cities filtered by region.");
            System.out.println("4. Cities filtered by country.");
            System.out.println("5. Cities filtered by district.");
            System.out.println("6. Top N cities by population.");
            System.out.println("7. Back to Report Category");
            System.out.println();

            System.out.print("Please choose a report to run: ");
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());

                switch (choice) {
                    case 1:
                        // Cities sorted by population
                        runReport(scanner, "Cities sorted by population",
                                "SELECT Name, Population FROM city ORDER BY Population DESC");
                        break;

                    case 2:
                        // Cities filtered by continent
                        System.out.print("Enter the continent: ");
                        String continent = scanner.nextLine().trim();
                        runReport(scanner, "Cities filtered by continent: " + continent,
                                "SELECT city.Name, city.Population " +
                                        "FROM city " +
                                        "JOIN country ON city.CountryCode = country.Code " +
                                        "WHERE country.Continent = ? " +
                                        "ORDER BY city.Population DESC");
                        break;

                    case 3:
                        // Cities filtered by region
                        System.out.print("Enter the region: ");
                        String region = scanner.nextLine().trim();
                        runReport(scanner, "Cities filtered by region: " + region,
                                "SELECT city.Name, city.Population " +
                                        "FROM city " +
                                        "JOIN country ON city.CountryCode = country.Code " +
                                        "WHERE country.Region = ? " +
                                        "ORDER BY city.Population DESC");
                        break;

                    case 4:
                        // Cities filtered by country
                        System.out.print("Enter the country code (e.g., USA): ");
                        String countryCode = scanner.nextLine().trim();
                        runReport(scanner, "Cities filtered by country: " + countryCode,
                                "SELECT Name, Population " +
                                        "FROM city " +
                                        "WHERE CountryCode = ? " +
                                        "ORDER BY Population DESC");
                        break;

                    case 5:
                        // Cities filtered by district
                        System.out.print("Enter the district: ");
                        String district = scanner.nextLine().trim();
                        runReport(scanner, "Cities filtered by district: " + district,
                                "SELECT Name, Population " +
                                        "FROM city " +
                                        "WHERE District = ? " +
                                        "ORDER BY Population DESC");
                        break;

                    case 6:
                        // Top N cities by population
                        System.out.print("Enter the number of top cities to display (N): ");
                        try {
                            int n = Integer.parseInt(scanner.nextLine().trim());
                            if (n > 0) {
                                runReport(scanner, "Top " + n + " cities by population",
                                        "SELECT Name, Population " +
                                                "FROM city " +
                                                "ORDER BY Population DESC " +
                                                "LIMIT ?");
                            } else {
                                System.out.println("Please enter a positive number.");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input. Please enter a valid number.");
                        }
                        break;

                    case 7:
                        // Back to main menu
                        return;

                    default:
                        System.out.println("Invalid input. Please enter a number between 1 and 7.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }


    private void handleLanguageReports(Scanner scanner) {
        while (true) {
            System.out.println("Language Reports:");
            System.out.println("1. Population speaking each language.");
            System.out.println("2. Percentage of the world population speaking each language.");
            System.out.println("3. Back to Report Category");
            System.out.println();

            System.out.print("Please choose a report to run: ");
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());

                switch (choice) {
                    case 1:
                        // Population speaking each language
                        runReport(scanner, "Population speaking each language",
                                "SELECT Language, SUM(country.Population * countrylanguage.Percentage / 100) AS TotalSpeakers " +
                                        "FROM countrylanguage " +
                                        "JOIN country ON country.Code = countrylanguage.CountryCode " +
                                        "WHERE Language IN ('Chinese', 'English', 'Hindi', 'Spanish', 'Arabic') " +
                                        "GROUP BY Language " +
                                        "ORDER BY TotalSpeakers DESC");
                        break;

                    case 2:
                        // Percentage of the world population speaking each language
                        runReport(scanner, "Percentage of the world population speaking each language",
                                "SELECT Language, " +
                                        "SUM(country.Population * countrylanguage.Percentage / 100) / " +
                                        "(SELECT SUM(Population) FROM country) * 100 AS Percentage " +
                                        "FROM countrylanguage " +
                                        "JOIN country ON country.Code = countrylanguage.CountryCode " +
                                        "WHERE Language IN ('Chinese', 'English', 'Hindi', 'Spanish', 'Arabic') " +
                                        "GROUP BY Language " +
                                        "ORDER BY Percentage DESC");
                        break;

                    case 3:
                        // Back to the main menu
                        return;

                    default:
                        System.out.println("Invalid input. Please enter a number between 1 and 3.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    private void runReport(Scanner scanner, String reportName, String query) {
        while (true) {
            System.out.println("\nRunning Report: " + reportName);
            StringBuilder sb = new StringBuilder();

            try {
                Statement stmt = con.createStatement();
                ResultSet rset = stmt.executeQuery(query);

                while (rset.next()) {
                    ResultSetMetaData metaData = rset.getMetaData();
                    for (int i = 1; i <= metaData.getColumnCount(); i++) {
                        sb.append(rset.getString(i)).append("\t");
                    }
                    sb.append("\n");
                }
                System.out.println("Results:");
                System.out.println(sb.toString());
            } catch (SQLException e) {
                System.out.println("Failed to execute query. Error: " + e.getMessage());
            }

            System.out.println("\nWhat would you like to do next?");
            System.out.println("1. Run Report Again");
            System.out.println("2. Back to Report Category");
            System.out.print("Enter your choice: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                switch (choice) {
                    case 1:
                        continue; // Run the report again
                    case 2:
                        return; // Back to the submenu
                    default:
                        System.out.println("Invalid input. Please enter 1 or 2.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
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
            } catch (SQLException e) {
                System.out.println("Error closing connection to database");
            }
        }
    }

    public Object getConnection() {
        return con;
    }
}
