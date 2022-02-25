package scripts;


import java.util.Collections;
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

public class TC_VerifyGetBadgeIDAPI extends InitializeAndDeInitialize {
	
	@Test
	public void verifyBadgeIDAPI() {
		
		/** Calling "/badges" API to get list of 100 badges that are 
		 * ranked GOLD and sorted by rank in desc*/
		Log.startTestCase("TC_VerifyGetBadgeIDAPI");
		Log.info("Started calling /badges api to get list of 100 badges that are ranked GOLD ");
		RestAssured.baseURI = APIConstants.BASEURI_BADGEIDS;	
		RequestSpecification request = RestAssured.given();	
		Response response = request.header(APIConstants.PARAM_ACCESS_TOKEN,configDetails.access_token).header(APIConstants.PARAM_CONTENTTYPE,ContentType.JSON).
							queryParam(APIConstants.PARAM_PAGE, 2).
							queryParam(APIConstants.PARAM_PAGESIZE, 100).
							queryParam(APIConstants.PARAM_ORDER, "desc").
							queryParam(APIConstants.PARAM_MAX, "gold").
							queryParam(APIConstants.PARAM_SORT, "rank").
							queryParam(APIConstants.PARAM_SITE, "stackoverflow").
							queryParam("key",configDetails.key).
							get();
		
		/** Getting response Code from Response*/
		Integer responseCode = response.getStatusCode();
		JsonPath path = response.jsonPath();
		
		/** If response code is NOT successful getting the error_message from response body ,
		 * so that user will know the reason for failure*/	
		System.out.println(path.prettyPrint());
		Assert.assertEquals(responseCode, APIConstants.SUCCESS_RESPONSE_CODE,"GET BADGE ID API CALL IS NOT SUCCESSFULL");
		Log.info("Validated that Response code for GET Badges API call is successful :"+responseCode);
		
		// Declared this variable to form a input String with badge-id's for /badge{ids} api call
		String badgerange = "";
		
		// Taking all the badge-id's from response json as List
		List<String> badges = path.getList("items.badge_id");
		
		/**storing all the individual badge details as map and reading rank 
		field value of each badge to validate that it is equal to GOLD*/
		Map badge_ids= Collections.emptyMap();
		for(int i=0;i<badges.size();i++) {		
			badge_ids = path.getMap("items["+i+"]");
			Assert.assertEquals(badge_ids.get("rank"), "gold");
			Log.info("Validated successfully that GET BADGE ID API CALL RETUNED GOLD RANK BADGE ID :"+ badge_ids.get("badge_id").toString());
			badgerange = badgerange+";"+badge_ids.get("badge_id").toString();
		}
		Assert.assertEquals(badges.size(), 100,"RESPONSE PAGE SIZE IS NOT AS EXPECTED");
		Log.info("Completed validating JSON response of GET Badges API");
		Log.info("################################################################################################");
		
		
		Log.info("Started validated invalid input for badges API call");		
		/** Invoke badges API call with invalid input max:gold and sort as type instead of rank*/
		RequestSpecification badrequestmax = RestAssured.given();
		Response badresponse = badrequestmax.header(APIConstants.PARAM_ACCESS_TOKEN,configDetails.access_token).header(APIConstants.PARAM_CONTENTTYPE,ContentType.JSON).
				
							queryParam(APIConstants.PARAM_ORDER, "desc").
							queryParam(APIConstants.PARAM_MAX, "gold").
							queryParam(APIConstants.PARAM_SORT, "type").
							queryParam(APIConstants.PARAM_SITE, "stackoverflow").
							queryParam(APIConstants.PARAM_KEY,configDetails.key).		
							get();
		responseCode = badresponse.getStatusCode();
		Assert.assertEquals(responseCode, APIConstants.ERROR_RESPONSE_CODE,"STATUS CODE IS NOT AS EXPECTED");
		JsonPath badresBody = badresponse.jsonPath();
		Assert.assertEquals(badresBody.get("error_message"),"max","ERROR MESSAGE IS NOT AS EXPECTED ");
		Log.info("Completed validating error response code for invalid input MAX: GOLD and SORT: TYPE: "+responseCode);
		Log.info("################################################################################################");
		
		
		Log.info("Started validating invalid input in pagesize");
		/** Invoke badges API call with pagesize >100 */
		RequestSpecification badrequestPagesize = RestAssured.given();
		badresponse = badrequestPagesize.header("acces_token",configDetails.access_token).header(APIConstants.PARAM_CONTENTTYPE,ContentType.JSON).							
							queryParam(APIConstants.PARAM_PAGESIZE, "101").
							queryParam(APIConstants.PARAM_ORDER, "desc").
							queryParam(APIConstants.PARAM_SORT, "type").
							queryParam(APIConstants.PARAM_SITE, "stackoverflow").
							queryParam(APIConstants.PARAM_KEY,configDetails.key).
							get();
		responseCode = badresponse.getStatusCode();
		Assert.assertEquals(responseCode, APIConstants.ERROR_RESPONSE_CODE,"STATUS CODE IS NOT AS EXPECTED");
		badresBody = badresponse.jsonPath();
		Assert.assertEquals(badresBody.get("error_message"),"pagesize","ERROR MESSAGE IS NOT AS EXPECTED ");
		Log.info("Completed validating error response code for invalid input PAGESIZE>100 "+responseCode);
		Log.info("################################################################################################");
		
		
		Log.info("Started Validating /badges{ids} API by passing the output range of badge-ids from previous success call");
		badgerange = badgerange.replaceFirst(";", "");
		Log.info("Badge IDS with semicolon delimited"+badgerange);
		
		RestAssured.baseURI=APIConstants.BASEURI_BADGEIDS+"/"+badgerange;
		RequestSpecification idrangeRequest = RestAssured.given();
		Response idsresponse = idrangeRequest.header(APIConstants.PARAM_ACCESS_TOKEN,configDetails.access_token).header(APIConstants.PARAM_CONTENTTYPE,ContentType.JSON).
											queryParam(APIConstants.PARAM_PAGESIZE, 100).
											  queryParam(APIConstants.PARAM_ORDER, "asc").											  
											  queryParam(APIConstants.PARAM_SORT, "type").
											  queryParam(APIConstants.PARAM_SITE, "stackoverflow").
											  queryParam(APIConstants.PARAM_KEY,configDetails.key).
											  get();	
		
		JsonPath idspath = idsresponse.jsonPath();
		responseCode = idsresponse.getStatusCode();
		Assert.assertEquals(responseCode, APIConstants.SUCCESS_RESPONSE_CODE,"GET BADGE ID's API CALL IS NOT SUCCESSFULL");
		
		// Taking all the badge-id's from response json as List
		badges = idspath.getList("items.badge_id");

		// loop through all the badge data to validate that API returned all the badge id's from the input range
		Map range_of_badgeids = Collections.emptyMap();
		for(int i=0;i<badges.size();i++) {		
			range_of_badgeids = idspath.getMap("items["+i+"]");		
			Assert.assertEquals(badgerange.contains(range_of_badgeids.get("badge_id").toString()), true,"BADGE IS FROM RESPONSE IS NOT AS EXPECTED");
			Log.info("Validated successfully that GET BADGE IDS API CALL CONTAINS BADGE ID :"+ range_of_badgeids.get("badge_id").toString());
			
		}		
		Log.info("Validated successfully that /badge{ids} API call s working as expected");
		Log.info("################################################################################################");
		
		
		Log.info("Started checking invalid input of sort: type and max:gold for badge{id} API");
		RestAssured.baseURI="https://api.stackexchange.com/2.3/badges/"+badgerange;
		RequestSpecification idrangeBadRequest = RestAssured.given();
		Response idsresponsebadRequest = idrangeBadRequest.header(APIConstants.PARAM_ACCESS_TOKEN,configDetails.access_token).header(APIConstants.PARAM_CONTENTTYPE,ContentType.JSON).
											 
											  queryParam(APIConstants.PARAM_ORDER, "asc").											  
											  queryParam(APIConstants.PARAM_SORT, "type").
											  queryParam(APIConstants.PARAM_SITE, "stackoverflow").
											  queryParam(APIConstants.PARAM_MAX, "gold").
											  queryParam(APIConstants.PARAM_KEY,configDetails.key).
											  get();
		responseCode = idsresponsebadRequest.getStatusCode();
		Assert.assertEquals(responseCode, APIConstants.ERROR_RESPONSE_CODE,"GET BADGE ID's API CALL IS NOT SUCCESSFULL");
		Log.info("Validated successfully that response code is "+idsresponsebadRequest.getStatusCode()+"when input is invalid");
		Log.info("################################################################################################");
		Log.endTestCase("TC_VerifyGetBadgeIDAPI");
	}

}
