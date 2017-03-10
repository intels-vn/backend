package lib.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class userdetail implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn
	private User user_id;

	@Column(nullable = false)
	private Long coin_amount;

	@Column(nullable = false)
	private Long bet_match_total;

	@Column(nullable = false)
	private Long win_match_total;

	@Column(nullable = false)
	private Long lose_match_total;

	public userdetail() {
		super();
		// TODO Auto-generated constructor stub
	}

	public User getUser_id() {
		return user_id;
	}

	public void setUser_id(User user_id) {
		this.user_id = user_id;
	}

	public Long getCoin_amount() {
		return coin_amount;
	}

	public void setCoin_amount(Long coin_amount) {
		this.coin_amount = coin_amount;
	}

	public Long getBet_match_total() {
		return bet_match_total;
	}

	public void setBet_match_total(Long bet_match_total) {
		this.bet_match_total = bet_match_total;
	}

	public Long getWin_match_total() {
		return win_match_total;
	}

	public void setWin_match_total(Long win_match_total) {
		this.win_match_total = win_match_total;
	}

	public Long getLose_match_total() {
		return lose_match_total;
	}

	public void setLose_match_total(Long lose_match_total) {
		this.lose_match_total = lose_match_total;
	}

}
