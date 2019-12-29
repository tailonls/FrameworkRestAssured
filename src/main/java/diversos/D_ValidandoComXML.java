package diversos;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.internal.path.xml.NodeImpl;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class D_ValidandoComXML {
	
	public static RequestSpecification requestSpec;
	public static ResponseSpecification responseSpec;
	
	@BeforeClass
	public static void setup() {
		
		RestAssured.baseURI = "http://restapi.wcaquino.me";
		RestAssured.port = 80;
		RestAssured.basePath = ""; // Pode ser usado para colocar /v1, /v2 etc... 

		// Informando o que sera mostrado no log nas requisições
		RequestSpecBuilder recBuilder = new RequestSpecBuilder();
		recBuilder.log(LogDetail.URI).and().log(LogDetail.METHOD);
		requestSpec = recBuilder.build();

		// Informando qual o status eposerado por todos os testes
		ResponseSpecBuilder respBuilder = new ResponseSpecBuilder();
		respBuilder.expectStatusCode(200);
		responseSpec = respBuilder.build();

		RestAssured.requestSpecification = requestSpec;
		RestAssured.responseSpecification = responseSpec;
	}
	
	@Test
	public void ValidacoesComXML() {

		given()
		.when()
			.get("/usersXML/3")
		.then()
			.body("user.name", is("Ana Julia"))
			.body("user.@id", is("3"))
			.body("user.filhos.name.size()", is(2))
			.body("user.filhos.name[0]", is("Zezinho"))
			.body("user.filhos.name[1]", is("Luizinho"))
			.body("user.filhos.name", hasItem("Luizinho"))
			.body("user.filhos.name", hasItems("Luizinho", "Zezinho"));
	}
	
	@Test
	public void ValidacoesXMLRoot() {

		given()
		.when()
			.get("/usersXML/3")
		.then()
			.rootPath("user") // Definindo Root do nó
			.body("@id", is("3"))
			.body("filhos.name[0]", is("Zezinho"));
	}
	
	@Test
	public void ValidacoesAvancadasXML() {

		given()
		.when()
			.get("/usersXML")
		.then()
			.body("users.user.size()", is(3))
			.body("users.user.findAll{it.age.toInteger() <= 25}.size()", is(2))
			.body("users.user.@id", hasItems("1", "2", "3"))
			.body("users.user.findAll{it.age ==  25}.name", is("Maria Joaquina"))
			.body("users.user.findAll{it.name.toString().contains('n')}.name", hasItems("Maria Joaquina", "Ana Julia"))
			.body("users.user.age.collect{it.toInteger() * 2}", hasItems(60, 50, 40))
			.body("users.user.name.findAll{it.toString().startsWith('Maria')}.collect{it.toString().toUpperCase()}", is("MARIA JOAQUINA"))
			;
	}

	@Test
	public void ValidacoesAvancadasXMLeJava() {

		ArrayList<NodeImpl> nomes = given()
			
		.when()
			.get("/usersXML")
		.then()
			
			.extract().path("users.user.name.findAll{it.toString().contains('n')}");
		
		Assert.assertEquals(2, nomes.size());
		Assert.assertEquals("Maria Joaquina".toUpperCase(), nomes.get(0).toString().toUpperCase());
		Assert.assertTrue("ANA JULIA".equals(nomes.get(1).toString().toUpperCase()));	
	}
	
	@Test
	public void ValidacoesAvancadasXMLComXPATH() {

		given()
		.when()
			.get("/usersXML")
		.then()
			.body(hasXPath("count(/users/user)", is("3")))
			.body(hasXPath("/users/user[@id='1']"))
			.body(hasXPath("//user[@id='2']"))
			.body(hasXPath("//name[text() = 'Luizinho']/../../name", is("Ana Julia")))
			.body(hasXPath("//name[text() = 'Ana Julia']/following-sibling::filhos", allOf(containsString("Luizinho"), containsString("Zezinho"))))
			.body(hasXPath("/users/user/name", is("João da Silva")))
			.body(hasXPath("/users/user[2]/name", is("Maria Joaquina")))
			.body(hasXPath("/users/user[last()]/name", is("Ana Julia")))
			.body(hasXPath("count(/users/user/name[contains(., 'n')])", is("2")))
			.body(hasXPath("//user[age < 24]/name", is("Ana Julia")))
			.body(hasXPath("//user[age > 20 and age < 30]/name", is("Maria Joaquina")))
			.body(hasXPath("//user[age > 20][age < 30]/name", is("Maria Joaquina")))
			;
	}
}