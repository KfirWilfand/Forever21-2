package client.controllers;

import java.io.IOException;

import client.ViewStarter;
import common.controllers.Message;
import common.controllers.enums.OperationType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class LibrarianClientController {

    @FXML
    private TextField tfSubscriberFirstName;

    @FXML
    private TextField tfSubscriberLastName;

    @FXML
    private TextField tfSubscriberUsrName;

    @FXML
    private TextField tfSubscruberPhone;

    @FXML
    private TextField tfSubscriberEmail;

    @FXML
    private TextField tfSubscriberPassword;

    @FXML
    private Button btnCreateSubscrciber;

    
    @FXML
    private Button btnBorrowBook;

    @FXML
    private TextField tfBorrowBookSubscriberNumber;

    @FXML
    private DatePicker tfBorrowBookBorrowDate;

    @FXML
    private DatePicker tfBorrowBookEndBorrowDate;

    @FXML
    private TextField tfBorrowBookCatalogNumber;

    @FXML
    private Button btnReturnBook;

    @FXML
    private TextField tfReturnBookSubscriberNumber;

    @FXML
    private DatePicker tfReturnBookBorrowDate;

    @FXML
    private DatePicker tfReturnBookEndBorrowDate;

    @FXML
    private DatePicker tfReturnBookReturningDate;

    @FXML
    private TextField tfReturnBookCatalogNumber;

    @FXML
    private TextField tfSearchSubscriberNumber;

    @FXML
    private Button btnSearchSubscriber;

    @FXML
    private AnchorPane ancPaneManageStock;
    
    @FXML
	public void initialize() {
		ViewStarter.client.librarianClientControllerObj = this;
		try {
			
			Parent newPane=FXMLLoader.load(getClass().getResource("/client/boundery/layouts/manageStock.fxml"));
			if (ancPaneManageStock != null)
				ancPaneManageStock.getChildren().setAll(newPane);
		} catch (IOException e  ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
    @FXML
    void onBorrowBookBtn(ActionEvent event) {

    }

    @FXML
    void onCreateSubscruberBtn(ActionEvent event) {//adding a new subscriber to the DB
    	Utils utils=new Utils(ViewStarter.client.mainViewController);
    	if ((tfSubscriberFirstName.getText().isEmpty() == true ||tfSubscriberLastName.getText().isEmpty() == true  || tfSubscriberUsrName.getText().isEmpty() == true  || tfSubscriberPassword.getText().isEmpty() == true  ||tfSubscruberPhone.getText().isEmpty() == true ||tfSubscriberEmail.getText().isEmpty() == true ))
    	{
			utils.showAlertWithHeaderText(AlertType.ERROR, "Error Dialog", "Please fill all required fields!");
    	}
    	else {
    	String createNewSubscriberQueryUserTable="INSERT INTO obl.users (usrName, usrPassword,usrFirstName, usrLastName,usrEmail) VALUES ('"+ tfSubscriberUsrName.getText() + "', '"+tfSubscriberPassword.getText() + "', '"+ tfSubscriberFirstName.getText()+ "','"+ tfSubscriberLastName.getText() + "','"+ tfSubscriberEmail.getText()+ "'); ";
       	String createNewSubscriberQuerySubscriberTable=	"INSERT INTO obl.subscribers (subNum, subPhoneNum) VALUES (LAST_INSERT_ID(), '" +tfSubscruberPhone.getText()+"');";
    	String checkEmailAndPhoneQuery="SELECT b.subNum, a.usrName, a.usrPassword, a.usrFirstName, a.usrLastName, a.usrEmail, b.subPhoneNum, a.usrType, b.subStatus FROM obl.users as a right join obl.subscribers as b on a.usrId=b.subNum WHERE a.usrEmail='"+tfSubscriberEmail.getText()+"' or b.subPhoneNum='"+tfSubscruberPhone.getText()+"' or usrName='"+tfSubscriberUsrName.getText()+"';";
    	String[] queryArr=new String[3];
    	queryArr[0]=createNewSubscriberQueryUserTable;
    	queryArr[1]=createNewSubscriberQuerySubscriberTable;
    	queryArr[2]=checkEmailAndPhoneQuery;
   
    	ViewStarter.client.handleMessageFromClientUI(new Message(OperationType.AddNewSubscriberByLibrarian, queryArr)); //sending to LibrarianController in the server
    	}
    }

    @FXML
    void onReturnBookBtn(ActionEvent event) {

    }

    @FXML
    void onSearchSubscriberBtn(ActionEvent event) {
    	String searchSubscriberQuery="SELECT b.subNum, a.usrName, a.usrPassword, a.usrFirstName, a.usrLastName, a.usrEmail, b.subPhoneNum, a.usrType, b.subStatus FROM obl.users as a right join obl.subsribers as b on a.usrId=b.subNum WHERE b.subNum = "+tfSearchSubscriberNumber.getText();
		ViewStarter.client.handleMessageFromClientUI(new Message(OperationType.SearchSubscriber, searchSubscriberQuery));
    }
    

}
