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

import static spark.Spark.externalStaticFileLocation;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.delete;
import static spark.Spark.options;

import java.net.ConnectException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class IDSAPI {
	
	private static final Logger LOGGER = LogManager.getLogger(IDSAPI.class);	
	static String localhost;
	static String port;
	static String url;
	
	public static void main(String[] args) {
		
		localhost = System.getenv().get("HOST_INFLUXDB") == null ? "localhost" : System.getenv().get("HOST_INFLUXDB");		
		port = System.getenv().get("PORT_INFLUXDB") == null ? "8086" : System.getenv().get("PORT_INFLUXDB");		
		url = "http://" + localhost + ":" + port;
		
		externalStaticFileLocation("src/main/resources/public");
		get("/swagger",(req, res) -> {res.redirect("index.html"); return null;});
		
		post("/independentStorage/createDB", (req, res) -> {	
			LOGGER.info("createDB query selected");			
			APIServiceImpl apiImpl = new APIServiceImpl(); 
			try {
				apiImpl.createDB(req.body(), url);
			}catch(ConnectException ex) {
				res.status(404);
				return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, "InfluxDB database not found"));
			}catch(IllegalArgumentException ex) {
				res.status(400);
				return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, ex.getMessage()));
			}catch(JsonSyntaxException ex) {
				res.status(400);
				return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, "Malformed JSON element"));
			}catch(Exception ex) {
				res.status(500);
				return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, ex.getMessage()));
			}		
			res.header("Content-Type", "application/json;charset=UTF-8");
			res.header("Access-Control-Allow-Origin", "*");
			res.header("Access-Control-Allow-Methods", "POST");
			res.header("Access-Control-Allow-Headers", "Origin, Content-Type, Authorization, Accept");
			return new Gson().toJson(new StandardResponse("The database was created successfully."));	
		});		
		
		options("/independentStorage/createDB", (request, response) -> {   
    		response.header("Access-Control-Allow-Origin", "*");
    		response.header("Access-Control-Allow-Methods", "POST");
    		response.header("Access-Control-Allow-Headers", "Origin, Content-Type, Authorization, Accept");
    		response.header("Allow", "POST, OPTIONS");
    		response.status(200);
    		return "";
        });
		
		post("/independentStorage/deleteDB", (req, res) -> {	
			LOGGER.info("deleteDB query selected");
			APIServiceImpl apiImpl = new APIServiceImpl(); 
			try {
				apiImpl.deleteDB(req.body(), url);
			}catch(ConnectException ex) {
				res.status(404);
				return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, "InfluxDB database not found"));
			}catch(IllegalArgumentException ex) {
				res.status(400);
				return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, ex.getMessage()));
			}catch(JsonSyntaxException ex) {
				res.status(400);
				return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, "Malformed JSON element"));
			}catch(Exception ex) {
				res.status(500);
				return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, ex.getMessage()));
			}
			res.header("Content-Type", "application/json;charset=UTF-8");
			res.header("Access-Control-Allow-Origin", "*");
			res.header("Access-Control-Allow-Methods", "POST");
			res.header("Access-Control-Allow-Headers", "Origin, Content-Type, Authorization, Accept");
			return new Gson().toJson(new StandardResponse("The database was deleted successfully."));
		});
		
		delete("/independentStorage/deleteDB", (req, res) -> {	
			LOGGER.info("deleteDB query selected");
			APIServiceImpl apiImpl = new APIServiceImpl(); 
			try {
				apiImpl.deleteDB(req.body(), url);
			}catch(ConnectException ex) {
				res.status(404);
				return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, "InfluxDB database not found"));
			}catch(IllegalArgumentException ex) {
				res.status(400);
				return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, ex.getMessage()));
			}catch(JsonSyntaxException ex) {
				res.status(400);
				return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, "Malformed JSON element"));
			}catch(Exception ex) {
				res.status(500);
				return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, ex.getMessage()));
			}
			res.header("Content-Type", "application/json;charset=UTF-8");
			res.header("Access-Control-Allow-Origin", "*");
			res.header("Access-Control-Allow-Methods", "POST");
			res.header("Access-Control-Allow-Headers", "Origin, Content-Type, Authorization, Accept");
			return new Gson().toJson(new StandardResponse("The database was deleted successfully."));
		});
		
		options("/independentStorage/deleteDB", (request, response) -> {   
    		response.header("Access-Control-Allow-Origin", "*");
    		response.header("Access-Control-Allow-Methods", "POST, DELETE");
    		response.header("Access-Control-Allow-Headers", "Origin, Content-Type, Authorization, Accept");
    		response.header("Allow", "POST, DELETE, OPTIONS");
    		response.status(200);
    		return "";
        });
		
		post("/independentStorage/insert", (req, res) -> {	
			LOGGER.info("insert measurement query selected");
			APIServiceImpl apiImpl = new APIServiceImpl(); 
			try {
				apiImpl.insertMeasurement(req.body(), url);	
			}catch(ConnectException ex) {
				res.status(404);
				return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, "InfluxDB database not found"));
			}catch(IllegalArgumentException ex) {
				res.status(400);
				return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, ex.getMessage()));
			}catch(JsonSyntaxException ex) {
				res.status(400);
				return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, "Malformed JSON element"));
			}catch(Exception ex) {
				res.status(500);
				return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, ex.getMessage()));
			}
			res.header("Content-Type", "application/json;charset=UTF-8");
			res.header("Access-Control-Allow-Origin", "*");
			res.header("Access-Control-Allow-Methods", "POST");
			res.header("Access-Control-Allow-Headers", "Origin, Content-Type, Authorization, Accept");
			return new Gson().toJson(new StandardResponse("Data inserted successfully."));
		});
		
		options("/independentStorage/insert", (request, response) -> {   
    		response.header("Access-Control-Allow-Origin", "*");
    		response.header("Access-Control-Allow-Methods", "POST");
    		response.header("Access-Control-Allow-Headers", "Origin, Content-Type, Authorization, Accept");
    		response.header("Allow", "POST, OPTIONS");
    		response.status(200);
    		return "";
        });
		
		post("/independentStorage/select", (req, res) -> {	 
			LOGGER.info("select measurement query selected");
			APIServiceImpl apiImpl = new APIServiceImpl(); 
			String measurementListString;			
			try {
				measurementListString = apiImpl.selectMeasurement(req.body(), url);		
			}catch(ConnectException ex) {
				res.status(404);
				return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, "InfluxDB database not found"));
			}catch(IllegalArgumentException ex) {
				res.status(400);
				return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, ex.getMessage()));
			}catch(JsonSyntaxException ex) {
				res.status(400);
				return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, "Malformed JSON element"));
			}catch(Exception ex) {
				res.status(500);
				return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, ex.getMessage()));
			}
			res.header("Content-Type", "application/json;charset=UTF-8");
			res.header("Access-Control-Allow-Origin", "*");
			res.header("Access-Control-Allow-Methods", "POST");
			res.header("Access-Control-Allow-Headers", "Origin, Content-Type, Authorization, Accept");
			return measurementListString;
		});		
		
		get("/independentStorage/select", (req, res) -> {	
			LOGGER.info("select measurement query selected");
			APIServiceImpl apiImpl = new APIServiceImpl(); 
			String measurementListString;			
			String db = req.queryParams("db");
			String table = req.queryParams("table");
			String query = req.queryParams("query");
			try {
				measurementListString = apiImpl.selectMeasurement(db, table, query, url);	
			}catch(ConnectException ex) {
				res.status(404);
				return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, "InfluxDB database not found"));
			}catch(IllegalArgumentException ex) {
				res.status(400);
				return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, ex.getMessage()));
			}catch(Exception ex) {
				res.status(500);
				return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, ex.getMessage()));
			}
			res.header("Content-Type", "application/json;charset=UTF-8");
			res.header("Access-Control-Allow-Origin", "*");
			res.header("Access-Control-Allow-Methods", "POST");
			res.header("Access-Control-Allow-Headers", "Origin, Content-Type, Authorization, Accept");
			return measurementListString;
		});		
		
		options("/independentStorage/select", (request, response) -> {   
    		response.header("Access-Control-Allow-Origin", "*");
    		response.header("Access-Control-Allow-Methods", "POST, GET");
    		response.header("Access-Control-Allow-Headers", "Origin, Content-Type, Authorization, Accept");
    		response.header("Allow", "POST, GET, OPTIONS");
    		response.status(200);
    		return "";
        });
		
		post("/independentStorage/delete", (req, res) -> {																
			LOGGER.info("delete measurement query selected");
			APIServiceImpl apiImpl = new APIServiceImpl(); 
			try {
				apiImpl.deleteMeasurement(req.body(), url);
			}catch(ConnectException ex) {
				res.status(404);
				return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, "InfluxDB database not found"));
			}catch(IllegalArgumentException ex) {
				res.status(400);
				return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, ex.getMessage()));
			}catch(JsonSyntaxException ex) {
				res.status(400);
				return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, "Malformed JSON element"));
			}catch(Exception ex) {
				res.status(500);
				return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, ex.getMessage()));
			}
			res.header("Content-Type", "application/json;charset=UTF-8");
			res.header("Access-Control-Allow-Origin", "*");
			res.header("Access-Control-Allow-Methods", "POST");
			res.header("Access-Control-Allow-Headers", "Origin, Content-Type, Authorization, Accept");
			return new Gson().toJson(new StandardResponse("Success"));
		});
		
		delete("/independentStorage/delete", (req, res) -> {																
			LOGGER.info("delete measurement query selected");
			APIServiceImpl apiImpl = new APIServiceImpl(); 
			try {
				apiImpl.deleteMeasurement(req.body(), url);
			}catch(ConnectException ex) {
				res.status(404);
				return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, "InfluxDB database not found"));
			}catch(IllegalArgumentException ex) {
				res.status(400);
				return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, ex.getMessage()));
			}catch(JsonSyntaxException ex) {
				res.status(400);
				return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, "Malformed JSON element"));
			}catch(Exception ex) {
				res.status(500);
				return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, ex.getMessage()));
			}
			res.header("Content-Type", "application/json;charset=UTF-8");
			res.header("Access-Control-Allow-Origin", "*");
			res.header("Access-Control-Allow-Methods", "POST");
			res.header("Access-Control-Allow-Headers", "Origin, Content-Type, Authorization, Accept");
			return new Gson().toJson(new StandardResponse("Success"));
		});
		
		options("/independentStorage/delete", (request, response) -> {   
    		response.header("Access-Control-Allow-Origin", "*");
    		response.header("Access-Control-Allow-Methods", "POST, DELETE");
    		response.header("Access-Control-Allow-Headers", "Origin, Content-Type, Authorization, Accept");
    		response.header("Allow", "POST, DELETE, OPTIONS");
    		response.status(200);
    		return "";
        });
		
		post("/independentStorage/update", (req, res) -> {		
			LOGGER.info("update measurement query selected");
			APIServiceImpl apiImpl = new APIServiceImpl(); 
			try {
				apiImpl.updateMeasurement(req.body(), url);	
			}catch(ConnectException ex) {
				res.status(404);
				return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, "InfluxDB database not found"));
			}catch(IllegalArgumentException ex) {
				res.status(400);
				return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, ex.getMessage()));
			}catch(JsonSyntaxException ex) {
				res.status(400);
				return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, "Malformed JSON element"));
			}catch(Exception ex) {
				res.status(500);
				return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, ex.getMessage()));
			}
			res.header("Content-Type", "application/json;charset=UTF-8");
			res.header("Access-Control-Allow-Origin", "*");
			res.header("Access-Control-Allow-Methods", "POST");
			res.header("Access-Control-Allow-Headers", "Origin, Content-Type, Authorization, Accept");
			return new Gson().toJson(new StandardResponse("Success"));
		});		
		
		options("/independentStorage/update", (request, response) -> {   
    		response.header("Access-Control-Allow-Origin", "*");
    		response.header("Access-Control-Allow-Methods", "POST");
    		response.header("Access-Control-Allow-Headers", "Origin, Content-Type, Authorization, Accept");
    		response.header("Allow", "POST, OPTIONS");
    		response.status(200);
    		return "";
        });
		
		post("/independentStorage/databases", (req, res) -> {		
			LOGGER.info("list databases selected");
			APIServiceImpl apiImpl = new APIServiceImpl(); 
			String databases;
			try {
				databases = apiImpl.showDatabases(url);	
			}catch(ConnectException ex) {
				res.status(404);
				return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, "InfluxDB database not found"));
			}catch(Exception ex) {
				res.status(500);
				return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, ex.getMessage()));
			}
			res.header("Content-Type", "application/json;charset=UTF-8");
			res.header("Access-Control-Allow-Origin", "*");
			res.header("Access-Control-Allow-Methods", "POST");
			res.header("Access-Control-Allow-Headers", "Origin, Content-Type, Authorization, Accept");
			return databases;
		});		
		
		options("/independentStorage/databases", (request, response) -> {   
    		response.header("Access-Control-Allow-Origin", "*");
    		response.header("Access-Control-Allow-Methods", "POST");
    		response.header("Access-Control-Allow-Headers", "Origin, Content-Type, Authorization, Accept");
    		response.header("Allow", "POST, OPTIONS");
    		response.status(200);
    		return "";
        });
		
		post("/independentStorage/tables", (req, res) -> {		
			LOGGER.info("list tables selected");
			APIServiceImpl apiImpl = new APIServiceImpl(); 
			String databases;
			try {
				databases = apiImpl.showTables(req.body(), url);	
			}catch(ConnectException ex) {
				res.status(404);
				return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, "InfluxDB database not found"));
			}catch(Exception ex) {
				res.status(500);
				return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, ex.getMessage()));
			}
			res.header("Content-Type", "application/json;charset=UTF-8");
			res.header("Access-Control-Allow-Origin", "*");
			res.header("Access-Control-Allow-Methods", "POST");
			res.header("Access-Control-Allow-Headers", "Origin, Content-Type, Authorization, Accept");
			return databases;
		});		
		
		options("/independentStorage/tables", (request, response) -> {   
    		response.header("Access-Control-Allow-Origin", "*");
    		response.header("Access-Control-Allow-Methods", "POST");
    		response.header("Access-Control-Allow-Headers", "Origin, Content-Type, Authorization, Accept");
    		response.header("Allow", "POST, OPTIONS");
    		response.status(200);
    		return "";
        });
					
	}

}
