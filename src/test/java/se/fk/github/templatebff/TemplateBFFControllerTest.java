package se.fk.github.templatebff;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
@QuarkusTestResource(WireMockTestResource.class)
class TemplateBFFControllerTest
{

   @BeforeEach
   void setUp()
   {
      WireMockTestResource.getServer().resetAll();
   }

   @Test
   void health_returns200WithStatusOk()
   {
      given()
            .when()
            .get("/api/health")
            .then()
            .statusCode(200)
            .body("status", equalTo("ok"))
            .body("timestamp", notNullValue());
   }

   @Test
   void getTask_returnsTaskData()
   {
      WireMockTestResource.getServer().stubFor(get(urlEqualTo("/task-123"))
            .willReturn(aResponse()
                  .withHeader("Content-Type", "application/json")
                  .withBody("{\"handlaggningId\": \"task-123\", \"status\": \"AKTIV\"}")));

      given()
            .contentType(ContentType.JSON)
            .body("{\"handlaggningId\": \"task-123\"}")
            .when()
            .post("/api/task")
            .then()
            .statusCode(200)
            .body("handlaggningId", equalTo("task-123"))
            .body("status", equalTo("AKTIV"));
   }

   @Test
   void getTask_returns400_whenHandlaggningIdIsBlank()
   {
      given()
            .contentType(ContentType.JSON)
            .body("{\"handlaggningId\": \"\"}")
            .when()
            .post("/api/task")
            .then()
            .statusCode(400);
   }

   @Test
   void getTask_returns500_whenBackendFails()
   {
      WireMockTestResource.getServer().stubFor(get(urlEqualTo("/task-123"))
            .willReturn(aResponse().withStatus(500)));

      given()
            .contentType(ContentType.JSON)
            .body("{\"handlaggningId\": \"task-123\"}")
            .when()
            .post("/api/task")
            .then()
            .statusCode(500)
            .body("error", equalTo("Upstream error"));
   }

   @Test
   void patchTask_returns200_onSuccess()
   {
      WireMockTestResource.getServer().stubFor(patch(urlEqualTo("/task-123"))
            .willReturn(aResponse().withStatus(200)));

      given()
            .contentType(ContentType.JSON)
            .body("{\"handlaggningId\": \"task-123\", \"ersattningId\": \"ers-1\", \"yrkandestatus\": \"GODKAND\"}")
            .when()
            .patch("/api/task")
            .then()
            .statusCode(200);
   }

   @Test
   void patchTask_returns400_whenHandlaggningIdIsBlank()
   {
      given()
            .contentType(ContentType.JSON)
            .body("{\"handlaggningId\": \"\", \"ersattningId\": \"ers-1\", \"yrkandestatus\": \"GODKAND\"}")
            .when()
            .patch("/api/task")
            .then()
            .statusCode(400);
   }

   @Test
   void patchTask_returns500_whenBackendFails()
   {
      WireMockTestResource.getServer().stubFor(patch(urlEqualTo("/task-123"))
            .willReturn(aResponse().withStatus(500)));

      given()
            .contentType(ContentType.JSON)
            .body("{\"handlaggningId\": \"task-123\", \"ersattningId\": \"ers-1\", \"yrkandestatus\": \"GODKAND\"}")
            .when()
            .patch("/api/task")
            .then()
            .statusCode(500)
            .body("error", equalTo("Upstream error"));
   }

   @Test
   void taskDone_returns204_onSuccess()
   {
      WireMockTestResource.getServer().stubFor(post(urlEqualTo("/task-123/done"))
            .willReturn(aResponse().withStatus(204)));

      given()
            .contentType(ContentType.JSON)
            .body("{\"handlaggningId\": \"task-123\"}")
            .when()
            .post("/api/task/done")
            .then()
            .statusCode(204);
   }

   @Test
   void taskDone_returns500_whenBackendFails()
   {
      WireMockTestResource.getServer().stubFor(post(urlEqualTo("/task-123/done"))
            .willReturn(aResponse().withStatus(500)));

      given()
            .contentType(ContentType.JSON)
            .body("{\"handlaggningId\": \"task-123\"}")
            .when()
            .post("/api/task/done")
            .then()
            .statusCode(500)
            .body("error", equalTo("Upstream error"));
   }

   @Test
   void getUppgiftsbeskrivning_returnsData()
   {
      WireMockTestResource.getServer().stubFor(get(urlEqualTo("/utokadUppgiftsbeskrivning"))
            .willReturn(aResponse()
                  .withHeader("Content-Type", "application/json")
                  .withBody("[{\"beskrivning\": \"Kontrollera ersättningsberäkning\"}]")));

      given()
            .when()
            .get("/api/uppgiftsbeskrivning")
            .then()
            .statusCode(200)
            .body("[0].beskrivning", equalTo("Kontrollera ersättningsberäkning"));
   }

   @Test
   void getUppgiftsbeskrivning_returns502_whenBackendUnavailable()
   {
      WireMockTestResource.getServer().stubFor(get(urlEqualTo("/utokadUppgiftsbeskrivning"))
            .willReturn(aResponse().withFault(com.github.tomakehurst.wiremock.http.Fault.CONNECTION_RESET_BY_PEER)));

      given()
            .when()
            .get("/api/uppgiftsbeskrivning")
            .then()
            .statusCode(502)
            .body("error", equalTo("Backend service unavailable"));
   }
}
