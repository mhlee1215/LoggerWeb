package uci.vision.logger.web;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import javax.servlet.ServletContext;
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
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;

import uci.vision.logger.domain.LogConfig;
import uci.vision.logger.service.ConfigService;
import uci.vision.logger.util.FileSubmitTracer;
import uci.vision.logger.util.LoggerProcess;
import uci.vision.logger.util.Misc;
import uci.vision.logger.util.SerialComm;


//#http://54.191.113.175:8080/LoggerServer/


@Controller
public class LoggerController{

	private Logger logger = Logger.getLogger(getClass());

	private static SerialComm serial = null;//new SerialComm();
	private static LoggerProcess depthLogger = null;//new LoggerProcess();

	public static final int LOG_INTERVAL_DEFAULT = 10;
	public static final int LOG_TIMES_DEFAULT = 5;
	public static final String MOVE_PLAN_DEFAULT = "1000, 1000, 2 3000, 1 -80000, 1 80000, 2 -3000, 1 -80000";

	private static int logInterval = LOG_INTERVAL_DEFAULT;
	private static int logTimes = LOG_TIMES_DEFAULT;
	private static int logCurTimes = 0;
	private static boolean isPlannedLogProgress = false;
	private static boolean isTransferProgress = false;
	private static String movePlan = MOVE_PLAN_DEFAULT;
	
	public static final String EMOTIMO_GREETING_MESSAGE = "hi";
	//			serial.moveToAndWait(2, 10000);
	//	serial.moveToAndWait(1, -15000);
	//	serial.moveToAndWait(1, 15000);
	//	serial.moveToAndWait(2, -8000);
	//	serial.moveToAndWait(1, -15000);

	//	@Autowired
	//    ServletContext context; 

