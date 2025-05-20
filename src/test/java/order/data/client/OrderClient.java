package order.data.client;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import order.data.model.Order;
import static io.restassured.RestAssured.given;
import static order.data.util.OrderConstants.CREATE_GET_ORDER;


public class OrderClient {
    static {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Step("Создать заказ")
    public Response createOrder(Order order) {
        return given()
                .header("Content-type", "application/json")
                .body(order)
                .when()
                .post(CREATE_GET_ORDER);
    }

    @Step("Получить список заказов")
    public Response getOrderList() {
        return given()
                .when()
                .get(CREATE_GET_ORDER);
    }
    }