package client.controllers.adapters;


import java.util.List;

import client.ViewStarter;
import client.controllers.SearchBookController;
import client.controllers.Utils;
import common.controllers.Message;
import common.controllers.enums.ReturnMessageType;
import common.entity.Book;
import common.entity.Subscriber;
import common.entity.User;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class MessageManager {

	public static void handle(Message msg) {

		switch (msg.getOperationType()) {
		case Login:
			User user = (User) msg.getObj();
		
			try {
					ViewStarter.client.mainViewController.onLogin(user);
	
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case SearchBook:
			List<Book> books = (List<Book>) msg.getObj();
				
			try {
					ViewStarter.client.searchBookControllerObj.onGetSearchResult(books);
					//TODO print the books to the screen
					
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case GetSubscriberDetails:
			Subscriber subscriber = (Subscriber) msg.getObj();
			try {
				ViewStarter.client.subscriberClientControllerObj.initializeDetailsAtLogin(subscriber);
			}catch (Exception e) {
			e.printStackTrace();
			}
			break;
		case EditDetailsBySubscriber:
			try {
				Utils utils=new Utils(ViewStarter.client.mainViewController);
				if(msg.getReturnMessageType()== ReturnMessageType.UpdateSuccesfully)
				{	
					utils.showAlertWithHeaderText(AlertType.INFORMATION, "Information Dialog", "Update details succeed!");
				}
				else
				{
					utils.showAlertWithHeaderText(AlertType.ERROR, "Error Dialog", "Update details failed!");
				}
				
			}catch (Exception e) {
			e.printStackTrace();
			}
			break;
		case AddNewSubscriberByLibrarian:
			try {
				Utils utils=new Utils(ViewStarter.client.mainViewController);
				if(msg.getReturnMessageType()== ReturnMessageType.EmailOrPhoneAreAlreadyExists) 
					utils.showAlertWithHeaderText(AlertType.ERROR, "Error Dialog", "Email or Phone number are already exists!");
				
				else if(msg.getReturnMessageType()== ReturnMessageType.SubscriberAddedSuccessfuly)	
					utils.showAlertWithHeaderText(AlertType.INFORMATION, "Information Dialog", "Subscriber was added!");
				else
					utils.showAlertWithHeaderText(AlertType.ERROR, "Error Dialog", "Adding was failed!");
			}catch (Exception e) {
				e.printStackTrace();
				}
				break;
				
		case SearchBookOnManageStock:
			if(msg.getReturnMessageType() == ReturnMessageType.BooksFoundOnManageStock)
			{
				ViewStarter.client.searchBookOnManageStockControllerObj.showBookResult((Book)msg.getObj());
			}
			
		
			
			
			
		}
	}

}
