package problem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Problem数据访问对象
 * 使用Spring Boot的JdbcTemplate进行数据库操作
 */
@Repository
public class ProblemDAO {
    private final JdbcTemplate jdbcTemplate;
    
    private final RowMapper<Problem> rowMapper = (ResultSet rs, int rowNum) -> {
        Problem problem = new Problem();
        problem.setId(rs.getInt("id"));
        problem.setTitle(rs.getString("title"));
        problem.setLevel(rs.getString("level"));
        if (rs.getMetaData().getColumnCount() > 3) {
            problem.setDescription(rs.getString("description"));
            problem.setTemplateCode(rs.getString("templateCode"));
            problem.setTestCode(rs.getString("testCode"));
        }
        return problem;
    };
    
    @Autowired
    public ProblemDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    /**
     * 重置自增ID
     * @throws SQLException SQL异常
     */
    public void resetAutoIncrement() throws SQLException {
        try {
            // 获取当前最大ID
            String maxIdSql = "SELECT MAX(id) FROM oj_table";
            Integer maxId = jdbcTemplate.queryForObject(maxIdSql, Integer.class);
            
            if (maxId != null) {
                // 重置自增ID为当前最大ID + 1
                String resetSql = "ALTER TABLE oj_table AUTO_INCREMENT = ?";
                jdbcTemplate.update(resetSql, maxId + 1);
            }
        } catch (Exception e) {
            // 如果重置自增ID失败，记录错误但继续执行
            System.err.println("重置自增ID失败: " + e.getMessage());
            // 不抛出异常，让插入操作继续执行
        }
    }
    
    /**
     * 插入一条题目记录
     * @param problem 题目对象
     * @throws SQLException SQL异常
     */
    public void insert(Problem problem) throws SQLException {
        try {
            // 先重置自增ID
            resetAutoIncrement();
            
            String sql = "INSERT INTO oj_table (title, level, description, templateCode, testCode) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
            problem.getTitle(),
            problem.getLevel(),
            problem.getDescription(),
            problem.getTemplateCode(),
            problem.getTestCode()
        );
        } catch (Exception e) {
            throw new SQLException("插入题目失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 删除指定ID的题目
     * @param problemId 题目ID
     * @throws SQLException SQL异常
     */
    public void delete(int problemId) throws SQLException {
        String sql = "DELETE FROM oj_table WHERE id = ?";
        jdbcTemplate.update(sql, problemId);
    }
    
    /**
     * 查询所有题目列表(简要信息)
     * @return 题目列表
     * @throws SQLException SQL异常
     */
    public List<Problem> selectAll() throws SQLException {
        String sql = "SELECT id, title, level FROM oj_table";
        return jdbcTemplate.query(sql, rowMapper);
    }
    
    /**
     * 查询指定ID的题目详情
     * @param problemId 题目ID
     * @return 题目对象
     * @throws SQLException SQL异常
     */
    public Problem selectOne(int problemId) throws SQLException {
        String sql = "SELECT * FROM oj_table WHERE id = ?";
        List<Problem> problems = jdbcTemplate.query(sql, rowMapper, problemId);
        return problems.isEmpty() ? null : problems.get(0);
    }
    
    /**
     * 计算题目总数
     * @return 题目总数
     * @throws SQLException SQL异常
     */
    public int countProblems() throws SQLException {
        String sql = "SELECT COUNT(*) FROM oj_table";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }
    
    /**
     * 更新题目信息
     * @param problem 题目对象
     * @throws SQLException SQL异常
     */
    public void update(Problem problem) throws SQLException {
        String sql = "UPDATE oj_table SET title = ?, level = ?, description = ?, templateCode = ?, testCode = ? WHERE id = ?";
        jdbcTemplate.update(sql,
            problem.getTitle(),
            problem.getLevel(),
            problem.getDescription(),
            problem.getTemplateCode(),
            problem.getTestCode(),
            problem.getId()
        );
    }
}
