package common.entity;

import java.io.Serializable;

import common.entity.enums.UserType;

public class LibraryManager extends Librarian implements Serializable{

	public LibraryManager(int usrId, String usrName, String usrPassword, String usrFirstName, String usrLastName,
			String usrEmail, UserType usrType) {
		super(usrId, usrName, usrPassword, usrFirstName, usrLastName, usrEmail, usrType);
	}

}
