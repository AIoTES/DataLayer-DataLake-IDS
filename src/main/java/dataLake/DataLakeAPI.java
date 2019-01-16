package dataLake;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.delete;

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
			return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, "The data base was created successfully."));		
		});		
		
		delete("/independentStorage/deleteDB", (req, res) -> {	
			LOGGER.info("deleteDB query selected");
			APIServiceImpl apiImpl = new APIServiceImpl(); 
			try {
				apiImpl.deleteDB(req.body(), url);
			}catch(Exception ex) {
				return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, ex.getMessage()));
			}
			return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, "The database was deleted successfully."));
		});
		
		post("/independentStorage/insert", (req, res) -> {	
			LOGGER.info("insert measurement query selected");
			APIServiceImpl apiImpl = new APIServiceImpl(); 
			try {
				apiImpl.insertMeasurement(req.body(), url);		
			}catch(Exception ex) {
				return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, ex.getMessage()));
			}
			return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, "Data inserted successfully."));	
		});
		
		get("/independentStorage/select", (req, res) -> {	 
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
		
		delete("/independentStorage/delete", (req, res) -> {																
			LOGGER.info("delete measurement query selected");
			APIServiceImpl apiImpl = new APIServiceImpl(); 
			try {
				apiImpl.deleteMeasurement(req.body(), url);
			}catch(Exception ex) {
				return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, ex.getMessage()));
			}
			return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS));
		});
		
		post("/independentStorage/update", (req, res) -> {	//TODO: 		
			return new Gson().toJson(new StandardResponse(StatusResponse.ERROR));		
		});		
				
	}

}
