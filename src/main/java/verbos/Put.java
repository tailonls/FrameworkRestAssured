package verbos;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class Put {
	
	@BeforeClass
	public static void setup() {
		RestAssured.baseURI = "http://restapi.wcaquino.me";
	}

	@Test
	public void deveAlterarUsuario() {

		given()
			.log().all()
			.contentType(ContentType.JSON)
			.body("{ \"name\": \"Maria Alterada\", \"age\": 31, \"salary\": 1000 }")
		.when()
			.put("/users/2")
		.then()
			.log().all()
			.statusCode(200)
			.body("id", is(2))
			.body("name", is("Maria Alterada"))
			.body("age", is(31))
			.body("salary", is(1000))
		;
	}
	
	@Test
	public void deveAlterarComURLCustomizada() {

		given()
			.log().all()
			.contentType(ContentType.JSON)
			.body("{ \"name\": \"Maria Alterada\", \"age\": 31, \"salary\": 1000 }")
			.pathParam("entidade", "users")
			.pathParam("userId", 2)
		.when()
			.put("/{entidade}/{userId}")
		.then()
			.log().all()
			.statusCode(200)
			.body("id", is(2))
			.body("name", is("Maria Alterada"))
			.body("age", is(31))
			.body("salary", is(1000))
		;
	}
}