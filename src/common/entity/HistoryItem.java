package common.entity;

import java.io.Serializable;
import java.util.Date;

import common.entity.enums.SubscriberHistoryType;
/**
 * The HistoryItem class represent the history items on reader card and implementing Serializable
 * @author  Kfir Wilfand
 * @author Bar Korkos
 * @author Zehavit Otmazgin
 * @author Noam Drori
 * @author Sapir Hochma
 */
public class HistoryItem implements Serializable {
	private Date date;
	private String description;
	private SubscriberHistoryType subscriberHistoryType;
	private Integer subId;
	/**
	 * HistoryItem class constructor use for display in ListView
	 * @param date             date of action
	 * @param description      description of action
	 */
	public HistoryItem(Date date, String description) {
		this.date = date;
		this.description = description;
	}
	/**
	 * HistoryItem class constructor use for send object from client to server
	 * @param subId                       subscriber id
	 * @param description                 action description
	 * @param subscriberHistoryType       history type
	 */
	public HistoryItem(Integer subId, String description,SubscriberHistoryType subscriberHistoryType) {
		this.subId = subId;
		this.description = description;
		this.subscriberHistoryType = subscriberHistoryType;
	}
	/**
	 * toString override of the class object
	 */
	@Override
	public String toString() {
		return date + ": " + description;
	}

	public Integer getSubId() {
		return subId;
	}
	public String getDescription() {
		return description;
	}
	public SubscriberHistoryType getSubscriberHistoryType() {
		return subscriberHistoryType;
	}
}
