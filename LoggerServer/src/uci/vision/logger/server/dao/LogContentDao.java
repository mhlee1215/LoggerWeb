package uci.vision.logger.server.dao;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Repository;
import com.ibatis.sqlmap.client.SqlMapClient;

import uci.vision.logger.server.domain.Category;
import uci.vision.logger.server.domain.Date;
import uci.vision.logger.server.domain.LogContent;

@Repository
public class LogContentDao extends SqlMapClientDaoSupport {
	
	 @Resource(name="sqlMapClient")
	 protected void initDAO(SqlMapClient sqlMapClient) {        
		 this.setSqlMapClient(sqlMapClient);
	 } 
	
	
	public List<Category> getCategory(LogContent logContent){
		return (List<Category>)getSqlMapClientTemplate().queryForList("ContentSql.getCategory", logContent);
	}
	
	public List<Date> getDate(LogContent logContent){
		return (List<Date>)getSqlMapClientTemplate().queryForList("ContentSql.getDate", logContent);
	}
	 
	public LogContent readContent(LogContent logContent) {
		return (LogContent)getSqlMapClientTemplate().queryForObject("ContentSql.readContent", logContent);
	}
	
	public List<LogContent> readContents(LogContent logContent) {
		return (List<LogContent>)getSqlMapClientTemplate().queryForList("ContentSql.readContents", logContent);
	}


	public void createContents(LogContent logContent) {
		getSqlMapClientTemplate().insert("ContentSql.createContents", logContent);
	}


	public void deleteContents(LogContent logContent) {
		getSqlMapClientTemplate().delete("ContentSql.deleteContents", logContent);
	}


	public void updateContents(LogContent logContent) {
		getSqlMapClientTemplate().update("ContentSql.updateContents", logContent);
	}
}
