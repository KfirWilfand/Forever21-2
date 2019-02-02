package client.controllers.adapters;

import java.io.IOException;

import client.ViewStarter;
import client.controllers.BookDetailsController;
import common.controllers.Message;
import common.controllers.enums.OperationType;
import common.entity.Book;
import common.entity.TransferFile;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;


/** The BookSearchRow class that extends ListCell represents the rows in book search results. */
public class BookSearchRow extends ListCell<Book> {
	/** tableRow is an anchorPane */
	@FXML
	private AnchorPane tableRow;
	
	/** ivBook is an ImageView that displayed */
	@FXML
	private ImageView ivBook;
	
	/** lblBookName is a label that contains the name of the book */
	@FXML
	private Label lblBookName;

	/** lblAuthor is a label that contains the name of the author */
	@FXML
	private Label lblAuthor;

	/** lblDescription is a text that contains the description of the book */
	@FXML
	private Text lblDescription;

	/** btnBookInfo is a button that redirect to information about the book*/
	@FXML
	private Button btnBookInfo;

	
	
	
	
	@FXML
	void onBookInfoBtn(ActionEvent event) {

	}

	
	/**
	 * updateItem is display book
	 * @param   bookItem  is book that supposed to be displayed
	 * @param   empty  tells if there is not such book means if null
	 */
	@Override
	protected void updateItem(Book bookItem, boolean empty) {
		super.updateItem(bookItem, empty);
		

		if (empty) {
			setText(null);
			setContentDisplay(ContentDisplay.TEXT_ONLY);
		} else {

			lblBookName.setText(bookItem.getBookName());
			lblAuthor.setText(bookItem.getAuthor().toString());
			lblDescription.setText(bookItem.getDescription());
			
			// Image image = new Image(item.getBookImagePath());
			// ivBook.setImage(image);
			
			this.setOnMouseClicked(new EventHandler<MouseEvent>() {
				/**
				 * handle is displaying the book details 
				 * @param e  is a  MouseEvent
				 * @exception  IOException 
				 */
				@Override
				public void handle(MouseEvent e) {
					try {
						// Get main controller
						AnchorPane mainView = ViewStarter.client.mainViewController.getMainView();

						// load book_details
						FXMLLoader loader = new FXMLLoader();
						loader.setLocation(getClass().getResource("/client/boundery/layouts/book_details.fxml"));
						Pane bookDetails = loader.load();
						
						
						// get book_details Controller
						BookDetailsController bookDetailsController = loader.<BookDetailsController>getController();

						// Pass to book_details Controller data and update elements
						bookDetailsController.setBook(bookItem);
						bookDetailsController.updateUi();

						// Display book_details
						mainView.getChildren().add(bookDetails);

					} catch (IOException e1) {
						e1.printStackTrace();
					}

				}
			});
			
			
			
			setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
			setGraphic(tableRow);
			
		}
	}
}
