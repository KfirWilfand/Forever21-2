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

public class SubscriberClientController {

    @FXML
    private TextField tfEditFirstName;

    @FXML
    private TextField tfEditLastName;

    @FXML
    private TextField tfEditGender;

    @FXML
    private TextField tfEditBirthDat;

    @FXML
    private TextField tfEditAge;

    @FXML
    private TextField tfEditStreet;

    @FXML
    private TextField tfEditCity;

    @FXML
    private TextField tfEditSubscriberNumber;

    @FXML
    private TextField tfEditUsrName;

    @FXML
    private TextField tfEditPhone;

    @FXML
    private TextField tfEditEmail;

    @FXML
    private ComboBox<ReaderCardStatus> cmbEditStatus;

    @FXML
    private Button btnEditSave;

    @FXML
    private ListView<?> listViewBooksList;

    @FXML
    private ListView<?> listViewHistoryActivity;

    @FXML
    private ListView<HistoryItem> ssLVBookRequest;

    @FXML
    private ListView<HistoryItem> ssLVBookApprove;
  

    @FXML
    private ListView<HistoryItem> ssLVBookReturn;

    @FXML
    private ListView<HistoryItem> ssLVEditProfile;

    @FXML
    private ListView<HistoryItem> ssLVChangeStatus;

    @FXML
    private ListView<BorrowCopy> lvMyBorrowedBooks;
    
    @FXML
    private TabPane tabvSubsciber;
    
    @FXML
    private Tab tabReaderCardHistory;
    
    @FXML
    private Tab btnBorrowedBooksTab;

	private Subscriber mSubscriber;
	
    @FXML
    private Text txtEmailError;

    @FXML
    private Text txtPhoneError;

	@FXML
	public void initialize() {
		// initialize status combo box
		ObservableList<ReaderCardStatus> options = FXCollections.observableArrayList(ReaderCardStatus.getEnums());
		cmbEditStatus.getItems().addAll(options);
		ViewStarter.client.subscriberClientControllerObj = this;
	}
	
	
	

    @FXML
    void onBorrowedBooksTab(Event event) 
    {
    
    	User usr=ViewStarter.client.mainViewController.getUser();
    	String query="SELECT c.bName ,a.copyID, a.subNum ,a.borrowDate, a.returnDueDate FROM obl.borrows as a left join obl.copeis as b on a.copyID=b.copyID left join obl.books as c on b.bCatalogNum=c.bCatalogNum where a.subNum= '"+usr.getId()+"' and a.actualReturnDate is null";
    	System.out.println(query);
    	ViewStarter.client.handleMessageFromClientUI(new Message(OperationType.ShowMyBorrowedBooks, query));
    }
	
	
	

	@FXML // This query update existed subscriber's details (only phone number and email)
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
				+ tfEditEmail.getText() + "' WHERE usrID=" + tfEditSubscriberNumber.getText() + ";";
				ViewStarter.client.handleMessageFromClientUI(
				new Message(OperationType.EditDetailsBySubscriber, editSubscriberDetailsQuery));
		}
	}

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
	 * This method invoke when ReaderCard History Tab Click. 
	 * @author kfir3
	 */
//    @FXML
//    void onSubsciberReaderCardHistoryTabClick(ActionEvent event) {
//		System.out.println(mSubscriber);
////		if (mSubscriber == null) {
////			System.out.println("kfir");
////			ViewStarter.client.alertClientControllerObj.error("Can't find Subscriber", "");
////			return;
////		}
////		
////		updateSubscriberHistoryUi(mSubscriber);
//	}

	/**
	 * This method update Subscriber History Ui 
	 * @param Subscriber
	 * @author kfir3
	 */
	public void updateSubscriberHistoryUi(Subscriber subscriber) {
System.out.println(subscriber);
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
