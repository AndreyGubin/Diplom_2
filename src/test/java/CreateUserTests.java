import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static config.Config.*;
import static org.hamcrest.Matchers.equalTo;
import static steps.RegistrationSteps.*;

public class CreateUserTests {
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
    @DisplayName("Регистрация пользователя с валидными данными")
    public void postCreateUserValidData() {
        // регистрация пользователя с валидными данными
        registerUser(randomMail, randomPass, randomName);
        responseRegister.then().assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Регистрация пользователя без email")
    public void postCreateUserNoEmail() {
        // регистрация пользователя без email
        registerUser("", randomPass, randomName);
        responseRegister.then().assertThat()
                .statusCode(403)
                .and()
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Регистрация пользователя без пароля")
    public void postCreateUserNoPass() {
        // регистрация пользователя без пароля
        registerUser(randomMail, "", randomName);
        responseRegister.then().assertThat()
                .statusCode(403)
                .and()
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Регистрация пользователя без имени")
    public void postCreateUserNoName() {
        // регистрация пользователя без имени
        registerUser(randomMail, randomPass, "");
        responseRegister.then().assertThat()
                .statusCode(403)
                .and()
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Регистрация пользователя с существующим email")
    public void postCreateUserExistingEmail() {
        // регистрация пользователя с несуществующим email
        registerUser(randomMail, randomPass, randomName);
        // регистрация пользователя с тем же email, ожидаем ошибку
        registerUser(randomMail, randomPass, randomName);
        responseRegister.then().assertThat()
                .statusCode(403)
                .and()
                .body("success", equalTo(false));
    }

    @After
    public void deleteTestData() {
        // удаляем пользователя
        loginUser(randomMail, randomPass);
        myToken = responseAuth.jsonPath().getString("accessToken");
        deleteUser();
    }
}
