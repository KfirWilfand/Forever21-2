package server.controllers;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.controllers.Message;
import common.controllers.enums.OperationType;
import common.controllers.enums.ReturnMessageType;
import common.entity.Book;
import common.entity.HistoryItem;
import common.entity.Librarian;
import common.entity.ReaderCard;
import common.entity.Subscriber;
import common.entity.User;
import common.entity.enums.ReaderCardStatus;
import common.entity.enums.SubscriberHistoryType;
import common.entity.enums.UserType;

public class LibrarianController {

	private static LibrarianController instance;

	private LibrarianController() {
	}

	public static LibrarianController getInstance() {
		if (instance == null) {
			instance = new LibrarianController();
		}
		return instance;
	}

	public Message createNewSubscriber(Object msg) throws SQLException// NEED TO HANDLE WITH THE 2 DIALOG BOX בו זמנית
	{
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
				return new Message(OperationType.AddNewSubscriberByLibrarian, null,
						ReturnMessageType.SubscriberAddedSuccessfuly);
			else
				return new Message(OperationType.AddNewSubscriberByLibrarian, null,
						ReturnMessageType.SubscriberFailedToAdd);
		}
	}


	public Message searchSubscriber(Object msg) throws SQLException {
		Subscriber subscriber = SubscriberController.getSubscriberById((String)((Message)msg).getObj());
		if (subscriber != null) {
			return new Message(OperationType.SearchSubscriber, subscriber, ReturnMessageType.SubsciberExist);
		} else {
			return new Message(OperationType.SearchSubscriber, null, ReturnMessageType.SubsciberNotExist);
		}
	}
}
