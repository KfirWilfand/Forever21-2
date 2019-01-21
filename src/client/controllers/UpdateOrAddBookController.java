package client.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import client.ViewStarter;
import common.controllers.Message;
import common.controllers.enums.OperationType;
import common.entity.Book;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class UpdateOrAddBookController {

    @FXML
    private TextField tfCatalogNumber;

    @FXML
    private TextField tfBookName;

    @FXML
    private TextField tfAuthorName;

    @FXML
    private TextField tfEditionNumber;

    @FXML
    private TextField tfLocationOnShelf;

    @FXML
    private TextArea txteDescription;
    
    @FXML
    private TextField tfGenre;

    @FXML
    private DatePicker dpPrintingDate;

    @FXML
    private DatePicker dpPurchaseDate;

    @FXML
    private TextField tfCopiesNumber;

    @FXML
    private CheckBox cbIsPopular;

    @FXML
    private Button btnUpdate;

   	@FXML
    private Button btnUploadTableOfContent;

    @FXML
    private Button btnAddBook;


	@FXML
    private Button btnBack;


  

	@FXML
	public void initialize() {
		ViewStarter.client.updateOrAddBookControllerObj = this;
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
//				if (btnAddCopy != null)
//					btnAddCopy.setVisible(false);
//				if (btnAddBook != null)
//					btnAddBook.setVisible(true);
//				if (btnUpdate != null)
//					btnUpdate.setVisible(false);
			
			}
		});
	
	}
    

    @FXML
    void onClickAddBook(ActionEvent event) {
    	ViewStarter.client.manageStockClientControllerObj.getTvCopies().getItems().clear();
      	
    	String addBookQuery = "insert  into obl.books (bName,bDescription,bEdition,bPrintDate,bCopiesNum,bShelfLocation,bGenre,bAuthor,bPurchaseDate,bAvilableCopiesNum,bIsPopular) "
    			+ "values ('"+ tfBookName.getText() +"','"+ txteDescription.getText() +"','"+ tfEditionNumber.getText() +"','"+ dpPrintingDate.getValue()+"'"
    					+ ",0,'"+tfLocationOnShelf.getText()+"','"+tfGenre.getText()+"','"+tfAuthorName.getText()+"'"
    							+ ",'"+dpPurchaseDate.getValue()+"',0,"+cbIsPopular.isSelected()+")";
    	System.out.println(addBookQuery);
    	
    	ViewStarter.client.handleMessageFromClientUI(new Message(OperationType.AddNewBook, addBookQuery));

    }



    @FXML
    void onClickBack(ActionEvent event)
    {
    	try 
    	{
    		ViewStarter.client.manageStockClientControllerObj.getTvCopies().getItems().clear();
			Parent newPane = FXMLLoader.load(getClass().getResource("/client/boundery/layouts/searchBook_by_number_at_manageStock.fxml"));
			if(ViewStarter.client.manageStockClientControllerObj.getInnerPaneInManageStock() != null)
			{
				ViewStarter.client.manageStockClientControllerObj.getInnerPaneInManageStock().getChildren().setAll(newPane);
			}
    	}
		 catch (IOException e) 
    	{
			e.printStackTrace();
		}
    }

    @FXML
    void onClickUpdate(ActionEvent event) 
    {
    	String query = "UPDATE obl.books SET bName='"+tfBookName.getText()+"',bAuthor='"+tfAuthorName.getText()+"',bGenre='"+tfGenre.getText()+"',bIsPopular="+cbIsPopular.isSelected()+",bEdition='"+tfEditionNumber.getText()+"',bPrintDate='"+dpPrintingDate.getValue()+"',bDescription='"+txteDescription.getText()+"',bPurchaseDate='"+dpPurchaseDate.getValue()+"',bShelfLocation='"+tfLocationOnShelf.getText()+"' WHERE bCatalogNum="+tfCatalogNumber.getText()+";";
    	
    	
    	System.out.println(query);
    	ViewStarter.client.handleMessageFromClientUI(new Message(OperationType.UpdateBookDetails,query ));
    	

    }
    
    public void showSelectedBookDetails(Book book)
    {
    	Platform.runLater(new Runnable() 
    	{
		@Override
		public void run() 
			{
				tfCatalogNumber.setText(String.valueOf(book.getCatalogNum()));
		    	tfBookName.setText(book.getBookName());
		    	tfAuthorName.setText(String.join(", ",book.getAuthor()));
		    	tfGenre.setText(String.join(", ",book.getGenre()));
		    	cbIsPopular.setSelected(book.isPopular());
		    	tfEditionNumber.setText(book.getEdition());
		    	dpPrintingDate.setValue(book.getPrintDate().toLocalDate());
		       	dpPurchaseDate.setValue(book.getPurchaseDate().toLocalDate());
		       	txteDescription.setText(book.getDescription());
		       	tfLocationOnShelf.setText(book.getShelfLocation());
		    	tfCopiesNumber.setText(String.valueOf(book.getCopiesNum()));
			}
    	});
    }
    
    public Button getBtnAddBook() {
		return btnAddBook;
	}
    public Button getBtnUpdate() {
		return btnUpdate;
	}




}
