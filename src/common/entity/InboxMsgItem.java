package common.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import common.entity.enums.InboxMsgType;
/**
 * The InboxMsgItem class represent the message that send to the inbox of the users
 * @author  Kfir Wilfand
 * @author Bar Korkos
 * @author Zehavit Otmazgin
 * @author Noam Drori
 * @author Sapir Hochma
 */
public class InboxMsgItem implements Serializable{
	

	private int userID;
	private String title;
	private String body;
	InboxMsgType type;
	boolean is_read;
	Timestamp time;
	
	public InboxMsgItem(int userID, String title, String body, InboxMsgType type, boolean is_read, Timestamp time) {
		super();
		this.userID = userID;
		this.title = title;
		this.body = body;
		this.type = type;
		this.is_read = is_read;
		this.time = time;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public InboxMsgType getType() {
		return type;
	}

	public void setType(InboxMsgType type) {
		this.type = type;
	}

	public boolean isIs_read() {
		return is_read;
	}

	public void setIs_read(boolean is_read) {
		this.is_read = is_read;
	}

	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

	@Override
	public String toString() {
		if(!this.is_read)
			return "*"+time+" :"+ title;
		else
			return time+" :"+ title;
	}
	

	
	
}
