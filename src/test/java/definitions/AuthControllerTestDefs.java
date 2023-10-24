package definitions;

import com.example.simplechatbackend.SimpleChatBackendApplication;
import com.fasterxml.jackson.core.JacksonException;
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
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.function.Supplier;
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
        try {
            logger.info("Calling: The User is logged in and receives a JWT");

            JsonPath jsonPath = response.jsonPath();

            Assert.assertNotEquals(jsonPath.get("jwt"), ("Authentication Failed"));
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        }
    }
}
