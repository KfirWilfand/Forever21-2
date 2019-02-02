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
import java.util.concurrent.TimeUnit;

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
 * The LibrarianController class represent the librarian controller on the
 * server side
 * 
 * @author Kfir Wilfand
 * @author Bar Korkos
 * @author Zehavit Otmazgin
 * @author Noam Drori
 * @author Sapir Hochma
 */
public class LibrarianController {
	/** alert is an object of AlertController */
	static AlertController alert = new AlertController();
	/** instance is a singleton of the class */
	private static LibrarianController instance;

	private LibrarianController() {
	}

	/**
	 * getInstance is creating the singleton object of the class
	 * 
	 * @return instance instance of LibrarianController object
	 */
	public static LibrarianController getInstance() {
		if (instance == null) {
			instance = new LibrarianController();
		}
		return instance;
	}

	/**
	 * createNewSubscriber is creating a new subscriber
	 * 
	 * @param msg contains the message from the client
	 * @throws SQLException SQLException
	 * @return Message return msg the client
	 */
	public Message createNewSubscriber(Object msg) throws SQLException {
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
				return new Message(OperationType.AddNewSubscriberByLibrarian, null, ReturnMessageType.Successful);
			else
				return new Message(OperationType.AddNewSubscriberByLibrarian, null, ReturnMessageType.Unsuccessful);
		}
	}

	/**
	 * searchSubscriber is searching a subscriber
	 * 
	 * @param msg contains the message from the client
	 * @throws SQLException SQLException
	 * @return Message return message to the client
	 */
	public Message searchSubscriber(Object msg) throws SQLException {
		Subscriber subscriber = SubscriberController.getSubscriberById((String) ((Message) msg).getObj());
		if (subscriber != null) {
			return new Message(OperationType.SearchSubscriber, subscriber, ReturnMessageType.Successful);
		} else {
			return new Message(OperationType.SearchSubscriber, null, ReturnMessageType.Unsuccessful);
		}
	}

	/**
	 * borrowBook is place a borrow of a book
	 * 
	 * @param msg contains the message from the client
	 * @throws SQLException SQLException
	 * @return Message return message to the client
	 */
	public Message borrowBook(Object msg) {
		try {
			DBcontroller dbControllerObj = DBcontroller.getInstance();
			Message queryMsg = ((Message) msg);

			Subscriber subscriber;

			subscriber = SubscriberController
					.getSubscriberById(String.valueOf(((BorrowCopy) queryMsg.getObj()).getSubNum()));

			Copy copy = ManageStockController.getCopyById(((BorrowCopy) queryMsg.getObj()).getCopyID());

			if (subscriber == null)
				return new Message(OperationType.BorrowBookByLibrarian, null, ReturnMessageType.SubscriberNotExist);

			if (copy == null)
				return new Message(OperationType.BorrowBookByLibrarian, null, ReturnMessageType.CopyNotExist);

			if (!subscriber.getReaderCard().getStatus().equals(ReaderCardStatus.Active))
				return new Message(OperationType.BorrowBookByLibrarian, null, ReturnMessageType.HoldOrLockStatus);
			if (!copy.isAvilabale()) {
				Queue<Subscriber> orderQueue = ManageStockController.getBookOrderQueue(copy.getbCatalogNum());
				if (orderQueue.isEmpty())
					return new Message(OperationType.BorrowBookByLibrarian, null, ReturnMessageType.CopyIsNotAvailable);
				else if (orderQueue.peek().getSubscriberNum() != subscriber.getSubscriberNum())
					return new Message(OperationType.BorrowBookByLibrarian, null, ReturnMessageType.CopyIsNotAvailable);
				else {
					Subscriber nextInQueue = orderQueue.remove();
					String delete_from_line = "DELETE FROM obl.book_arrived_mail where subNum="
							+ nextInQueue.getSubscriberNum() + " and copyID=" + copy.getCopyID();
					Boolean isDeleted = dbControllerObj.update(delete_from_line);
					String removeFromLine = "delete from obl.books_orders  where boSubNum="
							+ nextInQueue.getSubscriberNum() + " and boCatalogNum=" + copy.getbCatalogNum();
					Boolean isRemoved = dbControllerObj.update(removeFromLine);

				}
			}

			Book book = ManageStockController.getBookByCatalogNumber(copy.getbCatalogNum());

			if (book.isPopular()) {
				LocalDate returnDate = ((BorrowCopy) queryMsg.getObj()).getBorrowDate().toLocalDate().plusDays(3L);
				((BorrowCopy) queryMsg.getObj()).setReturnDueDate(Date.valueOf(returnDate));
			} else {
				LocalDate returnDate = ((BorrowCopy) queryMsg.getObj()).getBorrowDate().toLocalDate().plusDays(14L);
				((BorrowCopy) queryMsg.getObj()).setReturnDueDate(Date.valueOf(returnDate));
			}

			String insertBorrowBookQuery = "insert into obl.borrows (copyID, subNum, borrowDate,returnDueDate) values ('"
					+ ((BorrowCopy) queryMsg.getObj()).getCopyID() + "','"
					+ ((BorrowCopy) queryMsg.getObj()).getSubNum() + "','"
					+ ((BorrowCopy) queryMsg.getObj()).getBorrowDate() + "','"
					+ ((BorrowCopy) queryMsg.getObj()).getReturnDueDate() + "')";
			Boolean insertBorrowBook = dbControllerObj.update(insertBorrowBookQuery);
			if (copy.isAvilabale()) {
				String decreaseBookAviabilaty = "update obl.books set bAvilableCopiesNum=bAvilableCopiesNum-1 where bCatalogNum='"
						+ String.valueOf(book.getCatalogNum()) + "'";
				Boolean decreaseAviability = dbControllerObj.update(decreaseBookAviabilaty);
			}

			String updateBookCopyAviability = "update obl.copeis set isAvilable=0 where copyID='" + copy.getCopyID()
					+ "'";
			Boolean updateAviableCopy = dbControllerObj.update(updateBookCopyAviability);

			if (insertBorrowBook && updateAviableCopy) {
				Object[] arr = new Object[2];
				arr[0] = ((BorrowCopy) queryMsg.getObj());
				arr[1] = book.isPopular();

				// insert to the history
				SubscriberController scObj = SubscriberController.getInstance();
				HistoryItem hRecord = new HistoryItem(subscriber.getSubscriberNum(),
						"The book: " + book.getBookName() + " was borrowed", SubscriberHistoryType.BooksApprove);
				scObj.addHistoryRecordBySubId(new Message(OperationType.BorrowBookByLibrarian, hRecord));

				return new Message(OperationType.BorrowBookByLibrarian, arr, ReturnMessageType.Successful);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new Message(OperationType.BorrowBookByLibrarian, null, ReturnMessageType.Unsuccessful);

	}

	/**
	 * returnBook is place the return of a borrowed book and checks all the required
	 * conditions
	 * 
	 * @param msg contains the message from the client
	 * @throws SQLException SQLException
	 * @return Message return message to the client
	 */
	public Message returnBook(Object msg) throws SQLException {

		SubscriberController scObj = SubscriberController.getInstance();
		DBcontroller dbControllerObj = DBcontroller.getInstance();
		Message copyIDofReturnedBook = ((Message) msg);
		String copyIDtemp = ((BorrowCopy) copyIDofReturnedBook.getObj()).getCopyID();
		Copy copy = ManageStockController.getCopyById(copyIDtemp);
		// check if copy id is correct
		if (copy == null)
			return new Message(OperationType.ReturnBookByLibrarian, null, ReturnMessageType.CopyNotExist);

		BorrowCopy borrowCopyFromDB = ManageStockController.getBorrowCopyByCopyID(copyIDtemp);
		if (borrowCopyFromDB == null)
			return new Message(OperationType.ReturnBookByLibrarian, null, ReturnMessageType.wrongBorrowDetails);

		borrowCopyFromDB.setActualReturnDate(((BorrowCopy) copyIDofReturnedBook.getObj()).getActualReturnDate());

		Subscriber subscriber = SubscriberController.getSubscriberById(String.valueOf(borrowCopyFromDB.getSubNum()));

		ReturnMessageType op = ReturnMessageType.Unsuccessful;
		ArrayList<String> booksArray = null;
		if (subscriber.getGraduationDate().before(borrowCopyFromDB.getActualReturnDate())
				|| subscriber.getGraduationDate().equals(borrowCopyFromDB.getActualReturnDate())) {// student that
																									// graduate
			booksArray = new ArrayList<String>();
			String query = "SELECT copyID FROM obl.borrows where copyID='" + borrowCopyFromDB.getCopyID()
					+ "' and actualReturnDate is null ";
			ResultSet subscriberBorrowbooks = dbControllerObj.query(query);
			while (subscriberBorrowbooks.next())
				booksArray.add(subscriberBorrowbooks.getString("copyID"));
			if (booksArray.size() > 1)
				op = ReturnMessageType.GraduateWithMoreBooksToReturn;
			else {
				String updateGraduateSubscriberStatus = "update obl.subscribers set subStatus='Lock' where subNum='"
						+ subscriber.getSubscriberNum() + "'";
				Boolean isUpdate = dbControllerObj.update(updateGraduateSubscriberStatus);
				op = ReturnMessageType.ChangeGraduateStatusToLock;
			}
		} else {// regular subscriber
			if (borrowCopyFromDB.getActualReturnDate().after(borrowCopyFromDB.getReturnDueDate())
					&& subscriber.getReaderCard().getStatus().equals(ReaderCardStatus.Hold)) {// return not in time
				String updateSubscriberStatus = "update obl.subscribers set subStatus='Active' where subNum='"
						+ subscriber.getSubscriberNum() + "'";
				Boolean isUpdate = dbControllerObj.update(updateSubscriberStatus);
				// add to history status changing
				HistoryItem hRecord = new HistoryItem(subscriber.getSubscriberNum(),
						"Status changed from Hold to Active", SubscriberHistoryType.ChangeStatus);
				scObj.addHistoryRecordBySubId(new Message(OperationType.ReturnBookByLibrarian, hRecord));
				op = ReturnMessageType.ChangeStatusToActive;
			}

		}

		Book bookDetails = ManageStockController.getBookByCatalogNumber(copy.getbCatalogNum());

		Queue<Subscriber> orderQueue = ManageStockController.getBookOrderQueue(copy.getbCatalogNum());
		if (orderQueue.isEmpty()) {// there is no subscribers in waiting list
									// update number of available copies
			String incOnBooksAviableCopy = "update obl.books set bAvilableCopiesNum=bAvilableCopiesNum+1 where bCatalogNum='"
					+ copy.getbCatalogNum() + "'";
			Boolean incOnBooksAviableCopy_res = dbControllerObj.update(incOnBooksAviableCopy);

			// update copy to be available
			String returnToCopeisTable = "update obl.copeis set isAvilable=1 where copyID='" + copy.getCopyID() + "'";
			Boolean returnToCopeisTable_res = dbControllerObj.update(returnToCopeisTable);
			if (op.equals(ReturnMessageType.Unsuccessful))
				op = ReturnMessageType.Successful;

		} else {// there is subscriber in orderQueue
			Subscriber firstInLine = orderQueue.peek();

			// sand mail that the book is arrived
			String mailSubject = "Your Book is arraived";
			String mailBody = "Your Book: " + bookDetails.getBookName()
					+ "is arraived. You have two days to take it before your order cancelled.";
			SendMailController.sendMailToSubscriber(firstInLine, mailSubject, mailBody);

			String query = "insert into obl.book_arrived_mail (subNum,copyID,reminderDate) values ("
					+ firstInLine.getSubscriberNum() + ",'" + copy.getCopyID() + "','"
					+ borrowCopyFromDB.getActualReturnDate() + "')";
			Boolean insertToBookArrivedMail = dbControllerObj.update(query);
			op = ReturnMessageType.subscriberInWaitingList;

		}

		// add return to history
		String historyMsg = "The book: " + bookDetails.getBookName() + " returned.";
		if (op.equals(ReturnMessageType.ChangeStatusToActive))
			historyMsg = historyMsg + " the subscriber was late in return.";

		HistoryItem hRecord = new HistoryItem(subscriber.getSubscriberNum(), historyMsg,
				SubscriberHistoryType.BooksReturn);
		scObj.addHistoryRecordBySubId(new Message(OperationType.ReturnBookByLibrarian, hRecord));

		// update actual return date in DB
		String updateActualReturnDate = "update obl.borrows set actualReturnDate='"
				+ borrowCopyFromDB.getActualReturnDate() + "' where copyID='" + copy.getCopyID() + "' and subNum='"
				+ subscriber.getSubscriberNum() + "' and borrowDate='" + borrowCopyFromDB.getBorrowDate()
				+ "' and actualReturnDate is null";
		Boolean updateActualReturnDate_res = dbControllerObj.update(updateActualReturnDate);

		if (op.equals(ReturnMessageType.GraduateWithMoreBooksToReturn)) {
			Object[] msgObj = new Object[2];
			msgObj[0] = borrowCopyFromDB;
			msgObj[1] = booksArray;
			return new Message(OperationType.ReturnBookByLibrarian, msgObj, op);
		}
		return new Message(OperationType.ReturnBookByLibrarian, borrowCopyFromDB, op);
	}

	/**
	 * extensionBookManually is a function that check all the extension criteria and
	 * according to it choose if to make an extension
	 * 
	 * @param msg contains the message from the client
	 * @throws SQLException SQLException
	 * @return Message return message to the client
	 */
	public Message extensionBookManually(Message msg) throws SQLException {
		DBcontroller dbControllerObj = DBcontroller.getInstance();
		Message copyIDforExtensionBook = ((Message) msg);
		String copyIDtemp = ((BorrowCopy) copyIDforExtensionBook.getObj()).getCopyID();// copy id string
		Date currentDate = ((BorrowCopy) copyIDforExtensionBook.getObj()).getActualReturnDate();// current date
		Copy copy = ManageStockController.getCopyById(copyIDtemp);// get copy object
		BorrowCopy borrowCopyFromDB = ManageStockController.getBorrowCopyByCopyID(copyIDtemp);
		if (copy == null)
			return new Message(OperationType.ExtensionBookByLibrarian, null, ReturnMessageType.CopyNotExist);
		// if(borrowCopyFromDB==null)
		// return new Message(OperationType.ExtensionBookByLibrarian, null ,
		// ReturnMessageType.wrongBorrowDetails);
		Date returnDue = borrowCopyFromDB.getReturnDueDate();// getting determined return date
		if (currentDate.after(returnDue))// if today's date is greater then the determined scheduled return date
			return new Message(OperationType.ExtensionBookByLibrarian, null, ReturnMessageType.MustReturnBook);
		int subNum = borrowCopyFromDB.getSubNum();
		String checkSubStatus = "select subStatus from obl.subscribers where subNum='" + subNum + "'";
		ResultSet checkSubStatus_res = dbControllerObj.query(checkSubStatus);
		if (checkSubStatus_res.next()) {
			if (!(checkSubStatus_res.getString("subStatus").equals("Active")))// if subscriber is not active
				return new Message(OperationType.ExtensionBookByLibrarian, null,
						ReturnMessageType.CheckSubscriberStatus);
			// check if book is popular
			String checkIfPopular = "select bIsPopular, bName from obl.books where bCatalogNum='"
					+ copy.getbCatalogNum() + "'";
			ResultSet checkIfPopular_res = dbControllerObj.query(checkIfPopular);
			if (checkIfPopular_res.next()) {
				int popular = (int) checkIfPopular_res.getObject("bIsPopular");
				if (popular == 1)
					return new Message(OperationType.ExtensionBookByLibrarian, null, ReturnMessageType.PopularBook);
				// check if book in order queue
				String checkIfBookInOrderQueue = "select boSubNum from obl.books_orders where boCatalogNum='"
						+ copy.getbCatalogNum() + "'";
				ResultSet checkIfBookInOrderQueue_res = dbControllerObj.query(checkIfBookInOrderQueue);
				if (checkIfBookInOrderQueue_res.next())// if has next, means waiting list, return can't return book
					return new Message(OperationType.ExtensionBookByLibrarian, null,
							ReturnMessageType.BookHaveWaitingList);
				LocalDate todaylocaldate = LocalDate.now();
				Date todaydate = Date.valueOf(todaylocaldate);
				Long diff = borrowCopyFromDB.getReturnDueDate().getTime() - todaydate.getTime();
				int dayVar = Math.toIntExact(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
				if (dayVar > 7)
					return new Message(OperationType.ExtensionBookByLibrarian, null, ReturnMessageType.Unsuccessful);

				// if extension approved by the system
				LocalDate returnDateUpdated = borrowCopyFromDB.getReturnDueDate().toLocalDate().plusDays(7L); // set the
																												// returnDueDate
																												// to
																												// plus
																												// 7
																												// days
				((BorrowCopy) copyIDforExtensionBook.getObj()).setReturnDueDate(Date.valueOf(returnDateUpdated));
				Date newDate = ((BorrowCopy) copyIDforExtensionBook.getObj()).getReturnDueDate();
				borrowCopyFromDB.setReturnDueDate(newDate);
				String extendBookDate = "update obl.borrows set returnDueDate='" + newDate + "' where copyId='"
						+ borrowCopyFromDB.getCopyID() + "' and borrowDate='" + borrowCopyFromDB.getBorrowDate()
						+ "' and actualReturnDate is null and subNum=" + borrowCopyFromDB.getSubNum() + "";
				Boolean extendBookDateBoolean = dbControllerObj.update(extendBookDate);
				if (extendBookDateBoolean) {
					SubscriberController scObj = SubscriberController.getInstance();
					System.out.println(borrowCopyFromDB.getSubNum());
					HistoryItem hRecord = new HistoryItem(borrowCopyFromDB.getSubNum(),
							"Subscriber got extenation for the book" + checkIfPopular_res.getString("bName"),
							SubscriberHistoryType.BookExtension);
					scObj.addHistoryRecordBySubId(new Message(OperationType.ReturnBookByLibrarian, hRecord));
					return new Message(OperationType.ExtensionBookByLibrarian, borrowCopyFromDB,
							ReturnMessageType.Successful);
				} else
					return new Message(OperationType.ExtensionBookByLibrarian, null, ReturnMessageType.Unsuccessful);
			}
			return new Message(OperationType.ExtensionBookByLibrarian, null, ReturnMessageType.Unsuccessful);

		}
		return new Message(OperationType.ExtensionBookByLibrarian, null, ReturnMessageType.Unsuccessful);
	}

}
