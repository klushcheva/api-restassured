package tests;

import models.ErrorResponseModel;
import models.PlacesMatrixResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;
import java.util.Set;

import static config.Endpoints.v2PricesPlacesMatrix;
import static data.ErrorData.*;
import static helpers.LocationsHelper.getDestinationSets;
import static helpers.LocationsHelper.getOriginSets;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static specs.GenericErrorSpec.getGenericErrorSpec;
import static specs.PlacesMatrixSpec.getPlacesMatrix200ResponseSpec;

public class GetPlacesMatrixTest extends BaseAPITest{
    private final String BAD_LOCATION = "zzz";
    private final String BAD_FLEX = "d";
    private final String BAD_DIST = ".d";
    @ParameterizedTest
    @CsvSource({
            "RUB, MOW, LED",
            "GBP, LON, EDI",
            "JPY, TYO, SEL"
    })
    @Tags({
            @Tag("Smoke"),
            @Tag("Regress")
    })
    @DisplayName("Получение месячной матрицы полетов на определенном маршруте")
    void getMonthlyPriceMatrixTest(String currency, String origin, String destination) {

        PlacesMatrixResponseModel getPlacesMatrixResponseModel =
                step("Запрос на получение информации о пользователе", () ->
                        given()
                                .param("currency", currency)
                                .param("origin", origin)
                                .param("destination", destination)
                                .param("distance", 1000)
                                .param("flexibility", 7)
                                .param("limit", 3)
                                .param("token", token)
                        .when()
                                .get(v2PricesPlacesMatrix)
                        .then()
                                .statusCode(200)
                                .spec(getPlacesMatrix200ResponseSpec))
                        .extract().as(PlacesMatrixResponseModel.class);

        List<Set<String>> destinations = getDestinationSets(getPlacesMatrixResponseModel);
        List<Set<String>> origins = getOriginSets(getPlacesMatrixResponseModel);

        step("Проверка ответа", () -> {
            assertNull(getPlacesMatrixResponseModel.getErrors());
            assertThat("Destinations in the response match the unique 'destinations' set",
                    destinations.get(0), containsInAnyOrder(destinations.get(1).toArray()));
            assertThat("Destinations in the response match the unique 'destinations' set",
                    origins.get(0), containsInAnyOrder(origins.get(1).toArray()));
        });
    }
    @Test
    @Tag("Regress")
    @DisplayName("Получение ошибки при отсутствии точки отправления")
    void getNoOriginTest() {
        ErrorResponseModel getErrorResponseModel =
                step("Запрос на получение информации о пользователе", () ->
                        given()
                                .param("destination", "MOW")
                                .param("distance", 1000)
                                .param("flexibility", 7)
                                .param("limit", 3)
                                .param("token", token)
                                .when()
                                .get(v2PricesPlacesMatrix)
                                .then()
                                .statusCode(400)
                                .spec(getGenericErrorSpec))
                        .extract().as(ErrorResponseModel.class);

        step("Проверка ответа", () -> {
            assertFalse(getErrorResponseModel.isSuccess());
            assertEquals(errorNoOrigin, getErrorResponseModel.getError());
        });
    }

    @Test
    @Tag("Regress")
    @DisplayName("Получение ошибки при отсутствии точки прибытия")
    void getNoDestinationTest() {
        ErrorResponseModel getErrorResponseModel =
                step("Запрос на получение информации о пользователе", () ->
                        given()
                                .param("origin", "MOW")
                                .param("distance", 1000)
                                .param("flexibility", 7)
                                .param("limit", 3)
                                .param("token", token)
                                .when()
                                .get(v2PricesPlacesMatrix)
                                .then()
                                .statusCode(400)
                                .spec(getGenericErrorSpec))
                        .extract().as(ErrorResponseModel.class);

        step("Проверка ответа", () -> {
            assertFalse(getErrorResponseModel.isSuccess());
            assertEquals(errorNoDestination, getErrorResponseModel.getError());
        });
    }

    @Test
    @Tag("Regress")
    @DisplayName("Получение ошибки при неправильном формате точки отправления")
    void getBadOriginTest() {
        ErrorResponseModel getErrorResponseModel =
                step("Запрос на получение информации о пользователе", () ->
                        given()
                                .param("origin", BAD_LOCATION)
                                .param("destination", "LED")
                                .param("distance", 1000)
                                .param("flexibility", 7)
                                .param("limit", 3)
                                .param("token", token)
                                .when()
                                .get(v2PricesPlacesMatrix)
                                .then()
                                .statusCode(400)
                                .spec(getGenericErrorSpec))
                        .extract().as(ErrorResponseModel.class);

        step("Проверка ответа", () -> {
            assertFalse(getErrorResponseModel.isSuccess());
            assertEquals(errorCantFindOrigin + "ZZZ", getErrorResponseModel.getError());
        });
    }

