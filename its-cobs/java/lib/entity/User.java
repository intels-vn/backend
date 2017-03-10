package lib.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

// Creating table
@Entity
public class User {
	@Id
	private String id;

	@Column(length = 255, unique = true, nullable = false)
	private String useraccount;

	@Column(length = 255, unique = true, nullable = false)
	private String phonenumber;
	
	@Column(length = 255, unique = true, nullable = false)
	private String phone_received_exchange;
	
	@Column(length = 255, nullable = false)
	private String fullname;

	@Column(length = 255, nullable = false)
	private String password;

	private Integer password_expired;

	@Column(length = 255)
	private String email;

	@Column(nullable = false)
	private Integer is_active;

	@Column(nullable = false)
	private Timestamp date_created;

	@Column(nullable = false)
	private Timestamp last_updated;

	@ManyToOne
	@JoinColumn(name = "user_role_id")
	private user_role user_role_id;

	@ManyToOne
	@JoinColumn(name = "user_group_id")
	private user_group user_group_id;

	public User() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUseraccount() {
		return useraccount;
	}

	public void setUseraccount(String useraccount) {
		this.useraccount = useraccount;
	}

	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer isPassword_expired() {
		return password_expired;
	}

	public void setPassword_expired(Integer password_expired) {
		this.password_expired = password_expired;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer isIs_active() {
		return is_active;
	}

	public void setIs_active(Integer is_active) {
		this.is_active = is_active;
	}

	public Timestamp getDate_created() {
		return date_created;
	}

	public void setDate_created(Timestamp date_created) {
		this.date_created = date_created;
	}

	public Timestamp getLast_updated() {
		return last_updated;
	}

	public void setLast_updated(Timestamp last_updated) {
		this.last_updated = last_updated;
	}

	public user_role getUser_role_id() {
		return user_role_id;
	}

	public void setUser_role_id(user_role user_role_id) {
		this.user_role_id = user_role_id;
	}

	public user_group getUser_group_id() {
		return user_group_id;
	}

	public void setUser_group_id(user_group user_group_id) {
		this.user_group_id = user_group_id;
	}

	public String getPhone_received_exchange() {
		return phone_received_exchange;
	}

	public void setPhone_received_exchange(String phone_received_exchange) {
		this.phone_received_exchange = phone_received_exchange;
	}

	
}
