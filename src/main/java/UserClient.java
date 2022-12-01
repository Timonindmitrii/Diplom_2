import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class UserClient extends BaseClient {
    private static final String REGISTER  = "/api/auth/register";
    private static final String PATH = "/api/auth/user";
    private static final String LOGIN = "/api/auth/login";


    @Step("Создание нового пользователя")
    public ValidatableResponse create(User user){
        return given()
                .spec(getSpec())
                .body(user)
                .when()
                .post(REGISTER)
                .then().log().all();
    }

    @Step("Удаление пользователя")
    public  ValidatableResponse delete(String accessToken){
        return given()
                .spec(getSpec())
                .header("Authorization", accessToken)
                .body("")
                .when()
                .delete(PATH)
                .then().log().all();
    }

    @Step("Логин пользователя")
    public ValidatableResponse login(UserCredentials userCredentials) {
        return given()
                .spec(getSpec())
                .body(userCredentials)
                .when()
                .post(LOGIN)
                .then().log().all();

    }

    @Step("Изменение данных созданного пользователя c авторизацией")
    public ValidatableResponse edit(User user, String token){
        return  given()
                .spec(getSpec())
                .header("Authorization", token)
                .body(user)
                .when()
                .patch(PATH)
                .then().log().all();
    }

    @Step("Изменение данных созданного пользователя c авторизацией")
    public ValidatableResponse editNotAuthorization(User user){
        return  given()
                .spec(getSpec())
                .body(user)
                .when()
                .patch(PATH)
                .then().log().all();
    }


}
