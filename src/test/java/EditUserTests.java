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

public class EditUserTests {

    @Before
    public void resetRandom() {
        // генерируем новые данные для каждого теста
        randomMail = RandomStringUtils.randomAlphabetic(10).toLowerCase() + "@yandex.ru";
        randomPass = RandomStringUtils.randomAlphabetic(10);
        randomName = RandomStringUtils.randomAlphabetic(10).toLowerCase();
        // очищаем токен
        myToken = "";

        // регистрация пользователя с валидными данными
        registerUser(randomMail, randomPass, randomName);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Изменение email у пользователя")
    public void patchUserEmail() {
        // авторизация
        loginUser(randomMail, randomPass);
        myToken = responseAuth.jsonPath().getString("accessToken");
        // изменяем email
        patchUser(1 + randomMail, randomPass, randomName);
        // проверяем, что данные изменены
        responseEdit.then()
                .statusCode(200)
                .and()
                .body("success", equalTo(true))
                .and()
                .body("user.email", equalTo(1 + randomMail));

    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Изменение пароля у пользователя")
    public void patchUserPass() {
        // авторизация
        loginUser(randomMail, randomPass);
        myToken = responseAuth.jsonPath().getString("accessToken");
        // изменяем пароль
        patchUser(randomMail, randomPass + 1, randomName);
        // авторизуемся с новым паролем
        loginUser(randomMail, randomPass + 1);
        responseAuth.then().assertThat()
                .statusCode(200)
                .and()
                .body("success", equalTo(true));
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Изменение имени у пользователя")
    public void patchUserName() {
        // авторизация
        loginUser(randomMail, randomPass);
        myToken = responseAuth.jsonPath().getString("accessToken");
        // изменяем имя
        patchUser(randomMail, randomPass, 1 + randomName);
        // проверяем, что данные изменены
        responseEdit.then()
                .statusCode(200)
                .and()
                .body("success", equalTo(true))
                .and()
                .body("user.name", equalTo(1 + randomName));
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Изменение пользователя без авторизации")
    public void patchUserNoAuth() {
        // изменяем email без авторизации
        patchUser(randomMail, randomPass, randomName);
        responseEdit.then()
                .statusCode(401)
                .and()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("You should be authorised"));
    }

    @After
    public void deleteTestData() {
        // удаляем пользователя
        deleteUser();
    }
}
