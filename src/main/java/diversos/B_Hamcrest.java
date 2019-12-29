package diversos;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class B_Hamcrest {

	@Test
	public void testeHamcrest() {

		assertThat("Maria", is("Maria"));
		assertThat("Maria", anyOf(is("Maria"), is("Mariana"))); // Ou...
		
		assertThat("Adalbertino", 
						allOf(startsWith("Ada"), 
						containsString("lbert"), 
						endsWith("ino")));
		
		assertThat(128d, isA(Double.class));
		assertThat(100, lessThan(101));

		List<Integer> impares = Arrays.asList(1, 3, 5, 7, 9);
		assertThat(impares, contains(1, 3, 5, 7, 9)); // CONTAINS PRECISA SER NA ORDEM EM QUE RECEBE!
		assertThat(impares, hasSize(5));

		assertThat(impares, hasItems(5, 3));
		assertThat(impares, hasItems(5, 3));

	}
	
	@Test
	public void DevoValidarBodyComHamcrest() {

		given()
		.when()
			.get("http://restapi.wcaquino.me:80/ola")
		.then()
			.statusCode(200) // Verificar Status Code antes dos demais 
			.body(is("Ola Mundo!"))
			.body(containsString("Mundo"))
			.body(is(not(nullValue())));
	}

}