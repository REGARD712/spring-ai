package com.me.chatbot.rag;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.markdown.MarkdownDocumentReader;
import org.springframework.ai.reader.markdown.config.MarkdownDocumentReaderConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 载入知识库
 *
 * @ClassName: DocumentLoader
 * @Author: JacksonLee
 * @Date: 2026/11/14
 */
@Slf4j
@Component
public class DocumentLoader {

    @Autowired
    private ResourceLoader resourceLoader;

    /**
     * 获取资源
     *
     * @param location 资源路径
     * @return Resource
     */
    public Resource[] getResources(String location) throws IOException {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(resourceLoader);
        return resolver.getResources(location);
    }

    /**
     * 加载Markdown文件
     *
     * @param
     * @return List<Document> 文档列表
     */
    public List<Document> loadMarkdown() {
        List<Document> documents = new ArrayList<>();

        try {
            // 获取所有Markdown文件
            Resource[] resources = getResources("classpath:/files/*.md");
            log.info("加载到 {} 个Markdown文件", resources.length);
            MarkdownDocumentReaderConfig config = MarkdownDocumentReaderConfig.builder()
                    .withHorizontalRuleCreateDocument(true) // 将水平规则作为文档分隔符
                    .withIncludeCodeBlock(true)             // 包含代码块
                    .withIncludeBlockquote(true)            // 包含引用块
                    .build();
            // 对每个Markdown文件进行读取和解析
            for (Resource resource : resources) {
                // 读取Markdown文件
                log.info("加载文件: {}", resource.getFilename());
                MarkdownDocumentReader reader = new MarkdownDocumentReader(resource, config);
                documents.addAll(reader.get());
            }
        } catch (IOException e) {
            log.error("加载Markdown文件失败, e:", e);
        }

        log.info("文档加载完成");
        return documents;
    }
}
