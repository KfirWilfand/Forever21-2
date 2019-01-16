package client.controllers;

import client.ViewStarter;
import common.controllers.Message;
import common.controllers.enums.OperationType;
import common.entity.Subscriber;
import common.entity.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class SubscriberClientController {

    @FXML
    private TextField tfEditFirstName;

    @FXML
    private TextField tfEditLastName;

    @FXML
    private TextField tfEditSubscriberNumber;

    @FXML
    private TextField tfEditUsrName;

    @FXML
    private TextField tfEditPhone;

    @FXML
    private TextField tfEditEmail;

    @FXML
    private ComboBox cmbEditStatus;

    @FXML
    private Button btnEditSave;

    @FXML
    private ListView<?> listViewBooksList;

    @FXML
    private ListView<?> listViewHistoryActivity;
    
    @FXML
    public void initialize() {
    	//initialize status combo box
    	ObservableList<String> options = FXCollections.observableArrayList("Lock","Hold","Active");
    	cmbEditStatus.getItems().addAll(options);
    	ViewStarter.client.subscriberClientControllerObj=this;
	}

    @FXML//This query update existed subscriber's details (only phone number and email)
    void onEditSaveBtn(ActionEvent event) {
		Utils utils=new Utils(ViewStarter.client.mainViewController);
		String checkPhoneValid=tfEditPhone.getText();
		String checkEmailValid=tfEditEmail.getText();
		Boolean[] check = new Boolean[2];
		check[0]=utils.isValidEmail(checkEmailValid);
		check[1]=utils.validatePhoneNumber(checkPhoneValid);
		if(!check[0]) 
		{
			utils.showAlertWithHeaderText(AlertType.ERROR, "Error Dialog", "Mail is not valid!");
			return;
		}
		if(!check[1])
		{
			utils.showAlertWithHeaderText(AlertType.ERROR, "Error Dialog", "Phone number is not valid!");
			return;
		}
    	String editSubscriberDetailsQuery= "UPDATE obl.subsribers SET subPhoneNum='"+ tfEditPhone.getText() +"' WHERE subNum="+tfEditSubscriberNumber.getText() +";UPDATE obl.users SET usrEmail='"+tfEditEmail.getText() +"' WHERE usrID="+tfEditSubscriberNumber.getText() +";";	
    	ViewStarter.client.handleMessageFromClientUI(new Message(OperationType.EditDetailsBySubscriber, editSubscriberDetailsQuery));
    }
    

    
   public void initializeDetailsAtLogin(Subscriber subscriber) {
	   tfEditFirstName.setText(subscriber.getFirstName());
	   tfEditLastName.setText(subscriber.getLastName());	
	   tfEditSubscriberNumber.setText((subscriber.getSubscriberNum()).toString());
	   tfEditUsrName.setText(subscriber.getUsrName());
	   tfEditPhone.setText(subscriber.getPhoneNum());
	   tfEditEmail.setText(subscriber.getEmail());
	   cmbEditStatus.setValue(subscriber.getStatus());   
   }
  

}
