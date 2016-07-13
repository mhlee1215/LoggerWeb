package uci.vision.logger.domain;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.ServletRequestUtils;

import uci.vision.logger.util.LoggerProcess;

public class LogContent implements Comparable<LogContent>{
	
	String id = "";
	String filename = "";
	String category = "";
	String isvalid = "";
	String date = "";
	String transmitted = "";
	String type = "";
	
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public LogContent(){
		
	}
	
	public LogContent(String filename){
		this.filename=filename;
	}
	
	public static LogContent getInstance(String filename){
		return getInstance(filename, "", "");
	}
	
	public static LogContent getInstance(String filename, String category, String type){
		LogContent lc = new LogContent();
		lc.setFilename(filename);
		lc.setTransmitted("N");
		lc.setCategory(category);
		lc.setDate(LoggerProcess.time_formatter.format(System.currentTimeMillis()));
		lc.setIsvalid("Y");
		lc.setType(type);
		return lc;
	}
	
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
	
	public String toString(String delimeter){
		return id+delimeter+filename+delimeter+category+delimeter+isvalid+delimeter+date+delimeter+transmitted+delimeter+type;
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
		if(!encode) return "?filename="+filename+"&category="+category+"&isvalid="+isvalid+"&date="+date+"&transmitted="+transmitted+"&type="+type;
		
		String s = "";
		try {
			s = "?filename="+URLEncoder.encode(filename, "UTF-8")+"&category="+URLEncoder.encode(category, "UTF-8")+
			"&isvalid="+URLEncoder.encode(isvalid, "UTF-8")+"&date="+URLEncoder.encode(date, "UTF-8")+"&transmitted="+URLEncoder.encode(transmitted, "UTF-8")+"&type="+URLEncoder.encode(type, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return s;
		
	}
	
	
	
	public static void main(String[] args){
		LogContent a = new LogContent();
		LogContent b = new LogContent();
		LogContent c = new LogContent();
		LogContent d = new LogContent();
		a.setFilename("aaa");
		b.setFilename("aab");
		b.setId("1");
		c.setFilename("aac");
		d.setFilename("aab");
		
		List<LogContent> lcList = new ArrayList<LogContent>();
		lcList.add(a);
		lcList.add(b);
		lcList.add(c);
		
		
		System.out.println(lcList);
		
		lcList.remove(d);
		System.out.println(lcList);
		
	}
	
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if(!(obj instanceof LogContent)) return false;
		else{
			LogContent lc = (LogContent)obj;
			if("".equals(lc.getFilename()) && "".equals(getFilename())) return false; 
			if(lc.getFilename().equals(this.filename)) return true;
			else return false;
		}
	}

	@Override
	public int compareTo(LogContent o) {
		// TODO Auto-generated method stub
		if(id.isEmpty()) return 1;
		else if(o.getId().isEmpty()) return 0;
		else{
			return Integer.parseInt(id) - Integer.parseInt(o.getId());	
		}
		
	}
	
	
	
}
