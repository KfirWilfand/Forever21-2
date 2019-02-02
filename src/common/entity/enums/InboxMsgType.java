package common.entity.enums;
/**
 * enum class that present all the types of inbox messages
 */
public enum InboxMsgType {
	
	LockReader, Reminder, Alert;
	
	
	public static InboxMsgType stringToEnum(String userType){
		switch(userType) {
		case "LockReader":
			return InboxMsgType.LockReader;
		case "Reminder":
			return InboxMsgType.Reminder;
		case "Alert":
			return InboxMsgType.Alert;
		}
		
		return null;	
	}
}
