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

public class TC_VerifyGetTagsAPI extends InitializeAndDeInitialize {
	
	@Test
	public void verifyGetTagsAPI() {
		

		//** Call /badges/tags API to get list of tag_based badge details with silver rank
		Log.info("Started validating /badges/tags API to get tag_based badge details with silver rank");
		RestAssured.baseURI = APIConstants.BASEURI_TAGS;	
		RequestSpecification request = RestAssured.given();	
		Response response = request.header(APIConstants.PARAM_ACCESS_TOKEN,configDetails.access_token).header(APIConstants.PARAM_CONTENTTYPE,ContentType.JSON).
							queryParam(APIConstants.PARAM_PAGE, 2).
							queryParam(APIConstants.PARAM_PAGESIZE, 100).
							queryParam(APIConstants.PARAM_ORDER, "desc").
							queryParam(APIConstants.PARAM_MAX, "silver").
							queryParam(APIConstants.PARAM_MIN,"silver").
							queryParam(APIConstants.PARAM_SORT, "rank").
							queryParam(APIConstants.PARAM_SITE, "stackoverflow").
							get();
		
		int responseCode = response.getStatusCode();
		JsonPath path = response.jsonPath();
		if (responseCode!=200) {
			Log.info(path.get("error_message").toString());
		}
		Assert.assertEquals(responseCode, 200,"GET BADGE ID API CALL IS NOT SUCCESSFULL"+response.getStatusLine());		
		List<String> badges = path.getList("items.badge_id");
		Map badge_ids= Collections.emptyMap();
		for(int i=0;i<badges.size();i++) {		
			badge_ids = path.getMap("items["+i+"]");
			Assert.assertEquals(badge_ids.get("rank"), "silver");
			Assert.assertEquals(badge_ids.get("badge_type"),"tag_based");
			Log.info("Validated successfully that badge_type and rank are as expected for badge_id"+badge_ids.get("badge_id").toString());
			Log.info("Validated successfully that GET BADGE TAGS API CALL RETURNED SILVER RANK BADGE ID :"+ badge_ids.get("badge_id").toString()+"AND IS ALSO TAGBASED");			
		}
		Log.info("########################################################################################################");
		
		/** Call /badges/tags API with invalid input in min:purple*/
		Log.info("Started validating /badges/tags API with invalid input in queryparam : min");
		RequestSpecification badrequestmax = RestAssured.given();
		Response badresponse = badrequestmax.header(APIConstants.PARAM_ACCESS_TOKEN,configDetails.access_token).header(APIConstants.PARAM_CONTENTTYPE,ContentType.JSON).				
							queryParam(APIConstants.PARAM_ORDER, "desc").
							queryParam(APIConstants.PARAM_MIN, "purple").
							queryParam(APIConstants.PARAM_SORT, "rank").
							queryParam(APIConstants.PARAM_SITE, "stackoverflow").
							get();
		Assert.assertEquals(badresponse.getStatusCode(), 400,"STATUS CODE IS NOT AS EXPECTED");
		JsonPath badresBody = badresponse.jsonPath();
		Assert.assertEquals(badresBody.get("error_message"),APIConstants.PARAM_MIN,"ERROR MESSAGE IS NOT AS EXPECTED ");
		Log.info("VALIDATED SUCCESSFULLY THAT 400 RESPONSE CODE IS DISPLAYED FOR INVALID RANK");
		Log.info("########################################################################################################");

		/** Call /badges/tags API with pagesize:1 and validate that only one badge details is displayed in response*/
		Log.info("Started validating /badges/tags API with pagesize:1.... ");		
		RequestSpecification requestpagesizeborder = RestAssured.given();
		response = requestpagesizeborder.header(APIConstants.PARAM_ACCESS_TOKEN,configDetails.access_token).header(APIConstants.PARAM_CONTENTTYPE,ContentType.JSON).
							
							queryParam(APIConstants.PARAM_PAGESIZE, "1").
							queryParam(APIConstants.PARAM_ORDER, "asc").
							queryParam(APIConstants.PARAM_SORT, "rank").
							queryParam(APIConstants.PARAM_SITE, "stackoverflow").
							get();
		Assert.assertEquals(response.getStatusCode(), 200,"STATUS CODE IS NOT AS EXPECTED");
		JsonPath body = response.jsonPath();		
		List<String> listOfBadges = body.getList("items");
		Assert.assertEquals(listOfBadges.size(), 1,"RESPONSE JSON PAGE SIZE IS NOT AS EXPECTED");
		Log.info("JSON BODY OF TAGS API WHEN INPUT OF PAGESIZE IS 1 IS AS BELOW");
		Log.info(body.prettyPrint());
		Log.info("########################################################################################################");
		
		/** Call /badges/tags PAI with invalid input in page parameter */
		Log.info("Started validating /badges/tags API with invalid input in query param page");
		RequestSpecification badrequestPagesize = RestAssured.given();
		badresponse = badrequestPagesize.header(APIConstants.PARAM_ACCESS_TOKEN,configDetails.access_token).header(APIConstants.PARAM_CONTENTTYPE,ContentType.JSON).
							queryParam(APIConstants.PARAM_PAGE,"26").
							queryParam(APIConstants.PARAM_ORDER, "asc").
							queryParam(APIConstants.PARAM_SORT, "rank").
							queryParam(APIConstants.PARAM_SITE, "stackoverflow").
							get();
		badresBody = badresponse.jsonPath();
		Assert.assertEquals(badresponse.getStatusCode(), 400,"STATUS CODE IS NOT AS EXPECTED");
		Assert.assertEquals(badresBody.get("error_id").toString(), "403","ERROR ID NOT AS EXPECTED");
		Log.info("VALIDATED SUCCESSFULLY THAT 403 ERROR ID IS DISPLAYED FOR PAGELIMIT GREATER THAN 25");
		Log.info("########################################################################################################");
		
	}

}
