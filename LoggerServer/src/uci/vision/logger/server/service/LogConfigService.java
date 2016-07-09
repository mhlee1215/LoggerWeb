package uci.vision.logger.server.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.commons.mail.EmailException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import uci.vision.logger.server.dao.LogConfigDao;



@Service
public class LogConfigService  {

	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	private LogConfigDao		logConfigDao;

	public String readValue(String key){
		return logConfigDao.readValue(key);
	}
	
	public void createValue(String key, String value){
		logConfigDao.createValue(key, value);
	}
	
	public void deleteValue(String key){
		logConfigDao.deleteUser(key);
	}
	
	public void updateValue(String key, String value){
		logConfigDao.updateUser(key, value);
	}
	
}
