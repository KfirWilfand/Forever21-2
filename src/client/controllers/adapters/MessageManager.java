package client.controllers.adapters;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import client.ViewStarter;
import client.controllers.Utils;
import client.controllers.Utils.SearchBookRowFactory;
import common.controllers.FilesController;
import common.controllers.Message;
import common.controllers.enums.OperationType;
import common.controllers.enums.ReturnMessageType;
import common.entity.ActiviySnapshot;
import common.entity.Book;
import common.entity.BookSelected;
import common.entity.BookStatistic;
import common.entity.BorrowBook;
import common.entity.BorrowCopy;
import common.entity.Copy;
import common.entity.InboxMsgItem;
import common.entity.Statistic;
import common.entity.Subscriber;
import common.entity.TransferFile;
import common.entity.User;
import javafx.application.Platform;
import javafx.scene.control.Alert.AlertType;
import server.controllers.ManageStockController;

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
	 */
	public static void handle(Message msg) {
		try {
			switch (msg.getOperationType()) {
			case Login:
				switch (msg.getReturnMessageType()) {
				case Successful:
					Object[] objMsg=(Object[])msg.getObj();
					ViewStarter.client.mainViewController.onLogin(objMsg);
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
				case CopyIsAlreadyExist:
					utils.showAlertWithHeaderText(AlertType.ERROR, "Error Dialog", "Fail - Copy is already exist!");
					break;
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
					utils.showAlertWithHeaderText(AlertType.INFORMATION, "","there is a waiting list to this book\nput the book aside.");
					ViewStarter.client.librarianClientControllerObj.updateReturnUI((BorrowCopy) msg.getObj());
					break;
				case ChangeStatusToActive:
					utils.showAlertWithHeaderText(AlertType.INFORMATION, "",
							"Subscriber status changed to 'Active'.\nSubscriber was late in return");
					ViewStarter.client.librarianClientControllerObj.updateReturnUI((BorrowCopy) msg.getObj());
					break;
				case ChangeGraduateStatusToLock:
					utils.showAlertWithHeaderText(AlertType.INFORMATION, "","Subscriber status might changed to 'Lock'.\nSubscriber was late in return more than 3 times");
					ViewStarter.client.librarianClientControllerObj.updateReturnUI((BorrowCopy) msg.getObj());
					break;
				case GraduateWithMoreBooksToReturn:
					Object[] o = (Object[]) msg.getObj();
					ArrayList<String> arr=(ArrayList<String>)o[1];
					String listString = String.join(", ", arr);
					utils.showAlertWithHeaderText(AlertType.INFORMATION, "","Graduate need to return the following books :"+listString);
				}
				break;
			case DownloadTableOfContent:
				switch (msg.getReturnMessageType()) {
				case Successful:
					FilesController fc = FilesController.getInstance();
					Object[] o = (Object[]) msg.getObj();
					
			    	String path="";
					try {
						path = (MessageManager.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
					} catch (URISyntaxException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			  		path = path.substring(0, path.lastIndexOf("/"))+"/TableOfContent/";
					
					fc.SaveTableOfContent((TransferFile) o[0], (String) o[1],path);
					ViewStarter.client.bookDetailsControllerObj.downloadTableOC((String) o[1]);
					break;
				case Unsuccessful:
					utils.showAlertWithHeaderText(AlertType.ERROR, "Error Dialog", "TABLE OF CONTENT DO NOT EXISTS");
					break;
				}
				break;
			case ShowBookPhoto:
				switch (msg.getReturnMessageType()) {
				case Successful:
					FilesController fc = FilesController.getInstance();
					Object[] o = (Object[]) msg.getObj();
					String path =(ManageStockController.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
			  		path = path.substring(0, path.lastIndexOf("/"));
	
					fc.SavePhoto((TransferFile) o[0], (String) o[1], path+"/BooksImages/");
					ViewStarter.client.updateOrAddBookControllerObj.showPhoto((String) o[1]);
					break;
				case Unsuccessful:
					break;
				}
				break;
			case AddHistoryRecord:
				if (msg.getReturnMessageType() == ReturnMessageType.Successful) {
					utils.showAlertWithHeaderText(AlertType.INFORMATION, "History record", "History record saved!");
				} else {
					utils.showAlertWithHeaderText(AlertType.ERROR, "History record", "Can't save history record");
				}
				break;
			case ShowMyBorrowedBooks:
				switch (msg.getReturnMessageType()) {
				case Successful:
					List<BorrowCopy> borrowBooks = (List<BorrowCopy>) msg.getObj();
					ViewStarter.client.subscriberClientControllerObj.onGetBorrowedBooksResult(borrowBooks);
					break;
				case Unsuccessful:
					//utils.showAlertWithHeaderText(AlertType.ERROR, "", "Books not found!");
					break;
				}
				break;
			case LossReporting:
				switch (msg.getReturnMessageType()) {
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
					ViewStarter.client.subscriberClientControllerObj.onBorrowedBooksTab(null);
					break;
				case Unsuccessful:
					utils.showAlertWithHeaderText(AlertType.ERROR, "", "Automatic Borrow Extenation failed!");
					break;
				case BookHaveWaitingList:
					utils.showAlertWithHeaderText(AlertType.ERROR, "", "This Book has waiting list , Sorry!");
					break;
				case SubscriberStatusNotActive:
					utils.showAlertWithHeaderText(AlertType.ERROR, "", "Your Reader Card is Hold!");
					break;
				
				}
				//ViewStarter.client.bookListItemControllerObj.getBtnAskBorrowExtenation().setDisable(true);
				break;
			case ShowBookPhotoOnSearchBookDetails:
				switch (msg.getReturnMessageType()) {
				case Successful:
					System.out.println("onn success");
					FilesController fc = FilesController.getInstance();
					Object[] o = (Object[]) msg.getObj();
					String path =(MessageManager.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
			  		path = path.substring(0, path.lastIndexOf("/"));
					fc.SavePhoto((TransferFile) o[0], (String) o[1], path+"/BooksImages/");
					ViewStarter.client.bookDetailsControllerObj.showPhoto((String) o[1]);
					break;
				case Unsuccessful:
					System.out.println("failllll!!!!");
					break;
				}
				break;
			case ExtensionBookByLibrarian:
				switch(msg.getReturnMessageType())
				{
				 case CopyNotExist:
					utils.showAlertWithHeaderText(AlertType.ERROR, "Error Dialog", "Copy do not exist");
					break;
				 //case wrongBorrowDetails:
					// utils.showAlertWithHeaderText(AlertType.ERROR, "Error Dialog", "Wrong Extension Details");
						//break;
				 case CheckSubscriberStatus:
						utils.showAlertWithHeaderText(AlertType.ERROR, "Error Dialog", "Subscriber status is not active!");
						break;
				 case MustReturnBook:
					 utils.showAlertWithHeaderText(AlertType.ERROR, "Error Dialog", "Subscriber must return the book due to late!");
					break;
				 case PopularBook:
					 utils.showAlertWithHeaderText(AlertType.ERROR, "", "Book cannot get an extension because book is popular!");
					break;
				 case Unsuccessful:
					 utils.showAlertWithHeaderText(AlertType.ERROR, "", "Book cannot get an extension!");
					break;
				 case Successful:
					 ViewStarter.client.librarianClientControllerObj.updateManualExtensionUI((BorrowCopy) msg.getObj());
					 utils.showAlertWithHeaderText(AlertType.INFORMATION, "", "Book just got 7 days extension!");
					break;
				 case BookHaveWaitingList:
					 utils.showAlertWithHeaderText(AlertType.ERROR, "", "Book cannot get an extension due to waiting list! ");
					break;
					 
				}
				break;
			case GetInboxMsg:
				switch (msg.getReturnMessageType()) {
				case Successful:
					ViewStarter.client.inboxControllerObj.showInboxMessages((List<InboxMsgItem>)msg.getObj());
					break;
				case Unsuccessful:
					utils.showAlertWithHeaderText(AlertType.INFORMATION, "", "No Messages");
					break;
				}
				break;
			case LockReaderCard:
				switch (msg.getReturnMessageType()) {
				case Successful:
					utils.showAlertWithHeaderText(AlertType.INFORMATION, "", "Reader Card Lock Successfully");
					ViewStarter.client.inboxControllerObj.getBtnLock().setDisable(true);
					ViewStarter.client.inboxControllerObj.getBtnActive().setDisable(false);
					break;
				case Unsuccessful:
					utils.showAlertWithHeaderText(AlertType.ERROR, "", "Fail to Lock Reader Card");
					break;	
				}		
				break;
			case ChangeToActiveReaderCard:
				switch (msg.getReturnMessageType()) {
				case Successful:
					utils.showAlertWithHeaderText(AlertType.INFORMATION, "", "Reader Card Status Change To 'Active' Successfully");
					ViewStarter.client.inboxControllerObj.getBtnLock().setDisable(false);
					ViewStarter.client.inboxControllerObj.getBtnActive().setDisable(true);
					break;
				case Unsuccessful:
					utils.showAlertWithHeaderText(AlertType.ERROR, "", "Fail to change Reader Card status");
					break;	
				}
				break;
			case GetBookStatstic:
				if (msg.getReturnMessageType() == ReturnMessageType.Successful) {
					ViewStarter.client.statisticClientControllerObj.updateBookStatsticUI((BookStatistic) msg.getObj());
				} else if (msg.getReturnMessageType() == ReturnMessageType.Unsuccessful) {
					utils.showAlertWithHeaderText(AlertType.ERROR, "Book Statstic", "No data to display");
				}
				break;
			case GetActiviySnapshotByDate:
				ActiviySnapshot activiySnapshot;
				switch (msg.getReturnMessageType()) {
				case Successful:
					activiySnapshot = (ActiviySnapshot) msg.getObj();
					ViewStarter.client.statisticClientControllerObj.onReturnActivitySnapshotByDate(activiySnapshot);
					break;
				case SuccessfulWithLastSnapshotDate:
					utils.showAlertWithHeaderText(AlertType.WARNING, "Activiy Snapshot",
							"Can't find this activity date or there is no daily activity yet , display last record activity instead");
					activiySnapshot = (ActiviySnapshot) msg.getObj();
					ViewStarter.client.statisticClientControllerObj.onReturnActivitySnapshotByDate(activiySnapshot);
					break;
				case Unsuccessful:
					utils.showAlertWithHeaderText(AlertType.ERROR, "Activiy Snapshot",
							"Can't find any activiy snapshot recoed");
					break;
				}
				break;
			case GetActiviySnapshotsByPeriod:
				switch (msg.getReturnMessageType()) {
				case Successful:
					ViewStarter.client.statisticClientControllerObj.onReturnActivitySnapshotByPeriod((List<ActiviySnapshot>)  msg.getObj());
					break;
				case Unsuccessful:
					ViewStarter.client.statisticClientControllerObj.onReturnActivitySnapshotByPeriodUnsuccessful();
					utils.showAlertWithHeaderText(AlertType.ERROR, "Activiy Snapshot",
							"Can't find any activiy in this snapshot period");
					break;
				}
				break;
			case GetLastActiviySnapshotRecord:
				switch (msg.getReturnMessageType()) {
				case Successful:
					ViewStarter.client.statisticClientControllerObj.onReturnActivitySnapshotByDate((ActiviySnapshot) msg.getObj());
					break;
				case Unsuccessful:
					utils.showAlertWithHeaderText(AlertType.ERROR, "Activiy Snapshot",
							"Can't find any activiy snapshot recoed");
					break;
				}
				break;
			case GetAllLatesReturnBySingleBookCatId:
				switch (msg.getReturnMessageType()) {
				case Successful:
					BookStatistic bookSelected = (BookStatistic) msg.getObj();
					ViewStarter.client.statisticClientControllerObj.updateBookStatsticUI(bookSelected);
					break;
				case Unsuccessful:
				//	ViewStarter.client.statisticClientControllerObj.updateBookStatsticUIUnsuccessful();
					ViewStarter.client.utilsControllers.showAlertWithHeaderText(AlertType.ERROR, "Search Single Book",
							"Can't find data on this book!");
					break;
				}
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

}
