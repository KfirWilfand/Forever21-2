package client.controllers;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import client.ViewStarter;
import client.controllers.adapters.MessageManager;
import common.controllers.Message;
import common.controllers.enums.OperationType;
import common.entity.Book;
import common.entity.BookInOrder;
import common.entity.Subscriber;
import common.entity.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

/**
 * The BookDetailsController class represent all the details about a book fxml functionality 
 * @author  Kfir Wilfand
 * @author Bar Korkos
 * @author Zehavit Otmazgin
 * @author Noam Drori
 * @author Sapir Hochma
 */
public class BookDetailsController {
	
	/** lblBookNameDetails is a label that contains book name */
	@FXML
	private Label lblBookNameDetails;

	/** lblAuthorDetails is a label that contains the author */
	@FXML
	private Label lblAuthorDetails;

	/** btnTableOfContent is a button that redirect to the table of contents of the book */
	@FXML
	private Button btnTableOfContent;
	
	/** txtBookLocation is a text that contains the location on shelf */
	@FXML
	private Text txtBookLocation;
	
	/** txtAvailableCopies is a text that contains the number of aviable copies */
	@FXML
	private Text txtAvailableCopies;

	/** btnCloseBookDetails is a button that shut down the book details */
	@FXML
	private Button btnCloseBookDetails;
	
	/** lblDescriptionDetails is text that contains the description of the book */
	@FXML
	private Text lblDescriptionDetails;
	
	/** paneBookDetails is the pane of the book details */
	@FXML
	private Pane paneBookDetails;
	
	/** btnOrderBook is a button that allow to place a order of the book*/
	@FXML
	private Button btnOrderBook;

	/** txtBookOrderStatusNotice is a text notice*/
	@FXML
	private Text txtBookOrderStatusNotice;
	
	/** book is an object of Book class*/
	private Book book;
	
	/** mainView is an anchor pane*/
	private AnchorPane mainView;
	

    @FXML
    private ImageView bookImage;

	 /**
   	 * initialize is check if its a random user or a subscriber in order to set the 'order' button to visible or not
   	 */
	@FXML
	public void initialize() {
		ViewStarter.client.bookDetailsControllerObj=this;
		
		paneBookDetails.setTranslateX(125);
		paneBookDetails.setTranslateY(160);
		mainView = ViewStarter.client.mainViewController.getMainView();
		String loginLabel=ViewStarter.client.mainViewController.getLblLoginAs().getText();	//checks if someone is logged in or if it's a randomly reader
		if(loginLabel== null)
			btnOrderBook.setVisible(false);
		else if(loginLabel.contains("Subscriber"))
			btnOrderBook.setVisible(true);

		else
			btnOrderBook.setVisible(false);	}


	 /**
   	 * onOrderCopyBtn place an order of a book if the number of aviable copies is eual to 0
   	 * @param event is an action event
   	 */
	@FXML
	void onOrderCopyBtn(ActionEvent event) {
		User usr=ViewStarter.client.mainViewController.getUser();
		Calendar calendar = Calendar.getInstance();				
		java.util.Date now = calendar.getTime();
		java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());
		BookInOrder bookInOrder=new BookInOrder(((Subscriber)usr).getSubscriberNum(), book.getCatalogNum(),currentTimestamp);
		ViewStarter.client.handleMessageFromClientUI(new Message(OperationType.OrderBook, bookInOrder));
	}
	
	 /**
   	 * onTableOfContentBtn display the pdf file of the table of contents of the chosen book
   	 * @param event is an action event
   	 */
	@FXML
	void onTableOfContentBtn(ActionEvent event) 
	{
         ViewStarter.client.handleMessageFromClientUI(new Message(OperationType.DownloadTableOfContent, book.getBookName() ));
	}
	
	
	public void downloadTableOC(String fileName) {
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {						        
		    	String path="";
				try {
					path = (BookDetailsController.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
				} catch (URISyntaxException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		  		path = path.substring(0, path.lastIndexOf("/"))+"/TableOfContent/";
				String str=path+fileName.replace(" ","_")+".pdf";
		        
			
				try {
					//File file = new File(str);
					Desktop.getDesktop().open(new File(str));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
	
	}
	
	

		
	 /**
   	 * onCloseBookDetailsBtn is shutting down the book details window
   	 * @param event is an action event
   	 */
	@FXML
	void onCloseBookDetailsBtn(ActionEvent event) {
		mainView.getChildren().remove(paneBookDetails);
	}

	/**
   	 * setBook id set 'book' to the bookItem
   	 * @param bookItem is the specific book
   	 */
	public void setBook(Book bookItem) {
		this.book = bookItem;
	}

	/**
   	 * updateUi is supposed to update the gui according to the number of aviable copies of a book
   	 */
	public void updateUi() {
		User usr=ViewStarter.client.mainViewController.getUser();
		lblBookNameDetails.setText(this.book.getBookName());
		lblAuthorDetails.setText(this.book.getAuthor().toString());
		//btnTableOfContent;
		//update image
		txtBookLocation.setText(this.book.getShelfLocation());
		txtAvailableCopies.setText(Integer.toString(this.book.getAvilableCopiesNum()));
		lblDescriptionDetails.setText(this.book.getDescription());
		if(usr instanceof Subscriber)
		{	String temp= ((Subscriber)usr).getReaderCard().getStatus().toString();
			if(temp.equals("Active"))
			{
				if(this.book.getAvilableCopiesNum() != 0) //if number of copies per book is not 0 then hide the order book button
				{	
					btnOrderBook.setDisable(true);
					txtBookOrderStatusNotice.setVisible(false);
				}
				else
				{
					txtBookOrderStatusNotice.setVisible(false);
					btnOrderBook.setDisable(false);
				}
			}
			else 
			{	btnOrderBook.setDisable(true);
				txtBookOrderStatusNotice.setVisible(true);
			}
		}
		
		ViewStarter.client.handleMessageFromClientUI(new Message(OperationType.ShowBookPhotoOnSearchBookDetails, book.getBookName()));
	}
	
    public void showPhoto(String fileName) throws URISyntaxException
    {    	
    	String path =(BookDetailsController.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
  		path = path.substring(0, path.lastIndexOf("/"))+"/BooksImages/";
		String str=path+fileName.replace(" ","_")+".png";
		System.out.println(str);
		bookImage.setImage(new Image(new File(str).toURI().toString()));
    }
	
}


