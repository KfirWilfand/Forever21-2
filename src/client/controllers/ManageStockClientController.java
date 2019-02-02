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
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

/**
 * The ManageStockClientController class represent the manage stock client controller
 * @author  Kfir Wilfand
 * @author Bar Korkos
 * @author Zehavit Otmazgin
 * @author Noam Drori
 * @author Sapir Hochma
 */
public class ManageStockClientController {
	
	/** tvCopies is a copies table view */
    @FXML
    private TableView<Copy> tvCopies;

    /** tvColumsCopyID is a table column of copy */
	@FXML
    private TableColumn<Copy, String> tvColumsCopyID;

	/** btnAddNewBook is the add new book button */
    @FXML
    private Button btnAddNewBook;

    /** InnerPaneInManageStock is the inner manage stock pane */
	@FXML
    private Pane InnerPaneInManageStock;
   
	/** tfEnterNewCopyID is a enter copyid text */
    @FXML
    private TextField tfEnterNewCopyID;

    /** btnAddNewCopy is a add new copy button */
    @FXML
    private Button btnAddNewCopy;
    
    /** btnDeleteCopy is delete cope button */
    @FXML
    private Button btnDeleteCopy;
    
    @FXML
    private Label label2;


	@FXML
    private Label label1;
	
    public Label getLabel2() {
		return label2;
	}

	public Label getLabel1() {
		return label1;
	}

	
	
    /**
   	 * getTfEnterNewCopyID is entering a new copy id
   	 * @return enter new copy id text
   	 */
	public TextField getTfEnterNewCopyID() {
		return tfEnterNewCopyID;
	}

	 /**
   	 * getBtnAddNewCopy is getting the add new copy button
   	 * @return a add new copy button 
   	 */
	public Button getBtnAddNewCopy() {
		return btnAddNewCopy;
	}
	 /**
   	 * setBtnAddNewCopy is setting the add new copy button
   	 * @param btnAddNewCopy button 
   	 */
	public void setBtnAddNewCopy(Button btnAddNewCopy) {
		this.btnAddNewCopy = btnAddNewCopy;
	}
	 /**
   	 * getBtnDeleteCopy is getting the delete copy button
   	 * return delete copy button
   	 */
	public Button getBtnDeleteCopy() {
		return btnDeleteCopy;
	}
	 /**
   	 * setBtnDeleteCopy is setting the delete copy button
   	 * @param btnDeleteCopy 
   	 */
	public void setBtnDeleteCopy(Button btnDeleteCopy) {
		this.btnDeleteCopy = btnDeleteCopy;
	}
	
	 /**
   	 * initialize manage stock
   	 */
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

