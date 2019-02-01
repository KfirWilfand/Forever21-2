package client.controllers.adapters;

import java.util.List;

import client.ViewStarter;
import client.controllers.Utils;
import client.controllers.Utils.SearchBookRowFactory;
import common.controllers.FilesController;
import common.controllers.Message;
import common.controllers.enums.ReturnMessageType;
import common.entity.Book;
import common.entity.BorrowBook;
import common.entity.BorrowCopy;
import common.entity.Copy;
import common.entity.Statistic;
import common.entity.Subscriber;
import common.entity.TransferFile;
import common.entity.User;
import javafx.application.Platform;
import javafx.scene.control.Alert.AlertType;

/**
 * The MessageManager class represent the cases of our operations type and how
 * we would handle each one of them on the client side
 * 
 * @author Kfir Wilfand
 * @author Bar Korkos
 * @author Zehavit Otmazgin
 * @author Noam Drori
 * @author Sapir Hochma
 */
public class MessageManager {
	/** alert */
	static AlertController alert = new AlertController();

	/** utils is a static Utils which handles with the operations type */
	static Utils utils = new Utils(ViewStarter.client.mainViewController);

	/**
	 * handle is checking with switch each possible case of our declared operation
	 * type after the return from the server
	 * 
	 * @param msg a Message that contains the respond from the server
	 * @exception printStackTrace
	 */
	public static void handle(Message msg) {
		try {
			switch (msg.getOperationType()) {
			case Login:
				switch (msg.getReturnMessageType()) {
				case Successful:
					User user = (User) msg.getObj();
					ViewStarter.client.mainViewController.onLogin(user);
					break;
				case Unsuccessful:
					utils.showAlertWithHeaderText(AlertType.ERROR, "", "Wrong User Name Or Password!");
					break;
				case ClientIsAlreadyLogin:
					utils.showAlertWithHeaderText(AlertType.ERROR, "", "User is already login");
					break;
				case SubscriberIsLocked:
					utils.showAlertWithHeaderText(AlertType.ERROR, "", "Can not Login.\n Subscriber status is LOCK! ");
					break;
				}
				break;
			case SearchBook:
				switch (msg.getReturnMessageType()) {
				case BooksFound:
					List<Book> books = (List<Book>) msg.getObj();
					ViewStarter.client.searchBookControllerObj.onGetSearchResult(books);
					break;
				case BooksNotFound:
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							ViewStarter.client.searchBookControllerObj.getLvBooks().getItems().clear();
						}
					});
					utils.showAlertWithHeaderText(AlertType.INFORMATION, "", "Do not found books!");
					break;
				}
				break;
			case GetSubscriberDetails:
				switch (msg.getReturnMessageType()) {
				case Successful:
					Subscriber subscriber = (Subscriber) msg.getObj();
					ViewStarter.client.subscriberClientControllerObj.initializeDetailsAtLogin(subscriber);
					break;
				case Unsuccessful:
					System.out.println("Error in get subscriber details");
					break;
				}
				break;
			case EditDetailsBySubscriber:
				switch (msg.getReturnMessageType()) {
				case Successful:
					utils.showAlertWithHeaderText(AlertType.INFORMATION, "", "Update details succeed!");
					break;
				case Unsuccessful:
					utils.showAlertWithHeaderText(AlertType.ERROR, "", "Update details failed!!");
					break;
				}
				break;
			case AddNewSubscriberByLibrarian:
				switch (msg.getReturnMessageType()) {
				case Successful:
					utils.showAlertWithHeaderText(AlertType.INFORMATION, "", "Subscriber was added!");
					ViewStarter.client.librarianClientControllerObj.cleanNewSubscriberFields();
					break;
				case Unsuccessful:
					utils.showAlertWithHeaderText(AlertType.ERROR, "", "Adding was failed!");
					break;
				case EmailOrPhoneAreAlreadyExists:
					utils.showAlertWithHeaderText(AlertType.ERROR, "", "Email or Phone number are already exists!");
					break;
				}
				break;
			case SearchBookOnManageStock:
				if (msg.getReturnMessageType() == ReturnMessageType.BooksFoundOnManageStock) {
					ViewStarter.client.searchBookOnManageStockControllerObj.showBookResult((List<Book>) msg.getObj());
				}
				break;
			case AddNewBook:
				switch (msg.getReturnMessageType()) {
				case Successful:
					utils.showAlertWithHeaderText(AlertType.INFORMATION, "", "Book added Successfully!");
					break;
				case Unsuccessful:
					utils.showAlertWithHeaderText(AlertType.ERROR, "", "Book added failed!");
					break;
				}
				break;
			case GetCopiesOfSelectedBook:
				ViewStarter.client.manageStockClientControllerObj.displayCopies((List<Copy>) msg.getObj());
				break;
			case SearchSubscriber:
				switch (msg.getReturnMessageType()) {
				case Successful:
					ViewStarter.client.librarianClientControllerObj.updateSearchSubscriberUI((Subscriber) msg.getObj());
					break;
				case Unsuccessful:
					utils.showAlertWithHeaderText(AlertType.ERROR, "", "Subscriber Not Found!");
					ViewStarter.client.librarianClientControllerObj.cleanAndDisableSearchSubscriberFields();
					break;
				}
				break;
			case EditDetailsByLibrarian:
				switch (msg.getReturnMessageType()) {
				case Successful:
					ViewStarter.client.librarianClientControllerObj.updateSearchSubscriberUI((Subscriber) msg.getObj());
					utils.showAlertWithHeaderText(AlertType.INFORMATION, "",
							"Subscriber details updated successfully!");
					break;
				case Unsuccessful:
					utils.showAlertWithHeaderText(AlertType.ERROR, "", "Can't update subscriber details");
					break;
				}
				break;
			case AddNewCopy:
				switch (msg.getReturnMessageType()) {
				case Successful:
					utils.showAlertWithHeaderText(AlertType.INFORMATION, "", "Copy added Successfully!");
					ViewStarter.client.manageStockClientControllerObj.addCopieToList((Copy) msg.getObj());
					ViewStarter.client.searchBookOnManageStockControllerObj.showBookDetails();
					break;
				case Unsuccessful:
					utils.showAlertWithHeaderText(AlertType.ERROR, "Error Dialog", "Copy added failed!");
					break;
				}
				ViewStarter.client.manageStockClientControllerObj.getTfEnterNewCopyID().clear();
				ViewStarter.client.manageStockClientControllerObj.getBtnAddNewCopy().setDisable(true);
				break;

