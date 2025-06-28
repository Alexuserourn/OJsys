# 在线判题系统 (Online Judge System)

这是一个基于Spring Boot的在线判题系统，提供了题目管理和代码编译执行功能。

## 项目功能

- 题目管理：添加、删除、查询题目
- 代码编译：编译并执行用户提交的代码，返回执行结果

## 技术栈

- Spring Boot 2.7.16
- MySQL 8.0
- JDK 1.8
- Maven 3.6+

## 开发环境准备

1. JDK 1.8+
2. Maven 3.6+
3. MySQL 8.0
4. IDE (推荐使用IntelliJ IDEA)

## 数据库准备

1. 创建MySQL数据库：`java41_oj`
2. 执行`src/main/resources/schema.sql`脚本，创建表和初始数据

## 配置修改

在`src/main/resources/application.properties`中修改以下配置：

```properties
# 数据库配置
spring.datasource.username=你的MySQL用户名
spring.datasource.password=你的MySQL密码
```

## 项目启动

### 使用IDE运行

1. 在IDE中打开项目
2. 构建并运行`com.example.oj.OJApplication`类

### 使用命令行运行

```bash
# 编译打包
mvn clean package

# 运行
java -jar target/OJ-1.0-SNAPSHOT.jar
```

## API接口

### 问题管理

- 获取所有问题：`GET /api/problem`
- 获取指定问题：`GET /api/problem/{id}`
- 添加问题：`POST /api/problem`
- 删除问题：`DELETE /api/problem/{id}`

### 代码编译

- 编译并运行代码：`POST /api/compile`

## 项目结构

```
src/main/java/
├── com.example.oj        # 主应用包
├── controller            # 控制器
├── service               # 服务层
│   └── impl              # 服务实现
├── problem               # 问题相关
├── compile               # 编译相关
└── util                  # 工具类
```

## 前端页面

前端静态资源位于`src/main/resources/static`目录下，访问地址为`http://localhost:8081`。 