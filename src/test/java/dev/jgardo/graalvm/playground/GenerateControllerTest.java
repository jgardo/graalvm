package dev.jgardo.graalvm.playground;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.Header;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.MediaType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class GenerateControllerTest {

    @Test
    public void testHelloEndpoint() {
        var requestBody = """
                {
                    "templateName": "template",
                    "params": {
                        "someParam": []
                    }
                }""";

        given()
          .when()
                .body(requestBody)
                .header(new Header("Content-Type", MediaType.APPLICATION_JSON))
                .post("/api/generator")
          .then()
             .statusCode(200)
             .body(is("The templateName is template"));
    }

}
