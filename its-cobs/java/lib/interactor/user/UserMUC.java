package lib.interactor.user;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import lib.dao.AuthenticationDAO;
import lib.dao.BlockDAO;
import lib.dao.CardDAO;
import lib.dao.ConfigDAO;
import lib.dao.UserDAO;
import lib.entity.Authentication;
import lib.entity.Block;
import lib.entity.User;
import lib.entity.userdetail;
import lib.interactor.AbstractMUC;
import lib.support.encryptDecrypt;

@Repository
public class UserMUC extends AbstractMUC<User, Long> implements UserDAO {
	@Autowired
	private ConfigDAO configDAO;

	@Autowired
	private AuthenticationDAO authenticationDAO;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private BlockDAO blockDAO;
	
	@Autowired
	private CardDAO cardDAO;

	// CREATE USER
	@Override
	public String createUser(User user, String deviceId) {
		String ipAddress = request.getHeader("X-FORWARDED-FOR");
		String userAgent = request.getHeader("User-Agent");
		int isMobile = userAgent.indexOf("Mobile", 0);
		if (isMobile == -1) {
			UUID uuid = UUID.randomUUID();
			while (this.findUserById(uuid.toString()) != null) {
				uuid = UUID.randomUUID();
			}

			java.sql.Timestamp date = new java.sql.Timestamp(new java.util.Date().getTime());
			String key = this.configDAO.getPrivateKey();
			try {
				String sql = createSql(user, uuid, key, date);
				getSession().createSQLQuery(sql).executeUpdate();

				String userDetail = createUserDetail(uuid);
				getSession().createSQLQuery(userDetail).executeUpdate();
				return uuid.toString();
			} catch (Exception e) {
				return "Error";
			}
		}

		else {
			ipAddress = request.getRemoteAddr();
			Block isblock = blockDAO.getBlockDeviceID(deviceId, ipAddress);
			long currentTimestamp = System.currentTimeMillis();

			Authentication auth = authenticationDAO.getAndroidInfo(deviceId, ipAddress);
			if (isblock == null) {
				if (auth == null) {
					UUID uuid = UUID.randomUUID();
					while (this.findUserById(uuid.toString()) != null) {
						uuid = UUID.randomUUID();
					}

					java.sql.Timestamp date = new java.sql.Timestamp(new java.util.Date().getTime());
					String key = this.configDAO.getPrivateKey();
					try {
						String sql = createSql(user, uuid, key, date);
						getSession().createSQLQuery(sql).executeUpdate();

						String userDetail = createUserDetail(uuid);
						getSession().createSQLQuery(userDetail).executeUpdate();

						String sql2 = "INSERT INTO Authentication (deviceId, ipAddress, timeAccess)" + " VALUES ('"
								+ deviceId + "', N'" + ipAddress + "', N'" + currentTimestamp + "')";
						getSession().createSQLQuery(sql2).executeUpdate();
						return uuid.toString();
					} catch (Exception e) {
						return "Error";
					}

				} else {
					long mobileAccessTimestamp = authenticationDAO.getAndroidInfo(deviceId, ipAddress).getTimeAccess();
					long timeCalculate = (currentTimestamp - mobileAccessTimestamp) / 1000;
					if (timeCalculate > 100) {
						UUID uuid = UUID.randomUUID();
						while (this.findUserById(uuid.toString()) != null) {
							uuid = UUID.randomUUID();
						}

						java.sql.Timestamp date = new java.sql.Timestamp(new java.util.Date().getTime());
						String key = this.configDAO.getPrivateKey();
						try {
							String sql = createSql(user, uuid, key, date);
							getSession().createSQLQuery(sql).executeUpdate();

							String userDetail = createUserDetail(uuid);
							getSession().createSQLQuery(userDetail).executeUpdate();

							String hql = "UPDATE Authentication a SET a.timeAccess = :currentTimestamp where a.deviceId = :deviceId and a.ipAddress = :ipAddress";
							Query query = getSession().createQuery(hql);
							query.setParameter("timeAccess", currentTimestamp);
							query.setParameter("deviceId", deviceId);
							query.setParameter("ipAddress", ipAddress);
							query.executeUpdate();
							return uuid.toString();
						} catch (Exception e) {
							return "Error";
						}
					} else {
						String sql = "INSERT into Block (deviceId,ipAddress,timeBlock)" + " VALUES ('" + deviceId
								+ "', N'" + ipAddress + "', N'" + currentTimestamp + "')";
						getSession().createSQLQuery(sql).executeUpdate();
						return "0";
					}
				}
			} else
				return "0";
		}
	}

