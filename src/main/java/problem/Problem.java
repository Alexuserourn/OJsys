package problem;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 题目实体类
 */
@Data
@NoArgsConstructor
public class Problem {
    /**
     * 题目ID
     */
    private Integer id;
    
    /**
     * 题目标题
     */
    private String title;
    
    /**
     * 题目难度级别
     */
    private String level;
    
    /**
     * 题目描述
     */
    private String description;
    
    /**
     * 题目模板代码
     */
    private String templateCode;
    
    /**
     * 题目测试代码
     */
    private String testCode;

    @Override
    public String toString() {
        return "Problem{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", level='" + level + '\'' +
                ", description='" + description + '\'' +
                ", templateCode='" + templateCode + '\'' +
                ", testCode='" + testCode + '\'' +
                '}';
    }
}
