package com.me.rag;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.model.transformer.KeywordMetadataEnricher;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private ChatModel chatModel;

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

    /**
     * 关键词提取: KeywordMetadataEnricher
     *
     * @param
     * @return void
     */
    @Test
    void testEnricher() {
        // 加载文档
        TextReader textReader = new TextReader("classpath:/files/rule.txt");
        List<Document> documents = textReader.get();
        System.out.println("文档加载, size: " + documents.size());

        // 文本拆分
        TokenTextSplitter splitter = new TokenTextSplitter(
                1000,
                300,
                5,
                5000,
                true
        );
        List<Document> splitterDocs = splitter.apply(documents);
        System.out.println("文本拆分, size: " + splitterDocs.size());

        // 关键词提取
        KeywordMetadataEnricher enricher = KeywordMetadataEnricher.builder(chatModel)
//                .keywordCount(3)
                .keywordsTemplate(new PromptTemplate("""
                        根据给定的文本: {context_str}, 生成关键字, 只允许以下关键字
                        [会员宗旨, 会员类型, 会员注册, 积分制度, 会员权益, 会员行为, 会员服务, 公告, 隐私保护]
                        只返回关键字, 其他信息不返回
                        """))
                .build();
        List<Document> enricherDocs = enricher.apply(splitterDocs);
        System.out.println("关键词提取, size: " + enricherDocs.size());
        enricherDocs.forEach(document -> {
            System.out.println("关键词提取, content: " + document.getText());
            System.out.println("关键词提取, metaData: " + document.getMetadata());
        });
    }
}
