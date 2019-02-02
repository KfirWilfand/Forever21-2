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
	 * LibraryManager constructor
	 * @param usrId                    user id
	 * @param usrName                  user name
	 * @param usrPassword              user password
	 * @param usrFirstName             user first name
	 * @param usrLastName              user last name
	 * @param usrEmail                 user email
	 * @param usrType                  user Type
	 */
	public LibraryManager(int usrId, String usrName, String usrPassword, String usrFirstName, String usrLastName,
			String usrEmail, UserType usrType) {
		super(usrId, usrName, usrPassword, usrFirstName, usrLastName, usrEmail, usrType);
	}

}
