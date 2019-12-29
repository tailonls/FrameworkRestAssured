package diversos;

import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.*;

import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class C_ValidandoComJSON {
	
	@BeforeClass
	public static void setup(){
		RestAssured.baseURI = "http://restapi.wcaquino.me";
		RestAssured.port = 80;
		//RestAssured.basePath = "";
	}
	
	@Test
	public void DevoValidarJsonNoBody() {

		given()
			.log().uri()
		.when()
			.get("/users/1")
		.then()
			.statusCode(200) 
			.body("id", is(1))
			.body("name", containsString("Silva"))
			.body("age", greaterThan(18));
	}
	
	@Test
	public void DevoValidarJsonComPath() {
		
		Response response = get("/users/1");

		// Path
		Assert.assertEquals(new Integer(1), response.path("id"));
		
		// Json path
		JsonPath jpath = new JsonPath(response.asString());
		Assert.assertEquals(1, jpath.getInt("id"));
		
	}
	
	@Test
	public void DevoValidarJsonSegundoNivel() {

		given()
			.log().uri()
		.when()
			.get("/users/2")
		.then()
			.statusCode(200)
			.body("name", containsString("Joaquina"))
			.body("endereco.rua", is("Rua dos bobos"));
		
	}
	
	@Test
	public void DevoValidarJsonComLista() {

		given()
			.log().uri()
		.when()
			.get("/users/3")
		.then()
			.statusCode(200)
			.body("name", containsString("Ana"))
			.body("filhos", hasSize(2))
			.body("filhos[0].name", is("Zezinho"))
			.body("filhos[1].name", is("Luizinho"))
			.body("filhos.name", hasItems("Zezinho", "Luizinho"));
		
	}
	
	@Test
	public void DevoValidarMensagemErro() {

		given()
			.log().uri()
		.when()
			.get("/users/4")
		.then()
			.statusCode(404)
			.body("error", is("Usuário inexistente"));
		
	}
	
	@SuppressWarnings("unchecked") // nada importante... :p
	@Test
	public void DevoValidarListaNaRaiz() {

		given()
			.log().uri()
		.when()
			.get("/users")
		.then()
			.statusCode(200)
			.body("$", hasSize(3))
			.body("name", hasItems("João da Silva", "Maria Joaquina", "Ana Júlia"))
			.body("age[1]", is(25))
			.body("filhos.name", hasItems(Arrays.asList("Zezinho", "Luizinho")))
			.body("salary", contains(1234.5678f, 2500, null));
	}
	
	// Json Path eh baseado em Groovy
	@Test
	public void ValidacoesAvancadas() {

		given()
			.log().uri()
		.when()
			.get("/users")
		.then()
			.statusCode(200)
			.body("$", hasSize(3))
			.body("age.findAll{it <= 25}.size()", is(2))
			.body("age.findAll{it <= 25 && it > 20}.size()", is(1))
			.body("findAll{it.age <= 25 && it.age > 20}.name", hasItem("Maria Joaquina"))
			.body("findAll{it.age <= 25}[0].name", is("Maria Joaquina")) // Pegando primeiro item do retorno
			.body("findAll{it.age <= 25}[-1].name", is("Ana Júlia")) // Pegando ultimo item do retorno
			.body("find{it.age <= 25}.name", is("Maria Joaquina")) // 'Find' encontra somente o unico item  
			.body("findAll{it.name.contains('n')}.name", hasItems("Maria Joaquina", "Ana Júlia")) // Validando quantos itens tem nome com a letra 'n'
			.body("findAll{it.name.length() > 10}.name", hasItems("João da Silva", "Maria Joaquina"))
			.body("id.max()", is(3))
			.body("salary.min()", is(1234.5678f))
			.body("salary.findAll{it != null}.sum()", is(closeTo(3734.5678f, 0.001)))
			;
	}
	
	@Test
	public void ValidandoComLinguagemJava() {
		
		ArrayList<String> lista = 
				
		given()
			.log().uri()
		.when()
			.get("/users")
		.then()
			.statusCode(200)
			.extract().path("name.findAll{it.startsWith('Maria')}");
		
		Assert.assertEquals(1, lista.size());
		Assert.assertEquals("Maria Joaquina".toUpperCase(), lista.get(0).toUpperCase());
		
	}
}