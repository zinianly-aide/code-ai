package com.example.codegenerator.generator;

import com.example.codegenerator.model.ColumnMetadata;
import com.example.codegenerator.model.TableMetadata;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Component
public class CodeGenerator {

    private static final Path BASE_PATH = Paths.get("generated");

    public void generateCode(TableMetadata table) throws IOException {
        generateEntity(table);
        generateMapperXml(table);
        generateMapperInterface(table);
        generateService(table);
        generateController(table);
        generateReactSchema(table);
    }

    private void generateEntity(TableMetadata table) throws IOException {
        String className = toCamelCase(table.getTableName(), true);
        StringBuilder sb = new StringBuilder();
        sb.append("package com.example.entity;\n\n");
        sb.append("import java.math.BigDecimal;\n");
        sb.append("import java.time.LocalDateTime;\n");
        sb.append("import java.util.Date;\n\n");
        sb.append("public class ").append(className).append(" {\n\n");

        for (ColumnMetadata col : table.getColumns()) {
            sb.append("    private ").append(col.getJavaType()).append(" ").append(toCamelCase(col.getColumnName(), false)).append(";\n");
        }

        sb.append("\n    // Getters and Setters\n");
        for (ColumnMetadata col : table.getColumns()) {
            String field = toCamelCase(col.getColumnName(), false);
            String method = toCamelCase(col.getColumnName(), true);
            sb.append("    public ").append(col.getJavaType()).append(" get").append(method).append("() {\n");
            sb.append("        return ").append(field).append(";\n");
            sb.append("    }\n\n");
            sb.append("    public void set").append(method).append("(").append(col.getJavaType()).append(" ").append(field).append(") {\n");
            sb.append("        this.").append(field).append(" = ").append(field).append(";\n");
            sb.append("    }\n\n");
        }

        sb.append("}\n");

        writeToFile(BASE_PATH.resolve("entity").resolve(className + ".java"), sb.toString());
    }

    private void generateMapperXml(TableMetadata table) throws IOException {
        String className = toCamelCase(table.getTableName(), true);
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n");
        sb.append("<mapper namespace=\"com.example.mapper.").append(className).append("Mapper\">\n\n");

        sb.append("    <resultMap id=\"BaseResultMap\" type=\"com.example.entity.").append(className).append("\">\n");
        for (ColumnMetadata col : table.getColumns()) {
            sb.append("        <result column=\"").append(col.getColumnName()).append("\" property=\"").append(toCamelCase(col.getColumnName(), false)).append("\"/>\n");
        }
        sb.append("    </resultMap>\n\n");

        sb.append("    <select id=\"selectByPrimaryKey\" resultMap=\"BaseResultMap\">\n");
        sb.append("        select ");
        for (int i = 0; i < table.getColumns().size(); i++) {
            sb.append(table.getColumns().get(i).getColumnName());
            if (i < table.getColumns().size() - 1) sb.append(", ");
        }
        sb.append(" from ").append(table.getTableName()).append("\n");
        sb.append("        where ");
        for (ColumnMetadata col : table.getColumns()) {
            if (col.isPrimaryKey()) {
                sb.append(col.getColumnName()).append(" = #{").append(toCamelCase(col.getColumnName(), false)).append("}\n");
            }
        }
        sb.append("    </select>\n\n");

        sb.append("</mapper>\n");

        writeToFile(BASE_PATH.resolve("mapper").resolve(className + "Mapper.xml"), sb.toString());
    }

    private void generateMapperInterface(TableMetadata table) throws IOException {
        String className = toCamelCase(table.getTableName(), true);
        StringBuilder sb = new StringBuilder();
        sb.append("package com.example.mapper;\n\n");
        sb.append("import com.example.entity.").append(className).append(";\n\n");
        sb.append("public interface ").append(className).append("Mapper {\n\n");
        sb.append("    ").append(className).append(" selectByPrimaryKey(");
        for (ColumnMetadata col : table.getColumns()) {
            if (col.isPrimaryKey()) {
                sb.append(col.getJavaType()).append(" ").append(toCamelCase(col.getColumnName(), false));
                break;
            }
        }
        sb.append(");\n\n");
        sb.append("}\n");

        writeToFile(BASE_PATH.resolve("mapper").resolve(className + "Mapper.java"), sb.toString());
    }

