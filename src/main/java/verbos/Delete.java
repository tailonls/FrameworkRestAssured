package verbos;

import static io.restassured.RestAssured.given;

import static org.hamcrest.Matchers.*;
import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.RestAssured;

public class Delete {
	
	@BeforeClass
	public static void setup() {
		RestAssured.baseURI = "http://restapi.wcaquino.me";
	}

	@Test
	public void deveApagarUsuario() {

		given()
			.log().all()
		.when()
			.delete("/users/2")
		.then()
			.log().all()
			.statusCode(204)			
		;
	}
	
	@Test
	public void deveApagarUsuarioInexistente() {

		given()
			.log().all()
		.when()
			.delete("/users/2000")
		.then()
			.log().all()
			.statusCode(400)
			.body("error", is("Registro inexistente"))
		;
	}
}