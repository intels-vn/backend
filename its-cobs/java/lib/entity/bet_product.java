package lib.entity;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class bet_product {
	@Id
	@GeneratedValue
	private Long id;

	private Integer number_element;

	@Column(length = 255)
	private String bet_product_list;

	@Column(nullable = false)
	private Timestamp date_created;

	@Column(nullable = false)
	private Timestamp last_updated;
	
	@OneToMany(mappedBy = "primaryKey.betproduct", cascade = CascadeType.ALL)
	private Set<bet_group_list> betgrouplist = new HashSet<bet_group_list>();

	public bet_product() {
	}

	public bet_product(Long id, Integer number_element, String bet_product_list, Timestamp date_created,
			Timestamp last_updated) {
		this.id = id;
		this.number_element = number_element;
		this.bet_product_list = bet_product_list;
		this.date_created = date_created;
		this.last_updated = last_updated;
	}

	public void addGroup(bet_group_list betgrouplist) {
		this.betgrouplist.add(betgrouplist);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getNumber_element() {
		return number_element;
	}

	public void setNumber_element(Integer number_element) {
		this.number_element = number_element;
	}

	public String getBet_product_list() {
		return bet_product_list;
	}

	public void setBet_product_list(String bet_product_list) {
		this.bet_product_list = bet_product_list;
	}

	public Timestamp getDate_created() {
		return date_created;
	}

	public void setDate_created(Timestamp date_created) {
		this.date_created = date_created;
	}

	public Timestamp getDate_updated() {
		return last_updated;
	}

	public void setDate_updated(Timestamp date_updated) {
		this.last_updated = date_updated;
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
