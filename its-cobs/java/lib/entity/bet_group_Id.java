package lib.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

@Embeddable
public class bet_group_Id implements Serializable{
	private static final long serialVersionUID = 1L;

	@ManyToOne(cascade = CascadeType.ALL)
	private bet_product betproduct;
	
	@ManyToOne(cascade = CascadeType.ALL)
	private user_group usergroup;

	
	public bet_product getBetProduct() {
		return betproduct;
	}

	public void setBetProduct(bet_product betproduct) {
		this.betproduct = betproduct;
	}

	
	public user_group getUserGroup() {
		return usergroup;
	}

	public void setUserGroup(user_group usergroup) {
		this.usergroup = usergroup;
	}
}
