package uci.vision.logger.domain;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.ServletRequestUtils;

public class LogContentBin {
	List<LogContent> logContents;

	public List<LogContent> getLogContents() {
		return logContents;
	}

	public void setGameResults(List<LogContent> logContents) {
		this.logContents = logContents;
	}

	@Override
	public String toString() {
		return "{\"logContents\":\"" + logContents + "\"}";
	}
}