			case OrderBook:
				switch (msg.getReturnMessageType()) {
				case Successful:
					utils.showAlertWithHeaderText(AlertType.INFORMATION, "",
							"Book was ordered, Email will be sent when book will arrive!");
					break;
				case Unsuccessful:
					utils.showAlertWithHeaderText(AlertType.ERROR, "", "Book order failed!");
					break;
				case SubscriberAlreadyInOrderList:
					utils.showAlertWithHeaderText(AlertType.ERROR, "",
							"You are already in the order list for this book!");
					break;
				case FullOrderList:
					utils.showAlertWithHeaderText(AlertType.ERROR, "", "Order List is full!");
					break;
				}
				break;
			case BorrowBookByLibrarian:
				switch (msg.getReturnMessageType()) {
				case SubscriberNotExist:
					utils.showAlertWithHeaderText(AlertType.ERROR, "Error Dialog", "Subscriber number is not exist!");
					break;
				case CopyNotExist:
					utils.showAlertWithHeaderText(AlertType.ERROR, "Error Dialog", "Copy ID is not exist!");
					break;
				case HoldOrLockStatus:
					utils.showAlertWithHeaderText(AlertType.ERROR, "Error Dialog", "Subscriber is Lock or Hold!");
					break;
				case CopyIsNotAvailable:
					utils.showAlertWithHeaderText(AlertType.ERROR, "Error Dialog", "Copy is not abilable!");
					break;
				case Successful:
					utils.showAlertWithHeaderText(AlertType.INFORMATION, "", "successfull borrowing");
					ViewStarter.client.librarianClientControllerObj.updateDetailsOnBorrow((Object[]) msg.getObj());
					break;
				case Unsuccessful:
					utils.showAlertWithHeaderText(AlertType.ERROR, "Error Dialog", "Error in operation!");
					break;
				}
				break;
			case UpdateBookDetails:
				switch (msg.getReturnMessageType()) {
				case Successful:
					utils.showAlertWithHeaderText(AlertType.INFORMATION, "", "Details update successfully");
					break;
				case Unsuccessful:
					utils.showAlertWithHeaderText(AlertType.ERROR, "", "Details do not update");
					break;
				}
				break;
			case DeleteCopy:
				switch (msg.getReturnMessageType()) {
				case Successful:
					utils.showAlertWithHeaderText(AlertType.INFORMATION, "", "Copy was deleted Successfully!");
					ViewStarter.client.manageStockClientControllerObj.removeCopiefromList();
					ViewStarter.client.searchBookOnManageStockControllerObj.showBookDetails();
					break;
				case Unsuccessful:
					utils.showAlertWithHeaderText(AlertType.ERROR, "", "Copy deleted was failed!");
					break;
				}
				ViewStarter.client.manageStockClientControllerObj.getBtnDeleteCopy().setDisable(true);
				break;
			case ReturnBookByLibrarian:
				switch (msg.getReturnMessageType()) {
				case Successful:
					utils.showAlertWithHeaderText(AlertType.INFORMATION, "", "successfull returning!");
					ViewStarter.client.librarianClientControllerObj.updateReturnUI((BorrowCopy) msg.getObj());
					break;
				case wrongBorrowDetails:
					utils.showAlertWithHeaderText(AlertType.ERROR, "Error Dialog", "Wrong borrow Details");
					break;
				case CopyNotExist:
					utils.showAlertWithHeaderText(AlertType.ERROR, "Error Dialog", "Copy do not exist");
					break;
				case subscriberInWaitingList:
					utils.showAlertWithHeaderText(AlertType.ERROR, "Error Dialog",
							"there is a waiting list to this book\nput the book aside.");
					break;
				case ChangeStatusToActive:
					utils.showAlertWithHeaderText(AlertType.ERROR, "Error Dialog",
							"Subscriber status changed to 'Active'.\nSubscriber was late in return");
					break;
				case ChangeStatusToLock:
					utils.showAlertWithHeaderText(AlertType.ERROR, "Error Dialog",
							"Subscriber status was changed to 'Lock'.\nSubscriber was late in return more than 3 times");
					break;
				}
				break;
			case DownloadTableOfContent:
				switch (msg.getReturnMessageType()) {
				case Successful:
					FilesController fc = FilesController.getInstance();
					Object[] o = (Object[]) msg.getObj();
					fc.SaveTableOfContent((TransferFile) o[0], (String) o[1], "../../client/boundery/tableOfContent/");
					ViewStarter.client.bookDetailsControllerObj.downloadTableOC((String) o[1]);
					break;
				case Unsuccessful:
					utils.showAlertWithHeaderText(AlertType.ERROR, "Error Dialog", "TABLE OF CONTENT DO NOT EXISTS");
					break;
				}
				break;
			case GetStatstic:
				if (msg.getReturnMessageType() == ReturnMessageType.Successful) {
					ViewStarter.client.librarianClientControllerObj.updateSearchStatsticUI((Statistic) msg.getObj());
				} else if (msg.getReturnMessageType() == ReturnMessageType.Unsuccessful) {

				} else if (msg.getReturnMessageType() == ReturnMessageType.SuccessfulWithLastSnapshotDate) {
					ViewStarter.client.librarianClientControllerObj.updateSearchStatsticUI((Statistic) msg.getObj());
					utils.showAlertWithHeaderText(AlertType.WARNING, "",
							"Can't find this activity date, display last record activity instead");
				}
				break;
			case ShowBookPhoto:
				switch (msg.getReturnMessageType()) {
				case Successful:
					FilesController fc = FilesController.getInstance();
					Object[] o = (Object[]) msg.getObj();
					fc.SavePhoto((TransferFile) o[0], (String) o[1], "../../client/boundery/photos/");
					ViewStarter.client.updateOrAddBookControllerObj.showPhoto((String) o[1]);
					break;
				case Unsuccessful:
					break;
				}
				break;
			case AddHistoryRecord:
				if (msg.getReturnMessageType() == ReturnMessageType.Successful) {
					utils.showAlertWithHeaderText(AlertType.INFORMATION, "History record","History record saved!");
				} else {
					utils.showAlertWithHeaderText(AlertType.ERROR, "History record","Can't save history record");
				}
				break;
				
