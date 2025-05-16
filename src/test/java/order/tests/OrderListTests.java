package order.tests;

import io.qameta.allure.Feature;
import io.restassured.response.Response;
import order.data.client.OrderClient;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.apache.http.HttpStatus.*;

@Feature("Получение заказов")
public class OrderListTests {
    private final OrderClient orderService = new OrderClient();

    @Test
    public void getOrderListTest() {
        Response response = orderService.getOrderList();
        response.then().statusCode(SC_OK)
                .and()
                .body("orders", notNullValue());
    }
}
