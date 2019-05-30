package dataLake;

import static spark.Spark.externalStaticFileLocation;
import static spark.Spark.get;
import static spark.Spark.post;

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
			return new Gson().toJson(new StandardResponse("The database was created successfully."));	
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
			return new Gson().toJson(new StandardResponse("The database was deleted successfully."));
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
			return new Gson().toJson(new StandardResponse("Data inserted successfully."));
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
			return measurementListString;
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
			return new Gson().toJson(new StandardResponse("Success"));
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
			return new Gson().toJson(new StandardResponse("Success"));
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
			return databases;
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
			return databases;
		});		
				
	}

}
