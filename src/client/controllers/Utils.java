package client.controllers;

import client.ViewStarter;
import client.controllers.adapters.BookSearchRow;
import client.controllers.adapters.BorrowBookRow;
import common.entity.Book;
import common.entity.BorrowBook;
import common.entity.Librarian;
import common.entity.LibraryManager;
import common.entity.Subscriber;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

public class Utils {
	private static final Logger LOGGER = Logger.getLogger(Utils.class.getName());
	private AnchorPane apListView;

	private MainViewController mController;

	public Utils(MainViewController mController) {
		this.mController = mController;
	}

	public List<Button> getButtons(Parent root) {// get a root and return all the child buttons
		List<Button> buttons = new ArrayList<Button>();
		for (Node node : root.getChildrenUnmodifiable()) {
			if (node instanceof Button)
				buttons.add((Button) node);
		}
		return buttons;
	}

	public void paneViewSwitcher(Pane container, Parent newLoadedPane) {
		container.getChildren().clear();
		container.getChildren().add(newLoadedPane);
	}

	public class SearchBookRowFactory implements Callback<ListView<Book>, ListCell<Book>> {

		@Override
		public ListCell<Book> call(ListView<Book> param) {
			try {
				FXMLLoader loader = new FXMLLoader(
						getClass().getResource("/client/boundery/layouts/search_book_item.fxml"));
				Pane root = (Pane) loader.load();
				BookSearchRow bookSearchRow = loader.getController();
				return bookSearchRow;
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	public class BorrowBookRowFactory implements Callback<ListView<BorrowBook>, ListCell<BorrowBook>> {

		@Override
		public ListCell<BorrowBook> call(ListView<BorrowBook> param) {
			try {
				FXMLLoader loader = new FXMLLoader(
						getClass().getResource("/client/boundery/layouts/borrow_book_item.fxml"));
				Parent root = (Parent) loader.load();
				BorrowBookRow borrowBookRow = loader.getController();
				return borrowBookRow;
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	public void showAlertWithHeaderText(AlertType alertType, String title, String text) {// this alert function pops up
																							// an alert type dialog box
																							// when needed
		try {

			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Alert alert = new Alert(alertType);
					alert.setTitle(title);
					alert.setHeaderText(null);
					alert.setContentText(text);

					alert.showAndWait();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public boolean isValidEmail(String email) { // checks if an email address is valid
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
				+ "A-Z]{2,7}$";

		Pattern pat = Pattern.compile(emailRegex);
		if (email == null)
			return false;
		return pat.matcher(email).matches();
	}

	public boolean validatePhoneNumber(String phoneNo) {// checks phone number input
		// validate phone numbers of format "1234567890"
		if (phoneNo.matches("\\d{10}"))
			return true;
		// validating phone number with -, . or spaces
		else if (phoneNo.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}"))
			return true;
		// validating phone number with extension length from 3 to 5
		else if (phoneNo.matches("\\d{3}-\\d{3}-\\d{4}\\s(x|(ext))\\d{3,5}"))
			return true;
		// validating phone number where area code is in braces ()
		else if (phoneNo.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}"))
			return true;
		// return false if nothing matches the input
		else
			return false;

	}

	public void setBtnPressed(boolean isHomePagePressed, boolean isSearchPressed, boolean isProfilePressed) {
		replaceBtnImg(isHomePagePressed, mController.getBtnHomePage());
		replaceBtnImg(isSearchPressed, mController.getBtnSearchBook());
		replaceBtnImg(isProfilePressed, mController.getBtnProfile());
	}

	private void replaceBtnImg(boolean isPressed, Button btn) {
		List<Node> btnChildren = btn.getChildrenUnmodifiable();
		if (btnChildren.get(0) instanceof ImageView) {
			ImageView iv = (ImageView) btnChildren.get(0);
			String newImagePath;

			if (isPressed) {
				if (iv.getImage().getUrl().endsWith("-pressed.png"))
					newImagePath = iv.getImage().getUrl();
				else
					newImagePath = iv.getImage().getUrl().replaceFirst(".png", "-pressed.png");
			} else
				newImagePath = iv.getImage().getUrl().replaceFirst("-pressed.png", ".png");
			Image image = new Image(newImagePath);
			iv.setImage(image);
		}
	}

	public void setApListView(AnchorPane apListView) {
		this.apListView = apListView;
	}

	public void layoutSwitcher(Pane parent, String layout,String subTitle) {
		
		mController.getLblSubTitle().setText(subTitle);
		
		try {
			Parent newLoadedPane = FXMLLoader.load(getClass().getResource("/client/boundery/layouts/" + layout));
			parent.getChildren().setAll(newLoadedPane);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
