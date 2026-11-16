package com.me.chatbot.rag;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Component
public class DataInit {

    @Autowired
    private DocumentLoader documentLoader;

    @Autowired
    private KeywordEnricher enricher;

    @Autowired
    private VectorStore vectorStore;

    /*
     * 标记向量数据库是否已完成初始化
     */
    @Value("${data.is-load}")
    private Boolean isLoad;

    /*
     * 固定线程池, 用于并行处理文档
     */
    private final ExecutorService executorService = Executors.newFixedThreadPool(8);

    @PostConstruct
    public void initData() {
        if (!isLoad) {
            log.info("向量数据库已初始化");
            return;
        }

        // 1.文档加载
        List<Document> documents = documentLoader.loadMarkdown();
        log.info("加载 {} 个文档", documents.size());

        // 2.文档分割
        CustomTokenTextSplitter tokenSplitter = new CustomTokenTextSplitter();
        List<Document> splitDocs = tokenSplitter.apply(documents);
        log.info("分割为 {} 个文档", splitDocs.size());

        processDocuments(splitDocs, 10);
    }

    /**
     * 并行处理文档
     *
     * @param documents 文档列表
     * @param batchSize 批量大小
     * @return void
     */
    public void processDocuments(List<Document> documents, int batchSize) {
        // 计算分多少批次
        log.info("开始处理文档, 批量大小: {}", batchSize);
        int batchCount = (int) Math.ceil((double) documents.size() / batchSize);

        // 分批
        List<List<Document>> batches = IntStream.range(0, batchCount)
                .mapToObj(i -> new ArrayList<>(documents.subList(
                        i * batchSize,
                        Math.min((i + 1) * batchSize, documents.size())
                )))
                .collect(Collectors.toList());
        log.info("分批完成, 共 {} 个批次", batches.size());

        // 分批并行处理
        CountDownLatch countDownLatch = new CountDownLatch(batches.size());
        batches.forEach(batch -> {
            executorService.submit(() -> {
                try {
                    // 提取元信息
                    List<Document> enrichedDocs = enricher.enrich(batch);
                    // 向量存储
                    vectorStore.add(enrichedDocs);
                    log.info("批次写入成功, 文档数: {}", enrichedDocs.size());
                } catch (Exception e) {
                    log.error("批次写入失败, ", e);
                } finally {
                    countDownLatch.countDown();
                }
            });
        });

        // 等待所有批次完成
        try {
            if (!countDownLatch.await(15, TimeUnit.MINUTES)) {
                log.warn("文档处理超时, 请检查文档数量是否超过最大处理能力");
            } else {
                log.info("知识库初始化完成");
            }
        } catch (InterruptedException e) {
            log.error("文档处理线程被中断, 请检查线程池配置", e);
            throw new RuntimeException(e);
        }
    }

}
