package server.controllers;

import java.sql.ResultSet;
import java.sql.SQLException;

import common.controllers.Message;
import common.controllers.enums.OperationType;
import common.controllers.enums.ReturnMessageType;
import common.entity.Subscriber;
import common.entity.enums.UserType;

public class SubscriberController {

	private static SubscriberController instance;
    
    private SubscriberController(){}
    
    public static SubscriberController getInstance(){
        if(instance == null){
            instance = new SubscriberController();
        }
        return instance;
    }
    
    
    public Message getSubscriber(Object msg) throws SQLException 
    {
    	String query=(String)((Message)msg).getObj();
    	DBcontroller dbControllerObj=DBcontroller.getInstance();
    	ResultSet subscriberResult=dbControllerObj.query(query);
    	if(subscriberResult.next()) {
    	Subscriber subscriber=new Subscriber(subscriberResult.getInt("subNum"), subscriberResult.getString("usrName"), subscriberResult.getString("usrPassword"), subscriberResult.getString("usrFirstName"), subscriberResult.getString("usrLastName"),
    	subscriberResult.getString("usrEmail"), UserType.stringToEnum(subscriberResult.getString("usrType")), subscriberResult.getString("subStatus"),subscriberResult.getString("subPhoneNum"));
    	return new Message(OperationType.GetSubscriberDetails, subscriber, ReturnMessageType.SubscriberFound);
    	}
    	else 
    		return new Message(OperationType.GetSubscriberDetails, null, ReturnMessageType.SubscriberNotFound);
    }
    
    public Message updateDetails (Object msg) throws SQLException
    {
    	String query=(String)((Message)msg).getObj();
    	DBcontroller dbControllerObj=DBcontroller.getInstance();
    	Boolean res=dbControllerObj.update(query);
    	if(res)
    		return new Message(OperationType.EditDetailsBySubscriber, null , ReturnMessageType.UpdateSuccesfully);
    	else
        	return new Message(OperationType.EditDetailsBySubscriber, null , ReturnMessageType.NotUpdateSuccesfully);
    }
}