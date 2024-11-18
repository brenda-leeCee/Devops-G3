package com.napier.airelux;

import org.junit.jupiter.api.Test;

class DatabaseConnectionTest {

    @Test
    void testDatabaseConnectionSuccess() {
        ReportSelector reportSelector = new ReportSelector();
        reportSelector.connect("localhost:33060", 0);
        assertNotNull(reportSelector.getConnection(), "Connection should be established successfully.");
        reportSelector.disconnect();
    }

    @Test
    void testDatabaseConnectionFailure() {
        ReportSelector reportSelector = new ReportSelector();
        reportSelector.connect("invalid-host", 0);
        assertNull(reportSelector.getConnection(), "Connection should fail and return null.");
    }
}
