package service;

import problem.Problem;

import java.util.List;

/**
 * 问题服务接口，定义操作问题的方法
 */
public interface ProblemService {
    /**
     * 查询所有问题
     * @return 问题列表
     */
    List<Problem> selectAll();
    
    /**
     * 根据ID查询问题
     * @param id 问题ID
     * @return 问题对象
     */
    Problem selectOne(int id);
    
    /**
     * 添加问题
     * @param problem 问题对象
     * @return 是否成功
     */
    boolean insert(Problem problem);
    
    /**
     * 删除问题
     * @param id 问题ID
     * @return 是否成功
     */
    boolean delete(int id);
    
    /**
     * 更新问题
     * @param problem 问题对象
     * @return 是否成功
     */
    boolean update(Problem problem);
} 