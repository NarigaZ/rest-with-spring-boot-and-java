package br.com.narigaz.restwithspringbootandjava.integrationtests.controller.withYaml;

import br.com.narigaz.restwithspringbootandjava.configs.TestConfigs;
import br.com.narigaz.restwithspringbootandjava.integrationtests.controller.withYaml.mapper.YMLMapper;
import br.com.narigaz.restwithspringbootandjava.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.narigaz.restwithspringbootandjava.integrationtests.vo.AccountCredentialsVO;
import br.com.narigaz.restwithspringbootandjava.integrationtests.vo.PersonVO;
import br.com.narigaz.restwithspringbootandjava.integrationtests.vo.TokenVO;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonControllerYamlTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static YMLMapper objectMapper;
    private static PersonVO person;

    @BeforeAll
    public static void setUp() {
        objectMapper = new YMLMapper();

        person = new PersonVO();
    }

    @Test
    @Order(0)
    void Authorization() {
        AccountCredentialsVO user = new AccountCredentialsVO("leandro", "admin123");

        var accessToken = given()
                .config(
                        RestAssuredConfig
                                .config()
                                .encoderConfig(EncoderConfig.encoderConfig()
                                        .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                .basePath("auth/signin")
                .port(TestConfigs.SERVER_PORT)
                .contentType(TestConfigs.CONTENT_TYPE_YML)
                .accept(TestConfigs.CONTENT_TYPE_YML)
                .body(user, objectMapper)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TokenVO.class, objectMapper)
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
    void testCreate() {
        mockPerson();
        var content = given()
                .config(
                        RestAssuredConfig
                                .config()
                                .encoderConfig(EncoderConfig.encoderConfig()
                                        .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                .spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_YML)
                .accept(TestConfigs.CONTENT_TYPE_YML)
                .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_NARIGAZ)
                .body(person, objectMapper)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(PersonVO.class, objectMapper);

        assertNotNull(content);
        assertNotNull(content.getFirstName());
        assertNotNull(content.getLastName());
        assertNotNull(content.getAddress());
        assertNotNull(content.getGender());
        assertTrue(content.isEnabled());

        assertTrue(content.getId() > 0);

        assertEquals("Nelson", content.getFirstName());
        assertEquals("Ferrer", content.getLastName());
        assertEquals("New York City, New York, US", content.getAddress());
        assertEquals("Male", content.getGender());
    }

    @Test
    @Order(2)
    void testUpdate() {
        person.setLastName("Piquet Maior");

        var content = given()
                .config(
                        RestAssuredConfig
                                .config()
                                .encoderConfig(EncoderConfig.encoderConfig()
                                        .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                .spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_YML)
                .accept(TestConfigs.CONTENT_TYPE_YML)
                .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_NARIGAZ)
                .body(person, objectMapper)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(PersonVO.class, objectMapper);

        person = content;

        assertNotNull(content);
        assertNotNull(content.getFirstName());
        assertNotNull(content.getLastName());
        assertNotNull(content.getAddress());
        assertNotNull(content.getGender());
        assertTrue(content.isEnabled());

        assertTrue(content.getId() > 0);

        assertEquals("Nelson", content.getFirstName());
        assertEquals("Piquet Maior", content.getLastName());
        assertEquals("New York City, New York, US", content.getAddress());
        assertEquals("Male", content.getGender());
    }

    @Test
    @Order(3)
    void testDisableById() {
        var content = given()
                .config(
                        RestAssuredConfig
                                .config()
                                .encoderConfig(EncoderConfig.encoderConfig()
                                        .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                .spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_YML)
                .accept(TestConfigs.CONTENT_TYPE_YML)
                .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_NARIGAZ)
                .pathParam("id", person.getId())
                .when()
                .patch("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(PersonVO.class, objectMapper);

        assertNotNull(content);
        assertNotNull(content.getFirstName());
        assertNotNull(content.getLastName());
        assertNotNull(content.getAddress());
        assertNotNull(content.getGender());
        assertFalse(content.isEnabled());

        assertTrue(content.getId() > 0);

        assertEquals("Nelson", content.getFirstName());
        assertEquals("Piquet Maior", content.getLastName());
        assertEquals("New York City, New York, US", content.getAddress());
        assertEquals("Male", content.getGender());
    }

    @Test
    @Order(4)
    void testFindiById() {
        mockPerson();

        var content = given()
                .config(
                        RestAssuredConfig
                                .config()
                                .encoderConfig(EncoderConfig.encoderConfig()
                                        .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                .spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_YML)
                .accept(TestConfigs.CONTENT_TYPE_YML)
                .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_NARIGAZ)
                .pathParam("id", person.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(PersonVO.class, objectMapper);

        assertNotNull(content);
        assertNotNull(content.getFirstName());
        assertNotNull(content.getLastName());
        assertNotNull(content.getAddress());
        assertNotNull(content.getGender());
        assertFalse(content.isEnabled());

        assertTrue(content.getId() > 0);

        assertEquals("Nelson", content.getFirstName());
        assertEquals("Piquet Maior", content.getLastName());
        assertEquals("New York City, New York, US", content.getAddress());
        assertEquals("Male", content.getGender());
    }

    @Test
    @Order(5)
    void testDelete() {
        given().spec(specification)
                .pathParam("id", person.getId())
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);
    }

    @Test
    @Order(6)
    void testFindAll() {
        var content = given()
                .config(
                        RestAssuredConfig
                                .config()
                                .encoderConfig(EncoderConfig.encoderConfig()
                                        .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                .spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_YML)
                .accept(TestConfigs.CONTENT_TYPE_YML)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(PersonVO[].class, objectMapper);

        List<PersonVO> people = Arrays.stream(content).toList();

        PersonVO foundPersonOne = people.get(0);

        assertNotNull(foundPersonOne);
        assertNotNull(foundPersonOne.getFirstName());
        assertNotNull(foundPersonOne.getLastName());
        assertNotNull(foundPersonOne.getAddress());
        assertNotNull(foundPersonOne.getGender());
        assertTrue(foundPersonOne.isEnabled());

        assertEquals(1, foundPersonOne.getId());

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
        assertTrue(foundPersonFour.isEnabled());

        assertEquals(5, foundPersonFour.getId());

        assertEquals("Nelson", foundPersonFour.getFirstName());
        assertEquals("Mvezo", foundPersonFour.getLastName());
        assertEquals("Mvezo - South Africa", foundPersonFour.getAddress());
        assertEquals("Male", foundPersonFour.getGender());
    }

    @Test
    @Order(7)
    void testFindAllWithoutToken() {
        var specification = new RequestSpecBuilder()
                .setBasePath("/api/v1/person")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_YML)
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
        person.setEnabled(true);
    }
}
