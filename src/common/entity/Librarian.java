package common.entity;

import java.io.Serializable;

import common.entity.enums.UserType;
/**
 * The Librarian class extends User implements Serializable represent the librarian entity
 * @author  Kfir Wilfand
 * @author Bar Korkos
 * @author Zehavit Otmazgin
 * @author Noam Drori
 * @author Sapir Hochma
 */
public class Librarian extends User implements Serializable{

	/**
	 * Librarian constructor
	 * @param usrId
	 * @param usrName
	 * @param usrPassword
	 * @param usrFirstName
	 * @param usrLastName
	 * @param usrEmail
	 * @param usrType
	 */
	public Librarian(Integer usrId, String usrName, String usrPassword, String usrFirstName, String usrLastName, String usrEmail, UserType usrType) 
	{
		super(usrName,usrPassword, usrFirstName, usrLastName, usrId, usrEmail,usrType);
	}
}
