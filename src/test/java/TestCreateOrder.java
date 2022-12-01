import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

public class TestCreateOrder {
    private User user;
    private Order ingredients;
    private UserClient userClient;
    private OrderClient orderClient;
    private String token;

    @Before
    @Description("Готовклю пользователя для заказа")
    public void setupOrder(){
        userClient = new UserClient();
        orderClient = new OrderClient();
        user = UserGenerate.getRandomUser();
        ValidatableResponse responseCreate =  userClient.create(user)
                .statusCode(SC_OK);

        token = responseCreate.extract().path("accessToken");
    }

    @After
    @Description("Удаляю созданного пользователя")
    public void deleteUserOrder(){

        userClient.delete(token).statusCode(SC_ACCEPTED);
    }


    @Test
    @DisplayName("Заказ авторизованного пользователя")
    @Description("Создаю заказ с валидными ингредиентами и авторизацией")
    public void createOrderWithAuthAndIngredientsPositive(){
        ingredients = Order.makeIngredients();

        ValidatableResponse responseCreateOrder = orderClient.create(ingredients,token)
                                                                        .statusCode(SC_OK);

        boolean isOk = responseCreateOrder.extract().path("success");

        assertTrue(isOk);

    }

    @Test
    @DisplayName("Заказ без авторизации")
    @Description("Создаю заказ с валидными ингредиентами без авторизации")
    public void createOrderWithoutAuth(){
        ingredients = Order.makeIngredients();

        // По документации заказ может делать только авторизованный пользак
       orderClient.createWithoutAuth(ingredients)
                                        .statusCode(SC_UNAUTHORIZED);
    }


    @Test
    @DisplayName("Заказ без ингредиентов")
    @Description("Создаю заказ с авторизацией без ингридиентов")
    public void createOrderWithoutIngredient(){
        ValidatableResponse response = orderClient.createWithAuthWithoutIngredient(token)
                                                                            .statusCode(SC_BAD_REQUEST);
        boolean isFalse = response.extract().path("success");
        String actualMessage = response.extract().path("message");

        String expected = "Ingredient ids must be provided";


        assertFalse(isFalse);
        assertEquals(expected,actualMessage);

    }

    @Test
    @DisplayName("Заказ с невалидным хэшем ингредиента")
    @Description("Создаю заказ с авторизацией и невалидным хэшом ингридиентов")
    public void createOrderWithIncorrectIngredient(){
        ingredients = Order.makeIngredientsWithWrongHash();

        orderClient.createWithAuthWithIncorrectHashIngredient(ingredients,token)
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }

}
