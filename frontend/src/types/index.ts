// 通用类型定义
export interface BaseEntity {
  id?: number | string;
  createdAt?: string;
  updatedAt?: string;
}

// 用户类型
export interface User extends BaseEntity {
  username: string;
  email: string;
  password?: string;
  fullName?: string;
  age?: number;
}

// 产品类型
export interface Product extends BaseEntity {
  name: string;
  description?: string;
  price: number;
  category?: string;
  stockQuantity?: number;
  isActive?: boolean;
}

// 订单类型
export interface OrderTable extends BaseEntity {
  userId: number;
  orderNumber: string;
  totalAmount: number;
  status?: string;
  orderDate?: string;
}

// 订单项类型
export interface OrderItem extends BaseEntity {
  orderId: number;
  productId: number;
  quantity: number;
  unitPrice: number;
  totalPrice: number;
}

// 表格列定义
export interface TableColumn {
  title: string;
  dataIndex: string;
  key: string;
  ellipsis?: boolean;
  width?: number | string;
  sorter?: boolean;
  filters?: any[];
}

// API响应类型
export interface ApiResponse<T> {
  data: T;
  message?: string;
  success: boolean;
  total?: number;
}