package client.controllers;

import client.ViewStarter;
import common.entity.Book;
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

	private Book book;

	private AnchorPane mainView;

	@FXML
	public void initialize() {
		paneBookDetails.setTranslateX(100);
		paneBookDetails.setTranslateY(160);
		mainView = ViewStarter.client.mainViewController.getMainView();
	}

	@FXML
	void onOrderCopyBtn(ActionEvent event) {

	}

	@FXML
	void onTableOfContentBtn(ActionEvent event) {

	}

	@FXML
	void onCloseBookDetailsBtn(ActionEvent event) {
		mainView.getChildren().remove(paneBookDetails);
	}

	public void setBook(Book bookItem) {
		this.book = bookItem;
	}

	public void updateUi() {

		lblBookNameDetails.setText(this.book.getBookName());
		lblAuthorDetails.setText(this.book.getAuthor().toString());
        //btnTableOfContent;
		//update image
		txtBookLocation.setText(this.book.getShelfLocation());
		txtAvailableCopies.setText(Integer.toString(this.book.getCopiesNum()));
		lblDescriptionDetails.setText(this.book.getDescription());
	}

}