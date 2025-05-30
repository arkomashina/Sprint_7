package сourier.tests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import сourier.data.CourierClient;
import сourier.data.CourierCreateRequest;
import сourier.data.CourierLoginRequest;
import сourier.data.CourierLoginResponse;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.apache.http.HttpStatus.*;

public class DeleteCourierTests {
    private CourierClient courierClient;
    private CourierCreateRequest courier;

    private int courierId;



    @Before
    public void setUp() {
        courierClient = new CourierClient();
        courier = new CourierCreateRequest("azazel" + System.currentTimeMillis(), "1234", "naruto");

        Response createResponse = courierClient.createCourier(courier);
        createResponse.then().statusCode(SC_CREATED);

        CourierLoginRequest loginRequest = new CourierLoginRequest(courier.getLogin(), courier.getPassword());
        Response loginResponse = courierClient.loginCourier(loginRequest);
        loginResponse.then().statusCode(SC_OK);

        CourierLoginResponse loginData = loginResponse.as(CourierLoginResponse.class);
        courierId = loginData.getId();
    }

    @Test
    @DisplayName("Проверить успешное удаление курьера")
    public void deleteCourierTest() {
        Response response = courierClient.deleteCourier(courierId);
        response.then().statusCode(SC_OK)
                .and().assertThat().body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Проверить отображение ошибки при попытке удаления курьера с несуществующим id")
    public void deleteCourierWithWrongIdTest() {
        Response response = courierClient.deleteCourier(0);
        response.then().statusCode(SC_NOT_FOUND)
                .and().assertThat().body("message", equalTo("Курьера с таким id нет."));
    }

    @Test
    @DisplayName("Проверить отображение ошибки при попытке удаления курьера с пустым id")
    public void deleteCourierWithoutIdTest() {
        Response response = courierClient.deleteCourierWithoutId();
        response.then().statusCode(SC_NOT_FOUND);
    }


}
