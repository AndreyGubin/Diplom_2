package config;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class Config {

    public static final String REGISTER = "auth/register";
    public static final String LOGIN = "auth/login";
    public static final String ORDERS = "orders";
    public static final String USER = "auth/user";
    public static final String INGREDIENTS = "ingredients";

    public static String myToken;
    public static String id1;
    public static String id2;
    public static String id3;
    public static String randomMail;
    public static String randomPass;
    public static String randomName;
    public static Response responseRegister;
    public static Response responseEdit;
    public static Response responseAuth;
    public static Response responseIngredients;
    public static Response responseOrderList;
    public static Response responseOrderCreate;
    public static Response responseDelete;
    public static RequestSpecification sendHeader = given()
            .filters(new AllureRestAssured(), new ResponseLoggingFilter())
            .contentType(ContentType.JSON).accept(ContentType.JSON)
            .baseUri("https://stellarburgers.nomoreparties.site/api/");
}
