package server.controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import client.controllers.adapters.AlertController;
import common.controllers.Message;
import common.controllers.enums.OperationType;
import common.controllers.enums.ReturnMessageType;
import common.entity.Book;
import common.entity.BookInOrder;
import common.entity.BorrowCopy;
import common.entity.HistoryItem;
import common.entity.ReaderCard;
import common.entity.Subscriber;
import common.entity.enums.ReaderCardStatus;
import common.entity.enums.SubscriberHistoryType;
import common.entity.enums.UserType;

/**
 * The SubscriberController class represent the subscriber controller on the
 * server side
 * 
 * @author Kfir Wilfand
 * @author Bar Korkos
 * @author Zehavit Otmazgin
 * @author Noam Drori
 * @author Sapir Hochma
 */
public class SubscriberController {
	/** instance is a singleton of the class */
	private static SubscriberController instance;
	/** alert is an object of AlertController */
	static AlertController alert = new AlertController();

	private SubscriberController() {
	}

	/**
	 * getInstance is creating the singleton object of the class
	 */
	public static SubscriberController getInstance() {
		if (instance == null) {
			instance = new SubscriberController();
		}
		return instance;
	}

	/**
	 * getSubscriberDetails is getting the subscriber details
	 * 
	 * @param msg contains the message from the client
	 * @throws SQLException when occurs
	 * @return Message
	 */
	public Message getSubscriberDetails(Object msg) throws SQLException {
		Subscriber subscriber = getSubscriberById((String) ((Message) msg).getObj());
		if (subscriber != null) {
			return new Message(OperationType.GetSubscriberDetails, subscriber, ReturnMessageType.Successful);
		} else
			return new Message(OperationType.GetSubscriberDetails, null, ReturnMessageType.Unsuccessful);
	}

	/**
	 * getSubscriberById is getting the subscriber by ID
	 * 
	 * @param userId is the user id
	 * @throws SQLException when occurs
	 * @return Subscriber
	 */
	public static Subscriber getSubscriberById(String userId) throws SQLException {

		String subscriberQuery = "SELECT b.subNum, a.usrName, a.usrPassword, a.usrFirstName, a.usrLastName, a.usrEmail, a.usrType,b.subPhoneNum, b.subLatesCounter, b.subStatus"
				+ ",b.subGraduationDate FROM obl.users as a right join obl.subscribers as b on a.usrId=b.subNum WHERE a.usrId = "
				+ userId;

		DBcontroller dbControllerObj = DBcontroller.getInstance();
		ResultSet subscriber_res = dbControllerObj.query(subscriberQuery);
		if (subscriber_res.next()) {
			String historySubscriberQuery = "SELECT * " + "FROM obl.subscribers_history " + "WHERE `subNum` = "
					+ subscriber_res.getInt("subNum");

			ResultSet history_res = dbControllerObj.query(historySubscriberQuery);

			ReaderCard readerCard = new ReaderCard(ReaderCardStatus.stringToEnum(subscriber_res.getString("subStatus")),
					subscriber_res.getInt("subLatesCounter"));

			while (history_res.next()) {
				SubscriberHistoryType subscriberHistoryType = SubscriberHistoryType
						.stringToEnum(history_res.getString("actionType"));

				readerCard.getHistory().get(subscriberHistoryType).add(
						new HistoryItem(history_res.getDate("actionDate"), history_res.getString("actionDescription")));
			}

			Subscriber subscriber = new Subscriber(subscriber_res.getInt("subNum"), subscriber_res.getString("usrName"),
					subscriber_res.getString("usrPassword"), subscriber_res.getString("usrFirstName"),
					subscriber_res.getString("usrLastName"), subscriber_res.getString("usrEmail"),
					UserType.stringToEnum(subscriber_res.getString("usrType")), subscriber_res.getString("subPhoneNum"),
					readerCard, subscriber_res.getDate("subGraduationDate"));
			return subscriber;
		}
		return null;
	}

	/**
	 * updateDetails is updating details of subscriber
	 * 
	 * @param msg is the message from the client
	 * @throws SQLException when occurs
	 * @return Message
	 */
	public Message updateDetails(Object msg) throws SQLException {
		String query = (String) ((Message) msg).getObj();
		DBcontroller dbControllerObj = DBcontroller.getInstance();
		String[] arr = query.split(";");
		Boolean res = dbControllerObj.update(arr[0]);
		Boolean res1 = dbControllerObj.update(arr[1]);
		
		SubscriberController scObj=SubscriberController.getInstance();
		HistoryItem hRecord=new HistoryItem(Integer.valueOf(arr[2]),"subscriber update his profile details",SubscriberHistoryType.EditProfile);
		scObj.addHistoryRecordBySubId(new Message(OperationType.EditDetailsBySubscriber,hRecord ));
		
		if (res && res1)
			return new Message(OperationType.EditDetailsBySubscriber, null, ReturnMessageType.Successful);
		else
			return new Message(OperationType.EditDetailsBySubscriber, null, ReturnMessageType.Unsuccessful);
	}

