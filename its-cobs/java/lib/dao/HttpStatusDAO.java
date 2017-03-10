package lib.dao;

import java.util.List;

import lib.entity.HttpStatus;

public interface HttpStatusDAO extends AbstractDAO<HttpStatus, Integer> {
	
	List<String> getHttpStatusByKey(String key);

	String getHttpStatusByKeyByLocalization(String localization, String key);
	
}
