package phase1;

import java.util.Map;
import groovy.json.JsonOutput;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;

public class SimpleGet {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		RestAssured.baseURI = "http://demoqa.com/utilities/weather/city";
		
		RequestSpecification httpRequest = RestAssured.given();
		Response response = httpRequest.queryParam("paramname","paramvalue").get("/Hyderabad");
		
		System.out.println(response.prettyPrint());
		System.out.println(response.getStatusCode());
		System.out.println(response.getStatusLine());
		ResponseBody body = response.getBody();
		System.out.println(body.asString());
		
		JsonPath json = response.jsonPath();
		System.out.println("City from response is: "+json.get("City"));
		Map<String,String> map = json.getMap("City");
		System.out.println(map.get("City"));
	
	}

}
