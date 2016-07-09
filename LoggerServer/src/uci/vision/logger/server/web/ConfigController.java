package uci.vision.logger.server.web;

import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import uci.vision.logger.server.service.LogConfigService;


@Controller
public class ConfigController {

	private Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private final LogConfigService logConfigService = null;
	
	@RequestMapping("/readValue.do")
	public @ResponseBody String readValue(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String key = ServletRequestUtils.getStringParameter(request, "key", "");
		key = URLDecoder.decode(key, "UTF-8");
		return logConfigService.readValue(key);
	}
	
	@RequestMapping("/deleteValue.do")
	public @ResponseBody String deleteValue(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String key = ServletRequestUtils.getStringParameter(request, "key", "");
		key = URLDecoder.decode(key, "UTF-8");
		logConfigService.deleteValue(key);
		return "success";
	}
	
	@RequestMapping("/createValue.do")
	public @ResponseBody String createValue(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String key = ServletRequestUtils.getStringParameter(request, "key", "");
		String value = ServletRequestUtils.getStringParameter(request, "value", "");
		key = URLDecoder.decode(key, "UTF-8");
		value = URLDecoder.decode(value, "UTF-8");
		logConfigService.createValue(key, value);
		return "success";
	}
	
	@RequestMapping("/updateValue.do")
	public @ResponseBody String updateValue(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String key = ServletRequestUtils.getStringParameter(request, "key", "");
		String value = ServletRequestUtils.getStringParameter(request, "value", "");
		key = URLDecoder.decode(key, "UTF-8");
		value = URLDecoder.decode(value, "UTF-8");
		logConfigService.updateValue(key, value);
		return "success";
	}
	
	@RequestMapping("/syncValue.do")
	public @ResponseBody String syncValue(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String key = ServletRequestUtils.getStringParameter(request, "key", "");
		String value = ServletRequestUtils.getStringParameter(request, "value", "");
		key = URLDecoder.decode(key, "UTF-8");
		value = URLDecoder.decode(value, "UTF-8");
		
		String v = logConfigService.readValue(key);
		
		//If there is no key/value
		//Then, add
		if(v == null){
			logConfigService.createValue(key, value);
		}
		//Otherwise, update
		else{
			logConfigService.updateValue(key, value);
		}
		return "success";
	}
}