	// GET ALL USERS
	@SuppressWarnings("unchecked")
	@Override
	public List<User> getAll() {
		String sql = "SELECT * FROM User";
		return getSession().createSQLQuery(sql).list();
	}

	// FIND USER BY ID
	@Override
	public User findUserById(String id) {
		String hql = "FROM User u WHERE u.id = :id";
		return (User) getSession().createQuery(hql).setParameter("id", id).uniqueResult();
	}

	// DELETE USER
	@Override
	public void deleteUserByID(String id) {
		String hql = "DELETE FROM User WHERE id = :id";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.executeUpdate();
	}

	// CHECKING LOG IN
	@Override
	public String checkLogin(String encrypted_username, String encrypted_password) {
		if(encryptDecrypt.checkDecrypt(encrypted_username, this.configDAO.getPrivateKey()) == true && encryptDecrypt.checkDecrypt(encrypted_password, this.configDAO.getPrivateKey()) == true) {
			String sql = "FROM User u WHERE u.useraccount = :username AND u.password = :password";
			Query query = getSession().createQuery(sql);
			query.setParameter("username", encryptDecrypt.decrypt(encrypted_username, this.configDAO.getPrivateKey()))
			.setParameter("password", encrypted_password);
			
			User user = (User) query.uniqueResult();
			
			if(user != null)
				return user.getId();
			else return "400";
		} else return "500";
	}

	// CHANGING STATUS OF ADMIN(such as: main-admin, sub-admin, inactive-admin)
	@Override
	public int changeStatus(String id, String status) {
		String sql = "Update User SET status = :status WHERE id = :id";
		Query query = getSession().createQuery(sql);
		query.setParameter("status", status);
		query.setParameter("id", id);
		return query.executeUpdate();
	}

	// CHANGING PASSWORD
	@Override
	public int changePassword(String id, String newPassword) {
		String sql = "Update User u SET u.password = :newPassword WHERE u.id = :id";
		Query query = getSession().createQuery(sql);
		query.setParameter("newPassword", newPassword);
		query.setParameter("id", id);
		return query.executeUpdate();
	}

	// CHECKING EMAIL
	@Override
	public User checkMail(String encrypted_username, String encrypted_email, String encrypted_phone) {
		String hql = "From User u where u.useraccount=:username and u.email=:email and u.phonenumber=:phone";
		return (User) getSession().createQuery(hql)
				.setParameter("username", encryptDecrypt.decrypt(encrypted_username, this.configDAO.getPrivateKey()))
				.setParameter("email", encryptDecrypt.decrypt(encrypted_email, this.configDAO.getPrivateKey()))
				.setParameter("phone", encryptDecrypt.decrypt(encrypted_phone, this.configDAO.getPrivateKey()))
				.uniqueResult();
	}

	// Change User Profile
	@Override
	public void changeUserProfile(String id, String encrypted_fullname, String encrypted_password,
			String encrypted_email, String encrypted_phone, String encrypted_phoneRX) {
		String hql = "UPDATE User u SET u.password = :password, u.email = :email, u.phonenumber = :phone, u.fullname = :fullname, u.phone_received_exchange = :phoneRX WHERE u.id = :id";
		Query query = getSession().createQuery(hql);
		query.setParameter("fullname", encryptDecrypt.decrypt(encrypted_fullname, this.configDAO.getPrivateKey()));
		query.setParameter("password", encrypted_password);
		query.setParameter("email", encryptDecrypt.decrypt(encrypted_email, this.configDAO.getPrivateKey()));
		query.setParameter("phone", encryptDecrypt.decrypt(encrypted_phone, this.configDAO.getPrivateKey()));
		query.setParameter("phoneRX", encryptDecrypt.decrypt(encrypted_phoneRX, this.configDAO.getPrivateKey()));
		query.setParameter("id", id);
		query.executeUpdate();
	}

