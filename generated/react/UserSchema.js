import { Table } from 'antd';

const columns = [
    {
        title: 'ID',
        dataIndex: 'id',
        key: 'id',
    },
    {
        title: 'USERNAME',
        dataIndex: 'username',
        key: 'username',
    },
    {
        title: 'EMAIL',
        dataIndex: 'email',
        key: 'email',
    },
    {
        title: 'PASSWORD',
        dataIndex: 'password',
        key: 'password',
    },
    {
        title: 'FULL_NAME',
        dataIndex: 'fullName',
        key: 'fullName',
    },
    {
        title: 'AGE',
        dataIndex: 'age',
        key: 'age',
    },
    {
        title: 'CREATED_AT',
        dataIndex: 'createdAt',
        key: 'createdAt',
    },
    {
        title: 'UPDATED_AT',
        dataIndex: 'updatedAt',
        key: 'updatedAt',
    },
];

export { columns };
