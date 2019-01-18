package client.controllers;

import java.io.IOException;

import client.ViewStarter;
import common.entity.Librarian;
import common.entity.LibraryManager;
import common.entity.Subscriber;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;

public class ManageStockClientController {

    @FXML
    private ListView<?> lvCopiesList;

    @FXML
    private Button btnAddNewBook;

    @FXML
    private Pane InnerPaneInManageStock;



	@FXML
	public void initialize() {
		ViewStarter.client.manageStockClientControllerObj = this;
		
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
    


}
