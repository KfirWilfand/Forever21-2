package client.controllers;

import java.io.IOException;
import java.util.List;

import client.ViewStarter;
import common.controllers.Message;
import common.controllers.enums.OperationType;
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
import javafx.scene.input.MouseEvent;
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
    private TextField tfEnterNewCopyID;

    @FXML
    private Button btnAddNewCopy;
    

    @FXML
    private Button btnDeleteCopy;


	public TextField getTfEnterNewCopyID() {
		return tfEnterNewCopyID;
	}

	public void setTfEnterNewCopyID(TextField tfEnterNewCopyID) {
		this.tfEnterNewCopyID = tfEnterNewCopyID;
	}

	public Button getBtnAddNewCopy() {
		return btnAddNewCopy;
	}

	public void setBtnAddNewCopy(Button btnAddNewCopy) {
		this.btnAddNewCopy = btnAddNewCopy;
	}

	public Button getBtnDeleteCopy() {
		return btnDeleteCopy;
	}

	public void setBtnDeleteCopy(Button btnDeleteCopy) {
		this.btnDeleteCopy = btnDeleteCopy;
	}

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
    
    @FXML
    void onClickAddCopy(ActionEvent event) 
    {
    	Object[] queryArr=new Object[3];
    	int catlogNumOfSelectedBook=ViewStarter.client.searchBookOnManageStockControllerObj.getBooksTable().getSelectionModel().getSelectedItem().getCatalogNum();
    	int copiesNumOfSelectedBook=ViewStarter.client.searchBookOnManageStockControllerObj.getBooksTable().getSelectionModel().getSelectedItem().getCopiesNum();
    	int avilableCopiesNumOfSelectedBook=ViewStarter.client.searchBookOnManageStockControllerObj.getBooksTable().getSelectionModel().getSelectedItem().getAvilableCopiesNum();
    	queryArr[0]="INSERT INTO obl.copeis (copyID,bCatalogNum, isAvilable) Values('"+tfEnterNewCopyID.getText()+"',"+catlogNumOfSelectedBook+",1);";
    	queryArr[1]="UPDATE obl.books SET bCopiesNum="+(copiesNumOfSelectedBook+1)+",bAvilableCopiesNum="+(avilableCopiesNumOfSelectedBook+1)+" WHERE bCatalogNum="+catlogNumOfSelectedBook+";";
    	queryArr[2]= new Copy(tfEnterNewCopyID.getText(),catlogNumOfSelectedBook,true);
    	ViewStarter.client.handleMessageFromClientUI(new Message(OperationType.AddNewCopy, queryArr));

    }
    
    public TableView<Copy> getTvCopies() {
		return tvCopies;
	}

    public void addCopieToList(Copy copy)
    {
    	
    	Platform.runLater(new Runnable() {
			@Override
			public void run() {
		    	if(copy!=null) {
		    		tvCopies.getItems().add(copy);
		    	}
			}

		});
    }
    
    @FXML
    void onClickDeleteCopy(ActionEvent event)
    {
    	String copyIDofSelected=tvCopies.getSelectionModel().getSelectedItem().getCopyID();
    	int catlogNumOfSelectedBook=tvCopies.getSelectionModel().getSelectedItem().getbCatalogNum();
      	int copiesNumOfSelectedBook=ViewStarter.client.searchBookOnManageStockControllerObj.getBooksTable().getSelectionModel().getSelectedItem().getCopiesNum();
    	int avilableCopiesNumOfSelectedBook=ViewStarter.client.searchBookOnManageStockControllerObj.getBooksTable().getSelectionModel().getSelectedItem().getAvilableCopiesNum();
    	String[] copyArr= new String[2];
    	copyArr[0]="Delete from obl.copeis where copyID='"+copyIDofSelected+"';";
    	copyArr[1]="UPDATE obl.books SET bCopiesNum="+(copiesNumOfSelectedBook-1)+",bAvilableCopiesNum="+(avilableCopiesNumOfSelectedBook-1)+" WHERE bCatalogNum="+catlogNumOfSelectedBook+";";
    	ViewStarter.client.handleMessageFromClientUI(new Message(OperationType.DeleteCopy, copyArr));

    }
    
    @FXML
    void onChosenRow(MouseEvent event) {

    }
    
   
 


}
