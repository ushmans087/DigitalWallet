package com.sample.wallet_server.RedisService;

import com.sample.wallet_server.BankDTO.BankAccountDTO;
import com.sample.wallet_server.WalletDTO.PaymentResponseDTO;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableCaching
public class RedisConfig {


    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory factory, ObjectMapper objectMapper) {

        // PaymentResponseDTO serializer
        JacksonJsonRedisSerializer<PaymentResponseDTO> paymentSerializer = new JacksonJsonRedisSerializer<>(objectMapper, PaymentResponseDTO.class);

        RedisCacheConfiguration paymentConfig =
                RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofMinutes(120))
                        .serializeValuesWith(
                                RedisSerializationContext.SerializationPair
                                        .fromSerializer(paymentSerializer)
                        );


        JavaType bankAccountListType = objectMapper.getTypeFactory().constructCollectionType(List.class, BankAccountDTO.class);
        JacksonJsonRedisSerializer<List<BankAccountDTO>> bankAccountSerializer = new JacksonJsonRedisSerializer<>(objectMapper, bankAccountListType);

        RedisCacheConfiguration bankAccountConfig =
                RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofMinutes(120))
                        .serializeValuesWith(
                                RedisSerializationContext.SerializationPair
                                        .fromSerializer(bankAccountSerializer)
                        );


        // Different cache configurations
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();

        cacheConfigurations.put("walletByEmail", paymentConfig);
        cacheConfigurations.put("bankAccountById", bankAccountConfig);

        return RedisCacheManager.builder(factory)
                .cacheDefaults(RedisCacheConfiguration.defaultCacheConfig())
                .withInitialCacheConfigurations(cacheConfigurations)
                .build();
    }
}