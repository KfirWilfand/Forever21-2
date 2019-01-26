package common.entity;

import java.io.Serializable;

import common.entity.enums.UserType;
/**
 * The LibraryManager class extends Librarian implements Serializable represent the library manager entity
 * @author  Kfir Wilfand
 * @author Bar Korkos
 * @author Zehavit Otmazgin
 * @author Noam Drori
 * @author Sapir Hochma
 */
public class LibraryManager extends Librarian implements Serializable{

	/**
	 * Copy Librarian constructor
	 * @param usrId
	 * @param usrName
	 * @param usrPassword
	 * @param usrFirstName
	 * @param usrLastName
	 * @param usrEmail
	 * @param usrType
	 * @return Librarian object
	 */
	public LibraryManager(int usrId, String usrName, String usrPassword, String usrFirstName, String usrLastName,
			String usrEmail, UserType usrType) {
		super(usrId, usrName, usrPassword, usrFirstName, usrLastName, usrEmail, usrType);
	}

}
