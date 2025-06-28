package service;

import dao.UserDao;
import entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户服务实现类
 */
@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User login(String username, String password) {
        if (username == null || password == null) {
            logger.warn("登录失败：用户名或密码为空");
            return null;
        }
        
        User user = userDao.selectByUsername(username);
        if (user == null) {
            logger.warn("登录失败：用户 {} 不存在", username);
            return null;
        }
        
        if (!password.equals(user.getPassword())) {
            logger.warn("登录失败：用户 {} 密码错误", username);
            return null;
        }
        
        logger.info("用户 {} 登录成功", username);
        return user;
    }

    @Override
    public boolean register(User user) {
        if (user == null || user.getUsername() == null || user.getPassword() == null) {
            logger.warn("注册失败：用户信息不完整");
            return false;
        }
        
        // 检查用户名是否已存在
        User existingUser = userDao.selectByUsername(user.getUsername());
        if (existingUser != null) {
            logger.warn("注册失败：用户名 {} 已存在", user.getUsername());
            return false;
        }
        
        // 默认角色为学生，如果没有指定角色
        if (user.getRole() == null) {
            user.setRole("student");
        }
        
        boolean success = userDao.insert(user);
        if (success) {
            logger.info("用户 {} 注册成功", user.getUsername());
        } else {
            logger.error("用户 {} 注册失败", user.getUsername());
        }
        return success;
    }

    @Override
    public User getUserById(int id) {
        return userDao.selectById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.selectAll();
    }

    @Override
    public boolean updateUser(User user) {
        if (user == null || user.getId() == null) {
            logger.warn("更新用户失败：缺少用户ID");
            return false;
        }
        
        boolean success = userDao.update(user);
        if (success) {
            logger.info("更新用户成功：ID = {}", user.getId());
        } else {
            logger.error("更新用户失败：ID = {}", user.getId());
        }
        return success;
    }

    @Override
    public boolean deleteUser(int id) {
        boolean success = userDao.delete(id);
        if (success) {
            logger.info("删除用户成功：ID = {}", id);
        } else {
            logger.error("删除用户失败：ID = {}", id);
        }
        return success;
    }
} 