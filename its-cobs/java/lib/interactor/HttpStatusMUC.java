package lib.interactor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;

import lib.dao.HttpStatusDAO;
import lib.entity.HttpStatus;

@Repository
public class HttpStatusMUC extends AbstractMUC<HttpStatus, Integer> implements HttpStatusDAO {

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getHttpStatusByKey(String key) {
		String sql = "SELECT s.message FROM HttpStatus s WHERE s.localizationKey = :localizationKey";
		Query query = getSession().createSQLQuery(sql);
		query.setParameter("localizationKey", key);
		return query.list();
	}

	@Override
	public String getHttpStatusByKeyByLocalization(String localization, String key) {
		String input = getHttpStatusByKey(key).toString();
		String newString = input.substring(1, input.length()-1);
		JSONObject jsonOBJ = new JSONObject(newString);
		Map<String, Object> result = jsonToMap(jsonOBJ);
		String message = result.get(localization).toString();
		return message;
	}
	
	
	//JSON MAP METHOD
	public static Map<String, Object> jsonToMap(JSONObject json) throws JSONException {
	    Map<String, Object> retMap = new HashMap<String, Object>();

	    if(json != JSONObject.NULL) {
	        retMap = toMap(json);
	    }
	    return retMap;
	}

	public static Map<String, Object> toMap(JSONObject object) throws JSONException {
	    Map<String, Object> map = new HashMap<String, Object>();

	    Iterator<String> keysItr = object.keys();
	    while(keysItr.hasNext()) {
	        String key = keysItr.next();
	        Object value = object.get(key);

	        if(value instanceof JSONArray) {
	            value = toList((JSONArray) value);
	        }

	        else if(value instanceof JSONObject) {
	            value = toMap((JSONObject) value);
	        }
	        map.put(key, value);
	    }
	    return map;
	}

	public static List<Object> toList(JSONArray array) throws JSONException {
	    List<Object> list = new ArrayList<Object>();
	    for(int i = 0; i < array.length(); i++) {
	        Object value = array.get(i);
	        if(value instanceof JSONArray) {
	            value = toList((JSONArray) value);
	        }

	        else if(value instanceof JSONObject) {
	            value = toMap((JSONObject) value);
	        }
	        list.add(value);
	    }
	    return list;
	}
}
