package com.stats.lolgg.fileTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class FileNameValidator {

    @Test
    void testRofl(){
        String[] validFileNames = {"1t_0503_2045.rofl", "123_1234_5678.rofl", "a0_0000_9999.rofl", "test_1234_5678.rofl"};
        String[] invalidFileNames = {"rofl.rofl.rofl", "12345_a1234_5678.rofl", "1_1234_5678.text","test_test_1234_5678.rofl"};


        String regexp = "^[a-zA-Z0-9]*_\\d{4}_\\d{4}\\.rofl$";
        for(String filename : validFileNames) {
            assertTrue(filename.matches(regexp));
        }

        for(String filename : invalidFileNames) {
            System.out.println(filename);
            assertFalse(filename.matches(regexp));
        }


    }
    
}
