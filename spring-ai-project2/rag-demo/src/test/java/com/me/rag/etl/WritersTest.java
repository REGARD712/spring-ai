package com.me.rag.etl;

import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.MetadataMode;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.writer.FileDocumentWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import java.util.List;

/**
 * ETL-Load: Writer测试类
 *
 * @ClassName: WriterTest
 * @Author: JacksonLee
 * @Date: 2026/11/9
 */
@SpringBootTest
public class WritersTest {

    /**
     * 写入文件File
     * FileDocumentWriter 测试
     *
     * @param resource 输入文件资源
     * @return void
     */
    @Test
    void testFileWriter(@Value("classpath:/files/rule.txt") Resource resource) {
        // 读取文本(Extract)
        TextReader textReader = new TextReader(resource);
        List<Document> documents = textReader.get();

        // 文档分割(Transform)
        TokenTextSplitter splitter = new TokenTextSplitter();
        List<Document> splitterDocs = splitter.apply(documents);

        // 写入文档(Load)
        FileDocumentWriter fileWriter = new FileDocumentWriter
                (
                        "output.txt",
                        true,
                        MetadataMode.ALL,
                        true
                );
        fileWriter.accept(splitterDocs);
        System.out.println("文档写入完成");
    }

    // 写入向量数据库
    private final SimpleVectorStore simpleVectorStore;

    @Autowired
    public WritersTest(EmbeddingModel embeddingModel) {
        this.simpleVectorStore = SimpleVectorStore.builder(embeddingModel).build();
    }

    /**
     * SimpleVectorStore 测试
     *
     * @param resource 输入文件资源
     * @return void
     */
    @Test
    void testSimpleVectorStore(@Value("classpath:/files/rule.txt") Resource resource) {
        // 读取文本(Extract)
        TextReader textReader = new TextReader(resource);
        List<Document> documents = textReader.get();

        // 文档分割(Transform)
        TokenTextSplitter splitter = new TokenTextSplitter();
        List<Document> splitterDocs = splitter.apply(documents);

        // 写入向量数据库(Load)
        simpleVectorStore.add(splitterDocs);
        System.out.println("向量数据库写入完成");
    }
}
