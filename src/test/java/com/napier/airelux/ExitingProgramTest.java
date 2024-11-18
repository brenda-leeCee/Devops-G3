package com.napier.airelux;

import org.junit.jupiter.api.Test;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ExitingProgramTest {

    @Test
    void testExitProgram() {
        ReportSelector reportSelector = new ReportSelector();
        Scanner scanner = new Scanner("exit\n");
        assertDoesNotThrow(() -> reportSelector.main(new String[0]), "Program should exit gracefully without exceptions.");
    }
}
