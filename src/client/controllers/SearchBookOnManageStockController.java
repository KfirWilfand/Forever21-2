package client.controllers;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.List;

import com.itextpdf.text.DocumentException;

import client.ViewStarter;
import client.controllers.adapters.PDFGenerator;
import common.controllers.Message;
import common.controllers.enums.OperationType;
import common.entity.Book;
import common.entity.Copy;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

/**
 * The SearchBookOnManageStockController class represent the search book in
 * manage stock client controller
 * 
 * @author Kfir Wilfand
 * @author Bar Korkos
 * @author Zehavit Otmazgin
 * @author Noam Drori
 * @author Sapir Hochma
 */
public class SearchBookOnManageStockController {

	/** tfCatalogNumberForSearch is the catalog number for search */
	@FXML
	private TextField tfCatalogNumberForSearch;

	/** btnSearchBook is the search book button */
	@FXML
	private Button btnSearchBook;

	/** tvColumnCatalogNumber is the book catalog number column on table */
	@FXML
	private TableColumn<Book, Integer> tvColumnCatalogNumber;

	/** tvColumnBookName is the book name column on the table */
	@FXML
	private TableColumn<Book, String> tvColumnBookName;

	/** tvColumnCopiesNumber is copies number column in table */
	@FXML
	private TableColumn<Book, Integer> tvColumnCopiesNumber;

	/** tvColumnEditionNumber column of edition number */
	@FXML
	private TableColumn<Book, String> tvColumnEditionNumber;

	/** booksTable is table view of books */
	@FXML
	private TableView<Book> booksTable;

	/** btnDeleteCopy is save stock list button */
	@FXML
	private Button btnSavePDF;

	/** PDF_PATH is the path for saved stock result file */
	private static final String PDF_PATH = System.getProperty("user.dir") + "/output/stock_result.pdf";

	/**
	 * getBooksTable get the books table view
	 * 
	 * @return books table view
	 */
	public TableView<Book> getBooksTable() {
		return booksTable;
	}

	/**
	 * initialize the search book on manage stock view
	 */
	public void initialize() {
		ViewStarter.client.searchBookOnManageStockControllerObj = this;
		tvColumnCatalogNumber.setCellValueFactory(new PropertyValueFactory<Book, Integer>("catalogNum"));
		tvColumnBookName.setCellValueFactory(new PropertyValueFactory<Book, String>("bookName"));
		tvColumnCopiesNumber.setCellValueFactory(new PropertyValueFactory<Book, Integer>("copiesNum"));
		tvColumnEditionNumber.setCellValueFactory(new PropertyValueFactory<Book, String>("edition"));

		if (booksTable != null) {
			String getAllBooksQuery = "Select * from obl.books";
			ViewStarter.client
					.handleMessageFromClientUI(new Message(OperationType.SearchBookOnManageStock, getAllBooksQuery));
		}

	}

	/**
	 * onClickSavePDF save current stock in PDF format
	 * 
	 * @param event action event
	 * @throws DocumentException
	 * @exception IOException
	 */
	@FXML
	void onClickSavePDF(ActionEvent event) {
		PDFGenerator.getInstance().createPdf(PDF_PATH, "Book Stock Result", booksTable.getItems());
		File file = new File(PDF_PATH);
		try {
			Desktop.getDesktop().open(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * onClickSearchBook is placing the search book query
	 * 
	 * @param event action of current event
	 */
	@FXML
	void onClickSearchBook(ActionEvent event) {
		String searchBookQuery;
		if (tfCatalogNumberForSearch.getText().isEmpty()) {
			searchBookQuery = "SELECT * FROM obl.books";
		} else
			searchBookQuery = "SELECT * FROM obl.books WHERE bCatalogNum='" + tfCatalogNumberForSearch.getText() + "'";
		ViewStarter.client
				.handleMessageFromClientUI(new Message(OperationType.SearchBookOnManageStock, searchBookQuery));
	}

	/**
	 * showBookResult display book search results
	 * 
	 * @param list list of books that match the search
	 */
	public void showBookResult(List<Book> list) {
		ObservableList<Book> BookToAdd = FXCollections.observableArrayList();
		for (Book book : list)
			BookToAdd.add(book);
		booksTable.setItems(BookToAdd);
	}

	/**
	 * onChosenRow chose the rows
	 * 
	 * @param event mouse event
	 * @exception IOException
	 */
	@FXML
	void onChosenRow(MouseEvent event) {
		if (event.getClickCount() == 1) {

			ViewStarter.client.manageStockClientControllerObj.getTvCopies().getItems().clear();
			Book book = booksTable.getSelectionModel().getSelectedItem();
			String query = "SELECT * FROM obl.copeis where bCatalogNum = " + book.getCatalogNum();
			ViewStarter.client.handleMessageFromClientUI(new Message(OperationType.GetCopiesOfSelectedBook, query));

			ViewStarter.client.manageStockClientControllerObj.getTfEnterNewCopyID().textProperty()
					.addListener((observable, oldValue, newValue) -> {
						Button b1 = ViewStarter.client.manageStockClientControllerObj.getBtnAddNewCopy();
						b1.setDisable(false);

					});

//			Button b2=ViewStarter.client.manageStockClientControllerObj.getBtnDeleteCopy();
//			b2.setDisable(false);
			TextField t = ViewStarter.client.manageStockClientControllerObj.getTfEnterNewCopyID();
			t.setDisable(false);
		}
		if (event.getClickCount() == 2) {
			ViewStarter.client.manageStockClientControllerObj.getTvCopies().setVisible(false);
			ViewStarter.client.manageStockClientControllerObj.getBtnAddNewCopy().setVisible(false);
			ViewStarter.client.manageStockClientControllerObj.getBtnDeleteCopy().setVisible(false);
			ViewStarter.client.manageStockClientControllerObj.getTfEnterNewCopyID().setVisible(false);
			ViewStarter.client.manageStockClientControllerObj.getLabel1().setVisible(false);
			ViewStarter.client.manageStockClientControllerObj.getLabel2().setVisible(false);
			try {
				Parent newPane = FXMLLoader
						.load(getClass().getResource("/client/boundery/layouts/updateOrAddBook.fxml"));
				if (ViewStarter.client.manageStockClientControllerObj.getInnerPaneInManageStock() != null) {
					ViewStarter.client.manageStockClientControllerObj.getInnerPaneInManageStock().getChildren()
							.setAll(newPane);
					Button b = ViewStarter.client.updateOrAddBookControllerObj.getBtnAddBook();
					b.setVisible(false);
					b = ViewStarter.client.updateOrAddBookControllerObj.getBtnUpdate();
					b.setVisible(true);
					Book book = booksTable.getSelectionModel().getSelectedItem();
					ViewStarter.client.updateOrAddBookControllerObj.showSelectedBookDetails(book);

				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * showBookDetails display book details
	 */
	public void showBookDetails() {

		Platform.runLater(new Runnable() {
			@Override
			public void run() {

				String getAllBooksQuery = "Select * from obl.books";
				ViewStarter.client.handleMessageFromClientUI(
						new Message(OperationType.SearchBookOnManageStock, getAllBooksQuery));

			}

		});
	}

}
