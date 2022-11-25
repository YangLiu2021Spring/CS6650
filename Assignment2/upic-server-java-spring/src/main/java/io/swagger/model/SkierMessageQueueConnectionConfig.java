package io.swagger.model;

public class SkierMessageQueueConnectionConfig {
    private String host;
    private Integer port;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private SkierMessageQueueConnectionConfig instance;
        public Builder() {
            this.instance = new SkierMessageQueueConnectionConfig();
        }

        public SkierMessageQueueConnectionConfig getInstance() {
            return this.instance;
        }

        public Builder withHost(String host) {
            this.getInstance().setHost(host);
            return this;
        }

        public Builder withPort(Integer port) {
            this.getInstance().setPort(port);
            return this;
        }

        public SkierMessageQueueConnectionConfig build() {
            return this.getInstance();
        }
    }
}
