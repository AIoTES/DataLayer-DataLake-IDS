package dataLake;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

import com.google.gson.annotations.Expose;

@Measurement(name = "meas")		
public class IoTMeasurementWithTime {
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
    
	public IoTMeasurementWithTime() {
    }
	
    public IoTMeasurementWithTime(String time, String platformId, String device, String observation) {
    	this.time = time;
    	this.platformId = platformId;
        this.device = device;
        this.observation = observation;
    }
    
    
	public String getPlatformId() {
		return platformId;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
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
