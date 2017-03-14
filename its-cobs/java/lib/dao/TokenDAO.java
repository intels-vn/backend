package lib.dao;

import lib.entity.Token;
import lib.entity.User;

public interface TokenDAO extends AbstractDAO<Token, Integer> {
	
	String createToken(User user);
	
	Token getToken(String code);
	
	void deleteToken(String code);
	
	boolean updateToken(String code);

	void deleteExpiredToken();
	
	boolean checkExistedToken(String code, String username);
	
	Token getTokenByUsername(String username);
	
}
