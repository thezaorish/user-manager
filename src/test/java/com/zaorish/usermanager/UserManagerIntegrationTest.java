package com.zaorish.usermanager;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.zaorish.usermanager.model.User;
import com.zaorish.usermanager.repository.UserDao;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.hasSize;
import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = UserManagerApplication.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class UserManagerIntegrationTest {

	@Value("${local.server.port}")
	int port;

	@Autowired
	private UserDao userDao;

	@Before
	public void setUp() {
		RestAssured.port = port;
		if (!userDao.exists("thezaorish")) {
			userDao.create(new User("thezaorish", "password", "thezaorish@email.com").withFirstname("Victor").withLastname("Balanica").from("RO"));
		}
	}

	private static String userCreated;
	private static String userChangedNickname;

	@RabbitListener(queues = "#{userCreatedQueue.name}")
	public void userCreatedReceiver(String in) {
		userCreated = in;
	}
	@RabbitListener(queues = "#{userUpdatedNicknameQueue.name}")
	public void userUpdatedNicknameReceiver(String in) {
		userChangedNickname = in;
	}

	@AfterClass
	public static void afterClass() {
		assertThat(userCreated, is("player"));
		assertThat(userChangedNickname, is("new_nick"));
	}

	@Test
	public void shouldNotAllowCreationWithExistingNickname() {
		Response response = given()
				.contentType("application/json").
						body("{\n" +
								" \"nickname\" : \"thezaorish\",\n" +
								" \"password\" : \"some\",\n" +
								" \"email\" : \"thezaorish@email.com\"\n" +
								" }").
						when().
						post("/users");

		response.then().
				statusCode(400).
				body("errors.property", hasSize(1)).
				body("errors.message", hasSize(1)).
				body("errors[0].property", is("nickname")).
				body("errors[0].message", notNullValue());
	}

	@Test
	public void shouldVerifySuccessfulCreation() {
		Response response = given()
				.contentType("application/json").
						body("{\n" +
								" \"nickname\" : \"player\",\n" +
								" \"password\" : \"1234\",\n" +
								" \"email\" : \"player@domain.org\"\n" +
								" }").
						when().
						post("/users");

		response.then().
				statusCode(201).
				header("Location", endsWith("/users/player"));

		assertThat(userDao.exists("player"), is(true));
	}

	@Test
	public void shouldVerifySuccessfulUpdate() {
		Response response = given()
				.contentType("application/json").
						body("{\n" +
								" \"nickname\" : \"new_nick\",\n" +
								" \"password\" : \"1234\",\n" +
								" \"email\" : \"player@domain.org\"\n" +
								" }").
						when().
						put("/users/thezaorish");

		response.then().
				statusCode(200).
				body("nickname", is("new_nick")).
				body("email", is("player@domain.org"));

		assertThat(userDao.exists("new_nick"), is(true));
		assertThat(userDao.exists("thezaorish"), is(false));
	}

	@Test
	public void shouldVerifySuccessfulDelete() {
		Response response = when().
						delete("/users/thezaorish");

		response.then().
				statusCode(204);

		assertThat(userDao.exists("thezaorish"), is(false));
	}

	@Test
	public void shouldVerifySuccessfulGet() {
		Response response = when().
						get("/users/thezaorish");

		response.then().
				statusCode(200).
				assertThat().body(matchesJsonSchemaInClasspath("user-schema.json")).
				body("firstname", is("Victor")).
				body("lastname", is("Balanica")).
				body("nickname", is("thezaorish")).
				body("email", is("thezaorish@email.com")).
				body("country", is("RO"));
	}

	@Test
	public void shouldHandleNotExistingNickname() {
		Response response = when().
				get("/users/random");

		response.then().
				statusCode(404);
	}

	@Test
	public void shouldRetrievePaginatedReferences() {
		for (int i = 1; i <= 31; i++) {
			userDao.create(new User("user_" + i, "password", "email_" + i + "@email.com"));
		}

		Response response = when().
				get("/users");

		response.then().
				statusCode(200).
				body("results", notNullValue()).
				body("page", is(1)).
				body("size", is(20)).
				body("totalPages", is(2)).
				body("results.nickname", hasSize(20)).
				body("results.email", hasSize(20)).
				header("Link", notNullValue());

		userDao.deleteAll();
	}

	@Test
	public void shouldRetrieveFilteredPaginatedReferences() {
		for (int i = 1; i <= 31; i++) {
			User user = new User("user_" + i, "password", "email_" + i + "@email.com");
			if (i % 5 == 0) {
				user.setCountry("UK");
			} else if (i % 7 == 0) {
				user.setCountry("ES");
			}
			userDao.create(user);
		}

		Response response = when().
				get("/users?country=UK");

		response.then().
				statusCode(200).
				body("results", notNullValue()).
				body("page", is(1)).
				body("size", is(6)).
				body("totalPages", is(1)).
				body("results.nickname", hasSize(6)).
				body("results.email", hasSize(6)).
				header("Link", notNullValue());

		userDao.deleteAll();
	}

	@Test
	public void shouldVerifySwaggerConfig() {
		when()
			.get("/v2/api-docs")
		.then().
			statusCode(200).
			body("host", notNullValue());
	}

	@Test
	public void shouldVerifyHealthEndpoint() {
		when()
			.get("/health")
		.then().
			statusCode(200).
			body("status", is("UP"));
	}

	@Test
	public void shouldVerifyDatabaseSizeEndpoint() {
		when()
			.get("/databaseSize")
		.then().
			statusCode(200);
	}

}
