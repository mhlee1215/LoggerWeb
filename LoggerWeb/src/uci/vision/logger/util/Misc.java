package uci.vision.logger.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Misc {
	public static String getIPAddress(){
		try {
		       String line;
		       Process p = Runtime.getRuntime().exec( "hostname -I" );

		       BufferedReader in = new BufferedReader(
		               new InputStreamReader(p.getInputStream()) );
		       while ((line = in.readLine()) != null) {
		         return line;
		       }
		       in.close();
		     }
		     catch (Exception e) {
		       // ...
		     }

		return "";

	}
}
