package compile;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 编译请求类
 */
@Data
@NoArgsConstructor
public class CompileRequest {
    /**
     * 题目ID
     */
    private int id;
    
    /**
     * 用户提交的代码
     */
    private String code;
} 