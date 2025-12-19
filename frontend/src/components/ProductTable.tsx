import React from 'react';
import { Table, Button, Space, Tag } from 'antd';
import type { ColumnsType } from 'antd/es/table';
import { Product } from '../types';
import { formatCurrency, formatDate } from '../utils';

interface ProductTableProps {
  data: Product[];
  loading?: boolean;
  onRefresh?: () => void;
}

const ProductTable: React.FC<ProductTableProps> = ({ data, loading = false, onRefresh }) => {
  const columns: ColumnsType<Product> = [
    {
      title: 'ID',
      dataIndex: 'id',
      key: 'id',
      width: 80,
      sorter: true,
    },
    {
      title: '产品名称',
      dataIndex: 'name',
      key: 'name',
      sorter: true,
      ellipsis: true,
    },
    {
      title: '描述',
      dataIndex: 'description',
      key: 'description',
      ellipsis: true,
    },
    {
      title: '价格',
      dataIndex: 'price',
      key: 'price',
      render: (price) => formatCurrency(price),
      sorter: true,
      width: 120,
    },
    {
      title: '分类',
      dataIndex: 'category',
      key: 'category',
      filters: [
        { text: '电子产品', value: 'Electronics' },
        { text: '配件', value: 'Accessories' },
        { text: '电器', value: 'Appliances' },
        { text: '运动', value: 'Sports' },
        { text: '家具', value: 'Furniture' },
      ],
      onFilter: (value, record) => record.category?.indexOf(value as string) === 0,
    },
    {
      title: '库存',
      dataIndex: 'stockQuantity',
      key: 'stockQuantity',
      sorter: true,
      width: 100,
      render: (stock) => (
        <Tag color={stock > 50 ? 'green' : stock > 20 ? 'orange' : 'red'}>
          {stock}
        </Tag>
      ),
    },
    {
      title: '状态',
      dataIndex: 'isActive',
      key: 'isActive',
      render: (isActive) => (
        <Tag color={isActive ? 'green' : 'red'}>
          {isActive ? '激活' : '停用'}
        </Tag>
      ),
      filters: [
        { text: '激活', value: true },
        { text: '停用', value: false },
      ],
      onFilter: (value, record) => record.isActive === value,
    },
    {
      title: '创建时间',
      dataIndex: 'createdAt',
      key: 'createdAt',
      render: (text) => formatDate(text),
      sorter: true,
    },
    {
      title: '操作',
      key: 'action',
      width: 120,
      render: (_, record) => (
        <Space size="small">
          <Button type="link" size="small">
            编辑
          </Button>
          <Button type="link" danger size="small">
            删除
          </Button>
        </Space>
      ),
    },
  ];

  return (
    <div>
      <Space style={{ marginBottom: 16 }}>
        <Button type="primary" onClick={onRefresh}>
          刷新
        </Button>
        <Button>新增产品</Button>
      </Space>
      <Table
        columns={columns}
        dataSource={data}
        rowKey="id"
        loading={loading}
        pagination={{
          showSizeChanger: true,
          showQuickJumper: true,
          showTotal: (total, range) =>
            `${range[0]}-${range[1]} 共 ${total} 条`,
        }}
        scroll={{ x: 1200 }}
      />
    </div>
  );
};

export default ProductTable;