package com.me.rag;

import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * ETL 的 Transformer 测试类
 *
 * @ClassName: TransformersTest
 * @Author: JacksonLee
 * @Date: 2026/11/2
 */
@SpringBootTest
public class TransformersTest {

    /**
     * Transformers Splitter
     *
     * @param
     * @return void
     */
    @Test
    void testSplitter() {
        // 加载文档
        TextReader textReader = new TextReader("classpath:/files/rule.txt");
        List<Document> documents = textReader.get();
        System.out.println("文档加载, size: " + documents.size());

        // 文本拆分
//        TokenTextSplitter splitter = new TokenTextSplitter();
        TokenTextSplitter splitter = new TokenTextSplitter(
                1000,
                300,
                5,
                5000,
                true
        );
        List<Document> apply = splitter.apply(documents);
        System.out.println("文本拆分, size: " + apply.size());
        apply.forEach(document -> {
            System.out.println("文本拆分, content: " + document.getText());
            System.out.println("文本拆分, metaData: " + document.getMetadata());
        });
    }
}
