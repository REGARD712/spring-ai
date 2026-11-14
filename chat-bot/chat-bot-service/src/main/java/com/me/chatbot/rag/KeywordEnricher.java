package com.me.chatbot.rag;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.model.transformer.KeywordMetadataEnricher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class KeywordEnricher {

    @Autowired
    private ChatModel chatModel;

    /**
     * 为文档添加关键词元数据
     *
     * @param documents 文档列表
     * @return List<Document> 添加关键词元数据后的文档列表
     */
    public List<Document> enrich(List<Document> documents) {
        KeywordMetadataEnricher enricher = KeywordMetadataEnricher
                .builder(chatModel)
                .keywordCount(5)
                .build();
        return enricher.apply(documents);
    }

}
