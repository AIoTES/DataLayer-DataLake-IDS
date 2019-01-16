package dataLake;

public interface APIService {		
	public void createDB(String messageBodyRequest, String localhost) throws Exception;
	
	public void deleteDB(String messageBodyRequest, String localhost) throws Exception;
	
	public void insertMeasurement(String messageBodyRequest, String localhost) throws Exception;

    public String selectMeasurement(String messageBodyRequest, String localhost) throws Exception;
    
    public void deleteMeasurement(String messageBodyRequest, String localhost) throws Exception;

    public void updateMeasurement(IoTMeasurement measurement, String localhost) /*throws UserException*/;	
    
}
