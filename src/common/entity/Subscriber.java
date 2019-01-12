package common.entity;

import java.io.Serializable;

import common.entity.enums.UserType;

public class Subscriber extends User implements Serializable {
	private Integer subscriberNum;
	private String phoneNum;
	private String Status;
	//ReaderCard readerCard;

//	Subscriber() {
//		this.readerCard = new ReaderCard();
//	}
	public Subscriber(Integer usrId, String usrName, String usrPassword, String usrFirstName, String usrLastName, String usrEmail, UserType usrType, String Status,String phoneNum) 
	{
		super(usrName,usrPassword, usrFirstName, usrLastName, usrId, usrEmail,usrType);
		this.Status=Status;
		this.subscriberNum= usrId;
		this.phoneNum=phoneNum;
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

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}


}
