package com.example.codegenerator.service;

import com.example.codegenerator.model.ColumnMetadata;
import com.example.codegenerator.model.TableMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CodeGeneratorService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<TableMetadata> getTableMetadata(String schema) throws SQLException {
        List<TableMetadata> tables = new ArrayList<>();
        DatabaseMetaData metaData = jdbcTemplate.getDataSource().getConnection().getMetaData();
        ResultSet rs = metaData.getTables(null, schema, "%", new String[]{"TABLE"});

        while (rs.next()) {
            TableMetadata table = new TableMetadata();
            table.setTableName(rs.getString("TABLE_NAME"));
            table.setTableComment(rs.getString("REMARKS"));
            table.setColumns(getColumnMetadata(metaData, schema, table.getTableName()));
            tables.add(table);
        }
        rs.close();
        return tables;
    }

    private List<ColumnMetadata> getColumnMetadata(DatabaseMetaData metaData, String schema, String tableName) throws SQLException {
        List<ColumnMetadata> columns = new ArrayList<>();
        ResultSet rs = metaData.getColumns(null, schema, tableName, "%");

        while (rs.next()) {
            ColumnMetadata column = new ColumnMetadata();
            column.setColumnName(rs.getString("COLUMN_NAME"));
            column.setColumnType(rs.getString("TYPE_NAME"));
            column.setJavaType(mapToJavaType(rs.getString("TYPE_NAME")));
            column.setNullable(rs.getInt("NULLABLE") == 1);
            column.setComment(rs.getString("REMARKS"));
            columns.add(column);
        }
        rs.close();

        // Check primary keys
        ResultSet pkRs = metaData.getPrimaryKeys(null, schema, tableName);
        while (pkRs.next()) {
            String pkColumn = pkRs.getString("COLUMN_NAME");
            columns.stream().filter(c -> c.getColumnName().equals(pkColumn)).forEach(c -> c.setPrimaryKey(true));
        }
        pkRs.close();

        return columns;
    }

    private String mapToJavaType(String sqlType) {
        switch (sqlType.toUpperCase()) {
            case "VARCHAR":
            case "CHAR":
            case "TEXT":
                return "String";
            case "INT":
            case "INTEGER":
                return "Integer";
            case "BIGINT":
                return "Long";
            case "DECIMAL":
            case "NUMERIC":
                return "BigDecimal";
            case "DATE":
                return "Date";
            case "DATETIME":
            case "TIMESTAMP":
                return "LocalDateTime";
            case "BOOLEAN":
                return "Boolean";
            default:
                return "String";
        }
    }
}