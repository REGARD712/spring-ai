package com.me.chatbot.rag;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class DataInit {

    @Autowired
    private DocumentLoader documentLoader;

    @Autowired
    private KeywordEnricher enricher;

    @Autowired
    private VectorStore vectorStore;

    @PostConstruct
    public void initData() {
        // 1.文档加载
        List<Document> documents = documentLoader.loadMarkdown();
        log.info("加载 {} 个文档", documents.size());

        // 2.文档分割
        CustomTokenTextSplitter tokenSplitter = new CustomTokenTextSplitter();
        List<Document> splitDocs = tokenSplitter.apply(documents);
        log.info("分割为 {} 个文档", splitDocs.size());

        // 3.提取元信息
        List<Document> enrichedDocs = enricher.enrich(splitDocs);
        log.info("添加 {} 个关键词元数据, 文档数: {}", enrichedDocs.size(),  enrichedDocs.size());

        // 4.向量存储
        vectorStore.add(enrichedDocs);
        log.info("向量存储完成");
        log.info("知识库初始化完成");
    }

}
