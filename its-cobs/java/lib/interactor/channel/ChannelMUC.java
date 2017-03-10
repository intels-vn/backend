package lib.interactor.channel;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import lib.dao.ChannelDAO;
import lib.entity.Channel;
import lib.interactor.AbstractMUC;

@Repository
public class ChannelMUC extends AbstractMUC<Channel, Long> implements ChannelDAO{
	@Override
	@SuppressWarnings("unchecked")
	public List<Channel> getChannelList(){
		String hql = "From Channel";
		Query query = getSession().createQuery(hql);
		return query.list();
	}
}
