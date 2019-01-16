package dataLake;

import org.influxdb.annotation.Measurement;
import java.lang.annotation.Annotation;
import java.util.concurrent.TimeUnit;

public class DynamicMeasurement implements Measurement{	
	private String name;
	private TimeUnit time;
	 
    public DynamicMeasurement(String name) {
        this.name = name;
        this.time = TimeUnit.MILLISECONDS;
    }
    @Override
    public String name() {
        return name;
    }
    
    @Override
    public TimeUnit timeUnit() {
    	return time;
    }
 
    @Override
    public Class<? extends Annotation> annotationType() {
        return DynamicMeasurement.class;
    }
}
