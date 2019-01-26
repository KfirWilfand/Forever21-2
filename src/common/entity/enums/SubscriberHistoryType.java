package common.entity.enums;

import java.util.ArrayList;
import java.util.List;

public enum SubscriberHistoryType {
	BooksRequest, BooksApprove, BooksReturn, EditProfile, ChangeStatus, BookExtension;

	public static SubscriberHistoryType stringToEnum(String userType) {
		switch (userType) {
		case "BooksRequest":
			return SubscriberHistoryType.BooksRequest;
		case "BooksApprove":
			return SubscriberHistoryType.BooksApprove;
		case "BooksReturn":
			return SubscriberHistoryType.BooksReturn;
		case "EditProfile":
			return SubscriberHistoryType.EditProfile;
		case "ChangeStatus":
			return SubscriberHistoryType.ChangeStatus;
		}

		return null;
	}
	
	public static List<SubscriberHistoryType> getEnums(){
		List<SubscriberHistoryType> list = new ArrayList<SubscriberHistoryType>();
		list.add(BooksRequest);
		list.add(BooksApprove);
		list.add(BooksReturn);
		list.add(EditProfile);
		list.add(ChangeStatus);
		
		return list;	
	}

}
