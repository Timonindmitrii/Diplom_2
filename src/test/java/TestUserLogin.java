import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.*;

public class TestUserLogin {
    private User user;
    private UserClient userClient = new UserClient();
    private  UserCredentials userCredentials;
    private String token;

    @Before
    @Description("Создаю нового рандомного пользователя")
    public void setup(){
        user = UserGenerate.getRandomUser();
        ValidatableResponse response = userClient.create(user)
                    .statusCode(SC_OK);
        token = response.extract().path("accessToken");

    }
    @After
    @Description("Удаляю созданного пользователя")
    public void teardown(){

        userClient.delete(token);

    }

    @Test
    @DisplayName("Логин пользователя")
    @Description("Логинюсь корректной учеткой")
    public void loginUser(){
        userCredentials = UserCredentials.from(user);
        ValidatableResponse responseLogin = userClient.login(userCredentials).statusCode(SC_OK);

        boolean isOk = responseLogin.extract().path("success");

        assertTrue(isOk);

    }

    @Test
    @DisplayName("Логин пользователя c неправильным логином")
    @Description("Логинюсь некорректной учеткой")
    public void loginUserIncorrectEmail(){
        userCredentials = UserCredentials.withIncorrectEmail(user);
        ValidatableResponse responseLogin = userClient.login(userCredentials).statusCode(SC_UNAUTHORIZED);

        boolean isFalse = responseLogin.extract().path("success");
        String errorMessage = responseLogin.extract().path("message");

        String expected = "email or password are incorrect";

        assertFalse(isFalse);
        assertEquals("Текс ошибки должен совпадать",expected,errorMessage);

    }

    @Test
    @DisplayName("Логин пользователя c неправильным паролем")
    @Description("Логинюсь некорректной учеткой")
    public void loginUserIncorrectPassword(){
        userCredentials = UserCredentials.withIncorrectPassword(user);
        ValidatableResponse responseLogin = userClient.login(userCredentials).statusCode(SC_UNAUTHORIZED);

        boolean isFalse = responseLogin.extract().path("success");
        String errorMessage = responseLogin.extract().path("message");

        String expected = "email or password are incorrect";

        assertFalse(isFalse);
        assertEquals("Текс ошибки должен совпадать",expected,errorMessage);

    }



}
