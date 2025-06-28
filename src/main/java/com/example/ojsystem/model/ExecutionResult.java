package com.example.ojsystem.model;

/**
 * 代码执行结果类
 */
public class ExecutionResult {
    private boolean success;
    private int errorCode; // 0:成功 1:编译错误 2:运行时错误 3:超时错误
    private String errorMessage;
    private Object output;

    public ExecutionResult() {
    }

    public ExecutionResult(boolean success, int errorCode, String errorMessage, Object output) {
        this.success = success;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.output = output;
    }

    public static ExecutionResult success(Object output) {
        return new ExecutionResult(true, 0, null, output);
    }

    public static ExecutionResult compileError(String errorMessage) {
        return new ExecutionResult(false, 1, errorMessage, null);
    }

    public static ExecutionResult runtimeError(String errorMessage) {
        return new ExecutionResult(false, 2, errorMessage, null);
    }

    public static ExecutionResult timeoutError(String errorMessage) {
        return new ExecutionResult(false, 3, errorMessage, null);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Object getOutput() {
        return output;
    }

    public void setOutput(Object output) {
        this.output = output;
    }
} 