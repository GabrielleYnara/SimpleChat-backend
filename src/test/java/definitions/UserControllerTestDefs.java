package definitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.HttpClientErrorException;

import java.util.logging.Logger;

public class UserControllerTestDefs {

    private static final Logger logger = Logger.getLogger(AuthControllerTestDefs.class.getName());

    private static final String BASE_URL = "http://localhost:";

    @LocalServerPort
    String port;

    private static Response response;

    private static String jwt;

    @Given("The User is logged in")
    public void theUserIsLoggedIn() throws JSONException {
        logger.info("Calling: The User is logged in");
        try {
            RestAssured.baseURI = BASE_URL;
            RequestSpecification request = RestAssured.given();
            JSONObject requestBody = new JSONObject();
            requestBody.put("username", "bobmyers");
            requestBody.put("password", "password1");
            request.header("Content-Type", "application/json");

            response = request.body(requestBody.toString()).post(BASE_URL + port + "/auth/login");

            Assert.assertEquals(200, response.getStatusCode());

            JsonPath jsonPath = response.jsonPath();
            jwt = jsonPath.get("jwt");

            Assert.assertNotEquals(jwt, ("Authentication Failed"));
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        }
    }

    @When("The User sends a request for all the Rooms in the database")
    public void requestRooms() throws JSONException {
        logger.info("Calling: The User sends a request for all the Rooms in the database");
        try {
            RestAssured.baseURI = BASE_URL;
            RequestSpecification request = RestAssured.given();
            request.header("Content-Type", "application/json");
            request.header("Authorization", "Bearer " + jwt);

            response = request.get(BASE_URL + port + "/rooms");

            Assert.assertEquals(200, response.getStatusCode());
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        }
    }
}
