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
	/**
	 * HistoryItem class constructor
	 * @param date
	 * @param description
	 * @return HistoryItem object
	 */
	public HistoryItem(Date date, String description) {
		this.date = date;
		this.description = description;
	}
	/**
	 * toString override of the class object
	 */
	@Override
	public String toString() {
		return date + ": " + description;
	}

}
