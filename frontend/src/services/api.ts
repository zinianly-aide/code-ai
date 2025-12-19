import axios from 'axios';

const api = axios.create({
  baseURL: '/api',
  timeout: 10000,
});

// 请求拦截器
api.interceptors.request.use(
  (config) => {
    // 可以在这里添加认证token等
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// 响应拦截器
api.interceptors.response.use(
  (response) => {
    return response.data;
  },
  (error) => {
    console.error('API Error:', error);
    return Promise.reject(error);
  }
);

export interface TableInfo {
  tableName: string;
  tableComment: string | null;
  columns: ColumnInfo[];
}

export interface ColumnInfo {
  columnName: string;
  columnType: string;
  javaType: string;
  primaryKey: boolean;
  nullable: boolean;
  comment: string | null;
}

export interface ApiResponse<T> {
  data: T;
  message?: string;
  success: boolean;
}

// 获取所有表信息
export const getTables = async (schema: string = 'PUBLIC'): Promise<TableInfo[]> => {
  return api.get(`/generator/tables?schema=${schema}`);
};

// 根据表名获取数据
export const getTableData = async (tableName: string): Promise<any[]> => {
  try {
    return api.get(`/generator/${tableName.toLowerCase()}`);
  } catch (error) {
    // 如果API不存在，返回空数组
    console.warn(`API for table ${tableName} not found, returning empty data`);
    return [];
  }
};

// 生成代码
export const generateCode = async (tableName: string, schema: string = 'PUBLIC'): Promise<string> => {
  return api.post(`/generator/generate/${tableName}?schema=${schema}`);
};

export default api;