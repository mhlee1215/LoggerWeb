package uci.vision.logger.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;

import uci.vision.logger.util.SerialComm.SerialReader;

public class ProcessBuilderTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String command = "/home/mhlee/Downloads/Logger_libfreenect_custom/bin/freenect-cvdemo";
		
		ProcessBuilder processBuilder = new ProcessBuilder(command);

	    try {
			Process process = processBuilder.start();
			
			BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
			(new Thread(new SerialReader(input))).start();
			
			
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println("KilleD!!!"+getPid(process));
			Runtime.getRuntime().exec("kill -2 "+getPid(process));
			//process.destroyForcibly();
			
		} catch (IOException e) {
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
	        return -1;
	    }
	}
	
	/** */
    public static class SerialReader implements Runnable 
    {
    	BufferedReader in;
        
        public SerialReader ( BufferedReader in )
        {
            this.in = in;
        }
        
        public void run ()
        {
        	System.out.println("is Running!");
            byte[] buffer = new byte[1024];
            //int len = -1;
            String line;
            try
            {
            	System.out.println("wait..");
                while ( (line = in.readLine()) != null )
                {
                    System.out.println(line);
                }
            }
            catch ( IOException e )
            {
                e.printStackTrace();
            }          
            System.out.println("is Running!???");
        }
    }

}
