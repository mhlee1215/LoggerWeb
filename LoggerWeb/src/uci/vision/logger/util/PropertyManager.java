package uci.vision.logger.util;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.DefaultResourceLoader;

import uci.vision.logger.domain.LogConfig;
import uci.vision.logger.service.ConfigService;
 
/**
 * @author Crunchify.com
 * 
 */
 
public class PropertyManager {

	String propFileName = "/conf/config.properties";
	String propActualPath = "";
 
	public PropertyManager(){
		URL r = this.getClass().getResource("/");
		propActualPath = r.getPath()+"../.."+propFileName;
	}
	
	public static PropertyManager getManager(){
		return new PropertyManager();
	}
	
	public LogConfig readConfig() {
		LogConfig lc = new LogConfig();
		
		InputStream inputStream = null;
		try {
			Properties prop = new Properties();
 
			
			inputStream = new FileInputStream(propActualPath);//loader.getResource("classpath:"+propFileName).getInputStream();
	
			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}
 
			
			for(Field f : LogConfig.class.getDeclaredFields()){
				LogConfig.class.getDeclaredMethod("set"+uppercaseFirst(f.getName()), String.class).invoke(lc, prop.getProperty(f.getName()));
			}
	
		} catch (Exception e) {
			//System.out.println("Exception: " + e);
			e.printStackTrace();
		} finally {
			if(inputStream != null)
				try {
					inputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
		return lc;
	}	
	
	public void writeConfig(LogConfig lc){
		//LogConfig lc = new LogConfig();
		
		InputStream inputStream = null;
		try {
			Properties prop = new Properties();
			
			File file = new File(propActualPath);
			OutputStream out = new FileOutputStream(file);
	        
	        for(Field f : LogConfig.class.getDeclaredFields()){
	        	String v = (String) LogConfig.class.getDeclaredMethod("get"+uppercaseFirst(f.getName())).invoke(lc);
	        	prop.setProperty(f.getName(), v);
	        }
	        
			prop.store(out, "LogConfig file. Sync from server to local first.");
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(inputStream != null)
				try {
					inputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
	}
	
	public void syncFromLocalToServer(){
		LogConfig lc = readConfig();
		try {
	        for(Field f : LogConfig.class.getDeclaredFields()){
	        	String v = (String) LogConfig.class.getDeclaredMethod("get"+uppercaseFirst(f.getName())).invoke(lc);
	        	ConfigService.syncValue(f.getName(), v);
	        }
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public LogConfig syncFromServerToLocal(){
		LogConfig lc = new LogConfig();
		try {
	        for(Field f : LogConfig.class.getDeclaredFields()){
	        	String v = ConfigService.readValue(f.getName());
	        	LogConfig.class.getDeclaredMethod("set"+uppercaseFirst(f.getName()), String.class).invoke(lc, v);
	        	
	        }
		} catch (Exception e) {
			e.printStackTrace();
		} 
		writeConfig(lc);
		return lc;
	}
	
	
	public static String uppercaseFirst(String a){
		if(a.length() < 2) return a.toUpperCase();
		return a.substring(0, 1).toUpperCase()+a.substring(1);
	}
	

	public static void main(String[] args){
//		LogConfig lc = PropertyManager.getManager().readConfig();
//		System.out.println(lc);
		//PropertyManager.getManager().syncFromLocalToServer();
		LogConfig lc = PropertyManager.getManager().syncFromServerToLocal();
		System.out.println(lc);
		//lc.setLogPrefix("NONAME1");
		//PropertyManager.getManager().writeConfig(lc);
		
	}
}