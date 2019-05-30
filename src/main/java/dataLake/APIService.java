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
