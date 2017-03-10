package web.user;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lib.dao.HttpStatusDAO;
import lib.dao.TokenDAO;
import lib.dao.UserDAO;
import lib.entity.Token;
import lib.entity.User;
import lib.entity.userdetail;
import lib.support.encryptDecrypt;

@RestController
@Api(value = "Users", description = "API for Users")
public class UserPresenter {
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private TokenDAO tokenDAO;
	
	@Autowired
	private HttpStatusDAO httpStatusDAO;

	// REGISTER
	@ApiOperation(value = "Create User", notes = "Retriving status 200 if success", response = User.class)
	@PostMapping(value = "/users", produces = "application/json; charset=UTF-8")
	public Map<String, Object> createUser(@RequestHeader(value = "Localization") String key,
			@RequestHeader(value = "DeviceId") String deviceId, @RequestBody User user) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		String userData = this.userDAO.createUser(user, deviceId);
		Token token = this.tokenDAO.getToken(this.tokenDAO.createToken(user));

		if (userData != "Error") {
			data.put("token", token);
			data.put("id", userData);
			
			result.put("data", data);
			result.put("status", "200");
			result.put("message", this.httpStatusDAO.getHttpStatusByKeyByLocalization(key, "200"));
			return result;
		}
		else if(userData != "0"){
			result.put("status", "400");
			result.put("message", this.httpStatusDAO.getHttpStatusByKeyByLocalization(key, "400"));
			return result;
			
		}else {
			result.put("status", "500");
			result.put("message", this.httpStatusDAO.getHttpStatusByKeyByLocalization(key, "500"));
			return result;
		}
	}
	
	// LOG IN
	@ApiOperation(value = "Login", notes = "Retrieving a Token", response = User.class)
	@PostMapping(value = "/users/login", produces = "application/json")
	public Map<String, Object> login(@RequestHeader(value = "Localization") String key,
			@RequestParam("username") String encrypted_username, @RequestParam("password") String encrypted_password) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();

		String response = this.userDAO.checkLogin(encrypted_username, encrypted_password);

		if (response.equals("400")) {
			result.put("status", "400");
			result.put("message", this.httpStatusDAO.getHttpStatusByKeyByLocalization(key, "400"));
			return result;
		}else if(response.equals("500")){
			result.put("status", "500");
			result.put("message", this.httpStatusDAO.getHttpStatusByKeyByLocalization(key, "500"));
			return result;
		} else {
			User user = this.userDAO.findUserById(response);
			Token token = this.tokenDAO.getToken(this.tokenDAO.createToken(user));
			data.put("token", token);
			data.put("id", user.getId());

			result.put("data", data);
			result.put("status", "200");
			result.put("message", this.httpStatusDAO.getHttpStatusByKeyByLocalization(key, "200"));
			return result;
		}
	}

	// LOG OUT
	@ApiOperation(value = "Log Out", response = User.class)
	@DeleteMapping(value = "/user/{id}")
	public Map<String, Object> deleteUser(@RequestHeader(value = "Localization") String key,
			@RequestHeader(value = "Authorization") String code, @PathVariable String id) {
		Map<String, Object> result = new HashMap<String, Object>();

		Token token = this.tokenDAO.getToken(code);
		if (token == null) {
			result.put("status", "400");
			result.put("message", this.httpStatusDAO.getHttpStatusByKeyByLocalization(key, "400"));
			return result;
		}

		User user = this.userDAO.findUserById(id);
		if (user == null) {
			result.put("status", "404");
			result.put("message", this.httpStatusDAO.getHttpStatusByKeyByLocalization(key, "404"));
			return result;
		}

		this.tokenDAO.deleteToken(code);
		result.put("status", "200");
		result.put("message", this.httpStatusDAO.getHttpStatusByKeyByLocalization(key, "200"));
		return result;
	}

	// UPDATE USER (PASSWORD, EMAIL, PHONE)
	@ApiOperation(value = "Update User", notes = "Update Only a User", response = User.class)
	@PutMapping("/user/{id}/profile")
	public Map<String, Object> updateUser(@RequestHeader(value = "Localization") String key,
			@RequestHeader(value = "Authorization") String code, @PathVariable String id,
			@RequestParam("password") String encrypted_password, @RequestParam("email") String encrypted_email,
			@RequestParam("phone") String encrypted_phone, @RequestParam("fullname") String encrypted_fullname,
			@RequestParam("phoneRX") String encrypted_phoneRX) {
		Map<String, Object> result = new HashMap<String, Object>();

		Token token = this.tokenDAO.getToken(code);
		if (token == null) {
			result.put("status", "400");
			result.put("message", this.httpStatusDAO.getHttpStatusByKeyByLocalization(key, "400"));
			return result;
		}

		User user = this.userDAO.findUserById(id);
		if (user == null) {
			result.put("status", "404");
			result.put("message", this.httpStatusDAO.getHttpStatusByKeyByLocalization(key, "404"));
			return result;
		}

		this.userDAO.changeUserProfile(id, encrypted_fullname, encrypted_password, encrypted_email, encrypted_phone,
				encrypted_phoneRX);

		this.tokenDAO.updateToken(code);

		result.put("status", "200");
		result.put("message", this.httpStatusDAO.getHttpStatusByKeyByLocalization(key, "200"));
		return result;
	}

	// Change Password
	@ApiOperation(value = "Change Password", notes = "Need old password and new password", response = User.class)
	@PutMapping("/users/{id}")
	public Map<String, Object> changePassword(@RequestHeader(value = "Localization") String key,
			@RequestHeader(value = "Authorization") String code, @PathVariable String id,
			@RequestParam("oldpass") String encrypted_oldpassword,
			@RequestParam("newpass") String encrypted_newpassword) {
		Map<String, Object> result = new HashMap<String, Object>();

		Token token = this.tokenDAO.getToken(code);
		if (token == null) {
			result.put("status", "400");
			result.put("message", this.httpStatusDAO.getHttpStatusByKeyByLocalization(key, "400"));
			return result;
		}

		User user = this.userDAO.findUserById(id);

		if (user == null) {
			result.put("status", "404");
			result.put("message", this.httpStatusDAO.getHttpStatusByKeyByLocalization(key, "404"));
			return result;
		}

		System.out.println("-----------" + user.getPassword());
		if (!user.getPassword().equals(encrypted_oldpassword)) {
			result.put("status", "405");
			result.put("message", this.httpStatusDAO.getHttpStatusByKeyByLocalization(key, "404"));
			return result;
		}

		this.userDAO.changePassword(id, encrypted_newpassword);

		this.tokenDAO.updateToken(code);

		result.put("status", "200");
		result.put("message", this.httpStatusDAO.getHttpStatusByKeyByLocalization(key, "200"));
		return result;
	}
	
	// RESET PASSWORD
	@ApiOperation(value = "Reset password by using username, email and phone", notes = "if request is success, we will a message 'We will contact you later. Please wait.' and the request will save in a log file.", response = User.class)
	@PostMapping(value = "/user", produces = "application/json")
	public Map<String, Object> resetPassword(@RequestHeader(value = "Localization") String key,
			@RequestParam("username") String encrypted_username, @RequestParam("email") String encrypted_email,
			@RequestParam("phone") String encrypted_phone) throws IOException {
		Map<String, Object> result = new HashMap<String, Object>();

		User user = this.userDAO.checkMail(encrypted_username, encrypted_email, encrypted_phone);

		if (user == null) {
			result.put("status", "400");
			result.put("message", this.httpStatusDAO.getHttpStatusByKeyByLocalization(key, "400"));
			return result;
		}

		FileWriter writer = new FileWriter(new File("D:/eclipse-workspace/its-cobs/java/config/webapp/WEB-INF/logfile/logs.txt"), true);
		BufferedWriter bw = new BufferedWriter(writer);
		bw.append("--- " + new Timestamp(System.currentTimeMillis()) + " ---");
		bw.newLine();
		bw.append("Username: [" + user.getUseraccount() + "] - Phone: [" + user.getPhonenumber() + "] - Email: ["
				+ user.getEmail() + "]");
		bw.newLine();
		bw.close();

		result.put("data", "We will contact you later. Please wait. :)");
		result.put("status", "200");
		result.put("message", this.httpStatusDAO.getHttpStatusByKeyByLocalization(key, "200"));
		return result;
	}

	// Get User Coin
	@ApiOperation(value = "Get user Coin", notes = "Get User Coin", response = User.class)
	@GetMapping(value = "/user/{id}/coin", produces = "application/json")
	public Map<String, Object> getUserCoin(@RequestHeader(value = "Localization") String key,
			@RequestHeader(value = "Authorization") String code, @PathVariable String id) {
		Map<String, Object> result = new HashMap<String, Object>();

		Token token = this.tokenDAO.getToken(code);
		if (token == null) {
			result.put("status", "400");
			result.put("message", this.httpStatusDAO.getHttpStatusByKeyByLocalization(key, "400"));
			return result;
		}

		userdetail userdetail = this.userDAO.getUserDetail(id);
		if (userdetail == null) {
			result.put("status", "400");
			result.put("message", this.httpStatusDAO.getHttpStatusByKeyByLocalization(key, "400"));
			return result;
		}

		this.tokenDAO.updateToken(code);
		result.put("data", userdetail.getCoin_amount());
		result.put("status", "200");
		result.put("message", this.httpStatusDAO.getHttpStatusByKeyByLocalization(key, "200"));
		return result;
	}

	// Get User Vip
	@ApiOperation(value = "Get user Vip", notes = "Get User Vip", response = User.class)
	@GetMapping(value = "/user/{id}/vip", produces = "application/json")
	public Map<String, Object> getUserVip(@RequestHeader(value = "Localization") String key,
			@RequestHeader(value = "Authorization") String code, @PathVariable String id) {
		Map<String, Object> result = new HashMap<String, Object>();

		Token token = this.tokenDAO.getToken(code);
		if (token == null) {
			result.put("status", "400");
			result.put("message", this.httpStatusDAO.getHttpStatusByKeyByLocalization(key, "400"));
			return result;
		}

		User user = this.userDAO.findUserById(id);
		if (user == null) {
			result.put("status", "400");
			result.put("message", this.httpStatusDAO.getHttpStatusByKeyByLocalization(key, "400"));
			return result;
		}

		this.tokenDAO.updateToken(code);
		result.put("data", user.getUser_group_id().getId());
		result.put("status", "200");
		result.put("message", this.httpStatusDAO.getHttpStatusByKeyByLocalization(key, "200"));
		return result;
	}

	// Get User Profile
	@ApiOperation(value = "Get user profile", notes = "Get User Profile", response = User.class)
	@GetMapping(value = "/user/{id}/profile", produces = "application/json")
	public Map<String, Object> getUserProfile(@RequestHeader(value = "Localization") String key,
			@RequestHeader(value = "Authorization") String code, @PathVariable String id) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();

		Token token = this.tokenDAO.getToken(code);
		if (token == null) {
			result.put("status", "400");
			result.put("message", this.httpStatusDAO.getHttpStatusByKeyByLocalization(key, "400"));
			return result;
		}

		User user = this.userDAO.findUserById(id);
		if (user == null) {
			result.put("status", "400");
			result.put("message", this.httpStatusDAO.getHttpStatusByKeyByLocalization(key, "400"));
			return result;
		}
		
		this.tokenDAO.updateToken(code);
		data.put("id", user.getId());
		data.put("useraccount", encryptDecrypt.encrypt(user.getUseraccount(), key));
		data.put("phonenumber", encryptDecrypt.encrypt(user.getPhonenumber(), key));
		data.put("phone_received_exchange", encryptDecrypt.encrypt(user.getPhone_received_exchange(), key));
		data.put("fullname", encryptDecrypt.encrypt(user.getFullname(), key));
		data.put("email", encryptDecrypt.encrypt(user.getEmail(), key));
		data.put("user_role", user.getUser_role_id().getId());
		data.put("user_group", user.getUser_group_id().getId());

		result.put("data", data);
		result.put("status", "200");
		result.put("message", this.httpStatusDAO.getHttpStatusByKeyByLocalization(key, "200"));
		return result;
	}
	
	@ApiOperation(value = "Get user summary", notes = "Get User Summary", response = User.class)
	@GetMapping(value = "/user/{id}", produces = "application/json")
	public Map<String, Object> getUserSummary(@RequestHeader(value = "Localization") String key,
			@RequestHeader(value = "Authorization") String code, @PathVariable String id) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();

		Token token = this.tokenDAO.getToken(code);
		if (token == null) {
			result.put("status", "400");
			result.put("message", this.httpStatusDAO.getHttpStatusByKeyByLocalization(key, "400"));
			return result;
		}

		User user = this.userDAO.findUserById(id);
		if (user == null) {
			result.put("status", "400");
			result.put("message", this.httpStatusDAO.getHttpStatusByKeyByLocalization(key, "400"));
			return result;
		}

		this.tokenDAO.updateToken(code);
		
		userdetail detail = this.userDAO.getUserDetail(id);
		data.put("totalWin", detail.getWin_match_total());
		data.put("totalLose", detail.getLose_match_total());
		data.put("coinAmount", detail.getCoin_amount());

		result.put("data", data);
		result.put("status", "200");
		result.put("message", this.httpStatusDAO.getHttpStatusByKeyByLocalization(key, "200"));
		return result;
	}

	// Withdraw
	@ApiOperation(value = "Withdraw", notes = "Retriving status 200 if success", response = User.class)
	@PostMapping(value = "/withdraw", produces = "application/json; charset=UTF-8")
	public Map<String, Object> withdraw(@RequestHeader(value = "Localization") String key,
			@RequestHeader(value = "Authorization") String code, @RequestParam("id") String id,
			@RequestParam("amount") Long amount, @RequestParam("phoneRX") String phoneRX,
			@RequestParam("password") String password) {
		Map<String, Object> result = new HashMap<String, Object>();
		User user = this.userDAO.checkWithdraw(id, phoneRX, password);
		Token token = this.tokenDAO.getToken(code);
		if (token == null) {
			result.put("status", "400");
			result.put("message", this.httpStatusDAO.getHttpStatusByKeyByLocalization(key, "400"));
			return result;
		}

		if (user == null) {
			result.put("status", "400");
			result.put("message", this.httpStatusDAO.getHttpStatusByKeyByLocalization(key, "400"));
			return result;
		}
		
		this.tokenDAO.updateToken(code);
		String data = this.userDAO.withdraw(amount, id);
		if (data == "unSuccess") {
			result.put("status", "400");
			result.put("message", this.httpStatusDAO.getHttpStatusByKeyByLocalization(key, "400"));
			return result;
		}
		else{
			result.put("data", data);
			result.put("status", "200");
			result.put("message", this.httpStatusDAO.getHttpStatusByKeyByLocalization(key, "200"));
			return result;
		}
	}
	
	// Deposit
	@ApiOperation(value = "Deposit", notes = "Retriving status 200 if success", response = User.class)
	@PostMapping(value = "/deposit", produces = "application/json; charset=UTF-8")
	public Map<String, Object> deposit(@RequestHeader(value = "Localization") String key,
			@RequestHeader(value = "Authorization") String code, @RequestParam("id") String id,
			@RequestParam("coinCode") String coinCode) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		Token token = this.tokenDAO.getToken(code);
		if (token == null) {
			result.put("status", "400");
			result.put("message", this.httpStatusDAO.getHttpStatusByKeyByLocalization(key, "400"));
			return result;
		}

		User user = this.userDAO.findUserById(id);
		if (user == null) {
			result.put("status", "400");
			result.put("message", this.httpStatusDAO.getHttpStatusByKeyByLocalization(key, "400"));
			return result;
		}
		
		this.tokenDAO.updateToken(code);
		String data = this.userDAO.deposit(coinCode, id);
		if (data == "unSuccess") {
			result.put("status", "400");
			result.put("message", this.httpStatusDAO.getHttpStatusByKeyByLocalization(key, "400"));
			return result;
		}
		else{
			result.put("data", data);
			result.put("status", "200");
			result.put("message", this.httpStatusDAO.getHttpStatusByKeyByLocalization(key, "200"));
			return result;
		}
	}
}
