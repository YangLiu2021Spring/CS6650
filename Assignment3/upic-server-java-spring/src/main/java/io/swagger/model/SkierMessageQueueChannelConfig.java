package io.swagger.model;

public class SkierMessageQueueChannelConfig extends BasicObjectPoolConfig {
    private String queueName;

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public static Builder builder() {
        return new Builder(SkierMessageQueueChannelConfig.class);
    }

    public static class Builder extends BasicObjectPoolConfig.Builder<SkierMessageQueueChannelConfig, Builder> {
        public Builder(Class<SkierMessageQueueChannelConfig> configClass) {
            super(configClass);
        }

        public Builder withQueueName(String queueName) {
            this.getInstance().setQueueName(queueName);
            return this;
        }
    }
}
