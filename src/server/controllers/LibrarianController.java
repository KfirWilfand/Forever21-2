package server.controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import common.controllers.Message;
import common.controllers.enums.OperationType;
import common.controllers.enums.ReturnMessageType;
import common.entity.Book;
import common.entity.Librarian;
import common.entity.Subscriber;
import common.entity.User;
import common.entity.enums.UserType;

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
	
    public Message searchSubscriber(Object msg) throws SQLException
    {
    	String searchSubscriberQuery= (String)((Message)msg).getObj();
    	DBcontroller dbControllerObj= DBcontroller.getInstance();
    	ResultSet subscriber_res= dbControllerObj.query(searchSubscriberQuery);
    	if(subscriber_res.next()) 
    	{
    		Subscriber subscriber = new Subscriber(subscriber_res.getInt("subNum"), subscriber_res.getString("usrName"),  subscriber_res.getString("usrPassword"), subscriber_res.getString("usrFirstName"), subscriber_res.getString("usrLastName"), subscriber_res.getString("usrEmail"), UserType.stringToEnum(subscriber_res.getString("usrType")), subscriber_res.getString("subStatus"), subscriber_res.getString("subPhoneNum"));
			return new Message(OperationType.SearchSubscriber, subscriber, ReturnMessageType.SubsciberExist);
       	}
    	else 
    	{
    		return new Message(OperationType.SearchSubscriber, null, ReturnMessageType.SubsciberNotExist);	
    	} 
    }
}
