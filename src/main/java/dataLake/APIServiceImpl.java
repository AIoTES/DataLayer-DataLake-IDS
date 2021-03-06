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

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.influxdb.BatchOptions;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.InfluxDBIOException;
import org.influxdb.annotation.Measurement;
import org.influxdb.dto.BoundParameterQuery.QueryBuilder;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.impl.InfluxDBResultMapper;
import org.joda.time.DateTime;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

public class APIServiceImpl implements APIService{
	
	private static final Logger LOGGER = LogManager.getLogger(IDSAPI.class);
	
	public void createDB(String MessageBodyRequest, String url) throws Exception {
		Gson gson = new Gson();		
		BodyRequest parsedMessageBodyRequest;
		
		InfluxDB influxDB = InfluxDBFactory.connect(url, "root", "root");	
		influxDB.enableBatch(BatchOptions.DEFAULTS);
		
		try {
			influxDB.ping();
			parsedMessageBodyRequest = gson.fromJson(MessageBodyRequest, BodyRequest.class);	
			CheckFields.CheckFieldDb(parsedMessageBodyRequest);
			influxDB.query(new Query("CREATE DATABASE "+ parsedMessageBodyRequest.getDb(), ""));
		}catch(InfluxDBIOException ex) {
			LOGGER.error(ex.getMessage());
			throw new ConnectException(ex.getMessage());	
		}catch(IllegalArgumentException ex) {
			LOGGER.error(ex.getMessage());
			throw new IllegalArgumentException(ex.getMessage());	
		}catch(JsonSyntaxException ex) {
			LOGGER.error(ex.getMessage());
			throw new JsonSyntaxException(ex.getMessage());	
		}catch(Exception ex) {
			LOGGER.error(ex.getMessage());
			throw new Exception(ex.getMessage());	
		}finally {
			influxDB.close();
		}
	}
	
	public void deleteDB(String messageBodyRequest, String url) throws Exception{
		Gson gson = new Gson();
		BodyRequest parsedMessageBodyRequest;	
		
		InfluxDB influxDB = InfluxDBFactory.connect(url, "root", "root");	
		influxDB.enableBatch(BatchOptions.DEFAULTS);
		
		try {
			influxDB.ping();
			parsedMessageBodyRequest = gson.fromJson(messageBodyRequest, BodyRequest.class);			
			CheckFields.CheckFieldDb(parsedMessageBodyRequest);
			influxDB.query(new Query("DROP DATABASE "+ parsedMessageBodyRequest.getDb(), parsedMessageBodyRequest.getDb()));
		}catch(InfluxDBIOException ex) {
			LOGGER.error(ex.getMessage());
			throw new ConnectException(ex.getMessage());	
		}catch(IllegalArgumentException ex) {
			LOGGER.error(ex.getMessage());
			throw new IllegalArgumentException(ex.getMessage());	
		}catch(JsonSyntaxException ex) {
			LOGGER.error(ex.getMessage());
			throw new JsonSyntaxException(ex.getMessage());	
		}catch(Exception ex) {
			LOGGER.error(ex.getMessage());
			throw new Exception(ex.getMessage());	
		}finally {
			influxDB.close();
		}
	}
	
