package uci.vision.logger.server.web;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import uci.vision.logger.server.domain.Category;
import uci.vision.logger.server.domain.Constant;
import uci.vision.logger.server.domain.Date;
import uci.vision.logger.server.domain.LogContent;
import uci.vision.logger.server.service.LogContentService;
import uci.vision.logger.service.utils.MyJsonUtil;
import uci.vision.logger.service.utils.ThreadReader;


@Controller
public class ContentController {

	private Logger logger = Logger.getLogger(getClass());

	@Autowired
	private final LogContentService logContentService = null;

	@RequestMapping("/readContents.do")
	public ResponseEntity<String> readContents(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LogContent lc = LogContent.readFromRequest(request);
		System.out.println("LC:"+lc);
		List<LogContent> lcList = logContentService.readContents(lc);

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(MyJsonUtil.toString(lcList, "logContents"), responseHeaders, HttpStatus.CREATED);
	}

	@RequestMapping("/readContentsForWeb.do")
	public ResponseEntity<String> readContentsForWeb(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LogContent lc = LogContent.readFromRequest(request);
		System.out.println("LC:"+lc);
		List<LogContent> lcList = logContentService.readContentsForWeb(lc);

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");
		return new ResponseEntity<String>(MyJsonUtil.toString(lcList, "logContents"), responseHeaders, HttpStatus.CREATED);
	}

	@RequestMapping("/deleteContents.do")
	public @ResponseBody String deleteContents(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LogContent lc = LogContent.readFromRequest(request);
		logContentService.deleteContents(lc);
		return "success";
	}

	@RequestMapping("/createContents.do")
	public @ResponseBody String createValue(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LogContent lc = LogContent.readFromRequest(request);
		logContentService.createContents(lc);
		return "success";
	}

	@RequestMapping("/updateContents.do")
	public @ResponseBody String updateValue(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LogContent lc = LogContent.readFromRequest(request);
		logContentService.updateContents(lc);
		return "success";
	}

	@RequestMapping("/syncContents.do")
	public @ResponseBody String syncContents(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LogContent lc = LogContent.readFromRequest(request);
		System.out.println("SYNC LC:"+lc);
		LogContent lcp = new LogContent();
		lcp.setFilename(lc.getFilename());
		LogContent v = logContentService.readContent(lcp);
		System.out.println("VV:"+v);
		//If there is no key/value
		//Then, add
		if(v == null){
			logContentService.createContents(lc);
		}
		//Otherwise, update
		else{
			logContentService.updateContents(lc);
		}
		return "success";
	}


