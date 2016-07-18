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

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import uci.vision.logger.domain.LogConfig;
import uci.vision.logger.domain.LogContent;
import uci.vision.logger.domain.LogContentBin;
import uci.vision.logger.util.HttpThread;

public class ContentService {
	
	
	public static List<LogContent> readContents(){
		return readContents(new LogContent());
	}
	public static List<LogContent> readContents(LogContent lc){
		//WordBook wordbook = null;
		HttpClient httpclient = new DefaultHttpClient();
		ArrayList<LogContent> logContent = null;
		
		try{
			String configHost = ConfigService.readHost();
			
			System.out.println(configHost + "readContents.do" + lc.serialize());
			InputStream in = new URL(configHost + "readContents.do"
					+ lc.serialize())
					.openStream();
			JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
			Gson gson = new Gson();
			LogContentBin logContentBin = gson.fromJson(reader,	LogContentBin.class);
			logContent = (ArrayList<LogContent>) logContentBin.getLogContents();

		}catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
		return logContent;
	}
	
	public static String syncContents(LogContent lc){

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
		
		 String configHost = LogConfig.readLogConfig().readLogConfig().getConfigHost();
	    //if(configHost.isEmpty()) configHost = DEFAULT_CONFIG_SERVER;
	    new HttpThread(configHost + "syncContents.do"+lc.serialize()).start();
		
//		HttpClient httpclient = new DefaultHttpClient();
//		try {
//			String configHost = "http://localhost:8081/LoggerServer/";//LogConfig.readLogConfig().readLogConfig().getConfigHost();
//			HttpGet httpget = new HttpGet(configHost + "syncContents.do"+lc.serialize());
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
		
//		LogContent lc = new LogContent();
//		System.out.println(ContentService.readContents(lc));

		LogContent lc = new LogContent();
		lc.setFilename("service test1");
		lc.setCategory("service category");
		lc.setIsvalid("Y");
		lc.setTransmitted("Y");
		ContentService.syncContents(lc);
		
		System.out.println(ContentService.readContents());
		
	}

}
