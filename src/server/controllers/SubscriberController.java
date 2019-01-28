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
	 * getSubscriberById is getting the subscriber ри ID
	 * 
	 * @param userId is the user id
	 * @throws SQLException when occurs
	 * @return Subscriber
	 */
	public static Subscriber getSubscriberById(String userId) throws SQLException {

		String subscriberQuery = "SELECT b.subNum, a.usrName, a.usrPassword, a.usrFirstName, a.usrLastName, a.usrEmail, a.usrType,b.subPhoneNum, b.subLatesCounter, b.subStatus,b.subGraduationDate FROM obl.users as a right join obl.subscribers as b on a.usrId=b.subNum WHERE a.usrId = "
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

		String recordQuery = "INSERT INTO `obl`.`subscribers_history` (`subNum`, `actionDate`, `actionDescription`, `actionType`) VALUES ('"
				+ historyItem.getSubId() + "', '" + LocalDate.now() + "', '" + historyItem.getDescription() + "', '"
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
					.getBookByCatalogNumber(checkIfOrderAlreadyExist_res.getInt("boCatalogNum"));
			Queue<Subscriber> orderQueue = ManageStockController.getBookOrderQueue(bookToOrder.getCatalogNum());
			if (bookToOrder.getCopiesNum() == orderQueue.size())
				return new Message(OperationType.OrderBook, null, ReturnMessageType.FullOrderList);
			String orderQuery = "INSERT INTO OBL.books_orders (boSubNum, boCatalogNum, dateOfOrder) VALUES('"
					+ tempSubNum + "','" + tempBookCatalogNum + "','" + orderDate + "')";
			Boolean insertBookInOrder = dbControllerObj.update(orderQuery);
			if (insertBookInOrder)// if order executed
				return new Message(OperationType.OrderBook, null, ReturnMessageType.Successful);
			else
				return new Message(OperationType.OrderBook, null, ReturnMessageType.Unsuccessful);
		}
	}
	
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
	
	public Message lossCopyReport(Object msg) throws SQLException
	{
		Object[] updateLossCopyQuery=(Object[])((Message)msg).getObj();
    	DBcontroller dbControllerObj= DBcontroller.getInstance();
    	boolean query_res1= dbControllerObj.update((String)updateLossCopyQuery[0]);
    	boolean query_res2= dbControllerObj.update((String)updateLossCopyQuery[1]);
    	if (query_res1&&query_res2)
				return new Message(OperationType.LossReporting, null , ReturnMessageType.Successful);
		else
				return new Message(OperationType.LossReporting, null , ReturnMessageType.Unsuccessful);
    		
	}

}