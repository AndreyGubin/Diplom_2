import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import static config.Config.*;
import static org.hamcrest.Matchers.equalTo;
import static steps.RegistrationSteps.*;

public class LoginUserTests {

    @Before
    public void resetRandom() {
        // генерируем новые данные для каждого теста
        randomMail = RandomStringUtils.randomAlphabetic(10).toLowerCase() + "@yandex.ru";
        randomPass = RandomStringUtils.randomAlphabetic(10);
        randomName = RandomStringUtils.randomAlphabetic(10).toLowerCase();
        // очищаем токен
        myToken = "";
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Авторизация пользователя с валидными данными")
    public void postLoginUserValidData() {
        // регистрация пользователя с валидными данными
        registerUser(randomMail, randomPass, randomName);
        // авторизация
        loginUser(randomMail, randomPass);
        responseAuth.then().assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));
        myToken = responseAuth.jsonPath().getString("accessToken");
        // удаляем пользователя
        deleteUser();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Авторизация пользователя с невалидными данными")
    public void postLoginUserInvalidData() {
        // авторизация с невалидными данными
        loginUser("testmail@yandex.ru", "123456");
        responseAuth.then().assertThat()
                .statusCode(401)
                .and()
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }
}
