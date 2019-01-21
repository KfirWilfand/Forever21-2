package server.controllers;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.controllers.Message;
import common.controllers.enums.OperationType;
import common.controllers.enums.ReturnMessageType;
import common.entity.Book;
import common.entity.HistoryItem;
import common.entity.BorrowBook;
import common.entity.BorrowCopy;
import common.entity.Librarian;
import common.entity.ReaderCard;
import common.entity.Subscriber;
import common.entity.User;
import common.entity.enums.ReaderCardStatus;
import common.entity.enums.SubscriberHistoryType;
import common.entity.enums.UserType;

public class LibrarianController {

	private static LibrarianController instance;

	private LibrarianController() {
	}

	public static LibrarianController getInstance() {
		if (instance == null) {
			instance = new LibrarianController();
		}
		return instance;
	}

	public Message createNewSubscriber(Object msg) throws SQLException// NEED TO HANDLE WITH THE 2 DIALOG BOX �� �����
	{
		String[] query = (String[]) ((Message) msg).getObj();
		DBcontroller dbControllerObj = DBcontroller.getInstance();
		ResultSet res2 = dbControllerObj.query(query[2]);
		if (res2.next())
			return new Message(OperationType.AddNewSubscriberByLibrarian, null,
					ReturnMessageType.EmailOrPhoneAreAlreadyExists);
		else {
			Boolean res = dbControllerObj.update(query[0]);
			Boolean res1 = dbControllerObj.update(query[1]);
			if (res && res1)
				return new Message(OperationType.AddNewSubscriberByLibrarian, null,
						ReturnMessageType.SubscriberAddedSuccessfuly);
			else
				return new Message(OperationType.AddNewSubscriberByLibrarian, null,
						ReturnMessageType.SubscriberFailedToAdd);
		}
	}


	public Message searchSubscriber(Object msg) throws SQLException {
		Subscriber subscriber = SubscriberController.getSubscriberById((String)((Message)msg).getObj());
		if (subscriber != null) {
			return new Message(OperationType.SearchSubscriber, subscriber, ReturnMessageType.SubsciberExist);
       	}
    	else 
    	{
    		return new Message(OperationType.SearchSubscriber, null, ReturnMessageType.SubsciberNotExist);	
    	} 
    }
    public Message borrowBook (Object msg) throws SQLException
    {
    	Message queryMsg=((Message)msg);
    	String queryPopularBook="select c.copyId,b.bIsPopular,b.bCatalogNum from obl.copeis as c right join obl.books as b on c.bcatalogNum=b.bCatalogNum where c.copyId='"+((BorrowCopy)queryMsg.getObj()).getCopyID()+"'";
    	DBcontroller dbControllerObj= DBcontroller.getInstance();
    	ResultSet isPopular_res= dbControllerObj.query(queryPopularBook);
    	if(isPopular_res.next())
    	{
    		Date returnDueDate;
    		boolean isPopular=isPopular_res.getBoolean("bIsPopular");
    		if(isPopular)
    		{
    			LocalDate returnDate=((BorrowCopy)queryMsg.getObj()).getBorrowDate().toLocalDate().plusDays(3);
    	    	returnDueDate=Date.valueOf(returnDate);
    	    	((BorrowCopy)queryMsg.getObj()).setReturnDueDate(returnDueDate);
    		}
    		else
    		{
    			LocalDate returnDate=((BorrowCopy)queryMsg.getObj()).getBorrowDate().toLocalDate().plusDays(14);
    	    	returnDueDate=Date.valueOf(returnDate);
    	    	((BorrowCopy)queryMsg.getObj()).setReturnDueDate(returnDueDate);
    		}
    		String insertBorrowBookQuery="insert into obl.borrows (copyID, subNum, borrowDate,returnDueDate) values ('"+((BorrowCopy)queryMsg.getObj()).getCopyID()+"','"+((BorrowCopy)queryMsg.getObj()).getSubNum()+"','"+((BorrowCopy)queryMsg.getObj()).getBorrowDate()+"','"+returnDueDate+"')";
    		Boolean insertBorrowBook= dbControllerObj.update(insertBorrowBookQuery);
    		String decreaseBookAviabilaty="update obl.books set bAvilableCopiesNum=bAvilableCopiesNum-1 where bCatalogNum='"+String.valueOf(isPopular_res.getInt("bCatalogNum"))+"'";
    		Boolean decreaseAviability= dbControllerObj.update(decreaseBookAviabilaty);
    		String updateBookCopyAviability= "update obl.copeis set isAvilable=0 where copyID='"+((BorrowCopy)queryMsg.getObj()).getCopyID()+"'";
    		Boolean updateAviableCopy= dbControllerObj.update(updateBookCopyAviability);
    		if(insertBorrowBook&&decreaseAviability&&updateAviableCopy)
    		{
    			Object[] arr= new Object[2];
    			arr[0]=((BorrowCopy)queryMsg.getObj());
    			arr[1]=isPopular_res.getBoolean("bIsPopular");
    			return new Message(OperationType.BorrowBookByLibrarian, arr , ReturnMessageType.Successful);
    		}
    		else
    		{
    			return new Message(OperationType.BorrowBookByLibrarian, null , ReturnMessageType.Unsuccessful);
    		}
    		
    	}
    	else
    	{
			return new Message(OperationType.BorrowBookByLibrarian, null , ReturnMessageType.ErrorWhileTyping);

    	}
    }
    
}
