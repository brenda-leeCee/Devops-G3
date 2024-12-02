package com.napier.airelux;

import org.junit.jupiter.api.*;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class IntegrationTest {

    private static ReportSelector reportSelector;

    @BeforeAll
    public static void setup() {
        // Initialize the ReportSelector instance
        reportSelector = new ReportSelector();
    }

    @BeforeEach
    public void connectToDatabase() {
        // Ensure connection before each test
        reportSelector.connect("localhost:33060", 0);
        assertNotNull(reportSelector.getConnection(), "Database connection should be established.");
    }

    @AfterEach
    public void disconnectFromDatabase() {
        // Disconnect after each test
        reportSelector.disconnect();
        assertNull(reportSelector.getConnection(), "Database connection should be closed.");
    }

    @Test
    public void testDatabaseConnection() {
        // Verify connection
        assertNotNull(reportSelector.getConnection(), "Connection should not be null for valid parameters.");
    }

    @Test
    public void testInvalidDatabaseConnection() {
        // Test connection with invalid parameters
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            reportSelector.connect("invalid-host:3306", 0);
        });
        assertTrue(exception.getMessage().contains("Failed to connect"),
                "Error message should indicate connection failure.");
    }

    @Test
    public void testRunReportWithInvalidQuery() {
        // Test running an invalid SQL query
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            reportSelector.runReport("SELECT * FROM invalid_table");
        });
        assertTrue(exception.getMessage().contains("Error executing query"),
                "Error message should indicate query execution error.");
    }

    @Test
    public void testRunReportWithEmptyQuery() {
        // Test running an empty query
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            reportSelector.runReport("");
        });
        assertTrue(exception.getMessage().contains("Query cannot be null or empty"),
                "Error message should indicate that query is null or empty.");
    }

    @Test
    public void testRunReportWithNullQuery() {
        // Test running a null query
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            reportSelector.runReport(null);
        });
        assertTrue(exception.getMessage().contains("Query cannot be null or empty"),
                "Error message should indicate that query is null or empty.");
    }

    @Test
    public void testReconnectToDatabase() {
        // Test reconnecting to the database
        reportSelector.disconnect();
        assertNull(reportSelector.getConnection(), "Connection should be null after disconnect.");
        reportSelector.connect("localhost:33060", 0);
        assertNotNull(reportSelector.getConnection(), "Connection should be re-established.");
    }

    @Test
    public void testQueryExecutionTime() {
        // Test the time it takes to execute a query
        assertTimeoutPreemptively(Duration.ofSeconds(5), () -> {
            reportSelector.runReport("SELECT * FROM country");
        }, "Query execution should complete within 5 seconds.");
    }

    @AfterAll
    public static void cleanup() {
        // Final cleanup to ensure disconnection
        reportSelector.disconnect();
    }
}
