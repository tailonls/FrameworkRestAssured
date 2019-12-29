package serializacao;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class Serializacao {
	
	@BeforeClass
	public static void setup() {
		RestAssured.baseURI = "http://restapi.wcaquino.me";
	}

	@Test
	public void deveCriarNovoUsuarioComMap() {
		
		Map<String, Object> paramentros = new HashMap<String, Object>();
		paramentros.put("name", "Carlos Usuario");
		paramentros.put("age", 30);

		given()
			.log().all()
			.contentType(ContentType.JSON)
			.body(paramentros)
		.when()
			.post("/users")
		.then()
			.log().all()
			.statusCode(201)
			.body("id", is(notNullValue()))
			.body("name", is("Carlos Usuario"))
			.body("age", is(30));
	}
	
	@Test
	public void deveCriarNovoUsuarioComObjeto() {
		
		User user = new User("Carlos", "30", "1200");

		given()
			.log().all()
			.contentType(ContentType.JSON)
			.body(user)
		.when()
			.post("/users")
		.then()
			.log().all()
			.statusCode(201)
			.body("id", is(notNullValue()))
			.body("name", is("Carlos"))
			.body("age", is("30"))
			.body("salary", is("1200"));
	}
	
	
	@Test
	public void deveDeserializarAoCriarNovoUsuario() {
		
		User user = new User("Carlos", "30", "1200");

		User usuarioDeserializado = 
		given()
			.log().all()
			.contentType(ContentType.JSON)
			.body(user)
		.when()
			.post("/users")
		.then()
			.log().all()
			.statusCode(201)
			.extract().as(User.class);
		
		System.out.println(usuarioDeserializado);
		
		Assert.assertThat(usuarioDeserializado.getId(), is(notNullValue()));
		Assert.assertEquals("Carlos", usuarioDeserializado.getName());
		Assert.assertThat(usuarioDeserializado.getAge(), is("30"));
	}
	
	@Test
	public void deveSerializarParaXMLAoCriarNovoUsuario() {
		
		User user = new User("Denilson", "20", "200");

		given()
			.log().all()
			.contentType(ContentType.XML)
			.body(user)
		.when()
			.post("/usersXML")
		.then()
			.log().all()
			.statusCode(201);
	}
	
	@Test
	public void deveDeserializarXMLAoCriarNovoUsuario() {
		
		User user = new User("Dennis", "10", "1100");

		User usuarioDeserializado = 
		given()
			.log().all()
			.contentType(ContentType.XML)
			.body(user)
		.when()
			.post("/usersXML")
		.then()
			.log().all()
			.statusCode(201)
			.extract().as(User.class);
		
		System.out.println(usuarioDeserializado);
		
		Assert.assertThat(usuarioDeserializado.getId(), is(notNullValue()));
		Assert.assertEquals("Dennis", usuarioDeserializado.getName());
		Assert.assertThat(usuarioDeserializado.getAge(), is("10"));
	}
}