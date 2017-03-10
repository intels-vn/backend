package lib.dao;

import lib.entity.Config;

public interface ConfigDAO extends AbstractDAO<Config, Integer> {
	
	String getPrivateKey();
	
	String getTokenKey();
	
	long getTokenTimeOut();
	
}
