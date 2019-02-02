package server.controllers;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Queue;

import common.controllers.Message;
import common.controllers.enums.OperationType;
import common.entity.Book;
import common.entity.Copy;
import common.entity.HistoryItem;
import common.entity.Subscriber;
import common.entity.enums.SubscriberHistoryType;
/**
 * The AutomaticFunction class represent the functions that execute automatically at 00:00 every day. if you want to change the execute time 
 * you need to click on the "set time" button at the server GUI
 * @author  Kfir Wilfand
 * @author Bar Korkos
 * @author Zehavit Otmazgin
 * @author Noam Drori
 * @author Sapir Hochma
 */
public class AutomaticFunctions {
	
	
	/**
	 * function that check every day the books that their return day is arrived but still do not return the book and change the reader card status to hold
	 * @throws SQLException  SQLException
	 */
	public static void checkLatesInReturns() throws SQLException
	{
		
		System.out.println("function 1 strat!!!");
		LocalDate today=LocalDate.now();
		today=today.minusDays(1L);
		Date date=Date.valueOf(today);
		String query1 = "select * from obl.borrows where returnDueDate = '"+date+"' and actualReturnDate is NULL";
		DBcontroller dbControllerObj=DBcontroller.getInstance();
	    ResultSet lateInReturnBorrow = dbControllerObj.query(query1);
	    while(lateInReturnBorrow.next())
	    {
	    	String updateLatesCnt= "update obl.subscribers set subLatesCounter=subLatesCounter+1 where subNum= "+lateInReturnBorrow.getString("subNum");
	    	boolean isUpdate = dbControllerObj.update(updateLatesCnt);
	    	if(!isUpdate)
	    	{
	    		System.out.println("ERROR in update the number of lates - subscriber number"+lateInReturnBorrow.getString("subNum"));
	    		continue;
	    	}
	    	String howManyLates = "select subLatesCounter from obl.subscribers where subNum="+lateInReturnBorrow.getString("subNum");
	    	ResultSet latesCnt = dbControllerObj.query(howManyLates);
	    	if(latesCnt.next()) 
	    	{
	    		int latesCounter = latesCnt.getInt("subLatesCounter");
	    		if(latesCounter >= 3)
	    		{
	    			SendMailController.sendLockInboxToLibraryManager(lateInReturnBorrow.getInt("subNum"));
		    		 continue;
	    		}
	    		 String updateStatusToHold= "update obl.subscribers set subStatus='Hold' where subNum="+lateInReturnBorrow.getString("subNum");
	    		 boolean isHold= dbControllerObj.update(updateStatusToHold);

	    		 
	    		 if(isHold)
	    		 { 
		    		SubscriberController scObj=SubscriberController.getInstance();
		    		HistoryItem hRecord=new HistoryItem(lateInReturnBorrow.getInt("subNum"),"Subscriber status was cahnged to Hold",SubscriberHistoryType.ChangeStatus);
		    		scObj.addHistoryRecordBySubId(new Message(OperationType.ReturnBookByLibrarian,hRecord ));
	       			System.out.println("subscriber number"+lateInReturnBorrow.getString("subNum")+"is Hold!!");
	    		 }
	    		 else
	    			 System.out.println("ERROR in holding - subscriber number"+lateInReturnBorrow.getString("subNum"));
	    		 Copy copy=ManageStockController.getCopyById(lateInReturnBorrow.getString("copyID"));
    			 Book book=ManageStockController.getBookByCatalogNumber(copy.getbCatalogNum());
    			 SendMailController.sendAlertInbox(lateInReturnBorrow.getInt("subNum"), "Late In Return", "Your reader card is HOLD , you are late in returning the book :"+book.getBookName());
    			
	    		 continue;		
	    	}
	    	System.out.println("ERROR in search how many lates "+lateInReturnBorrow.getString("subNum"));
	    	
	    }	    
	}
	
	

