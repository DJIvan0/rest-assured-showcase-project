package testCases;

import org.json.JSONObject;
import org.testng.annotations.Test;

import io.restassured.module.jsv.JsonSchemaValidator;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import java.io.File;
import java.util.HashMap;

public class HTTPRequests {  
	
	int id;
	
	@Test
	void getUsers() {
		given()
		.when()
			.get("https://reqres.in/api/users?page=2")
		.then()
			.statusCode(200)
			.body("page",equalTo(2))
			.and()
			.body("data[0].first_name", equalTo("Michael"))
			.log().all();
	}
	
	@Test
	void createUser() {
		HashMap hm=new HashMap();
		hm.put("name", "ivan");
		hm.put("job","tester");
		
		id=given()
			.contentType("application/json")
			.body(hm)
		.when()
			.post("https://reqres.in/api/users")
			.jsonPath().getInt("id");
		//.then()
			//.statusCode(201)
			//.log().all();
	}
	
	@Test(dependsOnMethods={"createUser"})
	void updateUser() {
		JSONObject data=new JSONObject();
		data.put("name", "ivam");
		data.put("job","texter");
		
		given()
			.contentType("application/json")
			.body(data)
		.when()
			.put("https://reqres.in/api/users/"+id)
		.then()
			.statusCode(200)
			.log().all();
		
	}
	
	@Test
	void jsonSchemaValidation() {
		
		File jsonFile=new File(System.getProperty("user.dir")+"\\src\\test\\java\\resources\\jsonSchemaValidator.json");
		given()
		.when()
			.get("http://localhost:3000/store")
		.then()
			.statusCode(200)
			.assertThat().body(JsonSchemaValidator.matchesJsonSchema(jsonFile));
	}

}
