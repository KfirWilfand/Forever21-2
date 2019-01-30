package common.entity.enums;

import java.sql.SQLException;

/**
 * The UserType enum represent the user types
 * @author  Kfir Wilfand
 * @author Bar Korkos
 * @author Zehavit Otmazgin
 * @author Noam Drori
 * @author Sapir Hochma
 */
public enum UserType {
	Librarian,LibraryManager,Subsriber;
	/**
	 * stringToEnum is taking a string and convert to enum
	 * @param userType contains string of user type
	 * @return UserType 
	 */
	public static UserType stringToEnum(String userType){
		switch(userType) {
		case "Librarian":
			return UserType.Librarian;
		case "LibraryManager":
			return UserType.LibraryManager;
		case "Subsriber":
			return UserType.Subsriber;
		}
		
		return null;	
	}
}
