package tests;

import models.ErrorResponseModel;
import models.FlightResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static config.Endpoints.v2PricesWeekMatrix;
import static data.ErrorData.*;
import static helpers.DateHelper.*;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static specs.FlightMatrixSpec.getFlightMatrix200ResponseSpec;
import static specs.GenericErrorSpec.getGenericErrorSpec;

public class GetWeeklyMatrixTest extends BaseAPITest{
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
    @DisplayName("Получение недельной матрицы полетов на определенном маршруте")
    void getWeeklyPriceMatrixTest(String currency, String origin, String destination) {
        String depart_date = getTomorrowDate();
        String return_date = get3WeeksFutureDate();

        FlightResponseModel getFlightResponseModel =
                step("Запрос на получение информации о пользователе", () ->
                        given()
                                .param("currency", currency)
                                .param("origin", origin)
                                .param("destination", destination)
                                .param("depart_date", depart_date)
                                .param("return_date", return_date)
                                .param("limit", 5)
                                .param("token", token)
                        .when()
                                .get(v2PricesWeekMatrix)
                        .then()
                                .statusCode(200)
                                .spec(getFlightMatrix200ResponseSpec))
                                .extract().as(FlightResponseModel.class);

                step("Проверка ответа", () -> {
                    assertTrue(getFlightResponseModel.isSuccess());
                    assertEquals(currency, getFlightResponseModel.getCurrency());
                    assertEquals(origin, getFlightResponseModel.getData().get(0).getOrigin());
                    assertEquals(destination, getFlightResponseModel.getData().get(0).getDestination());
                });
    }
    @Test
    @Tag("Regress")
    @DisplayName("Проверка ошибки слишком большого временного интервала")
    void getErrorWithBigPeriodTest() {
        String depart_date = getTomorrowDate();
        String return_date = get10WeeksFutureDate();

        ErrorResponseModel getErrorResponseModel =
                step("Запрос на получение информации о пользователе", () ->
                        given()
                                .param("currency", "USD")
                                .param("origin", "LDN")
                                .param("destination", "EDI")
                                .param("depart_date", depart_date)
                                .param("return_date", return_date)
                                .param("limit", 5)
                                .param("token", token)
                        .when()
                                .get(v2PricesWeekMatrix)
                        .then()
                                .statusCode(400)
                                .spec(getGenericErrorSpec))
                        .extract().as(ErrorResponseModel.class);

                step("Проверка ответа", () -> {
                    assertFalse(getErrorResponseModel.isSuccess());
                    assertEquals(errorDateInterval, getErrorResponseModel.getError());
                });
    }

    @Test
    @Tag("Regress")
    @DisplayName("Проверка ошибки отсутствия одной из локаций")
    void getErrorWithNoLocationTest() {
        String depart_date = getTomorrowDate();
        String return_date = get3WeeksFutureDate();

        ErrorResponseModel getErrorResponseModel =
                step("Запрос на получение информации о пользователе", () ->
                        given()
                                .param("currency", "USD")
                                .param("origin", "WAW")
                                .param("depart_date", depart_date)
                                .param("return_date", return_date)
                                .param("limit", 5)
                                .param("token", token)
                        .when()
                                .get(v2PricesWeekMatrix)
                        .then()
                                .statusCode(400)
                                .spec(getGenericErrorSpec))
                        .extract().as(ErrorResponseModel.class);

                step("Проверка ответа", () -> {
                    assertFalse(getErrorResponseModel.isSuccess());
                    assertEquals(errorNoLocationCode, getErrorResponseModel.getError());
                });
    }

    @Test
    @Tag("Regress")
    @DisplayName("Проверка ошибки отсутствия обеих локаций")
    void getErrorWithNoLocationsTest() {
        String depart_date = getTomorrowDate();
        String return_date = get3WeeksFutureDate();

        ErrorResponseModel getErrorResponseModel =
                step("Запрос на получение информации о пользователе", () ->
                        given()
                                .param("currency", "USD")
                                .param("depart_date", depart_date)
                                .param("return_date", return_date)
                                .param("limit", 5)
                                .param("token", token)
                        .when()
                                .get(v2PricesWeekMatrix)
                        .then()
                                .statusCode(400)
                                .spec(getGenericErrorSpec))
                        .extract().as(ErrorResponseModel.class);

                step("Проверка ответа", () -> {
                    assertFalse(getErrorResponseModel.isSuccess());
                    assertEquals(errorNoLocations, getErrorResponseModel.getError());
                });
    }

    @Test
    @Tag("Regress")
    @DisplayName("Проверка ошибки при отсутствии параметра return_date")
    void getErrorWithMissingReturnDateTest() {
        String departDate = getTomorrowDate();

        ErrorResponseModel getErrorResponseModel =
                step("Запрос на получение информации о пользователе", () ->
                        given()
                                .param("currency", "USD")
                                .param("origin", "WAW")
                                .param("destination", "EDI")
                                .param("depart_date", departDate)
                                .param("limit", 5)
                                .param("token", token)
                        .when()
                                .get(v2PricesWeekMatrix)
                        .then()
                                .statusCode(400)
                                .spec(getGenericErrorSpec))
                        .extract().as(ErrorResponseModel.class);

                step("Проверка ответа", () -> {
                    assertFalse(getErrorResponseModel.isSuccess());
                    assertEquals(errorNoDateIntersection, getErrorResponseModel.getError());
                });
    }
}
