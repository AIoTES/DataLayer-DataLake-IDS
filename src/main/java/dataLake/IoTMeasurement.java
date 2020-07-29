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

import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

import com.google.gson.annotations.Expose;

@Measurement(name = "meas")		
public class IoTMeasurement {
	@Column(name = "time")
	@Expose
	private String time;
	@Column(name = "platformId")
	@Expose
	private String platformId;
	@Column(name = "device")
	@Expose
    private String device;
	@Column(name = "observation")
	@Expose	(deserialize = false)		
    private String observation;		
    
	public IoTMeasurement() {
    }
	
    public IoTMeasurement(String time, String platformId, String device, String observation) {
    	this.time = time;
    	this.platformId = platformId;
        this.device = device;
        this.observation = observation;
    }
    
    public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
    
	public String getPlatformId() {
		return platformId;
	}


	public void setPlatformId(String platformId) {
		this.platformId = platformId;
	}


	public String getDevice() {
		return device;
	}


	public void setDevice(String device) {
		this.device = device;
	}


	public String getObservation() {
		return observation;
	}


	public void setObservation(String observation) {
		this.observation = observation;
	}    
    
}
