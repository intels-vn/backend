package lib.interactor;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import lib.dao.BlockDAO;
import lib.entity.Block;

@Repository
public class BlockMUC extends AbstractMUC<Block, Integer> implements BlockDAO{
	@Override
	public Block getBlockDeviceID(String deviceId, String ipAddress){
		String hql = "From Block b where b.deviceId = :deviceId and b.ipAddress = :ipAddress";
		Query query = getSession().createQuery(hql);
		query.setParameter("deviceId", deviceId);
		query.setParameter("ipAddress", ipAddress);
		return (Block) query.uniqueResult();
	}
}
