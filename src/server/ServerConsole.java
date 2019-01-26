package server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import client.ViewStarter;
import common.controllers.Message;
import common.controllers.enums.OperationType;
import common.controllers.enums.ReturnMessageType;
import common.entity.BookInOrder;
import common.entity.Librarian;
import common.entity.LibraryManager;
import common.entity.Subscriber;
import common.entity.User;
import common.entity.enums.UserType;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;
import server.controllers.AutomaticFunctionsController;
import server.controllers.DBcontroller;
import server.controllers.LibrarianController;
import server.controllers.LibraryManagerController;
import server.controllers.ManageStockController;
import server.controllers.ReaderController;
import server.controllers.StatisticController;
import server.controllers.SubscriberController;

public class ServerConsole extends AbstractServer {
	final public static int DEFAULT_PORT = 5555;
	//public static List<BookInOrder> BooksOrders = Collections.synchronizedList(new LinkedList<BookInOrder>());
	public static ArrayList<Integer> connectedClients;
	private int port;
	
	public ServerConsole(int port) {
		super(port);
		connectedClients=new ArrayList<Integer>();
	}
	
	private static final Logger LOGGER = Logger.getLogger(ServerConsole.class.getName());

	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		LOGGER.info("Message received: " + msg + " from " + client);
		Message returnMessageToClient;
		ReaderController readerControllerObj=ReaderController.getInstance();
		SubscriberController subscriberControllerObj=SubscriberController.getInstance();
		LibrarianController librarianControllerObj=LibrarianController.getInstance();
		ManageStockController manageStockControllerObj=ManageStockController.getInstance();
		StatisticController statisticController = StatisticController.getInstance();
		
		try {
			switch (((Message) msg).getOperationType()) {
			case Login:
				returnMessageToClient=readerControllerObj.login(msg);
				//LOGGER.severe(returnMessageToClient.getReturnMessageType());
				this.sendToClient(returnMessageToClient,client);
				break;
			case Logout:
				Integer usrID=((User)((Message)msg).getObj()).getId();
				connectedClients.remove(usrID);
				break;
			case SearchBook:
				returnMessageToClient=readerControllerObj.searchBook(msg);
				this.sendToClient(returnMessageToClient,client);
				break;
			case GetSubscriberDetails:
				returnMessageToClient=subscriberControllerObj.getSubscriberDetails(msg);
				this.sendToClient(returnMessageToClient,client);
				break;	
			case SearchSubscriber:
				returnMessageToClient=librarianControllerObj.searchSubscriber(msg);
				this.sendToClient(returnMessageToClient,client);
				break;
			case EditDetailsBySubscriber:
				returnMessageToClient=subscriberControllerObj.updateDetails(msg);
				this.sendToClient(returnMessageToClient, client);
				break;
			case AddNewSubscriberByLibrarian:
				returnMessageToClient=librarianControllerObj.createNewSubscriber(msg);
				this.sendToClient(returnMessageToClient, client);
				break;
			case SearchBookOnManageStock:
				returnMessageToClient=readerControllerObj.searchBook(msg);
				this.sendToClient(returnMessageToClient, client);
				break;
			case AddNewBook:
				returnMessageToClient=manageStockControllerObj.addNewBook(msg);
				this.sendToClient(returnMessageToClient, client);
				break;
			case GetCopiesOfSelectedBook:
				returnMessageToClient=manageStockControllerObj.getCopiesbyCatalogNumber(msg);
				this.sendToClient(returnMessageToClient, client);
				break;
			case AddNewCopy:
				returnMessageToClient=manageStockControllerObj.addNewCopy(msg);
				this.sendToClient(returnMessageToClient, client);
				break;
			case DeleteCopy:
				returnMessageToClient=manageStockControllerObj.deleteCopy(msg);
				this.sendToClient(returnMessageToClient, client);
				break;
			case EditDetailsByLibrarian:
				returnMessageToClient=manageStockControllerObj.editDetailsByLibrarian(msg);
				this.sendToClient(returnMessageToClient, client);				
				break;
			case UpdateBookDetails:
				returnMessageToClient=manageStockControllerObj.updateBookDetails(msg);
				break;
			case OrderBook:
				returnMessageToClient=subscriberControllerObj.orderBook(msg);
				this.sendToClient(returnMessageToClient, client);
				break;
				
			case BorrowBookByLibrarian:
				returnMessageToClient=librarianControllerObj.borrowBook(msg);
				this.sendToClient(returnMessageToClient, client);
				break;	
			case GetStatstic:
				returnMessageToClient=statisticController.getStatstic(msg);
				this.sendToClient(returnMessageToClient, client);
				break;
			case ReturnBookByLibrarian:
				returnMessageToClient=librarianControllerObj.returnBook(msg);
				this.sendToClient(returnMessageToClient, client);
				break;	
			}
			
		} catch(Exception ex) {
			LOGGER.severe("ERROR in handleMessageFromClient: " + ex);
		}
		
	}

	@Override
	public void serverStarted() {
		LOGGER.log(Level.INFO, "Server listening for connections on port " + getPort());
		AutomaticFunctionsController afObj=AutomaticFunctionsController.getInstance();
		afObj.startExecutionAt(00,00,01);
	}

	@Override
	public void serverStopped() {
		ServerStarter.server.stopListening();
		LOGGER.log(Level.INFO, "Server has stopped listening for connections.");
	}

}
