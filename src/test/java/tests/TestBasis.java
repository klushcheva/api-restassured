package tests;

import config.ApiConfig;
import io.restassured.RestAssured;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.BeforeAll;

public abstract class TestBasis {
    static ApiConfig config = ConfigFactory.create(ApiConfig.class, System.getProperties());

    @BeforeAll
    static void beforeAll() {
        RestAssured.baseURI = config.baseUrl();
        // токен сюда?
    }
}
