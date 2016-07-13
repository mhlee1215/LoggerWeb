package uci.vision.logger.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Misc {
	public static String getIPAddress(){
		InetAddress ip;
		try {

			ip = InetAddress.getLocalHost();
			System.out.println("Current IP address : " + ip);
			System.out.println("Current IP address : " + ip.getHostAddress());
			return ip.getHostAddress();
		} catch (UnknownHostException e) {

			e.printStackTrace();

		}

		return "";

	}
}
