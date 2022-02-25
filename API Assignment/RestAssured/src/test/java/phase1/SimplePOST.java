package phase1;



import org.json.simple.JSONObject;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class SimplePOST {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		RestAssured.baseURI = "https://demoqa.com/BookStore/v1/Books";
		RequestSpecification request = RestAssured.given();
		JSONObject queryparams = new JSONObject();
		
		queryparams.put("userId", "TQ123");
		queryparams.put("isbn", "9781449325862");
		
		Response response= request.header("Content-Type","application/json").body(queryparams.toJSONString()).post("/BookStoreV1BooksPost");
		System.out.println(response.getStatusCode());
		
		JsonPath json = response.jsonPath();
		System.out.println("JSON:"+json.prettyPrint());
//		RestAssured.baseURI = "https://abc.com/account";
//		RequestSpecification request1 = RestAssured.given();
//		JSONObject queryparams1 = new JSONObject();
//		queryparams1.put("user", "Usha");
//		queryparams1.put("city", "Hyd");
//		Response response1 = request1.header("Content-Type","application/json").body(queryparams.toJSONString()).post();
//		response1.getStatusCode();
//		response1.getStatusLine();
//		//JsonPath path = response1.body();
//		JsonPath path = response1.jsonPath();
//		Sytepath.get("user");
//		path.getMap("user");
	}

}
