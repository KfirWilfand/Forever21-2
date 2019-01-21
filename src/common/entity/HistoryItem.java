package common.entity;

import java.io.Serializable;
import java.util.Date;

import common.entity.enums.SubscriberHistoryType;

public class HistoryItem implements Serializable {
	private Date date;
	private String description;

	public HistoryItem(Date date, String description) {
		this.date = date;
		this.description = description;
	}
	
	@Override
	public String toString() {
		return date + ": " + description;
	}

}
