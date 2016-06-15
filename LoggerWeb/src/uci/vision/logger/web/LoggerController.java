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

import uci.vision.logger.util.SerialComm;





@Controller
public class LoggerController {

	private Logger logger = Logger.getLogger(getClass());
	
	private static SerialComm serial = new SerialComm();
	
	public LoggerController(){
		System.out.println("INITIALIZE!");
		serial.initialize();
	}
	
	@RequestMapping("/index.do")
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws Exception {

		ModelAndView model = new ModelAndView("index");
		
				
		return model;
    }
	
	@RequestMapping("/flush.do")
    public @ResponseBody String flush(HttpServletRequest request, HttpServletResponse response) throws Exception {

		int motor = ServletRequestUtils.getIntParameter(request, "motor", 0);
		int direction = ServletRequestUtils.getIntParameter(request, "direction", 0);
		
		serial.movie(motor, direction);
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
	
	
	
	
}
