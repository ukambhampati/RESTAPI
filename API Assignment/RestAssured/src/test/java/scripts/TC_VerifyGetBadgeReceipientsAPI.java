package scripts;


import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import constants.APIConstants;
import functions.InitializeAndDeInitialize;
import functions.Log;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class TC_VerifyGetBadgeReceipientsAPI extends InitializeAndDeInitialize {
	
	@Test
	public void verifyBadgeRecipientAPI() {
		
		/** Calling /badges/recipients API to get the user_info and badge_info and print displayname,badgeid and rank details*/
		Log.info("Started validating /badges/recipients API");
		RestAssured.baseURI = APIConstants.BASEURI_RECEPIENTS;	
		RequestSpecification request = RestAssured.given();	
		Response response = request.header(APIConstants.PARAM_ACCESS_TOKEN,configDetails.access_token).header(APIConstants.PARAM_CONTENTTYPE, ContentType.JSON).
							queryParam(APIConstants.PARAM_PAGE, 2).
							queryParam(APIConstants.PARAM_PAGESIZE, 25).
							queryParam(APIConstants.PARAM_FROMDATE, "1645142400").
							queryParam(APIConstants.PARAM_TODATE, "1645574400").
							queryParam(APIConstants.PARAM_SITE, "stackoverflow").
							get();
		
		Integer responseCode = response.getStatusCode();
		JsonPath path = response.jsonPath();

		Assert.assertEquals(responseCode, APIConstants.SUCCESS_RESPONSE_CODE,"GET BADGE ID API CALL IS NOT SUCCESSFULL");

		List<String> badges = path.getList("items.badge_id");
		Map user_info= Collections.emptyMap();
		Map badge_info = Collections.emptyMap();
		for(int i=0;i<badges.size();i++) {	
			badge_info = path.getMap("items["+i+"]");
			user_info = path.getMap("items["+i+"].user");
			Assert.assertEquals(user_info.isEmpty(),false,"USER DETAILS ARE NOT AVAILABLE IN JSON RESPONSE");
			Log.info("Validated successfully that "+badge_info.get("badge_id").toString()+" is having User info");
			String displayname = user_info.get("display_name").toString();			
			Log.info("RECIPIENT : "+displayname+" RECEIVED BADGE ID :"+badge_info.get("badge_id").toString()+" WITH RANK :"+badge_info.get("rank"));
		}
		Log.info("Completed validating /badges/recipients API call");
		Log.info("############################################################################################");
		
		Log.info("Started validating invalid inputs for /badges/recipients API");
		RequestSpecification badrequestPagesize = RestAssured.given();
		Response badresponse = badrequestPagesize.header(APIConstants.PARAM_ACCESS_TOKEN,configDetails.access_token).header(APIConstants.PARAM_CONTENTTYPE,ContentType.JSON).							
							queryParam(APIConstants.PARAM_PAGESIZE, "101").
							queryParam(APIConstants.PARAM_SITE, "stackoverflow").
							get();
		Assert.assertEquals(badresponse.getStatusCode(), 400,"STATUS CODE IS AS EXPECTED");
		JsonPath badresBody = badresponse.jsonPath();
		Assert.assertEquals(badresBody.get("error_message"),"pagesize","ERROR MESSAGE IS NOT AS EXPECTED ");
		Log.info("Validated successfully that reponse contains error_message :"+badresBody.get("error_message"));
		Log.info("#################################################################################################");

		Log.info("Started validating invalid date format as input for fromdate and todate");
		LocalDate todaysDate = LocalDate.now();
		RequestSpecification badrequestdates = RestAssured.given();
		Response badresponsedates = badrequestdates.header(APIConstants.PARAM_ACCESS_TOKEN,configDetails.access_token).header(APIConstants.PARAM_CONTENTTYPE,ContentType.JSON).														
							queryParam(APIConstants.PARAM_FROMDATE, todaysDate).
							queryParam(APIConstants.PARAM_TODATE, todaysDate).							
							queryParam(APIConstants.PARAM_SITE, "stackoverflow").
							get();
		responseCode = badresponsedates.getStatusCode();
		Assert.assertEquals(responseCode, APIConstants.ERROR_RESPONSE_CODE,"STATUS CODE IS AS EXPECTED");
		badresBody = badresponsedates.jsonPath();
		Assert.assertEquals(badresBody.get("error_message"),"fromdate","ERROR MESSAGE IS NOT AS EXPECTED ");
		Log.info("Validated successfully that reponse contains error_message :"+badresBody.get("error_message"));
		Log.info("#################################################################################################");		
		
		Log.info("Started Validating /badges/recipents API with border inputs of both fromdate and todate as today's date");
		System.out.println(todaysDate.toEpochDay());
		RequestSpecification todayRequest = RestAssured.given();	
		Response todayresponse = todayRequest.header(APIConstants.PARAM_ACCESS_TOKEN,configDetails.access_token).header(APIConstants.PARAM_CONTENTTYPE,ContentType.JSON).
							queryParam(APIConstants.PARAM_FROMDATE, todaysDate.toEpochDay()).
							queryParam(APIConstants.PARAM_TODATE, todaysDate.toEpochDay()).
							queryParam(APIConstants.PARAM_SITE, "stackoverflow").
							get();
		
		responseCode = todayresponse.getStatusCode();		
		Assert.assertEquals(responseCode, APIConstants.SUCCESS_RESPONSE_CODE,"RESPONSE CODE FOR TODAYS RECIPIENTS CALL IS NOT AS EXPECTED");
		Log.info("Validated successfully that reponse Code is :"+responseCode);
		Log.info("#################################################################################################");
	}

}
