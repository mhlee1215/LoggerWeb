package uci.vision.logger.server.domain;

public class Date {
	String date;
	int size;
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	
	@Override
	public String toString() {
		return "{\"date\":\"" + date + "\",\"size\":\"" + size + "\"}";
	}
}
