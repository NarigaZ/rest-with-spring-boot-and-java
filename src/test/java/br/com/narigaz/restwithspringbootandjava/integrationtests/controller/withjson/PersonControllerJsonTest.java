package br.com.narigaz.restwithspringbootandjava.integrationtests.controller.withjson;

import br.com.narigaz.restwithspringbootandjava.configs.TestConfigs;
import br.com.narigaz.restwithspringbootandjava.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.narigaz.restwithspringbootandjava.integrationtests.vo.AccountCredentialsVO;
import br.com.narigaz.restwithspringbootandjava.integrationtests.vo.PersonVO;
import br.com.narigaz.restwithspringbootandjava.integrationtests.vo.TokenVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonControllerJsonTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;
    private static PersonVO person;

    @BeforeAll
    public static void setUp(){
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        person = new PersonVO();
    }

    @Test
    @Order(0)
    public void Authorization() {
        AccountCredentialsVO user = new AccountCredentialsVO("leandro", "admin123");

        var accessToken = given()
                .basePath("auth/signin")
                .port(TestConfigs.SERVER_PORT)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(user)
                    .when()
                .post()
                    .then()
                        .statusCode(200)
                            .extract()
                                .body()
                                    .as(TokenVO.class)
                                .getAccessToken();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + accessToken)
                .setBasePath("/api/v1/person")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

    }
    @Test
    @Order(1)
    public void testCreate() throws JsonProcessingException {
        mockPerson();
        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_NARIGAZ)
                .body(person)
                .when()
                    .post()
                .then()
                    .statusCode(200)
                .extract()
                    .body()
                        .asString();

        PersonVO createdPerson = objectMapper.readValue(content, PersonVO.class);
        person = createdPerson;

        assertNotNull(createdPerson);
        assertNotNull(createdPerson.getFirstName());
        assertNotNull(createdPerson.getLastName());
        assertNotNull(createdPerson.getAddress());
        assertNotNull(createdPerson.getGender());

        assertTrue(createdPerson.getId() > 0);

        assertEquals("Nelson", createdPerson.getFirstName());
        assertEquals("Ferrer", createdPerson.getLastName());
        assertEquals("New York City, New York, US", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
    }

    @Test
    @Order(2)
    public void testUpdate() throws JsonProcessingException {
        person.setLastName("Piquet Maior");

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_NARIGAZ)
                .body(person)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        PersonVO createdPerson = objectMapper.readValue(content, PersonVO.class);
        person = createdPerson;

        assertNotNull(createdPerson);
        assertNotNull(createdPerson.getFirstName());
        assertNotNull(createdPerson.getLastName());
        assertNotNull(createdPerson.getAddress());
        assertNotNull(createdPerson.getGender());

        assertTrue(createdPerson.getId() > 0);

        assertEquals("Nelson", createdPerson.getFirstName());
        assertEquals("Piquet Maior", createdPerson.getLastName());
        assertEquals("New York City, New York, US", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
    }

    @Test
    @Order(3)
    public void testFindiById() throws JsonProcessingException {
        mockPerson();

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_NARIGAZ)
                    .pathParam("id", person.getId())
                .when()
                    .get("{id}")
                .then()
                    .statusCode(200)
                        .extract()
                            .body()
                                .asString();


        PersonVO persistedPerson = objectMapper.readValue(content, PersonVO.class);
        person = persistedPerson;

        assertNotNull(persistedPerson);
        assertNotNull(persistedPerson.getFirstName());
        assertNotNull(persistedPerson.getLastName());
        assertNotNull(persistedPerson.getAddress());
        assertNotNull(persistedPerson.getGender());

        assertTrue(persistedPerson.getId() > 0);

        assertEquals("Nelson", persistedPerson.getFirstName());
        assertEquals("Piquet Maior", persistedPerson.getLastName());
        assertEquals("New York City, New York, US", persistedPerson.getAddress());
        assertEquals("Male", persistedPerson.getGender());
    }

    @Test
    @Order(4)
    public void testDelete() {
        given().spec(specification)
                .pathParam("id", person.getId())
                .when()
                    .delete("{id}")
                .then()
                    .statusCode(204);
    }

    @Test
    @Order(5)
    public void testFindAll() throws JsonProcessingException {
        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .when()
                    .get()
                .then()
                    .statusCode(200)
                        .extract()
                            .body()
                .asString();
        List<PersonVO> people = objectMapper.readValue(content, new TypeReference<>() {});

        PersonVO foundPersonOne = people.get(0);

        assertNotNull(foundPersonOne);
        assertNotNull(foundPersonOne.getFirstName());
        assertNotNull(foundPersonOne.getLastName());
        assertNotNull(foundPersonOne.getAddress());
        assertNotNull(foundPersonOne.getGender());

        assertEquals(1,foundPersonOne.getId());

        assertEquals("Felipe", foundPersonOne.getFirstName());
        assertEquals("Rezende", foundPersonOne.getLastName());
        assertEquals("Goiania", foundPersonOne.getAddress());
        assertEquals("Male", foundPersonOne.getGender());

        PersonVO foundPersonFour = people.get(4);

        assertNotNull(foundPersonFour);
        assertNotNull(foundPersonFour.getFirstName());
        assertNotNull(foundPersonFour.getLastName());
        assertNotNull(foundPersonFour.getAddress());
        assertNotNull(foundPersonFour.getGender());

        assertEquals(9,foundPersonFour.getId());

        assertEquals("Nelson", foundPersonFour.getFirstName());
        assertEquals("Mvezo", foundPersonFour.getLastName());
        assertEquals("Mvezo - South Africa", foundPersonFour.getAddress());
        assertEquals("Male", foundPersonFour.getGender());
    }

    @Test
    @Order(5)
    public void testFindAllWithoutToken() {

        var specification = new RequestSpecBuilder()
                .setBasePath("/api/v1/person")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

         given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .when()
                .get()
                .then()
                .statusCode(403);
    }

    private void mockPerson() {
        person.setFirstName("Nelson");
        person.setLastName("Ferrer");
        person.setAddress("New York City, New York, US");
        person.setGender("Male");
    }
}
