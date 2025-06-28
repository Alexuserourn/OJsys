package util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 数据库工具类，提供了获取数据库连接以及关闭数据库资源的方法
 * 在Spring Boot环境中，我们可以直接使用DataSource和JdbcTemplate
 */
@Component
public class DBUtil {
    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;
    
    @Autowired
    public DBUtil(DataSource dataSource, JdbcTemplate jdbcTemplate) {
        this.dataSource = dataSource;
        this.jdbcTemplate = jdbcTemplate;
        System.out.println("DBUtil已初始化，使用Spring Boot管理的数据源");
    }
    
    /**
     * 获取数据库连接
     * @return 数据库连接对象
     */
    public Connection getConnection() {
        try {
            Connection connection = dataSource.getConnection();
            if (connection != null) {
                System.out.println("数据库连接成功！");
            } else {
                System.err.println("获取到的数据库连接为null");
            }
            return connection;
        } catch (SQLException e) {
            System.err.println("获取数据库连接失败：" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 关闭数据库资源
     * @param connection 数据库连接
     * @param statement 预处理语句
     * @param resultSet 结果集
     */
    public static void close(Connection connection, PreparedStatement statement, ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
                System.out.println("数据库连接已关闭");
            }
        } catch (SQLException e) {
            System.err.println("关闭数据库资源时出错：" + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 获取JdbcTemplate实例
     * @return JdbcTemplate实例
     */
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }
}
