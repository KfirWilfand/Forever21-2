package common.entity.enums;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * The ReaderCardStatus enum represent the reader card status
 * @author  Kfir Wilfand
 * @author Bar Korkos
 * @author Zehavit Otmazgin
 * @author Noam Drori
 * @author Sapir Hochma
 */
public enum ReaderCardStatus {
	Lock, Hold,Active;
	/**
	 * stringToEnum is taking a string and convert to enum
	 * @param userType contains the message from the client 
	 * @return ReaderCardStatus
	 */
	public static ReaderCardStatus stringToEnum(String userType){
		switch(userType) {
		case "Lock":
			return ReaderCardStatus.Lock;
		case "Hold":
			return ReaderCardStatus.Hold;
		case "Active":
			return ReaderCardStatus.Active;
		}
		
		return null;	
	}
	/**
	 * getEnums method get enums
	 * @return  list             List of reader card history
	 */
	public static List<ReaderCardStatus> getEnums(){
		List<ReaderCardStatus> list = new ArrayList<ReaderCardStatus>();
		list.add(Lock);
		list.add(Hold);
		list.add(Active);

		
		return list;	
	}
}
