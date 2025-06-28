package controller;

import entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户控制器，处理用户相关请求
 */
@RestController
@RequestMapping("/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 用户登录
     * @param loginInfo 登录信息
     * @param session HTTP会话
     * @return 登录结果
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> loginInfo, HttpSession session) {
        String username = loginInfo.get("username");
        String password = loginInfo.get("password");
        logger.info("接收到登录请求，用户名：{}", username);
        
        Map<String, Object> result = new HashMap<>();
        
        if (username == null || password == null) {
            result.put("success", false);
            result.put("message", "用户名或密码不能为空");
            return ResponseEntity.badRequest().body(result);
        }
        
        User user = userService.login(username, password);
        if (user == null) {
            result.put("success", false);
            result.put("message", "用户名或密码错误");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
        }
        
        // 登录成功，将用户信息保存到会话中
        session.setAttribute("user", user);
        
        result.put("success", true);
        result.put("message", "登录成功");
        result.put("user", user);
        
        logger.info("用户 {} 登录成功，角色：{}", username, user.getRole());
        return ResponseEntity.ok(result);
    }
    
    /**
     * 用户注销
     * @param session HTTP会话
     * @return 注销结果
     */
    @GetMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(HttpSession session) {
        User user = (User) session.getAttribute("user");
        Map<String, Object> result = new HashMap<>();
        
        if (user != null) {
            logger.info("用户 {} 注销", user.getUsername());
            session.invalidate();
        }
        
        result.put("success", true);
        result.put("message", "注销成功");
        
        return ResponseEntity.ok(result);
    }
    
    /**
     * 用户注册
     * @param user 用户信息
     * @return 注册结果
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody User user) {
        logger.info("接收到注册请求，用户名：{}", user.getUsername());
        
        Map<String, Object> result = new HashMap<>();
        
        if (user.getUsername() == null || user.getPassword() == null) {
            result.put("success", false);
            result.put("message", "用户名或密码不能为空");
            return ResponseEntity.badRequest().body(result);
        }
        
        // 默认注册为学生角色
        if (user.getRole() == null) {
            user.setRole("student");
        }
        
        boolean success = userService.register(user);
        if (success) {
            result.put("success", true);
            result.put("message", "注册成功");
            logger.info("用户 {} 注册成功", user.getUsername());
            return ResponseEntity.ok(result);
        } else {
            result.put("success", false);
            result.put("message", "注册失败，用户名可能已存在");
            logger.warn("用户 {} 注册失败", user.getUsername());
            return ResponseEntity.badRequest().body(result);
        }
    }
    
    /**
     * 获取当前登录用户信息
     * @param session HTTP会话
     * @return 用户信息
     */
    @GetMapping("/current")
    public ResponseEntity<Map<String, Object>> getCurrentUser(HttpSession session) {
        User user = (User) session.getAttribute("user");
        Map<String, Object> result = new HashMap<>();
        
        if (user == null) {
            result.put("success", false);
            result.put("message", "未登录");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
        }
        
        result.put("success", true);
        result.put("user", user);
        
        return ResponseEntity.ok(result);
    }
    
    /**
     * 获取所有用户信息（仅限教师）
     * @param request HTTP请求
     * @return 用户列表
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getAllUsers(HttpServletRequest request) {
        User currentUser = (User) request.getSession().getAttribute("user");
        
        // 验证权限，只有教师才能查看所有用户
        if (currentUser == null || !"teacher".equals(currentUser.getRole())) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "权限不足");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(result);
        }
        
        List<User> users = userService.getAllUsers();
        logger.info("用户 {} 查询了所有用户信息，共{}条记录", currentUser.getUsername(), users.size());
        
        return ResponseEntity.ok(users);
    }
    
    /**
     * 更新用户信息（仅限教师或本人）
     * @param id 用户ID
     * @param user 用户信息
     * @param request HTTP请求
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateUser(@PathVariable int id, @RequestBody User user, HttpServletRequest request) {
        User currentUser = (User) request.getSession().getAttribute("user");
        Map<String, Object> result = new HashMap<>();
        
        // 验证权限，只有教师或本人才能更新用户信息
        if (currentUser == null || (!"teacher".equals(currentUser.getRole()) && currentUser.getId() != id)) {
            result.put("success", false);
            result.put("message", "权限不足");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(result);
        }
        
        user.setId(id); // 确保ID与路径中的ID一致
        
        // 只有教师才能修改用户角色
        if (!"teacher".equals(currentUser.getRole()) && user.getRole() != null) {
            User existingUser = userService.getUserById(id);
            user.setRole(existingUser.getRole());
        }
        
        boolean success = userService.updateUser(user);
        
        if (success) {
            result.put("success", true);
            result.put("message", "更新成功");
            logger.info("用户 {} 更新了用户 {} 的信息", currentUser.getUsername(), id);
            return ResponseEntity.ok(result);
        } else {
            result.put("success", false);
            result.put("message", "更新失败");
            logger.warn("用户 {} 更新用户 {} 信息失败", currentUser.getUsername(), id);
            return ResponseEntity.badRequest().body(result);
        }
    }
    
    /**
     * 删除用户（仅限教师）
     * @param id 用户ID
     * @param request HTTP请求
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable int id, HttpServletRequest request) {
        User currentUser = (User) request.getSession().getAttribute("user");
        Map<String, Object> result = new HashMap<>();
        
        // 验证权限，只有教师才能删除用户
        if (currentUser == null || !"teacher".equals(currentUser.getRole())) {
            result.put("success", false);
            result.put("message", "权限不足");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(result);
        }
        
        // 不能删除自己
        if (currentUser.getId() == id) {
            result.put("success", false);
            result.put("message", "不能删除当前登录用户");
            return ResponseEntity.badRequest().body(result);
        }
        
        boolean success = userService.deleteUser(id);
        
        if (success) {
            result.put("success", true);
            result.put("message", "删除成功");
            logger.info("用户 {} 删除了用户 {}", currentUser.getUsername(), id);
            return ResponseEntity.ok(result);
        } else {
            result.put("success", false);
            result.put("message", "删除失败");
            logger.warn("用户 {} 删除用户 {} 失败", currentUser.getUsername(), id);
            return ResponseEntity.badRequest().body(result);
        }
    }
} 