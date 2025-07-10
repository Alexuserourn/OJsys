package service;

import entity.Submission;

import java.util.List;
import java.util.Map;

/**
 * 提交记录服务接口
 */
public interface SubmissionService {
    
    /**
     * 添加提交记录
     * @param submission 提交记录
     * @return 是否添加成功
     */
    boolean addSubmission(Submission submission);
    
    /**
     * 获取用户的提交记录
     * @param userId 用户ID
     * @return 提交记录列表
     */
    List<Submission> getUserSubmissions(int userId);
    
    /**
     * 获取题目的提交记录
     * @param problemId 题目ID
     * @return 提交记录列表
     */
    List<Submission> getProblemSubmissions(int problemId);
    
    /**
     * 获取题目的统计信息
     * @param problemId 题目ID
     * @return 统计信息
     */
    Map<String, Integer> getProblemStats(int problemId);
    
    /**
     * 获取所有题目的统计信息
     * @return 统计信息列表
     */
    List<Map<String, Object>> getAllProblemStats();
}