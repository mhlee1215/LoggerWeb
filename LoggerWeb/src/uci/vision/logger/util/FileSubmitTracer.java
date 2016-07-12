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
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;


public class FileSubmitTracer {

	InputStream inputStream = null;
	String pathToBeTransmitted = "/fileLog/log.be.transmitted";
	String pathTransmitted = "/fileLog/log.transmitted";
	
	String ActualPathToBe = "";
	String ActualPatheD = "";
	 
	
	
	List<String> transmittedList;
	List<String> toBeTransmittedList;
	
	
	public FileSubmitTracer(){
		URL r = this.getClass().getResource("/");
		ActualPathToBe = r.getPath()+".."+pathToBeTransmitted;
		ActualPatheD = r.getPath()+".."+pathTransmitted;
		
		System.out.println(ActualPatheD);
		System.out.println(ActualPathToBe);
		
		transmittedList = readList(ActualPatheD);
		toBeTransmittedList = readList(ActualPathToBe);
	}
	
	public void writeList(String fileName, List<String> q){
		PrintWriter pw = null;
		
		try {
			
			URL resourceUrl = getClass().getResource(fileName);
			File file = new File(resourceUrl.toURI());
			OutputStream output = new FileOutputStream(fileName);
						
			pw = new PrintWriter(new OutputStreamWriter(output));
			
			for(int i = 0 ; i < q.size(); i++){
				//Use like System.out.println
				pw.println(q.get(i));
			}
			pw.flush();
			pw.close();
			
		} catch (IOException | URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sync(){
		writeList(ActualPatheD, transmittedList);
		writeList(ActualPathToBe, toBeTransmittedList);
	}
	
	public List<String> readList(String fileName){
		List<String> fileList = new LinkedList<String>();
		
		try {
			
//			URL resourceUrl = getClass().getResource(path);
//			File file = new File(resourceUrl.toURI());
//			OutputStream output = new FileOutputStream(file);
			
			InputStream is = new FileInputStream(fileName);//getClass().getClassLoader().getResourceAsStream(fileName);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			
			String thisLine = null;
			while ((thisLine = br.readLine()) != null) {
				fileList.add(thisLine);
			}       
			br.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return fileList;
	}
	
	public String getNext(){
		if(toBeTransmittedList.size() == 0) return null;
		return toBeTransmittedList.get(0);
	}
	
	public void doFlush(){
		if(toBeTransmittedList.size()>0){
			
			transmittedList.add(toBeTransmittedList.get(0));
			toBeTransmittedList.remove(0);
		}
		sync();
	}
	
	public void doFlush(String name){
		transmittedList.add(name);
		toBeTransmittedList.remove(name);
		sync();
	}
	
	public void addWork(String name){
		toBeTransmittedList.add(name);
		sync();
	}
	
	public String toString(){
		return "To be transfer: "+toBeTransmittedList+"\nTransferred:"+transmittedList;
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
