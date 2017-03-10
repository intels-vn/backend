package lib.dao;

import java.util.List;

import lib.entity.Channel;

public interface ChannelDAO extends AbstractDAO<Channel, Long>{

	List<Channel> getChannelList();

}
