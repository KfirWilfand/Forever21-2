package server.controllers;

import java.sql.SQLException;

import common.controllers.Message;
import common.controllers.enums.OperationType;
import common.controllers.enums.ReturnMessageType;
import common.entity.HistoryItem;
import common.entity.enums.SubscriberHistoryType;
/**
 * The LibraryManagerController class represent the library manager controller on the server side
 * @author  Kfir Wilfand
 * @author Bar Korkos
 * @author Zehavit Otmazgin
 * @author Noam Drori
 * @author Sapir Hochma
 */
public class LibraryManagerController {

	private static LibraryManagerController instance;
	
	private LibraryManagerController(){}
	 /**
	 * getInstance is creating the singleton object of the class
	 * @return instance      instance of  LibraryManagerController object
	 */
	public static LibraryManagerController getInstance(){
		if(instance == null){
			instance = new LibraryManagerController();
		}
		return instance;
	}
	
	/**
	 * function that lock the Reader Card
	 * @return Message message to the client
	 * @throws SQLException   SQLException
	 * @param msg     message from client
	 */
	public Message lockReaderCard(Message msg) throws SQLException
	{
		
		 String updateStatusToLock= "update obl.subscribers set subStatus='Lock' where subNum="+(String)msg.getObj();
		 DBcontroller db=DBcontroller.getInstance();
		 boolean isLock= db.update(updateStatusToLock);
		 String updateLatesCnt= "update obl.subscribers set subLatesCounter=0 where subNum="+(String)msg.getObj();
		 boolean isUpdateCnt= db.update(updateLatesCnt);
		 if(isLock && isUpdateCnt)
		 {	
			SubscriberController scObj=SubscriberController.getInstance();
			HistoryItem hRecord=new HistoryItem(Integer.valueOf((String)msg.getObj()),"Subscriber status was cahnged to Lock",SubscriberHistoryType.ChangeStatus);
			scObj.addHistoryRecordBySubId(new Message(OperationType.ReturnBookByLibrarian,hRecord ));
			 return new Message(OperationType.LockReaderCard, null , ReturnMessageType.Successful);
		 }
		 else
			 return new Message(OperationType.LockReaderCard, null , ReturnMessageType.Unsuccessful);
	}
	
	
	/**
	 * The function change the status of the reader card to active
	 * @return Message message to the client
	 * @throws SQLException   SQLException
	 * @param msg  message from client
	 */
	public Message changeToActiveReaderCard(Message msg) throws SQLException 
	{
		
		
		String updateStatusToLock= "update obl.subscribers set subStatus='Active' where subNum="+(String)msg.getObj();
		 DBcontroller db=DBcontroller.getInstance();
		 boolean isActive= db.update(updateStatusToLock);
		 String updateLatesCnt= "update obl.subscribers set subLatesCounter=0 where subNum="+(String)msg.getObj();
		 boolean isUpdateCnt= db.update(updateLatesCnt);
		 if(isActive && isUpdateCnt)
		 {	SubscriberController scObj=SubscriberController.getInstance();
			HistoryItem hRecord=new HistoryItem(Integer.valueOf((String)msg.getObj()),"Subscriber status was cahnged to Lock",SubscriberHistoryType.ChangeStatus);
			scObj.addHistoryRecordBySubId(new Message(OperationType.ReturnBookByLibrarian,hRecord ));
			return new Message(OperationType.ChangeToActiveReaderCard, null , ReturnMessageType.Successful);
		 }
		 else
			 return new Message(OperationType.ChangeToActiveReaderCard, null , ReturnMessageType.Unsuccessful);
	}
	


}