	/**
	 * addHistoryRecordBySubId add a new record to subscriber history table
	 * 
	 * @param msg is the message from the client with subsciberId, type of history
	 *            record and description
	 * @throws SQLException when occurs
	 * @return Message
	 */
	public Message addHistoryRecordBySubId(Object msg) throws SQLException {

		Message recordMsg = ((Message) msg);
		HistoryItem historyItem = ((HistoryItem) recordMsg.getObj());
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		
		String recordQuery = "INSERT INTO `obl`.`subscribers_history` (`subNum`, `actionDate`, `actionDescription`, `actionType`) VALUES ('"
				+ historyItem.getSubId() + "', '" + timestamp+ "', '" + historyItem.getDescription() + "', '"
				+ historyItem.getSubscriberHistoryType() + "');";
		
		DBcontroller dbControllerObj = DBcontroller.getInstance();
	
		if (dbControllerObj.update(recordQuery))// if order executed
			return new Message(OperationType.AddHistoryRecord, null, ReturnMessageType.Successful);
		else
			return new Message(OperationType.AddHistoryRecord, null, ReturnMessageType.Unsuccessful);
	}

	/**
	 * orderBook is ordering a book by subscriber
	 * 
	 * @param msg is the message from the client
	 * @throws SQLException when occurs
	 * @return Message
	 */
	public Message orderBook(Object msg) throws SQLException {

		Message orderMsg = ((Message) msg);
		int tempSubNum = ((BookInOrder) orderMsg.getObj()).getSubNum();
		int tempBookCatalogNum = ((BookInOrder) orderMsg.getObj()).getbCatalogNum();
		Timestamp orderDate = ((BookInOrder) orderMsg.getObj()).getDateOfOrder();
		DBcontroller dbControllerObj = DBcontroller.getInstance();
		String checkIfOrderAlreadyExist = "select boSubNum, boCatalogNum from obl.books_orders where boSubNum="
				+ tempSubNum + " and boCatalogNum='" + tempBookCatalogNum + "'";
		ResultSet checkIfOrderAlreadyExist_res = dbControllerObj.query(checkIfOrderAlreadyExist);

		if (checkIfOrderAlreadyExist_res.next())
			return new Message(OperationType.OrderBook, null, ReturnMessageType.SubscriberAlreadyInOrderList);
		else {
			Book bookToOrder = ManageStockController
					.getBookByCatalogNumber(tempBookCatalogNum);
			Queue<Subscriber> orderQueue = ManageStockController.getBookOrderQueue(bookToOrder.getCatalogNum());
			if (bookToOrder.getCopiesNum() == orderQueue.size())
				return new Message(OperationType.OrderBook, null, ReturnMessageType.FullOrderList);
			String orderQuery = "INSERT INTO OBL.books_orders (boSubNum, boCatalogNum, dateOfOrder) VALUES('"
					+ tempSubNum + "','" + tempBookCatalogNum + "','" + orderDate + "')";
			Boolean insertBookInOrder = dbControllerObj.update(orderQuery);
			if (insertBookInOrder)// if order executed
			{
	    		SubscriberController scObj=SubscriberController.getInstance();
	    		HistoryItem hRecord=new HistoryItem(tempSubNum,"Subscriber order the book: "+bookToOrder.getBookName(),SubscriberHistoryType.BooksRequest);
	    		scObj.addHistoryRecordBySubId(new Message(OperationType.ReturnBookByLibrarian,hRecord ));
				return new Message(OperationType.OrderBook, null, ReturnMessageType.Successful);
			}
			else
				return new Message(OperationType.OrderBook, null, ReturnMessageType.Unsuccessful);
		}
	}
	
	/**
	 * showBorrowedBooks is function that show all the borrowed books of a specific subscriber that log in 
	 * @param msg is the message from the client
	 * @throws SQLException when occurs
	 * @return Message
	 */
	public Message showBorrowedBooks(Object msg) throws SQLException
    {
    	String borrowQuery= (String)((Message)msg).getObj();
    	DBcontroller dbControllerObj= DBcontroller.getInstance();
    	ResultSet books_res= dbControllerObj.query(borrowQuery);
    	List<BorrowCopy> BorrowBooks_list = BorrowCopy.resultSetToList(books_res);
    	System.out.println(BorrowBooks_list);
    	if(!BorrowBooks_list.isEmpty())
    			return new Message(OperationType.ShowMyBorrowedBooks, BorrowBooks_list, ReturnMessageType.Successful);   		
    	else
    		return new Message(OperationType.ShowMyBorrowedBooks, null, ReturnMessageType.Unsuccessful);
    }
	
