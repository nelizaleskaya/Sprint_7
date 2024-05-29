package org.example.steps;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.example.courier.CreateOrder;

import static io.restassured.RestAssured.given;
import static java.net.HttpURLConnection.HTTP_CREATED;
import static java.net.HttpURLConnection.HTTP_OK;
import static org.example.constants.Urls.*;
import static org.hamcrest.Matchers.notNullValue;

public class OrderSteps {
    @Step("Создание нового заказа с использованием {order}")
    public Response createOrder(CreateOrder order) {
        return given()
                .contentType(ContentType.JSON)
                .body(order)
                .when()
                .post(ORDERS);
    }

    @Step("Checking the response body contains a non-empty \"track\"")
    public void checkOrderTrackNotNullNew(Response response) {
        response.then()
                .statusCode(HTTP_CREATED).and().assertThat().body("track", notNullValue());
    }

    @Step("Request a list of orders")
    public Response getOrdersList() {
        return given()
                .contentType(ContentType.JSON)
                .when()
                .get(ORDERS);
    }

    @Step("Check getting a list of orders is not empty")
    public void checkOrderListNotNullNew(Response response) {
        response.then()
                .statusCode(HTTP_OK).and().assertThat().body("orders", notNullValue());
    }

}

