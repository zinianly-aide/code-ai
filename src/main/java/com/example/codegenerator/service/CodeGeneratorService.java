package com.example.codegenerator.service;

import com.example.codegenerator.model.ColumnMetadata;
import com.example.codegenerator.model.TableMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CodeGeneratorService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<TableMetadata> getTableMetadata(String schema) throws SQLException {
        List<TableMetadata> tables = new ArrayList<>();
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             ResultSet rs = connection.getMetaData().getTables(null, schema, "%", new String[]{"TABLE"})) {
            DatabaseMetaData metaData = connection.getMetaData();
            while (rs.next()) {
                TableMetadata table = new TableMetadata();
                table.setTableName(rs.getString("TABLE_NAME"));
                table.setTableComment(rs.getString("REMARKS"));
                table.setColumns(getColumnMetadata(metaData, schema, table.getTableName()));
                tables.add(table);
            }
        }
        return tables;
    }

    public Optional<TableMetadata> getTableMetadata(String schema, String tableName) throws SQLException {
        try (Connection connection = jdbcTemplate.getDataSource().getConnection();
             ResultSet rs = connection.getMetaData().getTables(null, schema, tableName, new String[]{"TABLE"})) {
            DatabaseMetaData metaData = connection.getMetaData();
            if (rs.next()) {
                TableMetadata table = new TableMetadata();
                table.setTableName(rs.getString("TABLE_NAME"));
                table.setTableComment(rs.getString("REMARKS"));
                table.setColumns(getColumnMetadata(metaData, schema, table.getTableName()));
                return Optional.of(table);
            }
        }
        return Optional.empty();
    }

    private List<ColumnMetadata> getColumnMetadata(DatabaseMetaData metaData, String schema, String tableName) throws SQLException {
        List<ColumnMetadata> columns = new ArrayList<>();
        Map<String, ColumnMetadata> columnsByName = new HashMap<>();
        try (ResultSet rs = metaData.getColumns(null, schema, tableName, "%")) {
            while (rs.next()) {
                ColumnMetadata column = new ColumnMetadata();
                String columnName = rs.getString("COLUMN_NAME");
                column.setColumnName(columnName);
                column.setColumnType(rs.getString("TYPE_NAME"));
                column.setJavaType(mapToJavaType(rs.getString("TYPE_NAME")));
                column.setNullable(rs.getInt("NULLABLE") == 1);
                column.setComment(rs.getString("REMARKS"));
                columns.add(column);
                columnsByName.put(columnName, column);
            }
        }

        try (ResultSet pkRs = metaData.getPrimaryKeys(null, schema, tableName)) {
            while (pkRs.next()) {
                String pkColumn = pkRs.getString("COLUMN_NAME");
                ColumnMetadata column = columnsByName.get(pkColumn);
                if (column != null) {
                    column.setPrimaryKey(true);
                }
            }
        }

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
