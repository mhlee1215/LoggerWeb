package uci.vision.logger.service.utils;

import java.io.BufferedReader;
import java.io.IOException;

/** */
	public class ThreadReader implements Runnable 
	{	
		String log;
		BufferedReader in;
		boolean isStop = false;
	    
	    public ThreadReader ( BufferedReader in )
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
	            //log += getExceptionStackMessage(e);
	        }          
	        System.out.println("Reader terminated");
	    }
	}