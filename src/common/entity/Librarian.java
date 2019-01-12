package common.entity;

import java.io.Serializable;

import common.entity.enums.UserType;

public class Librarian extends User implements Serializable{

	public Librarian(Integer usrId, String usrName, String usrPassword, String usrFirstName, String usrLastName, String usrEmail, UserType usrType) 
	{
		super(usrName,usrPassword, usrFirstName, usrLastName, usrId, usrEmail,usrType);
	}
}
