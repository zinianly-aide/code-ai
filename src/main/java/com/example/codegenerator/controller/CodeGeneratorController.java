package com.example.codegenerator.controller;

import com.example.codegenerator.generator.CodeGenerator;
import com.example.codegenerator.model.TableMetadata;
import com.example.codegenerator.service.CodeGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/generator")
public class CodeGeneratorController {

    @Autowired
    private CodeGeneratorService service;

    @Autowired
    private CodeGenerator generator;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/tables")
    public List<TableMetadata> getTables(@RequestParam String schema) throws SQLException {
        return service.getTableMetadata(schema);
    }

    @PostMapping("/generate/{tableName}")
    public String generateCode(@PathVariable String tableName, @RequestParam String schema) throws SQLException {
        return service.getTableMetadata(schema, tableName)
                .map(table -> {
                    try {
                        generator.generateCode(table);
                        return "Code generated for table: " + tableName;
                    } catch (IOException e) {
                        return "Failed to generate code for table: " + tableName;
                    }
                })
                .orElse("Table not found");
    }

    // 新增：获取表数据的API
    @GetMapping("/{tableName}")
    public List<Map<String, Object>> getTableData(
            @PathVariable String tableName,
            @RequestParam(defaultValue = "PUBLIC") String schema
    ) {
        try {
            TableMetadata table = service.getTableMetadata(schema, tableName).orElse(null);
            if (table == null) {
                return List.of();
            }
            // 使用双引号包围表名以处理H2保留字，同时仅允许metadata中存在的表名
            String safeTableName = table.getTableName().replace("\"", "\"\"");
            String sql = "SELECT * FROM \"" + safeTableName + "\"";
            List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);
            System.out.println("Query result for table " + tableName + ": " + result.size() + " rows");
            return result;
        } catch (Exception e) {
            System.out.println("Error querying table " + tableName + ": " + e.getMessage());
            // 如果表不存在或查询失败，返回空列表
            return List.of();
        }
    }

    // 测试数据库连接
    @GetMapping("/test")
    public String testConnection() {
        try {
            Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM \"USER\"", Integer.class);
            return "Database connection successful. User count: " + count;
        } catch (Exception e) {
            return "Database connection failed: " + e.getMessage();
        }
    }
}
