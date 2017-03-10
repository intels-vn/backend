package lib.interactor;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import lib.dao.ConfigDAO;
import lib.entity.Config;

@Repository
public class ConfigMUC extends AbstractMUC<Config, Integer> implements ConfigDAO {

	@Override
	public String getPrivateKey() {
		String sql = "SELECT c.value FROM Config c WHERE c.name = 'Private Key'";
		Query query = getSession().createSQLQuery(sql);
		return (String) query.uniqueResult();
	}

	@Override
	public String getTokenKey() {
		String sql = "SELECT c.value FROM Config c WHERE c.name = 'Token Key'";
		Query query = getSession().createSQLQuery(sql);
		return (String) query.uniqueResult();
	}

	@Override
	public long getTokenTimeOut() {
		String sql = "SELECT c.value FROM Config c WHERE c.name = 'Token Time Out'";
		Query query = getSession().createSQLQuery(sql);
		return Long.parseLong((String) query.uniqueResult()) * 60 * 1000;
	}

}
