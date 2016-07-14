package uci.vision.logger.domain;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import uci.vision.logger.util.PropertyManager;

public class LogConfig {
	String logPrefix;
	String ftpHost;
	String ftpId;
	String ftpPwd;
	String ftpDst;
	String configHost;
	String recordAfterBoot;
	//String recordInterval;
	//String recordTimes;
	String transferResumeOnStart;
	String movePlan;
	String logInterval;
	String logTimes;
	String deleteAfterTransfer;
	
	public String getDeleteAfterTransfer() {
		return deleteAfterTransfer;
	}
	public void setDeleteAfterTransfer(String deleteAfterTransfer) {
		this.deleteAfterTransfer = deleteAfterTransfer;
	}
	public String getMovePlan() {
		return movePlan;
	}
	public void setMovePlan(String movePlan) {
		this.movePlan = movePlan;
	}
	public int getLogIntervalInt() {
		if(logInterval == null || logInterval.isEmpty()) return 0;
		return Integer.parseInt(logInterval);
	}
	
	public int getLogTimesInt() {
		if(logTimes == null || logTimes.isEmpty()) return 0;
		return Integer.parseInt(logTimes);
	}
	public String getLogInterval() {
		return logInterval;
	}
	public void setLogInterval(String logInterval) {
		this.logInterval = logInterval;
	}
	public String getLogTimes() {
		return logTimes;
	}
	public void setLogTimes(String logTimes) {
		this.logTimes = logTimes;
	}
	public String getTransferResumeOnStart() {
		return transferResumeOnStart;
	}



	public void setTransferResumeOnStart(String transferResumeOnStart) {
		this.transferResumeOnStart = transferResumeOnStart;
	}



	public String getRecordAfterBoot() {
		return recordAfterBoot;
	}



	public void setRecordAfterBoot(String recordAfterBoot) {
		this.recordAfterBoot = recordAfterBoot;
	}



//	public String getRecordInterval() {
//		return recordInterval;
//	}
//
//
//
//	public void setRecordInterval(String recordInterval) {
//		this.recordInterval = recordInterval;
//	}
//
//
//
//	public String getRecordTimes() {
//		return recordTimes;
//	}
//
//
//
//	public void setRecordTimes(String recordTimes) {
//		this.recordTimes = recordTimes;
//	}



	public static LogConfig readLogConfig(){
		return PropertyManager.getManager().readConfig();
	}
	
	

	public String getConfigHost() {
		return configHost;
	}



	public void setConfigHost(String configHost) {
		this.configHost = configHost;
	}



	public String getFtpDst() {
		return ftpDst;
	}

	public void setFtpDst(String ftpDst) {
		this.ftpDst = ftpDst;
	}

	public String getFtpHost() {
		return ftpHost;
	}

	public void setFtpHost(String ftpHost) {
		this.ftpHost = ftpHost;
	}

	public String getFtpId() {
		return ftpId;
	}

	public void setFtpId(String ftpId) {
		this.ftpId = ftpId;
	}

	public String getFtpPwd() {
		return ftpPwd;
	}

	public void setFtpPwd(String ftpPwd) {
		this.ftpPwd = ftpPwd;
	}

	public String getLogPrefix() {
		return logPrefix;
	}

	
	public void setLogPrefix(String logPrefix) {
		setLogPrefix(logPrefix, false);
	}
	public void setLogPrefix(String logPrefix, boolean isWrite) {
		this.logPrefix = logPrefix;
		if(isWrite)
			PropertyManager.getManager().writeConfig(this);
	}

	@Override
	public String toString() {
		return "{\"logPrefix\":\"" + logPrefix + "\",\"ftpHost\":\"" + ftpHost + "\",\"ftpId\":\"" + ftpId
				+ "\",\"ftpPwd\":\"" + ftpPwd + "\",\"ftpDst\":\"" + ftpDst + "\",\"configHost\":\"" + configHost
				+ "\",\"recordAfterBoot\":\"" + recordAfterBoot + "\",\"transferResumeOnStart\":\""
				+ transferResumeOnStart + "\",\"movePlan\":\"" + movePlan + "\",\"logInterval\":\"" + logInterval
				+ "\",\"logTimes\":\"" + logTimes + "\",\"deleteAfterTransfer\":\"" + deleteAfterTransfer + "\"}";
	}
	
	public static void main(String[] args){
//		LogConfig lc = LogConfig.readLogConfig();
//		System.out.println(lc);		
//		System.out.println(LogConfig.class.getDeclaredFields().length);
//		Field f = LogConfig.class.getDeclaredFields()[0];
//		System.out.println(f.getName());
//		
//		LogConfig lc = new LogConfig();
//		lc.setConfigHost("abcdef");
//		try {
//			try {
//				System.out.println(lc);
//				String a = (String) LogConfig.class.getDeclaredMethod("getConfigHost").invoke(lc);
//				System.out.println(a);
//			} catch (IllegalAccessException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IllegalArgumentException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (InvocationTargetException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		} catch (NoSuchMethodException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SecurityException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//		String a = "aaaa12a";
//		System.out.println(uppercaseFirst(a));
	}
	
	public static String uppercaseFirst(String a){
		if(a.length() < 2) return a.toUpperCase();
		return a.substring(0, 1).toUpperCase()+a.substring(1);
	}
}
