import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import steps.OrdersSteps;

import static config.Config.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static steps.RegistrationSteps.*;


public class GetOrderTests {
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
    @DisplayName("Получение списка заказов с валидными данными")
    public void getOrders() {
        // регистрация пользователя с валидными данными
        registerUser(randomMail, randomPass, randomName);
        // авторизация
        loginUser(randomMail, randomPass);
        myToken = responseAuth.jsonPath().getString("accessToken");
        // получение списка заказов
        OrdersSteps.getMyOrders();
        responseOrderList.then().assertThat()
                .statusCode(200);
        // удаляем пользователя
        deleteUser();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Получение списка заказов без авторизации")
    public void getOrdersNoAuth() {
        // получение списка заказов без авторизации и токена
        OrdersSteps.getMyOrders();
        responseOrderList.then()
                .statusCode(401)
                .and()
                .body("message", equalTo("You should be authorised"));
    }
}
