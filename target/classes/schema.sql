-- 如果不存在则创建数据库
CREATE DATABASE IF NOT EXISTS java41_oj;

USE java41_oj;

-- 删除已存在的表
DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS oj_table;

-- 创建用户表
CREATE TABLE user (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL
);

-- 插入初始用户数据
INSERT INTO user (username, password, role) VALUES 
('teacher', '123456', 'teacher'),  -- 教师用户
('student', '123456', 'student');  -- 学生用户

-- 创建题目表
CREATE TABLE oj_table (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(100) NOT NULL,
    level VARCHAR(20) NOT NULL,
    description TEXT NOT NULL,
    templateCode TEXT NOT NULL,
    testCode TEXT NOT NULL
);


