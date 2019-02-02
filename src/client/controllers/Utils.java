package client.controllers;

import client.ViewStarter;
import client.controllers.adapters.BookListItemController;
import client.controllers.adapters.BookSearchRow;
import client.controllers.adapters.BorrowBookRow;
import common.entity.Book;
import common.entity.BorrowBook;
import common.entity.BorrowCopy;
import common.entity.Librarian;
import common.entity.LibraryManager;
import common.entity.Subscriber;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.css.Styleable;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Control;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

/**
 * The Utils class represent all the utils function
 * 
 * @author Kfir Wilfand
 * @author Bar Korkos
 * @author Zehavit Otmazgin
 * @author Noam Drori
 * @author Sapir Hochma
 */
public class Utils {
	/** LOGGER is giving information */
	private static final Logger LOGGER = Logger.getLogger(Utils.class.getName());
	private AnchorPane apListView;

	private MainViewController mController;

	public Utils(MainViewController mController) {
		this.mController = mController;
	}

	/**
	 * getButtons is getting the root and return all the child buttons
	 * 
	 * @param root
	 * @return list of children buttons
	 */
	public List<Button> getButtons(Parent root) {// get a root and return all the child buttons
		List<Button> buttons = new ArrayList<Button>();
		for (Node node : root.getChildrenUnmodifiable()) {
			if (node instanceof Button)
				buttons.add((Button) node);
		}
		return buttons;
	}

	/**
	 * paneViewSwitcher is switching between pane views
	 * 
	 * @param container     is the current
	 * @param newLoadedPane is the one we want to switch to
	 */
	public void paneViewSwitcher(Pane container, Parent newLoadedPane) {
		container.getChildren().clear();
		container.getChildren().add(newLoadedPane);
	}

	/**
	 * SearchBookRowFactory class represents book row factory that implements
	 * CallBack and ListCell
	 * 
	 * @author kfir wilfand
	 */
	public class SearchBookRowFactory implements Callback<ListView<Book>, ListCell<Book>> {

		/**
		 * call call list cell of books
		 * 
		 * @param param list view of books
		 * @return bookSearchRow null
		 */
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

	/**
	 * BorrowBookRowFactory class represents borrow book row factory that implements
	 * CallBack and ListCell
	 * 
	 * @author kfir wilfand
	 */
	public class BorrowBookRowFactory implements Callback<ListView<BorrowCopy>, ListCell<BorrowCopy>> {

		/**
		 * call call list cell of borrowed books
		 * 
		 * @param param list view of books
		 * @return borrowBookRow
		 */
		@Override
		public ListCell<BorrowCopy> call(ListView<BorrowCopy> param) {
			try {
				FXMLLoader loader = new FXMLLoader(
						getClass().getResource("/client/boundery/layouts/borrow_book_item.fxml"));
				Parent root = (Parent) loader.load();
				BookListItemController borrowBookRow = loader.getController();
				return borrowBookRow;
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	/**
	 * showAlertWithHeaderText pops up pops up when needed
	 * 
	 * @param alertType the type of alert
	 * @param title     the title
	 * @param text      the text in the alert
	 */
	public void showAlertWithHeaderText(AlertType alertType, String title, String text) {// this alert function pops up
																							// pops up
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

	/**
	 * isValidEmail returns if an email is correct
	 * 
	 * @param email email of specific subscriber
	 * @return boolean
	 */
	public boolean isValidEmail(String email) { // checks if an email address is valid
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
				+ "A-Z]{2,7}$";

		Pattern pat = Pattern.compile(emailRegex);
		if (email == null)
			return false;
		return pat.matcher(email).matches();
	}

	/**
	 * validatePhoneNumber returns if phone number is correct
	 * 
	 * @param phoneNo phone number of specific subscriber
	 * @return boolean
	 */
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

	/**
	 * setBtnPressed is set button pressed 
	 * @param isSearchPressed
	 * @param isProfilePressed
	 * @param isMailBox
	 */
	public void setBtnPressed(boolean isHomePagePressed, boolean isSearchPressed, boolean isProfilePressed,
			boolean isMailBox) {
		replaceBtnImg(isHomePagePressed, mController.getBtnHomePage());
		replaceBtnImg(isSearchPressed, mController.getBtnSearchBook());
		replaceBtnImg(isProfilePressed, mController.getBtnProfile());
		replaceBtnImg(isMailBox, mController.getBtnMailBox());
	}

	/**
	 * replaceBtnImg replacing image
	 * 
	 * @param isPressed
	 * @param btn
	 */
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

	/**
	 * setApListView is setting a list view
	 * 
	 * @param apListView is an anchor pane
	 */
	public void setApListView(AnchorPane apListView) {
		this.apListView = apListView;
	}

	/**
	 * layoutSwitcher is switching between layouts
	 * 
	 * @param parent
	 * @param layout
	 * @param subTitle
	 */
	public void layoutSwitcher(Pane parent, String layout, String subTitle) {

		mController.getLblSubTitle().setText(subTitle);

		try {
			Parent newLoadedPane = FXMLLoader.load(getClass().getResource("/client/boundery/layouts/" + layout));
			parent.getChildren().setAll(newLoadedPane);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * setStyleToList is set style to list
	 * 
	 * @param txtfields    textfields list
	 * @param css          setStyleToList
	 */
	public void setStyleToList(List<Control> txtfields, String css) {
		for (Control tf : txtfields) {
			tf.setStyle(css);
		}

	}



}
