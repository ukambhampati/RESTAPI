package functions;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import constants.APIConstants;
import contexts.ConfigDetailsContext;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class InitializeAndDeInitialize {
	
	
	
	public static Properties config = new Properties();
	public static ConfigDetailsContext configDetails = new ConfigDetailsContext();
	String directory = System.getProperty("user.dir");
	
	@BeforeTest
	@Parameters("code")
	public void setUp(String paramcode) throws IOException {	
		
		ReadPropertyFile readProp = new ReadPropertyFile();
		config = readProp.initPropertyFile(APIConstants.PATH_FOR_CONFIGFILE,config);	
		if(config.getProperty("access_token").isEmpty()) {
			//skip suite if code is not passed when access token is empty
			Assert.assertEquals(paramcode.isEmpty(),false,"code is mandatory to pass from testplan when access_token is empty");
			String client_id = config.getProperty("client_id");
			String client_secret = config.getProperty("client_secret");
			String redirect_uri = config.getProperty("redirect_uri");
			String code = paramcode;
			String key = config.getProperty("key");
			
			RestAssured.baseURI = config.getProperty("baseURI");
			RequestSpecification request = RestAssured.given();
			Response response =				request.queryParams("client_id", client_id).
											queryParam("client_secret", client_secret).
											queryParam("redirect_uri", redirect_uri).
											queryParam("code", code).queryParam("key",key).post();
			
			int status_code = response.getStatusCode();
			Assert.assertEquals(status_code, 200,"Authorization NOT Granted for the user");
			String body = response.asString();
			String[] bodyarr = body.split("=");
			configDetails.access_token = bodyarr[1];
			FileOutputStream outFile = new FileOutputStream(APIConstants.PATH_FOR_CONFIGFILE);
			config.setProperty("access_token", configDetails.access_token);
			config.put("code", paramcode);
			
			config.store(outFile,null);
			
		} else {
			configDetails.access_token = config.getProperty("access_token");
		}
		configDetails.baseuri = config.getProperty("baseURI");
		configDetails.key = config.getProperty("key");
		// deleting log file
		FileUtils.forceDelete(new File(APIConstants.PATH_FOR_LOGFILE));
	}
}
