package uci.vision.logger.server.domain;

public class Category {
	String category;
	int size;
	
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	@Override
	public String toString() {
		return "{\"category\":\"" + category + "\",\"size\":\"" + size + "\"}";
	}
	
	

}
