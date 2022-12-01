import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

public class TestEditUserData {
    private  User user;
    private final UserClient userClient = new UserClient();
    private String token;

    @Before
    @Description("Создаю пользователя")
    public void setupUsers (){
        user = UserGenerate.getRandomUser();
        ValidatableResponse responseCreate =  userClient.create(user)
                                                .statusCode(SC_OK);

        token = responseCreate.extract().path("accessToken");
    }


    @After
    @Description("Удаляю созданного пользователя")
    public void post(){

        userClient.delete(token).statusCode(SC_ACCEPTED);
    }

    @Test
    @DisplayName("Редактирую авторизованного пользователя")
    @Description("Изменяю  все данные авторизованного пользователя")
    public void editUserAuthorization(){

        ValidatableResponse responseEdit = userClient.edit(UserGenerate.getUserWithModified(user),token)
                                                                        .statusCode(SC_OK);
        boolean isOk = responseEdit.extract().path("success");

        JsonPath jsonpath= responseEdit.extract().jsonPath();
        String actualEmail = jsonpath.getString("user.email");
        String actualName = jsonpath.getString("user.name");
        String expectedEmail = "timonindv@yandex.ru";
        String expectedName = "Дмитрий";


        assertTrue(isOk);
        assertEquals("Email должен совпадать", expectedEmail, actualEmail );
        assertEquals("Email должен совпадать", expectedName, actualName );

    }


    @Test
    @DisplayName("Редактирую неавторизованного пользователя")
    @Description("Редактирую все поля пользователя")
    public void editUserNotAuthorization() {
        ValidatableResponse responseEdit = userClient.editNotAuthorization(UserGenerate.getUserWithModified(user))
                .statusCode(SC_UNAUTHORIZED);

        boolean isFalse = responseEdit.extract().path("success");
        String actualMessage = responseEdit.extract().path("message");

        String expectedMessage ="You should be authorised";

        assertFalse(isFalse);
        assertEquals(expectedMessage,actualMessage);

    }
}
