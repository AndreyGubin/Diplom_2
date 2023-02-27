package steps;

import config.Config;
import io.qameta.allure.Step;
import model.OrderHelper;

import java.util.List;

import static io.restassured.RestAssured.given;

public class OrdersSteps extends Config {

    @Step("Получение списка заказов")
    public static void getMyOrders() {
        if (myToken != null) {
            responseOrderList = given().log().all()
                    .spec(sendHeader)
                    .header("Authorization", myToken)
                    .when()
                    .get(ORDERS);
        } else {
            responseOrderList = given().log().all()
                    .spec(sendHeader)
                    .when()
                    .get(ORDERS);
        }
    }

    @Step("Получение ингредиентов")
    public static void getOrderIngredients() {

        // получаем список ингредиентов
        if (myToken != null) {
            responseIngredients = given().log().all()
                    .spec(sendHeader)
                    .header("Authorization", myToken)
                    .when()
                    .get(INGREDIENTS);
        } else {
            responseIngredients = given().log().all()
                    .spec(sendHeader)
                    .when()
                    .get(INGREDIENTS);
        }
    }

    @Step("Создание заказа")
    public static void orderCreate(List<String> ingredients) {
        OrderHelper orderBody = new OrderHelper(ingredients);

        // создаём заказ
        if (myToken != null) {
            responseOrderCreate = given().log().all()
                    .spec(sendHeader)
                    .header("Authorization", myToken)
                    .body(orderBody)
                    .when()
                    .post(ORDERS);
        } else {
            responseOrderCreate = given().log().all()
                    .spec(sendHeader)
                    .body(orderBody)
                    .when()
                    .post(ORDERS);
        }
    }

    @Step("Создание заказа без ингредиентов")
    public static void orderCreateNoIngredietns() {
        // создаём заказ
        responseOrderCreate = given().log().all()
                .spec(sendHeader)
                .header("Authorization", myToken)
                .when()
                .post(ORDERS);
    }
}

