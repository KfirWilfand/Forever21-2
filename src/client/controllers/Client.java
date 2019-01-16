package client.controllers;

import java.io.IOException;
import java.util.logging.Logger;

import client.controllers.adapters.MessageManager;
import common.controllers.Message;
import common.entity.User;
import ocsf.client.AbstractClient;

public class Client extends AbstractClient {
	private static final Logger LOGGER = Logger.getLogger(Client.class.getName());
	public MainViewController mainViewController;
	public SearchBookController searchBookControllerObj;
	public SubscriberClientController subscriberClientControllerObj;
	public LibrarianClientController librarianClientControllerObj;

	public Client(String host, int port) throws IOException {
		super(host, port);
		openConnection();
	}

	@Override
	protected void handleMessageFromServer(Object msg) {
		try {
			System.out.println("handleMessageFromServer " + ((Message) msg));
			MessageManager.handle((Message) msg);

		} catch (Exception e) {

			LOGGER.severe("Could not handle message from server. " + e);
		}

	}

	public void handleMessageFromClientUI(Object message) {
		try {
			sendToServer(message);
		} catch (IOException e) {

			LOGGER.severe("Could not send message to server.  Terminating client." + e);
		}
	}

}
