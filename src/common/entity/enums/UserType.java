package common.entity.enums;

public enum UserType {
	Librarian,LibraryManager,Subsriber;
	
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
