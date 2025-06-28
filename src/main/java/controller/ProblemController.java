package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import problem.Problem;
import service.ProblemService;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * 问题管理控制器，处理问题的增删改查操作
 */
@RestController
@RequestMapping("/problem")
public class ProblemController {
    private static final Logger logger = LoggerFactory.getLogger(ProblemController.class);
    private final ProblemService problemService;
    private Gson gson = new GsonBuilder().create();

    @Autowired
    public ProblemController(ProblemService problemService) {
        this.problemService = problemService;
    }

    /**
     * 获取所有问题列表或根据ID获取问题详情
     * @param id 问题ID (可选)
     * @return 问题列表或问题详情
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<Object> getProblems(@RequestParam(required = false) Integer id) {
        logger.info("接收到获取题目请求，id={}", id);
        // 如果有id参数，则获取具体题目详情
        if (id != null) {
            Problem problem = problemService.selectOne(id);
            if (problem == null) {
                logger.warn("未找到ID为{}的题目", id);
                return ResponseEntity.notFound().build();
            }
            logger.info("成功获取题目详情: {}", problem.getTitle());
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(problem);
        } 
        // 否则获取所有题目列表
        else {
            List<Problem> problems = problemService.selectAll();
            logger.info("成功获取题目列表，共{}条记录", problems.size());
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(problems);
        }
    }

    /**
     * 添加新问题或更新现有问题
     * @param problem 问题信息
     * @return 操作结果
     */
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<String> addOrUpdateProblem(@RequestBody Problem problem) {
        try {
            // 验证请求数据
            if (problem == null) {
                return ResponseEntity.badRequest()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{\"ok\": 0, \"reason\": \"请求数据不能为空\"}");
            }

            if (problem.getTitle() == null || problem.getTitle().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{\"ok\": 0, \"reason\": \"题目标题不能为空\"}");
            }

            if (problem.getLevel() == null || problem.getLevel().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{\"ok\": 0, \"reason\": \"题目难度不能为空\"}");
            }

            if (problem.getDescription() == null || problem.getDescription().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body("{\"ok\": 0, \"reason\": \"题目描述不能为空\"}");
            }

            // 记录请求数据
            logger.info("接收到题目请求: {}", gson.toJson(problem));

            // 如果问题ID大于0，说明是更新操作
            if (problem.getId() != null && problem.getId() > 0) {
                logger.info("执行更新题目操作: ID={}, 标题={}", problem.getId(), problem.getTitle());
                boolean success = problemService.update(problem);
                if (success) {
                    logger.info("成功更新题目: ID={}, 标题={}", problem.getId(), problem.getTitle());
                    return ResponseEntity.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body("{\"ok\": 1}");
                } else {
                    logger.error("更新题目失败: ID={}, 标题={}", problem.getId(), problem.getTitle());
                    return ResponseEntity.badRequest()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body("{\"ok\": 0, \"reason\": \"更新失败\"}");
                }
            } 
            // 否则是添加操作
            else {
                logger.info("执行添加题目操作: {}", problem.getTitle());
                boolean success = problemService.insert(problem);
                if (success) {
                    logger.info("成功添加题目: {}", problem.getTitle());
                    return ResponseEntity.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body("{\"ok\": 1}");
                } else {
                    logger.error("添加题目失败: {}", problem.getTitle());
                    return ResponseEntity.badRequest()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body("{\"ok\": 0, \"reason\": \"添加失败\"}");
                }
            }
        } catch (Exception e) {
            logger.error("处理题目请求时发生错误", e);
            return ResponseEntity.status(500)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"ok\": 0, \"reason\": \"服务器内部错误: " + e.getMessage() + "\"}");
        }
    }

    /**
     * 删除问题
     * @param id 问题ID
     * @return 操作结果
     */
    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
    public ResponseEntity<String> deleteProblem(@RequestParam int id) {
        logger.info("接收到删除题目请求，id={}", id);
        boolean success = problemService.delete(id);
        if (success) {
            logger.info("成功删除题目，id={}", id);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"ok\": 1}");
        } else {
            logger.error("删除题目失败，id={}", id);
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"ok\": 0, \"reason\": \"删除失败\"}");
        }
    }
} 