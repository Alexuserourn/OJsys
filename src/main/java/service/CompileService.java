package service;

import compile.CompileRequest;
import compile.CompileResponse;

/**
 * 代码编译服务接口
 */
public interface CompileService {
    /**
     * 编译并运行代码
     * @param request 编译请求
     * @return 编译响应
     */
    CompileResponse compileAndRun(CompileRequest request);
} 