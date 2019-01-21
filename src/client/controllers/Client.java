package client.controllers;

import java.io.IOException;
import java.util.logging.Logger;

import client.controllers.adapters.AlertController;
import client.controllers.adapters.MessageManager;
import common.controllers.Message;
import common.controllers.enums.ReturnMessageType;
import common.entity.User;
import ocsf.client.AbstractClient;

public class Client extends AbstractClient {
	private static final Logger LOGGER = Logger.getLogger(Client.class.getName());
	public MainViewController mainViewController;
	public SearchBookController searchBookControllerObj;
	public SubscriberClientController subscriberClientControllerObj;
	public Utils utilsControllers;
	public LibrarianClientController librarianClientControllerObj;
	public ManageStockClientController manageStockClientControllerObj;
	public UpdateOrAddBookController updateOrAddBookControllerObj;
	public SearchBookOnManageStockController searchBookOnManageStockControllerObj;
	public AlertController alertClientControllerObj;

	public Client(String host, int port) throws IOException {
		super(host, port);
		openConnection();
	}

	@Override
	protected void handleMessageFromServer(Object msg) {
		try {
			
			if(((Message) msg).getObj()!=null)
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
