package service.impl;

import dao.SubmissionDAO;
import entity.Submission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.SubmissionService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 提交记录服务实现类
 */
@Service
public class SubmissionServiceImpl implements SubmissionService {
    private static final Logger logger = LoggerFactory.getLogger(SubmissionServiceImpl.class);
    private final SubmissionDAO submissionDAO;
    
    @Autowired
    public SubmissionServiceImpl(SubmissionDAO submissionDAO) {
        this.submissionDAO = submissionDAO;
    }
    
    @Override
    public boolean addSubmission(Submission submission) {
        try {
            if (submission == null) {
                logger.error("添加提交记录失败: 提交记录对象为空");
                return false;
            }
            
            if (submission.getUserId() == null || submission.getProblemId() == null) {
                logger.error("添加提交记录失败: 用户ID或题目ID为空");
                return false;
            }
            
            submissionDAO.insert(submission);
            logger.info("成功添加提交记录: 用户ID={}, 题目ID={}, 结果={}", 
                    submission.getUserId(), submission.getProblemId(), submission.getResult());
            return true;
        } catch (SQLException e) {
            logger.error("添加提交记录失败", e);
            return false;
        }
    }
    
    @Override
    public List<Submission> getUserSubmissions(int userId) {
        try {
            List<Submission> submissions = submissionDAO.selectByUserId(userId);
            logger.info("成功获取用户ID={}的提交记录，共{}条", userId, submissions.size());
            return submissions;
        } catch (SQLException e) {
            logger.error("获取用户提交记录失败", e);
            return new ArrayList<>();
        }
    }
    
    @Override
    public List<Submission> getProblemSubmissions(int problemId) {
        try {
            List<Submission> submissions = submissionDAO.selectByProblemId(problemId);
            logger.info("成功获取题目ID={}的提交记录，共{}条", problemId, submissions.size());
            return submissions;
        } catch (SQLException e) {
            logger.error("获取题目提交记录失败", e);
            return new ArrayList<>();
        }
    }
    
    @Override
    public Map<String, Integer> getProblemStats(int problemId) {
        try {
            Map<String, Integer> stats = submissionDAO.getProblemStats(problemId);
            logger.info("成功获取题目ID={}的统计信息: 总提交数={}, 成功提交数={}", 
                    problemId, stats.get("total"), stats.get("success"));
            return stats;
        } catch (SQLException e) {
            logger.error("获取题目统计信息失败", e);
            Map<String, Integer> emptyStats = new HashMap<>();
            emptyStats.put("total", 0);
            emptyStats.put("success", 0);
            return emptyStats;
        }
    }
    
    @Override
    public List<Map<String, Object>> getAllProblemStats() {
        try {
            List<Map<String, Object>> statsList = submissionDAO.getAllProblemStats();
            logger.info("成功获取所有题目的统计信息，共{}道题目", statsList.size());
            return statsList;
        } catch (SQLException e) {
            logger.error("获取所有题目统计信息失败", e);
            return new ArrayList<>();
        }
    }
}