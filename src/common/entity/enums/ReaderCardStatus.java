package common.entity.enums;

import java.util.ArrayList;
import java.util.List;

public enum ReaderCardStatus {
	Lock, Hold,Active;
	
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
	
	public static List<ReaderCardStatus> getEnums(){
		List<ReaderCardStatus> list = new ArrayList<ReaderCardStatus>();
		list.add(Lock);
		list.add(Hold);
		list.add(Active);

		
		return list;	
	}
}
