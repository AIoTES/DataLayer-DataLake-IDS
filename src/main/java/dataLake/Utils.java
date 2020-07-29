/**
 * Copyright 2020 Universitat Politècnica de València
 *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
