Population Information Reporting System

Use Case Title:

Population Information Reporting System for Country, City, and Capital Data



Description:

This use case describes the process of designing and implementing a system for generating various population reports from a pre-existing SQL database (world database). The reports will allow the organization to access and analyze population data at different geographical levels, including country, continent, region, city, and language.

Use Case: Generate Country Population Report by Continent

Goal

To allow users to generate a report that lists countries within a selected continent, organized by population from largest to smallest.

Actors

User: Requests the report and specifies continent and sorting criteria.

System: Processes the request, retrieves data from the database, and formats the report for display.

Preconditions

The SQL database is accessible and contains population data for all countries.

The system’s user interface (UI) is active and allows data entry.

Trigger

The user selects the "Country Population Report by Continent" option in the report generation menu and specifies a continent.

Main Flow

User Request:

The user selects "Country Population Report by Continent" from the report menu.

The system prompts the user to specify the continent (e.g., Africa, Asia).

The user selects a continent.

System Query:

The system executes an SQL query to retrieve country data for the selected continent, ordered by population in descending order.

Report Generation:

The system formats the retrieved data according to the report structure: displaying "Country Code," "Country Name," "Continent," "Region," "Population," and "Capital."

Display Results:

The system displays the report on the UI and provides options to export it as a CSV or PDF file.

Alternative Flows

No Data Found:

If no data exists for the specified continent, the system displays an error message: "No data available for the selected continent."

Invalid Input:

If the user does not select a continent, the system prompts them to choose one before proceeding.

Postconditions

The user successfully views or downloads the population report for the specified continent.

Example Scenario

Input: The user selects "Africa" as the continent.

Expected Output: A report displaying countries in Africa ordered by population, showing each country's code, name, region, population, and capital.



Use Case: Generate Top N Populated Countries in the World

Goal

To allow users to generate a report listing the top N populated countries in the world, based on user-specified input.

Actors

User: Specifies the top N countries based on population.

System: Processes the request, retrieves data, and formats the report for display.

Preconditions

The SQL database is accessible and contains population data for all countries.

The system’s user interface (UI) is active and allows for data entry.

Trigger

The user selects "Top N Populated Countries in the World" from the report generation menu and specifies a value for N.

Main Flow

User Request:

The user selects "Top N Populated Countries in the World."

The system prompts the user to enter a numerical value for N.

The user inputs a value (e.g., 5 for the top 5 countries).

System Query:

The system executes an SQL query to retrieve country data ordered by population in descending order, limiting results to the top N based on user input.

Report Generation:

The system formats the retrieved data according to the report structure: displaying "Country Code," "Country Name," "Continent," "Region," "Population," and "Capital."

Display Results:

The system displays the report on the UI and provides export options for CSV or PDF files.

Alternative Flows

No Data Found:

If the database query returns no results, the system displays a message: "No data available for the specified criteria."

Invalid Input:

If the user enters a non-numeric value for N, the system prompts the user to enter a valid number.

Postconditions

The user views or downloads the report showing the top N populated countries worldwide.

Example Scenario

Input: The user selects "Top N Populated Countries in the World" and inputs N = 5.

Expected Output: A report showing the top 5 most populated countries, with each country's code, name, continent, region, population, and capital.

Use Case: Generate City Population Report by Country

Goal

To allow users to generate a report listing cities within a specified country, ordered by population from largest to smallest.

Actors

User: Requests the report by selecting a specific country.

System: Processes the request and retrieves the appropriate data.

Preconditions

The SQL database is accessible with population data for cities within each country.

The system’s UI is active and allows data selection.

Trigger

The user selects "City Population Report by Country" and specifies a country.

Main Flow

User Request:

The user selects "City Population Report by Country."

The system prompts the user to choose a country.

The user selects a country (e.g., "Japan").

System Query:

The system executes an SQL query to retrieve city data within the specified country, ordered by population in descending order.

Report Generation:

The system formats the retrieved data: displaying "City Name," "Country," "District," and "Population."

Display Results:

The system displays the report on the UI with export options.

Alternative Flows

No Data Found:

If no data exists for the selected country, the system displays "No data available for the specified country."

Invalid Input:

If the user does not select a country, the system prompts the user to choose one.

Postconditions

The user views or downloads a report showing cities in the specified country by population.

Example Scenario

Input: The user selects "City Population Report by Country" and chooses "Japan."

Expected Output: A report listing Japanese cities by population, showing each city's name, district, and population.

Use Case: Generate Capital City Report by Continent

Goal

To allow users to generate a report that lists capital cities within a specified continent, organized by population.

Actors

User: Requests the report and specifies the continent.

System: Retrieves data and formats the report.

Preconditions

The SQL database contains capital city population data.

The system UI allows continent selection.

Trigger

The user selects "Capital City Report by Continent" and specifies a continent.

Main Flow

User Request:

The user selects "Capital City Report by Continent."

The system prompts the user to select a continent.

The user chooses a continent (e.g., "Europe").

System Query:

The system executes an SQL query to retrieve capital cities within the selected continent, ordered by population.

Report Generation:

The system displays the "City Name," "Country," and "Population" for each capital city in the report.

Display Results:

The system shows the report with export options.

Alternative Flows

No Data Found:

If no data is available for the specified continent, the system displays "No data available for the selected continent."

Postconditions

The user can view or export a capital city report for the specified continent.

Example Scenario

Input: The user selects "Capital City Report by Continent" and chooses "Europe."

Expected Output: A report listing European capital cities by population, displaying each city's name, country, and population.

Use Case: Generate Population Summary by Continent

Goal

To allow users to view a summary of population data for each continent, including total population, city population, and non-city population with percentages.

Actors

User: Requests the summary report.

System: Processes the request and retrieves relevant data.

Preconditions

The SQL database is accessible with data on population by continent.

The UI is active and allows report requests.

Trigger

The user selects "Population Summary by Continent."

Main Flow

User Request:

The user selects "Population Summary by Continent" from the report menu.

System Query:

The system executes SQL queries to retrieve population, city population, and non-city population for each continent.

Calculates the percentage of people living in and outside cities for each continent.

Report Generation:

The system displays "Continent Name," "Total Population," "City Population," "Non-City Population," "% in Cities," and "% Not in Cities."

Display Results:

The system displays the summary on the UI and provides export options.

Alternative Flows

No Data Found:

If no data is available, the system shows "No data available for the selected criteria."

Postconditions

The user can view or download the population summary for each continent.

Example Scenario

Expected Output: A report showing each continent’s total population, population in cities, and population not in cities, with percentages for each. 

 