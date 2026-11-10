package com.me.rag;

import org.junit.jupiter.api.Test;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

/**
 * 向量模型 Embedding 测试
 *
 * @ClassName: TextEmbeddingTest
 * @Author: JacksonLee
 * @Date: 2026/10/29
 */
@SpringBootTest
public class TextEmbeddingTest {
    private final EmbeddingModel embeddingModel;

    @Autowired
    public TextEmbeddingTest(EmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
    }

    @Test
    void embedding() {
        float[] embed = embeddingModel.embed("Hello RAG!");
        System.out.println(embed.length);
        System.out.println(Arrays.toString(embed));
    }

    @Test
    void batchEmbedding() {
        List<float[]> embeddings = embeddingModel.embed(List.of("Hello RAG", "Hello Spring AI"));
        System.out.println(embeddings.get(0).length);
        System.out.println(Arrays.toString(embeddings.get(0)));
        System.out.println(embeddings.get(1).length);
        System.out.println(Arrays.toString(embeddings.get(1)));
    }
}
