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
