package uci.vision.logger.util;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.Properties;

import uci.vision.logger.domain.LogConfig;
 
/**
 * @author Crunchify.com
 * 
 */
 
public class PropertyManager {

	String propFileName = "conf/config.properties";
 
	public static PropertyManager getManager(){
		return new PropertyManager();
	}
	
	public LogConfig readConfig() {
		LogConfig lc = new LogConfig();
		
		InputStream inputStream = null;
		try {
			Properties prop = new Properties();
 
			inputStream = new FileInputStream(propFileName);//getClass().getClassLoader().getResourceAsStream(propFileName);
 
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
			
 
			//getClass().getClassLoader().
			//File f = new File(propFileName);
	        OutputStream out = new FileOutputStream( propFileName );
 
			 
			// get the property value and print it out
			//lc.setLogPrefix(prop.getProperty("logPrefix"));
	        
	        for(Field f : LogConfig.class.getDeclaredFields()){
	        	String v = (String) LogConfig.class.getDeclaredMethod("get"+uppercaseFirst(f.getName())).invoke(lc);
	        	prop.setProperty(f.getName(), v);
	        }
	        
			prop.store(out, "Optional header!");
			
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
	
	public void updateConfigToServer(LogConfig lc){
		try {
	        for(Field f : LogConfig.class.getDeclaredFields()){
	        	String v = (String) LogConfig.class.getDeclaredMethod("get"+uppercaseFirst(f.getName())).invoke(lc);
	        	ConfigService.syncValue(f.getName(), v);
	        }
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public static String uppercaseFirst(String a){
		if(a.length() < 2) return a.toUpperCase();
		return a.substring(0, 1).toUpperCase()+a.substring(1);
	}

	public static void main(String[] args){
		LogConfig lc = PropertyManager.getManager().readConfig();
		System.out.println(lc);
		PropertyManager.getManager().updateConfigToServer(lc);
		//lc.setLogPrefix("NONAME1");
		//PropertyManager.getManager().writeConfig(lc);
		
	}
}