package com.me.rag.vectorstore;

import com.alibaba.cloud.ai.dashscope.embedding.DashScopeEmbeddingModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
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
public class SimpleVectorStoreTest {
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
    void setUp() {
        simpleVectorStore = SimpleVectorStore.builder(embeddingModel).build();

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

    /**
     * 向量数据库存储
     *
     * @param
     * @return void
     */
//    @Test
//    void save() {
//        Document doc = Document.builder()
//                .text("2025年夏季奥运会将于巴黎举行, 预计吸引全球数百万观众")
//                .build();
//        Document doc2 = Document.builder()
//                .text("对比学习框架下多语言BERT模型的语义表示分析")
//                .build();
//        Document doc3 = Document.builder()
//                .text("暮色中的老槐树在风中摇曳, 枯枝划破绯红的晚霞")
//                .build();
//        Document doc4 = Document.builder()
//                .text("基于Transformer的预训练模型在机器翻译中的迁移学习研究")
//                .build();
//        simpleVectorStore.add(List.of(doc, doc2, doc3, doc4));
//        System.out.println("向量数据库存储成功");
//    }

    /**
     * 相似性检索
     *
     * @param
     * @return void
     */
    @Test
    void search() {
//        List<Document> documents = simpleVectorStore.similaritySearch("机器学习");
//        documents.forEach(System.out::println);

        SearchRequest searchRequest = SearchRequest.builder()
                .query("机器学习")
                .topK(5)
                .similarityThreshold(0.45)
                .build();
        List<Document> documents = simpleVectorStore.similaritySearch(searchRequest);
        documents.forEach(System.out::println);
    }

    /**
     * rag
     *
     * @param
     * @return void
     */
    @Test
    void rag() {
        ChatClient chatClient = ChatClient.builder(chatModel).build();
        QuestionAnswerAdvisor questionAnswerAdvisor = QuestionAnswerAdvisor.builder(simpleVectorStore).build();
        String message = "奥运会什么时候举行?";
        String content = chatClient.prompt()
                .advisors(questionAnswerAdvisor)
                .user(message)
                .call()
                .content();
        System.out.println(content);
    }

    /**
     * rag2
     *
     * @param
     * @return void
     */
    @Test
    void rag2() {
        ChatClient chatClient = ChatClient.builder(chatModel).build();
        String message = "奥运会什么时候举行?";
        QuestionAnswerAdvisor questionAnswerAdvisor = QuestionAnswerAdvisor
                .builder(simpleVectorStore)
                .searchRequest(SearchRequest.builder()
                        .query(message)
                        .similarityThreshold(0.45)
                        .topK(5)
                        .build())
                .build();
        String content = chatClient.prompt()
                .advisors(questionAnswerAdvisor)
                .user(message)
                .call()
                .content();
        System.out.println(content);
    }

    /**
     * rag3
     *
     * @param
     * @return void
     */
    @Test
    void rag3() {
        ChatClient chatClient = ChatClient.builder(chatModel).build();
        String message = "中国的首都是哪里?";
        PromptTemplate template = new PromptTemplate("""
                {query}
                         下面是上下文信息
                ---------------------
                {question_answer_context}
                ---------------------
                         根据给定的上下文信息进行回复, 并遵守以下规则:
                         1. 如果上下文信息中有相关的知识, 使用上下文的信息进行回复
                         2. 如果上下文信息中没有相关的知识, 直接回复"不知道"
                         3. 在回复时, 尽量避免使用"根据提供的信息...", "根据上下文信息..."类似的话语
                """);
        QuestionAnswerAdvisor questionAnswerAdvisor = QuestionAnswerAdvisor
                .builder(simpleVectorStore)
                .promptTemplate(template)
                .searchRequest(SearchRequest.builder()
                        .query(message)
                        .similarityThreshold(0.45)
                        .topK(5)
                        .build())
                .build();
        String content = chatClient.prompt()
                .advisors(questionAnswerAdvisor)
                .user(message)
                .call()
                .content();
        System.out.println(content);
    }
}
