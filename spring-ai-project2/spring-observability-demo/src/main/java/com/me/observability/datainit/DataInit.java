package com.me.observability.datainit;

import jakarta.annotation.PostConstruct;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInit {

    @Autowired
    private VectorStore vectorStore;

    /**
     * 初始化向量数据库
     *
     * @param
     * @return void
     */
    @PostConstruct
    public void init() {
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

        vectorStore.add(List.of(doc, doc2, doc3, doc4));
        System.out.println("向量数据库存储成功");
    }

}
