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

	public static final int LOG_INTERVAL_DEFAULT = 10;
	public static final int LOG_TIMES_DEFAULT = 5;
	public static final String MOVE_PLAN_DEFAULT = "500, 500, 2 5000, 1 -35000, 1 35000, 2 -3000, 1 -35000";

	private static int logInterval = LOG_INTERVAL_DEFAULT;
	private static int logTimes = LOG_TIMES_DEFAULT;
	private static int logCurTimes = 0;
	private static boolean isPlannedLogProgress = false;
	private static String movePlan = MOVE_PLAN_DEFAULT;
	//			serial.moveToAndWait(2, 10000);
	//	serial.moveToAndWait(1, -15000);
	//	serial.moveToAndWait(1, 15000);
	//	serial.moveToAndWait(2, -8000);
	//	serial.moveToAndWait(1, -15000);

	public LoggerController(){
		System.out.println("INITIALIZE!");
		serial.initialize();
	}

	@RequestMapping("/index.do")
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws Exception {

		int apachePort = ServletRequestUtils.getIntParameter(request, "apachePort", 80);
		boolean isRGB2BGR = ServletRequestUtils.getBooleanParameter(request, "isRGB2BGR", depthLogger.getRGB2BGR());
		boolean isUpsideDown = ServletRequestUtils.getBooleanParameter(request, "isUpsideDown", depthLogger.getUpsideDown());
		depthLogger.setRGB2BGR(isRGB2BGR);
		depthLogger.setUpsideDown(isUpsideDown);

		ModelAndView model = new ModelAndView("index");
		model.addObject("apachePort", apachePort);
		model.addObject("isRGB2BGR", depthLogger.getRGB2BGR());
		model.addObject("isUpsideDown", depthLogger.getUpsideDown());
		model.addObject("pulse1", serial.getPulse(1));
		model.addObject("pulse2", serial.getPulse(2));
		model.addObject("motorStep", serial.getMotorStep());
		model.addObject("isInitialized", serial.isInitialized());
		model.addObject("motorPos1", serial.getCurPos(1));
		model.addObject("motorPos2", serial.getCurPos(2));
		model.addObject("isLoggerStarted", depthLogger.isStarted());
		model.addObject("isLoggerInitialized", depthLogger.isInitialized());


		model.addObject("logInterval", logInterval);
		model.addObject("logTimes", logTimes);
		model.addObject("logCurTimes", logCurTimes);
		model.addObject("isPlannedLogProgress", isPlannedLogProgress);
		model.addObject("movePlan", movePlan);
		model.addObject("logPrefix", depthLogger.getLogPrefix());

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
		int serialInitResult = serial.initialize();
		//depthLogger = new LoggerProcess();

		if(serialInitResult == SerialComm.CONN_STATE_SUCCESS || serialInitResult == SerialComm.CONN_STATE_ALREADY_CONNECTED)
			return "success";
		else
			return "fail";
	}

	@RequestMapping("/initLogger.do")
	public @ResponseBody String initLogger(HttpServletRequest request, HttpServletResponse response) throws Exception {

		//depthLogger = new LoggerProcess();
		if(!depthLogger.isInitialized()){

			depthLogger.startLogger(true);		
			Thread.sleep(5000);
			depthLogger.stopLogger();
			Thread.sleep(1000);
			depthLogger.startLogger(true);		
			Thread.sleep(5000);
			depthLogger.stopLogger();
		}

		String log = depthLogger.getLog();
		depthLogger.flushLog();
		return log;
	}
	
	@RequestMapping("/setLogPrefix.do")
	public @ResponseBody String setLogPrefix(HttpServletRequest request, HttpServletResponse response) throws Exception {

		//depthLogger = new LoggerProcess();
		String logPrefix = ServletRequestUtils.getStringParameter(request, "logPrefix", LoggerProcess.LOG_PREFIX_DEFAULT);
		depthLogger.setLogPrefix(logPrefix);
		
		String log = depthLogger.getLog();
		depthLogger.flushLog();
		return log;
	}
	
	

	@RequestMapping("/goToOrigin.do")
	public @ResponseBody String goToOrigin(HttpServletRequest request, HttpServletResponse response) throws Exception {
		serial.moveToAndWait(1, 0);
		serial.moveToAndWait(2, 0);

		String log = SerialComm.getLog();
		SerialComm.flushLog();
		return log;
	}

	@RequestMapping("/doPlannedLogging.do")
	public @ResponseBody String doPlannedLogging(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logInterval = ServletRequestUtils.getIntParameter(request, "logInterval", LOG_INTERVAL_DEFAULT);
		logTimes = ServletRequestUtils.getIntParameter(request, "logTimes", LOG_TIMES_DEFAULT);
		movePlan = ServletRequestUtils.getStringParameter(request, "movePlan", MOVE_PLAN_DEFAULT);
		String logPrefix = ServletRequestUtils.getStringParameter(request, "logPrefix", LoggerProcess.LOG_PREFIX_DEFAULT);
		depthLogger.setLogPrefix(logPrefix);

		isPlannedLogProgress = true;

		try{

			for (int i = 0 ; i < logTimes ; i++){

				
				String[] parts = movePlan.split(",");
				
				System.out.println("movePlan: "+movePlan);
				System.out.println("parts.length : "+parts.length);
				for(int j = 0 ; j < parts.length ; j++){
					String mov = parts[j].trim();
					
					//First two is purse
					if(j < 2){
						int pulse = Integer.parseInt(mov);
						serial.setPulse(j+1, pulse);
						continue;
					}
					
					String[] subParts = mov.split(" ");
					if(subParts.length != 2){
						System.out.println("Move format error");
						break;
					}
					int motorIndex = Integer.parseInt(subParts[0]);
					int motorToPos = Integer.parseInt(subParts[1]);
					System.out.println(motorIndex+" "+motorToPos);
					serial.moveToAndWait(motorIndex, motorToPos);

					if(j == 0){
						boolean isPrerun = false;
						depthLogger.startLogger(isPrerun);
						Thread.sleep(2000);
					}
				}


				System.out.println("Logger stop!");
				depthLogger.stopLogger();
				depthLogger.transferToServer();
				
				if( i < logTimes-1 )
					Thread.sleep(1000*logInterval*60);
				
				logCurTimes++;
			}

		}catch(Exception e){
			isPlannedLogProgress = false;
			return e.toString();
		}

		isPlannedLogProgress = false;
		
		//Set to Origin
		serial.moveToAndWait(1, 0);
		serial.moveToAndWait(2, 0);
		
		String log = depthLogger.getLog();
		depthLogger.flushLog();
		return log;
	}

	@RequestMapping("/logger.do")
	public @ResponseBody String logger(HttpServletRequest request, HttpServletResponse response) throws Exception {
		boolean isStart = ServletRequestUtils.getBooleanParameter(request, "isStart", false);

		if(isStart){
			System.out.println("Logger start!");
			boolean isPrerun = false;
			depthLogger.startLogger(isPrerun);
		}
		else{
			System.out.println("Logger stop!");
			depthLogger.stopLogger();
			depthLogger.transferToServer();
		}

		String log = depthLogger.getLog();
		depthLogger.flushLog();
		return log;
	}

	@RequestMapping("/loggerWaitUntilTransfer.do")
	public @ResponseBody String loggerWaitUntilTransfer(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//wait until transfer finished.
		depthLogger.waitUntilTransferFinished();

		//Thread.sleep(1000);

		String log = depthLogger.getLog();
		depthLogger.flushLog();
		return log;
	}



	@RequestMapping("/loggerSettings.do")
	public @ResponseBody String loggerSettings(HttpServletRequest request, HttpServletResponse response) throws Exception {
		boolean isRGB2BGR = ServletRequestUtils.getBooleanParameter(request, "isRGB2BGR", depthLogger.getRGB2BGR());
		boolean isUpsideDown = ServletRequestUtils.getBooleanParameter(request, "isUpsideDown", depthLogger.getUpsideDown());
		depthLogger.setRGB2BGR(isRGB2BGR);
		depthLogger.setUpsideDown(isUpsideDown);

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

	@RequestMapping("/eMotimoflush.do")
	public @ResponseBody String eMotimoflush(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String log = SerialComm.getLog();
		SerialComm.flushLog();
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

	@RequestMapping("/setPulse.do")
	public @ResponseBody String setPulse(HttpServletRequest request, HttpServletResponse response) throws Exception {

		int motor = ServletRequestUtils.getIntParameter(request, "motor", 0);
		int pulse = ServletRequestUtils.getIntParameter(request, "pulse", 0);

		serial.setPulse(motor, pulse);
		String log = SerialComm.getLog();
		SerialComm.flushLog();
		return log;
	}

	@RequestMapping("/setMotorStep.do")
	public @ResponseBody String setMotorStep(HttpServletRequest request, HttpServletResponse response) throws Exception {

		int step = ServletRequestUtils.getIntParameter(request, "step", 0);

		serial.setMoveStep(step);
		String log = SerialComm.getLog();
		SerialComm.flushLog();
		return log;
	}

	@RequestMapping("/action.do")
	public @ResponseBody String action(HttpServletRequest request, HttpServletResponse response) throws Exception {

		int motor = ServletRequestUtils.getIntParameter(request, "motor", 0);
		int direction = ServletRequestUtils.getIntParameter(request, "direction", 0);
		int pos = ServletRequestUtils.getIntParameter(request, "pos", 0);

		if(direction != 0)
			serial.move(motor, direction);
		else if(pos != 0)
			serial.moveTo(motor, pos);

		String log = SerialComm.getLog();
		SerialComm.flushLog();
		return log;
	}

	@RequestMapping("/getMotorPos.do")
	public @ResponseBody String getMotorPos(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int motor = ServletRequestUtils.getIntParameter(request, "motor", 0);
		return serial.getCurPos(motor)+"";
	}

	@RequestMapping("/stopMotorAll.do")
	public @ResponseBody String stopMotorAll(HttpServletRequest request, HttpServletResponse response) throws Exception {

		serial.stopAll();
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
