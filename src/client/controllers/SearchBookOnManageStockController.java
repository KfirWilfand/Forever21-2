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
import javafx.scene.input.MouseEvent;

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
    

	public TableView<Book> getBooksTable() {
		return booksTable;
	}

	public void initialize() 
    {
		ViewStarter.client.searchBookOnManageStockControllerObj = this;
		tvColumnCatalogNumber.setCellValueFactory(new PropertyValueFactory<Book,Integer>("catalogNum"));
    	tvColumnBookName.setCellValueFactory(new PropertyValueFactory<Book,String>("bookName"));
    	tvColumnCopiesNumber.setCellValueFactory(new PropertyValueFactory<Book,Integer>("copiesNum"));
    	tvColumnEditionNumber.setCellValueFactory(new PropertyValueFactory<Book,String>("edition"));
    	
    	if (booksTable != null)
    	{	
    		String getAllBooksQuery = "Select * from obl.books";
    		ViewStarter.client.handleMessageFromClientUI(new Message(OperationType.SearchBookOnManageStock, getAllBooksQuery));
    	
    	
//	    	booksTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> 
//	    	{
//	    		System.out.println(oldSelection.toString());
//	    		System.out.println(newSelection.toString());
//	    		Button b1=ViewStarter.client.manageStockClientControllerObj.getBtnAddNewCopy();
//				b1.setDisable(false);
//				Button b2=ViewStarter.client.manageStockClientControllerObj.getBtnDeleteCopy();
//				b2.setDisable(false);
//				TextField t=ViewStarter.client.manageStockClientControllerObj.getTfEnterNewCopyID();
//				t.setDisable(false);
//				
//	    	});
//	    	
	    	
    	}
    	   	
	}

    @FXML
    void onClickSearchBook(ActionEvent event) {
       	String searchBookQuery="SELECT * FROM obl.books WHERE bCatalogNum='"+tfCatalogNumberForSearch.getText()+"'" ;
    	ViewStarter.client.handleMessageFromClientUI(new Message(OperationType.SearchBookOnManageStock, searchBookQuery));
    }
    
    public void showBookResult(List<Book> list)
    {
    	ObservableList<Book> BookToAdd = FXCollections.observableArrayList();
    	for(Book book: list)
    		BookToAdd.add(book);
    	booksTable.setItems(BookToAdd);      
    }
    

    @FXML
    void onChosenRow(MouseEvent event) {
    	if(event.getClickCount() == 1)
    	{

    		ViewStarter.client.manageStockClientControllerObj.getTvCopies().getItems().clear();
    		Book book = booksTable.getSelectionModel().getSelectedItem();
    		String query = "SELECT * FROM obl.copeis where bCatalogNum = " + book.getCatalogNum();
    		ViewStarter.client.handleMessageFromClientUI(new Message(OperationType.GetCopiesOfSelectedBook,query));
    		ViewStarter.client.manageStockClientControllerObj.getTfEnterNewCopyID().textProperty().addListener((observable, oldValue, newValue) -> {
    			Button b1=ViewStarter.client.manageStockClientControllerObj.getBtnAddNewCopy();
    			b1.setDisable(false);
       		});
   		
			Button b2=ViewStarter.client.manageStockClientControllerObj.getBtnDeleteCopy();
			b2.setDisable(false);
			TextField t=ViewStarter.client.manageStockClientControllerObj.getTfEnterNewCopyID();
			t.setDisable(false);
    	}
    	if(event.getClickCount() == 2) 
    	{
        	try 
        	{
    			Parent newPane = FXMLLoader.load(getClass().getResource("/client/boundery/layouts/updateOrAddBook.fxml"));
    			if(ViewStarter.client.manageStockClientControllerObj.getInnerPaneInManageStock() != null)
    			{
    				ViewStarter.client.manageStockClientControllerObj.getInnerPaneInManageStock().getChildren().setAll(newPane);
    				Button b=ViewStarter.client.updateOrAddBookControllerObj.getBtnAddBook();
					b.setVisible(false);
					b=ViewStarter.client.updateOrAddBookControllerObj.getBtnUpdate();
					b.setVisible(true);
					Book book = booksTable.getSelectionModel().getSelectedItem();
					ViewStarter.client.updateOrAddBookControllerObj.showSelectedBookDetails(book);
					
    			}		
    			
        	}
    		 catch (IOException e) 
        	{
    			e.printStackTrace();
    		}
    	}

    }
    

}
