package server.controllers;

import java.sql.ResultSet;
import java.sql.SQLException;

import common.controllers.Message;
import common.controllers.enums.OperationType;
import common.controllers.enums.ReturnMessageType;

public class LibrarianController {

	private static LibrarianController instance;

	 private LibrarianController(){}
	 
	 public static LibrarianController getInstance(){
	        if(instance == null){
	            instance = new LibrarianController();
	        }
	        return instance;
	    }
	 
	    public Message createNewSubscriber (Object msg) throws SQLException//NEED TO HANDLE WITH THE 2 DIALOG BOX בו זמנית
	    {
		String [] query=(String[])((Message)msg).getObj();
    	DBcontroller dbControllerObj=DBcontroller.getInstance();
    	ResultSet res2=dbControllerObj.query(query[2]);
    	if(res2.next())
			return new Message(OperationType.AddNewSubscriberByLibrarian, null , ReturnMessageType.EmailOrPhoneAreAlreadyExists);
    	else {
    		Boolean res=dbControllerObj.update(query[0]); 
        	Boolean res1=dbControllerObj.update(query[1]);
        		if(res && res1 )
    		return new Message(OperationType.AddNewSubscriberByLibrarian, null , ReturnMessageType.SubscriberAddedSuccessfuly);
        		else
    		return new Message(OperationType.AddNewSubscriberByLibrarian, null , ReturnMessageType.SubscriberFailedToAdd);
    	}
    }
	 
	public static void init(String data) {
		// TODO Auto-generated method stub
		
	}

}
