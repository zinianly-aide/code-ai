// 工具函数

// 将驼峰命名转换为下划线命名
export const camelToSnake = (str: string): string => {
  return str.replace(/[A-Z]/g, letter => `_${letter.toLowerCase()}`);
};

// 将下划线命名转换为驼峰命名
export const snakeToCamel = (str: string): string => {
  return str.replace(/_([a-z])/g, (_, letter) => letter.toUpperCase());
};

// 格式化日期
export const formatDate = (date: string | Date): string => {
  if (!date) return '';
  const d = new Date(date);
  return d.toLocaleString('zh-CN');
};

// 格式化货币
export const formatCurrency = (amount: number): string => {
  return new Intl.NumberFormat('zh-CN', {
    style: 'currency',
    currency: 'CNY',
  }).format(amount);
};

// 格式化数字
export const formatNumber = (num: number): string => {
  return new Intl.NumberFormat('zh-CN').format(num);
};

// 生成表格列配置
export const generateTableColumns = (columns: any[]) => {
  return columns.map(col => ({
    title: col.comment || col.columnName,
    dataIndex: snakeToCamel(col.columnName.toLowerCase()),
    key: snakeToCamel(col.columnName.toLowerCase()),
    ellipsis: true,
    sorter: col.primaryKey ? false : true,
  }));
};

// 转换数据格式（数据库字段名转换为驼峰）
export const transformData = (data: any[]): any[] => {
  return data.map(item => {
    const transformed: any = {};
    Object.keys(item).forEach(key => {
      transformed[snakeToCamel(key.toLowerCase())] = item[key];
    });
    return transformed;
  });
};