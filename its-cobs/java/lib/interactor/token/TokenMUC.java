package lib.interactor.token;

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
		
		String code = encryptDecrypt.encrypt(user.getUseraccount() + this.configDAO.getTokenKey() + System.currentTimeMillis(), this.configDAO.getPrivateKey());
		long fromTime = System.currentTimeMillis();
		long toTime = System.currentTimeMillis() + this.configDAO.getTokenTimeOut();
		
		String sql = "INSERT INTO Token (code, fromTime, toTime) VALUES (:code, :fromTime, :toTime)";
		Query query = getSession().createSQLQuery(sql);
		query.setParameter("code", code);
		query.setParameter("fromTime", fromTime);
		query.setParameter("toTime", toTime);
		query.executeUpdate();
		
		return code;
	}

	@Override
	public Token getToken(String code) {
		this.deleteExpiredToken();
		
		String hql = "FROM Token t WHERE t.code = :code";
		return (Token) getSession().createQuery(hql).setParameter("code", code).uniqueResult();
	}

	@Override
	public void deleteToken(String code) {
		this.deleteExpiredToken();
		
		String hql = "DELETE FROM Token t WHERE t.code = :code";
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
		String hql = "FROM Token t WHERE t.code = :code";
		Token token = (Token) getSession().createQuery(hql).setParameter("code", code).uniqueResult();
		
		if(token.getTo() >= currentTime) {
			String hql1 = "UPDATE Token t SET t.toTime = " + (currentTime + this.configDAO.getTokenTimeOut()) + " WHERE t.code = :code";
			getSession().createQuery(hql1).setParameter("code", code).executeUpdate();
			return true;
		} else {
			this.deleteToken(code);
			return false;
		}
	}
}
