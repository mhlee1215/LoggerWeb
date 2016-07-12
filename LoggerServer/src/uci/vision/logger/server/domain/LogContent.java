package uci.vision.logger.server.domain;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.ServletRequestUtils;

public class LogContent {
	String id;
	String filename;
	String category;
	String isvalid;
	String date;
	String transmitted;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getIsvalid() {
		return isvalid;
	}
	public void setIsvalid(String isvalid) {
		this.isvalid = isvalid;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTransmitted() {
		return transmitted;
	}
	public void setTransmitted(String transmitted) {
		this.transmitted = transmitted;
	}
	
	@Override
	public String toString() {
		return "{\"id\":\"" + id + "\",\"filename\":\"" + filename + "\",\"category\":\"" + category
				+ "\",\"isvalid\":\"" + isvalid + "\",\"date\":\"" + date + "\",\"transmitted\":\"" + transmitted
				+ "\"}";
	}
	
	public static LogContent readFromRequest(HttpServletRequest request){
		LogContent lc = new LogContent();
		for(Field f : LogContent.class.getDeclaredFields()){
			String v = ServletRequestUtils.getStringParameter(request, f.getName(), "");
			try {
				v = URLDecoder.decode(v, "UTF-8");
				LogContent.class.getDeclaredMethod("set"+uppercaseFirst(f.getName()), String.class).invoke(lc, v);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return lc;
	}
	
	public static String uppercaseFirst(String a){
		if(a.length() < 2) return a.toUpperCase();
		return a.substring(0, 1).toUpperCase()+a.substring(1);
	}
	
	public String serialize(){
		return serialize(true);
	}
	
	public String serialize(boolean encode){
		if(!encode) return "?filename="+filename+"&category="+category+"&isvalid="+isvalid+"&transmitted="+transmitted;
		
		String s = "";
		try {
			s = "?filename="+URLEncoder.encode(filename, "UTF-8")+"&category="+URLEncoder.encode(category, "UTF-8")+
			"&isvalid="+URLEncoder.encode(isvalid, "UTF-8")+"&transmitted="+URLEncoder.encode(transmitted, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return s;
		
	}
}
