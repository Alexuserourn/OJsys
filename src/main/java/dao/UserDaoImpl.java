package dao;

import entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * 用户数据访问实现类
 */
@Repository
public class UserDaoImpl implements UserDao {
    private static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 用户RowMapper，用于将数据库记录映射为User对象
    private static final RowMapper<User> USER_ROW_MAPPER = new RowMapper<User>() {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setRole(rs.getString("role"));
            return user;
        }
    };

    @Override
    public User selectByUsername(String username) {
        String sql = "SELECT * FROM user WHERE username = ?";
        try {
            return jdbcTemplate.queryForObject(sql, USER_ROW_MAPPER, username);
        } catch (Exception e) {
            logger.warn("没有找到用户名为 {} 的用户", username);
            return null;
        }
    }

    @Override
    public User selectById(int id) {
        String sql = "SELECT * FROM user WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, USER_ROW_MAPPER, id);
        } catch (Exception e) {
            logger.warn("没有找到ID为 {} 的用户", id);
            return null;
        }
    }

    @Override
    public List<User> selectAll() {
        String sql = "SELECT * FROM user";
        return jdbcTemplate.query(sql, USER_ROW_MAPPER);
    }

    @Override
    public boolean insert(User user) {
        String sql = "INSERT INTO user (username, password, role) VALUES (?, ?, ?)";
        try {
            int affected = jdbcTemplate.update(sql, user.getUsername(), user.getPassword(), user.getRole());
            return affected == 1;
        } catch (Exception e) {
            logger.error("添加用户失败: {}", user.getUsername(), e);
            return false;
        }
    }

    @Override
    public boolean update(User user) {
        String sql = "UPDATE user SET username = ?, password = ?, role = ? WHERE id = ?";
        try {
            int affected = jdbcTemplate.update(sql, user.getUsername(), user.getPassword(), user.getRole(), user.getId());
            return affected == 1;
        } catch (Exception e) {
            logger.error("更新用户失败: {}", user.getId(), e);
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM user WHERE id = ?";
        try {
            int affected = jdbcTemplate.update(sql, id);
            return affected == 1;
        } catch (Exception e) {
            logger.error("删除用户失败: {}", id, e);
            return false;
        }
    }
} 