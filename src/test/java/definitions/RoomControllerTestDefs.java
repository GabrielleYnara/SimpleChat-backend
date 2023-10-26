package definitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
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

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class RoomControllerTestDefs {

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

    @Then("The User receives a list of Rooms in the database")
    public void receiveRooms() throws JSONException {
        logger.info("Calling: The User receives a list of Rooms in the database");

        try {
            JsonPath jsonPath = response.jsonPath();
            List<Map<String, String>> rooms = jsonPath.get("data");
            Assert.assertEquals(rooms.size(), 3);  // number of Rooms created in Seed Data
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        }
    }

    @When("The User tries to create a new Room")
    public void createRoom() throws JSONException {
        logger.info("Calling: The User tries to create a new Room");

        try {
            RestAssured.baseURI = BASE_URL;
            RequestSpecification request = RestAssured.given();
            request.header("Content-Type", "application/json");
            request.header("Authorization", "Bearer " + jwt);

            JSONObject requestBody = new JSONObject();
            requestBody.put("name", "Rabbits");

            response = request.body(requestBody.toString()).post(BASE_URL + port + "/rooms");

            Assert.assertEquals(201, response.getStatusCode());
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        }
    }

    @Then("The New Room is created")
    public void newRoomCreated() throws JSONException {
        logger.info("Calling: The New Room is created");

        try {
            JsonPath jsonPath = response.jsonPath();
            Assert.assertEquals(jsonPath.get("message"), ("Successfully created new Room: Rabbits"));
            Assert.assertNotNull(jsonPath.get("data"));
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        }
    }

    @Given("The User is inside a specific Room")
    public void enterRoom() throws  JSONException {
        logger.info("Calling: The User is inside a specific Room");

        try {
            RestAssured.baseURI = BASE_URL;
            RequestSpecification request = RestAssured.given();
            JSONObject requestBody = new JSONObject();

            request.header("Content-Type", "application/json");
            request.header("Authorization", "Bearer " + jwt);

            response = request.body(requestBody.toString()).get(BASE_URL + port + "/rooms/1");

            Assert.assertEquals(200, response.getStatusCode());

            JsonPath jsonPath = response.jsonPath();
            Assert.assertEquals(jsonPath.get("message"), ("Success!"));
            Assert.assertNotNull(jsonPath.get("data"));
            Assert.assertEquals(jsonPath.get("data.name"), "Dogs");

        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        }
    }

    @Then("The User should be able to get a list of all Chats for that Room")
    public void getChatList() throws JSONException {
        logger.info("Calling: The User should be able to get a list of all Chats for that Room");
        try {
            JsonPath jsonPath = response.jsonPath();
            Assert.assertNotNull(jsonPath.get("data.chatList"));

            List<Map<String, String>> chats = jsonPath.get("data.chatList");
            Assert.assertEquals(chats.size(), 3);  //number of Chats inside Room 1 in SeedData
            Assert.assertEquals(chats.get(0).get("message"), "I love dogs!");
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        }
    }

    @When("The User tries to create a new Chat")
    public void createChat() throws JSONException {
        logger.info("Calling: The User tries to create a new Chat");

        try {
            RestAssured.baseURI = BASE_URL;
            RequestSpecification request = RestAssured.given();
            JSONObject requestBody = new JSONObject();
            requestBody.put("message", "Mine are Golden Retrievers!");

            request.header("Content-Type", "application/json");
            request.header("Authorization", "Bearer " + jwt);

            response = request.body(requestBody.toString()).post(BASE_URL + port + "/rooms/1");

            Assert.assertEquals(201, response.getStatusCode());
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        }
    }

    @Then("The New Chat is created")
    public void newChatCreated() throws JSONException {
        logger.info("Calling: The New Chat is created");

        try {
            JsonPath jsonPath = response.jsonPath();
            Assert.assertNotNull(jsonPath.get("data"));
            Assert.assertEquals(jsonPath.get("message"), ("Successfully created new Chat: Mine are Golden Retrievers!"));
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        }
    }

    @When("The User tries to update a Chat that belongs to them")
    public void updateChat() throws JSONException {
        logger.info("Calling: The User tries to update a Chat that belongs to them");

        try {
            RestAssured.baseURI = BASE_URL;
            RequestSpecification request = RestAssured.given();
            JSONObject requestBody = new JSONObject();
            requestBody.put("message", "I loooooove dogs!");

            request.header("Content-Type", "application/json");
            request.header("Authorization", "Bearer " + jwt);

            response = request.body(requestBody.toString()).put(BASE_URL + port + "/rooms/1/chats/1");

            Assert.assertEquals(200, response.getStatusCode());
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        }
    }

    @Then("The Chat is updated")
    public void chatIsUpdated() throws JSONException {
        logger.info("Calling: The Chat is updated");

        try {
            JsonPath jsonPath = response.jsonPath();
            Assert.assertNotNull(jsonPath.get("data"));
            Assert.assertEquals(jsonPath.get("message"), ("Successfully updated Chat with id: 1"));
            Assert.assertEquals(jsonPath.get("data.message"), "I loooooove dogs!");
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        }
    }
}