	/**
	 * function that check every day the books that their return day is tomorrow and remind to the students to return the book
	 * @throws SQLException      SQLException
	 */
	public static void remainderOneDayBeforeReturns() throws SQLException
	{	
		System.out.println("function 2 strat!!!");
		LocalDate today=LocalDate.now();
		today=today.plusDays(1L);
		Date date=Date.valueOf(today);
		String query1 = "select * from obl.borrows where returnDueDate = '"+date+"' and actualReturnDate is NULL";
		DBcontroller dbControllerObj=DBcontroller.getInstance();
	    ResultSet needToreturnTomorrow = dbControllerObj.query(query1);
	    while(needToreturnTomorrow.next())
	    {
	    	Copy copyToReturn=ManageStockController.getCopyById(needToreturnTomorrow.getString("copyID"));
	    	Book bookToReturn=ManageStockController.getBookByCatalogNumber(copyToReturn.getbCatalogNum());
	    	
	    	Subscriber subscriber=SubscriberController.getSubscriberById(needToreturnTomorrow.getString("subNum"));
 			String mailSubject="Reminder: Your need to return your book";
			String mailBody="Dear Student you need to return the book: "+bookToReturn.getBookName()+" until tomrrow\nOr your reader card will Hold!";
			
			SendMailController.sendMailToSubscriber(subscriber, mailSubject, mailBody);
			SendMailController.sendReminderInbox(needToreturnTomorrow.getInt("subNum"), "Return book", "You need to return the book :"+bookToReturn.getBookName());
			System.out.println("mail is send to the subscriber "+needToreturnTomorrow.getString("subNum"));	
	    }
	        
	}
	
	

	/**
	 * function that check every day if the first subscriber in the order list take the book and if 2 days already pass - the function move to the next subscriber at queue
	 * @exception SQLException     SQLException
	 */
	public static void moveToTheNextSubscriberInQueue() throws SQLException
	{
		System.out.println("function 3 strat!!!");
		LocalDate today=LocalDate.now();
		Date todayDate=Date.valueOf(today);
		today=today.minusDays(2L);
		Date date=Date.valueOf(today);
		String query1 = "select * from obl.book_arrived_mail where reminderDate = '"+date+"'";
		DBcontroller dbControllerObj=DBcontroller.getInstance();
	    ResultSet needToDeleteFromOrderQueue = dbControllerObj.query(query1);
	    while(needToDeleteFromOrderQueue.next())
	    {
	    	Copy copy=ManageStockController.getCopyById(needToDeleteFromOrderQueue.getString("copyID"));
	    	Queue<Subscriber> orderQueue=ManageStockController.getBookOrderQueue(copy.getbCatalogNum());
	    	Subscriber nextInQueue=orderQueue.remove();
	    	String replaceSubscriberToTheNext = "update obl.book_arrived_mail set subNum="+orderQueue.peek().getSubscriberNum()+", reminderDate='"+todayDate+"' where subNum="+nextInQueue.getSubscriberNum()+" and copyID="+needToDeleteFromOrderQueue.getInt("copyID");
	    	Boolean isUpdate=dbControllerObj.update(replaceSubscriberToTheNext);
	    	String removeFromLine = "delete from obl.books_orders  where boSubNum="+nextInQueue.getSubscriberNum()+" and boCatalogNum="+needToDeleteFromOrderQueue.getInt("catalogNum");
	    	Boolean isRemoved=dbControllerObj.update(removeFromLine);
	    	
	    	//send mail to the next in line
	    	Book book=ManageStockController.getBookByCatalogNumber(copy.getbCatalogNum());
	    	String mailSubject="Your Book is arraived";
			String mailBody="Your Book: "+book.getBookName()+"is arraived. You have two days to take it before your order cancelled.";
			SendMailController.sendMailToSubscriber(orderQueue.peek(), mailSubject, mailBody);
			SendMailController.sendReminderInbox(orderQueue.peek().getSubscriberNum(), mailSubject,mailBody);
			
	    }

	        
	}

}
