package client.controllers;

import java.util.List;
import java.util.Map;

import client.ViewStarter;
import client.controllers.Utils.SearchBookRowFactory;
import common.controllers.Message;
import common.controllers.enums.OperationType;
import common.entity.Book;
import common.entity.BorrowCopy;
import common.entity.HistoryItem;
import common.entity.Subscriber;
import common.entity.User;
import common.entity.enums.ReaderCardStatus;
import common.entity.enums.SubscriberHistoryType;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

/**
 * The SubscriberClientController class represent the subscriber controller on the client's side
 * @author  Kfir Wilfand
 * @author Bar Korkos
 * @author Zehavit Otmazgin
 * @author Noam Drori
 * @author Sapir Hochma
 */
public class SubscriberClientController {

	/** tfEditFirstName is the edit subscriber first name */
    @FXML
    private TextField tfEditFirstName;

    /** tfEditLastName is the edit subscriber last name */
    @FXML
    private TextField tfEditLastName;

    /** tfEditSubscriberNumber is the edit subscriber number edit */
    @FXML
    private TextField tfEditSubscriberNumber;

    /** tfEditUsrName is the edit username text */
    @FXML
    private TextField tfEditUsrName;

    /** tfEditPhone is the edit phone text */
    @FXML
    private TextField tfEditPhone;

    /** tfEditEmail is the edit email text */
    @FXML
    private TextField tfEditEmail;

    /** cmbEditStatus is thereader card status combo box */
    @FXML
    private ComboBox<ReaderCardStatus> cmbEditStatus;

    /** btnEditSave is the save edit button */
    @FXML
    private Button btnEditSave;

    /** listViewBooksList is the book list history list view */
    @FXML
    private ListView<?> listViewBooksList;

    /** listViewHistoryActivity is the activity history list view */
    @FXML
    private ListView<?> listViewHistoryActivity;
    
    /** ssLVBookRequest is the book request list view history*/
    @FXML
    private ListView<HistoryItem> ssLVBookRequest;

    /** ssLVBookApprove is the list view of approved books history */
    @FXML
    private ListView<HistoryItem> ssLVBookApprove;
  

    /** ssLVBookReturn is the list view of return book history */
    @FXML
    private ListView<HistoryItem> ssLVBookReturn;

    /** ssLVEditProfile is a list view of edit profile history */
    @FXML
    private ListView<HistoryItem> ssLVEditProfile;

    /** ssLVChangeStatus is the list view of change status history */
    @FXML
    private ListView<HistoryItem> ssLVChangeStatus;

    @FXML
    private ListView<BorrowCopy> lvMyBorrowedBooks;
    
    /** tabvSubsciber is the tab pance of subscriber */
    @FXML
    private TabPane tabvSubsciber;
    
    /** tabReaderCardHistory is the tab of reader card history */
    @FXML
    private Tab tabReaderCardHistory;
    

    @FXML
    private Tab btnBorrowedBooksTab;


    /** mSubscriber is object of Subscriber*/

	private Subscriber mSubscriber;
	
	/** txtEmailError is the email error text */
    @FXML
    private Text txtEmailError;

    /** txtPhoneError is the text phone error */
    @FXML
    private Text txtPhoneError;

    /**
   	 * initialize the subscriber status combo box
   	 */
	@FXML
	public void initialize() {
		// initialize status combo box
		ObservableList<ReaderCardStatus> options = FXCollections.observableArrayList(ReaderCardStatus.getEnums());
		cmbEditStatus.getItems().addAll(options);
		ViewStarter.client.subscriberClientControllerObj = this;
	}

	
	
	/**
   	 * onBorrowedBooksTab will present the books that the subscriber order but still do not return
   	 * @param event action event
   	 */
    @FXML
    public void onBorrowedBooksTab(Event event) 
    {
    
    	User usr=ViewStarter.client.mainViewController.getUser();
    	String query="SELECT c.bName ,a.copyID, a.subNum ,a.borrowDate, a.returnDueDate, c.bCatalogNum,c.bIsPopular FROM obl.borrows as a left join obl.copeis as b on a.copyID=b.copyID left join obl.books as c on b.bCatalogNum=c.bCatalogNum where a.subNum= '"+usr.getId()+"' and a.actualReturnDate is null";
    	System.out.println(query);
    	ViewStarter.client.handleMessageFromClientUI(new Message(OperationType.ShowMyBorrowedBooks, query));
    }
	
	
	


