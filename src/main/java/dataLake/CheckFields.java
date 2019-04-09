package dataLake;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CheckFields {
	
	private static final Logger LOGGER = LogManager.getLogger(IDSAPI.class);
	
	private static boolean getParam(String query, String keyWord) {
		String[] array = query.split(" ");

		if (array[0].equals(keyWord.toUpperCase())  || array[0].equals(keyWord.toLowerCase())) {
			return true;	
		}
		return false;		
	}	
	
	public static void CheckFieldDb(BodyRequest request) throws Exception {
		if (request.getDb() == null || request.getDb().trim().isEmpty()) {
			LOGGER.error("The field \"db\" is null or empty ");
			throw new Exception("Invalid request");
		}	
	}	
	
	public static void CheckFieldTable(BodyRequest request) throws Exception {
		if (request.getTable() == null || request.getTable().trim().isEmpty()) {
			LOGGER.error("The field \"table\" is null or empty ");
			throw new Exception("Invalid request");
		}	
	}	
	
	public static void CheckFieldData(BodyRequest request) throws Exception {
		if (request.getData() == null) {
			LOGGER.error("The field \"data\" is null or empty ");
			throw new Exception("Invalid request");
		}
	}	
	
	public static void CheckFieldDataAll(BodyRequest request) throws Exception {
		if (request.getData() == null) {
			LOGGER.error("The field \"data\" is null or empty ");
			throw new Exception("Invalid request");
		}
		
		if(request.getData().getDevice() == null || request.getData().getPlatformId() == null){	
			LOGGER.error("At least one of the \"data\" subfields is null");
			throw new Exception("Invalid request");
		}
		
		if(request.getData().getDevice().trim().isEmpty() || request.getData().getPlatformId().trim().isEmpty()){  
			LOGGER.error("At least one of the \"data\" subfields is empty");
			throw new Exception("Invalid request");
		}	
		
		/*if(request.getData().getObservation() == null ){  
			LOGGER.error("The \"observation\" subfields is null");
			throw new Exception("Invalid request");
		}	
		
		if(request.getData().getObservation().trim().isEmpty() ){  
			LOGGER.error("The \"observation\" subfields is empty");
			throw new Exception("Invalid request");
		}	*/
	}	
	
	public static void CheckFieldQuery(BodyRequest request) throws Exception {
		if (request.getQuery() == null || request.getQuery().trim().isEmpty()) {
			LOGGER.error("The field \"query\" is null or empty ");
			throw new Exception("Invalid request");
		}	
	}	
	
	public static void CheckFieldQuerySelect(BodyRequest request) throws Exception {
		if (!getParam(request.getQuery(), "SELECT")) {
			LOGGER.error("\"SELECT\" is not included in the query");
			throw new Exception("Invalid request");
		}	
	}	
	
	public static void CheckFieldQueryDelete(BodyRequest request) throws Exception {
		if (!getParam(request.getQuery(), "DELETE")) {
			LOGGER.error("\"DELETE\" is not included in the query");
			throw new Exception("Invalid request");
		}	
	}	
	
	public static void CheckInsertMeasurementRequest(BodyRequest request) throws Exception {
		CheckFieldDb(request);
		CheckFieldTable(request);
		CheckFieldDataAll(request);		
	}	
	
	public static void CheckSelectMeasurementRequest(BodyRequest request) throws Exception {		
		CheckFieldDb(request);
		CheckFieldTable(request);
		CheckFieldQuery(request);
		CheckFieldQuerySelect(request);			
	}	
	
	public static void CheckDeleteMeasurementRequest(BodyRequest request) throws Exception {		
		CheckFieldDb(request);
		CheckFieldTable(request);
		CheckFieldQuery(request);	
		CheckFieldQueryDelete(request);
	}	
	
	public static void CheckUpdateMeasurementRequest(BodyRequest request) throws Exception {
		CheckFieldDb(request);		
		CheckFieldTable(request);
		CheckFieldQuery(request);
		CheckFieldQuerySelect(request);	
		CheckFieldData(request);			
	}
}
