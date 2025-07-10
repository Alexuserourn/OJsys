package dao;

import entity.Submission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 提交记录数据访问对象
 */
@Repository
public class SubmissionDAO {
    private final JdbcTemplate jdbcTemplate;
    
    private final RowMapper<Submission> rowMapper = (rs, rowNum) -> {
        Submission submission = new Submission();
        submission.setId(rs.getInt("id"));
        submission.setUserId(rs.getInt("user_id"));
        submission.setProblemId(rs.getInt("problem_id"));
        submission.setCode(rs.getString("code"));
        submission.setResult(rs.getInt("result"));
        submission.setSubmitTime(rs.getTimestamp("submit_time"));
        
        // 如果查询中包含了用户名和题目标题
        try {
            submission.setUsername(rs.getString("username"));
        } catch (Exception e) {
            // 忽略异常，表示查询中没有这个字段
        }
        
        try {
            submission.setProblemTitle(rs.getString("title"));
        } catch (Exception e) {
            // 忽略异常，表示查询中没有这个字段
        }
        
        return submission;
    };
    
    @Autowired
    public SubmissionDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    /**
     * 插入一条提交记录
     * @param submission 提交记录对象
     * @throws SQLException SQL异常
     */
    public void insert(Submission submission) throws SQLException {
        String sql = "INSERT INTO submission (user_id, problem_id, code, result) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql,
            submission.getUserId(),
            submission.getProblemId(),
            submission.getCode(),
            submission.getResult()
        );
    }
    
    /**
     * 查询用户的所有提交记录
     * @param userId 用户ID
     * @return 提交记录列表
     * @throws SQLException SQL异常
     */
    public List<Submission> selectByUserId(int userId) throws SQLException {
        String sql = "SELECT s.*, p.title FROM submission s " +
                    "JOIN oj_table p ON s.problem_id = p.id " +
                    "WHERE s.user_id = ? " +
                    "ORDER BY s.submit_time DESC";
        return jdbcTemplate.query(sql, rowMapper, userId);
    }
    
    /**
     * 查询题目的所有提交记录
     * @param problemId 题目ID
     * @return 提交记录列表
     * @throws SQLException SQL异常
     */
    public List<Submission> selectByProblemId(int problemId) throws SQLException {
        String sql = "SELECT s.*, u.username FROM submission s " +
                    "JOIN user u ON s.user_id = u.id " +
                    "WHERE s.problem_id = ? " +
                    "ORDER BY s.submit_time DESC";
        return jdbcTemplate.query(sql, rowMapper, problemId);
    }
    
    /**
     * 获取题目的提交统计信息
     * @param problemId 题目ID
     * @return 统计信息，包含总提交数和正确提交数
     * @throws SQLException SQL异常
     */
    public Map<String, Integer> getProblemStats(int problemId) throws SQLException {
        String totalSql = "SELECT COUNT(*) FROM submission WHERE problem_id = ?";
        String successSql = "SELECT COUNT(*) FROM submission WHERE problem_id = ? AND result = 1";
        
        int total = jdbcTemplate.queryForObject(totalSql, Integer.class, problemId);
        int success = jdbcTemplate.queryForObject(successSql, Integer.class, problemId);
        
        Map<String, Integer> stats = new HashMap<>();
        stats.put("total", total);
        stats.put("success", success);
        
        return stats;
    }
    
    /**
     * 获取所有题目的提交统计信息
     * @return 统计信息列表，每个题目包含ID、标题、总提交数和正确率
     * @throws SQLException SQL异常
     */
    public List<Map<String, Object>> getAllProblemStats() throws SQLException {
        String sql = "SELECT p.id, p.title, " +
                    "COUNT(s.id) as total_submissions, " +
                    "SUM(CASE WHEN s.result = 1 THEN 1 ELSE 0 END) as successful_submissions " +
                    "FROM oj_table p " +
                    "LEFT JOIN submission s ON p.id = s.problem_id " +
                    "GROUP BY p.id, p.title";
        
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Map<String, Object> stats = new HashMap<>();
            stats.put("id", rs.getInt("id"));
            stats.put("title", rs.getString("title"));
            stats.put("totalSubmissions", rs.getInt("total_submissions"));
            
            int totalSubmissions = rs.getInt("total_submissions");
            int successfulSubmissions = rs.getInt("successful_submissions");
            
            double successRate = totalSubmissions > 0 ? 
                    (double) successfulSubmissions / totalSubmissions * 100 : 0;
            
            stats.put("successRate", Math.round(successRate * 100) / 100.0); // 保留两位小数
            
            return stats;
        });
    }
}