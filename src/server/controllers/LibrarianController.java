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

import client.ViewStarter;
import client.controllers.adapters.AlertController;
import common.controllers.Message;
import common.controllers.enums.OperationType;
import common.controllers.enums.ReturnMessageType;
import common.entity.Book;
import common.entity.HistoryItem;
import common.entity.BorrowBook;
import common.entity.BorrowCopy;
import common.entity.Copy;
import common.entity.Librarian;
import common.entity.ReaderCard;
import common.entity.Subscriber;
import common.entity.User;
import common.entity.enums.ReaderCardStatus;
import common.entity.enums.SubscriberHistoryType;
import common.entity.enums.UserType;

public class LibrarianController {

	static AlertController alert = new AlertController();

	private static LibrarianController instance;

	private LibrarianController(){}

	public static LibrarianController getInstance(){
		if(instance == null){
			instance = new LibrarianController();
		}
		return instance;
	}

	public Message createNewSubscriber (Object msg) throws SQLException
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
				return new Message(OperationType.AddNewSubscriberByLibrarian, null , ReturnMessageType.Successful);
			else
				return new Message(OperationType.AddNewSubscriberByLibrarian, null , ReturnMessageType.Unsuccessful);
		}
	}
	
	public Message searchSubscriber(Object msg) throws SQLException
	{
		Subscriber subscriber = SubscriberController.getSubscriberById((String)((Message)msg).getObj());
		if (subscriber != null) {
			return new Message(OperationType.SearchSubscriber, subscriber, ReturnMessageType.Successful);
		}
		else 
		{
			return new Message(OperationType.SearchSubscriber, null, ReturnMessageType.Unsuccessful);	
		} 
	}

	public Message borrowBook (Object msg) throws SQLException
	{
		Message queryMsg=((Message)msg);

		Subscriber subscriber = SubscriberController.getSubscriberById(String.valueOf(((BorrowCopy)queryMsg.getObj()).getSubNum()));
		Copy copy= ManageStockController.getCopyById(((BorrowCopy)queryMsg.getObj()).getCopyID());
		
		if(subscriber == null)
			return new Message(OperationType.BorrowBookByLibrarian, null , ReturnMessageType.SubscriberNotExist);	

		if(copy == null)
			return new Message(OperationType.BorrowBookByLibrarian, null , ReturnMessageType.CopyNotExist);
		
		if(subscriber.getReaderCard().getStatus() != ReaderCardStatus.Active)
			return new Message(OperationType.BorrowBookByLibrarian, null , ReturnMessageType.HoldOrLockStatus);
		
		if(!copy.isAvilabale())
			return new Message(OperationType.BorrowBookByLibrarian, null , ReturnMessageType.CopyIsNotAvailable);
	
		Book book = ManageStockController.getBookByCatalogNumber(copy.getbCatalogNum());
		

		if(book.isPopular())
		{
			LocalDate returnDate=((BorrowCopy)queryMsg.getObj()).getBorrowDate().toLocalDate().plusDays(3L);
			((BorrowCopy)queryMsg.getObj()).setReturnDueDate(Date.valueOf(returnDate));
		}
		else
		{
			LocalDate returnDate=((BorrowCopy)queryMsg.getObj()).getBorrowDate().toLocalDate().plusDays(14L);
			((BorrowCopy)queryMsg.getObj()).setReturnDueDate(Date.valueOf(returnDate));	
		}
		
		DBcontroller dbControllerObj=DBcontroller.getInstance();
		String insertBorrowBookQuery="insert into obl.borrows (copyID, subNum, borrowDate,returnDueDate) values ('"+((BorrowCopy)queryMsg.getObj()).getCopyID()+"','"+((BorrowCopy)queryMsg.getObj()).getSubNum()+"','"+((BorrowCopy)queryMsg.getObj()).getBorrowDate()+"','"+((BorrowCopy)queryMsg.getObj()).getReturnDueDate()+"')";
		Boolean insertBorrowBook= dbControllerObj.update(insertBorrowBookQuery);
		String decreaseBookAviabilaty="update obl.books set bAvilableCopiesNum=bAvilableCopiesNum-1 where bCatalogNum='"+String.valueOf(book.getCatalogNum())+"'";
		Boolean decreaseAviability= dbControllerObj.update(decreaseBookAviabilaty);
		String updateBookCopyAviability= "update obl.copeis set isAvilable=0 where copyID='"+copy.getCopyID()+"'";
		Boolean updateAviableCopy= dbControllerObj.update(updateBookCopyAviability);
		
		if(insertBorrowBook&&decreaseAviability&&updateAviableCopy)
		{
			Object[] arr= new Object[2];
			arr[0]=((BorrowCopy)queryMsg.getObj());
			arr[1]=book.isPopular();
			return new Message(OperationType.BorrowBookByLibrarian, arr , ReturnMessageType.Successful);
		}
		return new Message(OperationType.BorrowBookByLibrarian, null , ReturnMessageType.Unsuccessful);
			
	}
	
	
	
//	public Message returnBook (Object msg) throws SQLException
//	{
//		Message copyIDofReturnedBook=((Message)msg);
//		String copyIDtemp=((BorrowCopy)copyIDofReturnedBook.getObj()).getCopyID();	
//		Date returnActual;
//		LocalDate returnDate=((BorrowCopy)copyIDofReturnedBook.getObj()).getActualReturnDate().toLocalDate();
//		returnActual=Date.valueOf(returnDate);
//		((BorrowCopy)copyIDofReturnedBook.getObj()).setActualReturnDate(returnActual);
//		DBcontroller dbControllerObj= DBcontroller.getInstance();
//		String returnToCopeisTable="update obl.copeis set isAvilable=1 where copyID='"+((BorrowCopy)copyIDofReturnedBook.getObj()).getCopyID()+"'";
//		Boolean returnToCopeisTable_res=dbControllerObj.update(returnToCopeisTable);
//		String getCatalogNum="select bCatalogNum from obl.copeis where copyID='"+((BorrowCopy)copyIDofReturnedBook.getObj()).getCopyID()+"'";
//		ResultSet getCatalogNum_res= dbControllerObj.query(getCatalogNum);
//		if (getCatalogNum_res.next())
//		{
//			int catalogNum=getCatalogNum_res.getInt(1);
//			String incOnBooksAviableCopy="update obl.books set bAvilableCopiesNum=bAvilableCopiesNum+1 where bCatalogNum='"+catalogNum+"'";
//			Boolean incOnBooksAviableCopy_res=dbControllerObj.update(incOnBooksAviableCopy);
//			String updateActualReturnDate="update obl.borrows set actualReturnDate='"+
//
//		}
//		
//
//		
//		
//		Boolean isIncreasedAviableCopy="update obl.books set bAvilableCopiesNum=bAvilableCopiesNum+1 where bCatalogNum="
//			
//
//		
//		
//		
//		
//		
//		
//	}
	
	
	
	
	
	
	
}



