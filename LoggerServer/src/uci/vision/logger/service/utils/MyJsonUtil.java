package uci.vision.logger.service.utils;

import java.util.List;

public class MyJsonUtil{

	public static String toString(List data, String id) {
		String json = "";
		// TODO Auto-generated method stub
		json += "{";
		json += "\""+id+"\":";
		json += data.toString();
		json += "}";
		return json;
	}
}