	@RequestMapping("/index.do")
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView model = new ModelAndView("redirect:mergedList.do");
		return model;
	}
	
	@RequestMapping("/introduction.do")
	public ModelAndView introduction(HttpServletRequest request, HttpServletResponse response) {
		String isAdmin = ServletRequestUtils.getStringParameter(request, "isAdmin", "");

		if("Y".equalsIgnoreCase(isAdmin)){
			request.getSession().setAttribute("isAdmin", "Y");
		}

		if("".equals(isAdmin)){
			isAdmin = (String)request.getSession().getAttribute("isAdmin");
		}

		LogContent lc = LogContent.readFromRequest(request);

		lc.setIsvalid("Y");
		lc.setTransmitted("Y");
		lc.setType(Constant.FILE_TYPE_LOG);

		System.out.println("LC:"+lc);
		List<LogContent> lcList = logContentService.readContentsForWeb(lc);

		List<Category> cat = logContentService.getCategory(lc);
		List<Date> date = logContentService.getDate(lc);
		
		List<Date> dateForMergedModel = logContentService.getDate(new LogContent());
		List<Date> dataForMergedModelFiltered = new ArrayList<Date>();
		
		for(Date d : dateForMergedModel){
			File f = new File (Constant.potreeMerged_home+d.getDate()+".html");
			if(!f.exists()) continue;
			
			dataForMergedModelFiltered.add(d);
		}
		

		ModelAndView model = new ModelAndView("introduction");
		model.addObject("contentList", lcList);
		model.addObject("cat", cat);
		model.addObject("date", date);
		model.addObject("dataForMergedModel", dataForMergedModelFiltered);
		model.addObject("isAdmin", isAdmin);
		model.addObject("cur_cat", lc.getCategory());
		model.addObject("cur_date", lc.getDate().substring(0, Math.min(lc.getDate().length(), 10)));
		return model;
	}
	
	@RequestMapping("/mergedList.do")
	public ModelAndView mergedList(HttpServletRequest request, HttpServletResponse response) {
		String isAdmin = ServletRequestUtils.getStringParameter(request, "isAdmin", "");

		if("Y".equalsIgnoreCase(isAdmin)){
			request.getSession().setAttribute("isAdmin", "Y");
		}

		if("".equals(isAdmin)){
			isAdmin = (String)request.getSession().getAttribute("isAdmin");
		}

		LogContent lc = LogContent.readFromRequest(request);

		lc.setIsvalid("Y");
		lc.setTransmitted("Y");
		lc.setType(Constant.FILE_TYPE_LOG);

		System.out.println("LC:"+lc);
		List<LogContent> lcList = logContentService.readContentsForWeb(lc);

		List<Category> cat = logContentService.getCategory(lc);
		List<Date> date = logContentService.getDate(lc);
		
		List<Date> dateForMergedModel = logContentService.getDate(new LogContent());
		List<Date> dataForMergedModelFiltered = new ArrayList<Date>();
		
		for(Date d : dateForMergedModel){
			File f = new File (Constant.potreeMerged_home+d.getDate()+".html");
			if(!f.exists()) continue;
			
			dataForMergedModelFiltered.add(d);
		}
		

		ModelAndView model = new ModelAndView("mergedList");
		model.addObject("contentList", lcList);
		model.addObject("cat", cat);
		model.addObject("date", date);
		model.addObject("dataForMergedModel", dataForMergedModelFiltered);
		model.addObject("isAdmin", isAdmin);
		model.addObject("cur_cat", lc.getCategory());
		model.addObject("cur_date", lc.getDate().substring(0, Math.min(lc.getDate().length(), 10)));
		model.addObject("postfix", "2");
		return model;
	}
	
	@RequestMapping("/contentList.do")
	public ModelAndView contentList(HttpServletRequest request, HttpServletResponse response) {
		String isAdmin = ServletRequestUtils.getStringParameter(request, "isAdmin", "");

		if("Y".equalsIgnoreCase(isAdmin)){
			request.getSession().setAttribute("isAdmin", "Y");
		}

		if("".equals(isAdmin)){
			isAdmin = (String)request.getSession().getAttribute("isAdmin");
		}

		LogContent lc = LogContent.readFromRequest(request);

		lc.setIsvalid("Y");
		lc.setTransmitted("Y");
		lc.setType(Constant.FILE_TYPE_LOG);

		System.out.println("LC:"+lc);
		List<LogContent> lcList = logContentService.readContentsForWeb(lc);

		List<Category> cat = logContentService.getCategory(lc);
		List<Date> date = logContentService.getDate(lc);
		
		List<Date> dateForMergedModel = logContentService.getDate(new LogContent());
		List<Date> dataForMergedModelFiltered = new ArrayList<Date>();
		
		for(Date d : dateForMergedModel){
			File f = new File (Constant.potreeMerged_home+d.getDate()+".html");
			if(!f.exists()) continue;
			
			dataForMergedModelFiltered.add(d);
		}
		

		ModelAndView model = new ModelAndView("contentList");
		model.addObject("contentList", lcList);
		model.addObject("cat", cat);
		model.addObject("date", date);
		model.addObject("dataForMergedModel", dataForMergedModelFiltered);
		model.addObject("isAdmin", isAdmin);
		model.addObject("cur_cat", lc.getCategory());
		model.addObject("cur_date", lc.getDate().substring(0, Math.min(lc.getDate().length(), 10)));
		return model;
	}

	@RequestMapping("/remove.do")
	public ModelAndView remove(HttpServletRequest request, HttpServletResponse response) {
		String isAdmin = ServletRequestUtils.getStringParameter(request, "isAdmin", "");
		LogContent lc = LogContent.readFromRequest(request);

		logContentService.removeContents(lc);

		lc.setIsvalid("Y");
		lc.setTransmitted("Y");
		lc.setType(Constant.FILE_TYPE_LOG);

		System.out.println("LC:"+lc);
		List<LogContent> lcList = logContentService.readContents(lc);

		List<Category> cat = logContentService.getCategory(lc);

		ModelAndView model = new ModelAndView("redirect:index.do?category="+lc.getCategory());
		model.addObject("contentList", lcList);
		model.addObject("cat", cat);
		model.addObject("isAdmin", isAdmin);
		model.addObject("cur_cat", lc.getCategory());
		return model;
	}

	@RequestMapping("/changeCategory.do")
	public ModelAndView changeCategory(HttpServletRequest request, HttpServletResponse response) {
		String isAdmin = ServletRequestUtils.getStringParameter(request, "isAdmin", "");
		LogContent lc = LogContent.readFromRequest(request);
		String toCategory = ServletRequestUtils.getStringParameter(request, "toCategory", "");

		logContentService.changeCategory(lc.getFilename(), toCategory);

		ModelAndView model = new ModelAndView("redirect:index.do?category="+lc.getCategory());
		return model;
	}

	@RequestMapping("/undoEF.do")
	public ModelAndView undoEF(HttpServletRequest request, HttpServletResponse response) {
		String isAdmin = ServletRequestUtils.getStringParameter(request, "isAdmin", "");
		LogContent lc = LogContent.readFromRequest(request);

		logContentService.undoContents(lc);

		ModelAndView model = new ModelAndView("redirect:index.do");
		model.addObject("category", lc.getCategory());
		return model;
	}

	@RequestMapping("/logout.do")
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {

		request.getSession().removeAttribute("isAdmin");
		ModelAndView model = new ModelAndView("redirect:index.do");
		return model;
	}

	@RequestMapping("/genDayModel.do")
	public ModelAndView genDayModel(HttpServletRequest request, HttpServletResponse response) {
		LogContent lc = LogContent.readFromRequest(request);
		List<Date> date = logContentService.getDate(lc);
		for(Date d : date){
			ProcessBuilder pb = new ProcessBuilder(System.getProperty("user.home")+"/data_from_odroid/runDayModel.sh", d.getDate());
			Process process;
			try {
				process = pb.start();

				BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
				Thread outputReader = (new Thread(new ThreadReader(input)));
				outputReader.start();
				
				process.waitFor();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}


		request.getSession().removeAttribute("isAdmin");
		ModelAndView model = new ModelAndView("redirect:index.do");
		return model;
	}

}
