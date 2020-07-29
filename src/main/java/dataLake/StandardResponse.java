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

public class StandardResponse {
	private StatusResponse status;
    private String message;
    private JsonElement data;
       
    public StandardResponse(StatusResponse status, String message) {
        this.status = status;
        this.message = message;
    }   
    
    /*public StandardResponse(StatusResponse status, JsonElement data) {
        this.status = status;
        this.data = data;
    }*/
    
    /*public StandardResponse(StatusResponse status) {
        this.status = status;
    }*/
    
    public StandardResponse(String message) {
        this.message = message;
    }       

    public StatusResponse getStatus() {
        return status;
    }

    public void setStatus(StatusResponse status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public JsonElement getData() {
        return data;
    }

    public void setData(JsonElement data) {
        this.data = data;
}
}
