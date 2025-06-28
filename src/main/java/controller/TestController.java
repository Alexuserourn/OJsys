package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import problem.ProblemDAO;

import java.util.HashMap;
import java.util.Map;

@RestController
public class TestController {

    @Autowired
    private ProblemDAO problemDAO;

    @GetMapping("/test")
    public String test() {
        return "应用程序正在运行!";
    }
    
    @GetMapping("/api/status")
    public Map<String, Object> status() {
        Map<String, Object> result = new HashMap<>();
        result.put("appStatus", "running");
        
        try {
            // 尝试执行一个数据库查询来测试连接
            int count = problemDAO.countProblems();
            result.put("dbStatus", "connected");
            result.put("problemCount", count);
        } catch (Exception e) {
            result.put("dbStatus", "error");
            result.put("dbError", e.getMessage());
        }
        
        return result;
    }
} 