package entity;

import java.sql.Timestamp;

/**
 * 提交记录实体类
 */
public class Submission {
    private Integer id;          // 提交ID
    private Integer userId;      // 用户ID
    private Integer problemId;   // 题目ID
    private String code;         // 提交的代码
    private Integer result;      // 结果：0失败，1成功
    private Timestamp submitTime;// 提交时间
    
    // 非数据库字段，用于展示
    private String username;     // 用户名
    private String problemTitle; // 题目标题
    
    public Submission() {
    }
    
    public Submission(Integer userId, Integer problemId, String code, Integer result) {
        this.userId = userId;
        this.problemId = problemId;
        this.code = code;
        this.result = result;
    }
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getUserId() {
        return userId;
    }
    
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    
    public Integer getProblemId() {
        return problemId;
    }
    
    public void setProblemId(Integer problemId) {
        this.problemId = problemId;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public Integer getResult() {
        return result;
    }
    
    public void setResult(Integer result) {
        this.result = result;
    }
    
    public Timestamp getSubmitTime() {
        return submitTime;
    }
    
    public void setSubmitTime(Timestamp submitTime) {
        this.submitTime = submitTime;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getProblemTitle() {
        return problemTitle;
    }
    
    public void setProblemTitle(String problemTitle) {
        this.problemTitle = problemTitle;
    }
    
    @Override
    public String toString() {
        return "Submission{" +
                "id=" + id +
                ", userId=" + userId +
                ", problemId=" + problemId +
                ", result=" + result +
                ", submitTime=" + submitTime +
                ", username='" + username + '\'' +
                ", problemTitle='" + problemTitle + '\'' +
                '}';
    }
}