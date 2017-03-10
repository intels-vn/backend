package lib.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Block {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String deviceId;

	private String ipAddress;

	private long timeBlock;

	public Block() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public long getTimeBlock() {
		return timeBlock;
	}

	public void setTimeBlock(long timeBlock) {
		this.timeBlock = timeBlock;
	}

}
