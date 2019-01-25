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
		int tempSubNum=((BorrowCopy)queryMsg.getObj()).getSubNum();
		String checkIfActiveQuery="select subStatus from obl.subscribers where subNum='"+tempSubNum+"'"; 
		DBcontroller dbControllerObj= DBcontroller.getInstance();
		ResultSet isActive_res=dbControllerObj.query(checkIfActiveQuery);
		if(isActive_res.next())
		{
			String statusSub=isActive_res.getString(1); //in 0 spot it's the table name
			if(!statusSub.equals("Active"))//if subscriber is not active- librarian cannot place borrow
			{
				alert.error("Subscriber is locked or hold! cannot place order", "");
			}
		
		String queryPopularBook="select c.copyId,b.bIsPopular,b.bCatalogNum from obl.copeis as c right join obl.books as b on c.bcatalogNum=b.bCatalogNum where c.copyId='"+((BorrowCopy)queryMsg.getObj()).getCopyID()+"'";
		ResultSet isPopular_res= dbControllerObj.query(queryPopularBook);
		if (isPopular_res.next())
		{
			Date returnDueDate;
			boolean popularStatus = isPopular_res.getBoolean(2);//check if the book is popular due to our results from the query
			if(popularStatus)//if popular=true
			{
				//					Date returnDueDate;
				//    		boolean isPopular=isPopular_res.getBoolean("bIsPopular");
				//    		if(isPopular)
				//    		{
				LocalDate returnDate=((BorrowCopy)queryMsg.getObj()).getBorrowDate().toLocalDate().plusDays(3L);
				returnDueDate=Date.valueOf(returnDate);
				((BorrowCopy)queryMsg.getObj()).setReturnDueDate(returnDueDate);
			}
			else
			{
				LocalDate returnDate=((BorrowCopy)queryMsg.getObj()).getBorrowDate().toLocalDate().plusDays(14L);
				returnDueDate=Date.valueOf(returnDate);
				((BorrowCopy)queryMsg.getObj()).setReturnDueDate(returnDueDate);
				System.out.println(returnDate);
			}
			String insertBorrowBookQuery="insert into obl.borrows (copyID, subNum, borrowDate,returnDueDate) values ('"+((BorrowCopy)queryMsg.getObj()).getCopyID()+"','"+((BorrowCopy)queryMsg.getObj()).getSubNum()+"','"+((BorrowCopy)queryMsg.getObj()).getBorrowDate()+"','"+returnDueDate+"')";
			Boolean insertBorrowBook= dbControllerObj.update(insertBorrowBookQuery);
			String decreaseBookAviabilaty="update obl.books set bAvilableCopiesNum=bAvilableCopiesNum-1 where bCatalogNum='"+String.valueOf(isPopular_res.getInt("bCatalogNum"))+"'";
			Boolean decreaseAviability= dbControllerObj.update(decreaseBookAviabilaty);
			String updateBookCopyAviability= "update obl.copeis set isAvilable=0 where copyID='"+((BorrowCopy)queryMsg.getObj()).getCopyID()+"'";
			Boolean updateAviableCopy= dbControllerObj.update(updateBookCopyAviability);
//			String
//			String documentActionOnReaderCardHistory="insert into obl.subscribers_history (subNum, actionDate, actionDescription, actionType) values ('"+((BorrowCopy)queryMsg.getObj()).getSubNum()+"','"+((BorrowCopy)queryMsg.getObj()).getBorrowDate()+"','"+queryMsg.getOperationType() +"'"")"
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

	}
		return new Message(OperationType.BorrowBookByLibrarian, null , ReturnMessageType.Unsuccessful);

	}
	
	
	
	public Message returnBook (Object msg) throws SQLException
	{
		Message copyIDofReturnedBook=((Message)msg);
		String copyIDtemp=((BorrowCopy)copyIDofReturnedBook.getObj()).getCopyID();	
		DBcontroller dbControllerObj= DBcontroller.getInstance();
		String checkIfExistsInBorrows="select copyID, subNum, actualReturnDate, borrowDate from obl.borrows where copyID='"+((BorrowCopy)copyIDofReturnedBook.getObj()).getCopyID()+"' and actualReturnDate IS NULL";
		ResultSet checkIfExistsInBorrows_res= dbControllerObj.query(checkIfExistsInBorrows);
		Date returnActual;
		LocalDate returnDate=((BorrowCopy)copyIDofReturnedBook.getObj()).getActualReturnDate().toLocalDate();
		returnActual=Date.valueOf(returnDate);
		((BorrowCopy)copyIDofReturnedBook.getObj()).setActualReturnDate(returnActual);
		if(checkIfExistsInBorrows_res.next())
		{
			int subsNumber=checkIfExistsInBorrows_res.getInt(2);
			Date getBorrowDate=checkIfExistsInBorrows_res.getDate(4);//borrow copy date
			String updateActualReturnTime="update obl.borrows set returnActualTime='"+returnActual+"' where  copyID='"+((BorrowCopy)copyIDofReturnedBook.getObj()).getCopyID()+"' and subNum='"+subsNumber+"'";
			String returnToCopeisTable="update obl.copeis set isAvilable=1 where copyID='"+((BorrowCopy)copyIDofReturnedBook.getObj()).getCopyID()+"'";
			Boolean returnToCopeisTable_res=dbControllerObj.update(returnToCopeisTable);
			String getCatalogNum="select bCatalogNum from obl.copeis where copyID='"+((BorrowCopy)copyIDofReturnedBook.getObj()).getCopyID()+"'";
			ResultSet getCatalogNum_res= dbControllerObj.query(getCatalogNum);
			if (getCatalogNum_res.next() && returnToCopeisTable_res)//if update on copeis table succeeded and got book catalog number
			{
				int catalogNum=getCatalogNum_res.getInt(1);
				String incOnBooksAviableCopy="update obl.books set bAvilableCopiesNum=bAvilableCopiesNum+1 where bCatalogNum='"+catalogNum+"'";
				Boolean incOnBooksAviableCopy_res=dbControllerObj.update(incOnBooksAviableCopy);
				if(incOnBooksAviableCopy_res) //is increasing amount of aviable copies on books table succeeded then update subscriber actual return date
				{	
					String updateActualReturnDate="update obl.borrows set actualReturnDate='"+returnActual+"' where copyID='"+((BorrowCopy)copyIDofReturnedBook.getObj()).getCopyID()+"' and subNum='"+subsNumber+"' and borrowDate='"+getBorrowDate+"'";
					Boolean updateActualReturnDate_res=dbControllerObj.update(updateActualReturnDate);
					if(updateActualReturnDate_res)//if returned date updated now check if it's greater then the destined date
					{
						String checkIfReturnedOnDestinedDate="select returnDueDate from obl.borrows where copyID='"+((BorrowCopy)copyIDofReturnedBook.getObj()).getCopyID()+"' and subNum='"+subsNumber+"'";
						ResultSet checkIfReturnedOnDestinedDate_res= dbControllerObj.query(checkIfReturnedOnDestinedDate);
						if (checkIfReturnedOnDestinedDate_res.next())
						{
							Date returnDueDateOfBorrowedBook=checkIfReturnedOnDestinedDate_res.getDate(1);
							int flag=returnDueDateOfBorrowedBook.compareTo(returnActual);
							if(flag==0 || flag>0)//if returned in the exact same day
								return new Message(OperationType.ReturnBookByLibrarian, null , ReturnMessageType.Successful);
							if(flag<0)//if subscriber lated on return
							{
								String updateLatesCounter="update obl.subscribers set subLatesCounter=subLatesCounter+1 where subNum='"+subsNumber+"'";
								Boolean updateLatesCounter_res=dbControllerObj.update(updateLatesCounter);
								String getSubStatus="select subStatus, subLatesCounter from obl.subscribers where subNum='"+subsNumber+"'";
								ResultSet getSubStatus_res= dbControllerObj.query(getSubStatus);
								if(getSubStatus_res.next())
								{	String subscriberStatus=getSubStatus_res.getString(1);
									int subscriberLatesCounter=getSubStatus_res.getInt(2);
									String active = new String("Active");

									if(subscriberLatesCounter<3)//if it's less then 3 then change status back to active
									{
										String setActive="update obl.subscribers set subStatus='"+active+"' where subNum='"+subsNumber+"'";
										Boolean setActive_res=dbControllerObj.update(setActive);
										if(setActive_res)//we need to set to active because while book is not returned on time, our system automatically set subscriber status to hold
											return new Message(OperationType.ReturnBookByLibrarian, null , ReturnMessageType.Successful);
									}
									else
									{
										//here will be the code of sending to library manager request to lock the subscriber
										//his current status is hold due to our automatically system respond 
									}
								}
								return new Message(OperationType.ReturnBookByLibrarian, null , ReturnMessageType.Unsuccessful);
							}
						}
						return new Message(OperationType.ReturnBookByLibrarian, null , ReturnMessageType.Unsuccessful);
					}
					return new Message(OperationType.ReturnBookByLibrarian, null , ReturnMessageType.Unsuccessful);
			}
				return new Message(OperationType.ReturnBookByLibrarian, null , ReturnMessageType.Unsuccessful);

		}
			return new Message(OperationType.ReturnBookByLibrarian, null , ReturnMessageType.Unsuccessful);
	}
		return new Message(OperationType.ReturnBookByLibrarian, null , ReturnMessageType.Unsuccessful);	
}}



