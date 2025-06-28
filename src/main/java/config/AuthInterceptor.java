package config;

import entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

/**
 * 权限拦截器，用于验证用户权限
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(AuthInterceptor.class);
    
    // 不需要登录就可以访问的路径
    private static final List<String> PUBLIC_PATHS = Arrays.asList(
            "/user/login",
            "/user/register",
            "/user/current",
            "/login.html",
            "/index.html",
            "/css/",
            "/js/",
            "/img/",
            "/api/ping"
    );
    
    // 只有教师才能访问的路径
    private static final List<String> TEACHER_ONLY_PATHS = Arrays.asList(
            "/problem/add",
            "/problem/delete",
            "/problem/update"
    );

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        
        // 检查是否是公开路径
        for (String path : PUBLIC_PATHS) {
            if (requestURI.startsWith(path)) {
                return true;
            }
        }
        
        // 获取当前登录用户
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        // 如果用户未登录，则重定向到登录页面
        if (user == null) {
            logger.warn("未登录用户尝试访问：{}", requestURI);
            
            // 检查是否是AJAX请求
            String xRequestedWith = request.getHeader("X-Requested-With");
            if ("XMLHttpRequest".equals(xRequestedWith)) {
                // 对于AJAX请求，返回JSON响应
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"success\":false,\"message\":\"请先登录\"}");
            } else {
                // 对于普通请求，重定向到登录页面
                response.sendRedirect("/login.html");
            }
            return false;
        }
        
        // 检查是否是只有教师才能访问的路径
        for (String path : TEACHER_ONLY_PATHS) {
            if (requestURI.startsWith(path)) {
                // 如果不是教师，则拒绝访问
                if (!"teacher".equals(user.getRole())) {
                    logger.warn("权限不足：用户 {} 尝试访问 {}", user.getUsername(), requestURI);
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write("{\"success\":false,\"message\":\"权限不足\"}");
                    return false;
                }
                break;
            }
        }
        
        // 检查问题管理相关的POST、DELETE、PUT请求，只有教师才能执行
        String method = request.getMethod();
        if (requestURI.startsWith("/problem") && 
                ("POST".equals(method) || "DELETE".equals(method) || "PUT".equals(method))) {
            if (!"teacher".equals(user.getRole())) {
                logger.warn("权限不足：用户 {} 尝试执行 {} {}", user.getUsername(), method, requestURI);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"success\":false,\"message\":\"权限不足\"}");
                return false;
            }
        }
        
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 请求处理后的操作
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 请求完成后的操作
    }
} 