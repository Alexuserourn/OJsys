package com.example.ojsystem.test;

import java.util.Arrays;

/**
 * 测试工具类
 */
public class TestUtils {
    
    /**
     * 打印数组内容
     * @param array 数组
     * @return 数组的字符串表示
     */
    public static String arrayToString(int[] array) {
        return Arrays.toString(array);
    }
    
    /**
     * 比较两个数组是否相等
     * @param expected 期望的数组
     * @param actual 实际的数组
     * @return 是否相等
     */
    public static boolean arraysEqual(int[] expected, int[] actual) {
        if (expected == null && actual == null) return true;
        if (expected == null || actual == null) return false;
        if (expected.length != actual.length) return false;
        
        for (int i = 0; i < expected.length; i++) {
            if (expected[i] != actual[i]) return false;
        }
        
        return true;
    }
} 