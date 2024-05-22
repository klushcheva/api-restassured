package tests;

import models.ErrorResponseModel;
import models.FlightsPriceResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static config.Endpoints.v3GetPrices;
import static data.ErrorData.*;
import static helpers.DateHelper.get3MonthsFutureDate;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static specs.FlightMatrixSpec.getFlightMatrix200ResponseSpec;
import static specs.GenericErrorSpec.getGenericErrorSpec;

public class GetLatestPricesTest extends BaseAPITest {
    private final String BAD_NUM = "34663134";
    private final String BAD_STR = "...";
    private final Integer BAD_CLASS = 9;
    private final String BAG_PAGE = "999";


    @ParameterizedTest
    @CsvSource({
            "0, MOW, price",
            "1, LON, route",
            "2, DXB, distance_unit_price"
    })
    @Tags({
            @Tag("Smoke"),
            @Tag("Regress")
    })
    @DisplayName("Получение полетов в определенный год")
    void getYearlyPricesTest(String tripClass, String origin, String sorting) {
        FlightsPriceResponseModel getFlightsPriceModel =
                step("Получение информации о билетах", () ->
                        given()
                                .param("currency", "RUB")
                                .param("one_way", false)
                                .param("origin", origin)
                                .param("period_type", "year")
                                .param("beginning_of_period", "2024")
                                .param("trip_class", tripClass)
                                .param("limit", 3)
                                .param("sorting", sorting)
                                .param("token", token)
                                .when()
                                .get(v3GetPrices)
                                .then()
                                .statusCode(200)
                                .spec(getFlightMatrix200ResponseSpec))
                        .extract().as(FlightsPriceResponseModel.class);

        step("Проверка ответа", () -> {
            assertTrue(getFlightsPriceModel.isSuccess());
            assertEquals(3, getFlightsPriceModel.getData().size());
            assertEquals("rub", getFlightsPriceModel.getCurrency());

            for (int i = 0; i < 3; i++) {
                assertTrue(getFlightsPriceModel.getData().get(i).isActual());
                assertEquals(origin, getFlightsPriceModel.getData().get(i).getOrigin());
                assertEquals(tripClass, getFlightsPriceModel.getData().get(i).getTripClass());
            }
        });
    }

    @ParameterizedTest
    @CsvSource({
            "month, MOW",
            "day, LED"
    })
    @Tags({
            @Tag("Smoke"),
            @Tag("Regress")
    })
    @DisplayName("Получение полетов в определенный месяц и день")
    void getMonthAndDayPricesTest(String periodType, String origin) {
        String period = get3MonthsFutureDate();

        FlightsPriceResponseModel getFlightsPriceModel =
                step("Получение информации о билетах", () ->
                        given()
                                .param("currency", "USD")
                                .param("one_way", true)
                                .param("origin", origin)
                                .param("period_type", periodType)
                                .param("beginning_of_period", period)
                                .param("trip_class", 1)
                                .param("limit", 3)
                                .param("token", token)
                                .when()
                                .get(v3GetPrices)
                                .then()
                                .statusCode(200)
                                .spec(getFlightMatrix200ResponseSpec))
                        .extract().as(FlightsPriceResponseModel.class);

        step("Проверка ответа", () -> {
            assertTrue(getFlightsPriceModel.isSuccess());
            assertEquals("usd", getFlightsPriceModel.getCurrency());
            assertTrue(getFlightsPriceModel.getData().get(0).isActual());
            assertEquals(origin, getFlightsPriceModel.getData().get(0).getOrigin());
            assertEquals("1", getFlightsPriceModel.getData().get(0).getTripClass());
            assertEquals("", getFlightsPriceModel.getData().get(0).getReturnDate());
        });
    }

    @Test
    @Tag("Regress")
    @DisplayName("Проверка ошибки неверного временного интервала")
    void getBadPeriodErrorTest() {
        ErrorResponseModel getErrorResponseModel =
                step("Получение информации о билетах", () ->
                        given()
                                .param("currency", "RUB")
                                .param("origin", "MOW")
                                .param("period_type", "year")
                                .param("beginning_of_period", BAD_NUM)
                                .param("token", token)
                                .when()
                                .get(v3GetPrices)
                                .then()
                                .statusCode(400)
                                .spec(getGenericErrorSpec))
                        .extract().as(ErrorResponseModel.class);

        step("Проверка ответа", () -> {
            assertFalse(getErrorResponseModel.isSuccess());
            assertEquals(errorBadPeriod, getErrorResponseModel.getError());
        });
    }

