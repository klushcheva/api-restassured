package specs;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;
import org.hamcrest.Matchers;

import static io.restassured.filter.log.LogDetail.BODY;
import static io.restassured.filter.log.LogDetail.STATUS;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;

public class GenericErrorSpec {
    public static ResponseSpecification getGenericErrorSpec = new ResponseSpecBuilder()
            .log(STATUS)
            .log(BODY)
            .expectStatusCode(Matchers.either(equalTo(400)).or(equalTo(420)))
            .expectBody(matchesJsonSchemaInClasspath("schemas/generic-error-get.json"))
            .build();
}
