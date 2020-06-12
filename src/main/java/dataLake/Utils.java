package dataLake;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Utils {
	
	public static String getObservation(String messageBodyRequest) {
		JsonParser parser = new JsonParser();
		JsonElement jsonElement = parser.parse(messageBodyRequest);		
		JsonObject rootObject = jsonElement.getAsJsonObject();
		JsonObject childObject = rootObject.getAsJsonObject("data"); 
		String observation = childObject.get("observation").toString(); 	
		return observation;
	}	
	
	public static String getData(String messageBodyRequest) {
		JsonParser parser = new JsonParser();
		JsonElement jsonElement = parser.parse(messageBodyRequest);	
		JsonObject rootObject = jsonElement.getAsJsonObject();
		String data = rootObject.get("data").toString(); 
		return data;
	}	
}
