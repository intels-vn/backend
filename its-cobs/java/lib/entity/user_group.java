package lib.entity;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class user_group {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(length = 64, nullable = false, unique = true)
	private String group_name;

	@Column(length = 255, nullable = false)
	private String description;

	@Column(nullable = false)
	private Long bet_limit;

	@Column(nullable = false)
	private Long transfer_limit;

	@Column(nullable = false)
	private Long withdraw_limit;

	@Column(nullable = false)
	private Timestamp date_created;

	@Column(nullable = false)
	private Timestamp last_updated;

	@OneToMany(mappedBy = "primaryKey.usergroup", cascade = CascadeType.ALL)
	private Set<bet_group_list> betgrouplist = new HashSet<bet_group_list>();

	public user_group() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGroup_name() {
		return group_name;
	}

	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getBet_limit() {
		return bet_limit;
	}

	public void setBet_limit(Long bet_limit) {
		this.bet_limit = bet_limit;
	}

	public Long getTransfer_limit() {
		return transfer_limit;
	}

	public void setTransfer_limit(Long transfer_limit) {
		this.transfer_limit = transfer_limit;
	}

	public Long getWithdraw_limit() {
		return withdraw_limit;
	}

	public void setWithdraw_limit(Long withdraw_limit) {
		this.withdraw_limit = withdraw_limit;
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

	public Set<bet_group_list> getBetGroupList() {
		return betgrouplist;
	}

	public void setBetGroupList(Set<bet_group_list> betgrouplist) {
		this.betgrouplist = betgrouplist;
	}

	public void addBetGroupList(bet_group_list betgrouplist) {
		this.betgrouplist.add(betgrouplist);
	}
}
