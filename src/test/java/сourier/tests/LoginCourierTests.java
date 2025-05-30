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
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.apache.http.HttpStatus.*;

public class LoginCourierTests {
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
    @DisplayName("Авторизация курьера с корректными данными")
    public void courierLoginTest() {
        CourierLoginRequest loginRequest = new CourierLoginRequest(courier.getLogin(), courier.getPassword());
        Response response = courierClient.loginCourier(loginRequest);
        response.then().statusCode(SC_OK)
                .and()
                .assertThat().body("id", notNullValue());
    }


    @Test
    @DisplayName("Авторизация без поля логина")
    public void courierLoginWithoutLoginFieldTest() {
        CourierLoginRequest loginRequest = new CourierLoginRequest("", "1234");
        Response response = courierClient.loginCourier(loginRequest);

        response.then().statusCode(SC_BAD_REQUEST)
                .and()
                .assertThat().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Авторизация без поля пароля")
    public void courierLoginWithoutPasswordFieldTest() {
        CourierLoginRequest loginRequest = new CourierLoginRequest("azazel", "");
        Response response = courierClient.loginCourier(loginRequest);

        response.then().statusCode(SC_BAD_REQUEST)
                .and()
                .assertThat().body("message", equalTo("Недостаточно данных для входа"));
    }
    @Test
    @DisplayName("Авторизация с несуществующим логином")
    public void courierLoginWithNonExistedLogin() {
        CourierLoginRequest loginRequest = new CourierLoginRequest("user" + System.currentTimeMillis(), "1234");
        Response response = courierClient.loginCourier(loginRequest);

        response.then().statusCode(SC_NOT_FOUND)
                .and()
                .assertThat().body("message", equalTo("Учетная запись не найдена"));
    }
    @Test
    @DisplayName("Авторизация с неверным паролем")
    public void courierLoginWithIncorrectPassword() {
        CourierLoginRequest loginRequest = new CourierLoginRequest(courier.getLogin(), "wrongPassword");
        Response response = courierClient.loginCourier(loginRequest);

        response.then().statusCode(SC_NOT_FOUND)
                .and()
                .assertThat().body("message", equalTo("Учетная запись не найдена"));
    }
}