	/**
   	 * onEditSaveBtn update existed subscriber's details (only phone number and email)
   	 * @param event action event
   	 */
	@FXML 
	void onEditSaveBtn(ActionEvent event) {
		tfEditEmail.setStyle(null);
		tfEditPhone.setStyle(null);
		txtEmailError.setVisible(false);
		txtPhoneError.setVisible(false);
		Utils utils = new Utils(ViewStarter.client.mainViewController);
		String checkPhoneValid = tfEditPhone.getText();
		String checkEmailValid = tfEditEmail.getText();
		
		Boolean[] check = new Boolean[2];
		check[0] = utils.isValidEmail(checkEmailValid);
		check[1] = utils.validatePhoneNumber(checkPhoneValid);
		if (!check[0]) {
			//utils.showAlertWithHeaderText(AlertType.ERROR, "Error Dialog", "Mail is not valid!");
			txtEmailError.setVisible(true);
			tfEditEmail.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;-fx-border-radius: 5px;");
		}
		if (!check[1]) {
			txtPhoneError.setVisible(true);
			//utils.showAlertWithHeaderText(AlertType.ERROR, "Error Dialog", "Phone number is not valid!");
			tfEditPhone.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;-fx-border-radius: 5px;");
		}
		if(check[0] && check[1])
		{	String editSubscriberDetailsQuery = "UPDATE obl.subscribers SET subPhoneNum='" + tfEditPhone.getText()
				+ "' WHERE subNum=" + tfEditSubscriberNumber.getText() + ";UPDATE obl.users SET usrEmail='"
				+ tfEditEmail.getText() + "' WHERE usrID=" + tfEditSubscriberNumber.getText() + ";"+tfEditSubscriberNumber.getText();
				ViewStarter.client.handleMessageFromClientUI(
				new Message(OperationType.EditDetailsBySubscriber, editSubscriberDetailsQuery));
		}
	}

	/**
   	 * initializeDetailsAtLogin initialize details at login
   	 * @param subscriber contains the detailes of the logged subscriber
   	 */
	public void initializeDetailsAtLogin(Subscriber subscriber) {
		mSubscriber = subscriber;
		tfEditFirstName.setText(subscriber.getFirstName());
		tfEditLastName.setText(subscriber.getLastName());
		tfEditSubscriberNumber.setText((subscriber.getSubscriberNum()).toString());
		tfEditUsrName.setText(subscriber.getUsrName());
		tfEditPhone.setText(subscriber.getPhoneNum());
		tfEditEmail.setText(subscriber.getEmail());
		cmbEditStatus.setValue(subscriber.getReaderCard().getStatus());
		
		tabvSubsciber.getSelectionModel().selectedItemProperty().addListener(
			    new ChangeListener<Tab>() {
			        @Override
			        public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab t1) {
			           if(t1.equals(tabReaderCardHistory)) {
			        	   updateSubscriberHistoryUi(mSubscriber);
			           }
			        }
			    }
			);
	}

	/**
	 * updateSubscriberHistoryUi update Subscriber History Ui 
	 * @param subscriber contains details about the logged subscriber
	 */
	public void updateSubscriberHistoryUi(Subscriber subscriber) {

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				ObservableList<HistoryItem> items;
				Map<SubscriberHistoryType, List<HistoryItem>> history = subscriber.getReaderCard().getHistory();

				items = FXCollections.observableArrayList(history.get(SubscriberHistoryType.BooksRequest));
				ssLVBookRequest.setItems(items);

				items = FXCollections.observableArrayList(history.get(SubscriberHistoryType.BooksApprove));
				ssLVBookApprove.setItems(items);

				items = FXCollections.observableArrayList(history.get(SubscriberHistoryType.BooksReturn));
				ssLVBookReturn.setItems(items);

				items = FXCollections.observableArrayList(history.get(SubscriberHistoryType.EditProfile));
				ssLVEditProfile.setItems(items);

				items = FXCollections.observableArrayList(history.get(SubscriberHistoryType.ChangeStatus));
				ssLVChangeStatus.setItems(items);
			}
		});
	}
	
	/**
	 * onGetBorrowedBooksResult update the table view of the borrowed books - enter to every row the book
	 * @param BorrowedBooks 
	 */
	public void onGetBorrowedBooksResult(List<BorrowCopy> BorrowedBooks) {
		try {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					lvMyBorrowedBooks.getItems().clear();
					lvMyBorrowedBooks.setCellFactory(ViewStarter.client.utilsControllers.new BorrowBookRowFactory());
					lvMyBorrowedBooks.getItems().addAll(BorrowedBooks);
					
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
