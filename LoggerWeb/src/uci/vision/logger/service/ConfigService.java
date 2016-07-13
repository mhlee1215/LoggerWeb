package uci.vision.logger.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import uci.vision.logger.domain.LogConfig;
import uci.vision.logger.util.HttpThread;

public class ConfigService {
	
	
	public static String readValue(String key){

		String enKey="";
		try {
			enKey = URLEncoder.encode(key, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		HttpClient httpclient = new DefaultHttpClient();
		try {
			String configHost = LogConfig.readLogConfig().readLogConfig().getConfigHost();
			HttpGet httpget = new HttpGet(configHost + "readValue.do?key="+ enKey);

			System.out.println("executing request " + httpget.getURI());

			HttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				BufferedReader rd = new BufferedReader(new InputStreamReader(
						response.getEntity().getContent()));

				String line = "";
				while ((line = rd.readLine()) != null) {
					return line;
				}
			}
			httpget.abort();
			httpclient.getConnectionManager().shutdown();

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
		return "";
	}
	
//	public static String syncValue(String key, String value){
//
//		String enKey="";
//		String enValue = "";
//		try {
//			enKey = URLEncoder.encode(key, "UTF-8");
//			enValue = URLEncoder.encode(value, "UTF-8");
//		} catch (UnsupportedEncodingException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		
//		HttpClient httpclient = new DefaultHttpClient();
//		try {
//			String configHost = LogConfig.readLogConfig().readLogConfig().getConfigHost();
//			HttpGet httpget = new HttpGet(configHost + "syncValue.do?key="+ enKey+"&value="+enValue);
//
//			System.out.println("executing request " + httpget.getURI());
//
//			HttpResponse response = httpclient.execute(httpget);
//			HttpEntity entity = response.getEntity();
//
//			if (entity != null) {
//				BufferedReader rd = new BufferedReader(new InputStreamReader(
//						response.getEntity().getContent()));
//
//				String line = "";
//				while ((line = rd.readLine()) != null) {
//					return line;
//				}
//			}
//			httpget.abort();
//			httpclient.getConnectionManager().shutdown();
//
//		} catch (ClientProtocolException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			httpclient.getConnectionManager().shutdown();
//		}
//		return "";
//	}
	
	public static String syncValue(String key, String value){
	    String enKey="";
	    String enValue = "";
	    try {
	      enKey = URLEncoder.encode(key, "UTF-8");
	      enValue = URLEncoder.encode(value, "UTF-8");
	    } catch (UnsupportedEncodingException e1) {
	      // TODO Auto-generated catch block
	      e1.printStackTrace();
	    }
	    
	    String configHost = LogConfig.readLogConfig().readLogConfig().getConfigHost();
	    //if(configHost.isEmpty()) configHost = DEFAULT_CONFIG_SERVER;
	    new HttpThread(configHost + "syncValue.do?key="+ enKey+"&value="+enValue).start();
	    
//	    HttpClient httpclient = new DefaultHttpClient();
//	    try {
//	      String configHost = LogConfig.readLogConfig().readLogConfig().getConfigHost();
//	      HttpGet httpget = new HttpGet(configHost + "syncValue.do?key="+ enKey+"&value="+enValue);
	//
//	      System.out.println("executing request " + httpget.getURI());
	//
//	      HttpResponse response = httpclient.execute(httpget);
//	      HttpEntity entity = response.getEntity();
	//
//	      if (entity != null) {
//	        BufferedReader rd = new BufferedReader(new InputStreamReader(
//	            response.getEntity().getContent()));
	//
//	        String line = "";
//	        while ((line = rd.readLine()) != null) {
//	          return line;
//	        }
//	      }
//	      httpget.abort();
//	      httpclient.getConnectionManager().shutdown();
	//
//	    } catch (ClientProtocolException e) {
//	      e.printStackTrace();
//	    } catch (IOException e) {
//	      e.printStackTrace();
//	    } finally {
//	      httpclient.getConnectionManager().shutdown();
//	    }
	    return "";
	  }
	  
	
	public static void main(String[] args){
		//System.out.println(UserService.getUsers());
//		User user = new User();
//		user.setName("Aiden");
//		user.setEmail("aiden@gmail.com");
//		user.setPassword("Test Password1111");
//		try {
//			System.out.println(UserService.addUser(user));
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
			System.out.println(ConfigService.readValue("test"));
		
	}

}

