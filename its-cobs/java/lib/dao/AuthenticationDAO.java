package lib.dao;

import lib.entity.Authentication;

public interface AuthenticationDAO extends AbstractDAO<Authentication, Integer>{

	Authentication getAndroidInfo(String deviceId, String ipAddress);

}
