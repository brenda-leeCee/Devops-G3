package com.napier.airelux;

import org.junit.jupiter.api.*;

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
    public void testRunReport() {
        // Verify that a query runs without errors
        assertDoesNotThrow(() -> reportSelector.runReport("SELECT * FROM country LIMIT 5"),
                "The query should execute without throwing exceptions.");
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

    @AfterAll
    public static void cleanup() {
        // Final cleanup to ensure disconnection
        reportSelector.disconnect();
    }
}