package com.me.agents.config;

import com.me.agents.ChainWorkflow;
import com.me.agents.OrchestratorWorkers;
import com.me.agents.ParallelizationWorkflow;
import com.me.agents.RoutingWorkflow;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

/**
 * ChainWorkflow 的配置类
 *
 * @ClassName: AgentConfig
 * @Author: JacksonLee
 * @Date: 2026/11/12
 */
@Configuration
public class AgentConfig {

    String report = """
            Q3 季度业务总结：
            本季度客户满意度提升至92分.
            收入同比增长45%.
            主要市场的市场份额达到23%.
            客户流失率从8%下降至5%.
            新客户获取成本为每名用户43元.
            产品使用率达到78%.
            员工满意度评分为87分.
            运营利润率提升至34%.
            """;

    @Bean
    public CommandLineRunner commandLineRunner(ChatClient.Builder chatClientBuilder) {
        var chatClient = chatClientBuilder.build();

        return args -> {
            OrchestratorWorkers.FinalResponse response = new
                    OrchestratorWorkers(chatClient)
                    .process("为一款新型环保可重复使用的保温杯撰写产品介绍文案");

            // 打印最终结果
            System.out.println("\n\n最终聚合结果：\n");
            for (int i = 0; i < response.workerResponses().size(); i++) {
                String style = response.workerResponses().get(i).startsWith("【专业版】") ? "专业" : "亲民";
                        System.out.println(" [" + style + "风格] 输出：\n" +
                                response.workerResponses().get(i) + "\n");
            }
        };
    }
}
