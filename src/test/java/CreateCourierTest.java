import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.example.steps.CourierSteps;

import static org.example.constants.RandomData.*;
import static org.example.constants.Urls.URL;

public class CreateCourierTest {

    CourierSteps courierSteps;

    @Before
    public void setUp() {
        RestAssured.baseURI = URL;
        courierSteps = new CourierSteps();
    }


    @Test
    @DisplayName("Creating new courier")
    @Description("Creating new courier with correct credentials and check the positive creating courier")
    public void creatingCourierCorrectData() {
        Response responseCreate = courierSteps.createCourier(RANDOM_LOGIN, RANDOM_PASS, RANDOM_NAME);
        courierSteps.checkAnswerValidRegistration(responseCreate);
        Response responseDelete = courierSteps.deleteCourier(RANDOM_LOGIN, RANDOM_PASS);
        courierSteps.checkAnswerValidDeleting(responseDelete);
    }

    @Test
    @DisplayName("Creating identical couriers")
    @Description("Checking response (status code and body) when trying to create identical couriers")
    public void creatingIdenticalCouriers() {
        courierSteps.createCourier(RANDOM_LOGIN, RANDOM_PASS, RANDOM_NAME);
        Response responseIdentical = courierSteps.createCourier(RANDOM_LOGIN, RANDOM_PASS, RANDOM_NAME);
        courierSteps.checkAnswerReuseRegistration(responseIdentical);
    }

    @Test
    @DisplayName("Creating a courier with an existing login")
    @Description("Creating a courier with an existing login and password checking the response")
    public void creatingCourierWithExistingLogin() {
        courierSteps.createCourier(RANDOM_LOGIN, RANDOM_PASS, RANDOM_NAME);
        Response responseExisting = courierSteps.createCourier(RANDOM_LOGIN, RANDOM_PASS, RANDOM_NAME);
        courierSteps.checkAnswerReuseRegistration(responseExisting);
        Response responseDelete = courierSteps.deleteCourier(RANDOM_LOGIN, RANDOM_PASS);
        courierSteps.checkAnswerValidDeleting(responseDelete);
    }

    @Test
    @DisplayName("Creating a courier without login")
    @Description("Creating a courier without login and checking the response")
    public void creatingCourierWithoutLogin() {
        Response responseWithoutLogin = courierSteps.createCourier("", RANDOM_PASS, RANDOM_NAME);
        courierSteps.checkAnswerWithNotEnoughRegData(responseWithoutLogin);
    }

    @Test
    @DisplayName("Creating a courier without password")
    @Description("Creating a courier without password and checking the response")
    public void creatingCourierWithoutPassword() {
        Response responseWithoutPass = courierSteps.createCourier(RANDOM_LOGIN, "", RANDOM_NAME);
        courierSteps.checkAnswerWithNotEnoughRegData(responseWithoutPass);
    }

    @Test
    @DisplayName("Creating a courier without firstName")
    @Description("Creating a courier without firstName and checking the response")
    public void creatingCourierWithoutNamePositive() {
        Response responseWithoutName = courierSteps.createCourier(RANDOM_LOGIN, RANDOM_PASS, "");
        courierSteps.checkAnswerValidRegistration(responseWithoutName);
        Response responseDelete = courierSteps.deleteCourier(RANDOM_LOGIN, RANDOM_PASS);
        courierSteps.checkAnswerValidDeleting(responseDelete);
    }

}