    private void generateService(TableMetadata table) throws IOException {
        String className = toCamelCase(table.getTableName(), true);
        StringBuilder sb = new StringBuilder();
        sb.append("package com.example.service;\n\n");
        sb.append("import com.example.entity.").append(className).append(";\n");
        sb.append("import com.example.mapper.").append(className).append("Mapper;\n");
        sb.append("import org.springframework.beans.factory.annotation.Autowired;\n");
        sb.append("import org.springframework.stereotype.Service;\n\n");
        sb.append("@Service\n");
        sb.append("public class ").append(className).append("Service {\n\n");
        sb.append("    @Autowired\n");
        sb.append("    private ").append(className).append("Mapper mapper;\n\n");
        sb.append("    public ").append(className).append(" getById(");
        for (ColumnMetadata col : table.getColumns()) {
            if (col.isPrimaryKey()) {
                sb.append(col.getJavaType()).append(" ").append(toCamelCase(col.getColumnName(), false));
                break;
            }
        }
        sb.append(") {\n");
        sb.append("        return mapper.selectByPrimaryKey(");
        for (ColumnMetadata col : table.getColumns()) {
            if (col.isPrimaryKey()) {
                sb.append(toCamelCase(col.getColumnName(), false));
                break;
            }
        }
        sb.append(");\n");
        sb.append("    }\n\n");
        sb.append("}\n");

        writeToFile(BASE_PATH.resolve("service").resolve(className + "Service.java"), sb.toString());
    }

    private void generateController(TableMetadata table) throws IOException {
        String className = toCamelCase(table.getTableName(), true);
        StringBuilder sb = new StringBuilder();
        sb.append("package com.example.controller;\n\n");
        sb.append("import com.example.entity.").append(className).append(";\n");
        sb.append("import com.example.service.").append(className).append("Service;\n");
        sb.append("import org.springframework.beans.factory.annotation.Autowired;\n");
        sb.append("import org.springframework.web.bind.annotation.*;\n\n");
        sb.append("@RestController\n");
        sb.append("@RequestMapping(\"/").append(toCamelCase(table.getTableName(), false)).append("\")\n");
        sb.append("public class ").append(className).append("Controller {\n\n");
        sb.append("    @Autowired\n");
        sb.append("    private ").append(className).append("Service service;\n\n");
        sb.append("    @GetMapping(\"/{id}\")\n");
        sb.append("    public ").append(className).append(" getById(@PathVariable ");
        for (ColumnMetadata col : table.getColumns()) {
            if (col.isPrimaryKey()) {
                sb.append(col.getJavaType()).append(" id) {\n");
                sb.append("        return service.getById(id);\n");
                break;
            }
        }
        sb.append("    }\n\n");
        sb.append("}\n");

        writeToFile(BASE_PATH.resolve("controller").resolve(className + "Controller.java"), sb.toString());
    }

    private void generateReactSchema(TableMetadata table) throws IOException {
        String className = toCamelCase(table.getTableName(), true);
        StringBuilder sb = new StringBuilder();
        sb.append("import { Table } from 'antd';\n\n");
        sb.append("const columns = [\n");
        for (ColumnMetadata col : table.getColumns()) {
            sb.append("    {\n");
            sb.append("        title: '").append(col.getComment() != null ? col.getComment() : col.getColumnName()).append("',\n");
            sb.append("        dataIndex: '").append(toCamelCase(col.getColumnName(), false)).append("',\n");
            sb.append("        key: '").append(toCamelCase(col.getColumnName(), false)).append("',\n");
            sb.append("    },\n");
        }
        sb.append("];\n\n");
        sb.append("export { columns };\n");

        writeToFile(BASE_PATH.resolve("react").resolve(className + "Schema.js"), sb.toString());
    }

    private String toCamelCase(String str, boolean capitalizeFirst) {
        String[] parts = str.split("_");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            String part = parts[i].toLowerCase();
            if (i == 0 && !capitalizeFirst) {
                sb.append(part);
            } else {
                sb.append(part.substring(0, 1).toUpperCase()).append(part.substring(1));
            }
        }
        return sb.toString();
    }

    private void writeToFile(Path path, String content) throws IOException {
        Files.createDirectories(path.getParent());
        Files.writeString(path, content, StandardCharsets.UTF_8);
    }
}
