package dataLake;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static spark.Spark.*;
import com.google.gson.Gson;

public class DataLakeAPI {
	
	private static final Logger LOGGER = LogManager.getLogger(DataLakeAPI.class);	
	static String localhost;
	static String port;
	static String url;
	
	public static void main(String[] args) {
		
		localhost = System.getenv().get("HOST_INFLUXDB") == null ? "localhost" : System.getenv().get("HOST_INFLUXDB");		
		port = System.getenv().get("PORT_INFLUXDB") == null ? "8086" : System.getenv().get("PORT_INFLUXDB");		
		url = "http://" + localhost + ":" + port;
		
		post("/independentStorage/createDB", (req, res) -> {	
			LOGGER.info("createDB query selected");			
			APIServiceImpl apiImpl = new APIServiceImpl(); 
			try {
				apiImpl.createDB(req.body(), url);
			}catch(Exception ex) {
				return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, ex.getMessage()));
			}			
			return new Gson().toJson(new StandardResponse("The data base was created successfully."));	
		});		
		
		post("/independentStorage/deleteDB", (req, res) -> {	
			LOGGER.info("deleteDB query selected");
			APIServiceImpl apiImpl = new APIServiceImpl(); 
			try {
				apiImpl.deleteDB(req.body(), url);
			}catch(Exception ex) {
				return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, ex.getMessage()));
			}
			return new Gson().toJson(new StandardResponse("The database was deleted successfully."));
		});
		
		post("/independentStorage/insert", (req, res) -> {	
			LOGGER.info("insert measurement query selected");
			APIServiceImpl apiImpl = new APIServiceImpl(); 
			try {
				apiImpl.insertMeasurement(req.body(), url);		
			}catch(Exception ex) {
				return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, ex.getMessage()));
			}
			return new Gson().toJson(new StandardResponse("Data inserted successfully."));
		});
		
		post("/independentStorage/select", (req, res) -> {	 
			LOGGER.info("select measurement query selected");
			APIServiceImpl apiImpl = new APIServiceImpl(); 
			String measurementListString;			
			try {
				measurementListString = apiImpl.selectMeasurement(req.body(), url);			
			}catch(Exception ex) {
				return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, ex.getMessage()));
			}
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
				measurementListString = apiImpl.selectMeasurement(db, table, query, url);	//qu� era url?		
			}catch(Exception ex) {
				return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, ex.getMessage()));
			}
			return measurementListString;
		});		
		
		post("/independentStorage/delete", (req, res) -> {																
			LOGGER.info("delete measurement query selected");
			APIServiceImpl apiImpl = new APIServiceImpl(); 
			try {
				apiImpl.deleteMeasurement(req.body(), url);
			}catch(Exception ex) {
				return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, ex.getMessage()));
			}
			return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS));
		});
		
		post("/independentStorage/update", (req, res) -> {		
			LOGGER.info("update measurement query selected");
			APIServiceImpl apiImpl = new APIServiceImpl(); 
			try {
				apiImpl.updateMeasurement(req.body(), url);	
			}catch(Exception ex) {
				return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, ex.getMessage()));
			}
			return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS));		
		});		
				
	}

}
