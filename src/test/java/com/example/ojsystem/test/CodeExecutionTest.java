package com.example.ojsystem.test;

import com.example.ojsystem.model.ExecutionResult;
import com.example.ojsystem.service.CodeExecutionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 代码执行服务测试类
 * 注意：这个测试类需要JDK编译器，只有在运行环境有JDK时才能通过
 */
public class CodeExecutionTest {
    
    private CodeExecutionService codeExecutionService;
    
    @BeforeEach
    public void setUp() {
        codeExecutionService = new CodeExecutionService();
    }
    
    // 测试用例数据
    private static final String CORRECT_CODE = 
        "class Solution {\n" +
        "    public static int[] twoSum(int[] nums, int target) {\n" +
        "        for (int i = 0; i < nums.length; i++) {\n" +
        "            for (int j = i + 1; j < nums.length; j++) {\n" +
        "                if (nums[i] + nums[j] == target) {\n" +
        "                    return new int[] {i, j};\n" +
        "                }\n" +
        "            }\n" +
        "        }\n" +
        "        return null;\n" +
        "    }\n" +
        "}";
        
    private static final String COMPILE_ERROR_CODE = 
        "class Solution {\n" +
        "    public static int[] twoSum(int[] nums, int target) {\n" +
        "        for (int i = 0; i < nums.length; i++) {\n" +
        "            for (int j = i + 1; j < nums.length; j++) {\n" +
        "                if (nums[i] + nums[j] == target) {\n" +
        "                    return new int[] {i, j}\n" + // 缺少分号
        "                }\n" +
        "            }\n" +
        "        }\n" +
        "        return null;\n" +
        "    }\n" +
        "}";
        
    private static final String RUNTIME_ERROR_CODE = 
        "class Solution {\n" +
        "    public static int[] twoSum(int[] nums, int target) {\n" +
        "        nums = null;\n" +
        "        return new int[] {nums[0], nums[1]};\n" +
        "    }\n" +
        "}";
        
    private static final String TIMEOUT_CODE = 
        "class Solution {\n" +
        "    public static boolean isPalindrome(int x) {\n" +
        "        try {\n" +
        "            Thread.sleep(10000);\n" +
        "        } catch (Exception e) {}\n" +
        "        return true;\n" +
        "    }\n" +
        "}";
        
    private static final String CHINESE_CODE = 
        "class Solution {\n" +
        "    public static String test() {\n" +
        "        return \"测试中文输出\";\n" +
        "    }\n" +
        "}";
    
    @Test
    @Disabled("需要JDK环境才能运行")
    public void testCorrectCodeExecution() {
        // 准备测试数据
        int[] nums = {2, 7, 11, 15};
        int target = 9;
        
        // 执行测试
        ExecutionResult result = codeExecutionService.executeCode(CORRECT_CODE, nums, target);
        
        // 调试输出
        System.out.println("正确代码执行结果: " + result.isSuccess());
        System.out.println("错误码: " + result.getErrorCode());
        System.out.println("错误信息: " + result.getErrorMessage());
        System.out.println("输出: " + result.getOutput());
        
        // 使用多种断言验证结果
        assertNotNull(result, "执行结果不应为空");
        assertTrue(result.isSuccess(), "代码应该执行成功");
        assertEquals(0, result.getErrorCode(), "错误码应该为0");
        
        if (result.getOutput() instanceof int[]) {
            int[] output = (int[]) result.getOutput();
            assertTrue(
                (output[0] == 0 && output[1] == 1) || (output[0] == 1 && output[1] == 0),
                "输出结果应该为[0,1]或[1,0]，实际为" + TestUtils.arrayToString(output)
            );
        } else {
            fail("输出结果类型错误，应为int[]");
        }
        
        assertNull(result.getErrorMessage(), "成功执行时错误信息应为空");
    }
    
    @Test
    @Disabled("需要JDK环境才能运行")
    public void testCompileError() {
        ExecutionResult result = codeExecutionService.executeCode(COMPILE_ERROR_CODE, new int[]{}, 0);
        
        // 调试输出
        System.out.println("编译错误测试结果: " + result.isSuccess());
        System.out.println("错误码: " + result.getErrorCode());
        System.out.println("错误信息: " + result.getErrorMessage());
        
        assertNotNull(result, "执行结果不应为空");
        assertFalse(result.isSuccess(), "编译错误时不应成功");
        assertEquals(1, result.getErrorCode(), "错误码应该为1");
        assertNotNull(result.getErrorMessage(), "应该有错误信息");
        assertTrue(
            result.getErrorMessage().contains(";") || 
            result.getErrorMessage().contains("error") || 
            result.getErrorMessage().contains("错误"), 
            "错误信息应包含错误提示"
        );
    }
    
