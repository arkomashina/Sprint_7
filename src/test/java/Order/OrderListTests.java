package Order;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

public class OrderListTests {
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    public void getOrderListTest() {
        Response response =
                given()
                        .get("/api/v1/orders");
        response.then().statusCode(200)
                .and()
                .body("orders", notNullValue());
    }
}
