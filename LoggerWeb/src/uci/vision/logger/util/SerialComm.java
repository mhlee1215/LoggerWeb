package uci.vision.logger.util;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import gnu.io.CommPortIdentifier; 
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent; 
import gnu.io.SerialPortEventListener;

import java.util.Enumeration;
import java.util.Scanner;
import java.lang.reflect.Field;

/**
 * 
 * @author mhlee
 *
 * /etc/udev/rules.d/myEmotimo.rules
 * ACTION=="add", KERNEL=="ttyACM[0-9]*", ATTRS{idVendor}=="2341", ATTRS{idProduct}=="0042", MODE="0666"

 *
 */

public class SerialComm implements SerialPortEventListener {
	
	public static final int CONN_STATE_FAIL = 0;
	public static final int CONN_STATE_SUCCESS = 1;
	public static final int CONN_STATE_ALREADY_CONNECTED = 2;
	
	static SerialPort serialPort = null;
        /** The port we're normally going to use. */
	private static final String PORT_NAMES[] = { 
			"/dev/tty.usbserial-A9007UX1", // Mac OS X
                        "/dev/ttyACM", // Raspberry Pi
			"/dev/ttyUSB0", // Linux
			"COM3", // Windows
	};
	/**
	* A BufferedReader which will be fed by a InputStreamReader 
	* converting the bytes into characters 
	* making the displayed results codepage independent
	*/
	private BufferedReader input;
	/** The output stream to the port */
	private OutputStream output;
	/** Milliseconds to block while waiting for port open */
	private static final int TIME_OUT = 2000;
	/** Default bits per second for COM port. */
	private static final int DATA_RATE = 57600;
	
	private static String EMO_out;
	private static  String log;
	private static final int EMOTIMO_POS_UNDEFINED = -999999;

	//private static int curPortIdx = 0;
	
	private int moveStep = 500;
	private int pulse1 = 5000;
	private int pulse2 = 5000;
	
	private boolean isInitialized = false;
	
	static String connPortName = "";
	