    @Test
    @Tag("Regress")
    @DisplayName("Проверка ошибки неверного значения параметра sorting")
    void getBadSortingErrorTest() {
        ErrorResponseModel getErrorResponseModel =
                step("Получение информации о билетах", () ->
                        given()
                                .param("currency", "RUB")
                                .param("origin", "MOW")
                                .param("period_type", "year")
                                .param("beginning_of_period", "2024")
                                .param("sorting", BAD_NUM)
                                .param("token", token)
                        .when()
                                .get(v3GetPrices)
                        .then()
                                .statusCode(400)
                                .spec(getGenericErrorSpec))
                        .extract().as(ErrorResponseModel.class);

        step("Проверка ответа", () -> {
            assertFalse(getErrorResponseModel.isSuccess());
            assertEquals(errorBadSorting, getErrorResponseModel.getError());
        });
    }
    @Test
    @Tag("Regress")
    @DisplayName("Проверка ошибки неверного значения параметра period_type")
    void getBadPeriodTypeErrorTest() {
        ErrorResponseModel getErrorResponseModel =
                step("Получение информации о билетах", () ->
                        given()
                                .param("currency", "RUB")
                                .param("origin", "MOW")
                                .param("period_type", BAD_STR)
                                .param("beginning_of_period", "2024")
                                .param("token", token)
                        .when()
                                .get(v3GetPrices)
                        .then()
                                .statusCode(400)
                                .spec(getGenericErrorSpec))
                        .extract().as(ErrorResponseModel.class);

        step("Проверка ответа", () -> {
            assertFalse(getErrorResponseModel.isSuccess());
            assertEquals(errorBadPeriodType, getErrorResponseModel.getError());
        });
    }
    @Test
    @Tag("Regress")
    @DisplayName("Проверка ошибки неверного значения параметра trip_class")
    void getBadTripClassErrorTest() {
        ErrorResponseModel getErrorResponseModel =
                step("Получение информации о билетах", () ->
                        given()
                                .param("currency", "RUB")
                                .param("origin", "MOW")
                                .param("trip_class", BAD_CLASS)
                                .param("period_type", "year")
                                .param("beginning_of_period", "2024")
                                .param("token", token)
                        .when()
                                .get(v3GetPrices)
                        .then()
                                .statusCode(400)
                                .spec(getGenericErrorSpec))
                        .extract().as(ErrorResponseModel.class);

        step("Проверка ответа", () -> {
            assertFalse(getErrorResponseModel.isSuccess());
            assertEquals(errorBadClass, getErrorResponseModel.getError());
        });
    }

    @Test
    @Tag("Regress")
    @DisplayName("Проверка ошибки неверного типа параметра page")
    void getBadPageErrorTest() {
        ErrorResponseModel getErrorResponseModel =
                step("Получение информации о билетах", () ->
                        given()
                                .param("currency", "RUB")
                                .param("origin", "MOW")
                                .param("page", BAD_STR)
                                .param("period_type", "year")
                                .param("beginning_of_period", "2024")
                                .param("token", token)
                        .when()
                                .get(v3GetPrices)
                        .then()
                                .statusCode(400)
                                .spec(getGenericErrorSpec))
                        .extract().as(ErrorResponseModel.class);

        step("Проверка ответа", () -> {
            assertFalse(getErrorResponseModel.isSuccess());
            assertEquals(errorBadPageType, getErrorResponseModel.getError());
        });
    }
    @Test
    @Tag("Regress")
    @DisplayName("Проверка ошибки слишком большого значения параметра page")
    void getLargePageErrorTest() {
        ErrorResponseModel getErrorResponseModel =
                step("Получение информации о билетах", () ->
                        given()
                                .param("currency", "RUB")
                                .param("origin", "MOW")
                                .param("page", BAG_PAGE)
                                .param("period_type", "year")
                                .param("beginning_of_period", "2024")
                                .param("token", token)
                                .when()
                                .get(v3GetPrices)
                                .then()
                                .statusCode(400)
                                .spec(getGenericErrorSpec))
                        .extract().as(ErrorResponseModel.class);

        step("Проверка ответа", () -> {
            assertFalse(getErrorResponseModel.isSuccess());
            assertEquals(errorTooLargePage, getErrorResponseModel.getError());
        });
    }
}