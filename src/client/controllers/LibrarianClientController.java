package client.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;

import client.ViewStarter;
import client.controllers.adapters.AlertController;
import common.controllers.Message;
import common.controllers.enums.OperationType;
import common.entity.HistoryItem;
import common.entity.Subscriber;
import common.entity.User;
import common.entity.enums.SubscriberHistoryType;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import common.entity.BorrowBook;
import common.entity.BorrowCopy;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.converter.LocalDateStringConverter;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

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
	private TextField tfBorrowCopyID;

	@FXML
	private Text txtBorrowBookNotice;

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
	private TextField ssTfFirstName;

	@FXML
	private TextField ssTfLastName;

	@FXML
	private TextField ssTfPhone;

	@FXML
	private TextField ssTfUserName;

	@FXML
	private TextField ssTfPassword;

	@FXML
	private TextField ssTfEmail;

	@FXML
	private Label sslblStatus;

	@FXML
	private CheckBox ssCxbHoldSubscriber;

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
	private Label sslblLateReturn;

	@FXML
	private DatePicker ssPdGraduation;

	@FXML
	private Button ssbtnUpdate;

	@FXML
	private AnchorPane ancPaneManageStock;

	@FXML
	private Tab btnManageStockTab;

	static AlertController alert = new AlertController();

	@FXML
	void onBtnUpdate(ActionEvent event) {
		String updateUserDetailsQuery = " UPDATE `obl`.`users`" + " SET `usrName` = '" + ssTfUserName.getText()
		+ "', `usrPassword` = '" + ssTfPassword.getText() + "', `usrFirstName` = '" + ssTfFirstName.getText()
		+ "', `usrLastName` = '" + ssTfLastName.getText() + "', `usrEmail` = '" + ssTfEmail.getText()
		+ "' WHERE (`usrId` = " + tfSearchSubscriberNumber.getText() + ");";

		String updateSubscriberQuery = " UPDATE `obl`.`subscribers`" + " SET `subPhoneNum` = '" + ssTfPhone.getText();

		if (!ssCxbHoldSubscriber.isDisable()) {
			if (ssCxbHoldSubscriber.isSelected())
				updateSubscriberQuery = updateSubscriberQuery + "', `subStatus` = 'Hold";
			else
				updateSubscriberQuery = updateSubscriberQuery + "', `subStatus` = 'Active";
		}

		updateSubscriberQuery = updateSubscriberQuery + "', `subGraduationDate` = '" + ssPdGraduation.getValue()
		+ "' WHERE (`subNum` = " + tfSearchSubscriberNumber.getText() + ");";

		try {
			String[] params = new String[3];

			params[0] = tfSearchSubscriberNumber.getText();
			params[1] = updateUserDetailsQuery;
			params[2] = updateSubscriberQuery;

			ViewStarter.client.sendToServer(new Message(OperationType.EditDetailsByLibrarian, params));
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	@FXML
	public void initialize() {
		ViewStarter.client.librarianClientControllerObj = this;
		try {
			Parent newPane = FXMLLoader.load(getClass().getResource("/client/boundery/layouts/manageStock.fxml"));
			if (ancPaneManageStock != null)
				ancPaneManageStock.getChildren().setAll(newPane);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	void onBorrowBookBtn(ActionEvent event) {
		LocalDate borrowDate = LocalDate.now();
		Date date = Date.valueOf(borrowDate);
		if (tfBorrowBookSubscriberNumber.getText().isEmpty()|| tfBorrowCopyID.getText().isEmpty())// if the librarian missed a field
			alert.error("Subscriber or Copy id fields are missing!", "");
		else {
			BorrowCopy borrowCopy = new BorrowCopy(tfBorrowCopyID.getText(),
					Integer.parseInt(tfBorrowBookSubscriberNumber.getText()), date, null);
			ViewStarter.client.handleMessageFromClientUI(new Message(OperationType.BorrowBookByLibrarian, borrowCopy));
		}
	}

	@FXML
	void onCreateSubscruberBtn(ActionEvent event) {// adding a new subscriber to the DB
		Utils utils = new Utils(ViewStarter.client.mainViewController);
		if ((tfSubscriberFirstName.getText().isEmpty() == true || tfSubscriberLastName.getText().isEmpty() == true
				|| tfSubscriberUsrName.getText().isEmpty() == true || tfSubscriberPassword.getText().isEmpty() == true
				|| tfSubscruberPhone.getText().isEmpty() == true || tfSubscriberEmail.getText().isEmpty() == true)) {
			utils.showAlertWithHeaderText(AlertType.ERROR, "Error Dialog", "Please fill all required fields!");
		} else {
			String createNewSubscriberQueryUserTable = "INSERT INTO obl.users (usrName, usrPassword,usrFirstName, usrLastName,usrEmail) VALUES ('"
					+ tfSubscriberUsrName.getText() + "', '" + tfSubscriberPassword.getText() + "', '"
					+ tfSubscriberFirstName.getText() + "','" + tfSubscriberLastName.getText() + "','"
					+ tfSubscriberEmail.getText() + "'); ";
			String createNewSubscriberQuerySubscriberTable = "INSERT INTO obl.subscribers (subNum, subPhoneNum) VALUES (LAST_INSERT_ID(), '"
					+ tfSubscruberPhone.getText() + "');";
			String checkEmailAndPhoneQuery = "SELECT b.subNum, a.usrName, a.usrPassword, a.usrFirstName, a.usrLastName, a.usrEmail, b.subPhoneNum, a.usrType, b.subStatus FROM obl.users as a right join obl.subscribers as b on a.usrId=b.subNum WHERE a.usrEmail='"
					+ tfSubscriberEmail.getText() + "' or b.subPhoneNum='" + tfSubscruberPhone.getText()
					+ "' or usrName='" + tfSubscriberUsrName.getText() + "';";
			String[] queryArr = new String[3];
			queryArr[0] = createNewSubscriberQueryUserTable;
			queryArr[1] = createNewSubscriberQuerySubscriberTable;
			queryArr[2] = checkEmailAndPhoneQuery;

			ViewStarter.client
			.handleMessageFromClientUI(new Message(OperationType.AddNewSubscriberByLibrarian, queryArr)); // sending
			// to
			// LibrarianController
			// in
			// the
			// server
		}
	}




	@FXML
	void onReturnBookBtn(ActionEvent event)
	{
		LocalDate actualReturnDate = LocalDate.now();
		Date date = Date.valueOf(actualReturnDate);
		if(tfReturnBookCatalogNumber.getText().isEmpty())//if the librarian forgot to insert copyId
			alert.error("CopyID is missing!", "");
		else {
			BorrowCopy borrowCopy = new BorrowCopy(tfReturnBookCatalogNumber.getText(),date);
			ViewStarter.client.handleMessageFromClientUI(new Message(OperationType.ReturnBookByLibrarian, borrowCopy));
		}
	}

	@FXML
	void onSearchSubscriberBtn(ActionEvent event) {
		String searchSubscriberUsrId = tfSearchSubscriberNumber.getText();
		ViewStarter.client
		.handleMessageFromClientUI(new Message(OperationType.SearchSubscriber, searchSubscriberUsrId));
	}

	public void updateDetailsOnBorrow(Object[] objects) {
		BorrowCopy bCopy = (BorrowCopy) objects[0];
		Boolean isPopular = (Boolean) objects[1];
		tfBorrowBookBorrowDate.setValue(bCopy.getBorrowDate().toLocalDate());
		tfBorrowBookEndBorrowDate.setValue(bCopy.getReturnDueDate().toLocalDate());
		System.out.println(tfBorrowBookEndBorrowDate.getValue());
		if (isPopular) {
			txtBorrowBookNotice.setVisible(true);
		}
	}

	public void updateSearchSubscriberUI(Subscriber subscriber) {

		Platform.runLater(new Runnable() {
			@Override
			public void run() {

				switch (subscriber.getReaderCard().getStatus()) {
				case Hold:
					sslblStatus.setText("Hold");
					sslblStatus.setTextFill(Color.web("#FFBE0B"));
					ssCxbHoldSubscriber.setSelected(true);
					ssCxbHoldSubscriber.setDisable(false);

					break;
				case Active:
					sslblStatus.setText("Active");
					sslblStatus.setTextFill(Color.web("#7FFF00"));
					ssCxbHoldSubscriber.setDisable(false);
					ssCxbHoldSubscriber.setSelected(false);
					break;
				case Lock:
					sslblStatus.setText("Lock");
					sslblStatus.setTextFill(Color.web("#CE0E0E"));
					ssCxbHoldSubscriber.setDisable(true);
					ssCxbHoldSubscriber.setSelected(false);
					break;
				}

				ssTfFirstName.setText(subscriber.getFirstName());
				ssTfLastName.setText(subscriber.getLastName());
				ssTfPhone.setText(subscriber.getPhoneNum());
				ssTfUserName.setText(subscriber.getUsrName());
				ssTfEmail.setText(subscriber.getEmail());
				ssTfPassword.setText(subscriber.getPassword());
				ssPdGraduation.setValue(subscriber.getGraduationDate().toLocalDate());
				sslblLateReturn.setText(subscriber.getReaderCard().getLateReturnsBookCounter().toString());

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
}
