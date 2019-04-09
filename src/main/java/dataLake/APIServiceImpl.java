package dataLake;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.influxdb.BatchOptions;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.annotation.Measurement;
import org.influxdb.dto.BoundParameterQuery.QueryBuilder;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.impl.InfluxDBResultMapper;
import org.joda.time.DateTime;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dataLake.CheckFields;

public class APIServiceImpl implements APIService{
	
	private static final Logger LOGGER = LogManager.getLogger(IDSAPI.class);
	
	public void createDB(String MessageBodyRequest, String url) throws Exception {
		InfluxDB influxDB = InfluxDBFactory.connect(url, "root", "root");	
		influxDB.enableBatch(BatchOptions.DEFAULTS);		
		
		Gson gson = new Gson();		
		BodyRequest parsedMessageBodyRequest;
		
		try {
			parsedMessageBodyRequest = gson.fromJson(MessageBodyRequest, BodyRequest.class);	
			CheckFields.CheckFieldDb(parsedMessageBodyRequest);
			influxDB.query(new Query("CREATE DATABASE "+ parsedMessageBodyRequest.getDb(), ""));
		}catch(Exception ex) {
			LOGGER.error(ex.getMessage());
			throw new Exception(ex.getMessage());	
		}finally {
			influxDB.close();
		}
	}
	
	public void deleteDB(String messageBodyRequest, String url) throws Exception{
		InfluxDB influxDB = InfluxDBFactory.connect(url, "root", "root");	
		influxDB.enableBatch(BatchOptions.DEFAULTS);
		
		Gson gson = new Gson();
		BodyRequest parsedMessageBodyRequest;	
		
		try {
			parsedMessageBodyRequest = gson.fromJson(messageBodyRequest, BodyRequest.class);			
			CheckFields.CheckFieldDb(parsedMessageBodyRequest);
			influxDB.query(new Query("DROP DATABASE "+ parsedMessageBodyRequest.getDb(), parsedMessageBodyRequest.getDb()));
		}catch(Exception ex) {
			LOGGER.error(ex.getMessage());
			throw new Exception(ex.getMessage());	
		}finally {
			influxDB.close();
		}
	}
	
