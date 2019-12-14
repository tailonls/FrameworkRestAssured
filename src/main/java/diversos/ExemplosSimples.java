package diversos;

import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

public class ExemplosSimples {

	@Test
	public void teste() {
		
		Response response = RestAssured.request(Method.GET, "http://restapi.wcaquino.me:80/ola");
		
		// Validando retorno
		ValidatableResponse validacao = response.then();
		validacao.statusCode(201);
	}

}