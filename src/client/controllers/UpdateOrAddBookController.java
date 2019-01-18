package client.controllers;

import java.io.IOException;

import client.ViewStarter;
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
    private Button btnAddCopy;
    
  

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

    }

    @FXML
    void onClickAddCopy(ActionEvent event) {

    }

    @FXML
    void onClickBack(ActionEvent event)
    {
    	try 
    	{
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
    void onClickUpdate(ActionEvent event) {

    }
    public Button getBtnAddBook() {
		return btnAddBook;
	}
    public Button getBtnUpdate() {
		return btnUpdate;
	}
    public Button getBtnAddCopy() {
  		return btnAddCopy;
  	}



}
