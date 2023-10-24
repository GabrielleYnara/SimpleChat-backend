package definitions;

import com.example.simplechatbackend.SimpleChatBackendApplication;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.HttpClientErrorException;

import java.util.logging.Logger;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SimpleChatBackendApplication.class)
public class AuthControllerTestDefs {

    private static final Logger logger = Logger.getLogger(AuthControllerTestDefs.class.getName());

    private static final String BASE_URL = "http://localhost:";

    @LocalServerPort
    String port;

    private static Response response;

    @Given("The User tries to log in with a username and password combo that exists in the database")
    public void userTriesToLogin() throws JSONException {
        logger.info("Calling: The User tries to log in with a username and password combo that exists in the database");

        try {
            RestAssured.baseURI = BASE_URL;
            RequestSpecification request = RestAssured.given();
            JSONObject requestBody = new JSONObject();
            requestBody.put("username", "bobmyers");
            requestBody.put("password", "password1");
            request.header("Content-Type", "application/json");

            response = request.body(requestBody.toString()).post(BASE_URL+port+"/auth/login");

            Assert.assertEquals(200, response.getStatusCode());
        }
        catch (HttpClientErrorException e) {
            e.printStackTrace();
        }
    }

    @Then("The User is logged in and receives a JWT")
    public void userReceivesJWT() throws JSONException {
        logger.info("Calling: The User is logged in and receives a JWT");

        try {
            JsonPath jsonPath = response.jsonPath();

            Assert.assertNotEquals(jsonPath.get("jwt"), ("Authentication Failed"));
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        }
    }

    @When("A new User registers for an account")
    public void registerNewUser() throws JSONException {
        logger.info("Calling: A new User registers for an account");

        try {
            RequestSpecification request = RestAssured.given();
            JSONObject requestBody = new JSONObject();
            requestBody.put("username", "jimmymyers");
            requestBody.put("password", "password3");
            request.header("Content-Type", "application/json");

            response = request.body(requestBody.toString()).post(BASE_URL+port+"/auth/register");
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        }
    }

    @Then("The new User is saved to the database")
    public void newUserSaved() throws JSONException {
        logger.info("Calling: The new User is saved to the database");
        try {
            Assert.assertEquals(201, response.getStatusCode());

            JsonPath jsonPath = response.jsonPath();

            Assert.assertEquals(jsonPath.get("message"), ("Successfully registered new User: jimmymyers"));
            Assert.assertNotNull(jsonPath.get("data"));
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        }
    }

    @Then("The new User is able to log in and receives a JWT")
    public void newUserReceivesJWT() throws JSONException {
        logger.info("Calling: The new User is able to log in and receives a JWT");

        try {
            RestAssured.baseURI = BASE_URL;
            RequestSpecification request = RestAssured.given();
            JSONObject requestBody = new JSONObject();
            requestBody.put("username", "jimmymyers");
            requestBody.put("password", "password3");
            request.header("Content-Type", "application/json");

            response = request.body(requestBody.toString()).post(BASE_URL+port+"/auth/login");

            Assert.assertEquals(200, response.getStatusCode());

            JsonPath jsonPath = response.jsonPath();

            Assert.assertNotEquals(jsonPath.get("jwt"), ("Authentication Failed"));
        }
        catch (HttpClientErrorException e) {
            e.printStackTrace();
        }
    }
}
