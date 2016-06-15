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

/**
 * 
 * @author mhlee
 *
 * /etc/udev/rules.d/myEmotimo.rules
 * ACTION=="add", KERNEL=="ttyACM[0-9]*", ATTRS{idVendor}=="2341", ATTRS{idProduct}=="0042", MODE="0666"

 *
 */

public class SerialTest implements SerialPortEventListener {
	SerialPort serialPort;
        /** The port we're normally going to use. */
	private static final String PORT_NAMES[] = { 
			"/dev/tty.usbserial-A9007UX1", // Mac OS X
                        "/dev/ttyACM0", // Raspberry Pi
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

	public void initialize() {
                // the next line is for Raspberry Pi and 
                // gets us into the while loop and was suggested here was suggested http://www.raspberrypi.org/phpBB3/viewtopic.php?f=81&t=32186
                System.setProperty("gnu.io.rxtx.SerialPorts", "/dev/ttyACM0");

		CommPortIdentifier portId = null;
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
		

		//First, Find an instance of serial port as set in PORT_NAMES.
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
			System.out.println(currPortId.getName());
			for (String portName : PORT_NAMES) {
				if (currPortId.getName().equals(portName)) {
					portId = currPortId;
					break;
				}
			}
		}
		if (portId == null) {
			System.out.println("Could not find COM port.");
			return;
		}

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
			
			(new Thread(new SerialWriter(output))).start();
			(new Thread(new SerialReader(input))).start();
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}
	
	
	/** */
    public static class SerialWriter implements Runnable 
    {
    	Scanner scanner;
        OutputStream out;
        
        public SerialWriter ( OutputStream out )
        {
            this.out = out;
            scanner = new Scanner(System.in);
        }
        
        public void run ()
        {
            try
            {                
                //int c = 0;
                System.out.print("writer :");
                String str;
                while ( ( str = scanner.nextLine()) != null )
                {
                	str = str + "\r\n";
                	System.out.println(str);
                	//str = "hi\r\n";
                    this.out.write(str.getBytes());
                    System.out.print("writer :");
                }                
            }
            catch ( IOException e )
            {
                e.printStackTrace();
            }            
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
                    System.out.println("kk"+line);
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

	public static void main(String[] args) throws Exception {
		SerialTest main = new SerialTest();
		main.initialize();
////		Thread t=new Thread() {
////			public void run() {
////				//the following line will keep this app alive for 1000 seconds,
////				//waiting for events to occur and responding to them (printing incoming messages to console).
////				try {Thread.sleep(1000000);} catch (InterruptedException ie) {}
////			}
////		};
//		//t.start();
//		System.out.println("Started1123123");
//		
		
//		StringBuffer a = new StringBuffer();
//		a.append("a");
//		a.append("b");
//		
//		System.out.println(a);
		
	}
}