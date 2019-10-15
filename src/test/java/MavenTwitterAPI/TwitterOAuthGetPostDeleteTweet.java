package MavenTwitterAPI;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class TwitterOAuthGetPostDeleteTweet {
	
Properties prop = new Properties();
	
	@BeforeTest
	public void getData() throws IOException
	{
		FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+"//env.properties");  //Global declaration of path instead of hard coding 
		prop.load(fis);
	}
	
	private static Logger log = LogManager.getLogger(TwitterOAuthGetPostDeleteTweet.class.getName());  //log is object, LogManager is an API and getLogger is function inside it
	
	String id;
	
	//GET LATEST TWEET
	@Test(priority = 0)
	public void getLatestTweet()
	{
		log.info("Host Information   "+prop.getProperty("HOST"));
		RestAssured.baseURI=prop.getProperty("HOST");  // FROM env.properties
		Response res = given().auth().oauth(prop.getProperty("ConsumerKey"), prop.getProperty("ConsumerSecret"), prop.getProperty("AccessToken"), prop.getProperty("TokenSecret"))
		.queryParam("count","1")   //GET LATEST TWEET
		.when().get(prop.getProperty("getResource"))
		.then().extract().response();
		
		String response = res.asString();
		//log.info(response);
		JsonPath js  = new JsonPath(response);
		String str1 = js.get("text").toString();
		id = js.get("id_str").toString();
		log.info("LATEST TWEET: " +str1);
		log.info("LATEST TWEET ID: " +id);
		//System.out.println("LATEST TWEET ID: " +id);
	}
	
	//POST TWEET
	@Test(priority = 1)
	public void createTweet()
	{
		log.info("Host Information   "+prop.getProperty("HOST"));
		RestAssured.baseURI=prop.getProperty("HOST");  // FROM env.properties
		Response res = given().auth().oauth(prop.getProperty("ConsumerKey"), prop.getProperty("ConsumerSecret"), prop.getProperty("AccessToken"), prop.getProperty("TokenSecret"))
		.queryParam("status",prop.getProperty("tweetText"))   //Tweet text FROM env.properties
		.when().post(prop.getProperty("postResource"))
		.then().extract().response();
		
		String response = res.asString();
		//log.info(response);
		JsonPath js  = new JsonPath(response);
		String str2 = js.get("text").toString();
		String id1 = js.get("id").toString();
		log.info("AUTOMATED TWEET: " +str2);
		log.info("AUTOMATED TWEET ID: " +id1);
	}
	
	//DELETE TWEET
	@Test(priority = 2)
	public void deleteTweet()
	{
		log.info("Host Information   "+prop.getProperty("HOST"));
		RestAssured.baseURI=prop.getProperty("HOST");  // FROM env.properties 
		String removeID = id.substring(1, id.length()-1);
		//System.out.println("DELETE REQUEST: "+prop.getProperty("HOST")+"/destroy/"+removeID+".json");
		Response res = given().auth().oauth(prop.getProperty("ConsumerKey"), prop.getProperty("ConsumerSecret"), prop.getProperty("AccessToken"), prop.getProperty("TokenSecret"))
		.when().post("/destroy/"+removeID+".json")
		.then().extract().response();
		System.out.println("ID OF LAST TWEET:" +id);
		
		String response = res.asString();
		//log.info(response);
		JsonPath js  = new JsonPath(response);
		String str1 = js.get("text").toString();
		log.info("DELETED TWEET TEXT: " +str1);
		log.info("DELETED TWEET ID: " +id);
	}
}

