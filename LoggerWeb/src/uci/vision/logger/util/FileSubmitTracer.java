package uci.vision.logger.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;


public class FileSubmitTracer {

	InputStream inputStream = null;
	String pathToBeTransmitted = "conf/log.be.transmitted";
	String pathTransmitted = "conf/log.transmitted";
	
	List<String> transmittedList;
	List<String> toBeTransmittedList;
	
	
	public FileSubmitTracer(){
		transmittedList = readList(pathTransmitted);
		toBeTransmittedList = readList(pathToBeTransmitted);
	}
	
	public void writeList(String fileName, List<String> q){
		PrintWriter pw = null;
		
		try {
			pw = new PrintWriter(new FileWriter(fileName));
			
			for(int i = 0 ; i < q.size(); i++){
				//Use like System.out.println
				pw.println(q.get(i));
			}
			pw.flush();
			pw.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sync(){
		writeList(pathTransmitted, transmittedList);
		writeList(pathToBeTransmitted, toBeTransmittedList);
	}
	
	public List<String> readList(String fileName){
		List<String> fileList = new LinkedList<String>();
		
		try {
			FileReader fr = new FileReader(fileName);
			BufferedReader br = new BufferedReader(fr);
			
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
		
		fst.addWork("11");
		fst.addWork("22");
		fst.addWork("33");
		fst.addWork("44");
		
		fst.doFlush();
		fst.doFlush();
		fst.doFlush();
		
		System.out.println(fst);
		
//		fst.addWork("nunnunanna");
//		fst.addWork("nunnunanna1");
//		fst.addWork("nunnunanna3");
	}

}