	public LoggerController(){
//		System.out.println("INITIALIZE!");
//		serial.initialize();
//		System.out.println("SERVER IP:"+Misc.getIPAddress());
//		ConfigService.syncValue("OdroidIP", Misc.getIPAddress());
//		//System.out.println(context.getRealPath("/"));
//
//		loggerInit();
//		waitUntilMotorSet();
//		
//		LogConfig lc = LogConfig.readLogConfig();
//		if("Y".equalsIgnoreCase(lc.getRecordAfterBoot())){
//			doPlannedAction(depthLogger);
//		}
		try {
			System.out.println("HOSTNAME:"+InetAddress.getLocalHost().getHostName());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new InitThread().start();

	}

	public void waitUntilMotorSet(){
//		int serialInitResult = serial.initialize();
//
//		while(true){
//			if(serialInitResult == SerialComm.CONN_STATE_SUCCESS || serialInitResult == SerialComm.CONN_STATE_ALREADY_CONNECTED)
//				break;
//			else{
//				try {
//					System.out.println("Not connected..");
//					Thread.sleep(500);
//					serialInitResult = serial.initialize();
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}

		
		try {
			String log;
			serial.flush();
			log = SerialComm.getLog();
			Thread.sleep(1000);
			//SerialComm.flushLog();

			while(!log.contains(EMOTIMO_GREETING_MESSAGE)){
				System.out.println("Still Not connected..");
				Thread.sleep(2000);
				serial.flush();
				log = SerialComm.getLog();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RequestMapping("/index.do")
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws Exception {

		System.out.println(request.getSession().getServletContext().getRealPath("/"));

		int apachePort = ServletRequestUtils.getIntParameter(request, "apachePort", 80);
		boolean isRGB2BGR = ServletRequestUtils.getBooleanParameter(request, "isRGB2BGR", depthLogger.getRGB2BGR());
		boolean isUpsideDown = ServletRequestUtils.getBooleanParameter(request, "isUpsideDown", depthLogger.getUpsideDown());
		depthLogger.setRGB2BGR(isRGB2BGR);
		depthLogger.setUpsideDown(isUpsideDown);

		if(!isPlannedLogProgress)
			depthLogger.loadConfig();

		logInterval = depthLogger.getLogInterval();
		logTimes = depthLogger.getLogTimes();
		movePlan = depthLogger.getMovePlan();

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
		model.addObject("isTransferProgress", depthLogger.isTransfering());
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

	public String loggerInit(){
		//depthLogger = new LoggerProcess();
		if(!depthLogger.isInitialized()){

			depthLogger.startLogger(true);		
			try {
				Thread.sleep(5000);
				depthLogger.stopLogger();
				Thread.sleep(1000);
				depthLogger.startLogger(true);		
				Thread.sleep(5000);
				depthLogger.stopLogger();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		String log = depthLogger.getLog();
		depthLogger.flushLog();
		return log;
	}

	@RequestMapping("/initLogger.do")
	public @ResponseBody String initLogger(HttpServletRequest request, HttpServletResponse response) throws Exception {

		//depthLogger = new LoggerProcess();
		//		if(!depthLogger.isInitialized()){
		//
		//			depthLogger.startLogger(true);		
		//			Thread.sleep(5000);
		//			depthLogger.stopLogger();
		//			Thread.sleep(1000);
		//			depthLogger.startLogger(true);		
		//			Thread.sleep(5000);
		//			depthLogger.stopLogger();
		//		}
		//
		//		String log = depthLogger.getLog();
		//		depthLogger.flushLog();
		//		return log;
		return loggerInit();
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

	public String doPlannedAction(LoggerProcess depthLogger){
		int logInterval = depthLogger.getLogInterval();
		int logTimes = depthLogger.getLogTimes();
		String movePlan = depthLogger.getMovePlan();
		
		isPlannedLogProgress = true;
		
		System.out.println("====LOGGING STARTED=========");
		System.out.println("Log Interval: "+logInterval);
		System.out.println("Log Times: "+logTimes);
		System.out.println("movePlan: "+movePlan);
		System.out.println("============================");

		serial.setPulse(1, 20000);
		serial.setPulse(2, 20000);
		serial.moveToAndWait(1, 800);
		serial.moveToAndWait(1, -800);
		serial.moveToAndWait(1, 800);
		serial.moveToAndWait(1, -800);
		
		try{

			for (logCurTimes = 0 ; logCurTimes < logTimes ; logCurTimes++){

				serial.setPulse(1, 20000);
				serial.setPulse(2, 20000);

				int[] pulse = new int[2];

				String[] parts = movePlan.split(",");

				System.out.println("movePlan: "+movePlan);
				System.out.println("parts.length : "+parts.length);
				for(int j = 0 ; j < parts.length ; j++){
					String mov = parts[j].trim();

					//First two is purse
					if(j < 2){
						int curPulse = Integer.parseInt(mov);
						pulse[j] = curPulse;

						//serial.setPulse(j+1, pulse);
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

					//When j is 0, 1 it is pulse setting
					//When j is 2, 3 it is first pos setting
					//When j is 4, it is starting point of logging.
					if(j == 3){
						boolean isPrerun = false;

						serial.setPulse(1, pulse[0]);
						serial.setPulse(2, pulse[1]);

						depthLogger.startLogger(isPrerun);
						Thread.sleep(2000);
					}
				}


				System.out.println("Logger stop!");
				depthLogger.stopLogger();
				//isTransferProgress = true;
				depthLogger.transferToServer();
				//isTransferProgress = false;

				if( logCurTimes < logTimes-1 )
					Thread.sleep(1000*logInterval*60);

				//logCurTimes++;
			}

		}catch(Exception e){
			isPlannedLogProgress = false;
			return e.toString();
		}

		
		
		isPlannedLogProgress = false;

		//Set to Origin
		serial.setPulse(1, 20000);
		serial.setPulse(2, 20000);
		serial.moveToAndWait(1, 0);
		serial.moveToAndWait(2, 0);
		serial.moveToAndWait(1, 800);
		serial.moveToAndWait(1, -800);
		serial.moveToAndWait(1, 800);
		serial.moveToAndWait(1, -800);

		String log = depthLogger.getLog();
		depthLogger.flushLog();
		return log;
	}

	@RequestMapping("/doPlannedLogging.do")
	public @ResponseBody String doPlannedLogging(HttpServletRequest request, HttpServletResponse response) throws Exception {
		logInterval = ServletRequestUtils.getIntParameter(request, "logInterval", LOG_INTERVAL_DEFAULT);
		logTimes = ServletRequestUtils.getIntParameter(request, "logTimes", LOG_TIMES_DEFAULT);
		movePlan = ServletRequestUtils.getStringParameter(request, "movePlan", MOVE_PLAN_DEFAULT);
		String logPrefix = ServletRequestUtils.getStringParameter(request, "logPrefix", LoggerProcess.LOG_PREFIX_DEFAULT);

		depthLogger.setLogInterval(logInterval);
		depthLogger.setLogTimes(logTimes);
		depthLogger.setMovePlan(logPrefix);
		depthLogger.setLogPrefix(logPrefix);

		return doPlannedAction(depthLogger);
		//		isPlannedLogProgress = true;
		//
		//		try{
		//
		//			for (logCurTimes = 0 ; logCurTimes < logTimes ; logCurTimes++){
		//
		//				serial.setPulse(1, 20000);
		//				serial.setPulse(2, 20000);
		//				
		//				int[] pulse = new int[2];
		//				
		//				String[] parts = movePlan.split(",");
		//				
		//				System.out.println("movePlan: "+movePlan);
		//				System.out.println("parts.length : "+parts.length);
		//				for(int j = 0 ; j < parts.length ; j++){
		//					String mov = parts[j].trim();
		//					
		//					//First two is purse
		//					if(j < 2){
		//						int curPulse = Integer.parseInt(mov);
		//						pulse[j] = curPulse;
		//						
		//						//serial.setPulse(j+1, pulse);
		//						continue;
		//					}
		//					
		//					String[] subParts = mov.split(" ");
		//					if(subParts.length != 2){
		//						System.out.println("Move format error");
		//						break;	
		//					}
		//					int motorIndex = Integer.parseInt(subParts[0]);
		//					int motorToPos = Integer.parseInt(subParts[1]);
		//					System.out.println(motorIndex+" "+motorToPos);
		//					
		//					serial.moveToAndWait(motorIndex, motorToPos);
		//					
		//					//When j is 0, 1 it is pulse setting
		//					//When j is 2, 3 it is first pos setting
		//					//When j is 4, it is starting point of logging.
		//					if(j == 3){
		//						boolean isPrerun = false;
		//						
		//						serial.setPulse(1, pulse[0]);
		//						serial.setPulse(2, pulse[1]);
		//						
		//						depthLogger.startLogger(isPrerun);
		//						Thread.sleep(2000);
		//					}
		//				}
		//
		//
		//				System.out.println("Logger stop!");
		//				depthLogger.stopLogger();
		//				//isTransferProgress = true;
		//				depthLogger.transferToServer();
		//				//isTransferProgress = false;
		//				
		//				if( logCurTimes < logTimes-1 )
		//					Thread.sleep(1000*logInterval*60);
		//				
		//				//logCurTimes++;
		//			}
		//
		//		}catch(Exception e){
		//			isPlannedLogProgress = false;
		//			return e.toString();
		//		}
		//
		//		isPlannedLogProgress = false;
		//		
		//		//Set to Origin
		//		serial.setPulse(1, 20000);
		//		serial.setPulse(2, 20000);
		//		serial.moveToAndWait(1, 0);
		//		serial.moveToAndWait(2, 0);
		//		
		//		String log = depthLogger.getLog();
		//		depthLogger.flushLog();
		//		return log;
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

	@RequestMapping("/getTransferProgressLog.do")
	public @ResponseBody String getTransferProgressLog(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return depthLogger.getTransferStatusLog();
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
	
	class InitThread extends Thread{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			System.out.println("SERVER IP:"+Misc.getIPAddress());
			if("odroid".equalsIgnoreCase(Misc.getHostName()))
				ConfigService.syncValue("OdroidIP", Misc.getIPAddress());
			
			
			serial = new SerialComm();
			depthLogger = new LoggerProcess();
			System.out.println("INITIALIZE!");
			serial.initialize();
			
			//System.out.println(context.getRealPath("/"));

			loggerInit();
			
//			
			
			
			LogConfig lc = LogConfig.readLogConfig();
			if("Y".equalsIgnoreCase(lc.getRecordAfterBoot())){
				waitUntilMotorSet();
				System.out.println("System Ready!");
				int t = 0;
				System.out.println("START LOGGING! after "+(t/1000.0)+" sec.");
				try {
					Thread.sleep(t);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				doPlannedAction(depthLogger);
			}
		}
	}
}


