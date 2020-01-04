package autenticacoes;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import io.restassured.http.ContentType;

public class AuthTestsToken {

	
	// JSON Web Tokens
	@Test
	public void autenticacaoTokenJWT() {

		Map<String, Object> paramentros = new HashMap<String, Object>();
		paramentros.put("email", "tailonlimas@gmail.com");
		paramentros.put("senha", "teste123");

		// Logar na api e receber token
		String token = 
		given()
			.log().all()
			.body(paramentros)
			.contentType(ContentType.JSON)
		.when()
			.post("http://barrigarest.wcaquino.me/signin")
		.then()
			.log().all()
			.statusCode(200)
			.body("token", is(notNullValue()))
			.extract().path("token")
		;

		// Obter as contas
		given()
			.log().all()
			.header("Authorization", "JWT " + token)
		.when()
			.get("http://barrigarest.wcaquino.me/contas")
		.then()	
			.log().all()
			.statusCode(200)
			.body("nome", hasItem("Conta teste tailon")) // Conta criada anteriormente no site
		;
		
	}
}