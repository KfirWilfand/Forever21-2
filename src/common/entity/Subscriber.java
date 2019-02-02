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

/**
 * The Subscriber extends User implements Serializable represent the Subscriber entity
 * @author  Kfir Wilfand
 * @author Bar Korkos
 * @author Zehavit Otmazgin
 * @author Noam Drori
 * @author Sapir Hochma
 */
public class Subscriber extends User implements Serializable {
	/** subscriberNum is the subscriber number */
	private Integer subscriberNum;
	/** phoneNum is the subscriber phone number */
	private String phoneNum;
	/** graduationDate is the subscriber graduation dater */
	private Date graduationDate;
	private ReaderCard readerCard;
	/**
	 * Subscriber constructor
	 * @param usrId                       user id
	 * @param usrName                     user name
	 * @param usrPassword                 user password
	 * @param usrFirstName                user first name
	 * @param usrLastName                 user last name
	 * @param usrEmail                    user email
	 * @param usrType                     user type
	 * @param phoneNum                    user phone
	 * @param readerCard                  user reader card
	 * @param graduationDate              user graduation date
	 */
	public Subscriber(Integer usrId, String usrName, String usrPassword, String usrFirstName, String usrLastName,
			String usrEmail, UserType usrType, String phoneNum ,ReaderCard readerCard,Date graduationDate) {
		super(usrName, usrPassword, usrFirstName, usrLastName, usrId, usrEmail, usrType);
		
		this.subscriberNum = usrId;
		this.phoneNum = phoneNum;
		this.readerCard = readerCard;
		this.graduationDate = graduationDate;
		this.readerCard = readerCard;
	}
	/**
	 * getGraduationDate getter of subscriber graduation date
	 * @return graduationDate          subscriber graduationDate
	 */
	public Date getGraduationDate() {
		return graduationDate;
	}
	/**
	 * setGraduationDate getter of subscriber graduation date
	 * @param graduationDate  subscriber graduationDate
	 */
	public void setGraduationDate(Date graduationDate) {
		this.graduationDate = graduationDate;
	}
	/**
	 * getReaderCard getter of subscriber reader card
	 * @return readerCard         subscriber reader card 
	 */
	public ReaderCard getReaderCard() {
		return readerCard;
	}
	/**
	 * setReaderCard setter of subscriber reader card
	 * @param readerCard       subscriber reader card 
	 */
	public void setReaderCard(ReaderCard readerCard) {
		this.readerCard = readerCard;
	}
	/**
	 * getSubscriberNum getter of subscriber number
	 * @return subscriberNum     subscriber Number
	 */
	public Integer getSubscriberNum() {
		return subscriberNum;
	}
	/**
	 * setSubscriberNum setter of subscriber number
	 * @param subscriberNum   subscriber Number
	 */
	public void setSubscriberNum(Integer subscriberNum) {
		this.subscriberNum = subscriberNum;
	}
	/**
	 * getPhoneNum getter of subscriber phone number
	 * @return phoneNum    subscriber phone number
	 */
	public String getPhoneNum() {
		return phoneNum;
	}
	/**
	 * setPhoneNum setter of subscriber phone number
	 * @param phoneNum subscriber phone number
	 */
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	/** toString override method from Object class */
	@Override
	public String toString() {
		return "Subscriber: subscriberNum=" + subscriberNum
		+ ", subscriberNum=" + phoneNum
		+ ", graduationDate=" + graduationDate
		+ ", readerCard=" + readerCard;
	}
}
