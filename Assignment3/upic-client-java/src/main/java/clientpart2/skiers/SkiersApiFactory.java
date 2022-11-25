package clientpart2.skiers;

import com.squareup.okhttp.ConnectionPool;
import io.swagger.client.ApiClient;
import io.swagger.client.api.SkiersApi;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public final class SkiersApiFactory {
    private final String basePath;
    private ApiClient apiClient;
    private SkiersApi skiersApi;

    public SkiersApiFactory(String basePath) {
        this.basePath = basePath;
    }

    public SkiersApi getSkiersApi() {
        if (Objects.isNull(skiersApi)) {
            skiersApi = new SkiersApi(this.getApiClient());
        }
        return skiersApi;
    }

    public ApiClient getApiClient() {
        if (Objects.isNull(apiClient)) {
            apiClient = new ApiClient();
            apiClient.setBasePath(basePath);

            // maximum number of idle connections to each to keep in the pool
            int maxIdleConnections = 20; // default is 5

            // Time in milliseconds to keep the connection alive in the pool before closing it.
            long keepAliveDuration = 5 * 60 * 1000; // 5 min

            apiClient.getHttpClient().setConnectionPool(new ConnectionPool(
                maxIdleConnections, keepAliveDuration, TimeUnit.MILLISECONDS
            ));

            apiClient.getHttpClient().getDispatcher().setMaxRequests(Integer.MAX_VALUE);
            apiClient.getHttpClient().getDispatcher().setMaxRequestsPerHost(Integer.MAX_VALUE);
        }

        return apiClient;
    }
}