	/**
	 * lossCopyReport is function that update the DB that the book is loss 
	 * @param msg is the message from the client
	 * @throws SQLException when occurs
	 * @return Message
	 */
	public Message lossCopyReport(Object msg) throws SQLException
	{
		Object[] updateLossCopyQuery=(Object[])((Message)msg).getObj();
    	DBcontroller dbControllerObj= DBcontroller.getInstance();
    	boolean query_res1= dbControllerObj.update((String)updateLossCopyQuery[0]);
    	boolean query_res2= dbControllerObj.update((String)updateLossCopyQuery[1]);
    	int subNum=(Integer)updateLossCopyQuery[2];
    	String bookNameLoss=(String)updateLossCopyQuery[3];

    	
    	if (query_res1&&query_res2)
    	{
	    		HistoryItem hRecord=new HistoryItem(subNum,"Subscriber Loss the book "+bookNameLoss,SubscriberHistoryType.BooksReturn);
	    		addHistoryRecordBySubId(new Message(OperationType.ReturnBookByLibrarian,hRecord ));
	    		String bringAllLibrarians= "SELECT usrId from users where usrType='Librarian'";
	    		ResultSet librarians= dbControllerObj.query(bringAllLibrarians);
	    		while(librarians.next())
	    		{
	    			SendMailController.sendReminderInbox(librarians.getInt("usrID"), "Loss Reporting", "Subscriber number "+(int)updateLossCopyQuery[2]+" loss the book: "+(String)updateLossCopyQuery[3]);
	    			System.out.println("mail sent to the librarian number:"+librarians.getInt("usrID")+" inbox ");
	    		}
	    		return new Message(OperationType.LossReporting, null , ReturnMessageType.Successful);
    	}
		else
				return new Message(OperationType.LossReporting, null , ReturnMessageType.Unsuccessful);
    		
	}
	
	/**
	 * askForBorrowExtenation is function that check if the subscriber can get an extenuation of a book he
	 * borrowed and if it is possible the function update the new return date and send the librarians inbox message 
	 * @param msg is the message from the client
	 * @throws SQLException when occurs
	 * @return Message
	 */
	public Message askForBorrowExtenation(Object msg) throws SQLException
	{
		Object[] borrowExtenationQ=(Object[])((Message)msg).getObj();
		int num=(int)borrowExtenationQ[1];
    	Queue<Subscriber> q=ManageStockController.getBookOrderQueue(num);
    	String subNum_query=Integer.toString((int)borrowExtenationQ[2]);
    	ReaderCardStatus status=getSubscriberById(subNum_query).getReaderCard().getStatus();
    	if(status==ReaderCardStatus.Active)
    	{
	    	if(q.isEmpty()==true)
	    	{
	    		DBcontroller dbControllerObj= DBcontroller.getInstance();
	    		boolean query_res= dbControllerObj.update((String)borrowExtenationQ[0]);
	    		if(query_res==true)
	    		{
	    			HistoryItem hRecord=new HistoryItem((int)borrowExtenationQ[2],"Subscriber get extenation to the book: "+(String)borrowExtenationQ[3],SubscriberHistoryType.BooksReturn);
		    		addHistoryRecordBySubId(new Message(OperationType.ReturnBookByLibrarian,hRecord ));
		    		System.out.println((int)borrowExtenationQ[2]);
		    		
		    		String bringAllLibrarians= "SELECT usrId from users where usrType='Librarian'";
		    		ResultSet librarians= dbControllerObj.query(bringAllLibrarians);
		    		while(librarians.next())
		    		{
		    			SendMailController.sendReminderInbox(librarians.getInt("usrID"), "Extenation Approve", "Subscriber number "+(int)borrowExtenationQ[2]+" get extenation to the book: "+(String)borrowExtenationQ[3]);
		    			System.out.println("mail sent to the librarian number:"+librarians.getInt("usrID")+" inbox ");
		    		}
		    		return new Message(OperationType.AutomaticBorrowExtenation, null , ReturnMessageType.Successful);
	    		}
	    		return new Message(OperationType.AutomaticBorrowExtenation, null , ReturnMessageType.Unsuccessful);
	    	}	
	    	return new Message(OperationType.AutomaticBorrowExtenation, null , ReturnMessageType.BookHaveWaitingList);
    	}
    	return new Message(OperationType.AutomaticBorrowExtenation, null , ReturnMessageType.SubscriberStatusNotActive);
	}
}
    	
	
