package com.napier.airelux;

import org.junit.jupiter.api.Test;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class InvalidUserInputTest {

    @Test
    void testHandleInvalidMenuInput() {
        ReportSelector reportSelector = new ReportSelector();
        Scanner scanner = new Scanner("invalid\n7\n");
        assertDoesNotThrow(() -> reportSelector.showReportSelection(scanner), "Invalid input should not cause exceptions.");
    }
}
