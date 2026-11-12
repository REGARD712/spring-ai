package com.me.agents.config;

import com.me.agents.*;
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
            EvaluatorOptimizer.RefinedResponse refinedResponse = new
                    EvaluatorOptimizer(chatClient)
                    .loop(
                        """
                        我正在找一份专业相关的实习工作, 需要每周去公司3天.
                        如何写一段话, 向辅导员说明情况, 并申请办理实习手续?
                        希望表达出我对学业的重视, 同时说明实习对未来就业的重要性.
                        """
                    );
            System.out.println("FINAL OUTPUT:\n : " + refinedResponse);
        };
    }
}
