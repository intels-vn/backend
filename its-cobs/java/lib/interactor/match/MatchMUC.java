package lib.interactor.match;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import lib.dao.MatchDAO;
import lib.entity.Matcheess;
import lib.interactor.AbstractMUC;

@Repository
public class MatchMUC extends AbstractMUC<Matcheess, Long> implements MatchDAO{
	@SuppressWarnings("unchecked")
	@Override
	public List<Matcheess> getMatchInfo(Long id){
		String hql = "From Matcheess m where m.channel.id =:id";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		return query.list();
	}
}
