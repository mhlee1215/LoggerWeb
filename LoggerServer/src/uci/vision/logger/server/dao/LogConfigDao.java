package uci.vision.logger.server.dao;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Repository;
import com.ibatis.sqlmap.client.SqlMapClient;

@Repository
public class LogConfigDao extends SqlMapClientDaoSupport {
	
	 @Resource(name="sqlMapClient")
	 protected void initDAO(SqlMapClient sqlMapClient) {        
		 this.setSqlMapClient(sqlMapClient);
	 } 
	
	
	public String readValue(String key) {
		return (String)getSqlMapClientTemplate().queryForObject("ConfigSql.readValue", key);
	}


	public void createValue(String key, String value) {
		HashMap<String, String> kMap = new HashMap<>();
		kMap.put("key", key);
		kMap.put("value", value);
		System.out.println(">>>>"+kMap);
		getSqlMapClientTemplate().insert("ConfigSql.createValue", kMap);
	}


	public void deleteUser(String key) {
		getSqlMapClientTemplate().delete("ConfigSql.deleteValue", key);
	}


	public void updateUser(String key, String value) {
		HashMap<String, String> kMap = new HashMap<>();
		kMap.put("key", key);
		kMap.put("value", value);
		getSqlMapClientTemplate().update("ConfigSql.updateValue", kMap);
	}

}
