package сourier.tests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import сourier.data.CourierClient;
import сourier.data.CourierCreateRequest;
import сourier.data.CourierLoginRequest;
import сourier.data.CourierLoginResponse;


import static org.hamcrest.CoreMatchers.equalTo;
import static org.apache.http.HttpStatus.*;

public class CreateCourierTests {

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

    @After
    public void deleteCourierAfterTests() {
        courierClient.deleteCourier(courierId);
    }

    @Test
    @DisplayName("Проверить создание уникального курьера")
    public void courierCreationWithUniqueLoginTest() {

        CourierCreateRequest newCourier = new CourierCreateRequest("user" + System.currentTimeMillis(), "1234", "Saske");
        // создать курьера уникального
        Response response = courierClient.createCourier(newCourier);
        response.then().statusCode(SC_CREATED)
                .and()
                .assertThat().body("ok", equalTo(true));
        // удалить курьера после теста

        CourierLoginRequest loginRequest = new CourierLoginRequest(newCourier.getLogin(), newCourier.getPassword());
        Response loginResponse = courierClient.loginCourier(loginRequest);
        int id = loginResponse.as(CourierLoginResponse.class).getId();
        courierClient.deleteCourier(id).then().statusCode(SC_OK);

    }


    @Test
    @DisplayName("Проверить ошибку при повторном создании курьера с существующим логином")
    public void courierCreationWithSameLoginTest() {
        Response response = courierClient.createCourier(courier);
        response.then().statusCode(SC_CONFLICT)
                .and()
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }


}

