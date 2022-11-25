package io.swagger.model;

public class BasicObjectPoolConfig {
    private int maxTotal;
    private int maxIdle;
    private int minIdle;

    public int getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public static class Builder<T extends BasicObjectPoolConfig, B extends Builder<T, B>> {
        private T instance;

        public T getInstance() {
            return this.instance;
        }

        public Builder(Class<T> tClass) {
            try {
                this.instance = tClass.newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };

        public B withMaxTotal(int maxTotal) {
            this.instance.setMaxTotal(maxTotal);
            return (B) this;
        }

        public B withMinIdle(int minIdle) {
            this.instance.setMinIdle(minIdle);
            return (B) this;
        }

        public B withMaxIdle(int maxIdle) {
            this.instance.setMaxIdle(maxIdle);
            return (B) this;
        }

        public T build() {
            return this.instance;
        }
    }
}
