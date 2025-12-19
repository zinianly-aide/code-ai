import React from 'react';
import { Table, Button, Space } from 'antd';
import type { ColumnsType } from 'antd/es/table';
import { OrderItem } from '../types';
import { formatCurrency } from '../utils';

interface OrderItemTableProps {
  data: OrderItem[];
  loading?: boolean;
  onRefresh?: () => void;
}

const OrderItemTableComponent: React.FC<OrderItemTableProps> = ({ data, loading = false, onRefresh }) => {
  const columns: ColumnsType<OrderItem> = [
    {
      title: 'ID',
      dataIndex: 'id',
      key: 'id',
      width: 80,
      sorter: true,
    },
    {
      title: '订单ID',
      dataIndex: 'orderId',
      key: 'orderId',
      sorter: true,
      width: 100,
    },
    {
      title: '产品ID',
      dataIndex: 'productId',
      key: 'productId',
      sorter: true,
      width: 100,
    },
    {
      title: '数量',
      dataIndex: 'quantity',
      key: 'quantity',
      sorter: true,
      width: 80,
    },
    {
      title: '单价',
      dataIndex: 'unitPrice',
      key: 'unitPrice',
      render: (price) => formatCurrency(price),
      sorter: true,
      width: 120,
    },
    {
      title: '总价',
      dataIndex: 'totalPrice',
      key: 'totalPrice',
      render: (price) => formatCurrency(price),
      sorter: true,
      width: 120,
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
        <Button>新增订单项</Button>
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
        scroll={{ x: 800 }}
      />
    </div>
  );
};

export default OrderItemTableComponent;