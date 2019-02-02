package client.controllers;

import java.io.IOException;
import java.util.logging.Logger;

import client.controllers.adapters.AlertController;
import client.controllers.adapters.BookListItemController;
import client.controllers.adapters.MessageManager;
import common.controllers.Message;
import common.controllers.enums.ReturnMessageType;
import common.entity.User;
import ocsf.client.AbstractClient;
/**
 * The Client class extends AbstractClient represents the fulfill of client model. Was taken from lab06.
 * @author  Kfir Wilfand
 * @author Bar Korkos
 * @author Zehavit Otmazgin
 * @author Noam Drori
 * @author Sapir Hochma
 */
public class Client extends AbstractClient {
	private static final Logger LOGGER = Logger.getLogger(Client.class.getName());
	
	/** all of the below are objects of each client controllers that we created */

	public MainViewController mainViewController;
	public SearchBookController searchBookControllerObj;
	public SubscriberClientController subscriberClientControllerObj;
	public Utils utilsControllers;
	public LibrarianClientController librarianClientControllerObj;
	public ManageStockClientController manageStockClientControllerObj;
	public UpdateOrAddBookController updateOrAddBookControllerObj;
	public SearchBookOnManageStockController searchBookOnManageStockControllerObj;
	public AlertController alertClientControllerObj;
	public BookDetailsController bookDetailsControllerObj;
	public BookListItemController bookListItemControllerObj;

	public StatisticController statisticClientControllerObj;
	public InboxController inboxControllerObj;
	 /**
   	 * Client is execute the connection to the server
   	 * @param host is the host ip number
   	 * @param port is the port number
   	 * @throws IOException if IO problems occurs
   	 */
	public Client(String host, int port) throws IOException {
		super(host, port);
		openConnection();
	}
	 /**
   	 * handleMessageFromServer is handeling the respond from the server due to client request
   	 * @param msg is the message that recieved from the server
   	 */
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
	 /**
   	 * handleMessageFromClientUI is chenneling the client's request to the server
   	 * @param message is the message that need to be sent to the server
   	 */
	public void handleMessageFromClientUI(Object message) {
		try {
			sendToServer(message);
		} catch (IOException e) {

			LOGGER.severe("Could not send message to server.  Terminating client." + e);
		}
	}

}