			case ShowMyBorrowedBooks:
				switch (msg.getReturnMessageType()) 
				{
				case Successful:
					List<BorrowCopy> borrowBooks = (List<BorrowCopy>) msg.getObj();
					ViewStarter.client.subscriberClientControllerObj.onGetBorrowedBooksResult(borrowBooks);
					break;
				case Unsuccessful:
					utils.showAlertWithHeaderText(AlertType.ERROR, "", "Books not found!");
					break;
				}
				break;
			case LossReporting:
				switch (msg.getReturnMessageType()) 
				{
				case Successful:
					utils.showAlertWithHeaderText(AlertType.INFORMATION, "", "Loss reporting success");
					break;
				case Unsuccessful:
					utils.showAlertWithHeaderText(AlertType.ERROR, "", "Loss reporting failed!");
					break;
				}
				break;
			case AutomaticBorrowExtenation:
				switch (msg.getReturnMessageType()) 
				{
				case Successful:
					utils.showAlertWithHeaderText(AlertType.INFORMATION, "", "Automatic Borrow Extenation success");
					break;
				case Unsuccessful:
					utils.showAlertWithHeaderText(AlertType.ERROR, "", "Automatic Borrow Extenation failed!");
				case BookHaveWaitingList:
					utils.showAlertWithHeaderText(AlertType.ERROR, "", "This Book has waiting list , Sorry!");
				case SubscriberStatusNotActive:
					utils.showAlertWithHeaderText(AlertType.ERROR, "", "Your Reader Card is Hold!");
					break;
				
				}
				//ViewStarter.client.bookListItemControllerObj.getBtnAskBorrowExtenation().setDisable(true);
				break;
				
				
		
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

}
