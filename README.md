# Code Generator

这是一个基于Spring Boot的代码生成器，可以从数据库metadata生成对应的XML DAO、Service、Controller、Entity以及React Antd的schema语句，用于生成前端表格。

## 功能特性

- 获取数据库表结构metadata
- 自动生成Entity类
- 生成MyBatis XML Mapper文件
- 生成Mapper接口
- 生成Service类
- 生成Controller类
- 生成React Antd Table组件的schema

## 技术栈

- Spring Boot 3.2.0
- MyBatis 3.5.14
- H2 Database (用于测试)
- MySQL Connector/J 8.1.0 (生产环境)
- Java 17

## 使用方法

### 1. 配置数据库

默认使用H2内存数据库（包含测试数据）：

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
```

如需使用MySQL，请修改`application.properties`：

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/your_database
spring.datasource.username=root
spring.datasource.password=password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

### 2. 启动应用

```bash
mvn spring-boot:run
```

应用启动后，可以通过 http://localhost:8080/h2-console 访问H2控制台（用户名：sa，密码：空）

### 3. 获取表列表

```bash
GET /api/generator/tables?schema=PUBLIC
```

### 4. 生成代码

```bash
POST /api/generator/generate/{tableName}?schema=PUBLIC
```

生成的代码将保存在`generated/`目录下。

## 测试数据

项目包含以下测试表和数据：

### 用户表 (user)
- id: BIGINT (主键)
- username: VARCHAR(50)
- email: VARCHAR(100)
- password: VARCHAR(255)
- full_name: VARCHAR(100)
- age: INT
- created_at, updated_at: TIMESTAMP

### 产品表 (product)
- id: BIGINT (主键)
- name: VARCHAR(200)
- description: TEXT
- price: DECIMAL(10,2)
- category: VARCHAR(50)
- stock_quantity: INT
- is_active: BOOLEAN
- created_at: TIMESTAMP

### 订单表 (order_table)
- id: BIGINT (主键)
- user_id: BIGINT (外键)
- order_number: VARCHAR(50)
- total_amount: DECIMAL(10,2)
- status: VARCHAR(20)
- order_date: TIMESTAMP

### 订单项表 (order_item)
- id: BIGINT (主键)
- order_id: BIGINT (外键)
- product_id: BIGINT (外键)
- quantity: INT
- unit_price: DECIMAL(10,2)
- total_price: DECIMAL(10,2)

```
generated/
├── entity/
│   └── TableName.java
├── mapper/
│   ├── TableNameMapper.java
│   └── TableNameMapper.xml
├── service/
│   └── TableNameService.java
├── controller/
│   └── TableNameController.java
└── react/
    └── TableNameSchema.js
```

## 示例

假设有一个`user`表，包含`id`、`name`、`email`字段。

生成的Entity：

```java
public class User {
    private Integer id;
    private String name;
    private String email;
    // getters and setters
}
```

生成的React Schema：

```javascript
const columns = [
    {
        title: 'Id',
        dataIndex: 'id',
        key: 'id',
    },
    {
        title: 'Name',
        dataIndex: 'name',
        key: 'name',
    },
    {
        title: 'Email',
        dataIndex: 'email',
        key: 'email',
    },
];

export { columns };
```

## 注意事项

- 当前支持MySQL和H2数据库
- 主键字段会被自动识别
- 字段类型映射基于常见SQL类型
- 生成的代码需要根据实际需求进行调整
- H2控制台可通过 http://localhost:8080/h2-console 访问（用户名：sa，密码：空）

## 测试数据

项目包含以下测试表和数据：

### 用户表 (user)
- id: BIGINT (主键)
- username: VARCHAR(50)
- email: VARCHAR(100)
- password: VARCHAR(255)
- full_name: VARCHAR(100)
- age: INT
- created_at, updated_at: TIMESTAMP

### 产品表 (product)
- id: BIGINT (主键)
- name: VARCHAR(200)
- description: TEXT
- price: DECIMAL(10,2)
- category: VARCHAR(50)
- stock_quantity: INT
- is_active: BOOLEAN
- created_at: TIMESTAMP

### 订单表 (order_table)
- id: BIGINT (主键)
- user_id: BIGINT (外键)
- order_number: VARCHAR(50)
- total_amount: DECIMAL(10,2)
- status: VARCHAR(20)
- order_date: TIMESTAMP

### 订单项表 (order_item)
- id: BIGINT (主键)
- order_id: BIGINT (外键)
- product_id: BIGINT (外键)
- quantity: INT
- unit_price: DECIMAL(10,2)
- total_price: DECIMAL(10,2)

## 快速开始

1. 启动应用：`mvn spring-boot:run`
2. 访问 http://localhost:8080/h2-console 查看数据库
3. 调用API生成代码：
   ```bash
   # 获取表列表
   GET /api/generator/tables?schema=PUBLIC
   
   # 生成user表的代码
   POST /api/generator/generate/user?schema=PUBLIC
   ```
4. 查看生成的代码：`generated/` 目录