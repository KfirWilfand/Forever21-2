package client.controllers;

import java.io.IOException;
import java.util.List;

import client.ViewStarter;
import common.entity.Book;
import common.entity.Copy;
import common.entity.Librarian;
import common.entity.LibraryManager;
import common.entity.Subscriber;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

public class ManageStockClientController {

    @FXML
    private TableView<Copy> tvCopies;


	@FXML
    private TableColumn<Copy, String> tvColumsCopyID;

    @FXML
    private Button btnAddNewBook;

    @FXML
    private Pane InnerPaneInManageStock;
    
    @FXML
    private TextField tfNewCopyID;


	@FXML
	public void initialize() {
		ViewStarter.client.manageStockClientControllerObj = this;
		
		tvColumsCopyID.setCellValueFactory(new PropertyValueFactory<Copy,String>("copyID"));
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				try {
					
					Parent newPane=FXMLLoader.load(getClass().getResource("/client/boundery/layouts/searchBook_by_number_at_manageStock.fxml"));
					if (InnerPaneInManageStock != null)
						InnerPaneInManageStock.getChildren().setAll(newPane);
				} catch (IOException e  ) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			}
		});
	
	}
    
    @FXML
    void onClickAddNewBook(ActionEvent event) {
    	Platform.runLater(new Runnable() {
			@Override
			public void run() {
				try {
					btnAddNewBook.setVisible(false);
					Parent newPane=FXMLLoader.load(getClass().getResource("/client/boundery/layouts/updateOrAddBook.fxml"));
					if (InnerPaneInManageStock != null)
						InnerPaneInManageStock.getChildren().setAll(newPane);
					Button b=ViewStarter.client.updateOrAddBookControllerObj.getBtnAddBook();
					b.setVisible(true);
					b=ViewStarter.client.updateOrAddBookControllerObj.getBtnAddCopy();
					b.setVisible(false);
					b=ViewStarter.client.updateOrAddBookControllerObj.getBtnUpdate();
					b.setVisible(false);
					
				} catch (IOException e  ) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			}
		});
	

    }
    
    public Pane getInnerPaneInManageStock() {
		return InnerPaneInManageStock;
	}
    
    @FXML
    void onAddNewCopie(ActionEvent event) {
    	
    }
    
    public void displayCopies(List<Copy> list)
    {
    	if(!list.isEmpty()) {
    		ObservableList<Copy> items =FXCollections.observableArrayList ();
    	
    		for(Copy copy: list)
    		{
    			items.add(copy);
    		}
    		tvCopies.setItems(items); 
    	}
    }
    
    public TableView<Copy> getTvCopies() {
		return tvCopies;
	}



}
