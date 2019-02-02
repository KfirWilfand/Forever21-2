package client.controllers.adapters;

import java.io.IOException;


import common.entity.BorrowBook;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;


public class BorrowBookRow extends ListCell<BorrowBook> {

	/** lblBookName is a label that contains book name */
	@FXML
	private Label lblBookName;

	/** lblAuthor is a label that contains author  */
	@FXML
	private Label lblAuthor;

	/** lblDescription is a text "description" */
	@FXML
	private Text lblDescription;

	/** ivBook is a ImageView*/
	@FXML
	private ImageView ivBook;
	
	/** tableRow is a anchor pane*/
	@FXML
	private AnchorPane tableRow;

	/** bookDetails is a button*/
	private Button bookDetails;

	/** mainView is a anchor pane*/
	private AnchorPane mainView;

	/**
	 * BorrowBookRow is loading the main view fxml
	 * @param   mainView  is an anchor pane
	 */
	public BorrowBookRow(AnchorPane mainView) {
		this.mainView = mainView;
		loadFXML();
	}


	/**
	 * loadFXML is loading the book list items
	 * @exception RuntimeException if any run time occurs
	 */
	private void loadFXML() {
		try {
			Parent loader = FXMLLoader.load(getClass().getResource("/client/gui/layouts/book_list_item.fxml"));
			Scene scene = new Scene(loader);
//			lblBookName = (Label) scene.lookup("#lblBookName");
//			lblAuthor = (Label) scene.lookup("#lblAuthor");
//			lblDescription = (Text) scene.lookup("#lblDescription");
			tableRow = (AnchorPane) scene.lookup("#tableRow");
//			ivBook = (ImageView) scene.lookup("#ivBook");
//			bookDetails = (Button) scene.lookup("#bookDetails");

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * updateItem is display book
	 * @param   item  is the borrowed book
	 * @param   empty  tells if there is not such book means if null
	 */
	@Override
	protected void updateItem(BorrowBook item, boolean empty) {
		super.updateItem(item, empty);
		if (empty) {
			setText(null);
			setContentDisplay(ContentDisplay.TEXT_ONLY);
		} else {
//			lblBookName.setText(item.getBookName());
//			lblAuthor.setText(item.getAuthor());
//			lblDescription.setText(item.getDescription());
//			Image image = new Image(item.getBookImagePath());
//			ivBook.setImage(image);
			
			setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
			setGraphic(tableRow);
		}
	}
}
