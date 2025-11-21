package org.example.servicecapteurs.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic containerFullAlertTopic() {
        return TopicBuilder.name("container-full-alert")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic collectionCompletedTopic() {
        return TopicBuilder.name("collection-completed")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic routeOptimizedTopic() {
        return TopicBuilder.name("route-optimized")
                .partitions(3)
                .replicas(1)
                .build();
    }
}