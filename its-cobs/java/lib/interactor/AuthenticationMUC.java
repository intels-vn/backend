package lib.interactor;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import lib.dao.AuthenticationDAO;
import lib.entity.Authentication;

@Repository
public class AuthenticationMUC extends AbstractMUC<Authentication, Integer> implements AuthenticationDAO{
	@Override
	public Authentication getAndroidInfo(String deviceId, String ipAddress){
		String hql = "From Authentication a where a.deviceId = :deviceId and a.ipAddress = :ipAddress";
		Query query = getSession().createQuery(hql);
		query.setParameter("deviceId", deviceId);
		query.setParameter("ipAddress", ipAddress);
		return (Authentication) query.uniqueResult();
	}
	
	
}
