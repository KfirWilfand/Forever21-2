package server.controllers;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

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
	    		 //TODO SENT mail to the student to return his books !!!!!!
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
	    {// send mail dont forgettttttttttttttt motherfackerrrrrrr
	    	System.out.println("mail is send to the subscriber "+needToreturnTomorrow.getString("subNum"));	
	    }
	        
	}

}
