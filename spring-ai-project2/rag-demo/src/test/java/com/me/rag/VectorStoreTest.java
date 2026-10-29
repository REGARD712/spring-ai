package com.me.rag;

import com.alibaba.cloud.ai.dashscope.embedding.DashScopeEmbeddingModel;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * 向量数据库 SimpleVectorStore 测试
 *
 * @ClassName: VectorStoreTest
 * @Author: JacksonLee
 * @Date: 2026/10/29
 */
@SpringBootTest
public class VectorStoreTest {
    @Autowired
    private DashScopeEmbeddingModel embeddingModel;

    private SimpleVectorStore simpleVectorStore;

    @BeforeEach
    void setUp() {
        simpleVectorStore = SimpleVectorStore.builder(embeddingModel).build();
    }

    @Test
    void save() {
        Document doc = Document.builder()
                .text("2025年夏季奥运会将于巴黎举行, 预计吸引全球数百万观众")
                .build();
        Document doc2 = Document.builder()
                .text("对比学习框架下多语言BERT模型的语义表示分析")
                .build();
        Document doc3 = Document.builder()
                .text("暮色中的老槐树在风中摇曳, 枯枝划破绯红的晚霞")
                .build();
        Document doc4 = Document.builder()
                .text("基于Transformer的预训练模型在机器翻译中的迁移学习研究")
                .build();
        simpleVectorStore.add(List.of(doc, doc2, doc3, doc4));
        System.out.println("向量数据库存储成功");
    }
}
