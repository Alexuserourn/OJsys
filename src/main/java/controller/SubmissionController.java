package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entity.Submission;
import entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.SubmissionService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 提交记录控制器
 */
@RestController
@RequestMapping("/submission")
public class SubmissionController {
    private static final Logger logger = LoggerFactory.getLogger(SubmissionController.class);
    private final SubmissionService submissionService;
    private Gson gson = new GsonBuilder().create();

    @Autowired
    public SubmissionController(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    /**
     * 获取当前用户的提交记录
     * @param request HTTP请求
     * @return 提交记录列表
     */
    @GetMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<String> getUserSubmissions(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            logger.warn("获取用户提交记录失败: 用户未登录");
            return ResponseEntity.status(401)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"ok\": 0, \"reason\": \"用户未登录\"}");
        }

        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            logger.warn("获取用户提交记录失败: 用户会话无效");
            return ResponseEntity.status(401)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"ok\": 0, \"reason\": \"用户会话无效\"}");
        }

        List<Submission> submissions = submissionService.getUserSubmissions(currentUser.getId());
        
        Map<String, Object> response = new HashMap<>();
        response.put("ok", 1);
        response.put("submissions", submissions);
        
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(gson.toJson(response));
    }

    /**
     * 获取题目的提交记录（仅教师可用）
     * @param problemId 题目ID
     * @param request HTTP请求
     * @return 提交记录列表
     */
    @GetMapping(value = "/problem/{problemId}", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<String> getProblemSubmissions(@PathVariable int problemId, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            logger.warn("获取题目提交记录失败: 用户未登录");
            return ResponseEntity.status(401)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"ok\": 0, \"reason\": \"用户未登录\"}");
        }

        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null || !"teacher".equals(currentUser.getRole())) {
            logger.warn("获取题目提交记录失败: 权限不足");
            return ResponseEntity.status(403)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"ok\": 0, \"reason\": \"权限不足\"}");
        }

        List<Submission> submissions = submissionService.getProblemSubmissions(problemId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("ok", 1);
        response.put("submissions", submissions);
        
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(gson.toJson(response));
    }

    /**
     * 获取题目的统计信息（仅教师可用）
     * @param problemId 题目ID
     * @param request HTTP请求
     * @return 统计信息
     */
    @GetMapping(value = "/stats/{problemId}", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<String> getProblemStats(@PathVariable int problemId, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            logger.warn("获取题目统计信息失败: 用户未登录");
            return ResponseEntity.status(401)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"ok\": 0, \"reason\": \"用户未登录\"}");
        }

        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null || !"teacher".equals(currentUser.getRole())) {
            logger.warn("获取题目统计信息失败: 权限不足");
            return ResponseEntity.status(403)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"ok\": 0, \"reason\": \"权限不足\"}");
        }

        Map<String, Integer> stats = submissionService.getProblemStats(problemId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("ok", 1);
        response.put("stats", stats);
        
        // 计算正确率
        int total = stats.get("total");
        int success = stats.get("success");
        double successRate = total > 0 ? (double) success / total * 100 : 0;
        response.put("successRate", Math.round(successRate * 100) / 100.0); // 保留两位小数
        
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(gson.toJson(response));
    }

    /**
     * 获取所有题目的统计信息（仅教师可用）
     * @param request HTTP请求
     * @return 统计信息列表
     */
    @GetMapping(value = "/stats", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<String> getAllProblemStats(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            logger.warn("获取所有题目统计信息失败: 用户未登录");
            return ResponseEntity.status(401)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"ok\": 0, \"reason\": \"用户未登录\"}");
        }

        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null || !"teacher".equals(currentUser.getRole())) {
            logger.warn("获取所有题目统计信息失败: 权限不足");
            return ResponseEntity.status(403)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"ok\": 0, \"reason\": \"权限不足\"}");
        }

        List<Map<String, Object>> statsList = submissionService.getAllProblemStats();
        
        Map<String, Object> response = new HashMap<>();
        response.put("ok", 1);
        response.put("statsList", statsList);
        
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(gson.toJson(response));
    }
}