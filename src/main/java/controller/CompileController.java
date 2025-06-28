package controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import compile.CompileRequest;
import compile.CompileResponse;
import service.CompileService;

/**
 * 代码编译控制器
 * 处理代码编译请求
 */
@RestController
@RequestMapping("/compile")
public class CompileController {
    private final Logger logger = LoggerFactory.getLogger(CompileController.class);
    private final CompileService compileService;
    
    @Autowired
    public CompileController(CompileService compileService) {
        this.compileService = compileService;
    }
    
    /**
     * 处理代码编译请求
     * @param compileRequest 编译请求
     * @return 编译结果
     */
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<CompileResponse> compile(@RequestBody CompileRequest compileRequest) {
        logger.info("收到编译请求，题目ID: {}", compileRequest.getId());
        logger.debug("用户提交的代码:\n{}", compileRequest.getCode());
        
        CompileResponse response = compileService.compileAndRun(compileRequest);
        
        logger.info("编译结果: errno={}, 原因={}, 输出={}",
                response.getErrno(),
                response.getReason() != null ? "非空" : "空",
                response.getStdout() != null ? "非空" : "空");
        
        if (response.getReason() != null) {
            logger.debug("编译/运行错误信息: {}", response.getReason());
        }
        
        if (response.getStdout() != null) {
            logger.debug("标准输出: {}", response.getStdout());
        }
        
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
} 