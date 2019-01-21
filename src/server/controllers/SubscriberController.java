package server.controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.controllers.Message;
import common.controllers.enums.OperationType;
import common.controllers.enums.ReturnMessageType;
import common.entity.HistoryItem;
import common.entity.ReaderCard;
import common.entity.Subscriber;
import common.entity.enums.ReaderCardStatus;
import common.entity.enums.SubscriberHistoryType;
import common.entity.enums.UserType;

public class SubscriberController {

	private static SubscriberController instance;

	private SubscriberController() {
	}

	public static SubscriberController getInstance() {
		if (instance == null) {
			instance = new SubscriberController();
		}
		return instance;
	}

	public Message getSubscriberMessage(Object msg) throws SQLException {
		Subscriber subscriber = getSubscriberById((String) ((Message) msg).getObj());
		if (subscriber != null) {
			return new Message(OperationType.GetSubscriberDetails, subscriber, ReturnMessageType.SubscriberFound);
		} else
			return new Message(OperationType.GetSubscriberDetails, null, ReturnMessageType.SubscriberNotFound);
	}

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

	public Message updateDetails(Object msg) throws SQLException {
		String query = (String) ((Message) msg).getObj();
		DBcontroller dbControllerObj = DBcontroller.getInstance();
		String[] arr = query.split(";");
		Boolean res = dbControllerObj.update(arr[0]);
		Boolean res1 = dbControllerObj.update(arr[1]);
		if (res && res1)
			return new Message(OperationType.EditDetailsBySubscriber, null, ReturnMessageType.UpdateSuccesfully);
		else
			return new Message(OperationType.EditDetailsBySubscriber, null, ReturnMessageType.NotUpdateSuccesfully);
	}

}