package uci.vision.logger.util;

import java.util.LinkedList;
import java.util.Queue;

public class FileTransferWorker implements Runnable{

	public Queue<FileTransfer> transferQueue = new LinkedList<FileTransfer>();
	public FileTransfer curWork = null;
	public int workSize = 0;

	public static int STATE_TRANSFER_INIT = 1;
	public static int STATE_TRANSFER_SENDING = 2;
	public static int STATE_TRANSFER_FINISHED = 3;

	public int transferState = STATE_TRANSFER_INIT;


	@Override
	public void run() {
		transferState = STATE_TRANSFER_INIT;

		workSize = transferQueue.size();
		while(transferQueue.size() > 0){
			transferState = STATE_TRANSFER_SENDING;

			curWork = transferQueue.poll();
			curWork.uploadFile();
			//if(curWork.getSrcName().endsWith("klg"))
			LoggerProcess.transferFinished(curWork.getSrcName());
			curWork = null;
			//waitUntilFinished(curWork);
			workSize--;
		}

		transferState = STATE_TRANSFER_FINISHED;
	}

	public int getTransferState(){
		return transferState;
	}

	public String getWorkFileName(){
		if(curWork == null) return "";
		return curWork.getSrcName();
	}
	
	public int getWorkSize(){
		return transferQueue.size();
	}

	public int getCurProgress(){
		if(curWork == null) return 0;
		return curWork.getPercent();
	}

	public void addWork(String fileName){
		transferQueue.add(new FileTransfer(fileName));
	}

	public static void waitUntilFinished(Thread t){
		while(t.isAlive()){			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args){
		FileTransferWorker worker = new FileTransferWorker();
		String fName = "2016-07-04_15_07_15.klg";
		{
			worker.addWork(fName);
			worker.addWork(fName);

			Thread t = new Thread(worker);
			t.start();

			while(t.isAlive()){
				System.out.println(worker.getWorkSize()+"/"+worker.getCurProgress());

				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

		{
			worker.addWork(fName);
			worker.addWork(fName);

			Thread t = new Thread(worker);
			t.start();

			while(t.isAlive()){
				System.out.println(worker.getWorkSize()+"/"+worker.getCurProgress());

				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		
		System.out.println(worker.getWorkSize()+"/"+worker.getCurProgress());


	}

}
