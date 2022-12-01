import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.*;

public class TestGetOrder {
    private OrderClient orderClient;
    private Order ingredients;
    private User user;
    private UserClient userClient;
    private String token;


    @Before
    @Description("Создаю авторизованным пользователем заказ")
    public void setupUsersOrders(){
        orderClient = new OrderClient();
        userClient = new UserClient();
        ingredients = Order.makeIngredients();
        user = UserGenerate.getRandomUser();

        ValidatableResponse responseCreate =  userClient.create(user)
                .statusCode(SC_OK);
        token = responseCreate.extract().path("accessToken");

        orderClient.create(ingredients,token);

    }

    @After
    @Description("Удаляю созданного пользователя")
    public void deletedUsersOrders(){
        userClient.delete(token);
    }

    @Test
    @DisplayName("Успешное получени заказа конкретного пользователя")
    @Description("Получаю заказ авторизованного пользователя")
    public void getUserOrdersWithAuth(){
       ValidatableResponse response = orderClient.getOrdersWithAuth(token)
                                                        .statusCode(SC_OK);
       boolean isOk = response.extract().path("success");

        assertTrue(isOk);

    }

    @Test
    @DisplayName("Получение заказа конкретного пользователя без авторизации")
    @Description("Получаю ошибку при получении заказа не авторизованного пользователя")
    public void getUserOrdersWithoutAuth(){
        ValidatableResponse response = orderClient.getOrdersWithoutAuth()
                                            .statusCode(SC_UNAUTHORIZED);
        boolean isFalse = response.extract().path("success");
        String actualMessage = response.extract().path("message");
        String expected = "You should be authorised";


        assertFalse(isFalse);
        assertEquals(expected,actualMessage);

    }

}
