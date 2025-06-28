package com.example.ojsystem.test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 简单测试类，不依赖Spring Boot
 */
public class SimpleTest {

    @Test
    public void testAddition() {
        assertEquals(4, 2 + 2, "2 + 2应该等于4");
    }
    
    @Test
    public void testSubtraction() {
        assertEquals(0, 2 - 2, "2 - 2应该等于0");
    }
    
    @Test
    public void testMultiplication() {
        assertEquals(4, 2 * 2, "2 * 2应该等于4");
    }
    
    @Test
    public void testDivision() {
        assertEquals(1, 2 / 2, "2 / 2应该等于1");
    }
    
    @Test
    public void testArrayEquality() {
        int[] array1 = {1, 2, 3};
        int[] array2 = {1, 2, 3};
        int[] array3 = {3, 2, 1};
        
        assertTrue(TestUtils.arraysEqual(array1, array2), "array1和array2应该相等");
        assertFalse(TestUtils.arraysEqual(array1, array3), "array1和array3不应该相等");
    }
} 