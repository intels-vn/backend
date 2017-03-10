package lib.entity;

import java.sql.Timestamp;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Transient;

@Entity
@AssociationOverrides({
	@AssociationOverride(name = "primaryKey.betproduct", 
		joinColumns = @JoinColumn(name = "bet_product_id")),
	@AssociationOverride(name = "primaryKey.usergroup", 
		joinColumns = @JoinColumn(name = "user_group_id")) })
public class bet_group_list {
	// composite-id key
	@EmbeddedId
	private bet_group_Id primaryKey = new bet_group_Id();

	@Column(nullable = false)
	private Timestamp date_created;

	@Column(nullable = false)
	private Timestamp last_updated;
	
	public bet_group_Id getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(bet_group_Id primaryKey) {
		this.primaryKey = primaryKey;
	}
	
	@Transient
	public bet_product getBetProduct() {
		return getPrimaryKey().getBetProduct();
	}

	public void setBetProduct(bet_product betproduct) {
		getPrimaryKey().setBetProduct(betproduct);
	}
	
	@Transient
	public user_group getUserGroup() {
		return getPrimaryKey().getUserGroup();
	}

	public void setUserGroup(user_group group) {
		getPrimaryKey().setUserGroup(group);
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


}
