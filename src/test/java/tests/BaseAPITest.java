package tests;

import config.ApiConfig;
import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import org.aeonbits.owner.ConfigFactory;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.BeforeAll;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static helpers.CustomAllureListener.getFilter;

public abstract class BaseAPITest {
    static ApiConfig config = ConfigFactory.create(ApiConfig.class, System.getProperties());
    protected static String token;

    @BeforeAll
    static void setConfig() {
        RestAssured.baseURI = config.baseUrl();
    }

    @BeforeAll
    static void enableLogging() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
}

    @BeforeAll
    public static void setUp() {
        RestAssured.filters(getFilter());

        try {
            Properties props = new Properties();
            FileInputStream input = new FileInputStream("src/test/resources/config.properties");
            props.load(input);
            token = props.getProperty("api.token");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
