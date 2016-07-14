package uci.vision.logger.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Misc {
	public static String getIPAddress(){
		//InetAddress ip;
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		return "";

	}
}
