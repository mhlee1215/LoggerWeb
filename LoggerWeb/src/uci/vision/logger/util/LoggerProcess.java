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
	String command = System.getProperty("user.home")+"/LoggerHome/cvLogger";
	
	ProcessBuilder processBuilder;
	Process process;
	
	Thread outputReader;
	static String log;
	
	int isRGB2BGR = 1;
	int isUpsideDown = 1;
	int isViewer = 0;
	
	boolean isStarted = false;
	
	public LoggerProcess(){
		init();
		log = "";
	}
	
	
	public boolean init(){
		processBuilder = new ProcessBuilder(command, System.getProperty("user.home")+"/LoggerHome", isRGB2BGR+"", isUpsideDown+"");
		log += "executed: "+command+" "+System.getProperty("user.home")+"/LoggerHome"+" "+isRGB2BGR+""+" "+isUpsideDown+""+"\n";
		return true;
	}
	
	public void startLogger(){
		if(!isStarted){
			try {
				init();
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
			isStarted = true;
		}
		
	}
	
	public static String getExceptionStackMessage(Exception e){
		StringWriter errors = new StringWriter();
		e.printStackTrace(new PrintWriter(errors));
		return errors.toString();
	}
	
	public void stopLogger(){
		if(isStarted){
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
			isStarted = false;
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
	
	public void setRGB2BGR(boolean isRGB2BGR){
		if(isRGB2BGR){
			if(this.isRGB2BGR == 0){
				this.isRGB2BGR = 1;
				log += "RGB2BGR enabled.\n";
			}
		}
		else{
			if(this.isRGB2BGR == 1){
				this.isRGB2BGR = 0;
				log += "RGB2BGR disabled.\n";
			}
		}
	}
	
	public void setUpsideDown(boolean isUpsideDown){
		if(isUpsideDown){
			if(this.isUpsideDown == 0){
				this.isUpsideDown = 1;
				log += "UpsideDown enabled.\n";
			}
		}
		else{
			if(this.isUpsideDown == 1){
				this.isUpsideDown = 0;
				log += "UpsideDown disabled.\n";
			}
		}
	}
	
	public boolean getRGB2BGR(){
		if(this.isRGB2BGR == 1)
			return true;
		else
			return false;
	}
	
	public boolean getUpsideDown(){
		if(this.isUpsideDown == 1)
			return true;
		else
			return false;
	}
	
}



