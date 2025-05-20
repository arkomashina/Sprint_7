package сourier.data;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;


import static сourier.data.CourierConstants.*;
import static io.restassured.RestAssured.given;

public class CourierClient {
    public CourierClient() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Step("Отправить запрос на создание курьера")
    public Response createCourier(CourierCreateRequest request) {
        return given()
                .header("Content-type", "application/json")
                .body(request)
                .when()
                .post(CREATE_COURIER);
    }

    @Step("Отправить запрос авторизации курьера")
    public Response loginCourier(CourierLoginRequest request) {
        return given()
                .header("Content-type", "application/json")
                .body(request)
                .when()
                .post(LOGIN_COURIER);
    }

    @Step("Отправить запрос на удаление курьера")
    public Response deleteCourier(int courierId) {
        return given()
                .header("Content-type", "application/json")
                .when()
                .delete(DELETE_COURIER + courierId);
    }
    @Step("Отправить запрос на удаление курьера с несуществующим идентификатором")
    public Response deleteCourierWithoutId() {
        return given()
                .header("Content-type", "application/json")
                .when()
                .delete(DELETE_COURIER);
    }

    @Step("Отправить запрос на получение заказов курьера")
    public Response getOrdersCount(int courierId) {
        return given()
                .header("Content-type", "application/json")
                .when()
                .get("/api/v1/courier/" + courierId + "/ordersCount");
    }
}
