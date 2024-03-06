package dev.gym.workloadservice.rabbitmq;

public class RabbitMQConsts {
    public static final String WORKLOAD_DIRECT_EXCHANGE = "workload.direct";
    public static final String WORKLOAD_MESSAGES_DLX = "workload.messages.dlx";
    public static final String WORKLOAD_DIRECT_BINDING_KEY = "workload.trainer";
    public static final String WORKLOAD_MESSAGES = "workload.messages";
    public static final String DEAD_LETTER_BINDING_KEY = "dead.letter";
    public static final String WORKLOAD_MESSAGES_DLQ = "workload.messages.dlq";
}
