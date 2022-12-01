import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class BaseClient {
    private static final String BASE_URL ="https://stellarburgers.nomoreparties.site";

    protected RequestSpecification getSpec() {
        return given().log().all()
                .header("Content-Type", "application/json")
                .baseUri(BASE_URL);
    }

}