	public int initialize(){
		System.setProperty( "java.library.path", "/usr/lib/jni" );

		Field fieldSysPath;
		try {
			fieldSysPath = ClassLoader.class.getDeclaredField( "sys_paths" );
			fieldSysPath.setAccessible( true );
			fieldSysPath.set( null, null );
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		
		return initialize(0);
	}
	
	public int initialize(int curPortIndex) {
		if(serialPort!= null){
			System.out.println("Already Initalized");
			return CONN_STATE_ALREADY_CONNECTED;
		}
		if(curPortIndex > 10){
			System.out.println("Totally Could not find COM port.");
			return CONN_STATE_FAIL;
		}
                // the next line is for Raspberry Pi and 
                // gets us into the while loop and was suggested here was suggested http://www.raspberrypi.org/phpBB3/viewtopic.php?f=81&t=32186
		//java -Djava.library.path=/usr/lib/jni -cp /usr/share/java/RXTXcomm.jar:. TwoWaySerialComm
		
		
		//System.out.println(">>>>"+System.getProperty("java.library.path"));
        System.setProperty("gnu.io.rxtx.SerialPorts", "/dev/ttyACM"+curPortIndex);

		CommPortIdentifier portId = null;
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
		
		
		//First, Find an instance of serial port as set in PORT_NAMES.
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
			System.out.println(currPortId.getName());
			for (String portName : PORT_NAMES) {
				if (currPortId.getName().startsWith(portName)) {
					portId = currPortId;
					break;
				}
			}
		}
		if (portId == null) {
			//System.out.println("Could not find COM port.");
			return initialize(curPortIndex+1);
		}
		
		connPortName = portId.getName();
		String[] nameParts = connPortName.split("/");
		connPortName = nameParts[nameParts.length-1];
		System.out.println("connPortName:"+connPortName);

		try {
			// open serial port, and use class name for the appName.
			
			//serialPort.close();
			serialPort = (SerialPort) portId.open(this.getClass().getName(),
					TIME_OUT);

			// set port parameters
			serialPort.setSerialPortParams(DATA_RATE,
					SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);
			
			

			// open the streams
			//System.out.println("read...");
			input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
			
			//output.write("hi".getBytes());
			output = serialPort.getOutputStream();

			// add event listeners
			//serialPort.addEventListener(this);
			//serialPort.notifyOnDataAvailable(true);
			
//			System.out.println("write!");
//			output.write("hi\r\n".getBytes());
//			output.write("hi\r\n".getBytes());
			//output.write("hi\r\n".getBytes());
			//while()
			//System.out.println("read..."+input.readLine());
			
			//(new Thread(new SerialWriter(output))).start();
			(new Thread(new SerialReader(input))).start();
		} catch (Exception e) {
			System.err.println(e.toString());
		}
		
		EMO_out = "";
		log = "";	
		
		setPulse(1, pulse1);
		setPulse(2, pulse2);
		
		isInitialized = true;
		
		return CONN_STATE_SUCCESS;
	}
	
	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
		if(serialPort != null){
			output.close();
			input.close();
			serialPort.close();
		}
	}
	
	public int reset(){
		System.out.println("here?");
		isInitialized = false;
		if(serialPort != null){
			serialPort.removeEventListener();
			try {
				System.out.println("stream close!");
				output.close();
				System.out.println("stream close!2");
				System.out.println("stream close!3");
				//input.close();
				System.out.println("stream close!!!4");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println("conn closed!");
			
			serialPort.close();
			
			System.out.println("conn closed!??");
		}
//		ProcessBuilder processBuilder = new ProcessBuilder("rm /var/lock/LCK.."+connPortName);
//		try {
//			
//			processBuilder.start();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		System.out.println("Do Initialize!");
		return initialize();
	}
	
	
	
	
	
	public boolean isInitialized() {
		return isInitialized;
	}

	public void flush(){
		serialWriter("hi");
	}
	
	public void serialWriter(String str){
		str = str + "\r\n";

        try {
			output.write(str.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int getMotorStep(){
		return moveStep;
	}
	
	public void setZeropos(int motor){
		serialWriter("zm "+motor);
	}
	
	public void setPulse(int motor, int pulse){
		
		if(motor == 1){
			pulse1 = pulse;
			serialWriter("pr "+motor+" "+pulse);
		}else if(motor == 2){
			pulse2 = pulse;
			serialWriter("pr "+motor+" "+pulse);
		}
		
	}
	
	public int getPulse(int motor){
		if(motor == 1) return pulse1;
		else if(motor == 2) return pulse2;
		else return -1;
	}
	
	public int getCurPos(int motor){
		if(serialPort == null) return -99999;
		serialWriter("mp "+motor);
		int trial = 0;
		int maxTrial = 20;
		while(true){
			trial++;
			if(trial > maxTrial) break;
			//EMO_out
			
			String[] parts = EMO_out.split(" ");
			if(parts.length == 3){
				if("mp".equals(parts[0]) && (motor+"").equals(parts[1])){
					return Integer.parseInt(parts[2]);
				}	
			}
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return EMOTIMO_POS_UNDEFINED;
	}
	
	public void move(int motor, int direction){
		int curPos = getCurPos(motor);
		serialWriter("mm "+motor+" "+(curPos+moveStep*direction));
	}
	
	public void moveTo(int motor, int pos){
		serialWriter("mm "+motor+" "+(pos));
	}
	
	public void setMoveStep(int step){
		this.moveStep = step;
	}
	
	public void stopAll(){
		serialWriter("sa");
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
                    //System.out.println("kk"+line);
                    EMO_out = line;
                    log+="\n"+line;
                }
            }
            catch ( IOException e )
            {
                e.printStackTrace();
            }          
            System.out.println("is Running!???");
        }
    }

	/**
	 * This should be called when you stop using the port.
	 * This will prevent port locking on platforms like Linux.
	 */
	public synchronized void close() {
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}

	/**
	 * Handle an event on the serial port. Read the data and print it.
	 */
	public synchronized void serialEvent(SerialPortEvent oEvent) {
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				String inputLine=input.readLine();
				System.out.println("???"+inputLine);
			} catch (Exception e) {
				System.err.println(e.toString());
			}
		}
		// Ignore all the other eventTypes, but you should consider the other ones.
	}

	
	public static void flushLog(){
		log = "";
	}
	
	public static String getLog() {
		return log;
	}
	

	public static void main(String[] args) throws Exception {
		SerialComm main = new SerialComm();
		main.initialize();
//		Thread t=new Thread() {
//			public void run() {
//				//the following line will keep this app alive for 1000 seconds,
//				//waiting for events to occur and responding to them (printing incoming messages to console).
//				try {Thread.sleep(1000000);} catch (InterruptedException ie) {}
//			}
//		};
		//t.start();
		//System.out.println("Started1123123");
		
		
	}
}