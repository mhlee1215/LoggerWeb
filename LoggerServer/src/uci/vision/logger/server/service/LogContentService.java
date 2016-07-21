package uci.vision.logger.server.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
 
import org.apache.commons.mail.EmailException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import uci.vision.logger.server.dao.LogConfigDao;
import uci.vision.logger.server.dao.LogContentDao;
import uci.vision.logger.server.domain.Category;
import uci.vision.logger.server.domain.Constant;
import uci.vision.logger.server.domain.LogContent;



@Service
public class LogContentService  {

	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	private LogContentDao		logContentDao;

	public List<Category> getCategory(LogContent logContent){
		return logContentDao.getCategory(logContent);
	}
	
	public LogContent readContent(LogContent logContent){
		LogContent c = logContentDao.readContent(logContent);
		
		return c;
	}
	
	
	public List<LogContent> readContents(LogContent logContent){
		List<LogContent> c = logContentDao.readContents(logContent);
		List<LogContent> c2 = new ArrayList<LogContent>();
		for(LogContent cc : c){
			if(cc.getDate() != null){
				cc.setDate(cc.getDate().replace(" ", "<br>"));
			}
			
			File f = new File (Constant.complete_home+cc.getFilename()+".ply");
			if(!f.exists()) continue;
			File f2 = new File (Constant.complete_home+cc.getFilename()+".rgb.jpg");
			if(!f2.exists()) continue;
			
			
			c2.add(cc);
			
		}
		return c2;
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
	
	public void removeContents(LogContent logContent){
		logContent.setIsvalid("N");
		logContentDao.updateContents(logContent);
		
		moveFile(logContent.getFilename());
		moveFile(logContent.getFilename()+".ply");
		moveFile(logContent.getFilename()+".freiburg");
		moveFile(logContent.getFilename()+".rgb.jpg");
		
		
		
//		String command = "mv "+Constant.data_home+logContent.getFilename()+"*"+" "+Constant.data_home+"trash/";
//		System.out.println("COMMAND: "+command);
//		
//		
//		
//		try {
//		       String line;
//		       Process p = Runtime.getRuntime().exec(command);
//
//		       BufferedReader in = new BufferedReader(
//		               new InputStreamReader(p.getInputStream()) );
//		       while ((line = in.readLine()) != null) {
//		         System.out.println("OUTPUT: "+line);
//		       }
//		       in.close();
//		     }
//		     catch (Exception e) {
//		       // ...
//		     }
	}
	
	public void moveFile(String name){
		String curName = name;
		String fromName = Constant.complete_home+curName;
		String toName = Constant.trash_home+curName;
		File f = new File (fromName);
		if(f.exists()){
			System.out.println("Move from :"+fromName+", to "+toName);
			f.renameTo(new File(toName));
		}else{
			System.out.println("Move file "+fromName+" not exist.");
		}
	}
	
	public void undoFile(String name){
		String curName = name;
		String fromName = Constant.complete_home+curName;
		String toName = Constant.data_home+curName;
		File f = new File (fromName);
		if(f.exists()){
			System.out.println("Move from :"+fromName+", to "+toName);
			f.renameTo(new File(toName));
		}else{
			System.out.println("Move file "+name+" not exist.");
		}
	}
	
 
	public void deleteFile(String name){
		File f = new File (name);
		if(f.exists()){
			System.out.println("Delete "+name);
			f.delete();
		}else{
			System.out.println("Delete file "+name+" not exist.");
		}
	}
	
	public void deletePotreeWebFile(String name){
		deleteFile(name+".html");
		deleteFile(name+".js");
	}
	
	
	
	public void renameFile(String nameFrom, String nameTo){
		String fromName = Constant.complete_home+nameFrom;
		String toName = Constant.complete_home+nameTo;
		File f = new File (fromName);
		if(f.exists()){
			System.out.println("Move from :"+fromName+", to "+toName);
			f.renameTo(new File(toName));
		}else{
			System.out.println("Move file "+fromName+" not exist.");
		}
	}
	
	public void changeCategory(String fileName, String toCategory){
		LogContent lcParam = new LogContent(fileName);
		LogContent lc = logContentDao.readContent(lcParam);
		
		changeName(fileName, fileName.replace(lc.getCategory(), toCategory));
		
		LogContent lc2 = new LogContent();
		lc2.setId(lc.getId());
		lc2.setCategory(toCategory);
		logContentDao.updateContents(lc2);
	}
	
	public void changeName(String nameFrom, String nameTo){
		LogContent lcParam = new LogContent(nameFrom);
		LogContent lc = logContentDao.readContent(lcParam);
		
		LogContent lcParam2 = new LogContent(nameTo);
		lcParam2.setId(lc.getId());
		logContentDao.updateContents(lcParam2);
		
		renameFile(nameFrom, nameTo);
		undoFile(nameTo);
		renameFile(nameFrom+".ply", nameTo+".ply");
		undoFile(nameTo+".ply");
		renameFile(nameFrom+".rgb.jpg", nameTo+".rgb.jpg");
		undoFile(nameTo+".rgb.jpg");
		renameFile(nameFrom+".freiburg", nameTo+".freiburg");
		undoFile(nameTo+".freiburg");
		
		
		
	}
	
}
