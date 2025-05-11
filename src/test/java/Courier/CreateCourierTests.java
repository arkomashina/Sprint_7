package Courier;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.CoreMatchers.equalTo;

public class CreateCourierTests {

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
        response.then().statusCode(201)
                .and()
                .assertThat().body("ok", equalTo(true));
        // удалить курьера после теста

        CourierLoginRequest loginRequest = new CourierLoginRequest(newCourier.getLogin(), newCourier.getPassword());
        Response loginResponse = courierClient.loginCourier(loginRequest);
        int id = loginResponse.as(CourierLoginResponse.class).getId();
        courierClient.deleteCourier(id).then().statusCode(200);

    }


    @Test
    @DisplayName("Проверить ошибку при повторном создании курьера с существующим логином")
    public void courierCreationWithSameLoginTest() {
        Response response = courierClient.createCourier(courier);
        response.then().statusCode(409)
                .and()
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Проверить ошибку при попытке создания курьера с неполными данными")
    public void courierCreationWithEmptyLoginOrPasswordField() {
        List<CourierCreateRequest> wrongData = new ArrayList<>();
        wrongData.add(new CourierCreateRequest(null, "1234", "Naruto"));
        wrongData.add(new CourierCreateRequest("login" + System.currentTimeMillis(), null, "Naruto"));
        wrongData.add(new CourierCreateRequest(null, null, "Naruto"));
        wrongData.add(new CourierCreateRequest());

        for (CourierCreateRequest invalidCourier : wrongData) {
            Response response = courierClient.createCourier(invalidCourier);
            response.then().statusCode(400)
                    .and()
                    .body("message", equalTo("Недостаточно данных для создания учетной записи"));
        }
    }
}

