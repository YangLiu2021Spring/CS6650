package cs6650.fall.assignment.skiers;

import io.swagger.client.ApiClient;
import io.swagger.client.api.SkiersApi;

public final class SkiersApiFactory {
    //private static final String BASE_PATH = "http://192.168.68.101/upic";
    private static final String BASE_PATH = "http://44.227.82.44:8080/upic";

    /**
     * to create new instance of SkiersApi, It’s recommended to create an instance of ApiClient per thread in a
     * multithreaded environment to avoid any potential issues.
     * @return {@link SkiersApi}
     */
    public static SkiersApi newSkiersApi() {
        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath(BASE_PATH);
        SkiersApi skiersApi = new SkiersApi(apiClient);
        return skiersApi;
    }

    private SkiersApiFactory() {
        throw new UnsupportedOperationException();
    }
}
