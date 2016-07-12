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
import uci.vision.logger.server.dao.LogContentDao;
import uci.vision.logger.server.domain.LogContent;



@Service
public class LogContentService  {

	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	private LogContentDao		logContentDao;

	public List<LogContent> readContents(LogContent logContent){
		return logContentDao.readContents(logContent);
	}
	
	public void createContents(LogContent logContent){
		logContentDao.createContents(logContent);
	}
	
	public void deleteContents(LogContent logContent){
		logContentDao.deleteContents(logContent);
	}
	
	public void updateContents(LogContent logContent){
		logContentDao.updateContents(logContent);
	}
	
}
