package helpers;

import io.qameta.allure.Allure;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

public class CustomAllureListener implements Filter {
    private static final AllureRestAssured FILTER = new AllureRestAssured();

    static {
        FILTER.setRequestTemplate("request.ftl");
        FILTER.setResponseTemplate("response.ftl");
    }

    public static AllureRestAssured getFilter() {
        return FILTER;
    }

    @Override
    public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext ctx) {
        Response response = ctx.next(requestSpec, responseSpec);
        attachResponseToAllure(response);
        return response;
    }
    private void attachResponseToAllure(Response response) {
        Allure.addAttachment("API Response", response.getBody().asString());
    }
}