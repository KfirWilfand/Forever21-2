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

    @FXML
    void onEditSaveBtn(ActionEvent event) {
    	

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
