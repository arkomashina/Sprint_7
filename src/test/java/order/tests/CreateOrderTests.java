package order.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import order.data.model.Order;
import order.data.client.OrderClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.apache.http.HttpStatus.*;

@Feature("Создание заказа")
@RunWith(Parameterized.class)
public class CreateOrderTests {
    private final String[] colour;
    private final OrderClient orderClient = new OrderClient();

    public CreateOrderTests(String[] colour) {
        this.colour = colour;
    }

    @Parameterized.Parameters(name = "Цвет: {0}")
    public static Object[][] getTestData() {
        return new Object[][] {
                {new String[]{"BLACK"}},
                {new String[]{"GREY"}},
                {new String[]{"BLACK", "GREY"}},
                {new String[]{}},
        };
    }

    @Test
    @DisplayName("Успешное создание заказа")
    @Description("Тест проверяет, что заказ можно создать с разными комбинациями цветов")
    public void createSuccessfulOrderTest() {
        Order order = new Order("Naruto", "Uchiha", "Konoha, 142 apt.", "4",
                "+7 800 355 35 35", 5, "2020-06-06", "Saske, come back to Konoha", colour);

        Response response = orderClient.createOrder(order);
        response.then().statusCode(SC_CREATED)
                .and()
                .assertThat().body("track", notNullValue());
        System.out.println(response.body().asString());
    }
}
