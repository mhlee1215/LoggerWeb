package uci.vision.logger.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.ServletRequestUtils;

import uci.vision.logger.domain.LogContent;
import uci.vision.logger.service.ContentService;


public class FileSubmitTracer {

	InputStream inputStream = null;
	String pathToLog = "/fileLog/contentLog.txt";
	//String pathTransmitted = "/fileLog/log.transmitted";
	
	String ActualPath = "";
	//String ActualPatheD = "";
	 
	
	
	Map<String, LogContent> logMap;
	//List<LogContent> toBeTransmittedList;
	
	
	public FileSubmitTracer(){
		URL r = this.getClass().getResource("/");
		ActualPath = r.getPath()+"../.."+pathToLog;
		//ActualPatheD = r.getPath()+"../.."+pathTransmitted;
		
		System.out.println(ActualPath);
		//System.out.println(ActualPathToBe);
		
		logMap = readList(ActualPath);
		//toBeTransmittedList = readList(ActualPathToBe);
		syncFromServerToLocal();
		writeList();
		syncFromLocalToServer();
	}
	
	public void syncFromServerToLocal(){
		List<LogContent> lcList = ContentService.readContents();
		if(lcList != null){
			for(LogContent lc : lcList){
				logMap.put(lc.getFilename(), lc);
			}			
		}
//		System.out.println("BEFORE SYNC:"+lcList);
//		System.out.println("BEFORE SYNC:"+logMap);

//		System.out.println("AFTER SYNC:"+lcList);
//		System.out.println("AFTER SYNC:"+logMap);
	}
	
	
	public void syncFromLocalToServer(){		
		for(String lcName : logMap.keySet()){
			LogContent lc = logMap.get(lcName);
			ContentService.syncContents(lc);
		}
	}
	
	
	
	public void appendLogContent(String fileName, LogContent lc){
		PrintWriter pw = null;
		try {
			
//			URL resourceUrl = getClass().getResource(fileName);
//			File file = new File(resourceUrl.toURI());
//			OutputStream output = new FileOutputStream(fileName);
						
			//pw = new PrintWriter(new OutputStreamWriter(output), true);
			pw = new PrintWriter(new FileWriter(fileName, true));
			pw.println(lc.toString("\t"));
			pw.flush();
			pw.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void writeList(){
		writeList(ActualPath, logMap);
	}
	public void writeList(String fileName, Map<String, LogContent> q){
		PrintWriter pw = null;
		try {
			
//			URL resourceUrl = getClass().getResource(fileName);
//			File file = new File(resourceUrl.toURI());
//			OutputStream output = new FileOutputStream(fileName);
						
			//pw = new PrintWriter(new OutputStreamWriter(output), true);
			pw = new PrintWriter(new FileWriter(fileName));
			
			List<LogContent> lcList = new ArrayList<LogContent>();
			for(String lcName : q.keySet()){
				LogContent lc = q.get(lcName);
				lcList.add(lc);
				//pw.println(lc.toString("\t"));
			}
			
			Collections.sort(lcList);
			
			for(LogContent lc : lcList){
				pw.println(lc.toString("\t"));
			}
//			for(int i = 0 ; i < q.size(); i++){
//				//Use like System.out.println
//				pw.println(q.get(i).toString("\t"));
//			}
			
			pw.flush();
			pw.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sync(){
		writeList(ActualPath, logMap);
		//writeList(ActualPathToBe, toBeTransmittedList);
	}
	
	public Map<String, LogContent> readList(String fileName){
		Map<String, LogContent> logContentMap = new HashMap<>();
		
		try {
			
//			URL resourceUrl = getClass().getResource(path);
//			File file = new File(resourceUrl.toURI());
//			OutputStream output = new FileOutputStream(file);
			
			InputStream is = new FileInputStream(fileName);//getClass().getClassLoader().getResourceAsStream(fileName);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			
			String thisLine = null;
			while ((thisLine = br.readLine()) != null) {
				
				LogContent lc = new LogContent();
				//System.out.println("thisLine:"+thisLine);
				String[] parts = thisLine.split("\t");
				if(parts.length < 7) continue;
				
				int pIdx = 0;
				for(Field f : LogContent.class.getDeclaredFields()){
					
					try {
						String v = parts[pIdx];
						LogContent.class.getDeclaredMethod("set"+LogContent.uppercaseFirst(f.getName()), String.class).invoke(lc, v);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					pIdx++;
				}
				//fileList.add(thisLine);
				logContentMap.put(lc.getFilename(),lc);
			}       
			br.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return logContentMap;
	}
	
	public void addWork(LogContent lc){
		lc.setTransmitted("N");
		//logList.add(lc);
		logMap.put(lc.getFilename(), lc);
		writeList();
		//System.out.println(">>>>>>>>>>>>>>"+lc.toString());
		ContentService.syncContents(lc);
	}
	
	public Map<String, LogContent> getLogMap() {
		return logMap;
	}

	public void setLogMap(Map<String, LogContent> logMap) {
		this.logMap = logMap;
	}

	public List<LogContent> getNextList(){
		List<LogContent> nextList = new ArrayList<LogContent>();
		for(String lcName : logMap.keySet()){
			LogContent lc = logMap.get(lcName);
			if("N".equalsIgnoreCase(lc.getTransmitted()) && "Y".equalsIgnoreCase(lc.getIsvalid()))
				nextList.add(lc);
		}
		return nextList;
	}
	
	
	public void doFinish(LogContent lc){
		
//		System.out.println("BEFORE DO FINISH"+lc);
//		System.out.println("BEFORE DO FINISH"+logMap);
		LogContent lc2 = logMap.get(lc.getFilename());
		lc2.setTransmitted("Y");
		logMap.put(lc2.getFilename(), lc2);
//		System.out.println("AFTER DO FINISH"+lc);
//		System.out.println("AFTER DO FINISH"+logMap);
		
//		logList.remove(lc);
//		lc.setTransmitted("Y");
//		logList.add(lc);
		writeList();
		ContentService.syncContents(lc2);
	}
		
	public String toString(){
		return "log Map: "+logMap;
	}
	 
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		FileSubmitTracer fst = new FileSubmitTracer();
		System.out.println(fst);
//		System.out.println(fst.getNext());
//		fst.doFlush();
//		fst.doFlush();
//		fst.doFlush();
		
//		fst.addWork("11");
//		fst.addWork("22");
//		fst.addWork("33");
//		fst.addWork("44");
//		
//		fst.doFlush();
//		fst.doFlush();
//		fst.doFlush();
		
		System.out.println(fst);
		
//		fst.addWork("nunnunanna");
//		fst.addWork("nunnunanna1");
//		fst.addWork("nunnunanna3");
	}

}
