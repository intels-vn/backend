package lib.dao;

import lib.entity.Block;

public interface BlockDAO extends AbstractDAO<Block, Integer>{

	Block getBlockDeviceID(String deviceId, String ipAddress);

}
