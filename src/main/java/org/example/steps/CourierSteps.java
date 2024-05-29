package org.example.steps;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.example.courier.*;

import static io.restassured.RestAssured.given;
import static java.net.HttpURLConnection.*;
import static org.example.constants.Urls.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CourierSteps {

    @Step("Create courier")
    public Response createCourier(String login, String pass, String name) {
        CreateCourier courier = new CreateCourier(login, pass, name);
        return given()
                .contentType(ContentType.JSON)
                .body(courier)
                .when()
                .post(CREATE_COURIER);
    }

    @Step("Create login courier")
    public Response loginCourier(String login, String pass) {
        LoginCourier loginCourier = new LoginCourier(login, pass);
        return given()
                .contentType(ContentType.JSON)
                .body(loginCourier)
                .when()
                .post(LOGIN_COURIER);
    }

    @Step("Get id courier")
    public Integer getCourierId(String login, String pass) {
        return loginCourier(login, pass)
                .body()
                .as(CreateCourier.class).getId();
    }
    @Step("Preparing a deleting request")
    public String courierDeletePreparingToString(Integer courierID) {
        return DELETE_COURIER + courierID;
    }

    @Step("Delete courier")
    public Response deleteCourier(String login, String pass) {
        return given()
                .contentType(ContentType.JSON)
                .when()
                .delete(courierDeletePreparingToString(getCourierId(login, pass)));
    }

    @Step("Check body - ok and server status on first valid registration (201)")
    public void checkAnswerValidRegistration(Response response) {
        response
                .then()
                .statusCode(HTTP_CREATED)
                .and().assertThat().body("ok", equalTo(true));
    }

    @Step("Check body - ok and server status on deletion (200)")
    public void checkAnswerValidDeleting(Response response) {
        response
                .then()
                .statusCode(HTTP_OK)
                .and().assertThat().body("ok", equalTo(true));
    }

    @Step("Checking the body and status server when re-registering (409)")
    public void checkAnswerReuseRegistration(Response response) {
        response.then()
                .statusCode(HTTP_CONFLICT)
                .and().assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Step("Checking the body and status server with incomplete data (400)")
    public void checkAnswerWithNotEnoughRegData(Response response) {
        response.then()
                .statusCode(HTTP_BAD_REQUEST)
                .and().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Step("Checking the presence of the courier id-number by his login and password")
    public void checkAnswerAndPresenceId(Response response) {
        response.then()
                .statusCode(HTTP_OK).and().assertThat().body("id", notNullValue());
    }

    @Step("The system will return an error if the username or password is incorrect")
    public void checkAnswerWithWrongData(Response response) {
        response.then()
                .statusCode(HTTP_NOT_FOUND).assertThat().body("message", equalTo("Учетная запись не найдена"));
    }

    @Step("The system will return an error if the username or password is incorrect")
    public void checkAnswerWithoutData(Response response) {
        response.then()
                .statusCode(HTTP_BAD_REQUEST).assertThat().body("message", equalTo("Недостаточно данных для входа"));
    }
}

