import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

public class TestUserCreate {
    private User user;
    private UserClient userClient;

    @Before
    public void setUser(){
        user = UserGenerate.getRandomUser();
        userClient = new UserClient();
    }

    @Test
    @DisplayName("Cоздание нового пользователя")
    @Description("Создаю нового уникального пользователя и удаляю его после проверок")
    public void createNewUser() {
        ValidatableResponse responseCreate = userClient.create(user)
                                                .statusCode(SC_OK);
        boolean isOk = responseCreate.extract().path("success");
        String token = responseCreate.extract().path("accessToken");

        assertTrue(isOk);

        userClient.delete(token).statusCode(SC_ACCEPTED);


    }

    @Test
    @DisplayName("Создание пользователя который уже зарегестрирован")
    @Description("Cоздаю нового пользователя и повторно регестируемся под его данными")
    public void createDuplicateUser(){
       String token = userClient.create(user)
                        .statusCode(SC_OK)
                        .extract().path("accessToken") ;

       ValidatableResponse response = userClient.create(user)
                                        .statusCode(SC_FORBIDDEN);

       String actualMessage = response.extract().path("message");
       boolean isFalse = response.extract().path("success");
       String expectedMessage = "User already exists";

        assertEquals("Должен быть текст,что пользователь уже существует", expectedMessage, actualMessage );
        assertFalse(isFalse);


        userClient.delete(token).statusCode(SC_ACCEPTED);

    }

    @Test
    @DisplayName("Создание пользователя без поля name")
    @Description("Cоздаю нового пользователя без поля  и сравниваю код ответа и текст ошибки")
    public void createUserWithoutName(){
        user = UserGenerate.getUserWithoutName();

        ValidatableResponse response = userClient.create(user)
                .statusCode(SC_FORBIDDEN);

        String actualMessage = response.extract().path("message");
        boolean isFalse = response.extract().path("success");
        String expectedMessage = "Email, password and name are required fields";

        assertEquals("Должен быть текст,что пользователь не заполнил одно из полей", expectedMessage, actualMessage );
        assertFalse(isFalse);



    }

    @Test
    @DisplayName("Создание пользователя без поля email")
    @Description("Cоздаю нового пользователя без поля email и сравниваю код ответа и текст ошибки")
    public void createUserWithoutEmail(){
        user = UserGenerate.getUserWithoutEmail();

        ValidatableResponse response = userClient.create(user)
                .statusCode(SC_FORBIDDEN);

        String actualMessage = response.extract().path("message");
        boolean isFalse = response.extract().path("success");
        String expectedMessage = "Email, password and name are required fields";

        assertEquals("Должен быть текст,что пользователь не заполнил одно из полей", expectedMessage, actualMessage );
        assertFalse(isFalse);

    }


    @Test
    @DisplayName("Создание пользователя без поля password")
    @Description("Cоздаю нового пользователя без поля password и сравниваю код ответа и текст ошибки")
    public void createUserWithoutPassword(){

        user = UserGenerate.getUserWithoutPassword();

        ValidatableResponse response = userClient.create(user)
                .statusCode(SC_FORBIDDEN);

        String actualMessage = response.extract().path("message");
        boolean isFalse = response.extract().path("success");
        String expectedMessage = "Email, password and name are required fields";

        assertEquals("Должен быть текст,что пользователь не заполнил одно из полей", expectedMessage, actualMessage );
        assertFalse(isFalse);
    }


}
