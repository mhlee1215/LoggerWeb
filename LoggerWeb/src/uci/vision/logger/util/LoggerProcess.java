package uci.vision.logger.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;

public class LoggerProcess {
	//String command = "~/cvLogger";
	//String command = "~/Downloads/Logger_libfreenect_custom/bin/freenect-cvdemo";
	//String[] command = {System.getProperty("user.home")+"/cvLogger "+System.getProperty("user.home")};
	String command = System.getProperty("user.home")+"/LoggerHome/cvLogger";
	String ftpCommand = System.getProperty("user.home")+"/LoggerHome/ftp.sh";
	
	ProcessBuilder processBuilder;
	Process process;
	
	Thread outputReader;
	static String log;
	
	int isRGB2BGR = 1;
	int isUpsideDown = 1;
	int isViewer = 0;
	int isRecording = 1;
	
	boolean isStarted = false;
	boolean isInitialized  = false;
	//boolean isTransferStarted = false;
	
	SimpleDateFormat time_formatter;
	String current_time_str;
	
	public static int STATE_TRANSFER_INIT = 1;
	public static int STATE_TRANSFER_SENDING = 2;
	public static int STATE_TRANSFER_FINISHED = 3;
	
	public int transferState = STATE_TRANSFER_INIT;
	
	public static String LOG_PREFIX_DEFAULT = "NONAME";
	
	String logPrefix = LOG_PREFIX_DEFAULT;
	
	public LoggerProcess(){
		time_formatter = new SimpleDateFormat("yyyy-MM-dd_HH_mm");
		
		init();
		log = "";
		
	}	
	
	public String getLogPrefix() {
		return logPrefix;
	}

	public void setLogPrefix(String logPrefix) {
		this.logPrefix = logPrefix;
		log += "logPrefix was changed as :"+logPrefix;
	}

	public boolean init(){
		return init(false);
	}
	public boolean init(boolean isPreRunning){
		
		log += "Previous preview file was deleted.\n";
		processBuilder = new ProcessBuilder("rm", System.getProperty("user.home")+"/LoggerHome/capture/*.jpg");
		try {
			processBuilder.start().waitFor();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(isPreRunning){
			processBuilder = new ProcessBuilder(command, System.getProperty("user.home")+"/LoggerHome", isRGB2BGR+"", isUpsideDown+"", "0", "0");
			log += "executed: "+command+" "+System.getProperty("user.home")+"/LoggerHome"+" "+isRGB2BGR+""+" "+isUpsideDown+""+"0\n";	
		}else{
			
			current_time_str = time_formatter.format(System.currentTimeMillis());
			current_time_str = logPrefix +"-"+ current_time_str;
			processBuilder = new ProcessBuilder(command, System.getProperty("user.home")+"/LoggerHome", isRGB2BGR+"", isUpsideDown+"", isViewer+"", isRecording+"", current_time_str);
			log += "executed: "+command+" "+System.getProperty("user.home")+"/LoggerHome"+" "+isRGB2BGR+""+" "+isUpsideDown+" "+isViewer+" "+isRecording+" "+current_time_str+"\n";
		}
		return true;
	}	
	
	public void startLogger(){
		startLogger(true);
	}
	
	public void startLogger(boolean isPreRunning){
		System.out.println("I am startLogger:"+isStarted);
		if(!isStarted){
			try {
				init(isPreRunning);
				log += "Logger started.";
				process = processBuilder.start();
				
				BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
				outputReader = (new Thread(new DepthLoggerReader(input)));
				outputReader.start();
				
				if(isPreRunning){
					isInitialized = true;
				}
				
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
		stopLogger(false);
	}
	
	public void stopLogger(boolean isPreRunning){
		if(isStarted){
			try {
				isStarted = false;	
				
				log += "Logger stopped.";
				if(process!=null){
					Runtime.getRuntime().exec("kill -2 "+getPid(process));	
				}
				Thread.sleep(1000);
				process.destroy();
				outputReader.stop();
				//outputReader.stop();
			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log += getExceptionStackMessage(e);
			}
					
		}
	}
	
	public void transferToServer(){
		//If it is actual recording, transfer file to server automatically
				
		try {
			//current_time_str = time_formatter.format(System.currentTimeMillis())+".klg";
			processBuilder = new ProcessBuilder(ftpCommand, System.getProperty("user.home")+"/LoggerHome/capture/"+current_time_str+".klg");
			log += "executed: "+ftpCommand+" "+System.getProperty("user.home")+"/LoggerHome/capture/"+current_time_str+".klg"+"\n";
			log += "Transfer started.";
			process = processBuilder.start();
			
			//isTransferStarted = true;
			transferState = STATE_TRANSFER_SENDING;
			
			BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
			outputReader = (new Thread(new FTPReader(input)));
			outputReader.start();
			
			System.out.println("AM I wait?");
			process.waitFor();
			//Thread.sleep(5000);
			System.out.println("I AM WAIT!");
			
			//isTransferStarted = false;
			transferState = STATE_TRANSFER_FINISHED;
									
			//System.out.println("KilleD!!!"+getPid(process));
			//Runtime.getRuntime().exec("kill -2 "+getPid(process));
			//process.destroyForcibly();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log += getExceptionStackMessage(e);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	
	public void waitUntilTransferFinished(){
		while(transferState != STATE_TRANSFER_FINISHED){
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		transferState = STATE_TRANSFER_INIT;
	}
	
	public static void main(String[] args){
		
		
//		LoggerProcess lp = new LoggerProcess();
//		lp.startLogger();
//		
//		
//		
//		try {
//			Thread.sleep(10000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			log += getExceptionStackMessage(e);
//		}
//		
//		lp.stopLogger();
	}
	
	public String getLog(){
		return log;
	}
	
	public void flushLog(){
		log = "";
	}
	
	/** */
	class DepthLoggerReader implements Runnable 
	{
		BufferedReader in;
		boolean isStop = false;
	    
	    public DepthLoggerReader ( BufferedReader in )
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
	
	class FTPReader implements Runnable 
	{
		BufferedReader in;
		boolean isStop = false;
	    
	    public FTPReader ( BufferedReader in )
	    {
	        this.in = in;
	    }
	    
	    public void run ()
	    {
	    	System.out.println("FTP Reader started.");
	        //byte[] buffer = new byte[1024];
	        //int len = -1;
	        String line;
	        try
	        {
	        	System.out.println("wait..");
	            while ( (line = in.readLine()) != null )
	            {
	                System.out.println(line);
	                log += line+"\n";
	                if("end".equalsIgnoreCase(line)){
	                	System.out.println("FTP, meets end string");
	                	//isTransferStarted = false;
	                	
	                }
	            }
	        }
	        catch ( IOException e )
	        {
	            e.printStackTrace();
	            log += getExceptionStackMessage(e);
	        }          
	        System.out.println("FTP Reader terminated");
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


	public boolean isStarted() {
		return isStarted;
	}


	public void setStarted(boolean isStarted) {
		this.isStarted = isStarted;
	}


	public boolean isInitialized() {
		return isInitialized;
	}
}



