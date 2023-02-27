import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static config.Config.*;
import static org.hamcrest.Matchers.equalTo;
import static steps.OrdersSteps.*;
import static steps.RegistrationSteps.*;

public class CreateOrderTests {

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
    @DisplayName("Создание заказа с валидными данными")
    public void postCreateOrderValidData() {
        // авторизация и получение токена
        loginUser(randomMail, randomPass);
        myToken = responseAuth.jsonPath().getString("accessToken");
        // запрашиваем три ингредиента из списка
        getOrderIngredients();
        id1 = responseIngredients.jsonPath().getString("data._id[0]");
        id2 = responseIngredients.jsonPath().getString("data._id[1]");
        id3 = responseIngredients.jsonPath().getString("data._id[2]");
        // создаем заказ и передаем три ингредиента
        orderCreate(List.of(id1, id2, id3));
        responseOrderCreate.then().assertThat()
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Создание заказа без ингредиентов")
    public void postCreateOrderNoIngredients() {
        // авторизация
        loginUser(randomMail, randomPass);
        myToken = responseAuth.jsonPath().getString("accessToken");
        // создаем заказ без передачи ингредиентов
        orderCreateNoIngredietns();
        responseOrderCreate.then().assertThat()
                .statusCode(400)
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Создание заказа с неверным хэшем ингредиентов")
    public void postCreateOrderInvalidHash() {
        // авторизация
        loginUser(randomMail, randomPass);
        myToken = responseAuth.jsonPath().getString("accessToken");
        orderCreate(List.of("123456"));
        responseOrderCreate.then().assertThat()
                .statusCode(500);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Создание заказа без авторизации")
    public void postCreateOrderNoAuth() {
        getOrderIngredients();
        id1 = responseIngredients.jsonPath().getString("data._id[0]");
        id2 = responseIngredients.jsonPath().getString("data._id[1]");
        id3 = responseIngredients.jsonPath().getString("data._id[2]");
        orderCreate(List.of(id1, id2, id3));
        responseOrderCreate.then().assertThat()
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @After
    public void deleteTestData() {
        // удаляем пользователя
        deleteUser();
    }
}
