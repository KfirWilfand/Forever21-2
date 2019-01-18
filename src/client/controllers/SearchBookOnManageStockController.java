package client.controllers;

import java.io.IOException;
import java.util.List;

import client.ViewStarter;
import common.controllers.Message;
import common.controllers.enums.OperationType;
import common.entity.Book;
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
import javafx.scene.control.cell.PropertyValueFactory;

public class SearchBookOnManageStockController {

    @FXML
    private TextField tfCatalogNumberForSearch;

    @FXML
    private Button btnSearchBook;
    
    @FXML
    private TableColumn<Book, Integer> tvColumnCatalogNumber;

    @FXML
    private TableColumn<Book, String> tvColumnBookName;

    @FXML
    private TableColumn<Book, Integer> tvColumnCopiesNumber;

    @FXML
    private TableColumn<Book, String> tvColumnEditionNumber;

    @FXML
    private TableView<Book> booksTable;
    
    public void initialize() 
    {
		ViewStarter.client.searchBookOnManageStockControllerObj = this;
		tvColumnCatalogNumber.setCellValueFactory(new PropertyValueFactory<Book,Integer>("catalogNum"));
    	tvColumnBookName.setCellValueFactory(new PropertyValueFactory<Book,String>("bookName"));
    	tvColumnCopiesNumber.setCellValueFactory(new PropertyValueFactory<Book,Integer>("copiesNum"));
    	tvColumnEditionNumber.setCellValueFactory(new PropertyValueFactory<Book,String>("edition"));
	}

    @FXML
    void onClickSearchBook(ActionEvent event) {
       	String searchBookQuery="SELECT * FROM obl.books WHERE bCatalogNum='"+tfCatalogNumberForSearch.getText()+"'" ;
    	ViewStarter.client.handleMessageFromClientUI(new Message(OperationType.SearchBookOnManageStock, searchBookQuery));
    }
    
    public void showBookResult(Book book)
    {
    	ObservableList<Book> BookToAdd = FXCollections.observableArrayList(book);
      	booksTable.setItems(BookToAdd);
      	System.out.println(book.getBookName());
    }

}
