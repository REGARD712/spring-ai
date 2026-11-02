package com.me.rag;

import com.alibaba.cloud.ai.reader.mysql.MySQLDocumentReader;
import com.alibaba.cloud.ai.reader.mysql.MySQLResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Spring AI Alibaba 的 MySQL 文档读取器测试类
 *
 * @ClassName: MySQLDocumentReaderTest
 * @Author: JacksonLee
 * @Date: 2026/11/2
 */
@SpringBootTest
public class MySQLDocumentReaderTest {
    private MySQLResource mysqlResource;

    private MySQLDocumentReader reader;

    @BeforeEach
    void setUp() {
        String query = "SELECT * FROM user LIMIT 10;";

        List<String> contentColumns = List.of("User", "Host");
        List<String> metadataColumns = List.of("User", "Host");

        // Setup test MySQL resource
        mysqlResource = new MySQLResource(
                "localhost",
                3306,
                "mysql",
                "root",
                "1308670712.",
                query,
                contentColumns,
                metadataColumns);

        reader = new MySQLDocumentReader(mysqlResource);
    }

    @Test
    void testGetDocuments() {
        List<Document> documents = reader.get();
        System.out.println(documents.size());
        documents.forEach(document -> {
            System.out.println(document.getText());
            System.out.println(document.getMetadata());
        });
    }

    @Test
    void testInvalidConnection() {
        // Test with invalid credentials
        MySQLResource invalidResource = new MySQLResource("invalid_host", 3306, "invalid_db", "invalid_user",
                "invalid_pass", "SELECT * FROM test_table", null, null);

        MySQLDocumentReader invalidReader = new MySQLDocumentReader(invalidResource);

        // Should throw RuntimeException
        assertThrows(RuntimeException.class, invalidReader::get);
    }
}
