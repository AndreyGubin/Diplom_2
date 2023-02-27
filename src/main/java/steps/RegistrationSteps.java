package steps;

import config.Config;
import io.qameta.allure.Step;
import model.AuthHelper;
import model.RegistrationHelper;

import static io.restassured.RestAssured.given;

public class RegistrationSteps extends Config{

    @Step("Регистрация пользователя")
    public static void registerUser(String randomMail, String randomPass, String randomName){
        RegistrationHelper auth = new RegistrationHelper(randomMail, randomPass, randomName);
        // регистрация
        responseRegister = given().log().all()
                .spec(sendHeader)
                .body(auth)
                .when()
                .post(REGISTER);
    }

    @Step("Авторизация пользователя")
    public static void loginUser(String randomMail, String randomPass){
        AuthHelper auth = new AuthHelper(randomMail, randomPass);
        // авторизация
        responseAuth = given().log().all()
                .spec(sendHeader)
                .body(auth)
                .when()
                .post(LOGIN);
    }

    @Step("Редактирование пользователя")
    public static void patchUser(String randomMail, String randomPass, String randomName){
        RegistrationHelper auth = new RegistrationHelper(randomMail, randomPass, randomName);
        if (myToken != null) {
        responseEdit = given().log().all()
                .spec(sendHeader)
                .header("Authorization", myToken)
                .body(auth)
                .when()
                .patch(USER);
        } else {
            responseEdit = given().log().all()
                    .spec(sendHeader)
                    .body(auth)
                    .when()
                    .patch(USER);
        }
    }

    @Step("Удаление пользователя")
    public static void deleteUser(){
        if (myToken != null) {
            responseDelete = given().log().all()
                    .spec(sendHeader)
                    .header("Authorization", myToken)
                    .when()
                    .delete(USER);
        } else {
            responseDelete = given().log().all()
                    .spec(sendHeader)
                    .when()
                    .delete(USER);
        }
    }
}
