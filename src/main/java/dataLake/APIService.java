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

public interface APIService {		
	public void createDB(String messageBodyRequest, String url) throws Exception;
	
	public void deleteDB(String messageBodyRequest, String url) throws Exception;
	
	public void insertMeasurement(String messageBodyRequest, String url) throws Exception;

    public String selectMeasurement(String messageBodyRequest, String url) throws Exception;
    
    public String selectMeasurement(String db, String table, String query, String url) throws Exception;
    
    public void deleteMeasurement(String messageBodyRequest, String url) throws Exception;

    public void updateMeasurement(String messageBodyRequest, String url) throws Exception;
    
    public String showDatabases(String url) throws Exception;
    
    public String showTables(String messageBodyRequest, String url) throws Exception;
    
}
