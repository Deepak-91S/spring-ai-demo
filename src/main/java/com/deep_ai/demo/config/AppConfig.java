package com.deep_ai.demo.config;


import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
//import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.ai.vectorstore.redis.RedisVectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPooled;
//import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class AppConfig {

    @Bean
    public JedisPooled jedisPooled(){
        return new JedisPooled("localhost",6380);
    }

//    //For Simple Vector Store-In-memory
//    @Bean
//    public VectorStore vectorStore(EmbeddingModel embeddingModel){
//        return SimpleVectorStore.builder(embeddingModel).build();
//    }

//PG vector store
//    @Bean
//    public VectorStore vectorStore(JdbcTemplate jdbcTemplate, EmbeddingModel embeddingModel) {
//        return PgVectorStore.builder(jdbcTemplate, embeddingModel).dimensions(1536).build();
//    }

    @Bean
    public  VectorStore vectorStore(JedisPooled jedisPooled, EmbeddingModel embeddingModel){
        return RedisVectorStore.
                builder(jedisPooled,embeddingModel).
                indexName("product-index").
                initializeSchema(true).
                build();
    }



}
