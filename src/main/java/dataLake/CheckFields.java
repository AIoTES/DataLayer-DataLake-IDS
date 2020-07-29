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
	
	public static void CheckFieldDb(BodyRequest request) throws IllegalArgumentException {
		if (request.getDb() == null || request.getDb().trim().isEmpty()) {
			LOGGER.error("The field \"db\" is null or empty ");
			throw new IllegalArgumentException("Invalid request");
		}	
	}	
	
	public static void CheckFieldTable(BodyRequest request) throws IllegalArgumentException {
		if (request.getTable() == null || request.getTable().trim().isEmpty()) {
			LOGGER.error("The field \"table\" is null or empty ");
			throw new IllegalArgumentException("Invalid request");
		}	
	}	
	
	public static void CheckFieldData(BodyRequest request) throws IllegalArgumentException {
		if (request.getData() == null) {
			LOGGER.error("The field \"data\" is null or empty ");
			throw new IllegalArgumentException("Invalid request");
		}
	}	
	
	public static void CheckFieldDataAll(BodyRequest request) throws IllegalArgumentException {
		if (request.getData() == null) {
			LOGGER.error("The field \"data\" is null or empty ");
			throw new IllegalArgumentException("Invalid request");
		}
		
		if(request.getData().getDevice() == null || request.getData().getPlatformId() == null){	
			LOGGER.error("At least one of the \"data\" subfields is null");
			throw new IllegalArgumentException("Invalid request");
		}
		
		if(request.getData().getDevice().trim().isEmpty() || request.getData().getPlatformId().trim().isEmpty()){  
			LOGGER.error("At least one of the \"data\" subfields is empty");
			throw new IllegalArgumentException("Invalid request");
		}	
		
		/*if(request.getData().getObservation() == null ){  
			LOGGER.error("The \"observation\" subfields is null");
			throw new IllegalArgumentException("Invalid request");
		}	
		
		if(request.getData().getObservation().trim().isEmpty() ){  
			LOGGER.error("The \"observation\" subfields is empty");
			throw new IllegalArgumentException("Invalid request");
		}	*/
	}	
	
	public static void CheckFieldQuery(BodyRequest request) throws IllegalArgumentException {
		if (request.getQuery() == null || request.getQuery().trim().isEmpty()) {
			LOGGER.error("The field \"query\" is null or empty ");
			throw new IllegalArgumentException("Invalid request");
		}	
	}	
	
	public static void CheckFieldQuerySelect(BodyRequest request) throws IllegalArgumentException {
		if (!getParam(request.getQuery(), "SELECT")) {
			LOGGER.error("\"SELECT\" is not included in the query");
			throw new IllegalArgumentException("Invalid request");
		}	
	}	
	
	public static void CheckFieldQueryDelete(BodyRequest request) throws IllegalArgumentException {
		if (!getParam(request.getQuery(), "DELETE")) {
			LOGGER.error("\"DELETE\" is not included in the query");
			throw new IllegalArgumentException("Invalid request");
		}	
	}	
	
	public static void CheckInsertMeasurementRequest(BodyRequest request) throws IllegalArgumentException {
		CheckFieldDb(request);
		CheckFieldTable(request);
		CheckFieldDataAll(request);		
	}	
	
	public static void CheckSelectMeasurementRequest(BodyRequest request) throws IllegalArgumentException {		
		CheckFieldDb(request);
		CheckFieldTable(request);
		CheckFieldQuery(request);
		CheckFieldQuerySelect(request);			
	}	
	
	public static void CheckDeleteMeasurementRequest(BodyRequest request) throws IllegalArgumentException {		
		CheckFieldDb(request);
		CheckFieldTable(request);
		CheckFieldQuery(request);	
		CheckFieldQueryDelete(request);
	}	
	
	public static void CheckUpdateMeasurementRequest(BodyRequest request) throws IllegalArgumentException {
		CheckFieldDb(request);		
		CheckFieldTable(request);
		CheckFieldQuery(request);
		CheckFieldQuerySelect(request);	
		CheckFieldData(request);			
	}
}
