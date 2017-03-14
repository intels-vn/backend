package lib.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Token {
	@Id
	@Column(nullable = false, unique = true)
	private String token;
	@Column(nullable = false)
	private long fromTime;
	@Column(nullable = false)
	private long toTime;
	
	public Token() {
		super();
	}

	public String getCode() {
		return token;
	}
	public void setCode(String code) {
		this.token = code;
	}

	public long getFrom() {
		return fromTime;
	}
	public void setFrom(long from) {
		this.fromTime = from;
	}

	public long getTo() {
		return toTime;
	}
	public void setTo(long to) {
		this.toTime = to;
	}
}
