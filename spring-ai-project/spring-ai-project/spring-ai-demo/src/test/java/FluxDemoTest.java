import reactor.core.publisher.Flux;

import java.time.Duration;

/**
 * @ClassName: FluxDemoTest
 * @Author: JacksonLee
 * @Date: 2026/9/1
 * @Description: 通过Flux实现流式输出
 */
public class FluxDemoTest {
    public static void main(String[] args) throws InterruptedException {
        // 创建Flux对象，模拟流式输出数据
        Flux<String> fruitFlux = Flux.just("Apple", "Banana", "Cherry", "Grapes")
                .delayElements(Duration.ofSeconds(1)); // 每隔1秒输出一个元素
        // 处理数据并订阅数据
        fruitFlux.map(String::toUpperCase)
                .subscribe(System.out::println);
        // 防止程序处理完成之前，主线程退出
        Thread.sleep(4000);
    }
}
