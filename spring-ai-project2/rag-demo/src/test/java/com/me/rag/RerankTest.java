package com.me.rag;

import com.alibaba.cloud.ai.advisor.RetrievalRerankAdvisor;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.embedding.DashScopeEmbeddingModel;
import com.alibaba.cloud.ai.dashscope.rerank.DashScopeRerankModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import java.util.List;

/**
 * 重排序
 * 1.先粗排:使用向量数据库检索相关文档
 * 2.再细排:使用更精细的排序模型对文档进行排序
 * 3.返回排序后的文档
 *
 * @ClassName: RerankTest
 * @Author: JacksonLee
 * @Date: 2026/11/10
 */
@SpringBootTest
public class RerankTest {

    @Autowired
    private ChatModel chatModel;

    @Autowired
    private DashScopeEmbeddingModel embeddingModel;

    private SimpleVectorStore simpleVectorStore;

    /**
     * 向量数据库在测试之前初始化
     *
     * @param
     * @return void
     */
    @BeforeEach
    void setUp(@Value("classpath:/files/rule.txt") Resource resource) {
        simpleVectorStore = SimpleVectorStore.builder(embeddingModel).build();

        // 文档读取
        TextReader textReader = new TextReader(resource);
        List<Document> documents = textReader.get();

        // 文档分割
        TokenTextSplitter splitter = new TokenTextSplitter();
        List<Document> splittedDocs = splitter.apply(documents);

        // 存入向量数据库
        simpleVectorStore.add(splittedDocs);
        System.out.println("向量数据库存储成功");
    }

    /**
     * 重排序
     *
     * @param
     * @return void
     */
    @Test
    void testRerank(
            @Autowired DashScopeRerankModel rerankModel,
            @Autowired DashScopeChatModel chatModel
    ) {
        // 定义advisor,并提供rerank模型
        RetrievalRerankAdvisor advisor = new RetrievalRerankAdvisor
                (
                        simpleVectorStore,
                        rerankModel,
                        SearchRequest.builder().topK(10).build()
                );

        // 将advisor绑定给chatClient,并调用模型,获取排序后的文档
        ChatClient chatClient = ChatClient.builder(chatModel).build();
        String content = chatClient.prompt()
                .user("金卡打多少折?")
                .advisors(advisor)
                .call()
                .content();
        System.out.println(content);
    }
}
