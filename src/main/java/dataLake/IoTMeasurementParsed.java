package dataLake;

import com.google.gson.JsonObject;
	
public class IoTMeasurementParsed {
	private String platformId;
    private String device;
    private JsonObject observation;		
    
	public IoTMeasurementParsed() {
    }
	
    public IoTMeasurementParsed(String platformId, String device, JsonObject observation) {
    	this.platformId = platformId;
        this.device = device;
        this.observation = observation;
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


	public JsonObject getObservation() {
		return observation;
	}


	public void setObservation(JsonObject observation) {
		this.observation = observation;
	}    

}