	public void insertMeasurement(String messageBodyRequest, String url) throws Exception{		
		Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setPrettyPrinting()
                .serializeNulls()	
                .create();
		BodyRequest parsedMessageBodyRequest;		
		
		InfluxDB influxDB = InfluxDBFactory.connect(url, "root", "root");	
		influxDB.enableBatch(BatchOptions.DEFAULTS);
		
		try {
			influxDB.ping();		
			parsedMessageBodyRequest = gson.fromJson(messageBodyRequest, BodyRequest.class);
			CheckFields.CheckInsertMeasurementRequest(parsedMessageBodyRequest);
			IoTMeasurement measurement = parsedMessageBodyRequest.getData();
			
			CheckFields.CheckInsertMeasurementRequest(parsedMessageBodyRequest);
			Point point = Point
			        .measurement(parsedMessageBodyRequest.getTable())
			        .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)				        
			        .addField("platformId", measurement.getPlatformId())	//.tag
			        .addField("device", measurement.getDevice())	//.tag
			        .addField("observation", Utils.getObservation(messageBodyRequest))				        
			        .build();
			
			influxDB.setDatabase(parsedMessageBodyRequest.getDb());
			influxDB.write(point);		
		}catch(InfluxDBIOException ex) {
			LOGGER.error(ex.getMessage());
			throw new ConnectException(ex.getMessage());	
		}catch(IllegalArgumentException ex) {
			LOGGER.error(ex.getMessage());
			throw new IllegalArgumentException(ex.getMessage());	
		}catch(JsonSyntaxException ex) {
			LOGGER.error(ex.getMessage());
			throw new JsonSyntaxException(ex.getMessage());	
		}catch(Exception ex) {
			LOGGER.error(ex.getMessage());
			throw new Exception(ex.getMessage());	
		}finally {
			influxDB.close();
		}
	}
	
	public String selectMeasurement(String messageBodyRequest, String url) throws Exception{
		Gson gson = new Gson();			
		BodyRequest parsedMessageBodyRequest;	
		QueryResult queryResult;
		String measurementListString = null;
		
		InfluxDB influxDB = InfluxDBFactory.connect(url, "root", "root");	
		influxDB.enableBatch(BatchOptions.DEFAULTS);
		
		try {
			influxDB.ping();
			parsedMessageBodyRequest = gson.fromJson(messageBodyRequest, BodyRequest.class);			
			CheckFields.CheckSelectMeasurementRequest(parsedMessageBodyRequest);
			
			DynamicMeasurement altered = new DynamicMeasurement(parsedMessageBodyRequest.getTable());
			AnnotationHelper.alterAnnotationOn(IoTMeasurementWithoutTime.class, Measurement.class, altered);		

			queryResult = influxDB.query(new Query(parsedMessageBodyRequest.getQuery(), parsedMessageBodyRequest.getDb()));	

			InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();
			List<IoTMeasurementWithoutTime> measurementListIncorrectType = resultMapper.toPOJO(queryResult, IoTMeasurementWithoutTime.class);
			
			List<IoTMeasurementParsed> IoTMeasurementsParsed = new ArrayList<IoTMeasurementParsed>();		
			for (IoTMeasurementWithoutTime measure : measurementListIncorrectType) { 	
				JsonObject observationJson = new Gson().fromJson(measure.getObservation(), JsonObject.class);				
				IoTMeasurementsParsed.add(new IoTMeasurementParsed(measure.getPlatformId(), measure.getDevice(), observationJson));
			}				
			Gson gson2 = new GsonBuilder().setPrettyPrinting().create();	
			
			measurementListString = gson2.toJson(IoTMeasurementsParsed);
		}catch(InfluxDBIOException ex) {
			LOGGER.error(ex.getMessage());
			throw new ConnectException(ex.getMessage());
		}catch(IllegalArgumentException ex) {
			LOGGER.error(ex.getMessage());
			throw new IllegalArgumentException(ex.getMessage());	
		}catch(JsonSyntaxException ex) {
			LOGGER.error(ex.getMessage());
			throw new JsonSyntaxException(ex.getMessage());	
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
			influxDB.ping();
			parsedMessageBodyRequest.setDb(db);
			parsedMessageBodyRequest.setTable(table);
			parsedMessageBodyRequest.setQuery(query);
			CheckFields.CheckSelectMeasurementRequest(parsedMessageBodyRequest);
			
			DynamicMeasurement altered = new DynamicMeasurement(parsedMessageBodyRequest.getTable());
			AnnotationHelper.alterAnnotationOn(IoTMeasurementWithoutTime.class, Measurement.class, altered);

			queryResult = influxDB.query(new Query(parsedMessageBodyRequest.getQuery(), parsedMessageBodyRequest.getDb()));	
			
			InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();
			List<IoTMeasurementWithoutTime> measurementListIncorrectType = resultMapper.toPOJO(queryResult, IoTMeasurementWithoutTime.class);
			
			List<IoTMeasurementParsed> IoTMeasurementsParsed = new ArrayList<IoTMeasurementParsed>();		
			for (IoTMeasurementWithoutTime measure : measurementListIncorrectType) { 	
				JsonObject observationJson = new Gson().fromJson(measure.getObservation(), JsonObject.class);				
				IoTMeasurementsParsed.add(new IoTMeasurementParsed(measure.getPlatformId(), measure.getDevice(), observationJson));
			}				
			Gson gson2 = new GsonBuilder().setPrettyPrinting().create();	
			
			measurementListString = gson2.toJson(IoTMeasurementsParsed);
		}catch(InfluxDBIOException ex) {
			LOGGER.error(ex.getMessage());
			throw new ConnectException(ex.getMessage());	
		}catch(IllegalArgumentException ex) {
			LOGGER.error(ex.getMessage());
			throw new IllegalArgumentException(ex.getMessage());	
		} catch(Exception ex) {
			LOGGER.error(ex.getMessage());
			throw new Exception(ex.getMessage());	
		}finally {
			influxDB.close();
		}
		
		return measurementListString;		
	}
	
	public void deleteMeasurement(String messageBodyRequest, String url) throws Exception{
		Gson gson = new Gson();
		BodyRequest parsedMessageBodyRequest;
		
		InfluxDB influxDB = InfluxDBFactory.connect(url, "root", "root");
		influxDB.enableBatch(BatchOptions.DEFAULTS);
		
		try {
			influxDB.ping();
			parsedMessageBodyRequest = gson.fromJson(messageBodyRequest, BodyRequest.class);
			CheckFields.CheckDeleteMeasurementRequest(parsedMessageBodyRequest);

			Query queryResult = QueryBuilder.newQuery(parsedMessageBodyRequest.getQuery())
			        .forDatabase(parsedMessageBodyRequest.getDb())
			        .create();			
			influxDB.query(queryResult);
		}catch(InfluxDBIOException ex) {
			LOGGER.error(ex.getMessage());
			throw new ConnectException(ex.getMessage());
		}catch(IllegalArgumentException ex) {
			LOGGER.error(ex.getMessage());
			throw new IllegalArgumentException(ex.getMessage());	
		}catch(JsonSyntaxException ex) {
			LOGGER.error(ex.getMessage());
			throw new JsonSyntaxException(ex.getMessage());	
		}catch(Exception ex) {
			LOGGER.error(ex.getMessage());
			throw new Exception(ex.getMessage());	
		}finally {
			influxDB.close();
		}
	}
	
	public void updateMeasurement(String messageBodyRequest, String url) throws Exception{	
		BodyRequest parsedMessageBodyRequest;
		QueryResult queryResult;				
		String observation = null;
		Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setPrettyPrinting()
                .serializeNulls()	
                .create();
		//JSONObject jsonObjObservation;
		InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();
		List<IoTMeasurement> measurementList;
		
		InfluxDB influxDB = InfluxDBFactory.connect(url, "root", "root");	
		influxDB.enableBatch(BatchOptions.DEFAULTS);
				
		try {	
			influxDB.ping();
			parsedMessageBodyRequest = gson.fromJson(messageBodyRequest, BodyRequest.class);
			CheckFields.CheckUpdateMeasurementRequest(parsedMessageBodyRequest);	
			
			IoTMeasurement dataToUpdate = parsedMessageBodyRequest.getData();
			
			//jsonObjObservation = new JSONObject(messageBodyRequest);	
			
			//if (jsonObjObservation.getJSONObject("data").toString().contains("\"observation\":")) {
			if (Utils.getData(messageBodyRequest).contains("\"observation\":")) {
				//observation = jsonObjObservation.getJSONObject("data").getJSONObject("observation").toString();	
				observation = Utils.getObservation(messageBodyRequest);
			}					
					
			DynamicMeasurement altered = new DynamicMeasurement(parsedMessageBodyRequest.getTable());	
			AnnotationHelper.alterAnnotationOn(IoTMeasurement.class, Measurement.class, altered);
			queryResult = influxDB.query(new Query(parsedMessageBodyRequest.getQuery(), parsedMessageBodyRequest.getDb()));
			measurementList = resultMapper.toPOJO(queryResult, IoTMeasurement.class);				
			
			String deleteQuery = parsedMessageBodyRequest.getQuery().replaceFirst("select", "DELETE").replaceFirst("SELECT", "DELETE").replaceFirst("\\*", "");				
			
			Query queryDeleteResult = QueryBuilder.newQuery(deleteQuery)
			        .forDatabase(parsedMessageBodyRequest.getDb())
			        .create();			
			influxDB.query(queryDeleteResult);		
			
			for (IoTMeasurement measurement : measurementList) { 	
				DateTime time = new DateTime(measurement.getTime());		
				
				Point point = Point
				        .measurement(parsedMessageBodyRequest.getTable())	
				        .time(time.getMillis(), TimeUnit.MILLISECONDS)	
				        .addField("platformId", (dataToUpdate.getPlatformId() != null) ? dataToUpdate.getPlatformId() : measurement.getPlatformId())   //.tag
				        .addField("device", (dataToUpdate.getDevice() != null) ? dataToUpdate.getDevice() : measurement.getDevice())  //.tag
				        .addField("observation", (observation != null) ? observation : measurement.getObservation())	
				.build();    	
				influxDB.setDatabase(parsedMessageBodyRequest.getDb());
				influxDB.write(point);	
			}
			
			}catch(InfluxDBIOException ex) {
				LOGGER.error(ex.getMessage());
				throw new ConnectException(ex.getMessage());
			}catch(IllegalArgumentException ex) {
				LOGGER.error(ex.getMessage());
				throw new IllegalArgumentException(ex.getMessage());	
			}catch(JsonSyntaxException ex) {
				LOGGER.error(ex.getMessage());
				throw new JsonSyntaxException(ex.getMessage());	
			}catch(Exception ex) {
				LOGGER.error(ex.getMessage());
				throw new Exception(ex.getMessage());
			}finally {
				influxDB.close();
			}		
	}		
	
	public String showDatabases(String url) throws Exception {		
		List<String> databasesList = new ArrayList<String>();
		String databases = "";
		
		InfluxDB influxDB = InfluxDBFactory.connect(url, "root", "root");	
		influxDB.enableBatch(BatchOptions.DEFAULTS);		
		
		try {
			influxDB.ping();
			QueryResult queryResult = influxDB.query(new Query("SHOW DATABASES", ""));		

			if (queryResult.getResults().get(0).getSeries() != null) {
				int size = queryResult.getResults().get(0).getSeries().get(0).getValues().size();
				List<List<Object>> databasesListofLists = queryResult.getResults().get(0).getSeries().get(0).getValues();		
				
				for (int i=0; i<size; i++) {
					databasesList.add(databasesListofLists.get(i).get(0).toString());
				}			
				
				Gson gson2 = new GsonBuilder().setPrettyPrinting().create();	
				databases = gson2.toJson(databasesList);
			}
			
		}catch(InfluxDBIOException ex) {
			LOGGER.error(ex.getMessage());
			throw new ConnectException(ex.getMessage());	
		}catch(Exception ex) {
			LOGGER.error(ex.getMessage());
			throw new Exception(ex.getMessage());
		}finally {
			influxDB.close();
		}
		
		return databases;
	}
	
	public String showTables(String messageBodyRequest, String url) throws Exception {
		Gson gson = new Gson();
		BodyRequest parsedMessageBodyRequest;
		List<String> tablesList = new ArrayList<String>();
		String tables = "";
		
		InfluxDB influxDB = InfluxDBFactory.connect(url, "root", "root");	
		influxDB.enableBatch(BatchOptions.DEFAULTS);		
		
		try {
			influxDB.ping();
			parsedMessageBodyRequest = gson.fromJson(messageBodyRequest, BodyRequest.class);
			QueryResult queryResult = influxDB.query(new Query("SHOW MEASUREMENTS ON " + parsedMessageBodyRequest.getDb(), ""));	
			
			if (queryResult.getResults().get(0).getSeries() != null) {
				int size = queryResult.getResults().get(0).getSeries().get(0).getValues().size();
				List<List<Object>> tablesListofLists = queryResult.getResults().get(0).getSeries().get(0).getValues();		
				
				for (int i=0; i<size; i++) {
					tablesList.add(tablesListofLists.get(i).get(0).toString());
				}						
				
				Gson gson2 = new GsonBuilder().setPrettyPrinting().create();	
				tables = gson2.toJson(tablesList);
			}
			
			
		}catch(InfluxDBIOException ex) {
			LOGGER.error(ex.getMessage());
			throw new ConnectException(ex.getMessage());	
		}catch(Exception ex) {
			LOGGER.error(ex.getMessage());
			throw new Exception(ex.getMessage());
		}finally {
			influxDB.close();
		}
		
		//return jsonObject.toString();
		return tables;	
	}
}
