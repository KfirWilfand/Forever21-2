package client.controllers.adapters;

import java.io.IOException;

import common.entity.Book;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;


public class BookSearchRow extends ListCell<Book> {

    @FXML
    private AnchorPane tableRow;

    @FXML
    private ImageView ivBook;

    @FXML
    private Label lblBookName;

    @FXML
    private Label lblAuthor;

    @FXML
    private Text lblDescription;

    @FXML
    private Button btnBookInfo;



    @FXML
    void onBookInfoBtn(ActionEvent event) {

    }


	@Override
	protected void updateItem(Book item, boolean empty) {
		super.updateItem(item, empty);
		if (empty) {
			setText(null);
			setContentDisplay(ContentDisplay.TEXT_ONLY);
		} else {

			lblBookName.setText(item.getBookName());
			lblAuthor.setText(item.getAuthor().toString());
			lblDescription.setText(item.getDescription());
			//Image image = new Image(item.getBookImagePath());
		//	ivBook.setImage(image);
			btnBookInfo.setOnAction(new EventHandler<ActionEvent>() {


				@Override
				public void handle(ActionEvent e) {
					try {

						AnchorPane bookDetails = FXMLLoader.load(getClass().getResource("/client/boundery/layouts/book_details.fxml"));
						mainView.getChildren().add(bookDetails);
						

						
						btnCloseBookDetails.setOnAction(new EventHandler<ActionEvent>() {

							@Override
							public void handle(ActionEvent arg0) {
								mainView.getChildren().remove(bookDetails);
							}

						});
						


						
						lblBookNameDetails.setText(item.getBookName());
						lblAuthorDetails.setText(item.getAuthor().toString());
						lblDescriptionDetails.setText(item.getDescription());
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
