import React, { useState, useEffect } from 'react';
import { Layout, Menu, Button, Space, message, Spin } from 'antd';
import { DatabaseOutlined, TableOutlined, ReloadOutlined } from '@ant-design/icons';
import UserTable from './components/UserTable';
import ProductTable from './components/ProductTable';
import OrderTable from './components/OrderTable';
import OrderItemTable from './components/OrderItemTable';
import { getTables, getTableData, TableInfo } from './services/api';
import { transformData } from './utils';
import './App.css';

const { Header, Content, Sider } = Layout;

const App: React.FC = () => {
  const [tables, setTables] = useState<TableInfo[]>([]);
  const [selectedTable, setSelectedTable] = useState<string>('');
  const [tableData, setTableData] = useState<any[]>([]);
  const [loading, setLoading] = useState(false);
  const [collapsed, setCollapsed] = useState(false);

  useEffect(() => {
    fetchTables();
  }, []);

  const fetchTables = async () => {
    try {
      setLoading(true);
      const data = await getTables();
      setTables(data);
    } catch (error) {
      message.error('获取表列表失败');
      console.error('Error fetching tables:', error);
    } finally {
      setLoading(false);
    }
  };

  const fetchTableData = async (tableName: string) => {
    try {
      setLoading(true);
      const data = await getTableData(tableName);
      // 转换数据格式（数据库字段名转换为驼峰）
      const transformedData = transformData(data);
      setTableData(transformedData);
    } catch (error) {
      message.error(`获取${tableName}数据失败`);
      console.error('Error fetching table data:', error);
      // 如果API不存在，使用空数据
      setTableData([]);
    } finally {
      setLoading(false);
    }
  };

  const handleTableSelect = (tableName: string) => {
    setSelectedTable(tableName);
    fetchTableData(tableName);
  };

  const handleRefresh = () => {
    if (selectedTable) {
      fetchTableData(selectedTable);
    }
  };

  const renderTable = () => {
    if (!selectedTable) return null;

    const commonProps = {
      data: tableData,
      loading,
      onRefresh: handleRefresh,
    };

    switch (selectedTable.toLowerCase()) {
      case 'user':
        return <UserTable {...commonProps} />;
      case 'product':
        return <ProductTable {...commonProps} />;
      case 'order_table':
        return <OrderTable {...commonProps} />;
      case 'order_item':
        return <OrderItemTable {...commonProps} />;
      default:
        return (
          <div style={{ textAlign: 'center', padding: '50px' }}>
            <p>暂无对应的表格组件</p>
            <p>表名: {selectedTable}</p>
          </div>
        );
    }
  };

  const selectedTableInfo = tables.find(table => table.tableName === selectedTable);

  const menuItems = tables.map(table => ({
    key: table.tableName,
    icon: <TableOutlined />,
    label: table.tableName,
  }));

  return (
    <Layout style={{ minHeight: '100vh' }}>
      <Sider collapsible collapsed={collapsed} onCollapse={setCollapsed}>
        <div className="logo">
          <DatabaseOutlined style={{ fontSize: '20px', marginRight: '8px' }} />
          {!collapsed && '数据表'}
        </div>
        <Menu
          theme="dark"
          selectedKeys={[selectedTable]}
          mode="inline"
          items={menuItems}
          onClick={({ key }) => handleTableSelect(key)}
        />
      </Sider>
      <Layout>
        <Header style={{ padding: 0, background: '#fff' }}>
          <div style={{ padding: '0 24px', fontSize: '18px', fontWeight: 'bold' }}>
            代码生成器前端 - {selectedTable || '请选择数据表'}
          </div>
        </Header>
        <Content style={{ margin: '24px 16px', padding: 24, background: '#fff' }}>
          {loading && !selectedTable ? (
            <div style={{ textAlign: 'center', padding: '50px' }}>
              <Spin size="large" />
            </div>
          ) : selectedTable ? (
            <div>
              <Space style={{ marginBottom: 16 }}>
                <Button
                  type="primary"
                  icon={<ReloadOutlined />}
                  onClick={handleRefresh}
                  loading={loading}
                >
                  刷新数据
                </Button>
                <Button onClick={fetchTables}>
                  重新加载表列表
                </Button>
              </Space>
              {renderTable()}
            </div>
          ) : (
            <div style={{ textAlign: 'center', padding: '50px', color: '#999' }}>
              <TableOutlined style={{ fontSize: '48px', marginBottom: '16px' }} />
              <p>请从左侧选择一个数据表查看数据</p>
            </div>
          )}
        </Content>
      </Layout>
    </Layout>
  );
};

export default App;