package server;

import java.io.IOException;
import java.sql.Date;
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
import common.entity.ActiviySnapshot;
import common.entity.BookInOrder;
import common.entity.Librarian;
import common.entity.LibraryManager;
import common.entity.Subscriber;
import common.entity.User;
import common.entity.enums.UserType;
import javafx.scene.control.Alert.AlertType;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;
import server.controllers.AutomaticFunctionsController;
import server.controllers.DBcontroller;
import common.controllers.FilesController;
import server.controllers.LibrarianController;
import server.controllers.LibraryManagerController;
import server.controllers.ManageStockController;
import server.controllers.ReaderController;
import server.controllers.StatisticController;
import server.controllers.SubscriberController;

/**
 * The ServerConsole extends AbstractServer represent the server's console
 * 
 * @author Kfir Wilfand
 * @author Bar Korkos
 * @author Zehavit Otmazgin
 * @author Noam Drori
 * @author Sapir Hochma
 */
public class ServerConsole extends AbstractServer {
	final public static int DEFAULT_PORT = 5555;

	public static ArrayList<Integer> connectedClients;
	private int port;

	/**
	 * ServerConsole method
	 * 
	 * @param port
	 */
	public ServerConsole(int port) {
		super(port);

		connectedClients = new ArrayList<Integer>();
	}

	private static final Logger LOGGER = Logger.getLogger(ServerConsole.class.getName());

	/**
	 * handleMessageFromClient method handle with messages from the client with
	 * switch
	 * 
	 * @param msg
	 * @param client
	 * @exception Exception
	 */
	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		LOGGER.info("Message received: " + msg + " from " + client);
		Message returnMessageToClient;
		ReaderController readerControllerObj = ReaderController.getInstance();
		SubscriberController subscriberControllerObj = SubscriberController.getInstance();
		LibrarianController librarianControllerObj = LibrarianController.getInstance();
		ManageStockController manageStockControllerObj = ManageStockController.getInstance();
		StatisticController statisticController = StatisticController.getInstance();
		LibraryManagerController libraryManagerControllerObj = LibraryManagerController.getInstance();
		