	// Get User Detail
	@Override
	public userdetail getUserDetail(String id) {
		String hql = "FROM userdetail u where u.user_id.id =:id";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		return (userdetail) query.uniqueResult();
	}

	// ** WITHDRAW **
	// Check User id, phone, password
	@Override
	public User checkWithdraw(String id, String phoneRX, String password) {
		String hql = "FROM User u where u.id = :id AND u.phone_received_exchange = :phoneRX AND u.password = :password";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		query.setParameter("phoneRX", encryptDecrypt.decrypt(phoneRX, this.configDAO.getPrivateKey()));
		query.setParameter("password", password);
		return (User) query.uniqueResult();

	}

	@Override
	public String withdraw(Long amount, String id) {
		User user = findUserById(id);
		Long currentcoin = getUserDetail(id).getCoin_amount();

		Long coinBalance = currentcoin - amount;
		if (coinBalance >= 0) {
			String hql = "UPDATE userdetail u SET u.coin_amount = :amount WHERE u.user_id = :id";
			Query query = getSession().createQuery(hql);
			query.setParameter("amount",coinBalance);
			query.setParameter("id", user);
			query.executeUpdate();
			return Long.toString(getUserDetail(id).getCoin_amount());
		} else {
			return "unSuccess";
		}
	}
	
	// ** DEPOSIT **
	@Override
	public String deposit(String coinCode, String id) {
		User user = findUserById(id);
		Long amount = cardDAO.getCard(coinCode).getAmount();
		Long currentcoin = getUserDetail(id).getCoin_amount();

		Long coinBalance = currentcoin + amount;
		if (coinBalance >= 0) {
			String hql = "UPDATE userdetail u SET u.coin_amount = :amount WHERE u.user_id = :id";
			Query query = getSession().createQuery(hql);
			query.setParameter("amount",coinBalance);
			query.setParameter("id", user);
			query.executeUpdate();
			return Long.toString(getUserDetail(id).getCoin_amount());
		} else {
			return "unSuccess";
		}
	}
	
	// ** Support Function ** //
	private String createSql(User user, UUID uuid, String key, Timestamp date) {
		String sql = "INSERT INTO User (id, useraccount, phonenumber, phone_received_exchange, fullname, password, password_expired, email, user_role_id, user_group_id, is_active,  date_created, last_updated)"
				+ " VALUES ('" + uuid.toString() + "',N'" + encryptDecrypt.decrypt(user.getUseraccount(), key) + "', N'"
				+ encryptDecrypt.decrypt(user.getPhonenumber(), key) + "', N'"
				+ encryptDecrypt.decrypt(user.getPhone_received_exchange(), key) + "', N'"
				+ encryptDecrypt.decrypt(user.getFullname(), key) + "', N'" + user.getPassword() + "', N'" + 1 + "', N'"
				+ encryptDecrypt.decrypt(user.getEmail(), key) + "', N'" + user.getUser_role_id().getId() + "', N'"
				+ user.getUser_group_id().getId() + "', N'" + 1 + "', N'" + date + "', N'" + date + "')";
		return sql;
	}

	private String createUserDetail(UUID uuid) {
		String sql = "INSERT INTO userdetail (user_id_id, coin_amount, bet_match_total, win_match_total, lose_match_total)"
				+ " VALUES ('" + uuid.toString() + "','" + 0 + "', '" + 0 + "', '" + 0 + "', '" + 0 + "')";
		return sql;
	}
}
