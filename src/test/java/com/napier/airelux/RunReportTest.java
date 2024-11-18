package com.napier.airelux;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.mockito.Mockito.*;

class RunReportTest {

    @Test
    void testRunReportWithValidQuery() throws Exception {
        Connection mockConnection = Mockito.mock(Connection.class);
        Statement mockStatement = Mockito.mock(Statement.class);
        ResultSet mockResultSet = Mockito.mock(ResultSet.class);

        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getString(1)).thenReturn("Country1");

        ReportSelector reportSelector = new ReportSelector();
        reportSelector.setConnection(mockConnection);

        reportSelector.runReport(null, "Test Report", "SELECT name, population FROM country");

        verify(mockStatement).executeQuery("SELECT name, population FROM country");
    }
}
