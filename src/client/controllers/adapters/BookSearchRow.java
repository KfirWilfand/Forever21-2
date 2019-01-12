package client.controllers.adapters;

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
import java.io.IOException;

import common.entity.Book;


public class BookSearchRow extends ListCell<Book> {

	@FXML
	private Label lblBookName;

	@FXML
	private Label lblAuthor;

	@FXML
	private Text lblDescription;

	@FXML
	private ImageView ivBook;

	@FXML
	private AnchorPane tableRow;

	private Button bookDetails;

	private AnchorPane mainView;

	public BookSearchRow(AnchorPane mainView) {
		this.mainView = mainView;
		loadFXML();
	}

	private void loadFXML() {
		try {
			Parent loader = FXMLLoader.load(getClass().getResource("/client/gui/layouts/search_book_item.fxml"));
			Scene scene = new Scene(loader);
			lblBookName = (Label) scene.lookup("#lblBookName");
			lblAuthor = (Label) scene.lookup("#lblAuthor");
			lblDescription = (Text) scene.lookup("#lblDescription");
			tableRow = (AnchorPane) scene.lookup("#tableRow");
			ivBook = (ImageView) scene.lookup("#ivBook");
			bookDetails = (Button) scene.lookup("#bookDetails");

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected void updateItem(Book item, boolean empty) {
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
			bookDetails.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					try {

						Parent loginBox = FXMLLoader
								.load(getClass().getResource("/client/gui/layouts/book_details.fxml"));
						mainView.getChildren().add(loginBox);
						
						Button btnCloseBookDetails = (Button) loginBox.lookup("#btnCloseBookDetails");
						Label lblBookNameDetails = (Label) loginBox.lookup("#lblBookNameDetails");
						Label lblAuthorDetails = (Label) loginBox.lookup("#lblAuthorDetails");
						Text lblDescriptionDetails = (Text) loginBox.lookup("#lblDescriptionDetails");
						ImageView ivBookDetails = (ImageView) loginBox.lookup("#ivBookDetails");
						
						btnCloseBookDetails.setOnAction(new EventHandler<ActionEvent>() {

							@Override
							public void handle(ActionEvent arg0) {
								mainView.getChildren().remove(loginBox);
							}

						});
						


						
//						lblBookNameDetails.setText(item.getBookName());
//						lblAuthorDetails.setText(item.getAuthor());
//						lblDescriptionDetails.setText(item.getDescription());
//						Image image = new Image(item.getBookImagePath());
//						ivBookDetails.setImage(image);

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
