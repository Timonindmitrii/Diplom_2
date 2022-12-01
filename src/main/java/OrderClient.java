import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderClient extends BaseClient {
    private static final String CREATE_ORDER = "/api/orders";

    @Step("Создание закаказа c авторизацией и c ингредиентами")
    public ValidatableResponse create(Order order, String token){
        return given()
                .spec(getSpec())
                .header("Authorization", token)
                .body(order)
                .when()
                .post(CREATE_ORDER)
                .then().log().all();
    }

    @Step("Создание закаказа без авторизации c ингредиентами")
    public ValidatableResponse createWithoutAuth (Order order) {
        return given()
                .spec(getSpec())
                .body(order)
                .when()
                .post(CREATE_ORDER)
                .then().log().all();
    }



    @Step("Создание закаказа c авторизацией без ингредиентов")
    public ValidatableResponse createWithAuthWithoutIngredient(String token) {
        return given()
                .spec(getSpec())
                .header("Authorization", token)
                .when()
                .post(CREATE_ORDER)
                .then().log().all();
    }


    @Step("Создание закаказа c авторизацией и ингредиентами неверно указанами")
    public ValidatableResponse createWithAuthWithIncorrectHashIngredient (Order order, String token) {
        return given()
                .spec(getSpec())
                .header("Authorization", token)
                .body(order)
                .when()
                .post(CREATE_ORDER)
                .then().log().all();
    }

    @Step("Получение заказов конкретного пользователя")
    public ValidatableResponse getOrdersWithAuth(String token){
        return given()
                .spec(getSpec())
                .header("Authorization", token)
                .when()
                .get(CREATE_ORDER)
                .then().log().all();

    }

    @Step("Получение заказов конкретного пользователя")
    public ValidatableResponse getOrdersWithoutAuth(){
        return given()
                .spec(getSpec())
                .when()
                .get(CREATE_ORDER)
                .then().log().all();

    }


}
