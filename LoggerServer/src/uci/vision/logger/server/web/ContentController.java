package uci.vision.logger.server.web;

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
import uci.vision.logger.server.domain.LogContent;
import uci.vision.logger.server.service.LogContentService;
import uci.vision.logger.service.utils.MyJsonUtil;


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
		List<LogContent> v = logContentService.readContents(lcp);
		System.out.println("VV:"+v);
		//If there is no key/value
		//Then, add
		if(v == null || v.size() == 0){
			logContentService.createContents(lc);
		}
		//Otherwise, update
		else{
			logContentService.updateContents(lc);
		}
		return "success";
	}
	
	
	@RequestMapping("/index.do")
    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
		String isAdmin = ServletRequestUtils.getStringParameter(request, "isAdmin", "");
		
		LogContent lc = LogContent.readFromRequest(request);
		
		lc.setIsvalid("Y");
		lc.setTransmitted("Y");
		lc.setType(Constant.FILE_TYPE_LOG);
		
		System.out.println("LC:"+lc);
		List<LogContent> lcList = logContentService.readContents(lc);
		
		List<Category> cat = logContentService.getCategory(lc);
		
		ModelAndView model = new ModelAndView("contentList");
		model.addObject("contentList", lcList);
		model.addObject("cat", cat);
		model.addObject("isAdmin", isAdmin);
		model.addObject("cur_cat", lc.getCategory());
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
		
		ModelAndView model = new ModelAndView("redirect:index.do");
		model.addObject("contentList", lcList);
		model.addObject("cat", cat);
		model.addObject("isAdmin", isAdmin);
		model.addObject("cur_cat", lc.getCategory());
		return model;
    }
}
