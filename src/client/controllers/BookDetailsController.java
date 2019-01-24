package client.controllers;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import client.ViewStarter;
import common.controllers.Message;
import common.controllers.enums.OperationType;
import common.entity.Book;
import common.entity.BookInOrder;
import common.entity.Subscriber;
import common.entity.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class BookDetailsController {

	@FXML
	private Label lblBookNameDetails;

	@FXML
	private Label lblAuthorDetails;

	@FXML
	private Button btnTableOfContent;

	@FXML
	private Text txtBookLocation;

	@FXML
	private Text txtAvailableCopies;

	@FXML
	private Button btnCloseBookDetails;

	@FXML
	private Text lblDescriptionDetails;

	@FXML
	private Pane paneBookDetails;

	@FXML
	private Button btnOrderBook;

	@FXML
	private Text txtBookOrderStatusNotice;

	private Book book;

	private AnchorPane mainView;


	@FXML
	public void initialize() {
		paneBookDetails.setTranslateX(100);
		paneBookDetails.setTranslateY(160);
		mainView = ViewStarter.client.mainViewController.getMainView();
		String loginLabel=ViewStarter.client.mainViewController.getLblLoginAs().getText();	//checks if someone is logged in or if it's a randomly reader
		if(loginLabel== null)
			btnOrderBook.setVisible(false);
		else if(loginLabel.contains("Subscriber"))
			btnOrderBook.setVisible(true);

		else
			btnOrderBook.setVisible(false);	}



	@FXML
	void onOrderCopyBtn(ActionEvent event) {
		User usr=ViewStarter.client.mainViewController.getUser();
		Calendar calendar = Calendar.getInstance();				
		java.util.Date now = calendar.getTime();
		java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());
		BookInOrder bookInOrder=new BookInOrder(((Subscriber)usr).getSubscriberNum(), book.getCatalogNum(),currentTimestamp);
		ViewStarter.client.handleMessageFromClientUI(new Message(OperationType.OrderBook, bookInOrder));
	}

	@FXML
	void onTableOfContentBtn(ActionEvent event) 
	{

	}

	@FXML
	void onCloseBookDetailsBtn(ActionEvent event) {
		mainView.getChildren().remove(paneBookDetails);
	}

	public void setBook(Book bookItem) {
		this.book = bookItem;
	}

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
	}
	
}


