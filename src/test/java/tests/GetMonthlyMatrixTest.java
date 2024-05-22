package tests;

import models.ErrorResponseModel;
import models.FlightResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static config.Endpoints.v2PricesMonthMatrix;
import static config.Endpoints.v2PricesWeekMatrix;
import static data.ErrorData.*;
import static helpers.DateHelper.get10WeeksFutureDate;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static specs.FlightMatrixSpec.getFlightMatrix200ResponseSpec;
import static specs.GenericErrorSpec.getGenericErrorSpec;

public class GetMonthlyMatrixTest extends BaseAPITest {
    private final String BAD_CUR = "zzz";
    private final String CUR_NUM = "1234";
    private final String BAD_LOCATION = "TNQL";
    private final Integer LONG_DURATION = 31;
    private final Integer SHORT_DURATION = -1;

    @ParameterizedTest
    @CsvSource({
            "RUB, MOW, LED, 1",
            "GBP, LON, EDI, 3",
            "JPY, TYO, SEL, 9"
    })
    @Tags({
            @Tag("Smoke"),
            @Tag("Regress")
    })
    @DisplayName("Получение месячной матрицы полетов на определенном маршруте")
    void getMonthlyPriceMatrixTest(String currency, String origin, String destination, int duration) {
        FlightResponseModel getFlightResponseModel =
                step("Запрос на получение информации о пользователе", () ->
                        given()
                                .param("currency", currency)
                                .param("origin", origin)
                                .param("destination", destination)
                                .param("month", get10WeeksFutureDate())
                                .param("trip_duration", duration)
                                .param("limit", 3)
                                .param("token", token)
                        .when()
                                .get(v2PricesMonthMatrix)
                        .then()
                                .statusCode(200)
                                .spec(getFlightMatrix200ResponseSpec))
                        .extract().as(FlightResponseModel.class);

        step("Проверка ответа", () -> {
            assertTrue(getFlightResponseModel.isSuccess());
            assertEquals(currency, getFlightResponseModel.getCurrency());
            assertEquals(3, getFlightResponseModel.getData().size());
            for (int i = 0; i < 3; i++) {
                assertTrue(getFlightResponseModel.getData().get(i).isActual());
                assertEquals(origin, getFlightResponseModel.getData().get(i).getOrigin());
                assertEquals(destination, getFlightResponseModel.getData().get(i).getDestination());
            }
        });
    }

    @Test
    @Tag("Regress")
    @DisplayName("Проверка ошибки неверного значения параметра currency")
    void getMonthlyMatrixBadCurrencyTest() {
        ErrorResponseModel getErrorResponseModel =
                step("Запрос на получение информации о пользователе", () ->
                        given()
                                .param("currency", BAD_CUR)
                                .param("origin", "MOW")
                                .param("destination","LED")
                                .param("month", get10WeeksFutureDate())
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
            assertEquals(errorBadCurrency01 + BAD_CUR + errorBadCurrency02 + BAD_CUR, getErrorResponseModel.getError());
        });
    }

    @Test
    @Tag("Regress")
    @DisplayName("Проверка ошибки слишком длинного значения параметра currency")
    void getMonthlyMatrixBadCurrencyLengthTest() {
        ErrorResponseModel getErrorResponseModel =
                step("Запрос на получение информации о пользователе", () ->
                        given()
                                .param("currency", CUR_NUM)
                                .param("origin", "MOW")
                                .param("destination","LED")
                                .param("month", get10WeeksFutureDate())
                                .param("limit", 5)
                                .param("token", token)
                        .when()
                                .get(v2PricesMonthMatrix)
                        .then()
                                .statusCode(400)
                                .spec(getGenericErrorSpec))
                        .extract().as(ErrorResponseModel.class);

        step("Проверка ответа", () -> {
            assertFalse(getErrorResponseModel.isSuccess());
            assertEquals(errorBadCurrencyLength, getErrorResponseModel.getError());
        });
    }

    @Test
    @Tag("Regress")
    @DisplayName("Проверка ошибки слишком длинного значения параметра origin")
    void getMonthlyMatrixBadOriginTest() {
        ErrorResponseModel getErrorResponseModel =
                step("Запрос на получение информации о пользователе", () ->
                        given()
                                .param("currency", "RUB")
                                .param("origin", BAD_LOCATION)
                                .param("destination","LED")
                                .param("month", get10WeeksFutureDate())
                                .param("limit", 5)
                                .param("token", token)
                        .when()
                                .get(v2PricesMonthMatrix)
                        .then()
                                .statusCode(400)
                                .spec(getGenericErrorSpec))
                        .extract().as(ErrorResponseModel.class);

        step("Проверка ответа", () -> {
            assertFalse(getErrorResponseModel.isSuccess());
            assertEquals(errorBadOriginLength, getErrorResponseModel.getError());
        });
    }
    @Test
    @Tag("Regress")
    @DisplayName("Проверка ошибки слишком длинного значения параметра destination")
    void getMonthlyMatrixBadDestinationTest() {
        ErrorResponseModel getErrorResponseModel =
                step("Запрос на получение информации о пользователе", () ->
                        given()
                                .param("currency", "RUB")
                                .param("origin", "MOW")
                                .param("destination", BAD_LOCATION)
                                .param("month", get10WeeksFutureDate())
                                .param("limit", 5)
                                .param("token", token)
                        .when()
                                .get(v2PricesMonthMatrix)
                                .then()
                        .statusCode(400)
                                .spec(getGenericErrorSpec))
                        .extract().as(ErrorResponseModel.class);

        step("Проверка ответа", () -> {
            assertFalse(getErrorResponseModel.isSuccess());
            assertEquals(errorBadDestinationLength, getErrorResponseModel.getError());
        });
    }
    @Test
    @Tag("Regress")
    @DisplayName("Проверка ошибки слишком длинного значения параметра trip_duration")
    void getMonthlyMatrixLongDurationTest() {
        ErrorResponseModel getErrorResponseModel =
                step("Запрос на получение информации о пользователе", () ->
                        given()
                                .param("currency", "RUB")
                                .param("origin", "MOW")
                                .param("destination","LED")
                                .param("month", get10WeeksFutureDate())
                                .param("trip_duration", LONG_DURATION)
                                .param("token", token)
                        .when()
                                .get(v2PricesMonthMatrix)
                        .then()
                                .statusCode(420)
                                .spec(getGenericErrorSpec))
                        .extract().as(ErrorResponseModel.class);

        step("Проверка ответа", () -> {
            assertFalse(getErrorResponseModel.isSuccess());
            assertEquals(errorLongTripDuration01 + "31" + errorLongTripDuration02, getErrorResponseModel.getError());
        });
    }

    @Test
    @Tag("Regress")
    @DisplayName("Проверка ошибки неверного значения параметра trip_duration")
    void getMonthlyMatrixBadDurationTest() {
        ErrorResponseModel getErrorResponseModel =
                step("Запрос на получение информации о пользователе", () ->
                        given()
                                .param("currency", "RUB")
                                .param("origin", "MOW")
                                .param("destination","LED")
                                .param("month", get10WeeksFutureDate())
                                .param("trip_duration", SHORT_DURATION)
                                .param("token", token)
                                .when()
                                .get(v2PricesMonthMatrix)
                                .then()
                                .statusCode(400)
                                .spec(getGenericErrorSpec))
                        .extract().as(ErrorResponseModel.class);

        step("Проверка ответа", () -> {
            assertFalse(getErrorResponseModel.isSuccess());
            assertEquals(errorTripDuration, getErrorResponseModel.getError());
        });
    }
}