    @Test
    @Tag("Regress")
    @DisplayName("Получение ошибки при неправильном формате точки прибытия")
    void getBadDestinationTest() {
        ErrorResponseModel getErrorResponseModel =
                step("Запрос на получение информации о пользователе", () ->
                        given()
                                .param("origin", "LED")
                                .param("destination", BAD_LOCATION)
                                .param("distance", 1000)
                                .param("flexibility", 7)
                                .param("limit", 3)
                                .param("token", token)
                                .when()
                                .get(v2PricesPlacesMatrix)
                                .then()
                                .statusCode(400)
                                .spec(getGenericErrorSpec))
                        .extract().as(ErrorResponseModel.class);

        step("Проверка ответа", () -> {
            assertFalse(getErrorResponseModel.isSuccess());
            assertEquals(errorCantFindDestination + "ZZZ", getErrorResponseModel.getError());
        });
    }
    @Test
    @Tag("Regress")
    @DisplayName("Получение ошибки при неправильном формате параметра flexibility")
    void getBadFlexibilityTest() {
        ErrorResponseModel getErrorResponseModel =
                step("Запрос на получение информации о пользователе", () ->
                        given()
                                .param("origin", "LED")
                                .param("destination", "KGD")
                                .param("distance", 1000)
                                .param("flexibility", BAD_FLEX)
                                .param("limit", 3)
                                .param("token", token)
                                .when()
                                .get(v2PricesPlacesMatrix)
                                .then()
                                .statusCode(400)
                                .spec(getGenericErrorSpec))
                        .extract().as(ErrorResponseModel.class);

        step("Проверка ответа", () -> {
            assertFalse(getErrorResponseModel.isSuccess());
            assertEquals(errorBadFlexibilityName, getErrorResponseModel.getError());
        });
    }
    @Test
    @Tag("Regress")
    @DisplayName("Получение ошибки при слишком большом значении параметра flexibility")
    void getBigFlexibilityTest() {
        ErrorResponseModel getErrorResponseModel =
                step("Запрос на получение информации о пользователе", () ->
                        given()
                                .param("origin", "LED")
                                .param("destination", "KGD")
                                .param("distance", 1000)
                                .param("flexibility", 8)
                                .param("limit", 3)
                                .param("token", token)
                                .when()
                                .get(v2PricesPlacesMatrix)
                                .then()
                                .statusCode(400)
                                .spec(getGenericErrorSpec))
                        .extract().as(ErrorResponseModel.class);

        step("Проверка ответа", () -> {
            assertFalse(getErrorResponseModel.isSuccess());
            assertEquals(errorBadFlexibility, getErrorResponseModel.getError());
        });
    }
    @Test
    @Tag("Regress")
    @DisplayName("Получение ошибки при неправильном формате параметра distance")
    void getBadDistanceTest() {
        ErrorResponseModel getErrorResponseModel =
                step("Запрос на получение информации о пользователе", () ->
                        given()
                                .param("origin", "LED")
                                .param("destination", "KGD")
                                .param("distance", BAD_DIST)
                                .param("limit", 3)
                                .param("token", token)
                                .when()
                                .get(v2PricesPlacesMatrix)
                                .then()
                                .statusCode(400)
                                .spec(getGenericErrorSpec))
                        .extract().as(ErrorResponseModel.class);

        step("Проверка ответа", () -> {
            assertFalse(getErrorResponseModel.isSuccess());
            assertEquals(errorBadDistance, getErrorResponseModel.getError());
        });
    }

    @Test
    @Tag("Regress")
    @DisplayName("Получение ошибки при слишком маленьком значении параметра distance")
    void getSmallDistanceTest() {
        ErrorResponseModel getErrorResponseModel =
                step("Запрос на получение информации о пользователе", () ->
                        given()
                                .param("origin", "LED")
                                .param("destination", "KGD")
                                .param("distance", "0")
                                .param("limit", 3)
                                .param("token", token)
                                .when()
                                .get(v2PricesPlacesMatrix)
                                .then()
                                .statusCode(400)
                                .spec(getGenericErrorSpec))
                        .extract().as(ErrorResponseModel.class);

        step("Проверка ответа", () -> {
            assertFalse(getErrorResponseModel.isSuccess());
            assertEquals(errorSmallDistance, getErrorResponseModel.getError());
        });
    }

    @Test
    @Tag("Regress")
    @DisplayName("Получение ошибки при слишком большом значении параметра distance")
    void getBigDistanceTest() {
        ErrorResponseModel getErrorResponseModel =
                step("Запрос на получение информации о пользователе", () ->
                        given()
                                .param("origin", "LED")
                                .param("destination", "KGD")
                                .param("distance", 1001)
                                .param("limit", 3)
                                .param("token", token)
                                .when()
                                .get(v2PricesPlacesMatrix)
                                .then()
                                .statusCode(400)
                                .spec(getGenericErrorSpec))
                        .extract().as(ErrorResponseModel.class);

        step("Проверка ответа", () -> {
            assertFalse(getErrorResponseModel.isSuccess());
            assertEquals(errorBigDistance, getErrorResponseModel.getError());
        });
    }
}
