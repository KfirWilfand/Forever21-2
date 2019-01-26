package server.controllers;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Queue;

import common.entity.Book;
import common.entity.Copy;
import common.entity.Subscriber;

public class AutomaticFunctions {
	
	public static void checkStudentsGraduate() throws SQLException
	{
		LocalDate today=LocalDate.now();
		Date date=Date.valueOf(today);
		String query1 = "select * from obl.subscribers where subGraduationDate = '"+date+"'";
		DBcontroller dbControllerObj=DBcontroller.getInstance();
	    ResultSet graduateStudents = dbControllerObj.query(query1);
	    
	    while(graduateStudents.next())
	    {
	    	 String query2 = "select * from obl.borrows where subNum ="+graduateStudents.getString("subNum")+" and actualReturnDate is null";
	    	 ResultSet studentBorrowBook = dbControllerObj.query(query2);
	    	 if(studentBorrowBook.next())
	    	 {
	    		 String updateStatusToHold= "update obl.subscribers set subStatus='Hold' where subNum="+graduateStudents.getString("subNum");
	    		 boolean isHold= dbControllerObj.update(updateStatusToHold);
	    		 //SENT mail to the student to return his books 
	    		
	    		Subscriber graduateSub=SubscriberController.getSubscriberById(studentBorrowBook.getString("subNum"));
	 			String mailSubject="Your need to return your books";
				String mailBody="Dear Student congrats on graduation,\nYou have books you should return to the library!";
				SendMailController.sendMailToSubscriber(graduateSub, mailSubject, mailBody);
	    		System.out.println("EMAIL send to subscriber "+graduateSub.toString()); 
				
				
	    		 if(isHold)
	    			 System.out.println("subscriber number"+graduateStudents.getString("subNum")+"is hold!!");
	    		 else
	    			 System.out.println("ERROR in holding - subscriber number"+graduateStudents.getString("subNum"));
	    	 }
	    	 else
	    	 {
	    		 String updateStatusToLock= "update obl.subscribers set subStatus='Lock' where subNum="+graduateStudents.getString("subNum");
	    		 boolean isLock= dbControllerObj.update(updateStatusToLock);
	    		 if(isLock)
	    			 System.out.println("subscriber number"+graduateStudents.getString("subNum")+"is Lock!!");
	    		 else
	    			 System.out.println("ERROR in locking - subscriber number"+graduateStudents.getString("subNum"));
	    	 }
	    	 
	    }
	}
	
	
	public static void checkLatesInReturns() throws SQLException
	{
		LocalDate today=LocalDate.now();
		today.minusDays(1L);
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
	    		return;
	    	}
	    	String howManyLates = "select subLatesCounter from obl.subscribers where subNum="+lateInReturnBorrow.getString("subNum");
	    	ResultSet latesCnt = dbControllerObj.query(howManyLates);
	    	if(latesCnt.next()) 
	    	{
	    		int latesCounter = latesCnt.getInt("subLatesCounter");
	    		if(latesCounter >= 3)
	    		{
	    			 String updateStatusToLock= "update obl.subscribers set subStatus='Lock' where subNum="+lateInReturnBorrow.getString("subNum");
		    		 boolean isLock= dbControllerObj.update(updateStatusToLock);
		    		 if(isLock)
		    			 System.out.println("subscriber number"+lateInReturnBorrow.getString("subNum")+"is Lock!!");
		    		 else
		    			 System.out.println("ERROR in locking - subscriber number"+lateInReturnBorrow.getString("subNum"));
		    		 return;
	    		}
	    		 String updateStatusToHold= "update obl.subscribers set subStatus='Hold' where subNum="+lateInReturnBorrow.getString("subNum");
	    		 boolean isHold= dbControllerObj.update(updateStatusToHold);
	    		 if(isHold)
	    			 System.out.println("subscriber number"+lateInReturnBorrow.getString("subNum")+"is Hold!!");
	    		 else
	    			 System.out.println("ERROR in holding - subscriber number"+lateInReturnBorrow.getString("subNum"));
	    		 return;		
	    	}
	    	System.out.println("ERROR in search how many lates "+lateInReturnBorrow.getString("subNum"));
	    	return;
	    }	    
	}
	
	public static void remainderOneDayBeforeReturns() throws SQLException
	{
		LocalDate today=LocalDate.now();
		today.plusDays(1L);
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
	    	
			System.out.println("mail is send to the subscriber "+needToreturnTomorrow.getString("subNum"));	
	    }
	        
	}
	
	public static void moveToTheNextSubscriberInQueue() throws SQLException
	{
		LocalDate today=LocalDate.now();
		Date todayDate=Date.valueOf(today);
		today.minusDays(2L);
		Date date=Date.valueOf(today);
		String query1 = "select * from obl.book_arrived_mail where reminderDate = '"+date+"'";
		DBcontroller dbControllerObj=DBcontroller.getInstance();
	    ResultSet needToDeleteFromOrderQueue = dbControllerObj.query(query1);
	    while(needToDeleteFromOrderQueue.next())
	    {
	    	Queue<Subscriber> orderQueue=ManageStockController.getBookOrderQueue(needToDeleteFromOrderQueue.getInt("catalogNum"));
	    	Subscriber nextInQueue=orderQueue.remove();
	    	String replaceSubscriberToTheNext = "update obl.book_arrived_mail set subNum="+orderQueue.peek().getSubscriberNum()+", reminderDate='"+todayDate+"' where subNum="+nextInQueue.getSubscriberNum()+" and catalogNum="+needToDeleteFromOrderQueue.getInt("catalogNum");
	    	Boolean isUpdate=dbControllerObj.update(replaceSubscriberToTheNext);
	    	String removeFromLine = "delete from obl.books_orders  where boSubNum="+nextInQueue.getSubscriberNum()+" and boCatalogNum="+needToDeleteFromOrderQueue.getInt("catalogNum");
	    	Boolean isRemoved=dbControllerObj.update(removeFromLine);
	    	
	    	//send mail to the next in line
	    	Book book=ManageStockController.getBookByCatalogNumber(needToDeleteFromOrderQueue.getInt("catalogNum"));
	    	String mailSubject="Your Book is arraived";
			String mailBody="Your Book: "+book.getBookName()+"is arraived. You have two days to take it before your order cancelled.";
			SendMailController.sendMailToSubscriber(orderQueue.peek(), mailSubject, mailBody);
			
	    }

	        
	}

}
