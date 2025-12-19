# 代码生成器前端

这是一个基于React + TypeScript + Ant Design的前端项目，用于展示后端代码生成器生成的数据表格。

## 功能特性

- 🚀 基于React 18 + TypeScript
- 🎨 使用Ant Design组件库
- 📊 动态表格展示
- 🔄 实时数据刷新
- 🎯 类型安全
- 📱 响应式设计

## 技术栈

- **React 18** - 用户界面库
- **TypeScript** - 类型安全的JavaScript
- **Ant Design** - 企业级UI组件库
- **Axios** - HTTP客户端
- **Create React App** - 项目脚手架

## 项目结构

```
frontend/
├── public/
│   └── index.html          # HTML模板
├── src/
│   ├── components/         # 表格组件
│   │   ├── UserTable.tsx
│   │   ├── ProductTable.tsx
│   │   ├── OrderTable.tsx
│   │   └── OrderItemTable.tsx
│   ├── services/
│   │   └── api.ts          # API服务
│   ├── types/
│   │   └── index.ts        # 类型定义
│   ├── utils/
│   │   └── index.ts        # 工具函数
│   ├── App.tsx             # 主应用组件
│   ├── App.css             # 应用样式
│   ├── index.tsx           # 应用入口
│   └── index.css           # 全局样式
├── package.json
└── README.md
```

## 安装和运行

### 安装依赖

```bash
cd frontend
npm install
```

### 启动开发服务器

```bash
npm start
```

应用将在 http://localhost:3000 启动。

### 构建生产版本

```bash
npm run build
```

## 使用说明

1. **确保后端服务运行**
   ```bash
   # 在后端目录
   mvn spring-boot:run
   ```

2. **启动前端应用**
   ```bash
   cd frontend
   npm start
   ```

3. **查看数据**
   - 从左侧菜单选择数据表
   - 表格会自动加载数据
   - 支持分页、排序、筛选

## 表格组件

### UserTable (用户表)
- 显示用户信息
- 支持编辑、删除操作

### ProductTable (产品表)
- 显示产品信息
- 价格格式化
- 库存状态标签
- 分类筛选

### OrderTable (订单表)
- 显示订单信息
- 状态标签
- 金额格式化
- 日期格式化

### OrderItemTable (订单项表)
- 显示订单明细
- 价格计算

## API集成

前端通过代理配置连接后端API：

```javascript
// package.json 中的代理配置
"proxy": "http://localhost:8080"
```

所有API请求都会自动代理到后端服务。

## 开发说明

### 添加新表格组件

1. 在 `src/types/index.ts` 中定义数据类型
2. 在 `src/components/` 中创建新组件
3. 在 `src/App.tsx` 中添加路由逻辑
4. 更新 `src/services/api.ts` 中的API调用

### 自定义样式

- 全局样式：`src/index.css`
- 组件样式：`src/App.css`
- 主题定制：参考Ant Design文档

## 注意事项

- 确保后端服务在8080端口运行
- 前端开发服务器在3000端口
- CORS已配置允许前端访问后端API
- 生产环境需要配置正确的代理或API地址