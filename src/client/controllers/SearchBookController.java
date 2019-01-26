package client.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import client.ViewStarter;
import client.controllers.Utils.BorrowBookRowFactory;
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
import javafx.scene.layout.AnchorPane;

public class SearchBookController {

	@FXML
	private TextField tfBookName;

	@FXML
	private TextField tfAuthorName;

	@FXML
	private TextField tfBookGenre;

	@FXML
	private TextField tfTextFree;

	@FXML
	private ListView<Book> lvBooks;


	@FXML
	private AnchorPane apListView;

	@FXML
	private Button btnSearch;

	public void initialize() {
		ViewStarter.client.searchBookControllerObj = this;
		ViewStarter.client.utilsControllers.setApListView(apListView);
	}

	@FXML
	void onSearch(ActionEvent event) {
		QueryBuilder qbObj = QueryBuilder.getInstance();
		String searchQeury = qbObj.searchQuery(tfBookName.getText(), tfAuthorName.getText(), tfBookGenre.getText(),
				tfTextFree.getText());
		
		System.out.println(searchQeury);
		ViewStarter.client.handleMessageFromClientUI(new Message(OperationType.SearchBook, searchQeury));
	}

	
	public void onGetSearchResult(List<Book> books) {
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
	
	public ListView<Book> getLvBooks() {
		return lvBooks;
	}


};
