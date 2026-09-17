package com.sample.payment_server.RedisService;

import com.sample.payment_server.Transactions.TransactionResponseDTO;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.util.*;

@Configuration
@EnableCaching
public class RedisConfig {

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory factory, ObjectMapper objectMapper) {

        JavaType transactionListType = objectMapper.getTypeFactory().constructCollectionType(List.class, TransactionResponseDTO.class);
        JacksonJsonRedisSerializer<List<TransactionResponseDTO>> transactionSerializer = new JacksonJsonRedisSerializer<>(objectMapper, transactionListType);

        RedisCacheConfiguration transactionConfig =
                RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofMinutes(120))
                        .serializeValuesWith(
                                RedisSerializationContext.SerializationPair
                                        .fromSerializer(transactionSerializer)
                        );

        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();

        cacheConfigurations.put("getTransactionById", transactionConfig);
        cacheConfigurations.put("getRecentTransactionById", transactionConfig);

        return RedisCacheManager.builder(factory)
                .cacheDefaults(
                        RedisCacheConfiguration.defaultCacheConfig()
                )
                .withInitialCacheConfigurations(cacheConfigurations)
                .build();
    }
}
