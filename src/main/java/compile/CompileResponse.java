package compile;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 编译响应类
 */
@Data
@NoArgsConstructor
public class CompileResponse {
    /**
     * 错误码，0表示成功，非0表示失败
     */
    private int errno;
    
    /**
     * 错误原因
     */
    private String reason;
    
    /**
     * 标准输出结果
     */
    private String stdout;
} 