	public void insertMeasurement(String messageBodyRequest, String url) throws Exception{		
		InfluxDB influxDB = InfluxDBFactory.connect(url, "root", "root");	
		influxDB.enableBatch(BatchOptions.DEFAULTS);
		
		Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setPrettyPrinting()
                .serializeNulls()	
                .create();
		BodyRequest parsedMessageBodyRequest;	
		JSONObject jsonObj;
		
		try {
			parsedMessageBodyRequest = gson.fromJson(messageBodyRequest, BodyRequest.class);
			CheckFields.CheckInsertMeasurementRequest(parsedMessageBodyRequest);
			IoTMeasurement measurement = parsedMessageBodyRequest.getData();
			
			jsonObj = new JSONObject(messageBodyRequest);			
			String observation = jsonObj.getJSONObject("data").getJSONObject("observation").toString();			
			
			Point point = Point
			        .measurement(parsedMessageBodyRequest.getTable())
			        .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)	
			        .addField("platformId", measurement.getPlatformId())
			        .addField("device", measurement.getDevice())
			        .addField("observation", observation)
			        .build();
			
			influxDB.setDatabase(parsedMessageBodyRequest.getDb());
			influxDB.write(point);	
		}catch(Exception ex) {
			LOGGER.error(ex.getMessage());
			throw new Exception(ex.getMessage());	
		}finally {
			influxDB.close();
		}
	}
	
	public String selectMeasurement(String messageBodyRequest, String url) throws Exception{
		InfluxDB influxDB = InfluxDBFactory.connect(url, "root", "root");	
		influxDB.enableBatch(BatchOptions.DEFAULTS);
		
		Gson gson = new Gson();
		BodyRequest parsedMessageBodyRequest;	
		QueryResult queryResult;
		String measurementListString = null;
		
		try {
			parsedMessageBodyRequest = gson.fromJson(messageBodyRequest, BodyRequest.class);			
			CheckFields.CheckSelectMeasurementRequest(parsedMessageBodyRequest);
			
			DynamicMeasurement altered = new DynamicMeasurement(parsedMessageBodyRequest.getTable());
			AnnotationHelper.alterAnnotationOn(IoTMeasurement.class, Measurement.class, altered);

			queryResult = influxDB.query(new Query(parsedMessageBodyRequest.getQuery(), parsedMessageBodyRequest.getDb()));	
			
			InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();
			List<IoTMeasurement> measurementList = resultMapper.toPOJO(queryResult, IoTMeasurement.class);
			Gson gson2 = new GsonBuilder().setPrettyPrinting().create();	
			measurementListString = gson2.toJson(measurementList);
		} catch(Exception ex) {
			LOGGER.error(ex.getMessage());
			throw new Exception(ex.getMessage());	
		}finally {
			influxDB.close();
		}
		
		return measurementListString;
	}
	
	public String selectMeasurement(String db, String table, String query, String url) throws Exception{
		BodyRequest parsedMessageBodyRequest = new BodyRequest();	
		QueryResult queryResult;
		InfluxDB influxDB = InfluxDBFactory.connect(url, "root", "root");
		String measurementListString = null;
		
		try {
			parsedMessageBodyRequest.setDb(db);
			parsedMessageBodyRequest.setTable(table);
			parsedMessageBodyRequest.setQuery(query);
			CheckFields.CheckSelectMeasurementRequest(parsedMessageBodyRequest);
			
			DynamicMeasurement altered = new DynamicMeasurement(parsedMessageBodyRequest.getTable());
			AnnotationHelper.alterAnnotationOn(IoTMeasurement.class, Measurement.class, altered);

			queryResult = influxDB.query(new Query(parsedMessageBodyRequest.getQuery(), parsedMessageBodyRequest.getDb()));	
			
			InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();
			List<IoTMeasurement> measurementList = resultMapper.toPOJO(queryResult, IoTMeasurement.class);
			Gson gson2 = new GsonBuilder().setPrettyPrinting().create();	
			measurementListString = gson2.toJson(measurementList);
		} catch(Exception ex) {
			LOGGER.error(ex.getMessage());
			throw new Exception(ex.getMessage());	
		}finally {
			influxDB.close();
		}
		
		return measurementListString;		
	}
	
	public void deleteMeasurement(String messageBodyRequest, String url) throws Exception{
		InfluxDB influxDB = InfluxDBFactory.connect(url, "root", "root");
		influxDB.enableBatch(BatchOptions.DEFAULTS);
		
		Gson gson = new Gson();
		BodyRequest parsedMessageBodyRequest;
		
		try {
			parsedMessageBodyRequest = gson.fromJson(messageBodyRequest, BodyRequest.class);
			CheckFields.CheckDeleteMeasurementRequest(parsedMessageBodyRequest);

			Query queryResult = QueryBuilder.newQuery(parsedMessageBodyRequest.getQuery())
			        .forDatabase(parsedMessageBodyRequest.getDb())
			        .create();			
			influxDB.query(queryResult);
		}catch(Exception ex) {
			LOGGER.error(ex.getMessage());
			throw new Exception(ex.getMessage());	
		}finally {
			influxDB.close();
		}
	}
	
	public void updateMeasurement(String messageBodyRequest, String url) throws Exception{	
		InfluxDB influxDB = InfluxDBFactory.connect(url, "root", "root");	
		influxDB.enableBatch(BatchOptions.DEFAULTS);
		
		BodyRequest parsedMessageBodyRequest;
		QueryResult queryResult;				
		String observation = null;
		Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setPrettyPrinting()
                .serializeNulls()	
                .create();
		JSONObject jsonObjObservation;
		InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();
		List<IoTMeasurement> measurementList;
				
		try {	
			parsedMessageBodyRequest = gson.fromJson(messageBodyRequest, BodyRequest.class);
			CheckFields.CheckUpdateMeasurementRequest(parsedMessageBodyRequest);	
			
			IoTMeasurement dataToUpdate = parsedMessageBodyRequest.getData();
			
			jsonObjObservation = new JSONObject(messageBodyRequest);			
			if (jsonObjObservation.getJSONObject("data").toString().contains("\"observation\":")) {
				observation = jsonObjObservation.getJSONObject("data").getJSONObject("observation").toString();	
			}					
					
			DynamicMeasurement altered = new DynamicMeasurement(parsedMessageBodyRequest.getTable());	
			AnnotationHelper.alterAnnotationOn(IoTMeasurement.class, Measurement.class, altered);
			queryResult = influxDB.query(new Query(parsedMessageBodyRequest.getQuery(), parsedMessageBodyRequest.getDb()));
			measurementList = resultMapper.toPOJO(queryResult, IoTMeasurement.class);				
			
			for (IoTMeasurement measurement : measurementList) { 	
				DateTime time = new DateTime(measurement.getTime());
				
				Point point = Point
				        .measurement(parsedMessageBodyRequest.getTable())	
				        .time(time.getMillis(), TimeUnit.MILLISECONDS)	
				        .addField("platformId", (dataToUpdate.getPlatformId() != null) ? dataToUpdate.getPlatformId() : measurement.getPlatformId())
				        .addField("device", (dataToUpdate.getDevice() != null) ? dataToUpdate.getDevice() : measurement.getDevice())
				        .addField("observation", (observation != null) ? observation : measurement.getObservation())	
				.build();    	
				influxDB.setDatabase(parsedMessageBodyRequest.getDb());
				influxDB.write(point);	
			}
					
			}catch(Exception ex) {
				LOGGER.error(ex.getMessage());
				throw new Exception(ex.getMessage());
			}finally {
				influxDB.close();
			}		
	}		
}
