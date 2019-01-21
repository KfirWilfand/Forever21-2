package client.controllers.adapters;

import java.util.List;

import client.ViewStarter;
import client.controllers.Utils;
import common.controllers.Message;
import common.controllers.enums.ReturnMessageType;
import common.entity.Book;
import common.entity.BorrowBook;
import common.entity.BorrowCopy;
import common.entity.Copy;
import common.entity.Subscriber;
import common.entity.User;
import javafx.scene.control.Alert.AlertType;

public class MessageManager {

	static AlertController alert = new AlertController();

	public static void handle(Message msg) {
		try {
			switch (msg.getOperationType()) {
			case Login:
				User user = (User) msg.getObj();
				ViewStarter.client.mainViewController.onLogin(user);
				break;
			case SearchBook:
				List<Book> books = (List<Book>) msg.getObj();
				ViewStarter.client.searchBookControllerObj.onGetSearchResult(books);
				break;
			case GetSubscriberDetails:
				Subscriber subscriber = (Subscriber) msg.getObj();
				ViewStarter.client.subscriberClientControllerObj.initializeDetailsAtLogin(subscriber);
				break;
			case EditDetailsBySubscriber:
				if (msg.getReturnMessageType() == ReturnMessageType.UpdateSuccesfully) {
					alert.info("Update details succeed!", "");

				} else {
					alert.error("Update details failed!", "");
				}
				break;
			case AddNewSubscriberByLibrarian:
				if (msg.getReturnMessageType() == ReturnMessageType.EmailOrPhoneAreAlreadyExists)
					alert.error("Email or Phone number are already exists!", "");
				else if (msg.getReturnMessageType() == ReturnMessageType.SubscriberAddedSuccessfuly)
					alert.info("Subscriber was added!", "");
				else
					alert.error("Adding was failed!", "");
				break;

			case SearchBookOnManageStock:
				if (msg.getReturnMessageType() == ReturnMessageType.BooksFoundOnManageStock) {
					ViewStarter.client.searchBookOnManageStockControllerObj.showBookResult((List<Book>) msg.getObj());
				}
				break;
			case AddNewBook:
				if (msg.getReturnMessageType() == ReturnMessageType.Successful) {
					alert.info("Book added Successfully!", "");
				} else {
					alert.error("Book added failed!", "");
				}
				break;
			case GetCopiesOfSelectedBook:
				ViewStarter.client.manageStockClientControllerObj.displayCopies((List<Copy>) msg.getObj());
				break;
			case SearchSubscriber:
				ViewStarter.client.librarianClientControllerObj.updateSearchSubscriberUI((Subscriber) msg.getObj());
				break;
			case EditDetailsByLibrarian:
				if (msg.getReturnMessageType() == ReturnMessageType.Successful) {
					ViewStarter.client.librarianClientControllerObj.updateSearchSubscriberUI((Subscriber) msg.getObj());
					alert.info("Subscriber details updated successfully!", "");
				} else {
					alert.error("Can't update subscriber details", "");
				}
				break;
			case AddNewCopy:

				Utils utils = new Utils(ViewStarter.client.mainViewController);
				if (msg.getReturnMessageType() == ReturnMessageType.Successful) {
					alert.info("Copy added Successfully!","");
					ViewStarter.client.manageStockClientControllerObj.addCopieToList((Copy) msg.getObj());
				} else {
					utils.showAlertWithHeaderText(AlertType.ERROR, "Error Dialog", "Copy added failed!");
				}

			case OrderBook:
				if (msg.getReturnMessageType() == ReturnMessageType.Successful)
					alert.info("Book was ordered, SMS will be sent when book will arrive!","");
				else
					alert.error("Book cannot be ordered!","");

			case BorrowBookByLibrarian:
				if (msg.getReturnMessageType() == ReturnMessageType.Successful) {
					alert.info("Borrow executed successfully","");
					ViewStarter.client.librarianClientControllerObj.updateDetailsOnBorrow((Object[]) msg.getObj());

				} else if (msg.getReturnMessageType() == ReturnMessageType.ErrorWhileTyping)
					alert.error("Error in typing! copy does not exist","");
				else
					alert.error("Error in operation!","");

			case DeleteCopy:
				if (msg.getReturnMessageType() == ReturnMessageType.Successful) {
					alert.info("Copy was deleted Successfully!","");
				} else {
					alert.error("Copy was deleted failed!","");
				}

				break;

			case UpdateBookDetails:

				Utils utils12 = new Utils(ViewStarter.client.mainViewController);
				if (msg.getReturnMessageType() == ReturnMessageType.Successful) {
					alert.info("Book update Successfully!","");
				} else {
					alert.error("Book update failed!","");
				}

				break;
			}

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

}