	 /**
   	 * onClickAddNewBook adding a new book to the inventory
   	 * @param event action event
   	 * @exception IOException 
   	 */
    @FXML
    void onClickAddNewBook(ActionEvent event) {
    	Platform.runLater(new Runnable() {
			@Override
			public void run() {
				try {
					tvCopies.setVisible(false);
					btnAddNewCopy.setVisible(false);
					btnDeleteCopy.setVisible(false);
					tfEnterNewCopyID.setVisible(false);
					btnAddNewBook.setVisible(false);
					getLabel1().setVisible(false);
					getLabel2().setVisible(false);
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
    /**
   	 * getInnerPaneInManageStock is getting the inner pane in manage stock
   	 * return inner pane
   	 */
    public Pane getInnerPaneInManageStock() {
		return InnerPaneInManageStock;
	}
   
    /**
   	 * displayCopies display all the copies
   	 * @param list of copies
   	 */
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
    
    /**
   	 * onClickAddCopy adding a new copy to DB
   	 * @param event action event
   	 */
    @FXML
    void onClickAddCopy(ActionEvent event) 
    {
    	tfEnterNewCopyID.setStyle(null);
    	if(tfEnterNewCopyID.getText().isEmpty())
    		{	tfEnterNewCopyID.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;-fx-border-radius: 5px;");
    			return;
    		}
    	Book selectedBook=ViewStarter.client.searchBookOnManageStockControllerObj.getBooksTable().getSelectionModel().getSelectedItem();
    	if(selectedBook==null)
    		{Utils utils = new Utils(ViewStarter.client.mainViewController);
    		utils.showAlertWithHeaderText(AlertType.ERROR, "", "No book selected! please select book");
    		return;
    		}
    	
    	Object[] queryArr=new Object[4];
    	
    	int copiesNumOfSelectedBook=selectedBook.getCopiesNum();
    	int avilableCopiesNumOfSelectedBook=selectedBook.getAvilableCopiesNum();
    	int catlogNumOfSelectedBook=selectedBook.getCatalogNum();
    	for (Copy cp: tvCopies.getItems())
    		if (cp.getCopyID().equals(tfEnterNewCopyID.getText()))
    				{
    					Utils utils = new Utils(ViewStarter.client.mainViewController);
    					utils.showAlertWithHeaderText(AlertType.ERROR, "", "Copy already exists!");
    					return;
    				}
    	queryArr[0]="INSERT INTO obl.copeis (copyID,bCatalogNum, isAvilable) Values('"+tfEnterNewCopyID.getText()+"',"+catlogNumOfSelectedBook+",1);";
    	queryArr[1]="UPDATE obl.books SET bCopiesNum="+(copiesNumOfSelectedBook+1)+",bAvilableCopiesNum="+(avilableCopiesNumOfSelectedBook+1)+" WHERE bCatalogNum="+catlogNumOfSelectedBook+";";
    	queryArr[2]= new Copy(tfEnterNewCopyID.getText(),catlogNumOfSelectedBook,true);
    	queryArr[3]= "select * from obl.copeis where copyID='"+tfEnterNewCopyID.getText()+"'";
    	ViewStarter.client.handleMessageFromClientUI(new Message(OperationType.AddNewCopy, queryArr));
    	
    }
    
    /**
   	 * getTvCopies is display the table view of the copies 
   	 * @return table view of all copies
   	 */
    public TableView<Copy> getTvCopies() {
		return tvCopies;
	}

    /**
   	 * addCopieToList is adding a new copy to list
   	 * @param copy contains the details of the chosen copy 
   	 */
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
   
    /**
   	 * removeCopiefromList is removing copies from list
   	 */
    public void removeCopiefromList()
    {
    	
    	Platform.runLater(new Runnable() {
			@Override
			public void run() {
		    	
		    		Copy selectedItem = tvCopies.getSelectionModel().getSelectedItem();
		    		tvCopies.getItems().remove(selectedItem);
		    	
			}

		});
    }
    
    /**
   	 * onClickDeleteCopy is deleting a copy of a book
   	 * @param event action event
   	 */
    @FXML
    void onClickDeleteCopy(ActionEvent event)
    {
    	Copy selectedCopy=tvCopies.getSelectionModel().getSelectedItem();
    	if(selectedCopy==null)
    	{
    		Utils utils = new Utils(ViewStarter.client.mainViewController);
    		utils.showAlertWithHeaderText(AlertType.ERROR, "", "No copy selected! please select copy");
    		return;	
    	}
    	String copyIDofSelected=selectedCopy.getCopyID();
    	Book selectedBook=ViewStarter.client.searchBookOnManageStockControllerObj.getBooksTable().getSelectionModel().getSelectedItem();
     	if(selectedBook==null)
    	{
    		Utils utils = new Utils(ViewStarter.client.mainViewController);
    		utils.showAlertWithHeaderText(AlertType.ERROR, "", "No book selected! please select book");
    		return;	
    	}
    	int catlogNumOfSelectedBook=selectedCopy.getbCatalogNum();
      	int copiesNumOfSelectedBook=selectedBook.getCopiesNum();
    	int avilableCopiesNumOfSelectedBook=selectedBook.getAvilableCopiesNum();
    	String[] copyArr= new String[2];
    	copyArr[0]="Delete from obl.copeis where copyID='"+copyIDofSelected+"';";
    	copyArr[1]="UPDATE obl.books SET bCopiesNum="+(copiesNumOfSelectedBook-1)+",bAvilableCopiesNum="+(avilableCopiesNumOfSelectedBook-1)+" WHERE bCatalogNum="+catlogNumOfSelectedBook+";";
    	ViewStarter.client.handleMessageFromClientUI(new Message(OperationType.DeleteCopy, copyArr));

    }
    
    /**
   	 * onChosenRow is chosing row by the mouse
   	 * @param event get a mouse event
   	 */
    @FXML
    void onChosenRow(MouseEvent event) {
		if(event.getClickCount() == 1)
		{
			if(tvCopies.getSelectionModel().getSelectedItem() != null)
			{
				btnDeleteCopy.setDisable(false);
			}
		}

    }

    /**
   	 * getBtnAddNewBook is return the add new book button
   	 * @return add new book button 
   	 */
    public Button getBtnAddNewBook() {
		return btnAddNewBook;
	}



}