    @Test
    @Disabled("需要JDK环境才能运行")
    public void testRuntimeError() {
        ExecutionResult result = codeExecutionService.executeCode(RUNTIME_ERROR_CODE, new int[]{}, 0);
        
        // 调试输出
        System.out.println("运行时错误测试结果: " + result.isSuccess());
        System.out.println("错误码: " + result.getErrorCode());
        System.out.println("错误信息: " + result.getErrorMessage());
        
        assertNotNull(result, "执行结果不应为空");
        assertFalse(result.isSuccess(), "运行时错误时不应成功");
        assertEquals(2, result.getErrorCode(), "错误码应该为2");
        assertTrue(
            result.getErrorMessage().contains("NullPointerException") || 
            result.getErrorMessage().contains("空指针"), 
            "错误信息应包含空指针异常"
        );
    }
    
    @Test
    @Disabled("需要JDK环境才能运行")
    public void testTimeout() {
        ExecutionResult result = codeExecutionService.executeCode(TIMEOUT_CODE, 0);
        
        // 调试输出
        System.out.println("超时测试结果: " + result.isSuccess());
        System.out.println("错误码: " + result.getErrorCode());
        System.out.println("错误信息: " + result.getErrorMessage());
        
        assertNotNull(result, "执行结果不应为空");
        assertFalse(result.isSuccess(), "超时时不应成功");
        assertEquals(3, result.getErrorCode(), "错误码应该为3");
        assertTrue(
            result.getErrorMessage().contains("timeout") || 
            result.getErrorMessage().contains("超时"), 
            "错误信息应包含超时提示"
        );
    }
    
    @Test
    @Disabled("需要JDK环境才能运行")
    public void testChineseSupport() {
        ExecutionResult result = codeExecutionService.executeCode(CHINESE_CODE);
        
        // 调试输出
        System.out.println("中文支持测试结果: " + result.isSuccess());
        System.out.println("错误码: " + result.getErrorCode());
        System.out.println("错误信息: " + result.getErrorMessage());
        System.out.println("输出: " + result.getOutput());
        
        assertNotNull(result, "执行结果不应为空");
        assertTrue(result.isSuccess(), "中文代码应该执行成功");
        assertEquals("测试中文输出", result.getOutput(), "应该正确输出中文");
    }
    
    // Mock测试用例，不依赖JDK
    @Test
    public void testExecutionResultCreation() {
        // 测试成功结果
        ExecutionResult successResult = ExecutionResult.success("测试成功");
        assertTrue(successResult.isSuccess());
        assertEquals(0, successResult.getErrorCode());
        assertNull(successResult.getErrorMessage());
        assertEquals("测试成功", successResult.getOutput());
        
        // 测试编译错误结果
        ExecutionResult compileErrorResult = ExecutionResult.compileError("编译错误");
        assertFalse(compileErrorResult.isSuccess());
        assertEquals(1, compileErrorResult.getErrorCode());
        assertEquals("编译错误", compileErrorResult.getErrorMessage());
        assertNull(compileErrorResult.getOutput());
        
        // 测试运行时错误结果
        ExecutionResult runtimeErrorResult = ExecutionResult.runtimeError("运行时错误");
        assertFalse(runtimeErrorResult.isSuccess());
        assertEquals(2, runtimeErrorResult.getErrorCode());
        assertEquals("运行时错误", runtimeErrorResult.getErrorMessage());
        assertNull(runtimeErrorResult.getOutput());
        
        // 测试超时错误结果
        ExecutionResult timeoutErrorResult = ExecutionResult.timeoutError("超时错误");
        assertFalse(timeoutErrorResult.isSuccess());
        assertEquals(3, timeoutErrorResult.getErrorCode());
        assertEquals("超时错误", timeoutErrorResult.getErrorMessage());
        assertNull(timeoutErrorResult.getOutput());
    }
} 