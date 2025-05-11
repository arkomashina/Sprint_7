package Courier;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class DeleteCourierTests {
    private CourierClient courierClient;
    private CourierCreateRequest courier;

    private int courierId;



    @Before
    public void setUp() {
        courierClient = new CourierClient();
        courier = new CourierCreateRequest("azazel" + System.currentTimeMillis(), "1234", "naruto");

        Response createResponse = courierClient.createCourier(courier);
        createResponse.then().statusCode(201);

        CourierLoginRequest loginRequest = new CourierLoginRequest(courier.getLogin(), courier.getPassword());
        Response loginResponse = courierClient.loginCourier(loginRequest);
        loginResponse.then().statusCode(200);

        CourierLoginResponse loginData = loginResponse.as(CourierLoginResponse.class);
        courierId = loginData.getId();
    }

    @Test
    @DisplayName("Проверить успешное удаление курьера")
    public void deleteCourierTest() {
        Response response = courierClient.deleteCourier(courierId);
        response.then().statusCode(200)
                .and().assertThat().body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Проверить отображение ошибки при попытке удаления курьера с несуществующим id")
    public void deleteCourierWithWrongIdTest() {
        Response response = courierClient.deleteCourierWithWrongId(0);
        response.then().statusCode(404)
                .and().assertThat().body("message", equalTo("Курьера с таким id нет."));
    }

    @Test
    @DisplayName("Проверить отображение ошибки при попытке удаления курьера с пустым id")
    public void deleteCourierWithoutIdTest() {
        Response response = courierClient.deleteCourierWithoutId();
        response.then().statusCode(404);
    }


}
