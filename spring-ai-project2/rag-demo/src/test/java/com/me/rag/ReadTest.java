package com.me.rag;

import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.JsonReader;
import org.springframework.ai.reader.TextReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import java.util.List;

/**
 * ETL 的 Reader
 *
 * @ClassName: ReadTest
 * @Author: JacksonLee
 * @Date: 2026/10/30
 */
@SpringBootTest
public class ReadTest {

     /**
      * 读取 JSON 文件
      *
      * @param resource JSON 文件资源
      * @return void
      */
     @Test
     void testJsonReader(@Value("classpath:/files/web.json")Resource resource) {
          JsonReader jsonReader = new JsonReader(resource, "sites");
          List<Document> documents = jsonReader.get();
          System.out.println(documents.size());
          documents.forEach(document -> {
               System.out.println(document.getText());
          });
     }

     /**
      * 读取 JSON 数组格式文件
      *
      * @param resource JSON 数组格式文件资源
      * @return void
      */
     @Test
     void testJsonReader2(@Value("classpath:/files/webArray.json")Resource resource) {
          JsonReader jsonReader = new JsonReader(resource, "brand", "description");
          List<Document> documents = jsonReader.get();
          System.out.println(documents.size());
          documents.forEach(document -> {
               System.out.println(document.getText());
          });
     }

     /**
      * 读取文本文件
      *
      * @param resource 文本文件资源
      * @return void
      */
     @Test
     void testTextReader(@Value("classpath:/files/text.txt")Resource resource) {
          TextReader textReader = new TextReader(resource);
          List<Document> documents = textReader.get();
          System.out.println(documents.size());
          documents.forEach(document -> {
               System.out.println(document.getText());
          });
     }

     /**
      * 读取文本文件
      *
      * @return void
      */
     @Test
     void testTextReader() {
          TextReader textReader = new TextReader("classpath:/files/text.txt");
          List<Document> documents = textReader.get();
          System.out.println(documents.size());
          documents.forEach(document -> {
               System.out.println(document.getText());
          });
     }
}
