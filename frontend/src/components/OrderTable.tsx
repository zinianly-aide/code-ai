import React from 'react';
import { Table, Button, Space, Tag } from 'antd';
import type { ColumnsType } from 'antd/es/table';
import { OrderTable } from '../types';
import { formatCurrency, formatDate } from '../utils';

interface OrderTableProps {
  data: OrderTable[];
  loading?: boolean;
  onRefresh?: () => void;
}

const OrderTableComponent: React.FC<OrderTableProps> = ({ data, loading = false, onRefresh }) => {
  const getStatusColor = (status?: string) => {
    switch (status?.toLowerCase()) {
      case 'completed':
        return 'green';
      case 'shipped':
        return 'blue';
      case 'processing':
        return 'orange';
      case 'pending':
        return 'gold';
      case 'cancelled':
        return 'red';
      default:
        return 'default';
    }
  };

  const getStatusText = (status?: string) => {
    switch (status?.toLowerCase()) {
      case 'completed':
        return '已完成';
      case 'shipped':
        return '已发货';
      case 'processing':
        return '处理中';
      case 'pending':
        return '待处理';
      case 'cancelled':
        return '已取消';
      default:
        return status || '未知';
    }
  };

  const columns: ColumnsType<OrderTable> = [
    {
      title: 'ID',
      dataIndex: 'id',
      key: 'id',
      width: 80,
      sorter: true,
    },
    {
      title: '订单号',
      dataIndex: 'orderNumber',
      key: 'orderNumber',
      sorter: true,
    },
    {
      title: '用户ID',
      dataIndex: 'userId',
      key: 'userId',
      sorter: true,
      width: 100,
    },
    {
      title: '总金额',
      dataIndex: 'totalAmount',
      key: 'totalAmount',
      render: (amount) => formatCurrency(amount),
      sorter: true,
      width: 120,
    },
    {
      title: '状态',
      dataIndex: 'status',
      key: 'status',
      render: (status) => (
        <Tag color={getStatusColor(status)}>
          {getStatusText(status)}
        </Tag>
      ),
      filters: [
        { text: '待处理', value: 'PENDING' },
        { text: '处理中', value: 'PROCESSING' },
        { text: '已发货', value: 'SHIPPED' },
        { text: '已完成', value: 'COMPLETED' },
        { text: '已取消', value: 'CANCELLED' },
      ],
      onFilter: (value, record) => record.status?.toUpperCase().indexOf(value as string) === 0,
    },
    {
      title: '订单日期',
      dataIndex: 'orderDate',
      key: 'orderDate',
      render: (text) => formatDate(text),
      sorter: true,
    },
    {
      title: '操作',
      key: 'action',
      width: 150,
      render: (_, record) => (
        <Space size="small">
          <Button type="link" size="small">
            查看详情
          </Button>
          <Button type="link" size="small">
            编辑
          </Button>
          <Button type="link" danger size="small">
            取消订单
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
        <Button>新增订单</Button>
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
        scroll={{ x: 1000 }}
      />
    </div>
  );
};

export default OrderTableComponent;