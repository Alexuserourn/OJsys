package service.impl;

import compile.Answer;
import compile.CompileRequest;
import compile.CompileResponse;
import compile.Question;
import compile.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import problem.Problem;
import problem.ProblemDAO;
import service.CompileService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

/**
 * 代码编译服务实现类
 */
@Service
public class CompileServiceImpl implements CompileService {
    private final Logger logger = LoggerFactory.getLogger(CompileServiceImpl.class);
    private final ProblemDAO problemDAO;
    
    @Autowired
    public CompileServiceImpl(ProblemDAO problemDAO) {
        this.problemDAO = problemDAO;
    }
    
    @Override
    public CompileResponse compileAndRun(CompileRequest request) {
        CompileResponse response = new CompileResponse();
        
        try {
            // 1. 根据请求中的题目ID，查询数据库获取测试用例代码
            Problem problem = problemDAO.selectOne(request.getId());
            if (problem == null) {
                response.setErrno(1);
                response.setReason("题目不存在");
                return response;
            }
            
            // 2. 将用户提交的代码与测试用例代码合并
            String testCode = problem.getTestCode();
            String userCode = request.getCode();
            String finalCode = mergeCode(userCode, testCode);
            
            logger.info("合并后的代码：\n{}", finalCode);
            
            // 3. 创建Task对象，编译并运行代码
            Task task = new Task();
            Question question = new Question();
            question.setCode(finalCode);
            
            Answer answer = task.compileAndRun(question);
            
            // 4. 设置响应结果
            response.setErrno(answer.getErrno());
            
            // 确保编码正确
            if (answer.getReason() != null) {
                logger.info("编译/运行错误：{}", answer.getReason());
                response.setReason(answer.getReason());
            }
            
            if (answer.getStdout() != null) {
                logger.info("标准输出：{}", answer.getStdout());
                response.setStdout(answer.getStdout());
            }
            
            logger.info("编译响应：errno={}, reason={}, stdout={}", 
                    response.getErrno(), 
                    (response.getReason() != null ? "有错误信息" : "无错误信息"), 
                    (response.getStdout() != null ? "有标准输出" : "无标准输出"));
            
        } catch (SQLException | InterruptedException | IOException e) {
            response.setErrno(1);
            response.setReason("系统错误: " + e.getMessage());
            logger.error("编译过程发生异常", e);
        }

        return response;
    }
    
    /**
     * 合并用户代码和测试用例代码
     * @param userCode 用户代码
     * @param testCode 测试用例代码
     * @return 合并后的代码
     */
    private String mergeCode(String userCode, String testCode) {
        // 新的合并策略：将用户的Solution类和测试代码放在同一个文件中，但保持各自独立
        
        // 检查用户代码是否为Solution类
        if (userCode.contains("class Solution")) {
            // 用户提交的是Solution类，测试代码可能是单独的Test类
            // 我们不需要在结尾插入测试代码，而是将两者并列放置
            
            // 移除用户代码中可能存在的package语句
            userCode = userCode.replaceAll("^\\s*package\\s+[\\w\\.]+;", "");
            
            // 如果用户代码包含public访问修饰符，需要移除，因为一个文件中只能有一个public类
            if (userCode.contains("public class Solution")) {
                userCode = userCode.replace("public class Solution", "class Solution");
            }
            
            // 确保测试代码中的Test类是public的，因为它包含main方法
            if (testCode.contains("class Test") && !testCode.contains("public class Test")) {
                testCode = testCode.replace("class Test", "public class Test");
            }
            
            // 组合代码：先放用户的Solution类，然后是测试代码
            logger.info("使用并列方式合并代码");
            return userCode + "\n\n" + testCode;
        } else {
            // 对于旧的方式（用户提交的不是Solution类），我们保持原有的合并逻辑
            logger.info("使用插入方式合并代码");
            int pos = userCode.lastIndexOf("}");
            if (pos == -1) {
                logger.warn("无法在用户代码中找到结束括号");
                return null;
            }
            return userCode.substring(0, pos) + testCode + "}";
        }
    }
} 