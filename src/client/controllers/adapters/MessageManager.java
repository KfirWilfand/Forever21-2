package client.controllers.adapters;


import java.util.List;

import client.ViewStarter;
import client.controllers.SearchBookController;
import common.controllers.Message;
import common.entity.Book;
import common.entity.Subscriber;
import common.entity.User;

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
//			for(Book book: books) {
//				System.out.println(book.getCatalogNum());
//			}
//						
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
			
		}
	}

}
