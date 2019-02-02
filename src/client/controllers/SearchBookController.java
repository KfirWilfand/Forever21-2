package client.controllers;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.itextpdf.text.DocumentException;

import client.ViewStarter;
import client.controllers.Utils.BorrowBookRowFactory;
import client.controllers.adapters.PDFGenerator;
import client.controllers.adapters.QueryBuilder;
import common.controllers.Message;
import common.controllers.enums.OperationType;
import common.entity.Book;
import common.entity.BorrowBook;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;

/**
 * The SearchBookController class represent the search book client controller
 * 
 * @author Kfir Wilfand
 * @author Bar Korkos
 * @author Zehavit Otmazgin
 * @author Noam Drori
 * @author Sapir Hochma
 */
public class SearchBookController {

	/** tfBookName is the book name */
	@FXML
	private TextField tfBookName;

	/** tfAuthorName is a the book author name */
	@FXML
	private TextField tfAuthorName;

	/** tfBookGenre is the book genre text */
	@FXML
	private TextField tfBookGenre;

	/** tfTextFree is a the text free textfield */
	@FXML
	private TextField tfTextFree;

	/** lvBooks is the books list view */
	@FXML
	private ListView<Book> lvBooks;

	/** apListView is a anchor pane of list view */
	@FXML
	private AnchorPane apListView;

	/** btnSearch is the search button */
	@FXML
	private Button btnSearch;

	/** btnSavePDF is save search result to PDF file button */
	@FXML
	private Button btnSavePDF;

	/** list that keep the search result */
	private List<Book> books;

	/** PDF_PATH is the path for saved search result file */
	private static final String PDF_PATH = "output/search_result.pdf";


	/**
	 * initialize the book search view
	 */
	public void initialize() {
		ViewStarter.client.searchBookControllerObj = this;
		ViewStarter.client.utilsControllers.setApListView(apListView);
	}

	/**
	 * onSearch open is placing the book search
	 * 
	 * @param event action of current event
	 */
	@FXML
	void onSearch(ActionEvent event) {
		QueryBuilder qbObj = QueryBuilder.getInstance();
		String searchQeury = qbObj.searchQuery(tfBookName.getText(), tfAuthorName.getText(), tfBookGenre.getText(),
				tfTextFree.getText());

		ViewStarter.client.handleMessageFromClientUI(new Message(OperationType.SearchBook, searchQeury));
	}

	/**
	 * onSavePdfBt save the search result in PDF file.
	 * 
	 * @param event action of current event
	 */
	@FXML
	void onSavePdfBtn(ActionEvent event) throws IOException {
		File file = PDFGenerator.getInstance().createPdf(PDF_PATH, "Search Book Result", this.books);
		try {
			Desktop.getDesktop().open(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * onGetSearchResult display the results
	 * 
	 * @param books is the list view of books
	 */

	public void onGetSearchResult(List<Book> books) {
		this.books = books;
		try {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					lvBooks.getItems().clear();
					lvBooks.setCellFactory(ViewStarter.client.utilsControllers.new SearchBookRowFactory());
					lvBooks.getItems().addAll(books);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * getLvBooks is getting the list view of the search results
	 * 
	 * @return list view of books
	 */
	public ListView<Book> getLvBooks() {
		return lvBooks;
	}

};
