package lib.interactor.token;

import java.util.List;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import lib.dao.ConfigDAO;
import lib.dao.TokenDAO;
import lib.entity.Token;
import lib.entity.User;
import lib.interactor.AbstractMUC;
import lib.support.encryptDecrypt;

@Repository
public class TokenMUC extends AbstractMUC<Token, Integer> implements TokenDAO {
	@Autowired
	private ConfigDAO configDAO;
	
	@Override
	public String createToken(User user) {
		this.deleteExpiredToken();
		
		String code = encryptDecrypt.encrypt(user.getUsername() + this.configDAO.getTokenKey() + System.currentTimeMillis(), this.configDAO.getPrivateKey());
		long fromTime = System.currentTimeMillis();
		long toTime = System.currentTimeMillis() + this.configDAO.getTokenTimeOut();
		
		if(checkExistedToken(code, user.getUsername()) == false) {
			String sql = "INSERT INTO Token (token, fromTime, toTime) VALUES (:code, :fromTime, :toTime)";
			Query query = getSession().createSQLQuery(sql);
			query.setParameter("code", code);
			query.setParameter("fromTime", fromTime);
			query.setParameter("toTime", toTime);
			query.executeUpdate();
			
			return code;
		} else {
			try {
				code = getTokenByUsername(user.getUsername()).getCode();
				
				updateToken(code);
				
				return code;
			} catch(Exception e) {
				String sql = "INSERT INTO Token (token, fromTime, toTime) VALUES (:code, :fromTime, :toTime)";
				Query query = getSession().createSQLQuery(sql);
				query.setParameter("code", code);
				query.setParameter("fromTime", fromTime);
				query.setParameter("toTime", toTime);
				query.executeUpdate();
				
				return code;
			}
		}
	}

	@Override
	public Token getToken(String code) {
		this.deleteExpiredToken();
		
		String hql = "FROM Token t WHERE t.token = :code";
		return (Token) getSession().createQuery(hql).setParameter("code", code).uniqueResult();
	}

	@Override
	public void deleteToken(String code) {
		this.deleteExpiredToken();
		
		String hql = "DELETE FROM Token t WHERE t.token = :code";
		Query query = getSession().createQuery(hql);
		query.setParameter("code", code);
		query.executeUpdate();
	}
	
	@Override
	public void deleteExpiredToken() {
		long currentTime = System.currentTimeMillis();
		String hql = "DELETE FROM Token t WHERE t.toTime < :currentTime";
		Query query = getSession().createQuery(hql);
		query.setParameter("currentTime", currentTime);
		query.executeUpdate();
	}

	@Override
	public boolean updateToken(String code) {
		this.deleteExpiredToken();
		
		long currentTime = System.currentTimeMillis();
		String hql = "FROM Token t WHERE t.token = :code";
		Token token = (Token) getSession().createQuery(hql).setParameter("code", code).uniqueResult();
		
		if(token.getTo() >= currentTime) {
			String hql1 = "UPDATE Token t SET t.toTime = " + (currentTime + this.configDAO.getTokenTimeOut()) + " WHERE t.token = :code";
			getSession().createQuery(hql1).setParameter("code", code).executeUpdate();
			return true;
		} else {
			this.deleteToken(code);
			return false;
		}
	}

	@Override
	public boolean checkExistedToken(String code, String username) {
		User user = (User) getSession().createQuery(" FROM User WHERE username = :username").setParameter("username", username).uniqueResult();
		String key = encryptDecrypt.decrypt(code, this.configDAO.getPrivateKey()).split(this.configDAO.getTokenKey())[0];
		try {
			if(key.equals(user.getUsername()))
				return true;
			else return false;
		} catch(Exception e) {
			return false;
		}
	}

	@Override
	public Token getTokenByUsername(String username) {
		@SuppressWarnings("unchecked")
		List<Token> list = getSession().createQuery(" FROM Token").list();
		
		for(int i = 0; i < list.size(); i++) {
			if(encryptDecrypt.decrypt(list.get(i).getCode(), this.configDAO.getPrivateKey()).split(this.configDAO.getTokenKey())[0].equals(username))
				return list.get(i);
		}
		
		return null;
	}
}
