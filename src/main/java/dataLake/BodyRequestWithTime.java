package dataLake;

import com.google.gson.annotations.Expose;

public class BodyRequestWithTime {
	@Expose
	private String db;
	@Expose
	private String table;	//measurement
	@Expose
	private IoTMeasurementWithTime data;
	@Expose
	private String query;
	
	
	public String getDb() {
		return db;
	}
	public void setDb(String db) {
		this.db = db;
	}
	public IoTMeasurementWithTime getData() {	
		return data;
	}
	
	public void setData(IoTMeasurementWithTime data) {
		this.data = data;
	}
	
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
}
