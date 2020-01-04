package diversos;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.notNullValue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class E_upload {

	@BeforeClass
	public static void setup() {
		RestAssured.baseURI = "http://restapi.wcaquino.me";
	}

	@Test
	public void deveMostrarErroDeUpload() {

		given()
			.log().all()
			.contentType(ContentType.JSON)
		.when()
			.post("/upload")
		.then()
			.log().all()
			.statusCode(404)
			.body("error", is("Arquivo não enviado"))
		;
	}
	
	@Test
	public void deveFazerUpload() {

		given()
			.log().all()
			.multiPart("arquivo", new File("src/main/resources/arquivo_exemplo.pdf"))
		.when()
			.post("/upload")
		.then()
			.log().all()
			.statusCode(200)
			.body("name", is("arquivo_exemplo.pdf"))
			.body("md5", is(notNullValue()))
			.body("size", is(notNullValue()))
		;
	}
	
	// Esse teste deve falhar por padrão
	@Test
	public void validaTempoMaximoDeEspera() {

		given()
			.log().all()
			.multiPart("arquivo", new File("src/main/resources/arquivo_exemplo.pdf"))
		.when()
			.post("/upload")
		.then()
			.log().all()
			.time(lessThan(1000L))
		;
	}
	
	@Test
	public void deveFazerDownload() throws IOException {

		byte[] imagemExtraida = 
		given()
			.log().all()
		.when()
			.get("/download")
		.then()
			.statusCode(200)
			.extract().asByteArray()
		;
		
		File imagem = new File("src/main/resources/imagem_baixada.jpg");
		OutputStream out = new FileOutputStream(imagem);
		out.write(imagemExtraida);
		out.close();

		System.out.println("\nNome da imagem: " + imagem.getName());
		System.out.println("Tamanho da imagem: " + imagem.length() + "\n");

		Assert.assertThat(imagem.length(), lessThan(100000L));
		Assert.assertThat(imagem.getName(), is("imagem_baixada.jpg"));
	}
}