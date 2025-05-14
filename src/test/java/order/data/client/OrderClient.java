package order.data.client;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import order.data.model.Order;
import order.data.service.OrderService;


public class OrderClient {
    private final OrderService orderService = new OrderService();

    @Step("Создать заказ")
    public Response createOrder(Order order) {
        return orderService.createOrder(order);
    }

    @Step("Получить список заказов")
    public Response getOrderList() {
        return orderService.getOrderList();
    }


}