		try {
			switch (((Message) msg).getOperationType()) {
			case Login:
				returnMessageToClient = readerControllerObj.login(msg);
				// LOGGER.severe(returnMessageToClient.getReturnMessageType());
				this.sendToClient(returnMessageToClient, client);
				break;
			case Logout:
				Integer usrID = ((User) ((Message) msg).getObj()).getId();
				connectedClients.remove(usrID);
				break;
			case SearchBook:
				returnMessageToClient = readerControllerObj.searchBook(msg);
				this.sendToClient(returnMessageToClient, client);
				break;
			case GetSubscriberDetails:
				returnMessageToClient = subscriberControllerObj.getSubscriberDetails(msg);
				this.sendToClient(returnMessageToClient, client);
				break;
			case SearchSubscriber:
				returnMessageToClient = librarianControllerObj.searchSubscriber(msg);
				this.sendToClient(returnMessageToClient, client);
				break;
			case EditDetailsBySubscriber:
				returnMessageToClient = subscriberControllerObj.updateDetails(msg);
				this.sendToClient(returnMessageToClient, client);
				break;
			case AddNewSubscriberByLibrarian:
				returnMessageToClient = librarianControllerObj.createNewSubscriber(msg);
				this.sendToClient(returnMessageToClient, client);
				break;
			case SearchBookOnManageStock:
				returnMessageToClient = readerControllerObj.searchBook(msg);
				this.sendToClient(returnMessageToClient, client);
				break;
			case AddNewBook:
				returnMessageToClient = manageStockControllerObj.addNewBook(msg);
				this.sendToClient(returnMessageToClient, client);
				break;
			case GetCopiesOfSelectedBook:
				returnMessageToClient = manageStockControllerObj.getCopiesbyCatalogNumber(msg);
				this.sendToClient(returnMessageToClient, client);
				break;
			case AddNewCopy:
				returnMessageToClient = manageStockControllerObj.addNewCopy(msg);
				this.sendToClient(returnMessageToClient, client);
				break;
			case DeleteCopy:
				returnMessageToClient = manageStockControllerObj.deleteCopy(msg);
				this.sendToClient(returnMessageToClient, client);
				break;
			case EditDetailsByLibrarian:
				returnMessageToClient = manageStockControllerObj.editDetailsByLibrarian(msg);
				this.sendToClient(returnMessageToClient, client);
				break;
			case UpdateBookDetails:
				returnMessageToClient=manageStockControllerObj.updateBookDetails(msg);
				this.sendToClient(returnMessageToClient, client);
				break;
			case OrderBook:
				returnMessageToClient = subscriberControllerObj.orderBook(msg);
				this.sendToClient(returnMessageToClient, client);
				break;

			case BorrowBookByLibrarian:
				returnMessageToClient = librarianControllerObj.borrowBook(msg);
				this.sendToClient(returnMessageToClient, client);
				break;
			case ReturnBookByLibrarian:
				returnMessageToClient = librarianControllerObj.returnBook(msg);
				this.sendToClient(returnMessageToClient, client);
				break;
			case ShowMyBorrowedBooks:
				returnMessageToClient = subscriberControllerObj.showBorrowedBooks(msg);
				this.sendToClient(returnMessageToClient, client);
				break;
			case LossReporting:
				returnMessageToClient = subscriberControllerObj.lossCopyReport(msg);
				this.sendToClient(returnMessageToClient, client);
				break;
			case DownloadTableOfContent:
				returnMessageToClient = readerControllerObj.sendTableOfContantToClient((Message) msg);
				this.sendToClient(returnMessageToClient, client);
				break;
			case ShowBookPhoto:
				returnMessageToClient = manageStockControllerObj.sendBookPhotoToClient((Message) msg);
				this.sendToClient(returnMessageToClient, client);
				break;
			case ShowBookPhotoOnSearchBookDetails:
				returnMessageToClient = manageStockControllerObj.sendBookPhotoToClient((Message) msg);
				returnMessageToClient.setOperationType(OperationType.ShowBookPhotoOnSearchBookDetails);
				this.sendToClient(returnMessageToClient, client);
				break;
			case AddHistoryRecord:
				returnMessageToClient = subscriberControllerObj.addHistoryRecordBySubId((Message) msg);
				this.sendToClient(returnMessageToClient, client);
				break;
			case GetBookStatstic:
				returnMessageToClient = statisticController.getBookStatstic(msg);
				this.sendToClient(returnMessageToClient, client);
				break;
			case GetActiviySnapshotByDate:
				returnMessageToClient = statisticController.getSingleActiviySnapshotByDate((Message) msg);
				this.sendToClient(returnMessageToClient, client);
				break;
			case GetActiviySnapshotsByPeriod:
				returnMessageToClient = statisticController.getSingleActiviySnapshotByPeriod((Message) msg);
				this.sendToClient(returnMessageToClient, client);
				break;
			case GetLastActiviySnapshotRecord:
				returnMessageToClient = statisticController.getLastActiviySnapshotRecord((Message) msg);
				this.sendToClient(returnMessageToClient, client);
				break;
			case GetAllLatesReturnBySingleBookCatId:
				returnMessageToClient = statisticController.getLateBookStatsticByCatId((Message) msg);
				this.sendToClient(returnMessageToClient, client);
				break;
			case GetInboxMsg:
				returnMessageToClient=readerControllerObj.getInboxMessage((Message)msg);
				this.sendToClient(returnMessageToClient, client);
				break;	
			case makeAsRead:
				readerControllerObj.makeAsRead((Message)msg);
				break;	
			case ExtensionBookByLibrarian:
				returnMessageToClient=librarianControllerObj.extensionBookManually((Message)msg);
				this.sendToClient(returnMessageToClient, client);
				break;
			case LockReaderCard:
				returnMessageToClient=libraryManagerControllerObj.lockReaderCard((Message)msg);
				this.sendToClient(returnMessageToClient, client);
				break;
			case ChangeToActiveReaderCard:
				returnMessageToClient=libraryManagerControllerObj.changeToActiveReaderCard((Message)msg);
				this.sendToClient(returnMessageToClient, client);
				break;
			case AutomaticBorrowExtenation:
				returnMessageToClient=subscriberControllerObj.askForBorrowExtenation((Message)msg);
				this.sendToClient(returnMessageToClient, client);
				break;	
			}

		} catch (Exception ex) {
			LOGGER.severe("ERROR in handleMessageFromClient: " + ex);
		}

	}

	/**
	 * serverStarted method
	 */
	@Override
	public void serverStarted() {
		LOGGER.log(Level.INFO, "Server listening for connections on port " + getPort());
		AutomaticFunctionsController afObj = AutomaticFunctionsController.getInstance();
		//afObj.startExecutionAt(00, 00, 01);
	}

	/**
	 * serverStopped method
	 */
	@Override
	public void serverStopped() {
		ServerStarter.server.stopListening();
		LOGGER.log(Level.INFO, "Server has stopped listening for connections.");
	}
	


}
