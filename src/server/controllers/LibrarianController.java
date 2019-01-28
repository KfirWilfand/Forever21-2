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
import java.util.Queue;

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
/**
 * The LibrarianController class represent the librarian controller on the server side
 * @author  Kfir Wilfand
 * @author Bar Korkos
 * @author Zehavit Otmazgin
 * @author Noam Drori
 * @author Sapir Hochma
 */
public class LibrarianController {
	/**alert is an object of AlertController */
	static AlertController alert = new AlertController();
	/**instance is a singleton of the class */
	private static LibrarianController instance;

	private LibrarianController(){}
	 /**
	 * getInstance is creating the singleton object of the class
	 */
	public static LibrarianController getInstance(){
		if(instance == null){
			instance = new LibrarianController();
		}
		return instance;
	}
	/**
	 * createNewSubscriber is creating a new subscriber 
	 * @param msg contains the message from the client 
	 * @throws SQLException when occurs
	 * @return Message to the client
	 */
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
	/**
	 * searchSubscriber is searching a subscriber
	 * @param msg contains the message from the client 
	 * @throws SQLException when occurs
	 * @return Message to the client
	 */
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

	
	/**
	 * borrowBook is place a borrow of a book
	 * @param msg contains the message from the client 
	 * @throws SQLException when occurs
	 * @return Message to the client
	 */
	public Message borrowBook (Object msg) throws SQLException
	{
		DBcontroller dbControllerObj=DBcontroller.getInstance();
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
			{
				Queue<Subscriber> orderQueue=ManageStockController.getBookOrderQueue(copy.getbCatalogNum());
				if(orderQueue.isEmpty())
					return new Message(OperationType.BorrowBookByLibrarian, null , ReturnMessageType.CopyIsNotAvailable);
				
				else if(orderQueue.peek().getSubscriberNum() != subscriber.getSubscriberNum())
					return new Message(OperationType.BorrowBookByLibrarian, null , ReturnMessageType.CopyIsNotAvailable);
				else
					{
						Subscriber nextInQueue=orderQueue.remove();
						String delete_from_line="DELETE FROM obl.book_arrived_mail where subNum="+nextInQueue.getSubscriberNum()+" and catalogNum="+copy.getbCatalogNum();
						Boolean isDeleted=dbControllerObj.update(delete_from_line);
						String removeFromLine = "delete from obl.books_orders  where boSubNum="+nextInQueue.getSubscriberNum()+" and boCatalogNum="+copy.getbCatalogNum();
				    	Boolean isRemoved=dbControllerObj.update(removeFromLine);
					}
			}
	
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
			
			//insert to the history
			SubscriberController scObj=SubscriberController.getInstance();
			HistoryItem hRecord=new HistoryItem(subscriber.getSubscriberNum(),"The book: "+book.getBookName()+" was borrowed",SubscriberHistoryType.BooksApprove);
			scObj.addHistoryRecordBySubId(new Message(OperationType.BorrowBookByLibrarian,hRecord ));
			
			return new Message(OperationType.BorrowBookByLibrarian, arr , ReturnMessageType.Successful);
		}
		return new Message(OperationType.BorrowBookByLibrarian, null , ReturnMessageType.Unsuccessful);
			
	}
	
		
	/**
	 * returnBook is place the return of a borrowed book and checks all the required conditions
	 * @param msg contains the message from the client 
	 * @throws SQLException when occurs
	 * @return Message to the client
	 */
	public Message returnBook (Object msg) throws SQLException
	{
		
		SubscriberController scObj=SubscriberController.getInstance();
		DBcontroller dbControllerObj= DBcontroller.getInstance();
		Message copyIDofReturnedBook=((Message)msg);
		String copyIDtemp=((BorrowCopy)copyIDofReturnedBook.getObj()).getCopyID();
		Copy copy=ManageStockController.getCopyById(copyIDtemp);
		if(copy == null)
			return new Message(OperationType.ReturnBookByLibrarian, null , ReturnMessageType.CopyNotExist);
		
		BorrowCopy borrowCopyFromDB=ManageStockController.getBorrowCopyByCopyID(copyIDtemp);	
		if(borrowCopyFromDB==null)
			return new Message(OperationType.ReturnBookByLibrarian, null , ReturnMessageType.wrongBorrowDetails);
		
		borrowCopyFromDB.setActualReturnDate(((BorrowCopy)copyIDofReturnedBook.getObj()).getActualReturnDate());
		
		Subscriber subscriber=SubscriberController.getSubscriberById(String.valueOf(borrowCopyFromDB.getSubNum()));
		
		
		ReturnMessageType op;
		if( borrowCopyFromDB.getActualReturnDate().after(borrowCopyFromDB.getReturnDueDate()) )
		{//return not in time
			if (subscriber.getReaderCard().getLateReturnsBookCounter()>=2)
				{
					String updateSubscriberDetails="update obl.subscribers set subLatesCounter=subLatesCounter+1 subStatuse='Lock' where subNum='"+subscriber.getSubscriberNum()+"'";
					Boolean isUpdate=dbControllerObj.update(updateSubscriberDetails);
					op=ReturnMessageType.ChangeStatusToLock;
				}
			else
				{
					String updateSubscriberDetails="update obl.subscribers set subLatesCounter=subLatesCounter+1 subStatuse='Active' where subNum='"+subscriber.getSubscriberNum()+"'";
					Boolean isUpdate=dbControllerObj.update(updateSubscriberDetails);
					op=ReturnMessageType.ChangeStatusToActive;
					//add to history status changing
					HistoryItem hRecord=new HistoryItem(subscriber.getSubscriberNum(),"Status changed from Hold to Active",SubscriberHistoryType.ChangeStatus);
					scObj.addHistoryRecordBySubId(new Message(OperationType.ReturnBookByLibrarian,hRecord ));
				}
		}
		
		//update actual return date in DB
		String updateActualReturnDate="update obl.borrows set actualReturnDate='"+borrowCopyFromDB.getActualReturnDate()+"' where copyID='"+copy.getCopyID()+"' and subNum='"+subscriber.getSubscriberNum()+"' and borrowDate='"+borrowCopyFromDB.getBorrowDate()+"' and actualReturnDate is null";
		Boolean updateActualReturnDate_res=dbControllerObj.update(updateActualReturnDate);
		Book bookDetails=ManageStockController.getBookByCatalogNumber(copy.getbCatalogNum());
		Queue<Subscriber> orderQueue=ManageStockController.getBookOrderQueue(copy.getbCatalogNum());
		if(orderQueue.isEmpty())
		{//there is no subscribers in waiting list
			//update number of available copies
			String incOnBooksAviableCopy="update obl.books set bAvilableCopiesNum=bAvilableCopiesNum+1 where bCatalogNum='"+copy.getbCatalogNum()+"'";
			Boolean incOnBooksAviableCopy_res=dbControllerObj.update(incOnBooksAviableCopy);
			
			//update copy to be available
			String returnToCopeisTable="update obl.copeis set isAvilable=1 where copyID='"+copy.getCopyID()+"'";
			Boolean returnToCopeisTable_res=dbControllerObj.update(returnToCopeisTable);
			op=ReturnMessageType.Successful;
		}
		else
		{//there is subscriber in orderQueue
			Subscriber firstInLine = orderQueue.peek();
			
			// sand mail that the book is arrived
			String mailSubject="Your Book is arraived";
			String mailBody="Your Book: "+bookDetails.getBookName()+"is arraived. You have two days to take it before your order cancelled.";
			SendMailController.sendMailToSubscriber(firstInLine, mailSubject, mailBody);
			
			
			String query="insert into obl.book_arrived_mail (subNum,catalogNum,reminderDate) values ("+firstInLine.getSubscriberNum()+",'"+copy.getbCatalogNum()+"','"+borrowCopyFromDB.getActualReturnDate()+"')";
			Boolean insertToBookArrivedMail=dbControllerObj.update(query);
			op=ReturnMessageType.subscriberInWaitingList;
			
		}
		
		//add return to history
		String historyMsg="The book: "+bookDetails.getBookName()+" returned.";
		if(op==ReturnMessageType.ChangeStatusToActive || op==ReturnMessageType.ChangeStatusToLock)
			historyMsg=historyMsg+" the subscriber was late in return.";
	
		HistoryItem hRecord=new HistoryItem(subscriber.getSubscriberNum(),historyMsg,SubscriberHistoryType.BooksReturn);
		scObj.addHistoryRecordBySubId(new Message(OperationType.ReturnBookByLibrarian,hRecord ));
		
		
		
		return new Message(OperationType.ReturnBookByLibrarian, borrowCopyFromDB , op);
	}

}
