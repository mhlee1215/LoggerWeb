package uci.vision.logger.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;

public class LoggerProcess {
	//String command = "~/cvLogger";
	//String command = "~/Downloads/Logger_libfreenect_custom/bin/freenect-cvdemo";
	//String[] command = {System.getProperty("user.home")+"/cvLogger "+System.getProperty("user.home")};
	String command = System.getProperty("user.home")+"/cvLogger";
	
	ProcessBuilder processBuilder;
	Process process;
	
	Thread outputReader;
	static String log;
	
	public LoggerProcess(){
		processBuilder = new ProcessBuilder(command, System.getProperty("user.home"));
		log = "";
	}
	
	public void startLogger(){
		try {
			log += "Logger started.";
			process = processBuilder.start();
			
			BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
			outputReader = (new Thread(new SerialReader(input)));
			outputReader.start();
			
			
			//System.out.println("KilleD!!!"+getPid(process));
			//Runtime.getRuntime().exec("kill -2 "+getPid(process));
			//process.destroyForcibly();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log += getExceptionStackMessage(e);
		}
	}
	
	public static String getExceptionStackMessage(Exception e){
		StringWriter errors = new StringWriter();
		e.printStackTrace(new PrintWriter(errors));
		return errors.toString();
	}
	
	public void stopLogger(){
		try {
			log += "Logger stopped.";
			if(process!=null){
				Runtime.getRuntime().exec("kill -2 "+getPid(process));	
			}
			//outputReader.stop();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log += getExceptionStackMessage(e);
		}
	}
	
	public static int getPid(Process process) {
	    try {
	        Class<?> cProcessImpl = process.getClass();
	        Field fPid = cProcessImpl.getDeclaredField("pid");
	        if (!fPid.isAccessible()) {
	            fPid.setAccessible(true);
	        }
	        return fPid.getInt(process);
	    } catch (Exception e) {
	    	log += getExceptionStackMessage(e);
	        return -1;
	    }
	}
	
	public static void main(String[] args){
		LoggerProcess lp = new LoggerProcess();
		lp.startLogger();
		
		
		
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log += getExceptionStackMessage(e);
		}
		
		lp.stopLogger();
	}
	
	public String getLog(){
		return log;
	}
	
	public void flushLog(){
		log = "";
	}
	
	/** */
	class SerialReader implements Runnable 
	{
		BufferedReader in;
		boolean isStop = false;
	    
	    public SerialReader ( BufferedReader in )
	    {
	        this.in = in;
	    }
	    
	    public void run ()
	    {
	    	System.out.println("Reader started.");
	        byte[] buffer = new byte[1024];
	        //int len = -1;
	        String line;
	        try
	        {
	        	System.out.println("wait..");
	            while ( (line = in.readLine()) != null )
	            {
	                System.out.println(line);
	                log += line+"\n";
	            }
	        }
	        catch ( IOException e )
	        {
	            e.printStackTrace();
	            log += getExceptionStackMessage(e);
	        }          
	        System.out.println("Reader terminated");
	    }
	}
	
}



