package tests;

import models.ErrorResponseModel;
import models.PopularDirectionsResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static config.Endpoints.v1CityDirections;
import static data.ErrorData.*;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;
import static specs.CityDirectionsSpec.getCityDirections200ResponseSpec;
import static specs.GenericErrorSpec.getGenericErrorSpec;

public class GetCityDirectionsTest extends BaseAPITest{

    private final String BAD_PARAMETER = "123";
    @ParameterizedTest
    @CsvSource({
            "RUB, MOW",
            "USD, LON",
            "JPY, TYO"
    })
    @Tags({
            @Tag("Smoke"),
            @Tag("Regress")
    })
    @DisplayName("Получение популярных направлений из определенного города")
    void getCityDirectionsTest(String currency, String origin){
        PopularDirectionsResponseModel getPopularDirectionsResponseModel =
                step("Получение информации о билетах", () ->
                        given()
                        .param("currency", currency)
                        .param("origin", origin)
                        .param("limit", 3)
                        .param("token", token)
                .when()
                        .get(v1CityDirections)
                .then()
                        .statusCode(200)
                        .spec(getCityDirections200ResponseSpec))
                        .extract().as(PopularDirectionsResponseModel.class);

        step("Проверка ответа", () -> {
            assertTrue(getPopularDirectionsResponseModel.isSuccess());
            assertEquals(currency, getPopularDirectionsResponseModel.getCurrency());
            assertEquals(3, getPopularDirectionsResponseModel.getData().size());
            for (int i = 0; i < 3; i++) {
                String currentOrigin = getPopularDirectionsResponseModel.getData().values().stream()
                        .skip(i)
                        .findFirst()
                        .map(PopularDirectionsResponseModel.FlightData::getOrigin)
                        .orElse(null);
                assertEquals(origin, currentOrigin);
            }
        });
        }

         @Test
         @Tag("Regress")
         @DisplayName("Проверка ошибки при неправильном значении параметра currency")
        void getCityDirectionsBadCurrencyTest() {
         ErrorResponseModel getErrorResponseModel =
                step("Получение информации о билетах", () ->
                    given()
                        .param("currency", BAD_PARAMETER)
                        .param("token", token)
                    .when()
                        .get(v1CityDirections)
                     .then()
                        .statusCode(420)
                        .spec(getGenericErrorSpec)
                        .extract().as(ErrorResponseModel.class));

             step("Проверка ответа", () -> {
                 assertFalse(getErrorResponseModel.isSuccess());
                 assertEquals(errorCurrencyCode01 + BAD_PARAMETER + errorCurrencyCode02 + BAD_PARAMETER, getErrorResponseModel.getError());
             });
         }
        @Test
        @Tag("Regress")
        @DisplayName("Проверка ошибки при неправильном значении параметра origin")
        void getCityDirectionsBadOriginTest() {
            ErrorResponseModel getErrorResponseModel =
                    step("Получение информации о билетах", () ->
                            given()
                                .param("origin", BAD_PARAMETER)
                                .param("token", token)
                            .when()
                                .get(v1CityDirections)
                            .then()
                                .statusCode(400)
                                .spec(getGenericErrorSpec)
                                .extract().as(ErrorResponseModel.class));

            step("Проверка ответа", () -> {
                assertFalse(getErrorResponseModel.isSuccess());
                assertEquals(errorLocationCode + BAD_PARAMETER +"`", getErrorResponseModel.getError());
            });
        }
    }