package client.controllers.adapters;

import java.util.List;

import client.ViewStarter;
import client.controllers.Utils;
import client.controllers.Utils.SearchBookRowFactory;
import common.controllers.Message;
import common.controllers.enums.ReturnMessageType;
import common.entity.Book;
import common.entity.BorrowBook;
import common.entity.BorrowCopy;
import common.entity.Copy;
import common.entity.Subscriber;
import common.entity.User;
import javafx.application.Platform;
import javafx.scene.control.Alert.AlertType;

public class MessageManager {

	static AlertController alert = new AlertController();
	static Utils utils = new Utils(ViewStarter.client.mainViewController);
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
					utils.showAlertWithHeaderText(AlertType.ERROR,"", "Wrong User Name Or Password!");
					//alert.error("Wrong User Name Or Password!", "");
					break;
				case ClientIsAlreadyLogin:
					utils.showAlertWithHeaderText(AlertType.ERROR,"", "User is already login");
					break;
				case SubscriberIsLocked:
					utils.showAlertWithHeaderText(AlertType.ERROR,"", "Can not Login.\n Subscriber status is LOCK! ");
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
					utils.showAlertWithHeaderText(AlertType.INFORMATION,"", "Do not found books!");
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
					utils.showAlertWithHeaderText(AlertType.INFORMATION,"", "Update details succeed!");
					//alert.info("Update details succeed!", "");
					break;
				case Unsuccessful:
					utils.showAlertWithHeaderText(AlertType.ERROR,"", "Update details failed!!");
					//alert.error("Update details failed!", "");
					break;
				}
				break;
			case AddNewSubscriberByLibrarian:
				switch (msg.getReturnMessageType()) {
				case Successful:
					//alert.info("Subscriber was added!", "");
					utils.showAlertWithHeaderText(AlertType.INFORMATION,"", "Subscriber was added!");
					ViewStarter.client.librarianClientControllerObj.cleanNewSubscriberFields();
					break;
				case Unsuccessful:
					utils.showAlertWithHeaderText(AlertType.ERROR,"", "Adding was failed!");
					//alert.error("Adding was failed!", "");
					break;
				case EmailOrPhoneAreAlreadyExists:
					utils.showAlertWithHeaderText(AlertType.ERROR,"", "Email or Phone number are already exists!");
					//alert.error("Email or Phone number are already exists!", "");
					break;
				}
				break;
					
			case SearchBookOnManageStock:
				if (msg.getReturnMessageType() == ReturnMessageType.BooksFoundOnManageStock) {
					ViewStarter.client.searchBookOnManageStockControllerObj.showBookResult((List<Book>) msg.getObj());
				}
				break;
			case AddNewBook:
				if (msg.getReturnMessageType() == ReturnMessageType.Successful) {
					utils.showAlertWithHeaderText(AlertType.INFORMATION,"", "Book added Successfully!");
					//alert.info("Book added Successfully!", "");
				} else {
					utils.showAlertWithHeaderText(AlertType.ERROR,"", "Book added failed!");
					//alert.error("Book added failed!", "");
				}
				break;	
			case GetCopiesOfSelectedBook:
				ViewStarter.client.manageStockClientControllerObj.displayCopies((List<Copy>) msg.getObj());
				break;
			case SearchSubscriber:
				ViewStarter.client.librarianClientControllerObj.updateSearchSubscriberUI((Subscriber) msg.getObj());
				break;
			case EditDetailsByLibrarian:
				switch (msg.getReturnMessageType()) {
				case Successful:
					ViewStarter.client.librarianClientControllerObj.updateSearchSubscriberUI((Subscriber) msg.getObj());
					utils.showAlertWithHeaderText(AlertType.INFORMATION,"", "Subscriber details updated successfully!");
					//alert.info("Subscriber details updated successfully!", "");
					break;
				case Unsuccessful:
					utils.showAlertWithHeaderText(AlertType.ERROR,"", "Can't update subscriber details");
					//alert.error("Can't update subscriber details", "");
					break;
				}
				break;
			case AddNewCopy:
				if (msg.getReturnMessageType() == ReturnMessageType.Successful) {
					utils.showAlertWithHeaderText(AlertType.INFORMATION,"", "Copy added Successfully!");
					//alert.info("Copy added Successfully!","");
					ViewStarter.client.manageStockClientControllerObj.addCopieToList((Copy) msg.getObj());
					ViewStarter.client.searchBookOnManageStockControllerObj.showBookDetails();
				} else {
					utils.showAlertWithHeaderText(AlertType.ERROR, "Error Dialog", "Copy added failed!");
				}
				ViewStarter.client.manageStockClientControllerObj.getTfEnterNewCopyID().clear();
				ViewStarter.client.manageStockClientControllerObj.getBtnAddNewCopy().setDisable(true);
			
				break;
				
			case OrderBook:
				if (msg.getReturnMessageType() == ReturnMessageType.Successful)
					alert.info("Book was ordered, SMS will be sent when book will arrive!","");
				else
					alert.error("Book cannot be ordered!","");
				break;
				
			case BorrowBookByLibrarian:
				if (msg.getReturnMessageType() == ReturnMessageType.Successful) {
					//alert.info("Book was ordered, SMS will be sent when book will arrive!","");
					utils.showAlertWithHeaderText(AlertType.INFORMATION,"", "Book was ordered, SMS will be sent when book will arrive!");
				}
				else
				{
					utils.showAlertWithHeaderText(AlertType.ERROR,"", "Book cannot be ordered!");
					//alert.error("Book cannot be ordered!","");
					//alert.error("Error in operation!","");
				}
				break;
				
			case UpdateBookDetails:
				if (msg.getReturnMessageType() == ReturnMessageType.Successful) {
					utils.showAlertWithHeaderText(AlertType.ERROR,"", "Borrow executed successfully");
					//alert.info("Borrow executed successfully","");
					ViewStarter.client.librarianClientControllerObj.updateDetailsOnBorrow((Object[]) msg.getObj());

				} else if (msg.getReturnMessageType() == ReturnMessageType.ErrorWhileTyping) {
					utils.showAlertWithHeaderText(AlertType.ERROR,"", "Error in typing! copy does not exist");
					//alert.error("Error in typing! copy does not exist","");
				}
				else {
					utils.showAlertWithHeaderText(AlertType.ERROR,"", "Error in operation!");
					//alert.error("Error in operation!","");
				}
				break;
				
			case DeleteCopy:
				if (msg.getReturnMessageType() == ReturnMessageType.Successful) {
					//alert.info("Copy was deleted Successfully!","");
					utils.showAlertWithHeaderText(AlertType.INFORMATION,"", "Copy was deleted Successfully!");
					ViewStarter.client.manageStockClientControllerObj.removeCopiefromList();
					
				} else {
					utils.showAlertWithHeaderText(AlertType.ERROR,"", "Copy deleted was failed!");
					//alert.error("Copy was deleted failed!","");
				}
				ViewStarter.client.manageStockClientControllerObj.getBtnDeleteCopy().setDisable(true);
				break;
			}

		}
		catch (Exception e) {
			e.printStackTrace();

		}
	}

}
