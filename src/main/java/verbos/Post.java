package verbos;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class Post {
	
	@BeforeClass
	public static void setup() {
		RestAssured.baseURI = "http://restapi.wcaquino.me";
	}

	@Test
	public void deveCriarNovoUsuario() {

		given()
			.log().all()
			.contentType(ContentType.JSON)
			.body("{ \"name\": \"Dalton\", \"age\": 30, \"salary\": 4000.0 }")
		.when()
			.post("/users")
		.then()
			.log().all()
			.statusCode(201)
			.body("id", is(notNullValue()))
			.body("name", is("Dalton"))
			.body("age", is(30))
		;
	}
	
	@Test
	public void naoDeveSalvarSemNome() {

		given()
			.log().all()
			.contentType(ContentType.JSON)
			.body("{ \"age\": 30, \"salary\": 4000.0 }")
		.when()
			.post("/users")
		.then()
			.log().all()
			.statusCode(400)
			.body("id", is(nullValue()))
			.body("error", containsString("Name é um atributo obrigatório"))
		;
	}
	
	@Test
	public void deveCriarNovoUsuarioComXML() {

		given()
			.log().all()
			.contentType(ContentType.XML)
			.body("<user><name>Dalton</name><age>30</age><salary>4.000</salary></user>")
		.when()
			.post("/usersXML")
		.then()
			.log().all()
			.statusCode(201)
			.body("user.@id", is(notNullValue()))
			.body("user.name", is("Dalton"))
			.body("user.age", is("30"))
		;
	}
}