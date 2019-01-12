package client.controllers;

import java.util.List;

import client.ViewStarter;
import client.controllers.adapters.QueryBuilder;
import common.controllers.Message;
import common.controllers.enums.OperationType;
import common.entity.Book;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

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
    private ListView<?> lvBooks;

    @FXML
    private Button btnSearch;
	
    public void initialize() {
		ViewStarter.client.searchBookControllerObj = this;
	}

    @FXML
    void onSearch(ActionEvent event) {
    	QueryBuilder qbObj=QueryBuilder.getInstance();
    	String searchQeury=qbObj.searchQuery(tfBookName.getText(), tfAuthorName.getText(), tfBookGenre.getText(), tfTextFree.getText());
    	ViewStarter.client.handleMessageFromClientUI(new Message(OperationType.SearchBook, searchQeury));
    }
    
    public void onGetSearchResult(List<Book> books) {
    	
    }

};

