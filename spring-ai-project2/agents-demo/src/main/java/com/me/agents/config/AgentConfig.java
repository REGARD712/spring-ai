package com.me.agents.config;

import com.me.agents.ChainWorkflow;
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
        return args -> {
            // 定义四个简单明了的处理路线 (更短的提示词)
            Map<String, String> simpleRoutes = Map.of(
                    "财务",
                    "你是财务助手, 请回答账单、扣费、退款等问题. 开头写【财务】. ",
                    "技术",
                    "你是技术支持, 请回答登录、功能使用、系统错误等问题. 开头写【技术】. ",
                    "账户",
                    "你是账户助手, 请处理账号找回、密码重置、安全验证等问题. 开头写【账户】. ",
                    "产品",
                    "你是产品顾问, 请回答功能咨询、如何使用、推荐操作等问题. 开头写【产品】. "
            );
            // 模拟三个非常简短的用户提问 (贴近日常对话)
            List<String> simpleTickets = List.of(
                    "我忘记密码了, 登不进去账号", // → 应路由到"账户"
                    "上个月怎么多扣了50块钱? ", // → 应路由到"财务"
                    "导出数据的功能在哪? 怎么用? " // → 应路由到"产品"
            );

            var routerWorkflow = new RoutingWorkflow(chatClientBuilder.build());

            int i = 1;
            for (String ticket : simpleTickets) {
                System.out.println("\n💬 问题 " + i++);
                System.out.println("------------------------------");
                System.out.println(ticket);
                System.out.println("------------------------------");

                String response = routerWorkflow.route(ticket, simpleRoutes);
                System.out.println(response);
            }
        };
    }
}
