package common.entity;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.entity.enums.ReaderCardStatus;
import common.entity.enums.SubscriberHistoryType;
import common.entity.enums.UserType;

public class Subscriber extends User implements Serializable {
	private Integer subscriberNum;
	private String phoneNum;
	private Date graduationDate;
	private ReaderCard readerCard;

	public Subscriber(Integer usrId, String usrName, String usrPassword, String usrFirstName, String usrLastName,
			String usrEmail, UserType usrType, String phoneNum ,ReaderCard readerCard,Date graduationDate) {
		super(usrName, usrPassword, usrFirstName, usrLastName, usrId, usrEmail, usrType);
		
		this.subscriberNum = usrId;
		this.phoneNum = phoneNum;
		this.readerCard = readerCard;
		this.graduationDate = graduationDate;
		this.readerCard = readerCard;
	}
	
	public Date getGraduationDate() {
		return graduationDate;
	}

	public void setGraduationDate(Date graduationDate) {
		this.graduationDate = graduationDate;
	}
	
	public ReaderCard getReaderCard() {
		return readerCard;
	}

	public void setReaderCard(ReaderCard readerCard) {
		this.readerCard = readerCard;
	}

	public Integer getSubscriberNum() {
		return subscriberNum;
	}

	public void setSubscriberNum(Integer subscriberNum) {
		this.subscriberNum = subscriberNum;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	
	@Override
	public String toString() {
		return "Subscriber: subscriberNum=" + subscriberNum
		+ ", subscriberNum=" + phoneNum
		+ ", graduationDate=" + graduationDate
		+ ", readerCard=" + readerCard;
	}
}
