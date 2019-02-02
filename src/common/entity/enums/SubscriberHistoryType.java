package common.entity.enums;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/**
 * The SubscriberHistoryType enum is containing history type
 * @author  Kfir Wilfand
 * @author Bar Korkos
 * @author Zehavit Otmazgin
 * @author Noam Drori
 * @author Sapir Hochma
 */
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
	/**
	 * getEnums method get enums
	 * @return list    List of reader card history
	 */
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
