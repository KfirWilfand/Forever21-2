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

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
				Parent root = (Parent) loader.load();
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

	public void setBtnPressed(boolean isHomePagePressed, boolean isSearchPressed, boolean isProfilePressed) {
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

}
