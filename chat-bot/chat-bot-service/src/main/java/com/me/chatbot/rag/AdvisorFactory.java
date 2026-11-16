package com.me.chatbot.rag;

import com.alibaba.cloud.ai.advisor.RetrievalRerankAdvisor;
import com.alibaba.cloud.ai.model.RerankModel;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;

public class AdvisorFactory {

    /**
     * 创建一个问题回答advisor
     *
     * @param vectorStore 向量数据库
     * @return QuestionAnswerAdvisor
     */
    public static QuestionAnswerAdvisor createQuestionAnswerAdvisor(VectorStore vectorStore) {
        PromptTemplate promptTemplate = new PromptTemplate("""
                【用户问题信息】
                {query}
                【上下文信息】
                {question_answer_context}
                
                回答请遵循以下规则：
                1. 若上下文信息中包含与用户问题直接相关的内容，即使表述方式略有不同，也应通过语义理解判断其相关性，并据此进行回复。
                2. 在上下文中确实无法找到与问题相关的任何信息时，请告知用户联系专业课程顾问。
                3. 回复时直接给出答案或解决方案，避免使用“根据您提供的信息”、“根据上下文”等冗余表达。
                确保系统具备基本的语义匹配能力，不能仅依赖字面完全匹配来判断信息相关性
                
                请参考以下对话示例的风格和方式回应客户:
                ---
                【客户】如果来不及听直播的话, 可以回放吗?
                【助手】当然可以！如果您无法按时参加直播课程，完全不用担心。 
                所有直播课程都会全程录制并生成回放视频，您可以在【比特课堂】学习平台上随时查看，支持反复观看、倍速播放等功能。
                无论因何原因错过直播，都不会影响您的学习进度.
                """);
        return QuestionAnswerAdvisor
                .builder(vectorStore)
                .promptTemplate(promptTemplate)
                .build();
    }

    /**
     * 创建一个重排序advisor
     *
     * @param vectorStore 向量数据库
     * @param rerankModel 重排序模型
     * @return Advisor
     */
    public static Advisor createRerankAdvisor(
            VectorStore vectorStore,
            RerankModel rerankModel
    ) {
        return new RetrievalRerankAdvisor(
                vectorStore,
                rerankModel,
                SearchRequest.builder()
                        .topK(100)
                        .similarityThreshold(0.3)
                        .build()
        );
    }

}
