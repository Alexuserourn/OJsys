package dao;

import entity.User;
import java.util.List;

/**
 * 用户数据访问接口
 */
public interface UserDao {
    /**
     * 根据用户名查找用户
     * @param username 用户名
     * @return 用户对象，如果不存在则返回null
     */
    User selectByUsername(String username);
    
    /**
     * 根据ID查找用户
     * @param id 用户ID
     * @return 用户对象，如果不存在则返回null
     */
    User selectById(int id);
    
    /**
     * 获取所有用户
     * @return 用户列表
     */
    List<User> selectAll();
    
    /**
     * 添加用户
     * @param user 用户对象
     * @return 添加是否成功
     */
    boolean insert(User user);
    
    /**
     * 更新用户信息
     * @param user 用户对象
     * @return 更新是否成功
     */
    boolean update(User user);
    
    /**
     * 删除用户
     * @param id 用户ID
     * @return 删除是否成功
     */
    boolean delete(int id);
} 