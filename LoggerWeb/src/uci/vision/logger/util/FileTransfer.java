package uci.vision.logger.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.io.CopyStreamAdapter;

import uci.vision.logger.domain.LogConfig;

public class FileTransfer{
	File uploadfile = null;
	int percent = 0;
	

	private String srcName;
	
	public FileTransfer(String srcName){
		this.srcName = srcName;
	}
	
	public int getPercent(){
		return percent;
	}
	
	public int uploadFile(){
		// TODO Auto-generated method stub
		FileInputStream fis = null; // File Input Stream
		FTPClient ftp = null;

		int result = 0;
		String fileName = srcName;//"test123";

		try{
			LogConfig lc = LogConfig.readLogConfig();
			uploadfile = new File(System.getProperty("user.home")+"/LoggerHome/capture/"+srcName);
			//uploadfile = new File(System.getProperty("user.home")+"/data_from_odroid/"+srcName);
			ftp = new FTPClient();
			ftp.connect(lc.getFtpHost());
			ftp.login(lc.getFtpId(), lc.getFtpPwd());

			ftp.setFileType(FTP.BINARY_FILE_TYPE);
			ftp.changeWorkingDirectory(lc.getFtpDst());

			CopyStreamAdapter streamListener = new CopyStreamAdapter() {



				@Override
				public void bytesTransferred(long totalBytesTransferred, int bytesTransferred, long streamSize) {
					//this method will be called everytime some bytes are transferred


					percent = (int)(totalBytesTransferred*100/uploadfile.length());
					// update your progress bar with this percentage
					//System.out.println(percent);
				}

			};



			ftp.setCopyStreamListener(streamListener);


			try{
				fis = new FileInputStream(uploadfile); // 업로드할 File 생성

				boolean isSuccess = ftp.storeFile(fileName, fis); // File 업로드

				if (isSuccess){
					result = 1; // 성공     
				}
				else{
					//
				}
			} catch(IOException ex){
				System.out.println("IO Exception : " + ex.getMessage());
			}finally{
				if (fis != null){
					try{
						fis.close(); // Stream 닫기
						return result;

					}
					catch(IOException ex){
						System.out.println("IO Exception : " + ex.getMessage());
					}
				}
			}
			ftp.logout(); // FTP Log Out
		}catch(IOException e){
			System.out.println("IO:"+e.getMessage());
		}finally{
			if (ftp != null && ftp.isConnected()){
				try{
					ftp.disconnect(); // 접속 끊기
					return result;
				}
				catch (IOException e){
					System.out.println("IO Exception : " + e.getMessage());
				}
			}
		}
		if("Y".equalsIgnoreCase(LogConfig.readLogConfig().getDeleteAfterTransfer())){
			if(uploadfile != null)
				uploadfile.delete();
		}
			
		return result;
	}
	
	public String getSrcName(){
		return srcName;
	}

	public static void main(String[] args) {

		FileTransfer ft = new FileTransfer("NIGHT-2016-07-06_15_04.klg");
		//ft
//		ft.start();
//		
//		System.out.println("started!");
//		waitUntilFinished(ft);
//		System.out.println("end!");
		
		
//		while(true){
//			
//			System.out.println("percent: "+ft.getPercent());
//			System.out.println("isAlive?"+ftThread.isAlive());
//			
//			try {
//				Thread.sleep(500);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
			
		//}

	}

//	@Override
//	public void run() {
//		// TODO Auto-generated method stub
//		uploadFile();
//	}
	
//	public static void waitUntilFinished(Thread t){
//		while(t.isAlive()){			
//			//System.out.println("percent: "+ft.getPercent());
//			//System.out.println("isAlive?"+ftThread.isAlive());
//			
//			try {
//				Thread.sleep(500);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//	}

}
