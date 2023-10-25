package definitions;

import com.example.simplechatbackend.SimpleChatBackendApplication;
import io.cucumber.java.en.Given;
import io.cucumber.spring.CucumberContextConfiguration;
import io.restassured.response.Response;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.logging.Logger;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SimpleChatBackendApplication.class)

public class UserControllerTestDefs {

    private static final Logger logger = Logger.getLogger(AuthControllerTestDefs.class.getName());

    private static final String BASE_URL = "http://localhost:";

    @LocalServerPort
    String port;

    private static Response response;

    @Given("The User is logged in")
    public void theUserIsLoggedIn() {
    }
}
