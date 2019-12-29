package diversos;

import org.junit.Assert;
import org.junit.Test;

import static io.restassured.RestAssured.*;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

public class A_ExemplosSimples {

	@Test
	public void assertivasIniciais() {
		
		Response response = request(Method.GET, "http://restapi.wcaquino.me:80/ola");
		
		// Validando retorno com 'Then'
		ValidatableResponse validacao = response.then();
		validacao.statusCode(200);
		
		// Validando retorno com Assert
		Assert.assertEquals("Ola Munsdo!", response.getBody().asString());
		Assert.assertTrue("Status da requisição estava diferente do esperado!", response.getStatusCode() == 200);
		
		
		// Simplificando execução
		get("http://restapi.wcaquino.me:80/ola").then().statusCode(201);
		
		
		// Validando com codo fluente (Gherkin)
		given()
		.when()
			.get("http://restapi.wcaquino.me:80/ola")
		.then()
			.assertThat().statusCode(201); // assertThat() apenas para dar legibilidade
	}

}