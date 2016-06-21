package uci.vision.logger.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import uci.vision.logger.util.LoggerProcess;
import uci.vision.logger.util.SerialComm;





@Controller
public class LoggerController {

	private Logger logger = Logger.getLogger(getClass());
	
	private static SerialComm serial = new SerialComm();
	private static LoggerProcess depthLogger = new LoggerProcess();
	
	public LoggerController(){
		System.out.println("INITIALIZE!");
		serial.initialize();
	}
	
	@RequestMapping("/index.do")
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws Exception {

		int apachePort = ServletRequestUtils.getIntParameter(request, "apachePort", 80);
		ModelAndView model = new ModelAndView("index");
		model.addObject("apachePort", apachePort);
				
		return model;
    }
	
	@RequestMapping("/instruction.do")
    public ModelAndView instruction(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView model = new ModelAndView("instruction");				
		return model;
    }
	
	@RequestMapping("/init.do")
    public @ResponseBody String init(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("INITIALIZE!");
		boolean serialInitResult = serial.initialize();
		depthLogger = new LoggerProcess();
		
		if(serialInitResult)
			return "Success";
		else
			return "fail";
    }
	
	@RequestMapping("/logger.do")
    public @ResponseBody String logger(HttpServletRequest request, HttpServletResponse response) throws Exception {
		boolean isStart = ServletRequestUtils.getBooleanParameter(request, "isStart", false);
		
		if(isStart)
			depthLogger.startLogger();
		else
			depthLogger.stopLogger();
		
		String log = depthLogger.getLog();
		depthLogger.flushLog();
		return log;
    }
	
	@RequestMapping("/loggerFlush.do")
    public @ResponseBody String loggerFlush(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String log = depthLogger.getLog();
		depthLogger.flushLog();
		return log;
    }
	
	@RequestMapping("/flush.do")
    public @ResponseBody String flush(HttpServletRequest request, HttpServletResponse response) throws Exception {

		int motor = ServletRequestUtils.getIntParameter(request, "motor", 0);
		int direction = ServletRequestUtils.getIntParameter(request, "direction", 0);
		
		serial.flush();
		serial.getCurPos(1);
		serial.getCurPos(2);
		String log = SerialComm.getLog();
		SerialComm.flushLog();
		return log;
    }
	
	@RequestMapping("/action.do")
    public @ResponseBody String action(HttpServletRequest request, HttpServletResponse response) throws Exception {

		int motor = ServletRequestUtils.getIntParameter(request, "motor", 0);
		int direction = ServletRequestUtils.getIntParameter(request, "direction", 0);
		
		serial.movie(motor, direction);
		String log = SerialComm.getLog();
		SerialComm.flushLog();
		return log;
    }
	
	@RequestMapping("/zeroPos.do")
    public @ResponseBody String zeroPos(HttpServletRequest request, HttpServletResponse response) throws Exception {

		int motor = ServletRequestUtils.getIntParameter(request, "motor", 0);
				
		serial.setZeropos(motor);
		String log = SerialComm.getLog();
		SerialComm.flushLog();
		return log;
    }
	
	
	
	
}
