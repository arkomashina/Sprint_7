package order.data.service;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import order.data.model.Order;

import static io.restassured.RestAssured.given;
import static order.data.util.OrderConstants.CREATE_GET_ORDER;

public class OrderService {

    static {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    public static Response createOrder(Order order) {
        return given()
                .header("Content-type", "application/json")
                .body(order)
                .when()
                .post(CREATE_GET_ORDER);
    }

    public static Response getOrderList() {
        return given()
                .get(CREATE_GET_ORDER);
    }
}
