package client.controllers;

import client.ViewStarter;
import common.controllers.Message;
import common.controllers.enums.OperationType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class LibrarianClientController {

    @FXML
    private TextField tfSubscriberFirstName;

    @FXML
    private TextField tfSubscriberLastName;

    @FXML
    private TextField tfSubscriberGender;

    @FXML
    private TextField tfSubscriberBirthDay;

    @FXML
    private TextField tfSubscriberAge;

    @FXML
    private TextField tfSubscriberStreet;

    @FXML
    private TextField tfSubscriberCity;

    @FXML
    private TextField tfSubscriberNumber;

    @FXML
    private TextField tfSubscriberUsrName;

    @FXML
    private TextField tfSubscruberPhone;

    @FXML
    private TextField tfSubscriberEmail;

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
    void onBorrowBookBtn(ActionEvent event) {

    }

    @FXML
    void onCreateSubscruberBtn(ActionEvent event) {

    }

    @FXML
    void onReturnBookBtn(ActionEvent event) {

    }

    @FXML
    void onSearchSubscriberBtn(ActionEvent event) {
    	String searchSubscriberQuery="SELECT b.subNum, a.usrName, a.usrPassword, a.usrFirstName, a.usrLastName, a.usrEmail, b.subPhoneNum, a.usrType, b.subStatus FROM obl.user as a right join obl.subsriber as b on a.usrId=b.subNum WHERE b.subNum = "+tfSearchSubscriberNumber.getText();
		ViewStarter.client.handleMessageFromClientUI(new Message(OperationType.SearchSubscriber, searchSubscriberQuery));
    }
    

}
