package dev.gym.workloadservice.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static dev.gym.workloadservice.rabbitmq.RabbitMQConsts.DEAD_LETTER_BINDING_KEY;
import static dev.gym.workloadservice.rabbitmq.RabbitMQConsts.WORKLOAD_DIRECT_BINDING_KEY;
import static dev.gym.workloadservice.rabbitmq.RabbitMQConsts.WORKLOAD_DIRECT_EXCHANGE;
import static dev.gym.workloadservice.rabbitmq.RabbitMQConsts.WORKLOAD_MESSAGES;
import static dev.gym.workloadservice.rabbitmq.RabbitMQConsts.WORKLOAD_MESSAGES_DLX;

@Configuration
public class RabbitMQTestConfig {

    @Bean
    public DirectExchange direct() {
        return new DirectExchange(WORKLOAD_DIRECT_EXCHANGE);
    }

    @Bean
    public Queue workloadQueue() {
        return QueueBuilder.durable(WORKLOAD_MESSAGES)
                .withArgument("x-dead-letter-exchange", WORKLOAD_MESSAGES_DLX)
                .withArgument("x-dead-letter-routing-key", DEAD_LETTER_BINDING_KEY)
                .build();
    }

    @Bean
    public Binding trainerWorkloadBinding() {
        return BindingBuilder.bind(workloadQueue())
                .to(direct())
                .with(WORKLOAD_DIRECT_BINDING_KEY);
    }

}
