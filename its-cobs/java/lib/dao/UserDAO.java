package lib.dao;

import java.util.List;
import lib.entity.User;
import lib.entity.userdetail;

public interface UserDAO extends AbstractDAO<User, Long> {

	String createUser(User user, String deviceId);

	List<User> getAll();

	User findUserById(String id);

	void deleteUserByID(String id);

	String checkLogin(String encrypted_name, String encrypted_password);

	int changeStatus(String id, String status);

	int changePassword(String id, String newPassword);

	User checkMail(String encrypted_name, String encrypted_email, String encrypted_phone);

	void changeUserProfile(String id, String encrypted_fullname, String encrypted_email, String encrypted_phone);

	userdetail getUserDetail(String id);

	User checkWithdraw(String id, String phoneRX, String password);

	String withdraw(Long amount, String id);

	String deposit(String coinCode, String id);

	void verifyUserProfile(String id, String encrypt_PhoneRX, String encrypted_Password);

}
