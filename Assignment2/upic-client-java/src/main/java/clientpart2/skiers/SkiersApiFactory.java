package clientpart2.skiers;

import io.swagger.client.ApiClient;
import io.swagger.client.api.SkiersApi;

public final class SkiersApiFactory {
//    private static final String BASE_PATH = "http://44.227.82.44:8080/upic";
private final String basePath;

    public SkiersApiFactory(String basePath) {
        this.basePath = basePath;
    }

    public SkiersApi newSkiersApi() {
        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath(basePath);
        SkiersApi skiersApi = new SkiersApi(apiClient);
        return skiersApi;
    }
}