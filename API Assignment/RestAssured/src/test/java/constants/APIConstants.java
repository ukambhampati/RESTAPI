package constants;

public class APIConstants {
	
	public static String BASEURI_BADGEIDS = "https://api.stackexchange.com/2.3/badges";
	public static String BASEURI_RECEPIENTS = "https://api.stackexchange.com/2.3/badges/recipients";
	public static String BASEURI_TAGS = "https://api.stackexchange.com/2.3/badges/tags";
	
	public static String PARAM_CLIENT_ID = "client_id";
	public static String PARAM_CLIENT_SECRET = "client_secret";
	public static String PARAM_REDIRECT_URI = "redirect_uri";
	public static String PARAM_ACCESS_TOKEN = "access_token";
	public static String PARAM_CODE = "Code";
	public static String PARAM_CONTENTTYPE = "content_type";
	
	public static String PARAM_PAGE = "page";
	public static String PARAM_PAGESIZE = "pagesize";
	public static String PARAM_ORDER = "order";
	public static String PARAM_MAX = "max";
	public static String PARAM_MIN = "min";	
	public static String PARAM_SORT = "sort";	
	public static String PARAM_FROMDATE = "fromdate";
	public static String PARAM_TODATE = "todate";
	public static String PARAM_SITE = "site";
	public static String PARAM_KEY = "key";
	
	public static Integer SUCCESS_RESPONSE_CODE = 200;
	public static Integer ERROR_RESPONSE_CODE = 400;
	public static Integer ERROR_ID = 403;
	
	public static String RANK_GOLD = "gold";
	public static final String PATH_FOR_LOGFILE= System.getProperty("user.dir")+"\\logs\\propertieslogs.log";
	public static final String PATH_FOR_CONFIGFILE = System.getProperty("user.dir")+"\\src\\test\\resources\\properties\\Config.properties";
}
