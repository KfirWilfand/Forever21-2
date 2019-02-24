package client.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.sql.Date;
import client.ViewStarter;
import client.controllers.adapters.AlertController;
import common.controllers.Message;
import common.controllers.enums.OperationType;
import common.entity.HistoryItem;
import common.entity.Subscriber;
import common.entity.enums.SubscriberHistoryType;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import common.entity.BorrowCopy;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.text.Text;

/**
 * The LibrarianClientController class represent librarian client controller
 * 
 * @author Kfir Wilfand
 * @author Bar Korkos
 * @author Zehavit Otmazgin
 * @author Noam Drori
 * @author Sapir Hochma
 */
public class LibrarianClientController {
	/** tfSubscriberFirstName is the subscriber first name */
	@FXML
	private TextField tfSubscriberFirstName;

	/** tfSubscriberLastName is the subscriber last name */
	@FXML
	private TextField tfSubscriberLastName;

	/** tfSubscriberUsrName is the subscriber username */
	@FXML
	private TextField tfSubscriberUsrName;

	/** tfSubscruberPhone is the subscriber phone */
	@FXML
	private TextField tfSubscruberPhone;

	/** tfSubscriberEmail is the subscriber email */
	@FXML
	private TextField tfSubscriberEmail;

	/** tfSubscriberPassword is the password of the subscriber */
	@FXML
	private TextField tfSubscriberPassword;

	/** btnCreateSubscrciber is the create subscriber button */
	@FXML
	private Button btnCreateSubscrciber;

	/** btnBorrowBook is the borrow book button */
	@FXML
	private Button btnBorrowBook;

	/** tfBorrowBookSubscriberNumber is the subscriber's phone number */
	@FXML
	private TextField tfBorrowBookSubscriberNumber;

	/** tfBorrowBookBorrowDate is the borrow date */
	@FXML
	private DatePicker tfBorrowBookBorrowDate;

	/** tfBorrowBookEndBorrowDate is the end of borrow date */
	@FXML
	private DatePicker tfBorrowBookEndBorrowDate;

	/** tfBorrowBookCatalogNumber is the book catalog number */
	@FXML
	private TextField tfBorrowBookCatalogNumber;

	/** tfBorrowCopyID is the copyID of the borrowed book */
	@FXML
	private TextField tfBorrowCopyID;

	/** txtBorrowBookNotice is a notice while borrowing a book */
	@FXML
	private Text txtBorrowBookNotice;

	/** btnReturnBook is the returned button */
	@FXML
	private Button btnReturnBook;

	/**
	 * tfReturnBookSubscriberNumber is the number of the subscriber that returnes a
	 * copy
	 */
	@FXML
	private TextField tfReturnBookSubscriberNumber;

	/** tfReturnBookBorrowDate the date of return of the book */
	@FXML
	private DatePicker tfReturnBookBorrowDate;

	/** tfReturnBookEndBorrowDate is the date of borrow */
	@FXML
	private DatePicker tfReturnBookEndBorrowDate;

	/** tfReturnBookReturningDate is the returning date of the book */
	@FXML
	private DatePicker tfReturnBookReturningDate;

	/** tfReturnBookCatalogNumber is the book catalog number */
	@FXML
	private TextField tfReturnBookCatalogNumber;

	/** tfSearchSubscriberNumber is search subscriber by number */
	@FXML
	private TextField tfSearchSubscriberNumber;

	/** btnSearchSubscriber is the search subscriber button */
	@FXML
	private Button btnSearchSubscriber;

	/** ssTfFirstName is suscriber's name */
	@FXML
	private TextField ssTfFirstName;

	/** ssTfLastName is suscriber's last name */
	@FXML
	private TextField ssTfLastName;

	/** ssTfPhone is is suscriber's phone number */
	@FXML
	private TextField ssTfPhone;

	/** ssTfUserName is suscriber's username */
	@FXML
	private TextField ssTfUserName;

	/** ssTfPassword is subscriber's password */
	@FXML
	private TextField ssTfPassword;

	/** ssTfEmail is subscriber mail */
	@FXML
	private TextField ssTfEmail;

	/** sslblStatus is the label of subscriber's status */
	@FXML
	private Label sslblStatus;

	/** ssCxbHoldSubscriber is a combo box of hold subscriber */
	@FXML
	private CheckBox ssCxbHoldSubscriber;

	/** ssLVBookRequest is a list view of book request on the subscriber history */
	@FXML
	private ListView<HistoryItem> ssLVBookRequest;

	/**
	 * ssLVBookApprove is a list view of approved books on the subscriber history
	 */
	@FXML
	private ListView<HistoryItem> ssLVBookApprove;

	/** ssLVBookReturn is a list view on the subscriber history */
	@FXML
	private ListView<HistoryItem> ssLVBookReturn;

	/** ssLVEditProfile is the edit profile history list view */
	@FXML
	private ListView<HistoryItem> ssLVEditProfile;

	/** ssLVChangeStatus is the list view of change status */
	@FXML
	private ListView<HistoryItem> ssLVChangeStatus;

	/** sslblLateReturn is late return label */
	@FXML
	private Label sslblLateReturn;

	/** ssPdGraduation is a date picker */
	@FXML
	private DatePicker ssPdGraduation;

	/** ssbtnUpdate is the update subscriber button */
	@FXML
	private Button ssbtnUpdate;

	/** ancPaneManageStock is the manage stock anchor pane */
	@FXML
	private AnchorPane ancPaneManageStock;

	/** txtAddNewSubscriberEmailError is an email error text */
	@FXML
	private Text txtAddNewSubscriberEmailError;

	/**
	 * txtAddNewSubscriberPhoneError is a text that says the phone number is
	 * problematic
	 */
	@FXML
	private Text txtAddNewSubscriberPhoneError;

	/** btnManageStockTab is the manage stock tab */
	@FXML
	private TabPane tvSubsciber;

	@FXML
	private Tab btnManageStockTab;

	/** btnExtensionBook is the extension book button */
	@FXML
	private Button btnExtensionBook;

	/**
	 * dpGraduationDateNewSub is a date picker that contains subscriber graduation
	 * date
	 */
	@FXML
	private DatePicker dpGraduationDateNewSub;

	@FXML
	private Tab btnStatisticTab;

	@FXML
	private AnchorPane ancStatistic;

	private boolean mIsLibrarianManager;

	static AlertController alert = new AlertController();

	/**
	 * initialize the librarian controller
	 */
	@FXML
	public void initialize() {
		ViewStarter.client.librarianClientControllerObj = this;
	}

	/**
	 * initialize the librarian UI at login
	 */
	public void initializeDetailsAtLogin() {

		try {
			Parent newPane = FXMLLoader.load(getClass().getResource("/client/boundery/layouts/manageStock.fxml"));
			if (ancPaneManageStock != null)
				ancPaneManageStock.getChildren().setAll(newPane);

			if (mIsLibrarianManager) {
				btnStatisticTab.setDisable(false);
				ssCxbHoldSubscriber.setDisable(false);
				FXMLLoader statisticPaneloadder = new FXMLLoader(
						getClass().getResource("/client/boundery/layouts/statistic_libraian.fxml"));
				Parent statisticPane = statisticPaneloadder.load();
				if (ancStatistic != null) {
					ancStatistic.getChildren().setAll(statisticPane);
					statisticPaneloadder.<StatisticController>getController().initializeDetailsOnStatisticClick();

				}
			} else {
				btnStatisticTab.setDisable(true);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * updateReturnUI is updating the gui of the librarian on return procedure
	 * 
	 * @param borrowCopy is the return borrowed cope
	 */
	public void updateReturnUI(BorrowCopy borrowCopy) {
		tfReturnBookSubscriberNumber.setText(String.valueOf(borrowCopy.getSubNum()));
		tfReturnBookBorrowDate.setValue(borrowCopy.getBorrowDate().toLocalDate());
		tfReturnBookEndBorrowDate.setValue(borrowCopy.getReturnDueDate().toLocalDate());
		tfReturnBookReturningDate.setValue(borrowCopy.getActualReturnDate().toLocalDate());
	}

	/**
	 * onBtnUpdate is updating the subscriber details
	 * 
	 * @param event
	 * @exception IOException
	 */
	@FXML
	void onBtnUpdate(ActionEvent event) {

		Utils utils = new Utils(ViewStarter.client.mainViewController);
		List<Control> tflist = new ArrayList<Control>(Arrays.asList(ssTfUserName, ssTfPassword, ssTfFirstName,
				ssTfLastName, ssTfEmail, ssPdGraduation, ssTfPhone));
		utils.setStyleToList(tflist, null);

		boolean isEmailCorrect = utils.isValidEmail(ssTfEmail.getText());
		boolean isPhoneCorrect = utils.validatePhoneNumber(ssTfPhone.getText());

		tflist.clear();
		if (ssTfUserName.getText().isEmpty())
			tflist.add(ssTfUserName);
		if (ssTfPassword.getText().isEmpty())
			tflist.add(ssTfPassword);
		if (ssTfFirstName.getText().isEmpty())
			tflist.add(ssTfFirstName);
		if (ssTfLastName.getText().isEmpty())
			tflist.add(ssTfLastName);
		if (!isEmailCorrect)
			tflist.add(ssTfEmail);
		if (!isPhoneCorrect)
			tflist.add(ssTfPhone);
		if (ssPdGraduation.getValue() == null)
			tflist.add(ssPdGraduation);

		utils.setStyleToList(tflist, "-fx-border-color: red ; -fx-border-width: 2px ;-fx-border-radius: 5px;");

		if (!ssTfUserName.getText().isEmpty() && !ssTfPassword.getText().isEmpty() && !ssTfFirstName.getText().isEmpty()
				&& !ssTfLastName.getText().isEmpty() && isEmailCorrect && isPhoneCorrect
				&& ssPdGraduation.getValue() != null) {
			String updateUserDetailsQuery = " UPDATE `obl`.`users`" + " SET `usrName` = '" + ssTfUserName.getText()
					+ "', `usrPassword` = '" + ssTfPassword.getText() + "', `usrFirstName` = '"
					+ ssTfFirstName.getText() + "', `usrLastName` = '" + ssTfLastName.getText() + "', `usrEmail` = '"
					+ ssTfEmail.getText() + "' WHERE (`usrId` = " + tfSearchSubscriberNumber.getText() + ");";

			String updateSubscriberQuery = " UPDATE `obl`.`subscribers`" + " SET `subPhoneNum` = '"
					+ ssTfPhone.getText();

			if (!ssCxbHoldSubscriber.isDisable()) {
				if (ssCxbHoldSubscriber.isSelected())
					updateSubscriberQuery = updateSubscriberQuery + "', `subStatus` = 'Lock";
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
	}

	/**
	 * onBorrowBookBtn is the borrow book by librarian procedure event an action
	 * event
	 */

	@FXML
	void onBorrowBookBtn(ActionEvent event) {
		tfBorrowBookBorrowDate.setValue(null);
		tfBorrowBookEndBorrowDate.setValue(null);
		txtBorrowBookNotice.setVisible(false);

		tfBorrowCopyID.setStyle(null);
		tfBorrowBookSubscriberNumber.setStyle(null);

		if (tfBorrowCopyID.getText().isEmpty())
			tfBorrowCopyID.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;-fx-border-radius: 5px;");
		if (tfBorrowBookSubscriberNumber.getText().isEmpty())
			tfBorrowBookSubscriberNumber
					.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;-fx-border-radius: 5px;");

		if (!tfBorrowCopyID.getText().isEmpty() && !tfBorrowBookSubscriberNumber.getText().isEmpty()) {
			LocalDate borrowDate = LocalDate.now();
			Date date = Date.valueOf(borrowDate);
			BorrowCopy borrowCopy = new BorrowCopy(tfBorrowCopyID.getText(),
					Integer.parseInt(tfBorrowBookSubscriberNumber.getText()), date, null);
			ViewStarter.client.handleMessageFromClientUI(new Message(OperationType.BorrowBookByLibrarian, borrowCopy));
		}
	}

	/**
	 * onCreateSubscruberBtn creates a new subscriber event an action event
	 */
	@FXML
	void onCreateSubscruberBtn(ActionEvent event) {// adding a new subscriber to the DB
		txtAddNewSubscriberEmailError.setVisible(false);
		txtAddNewSubscriberPhoneError.setVisible(false);

		Utils utils = new Utils(ViewStarter.client.mainViewController);
		List<Control> tflist = new ArrayList<Control>(
				Arrays.asList(tfSubscriberFirstName, tfSubscriberLastName, tfSubscriberUsrName, tfSubscriberPassword,
						tfSubscruberPhone, tfSubscriberEmail, dpGraduationDateNewSub));
		utils.setStyleToList(tflist, null);

		boolean isPhoneCorrect = utils.validatePhoneNumber(tfSubscruberPhone.getText());
		boolean isEmailCoorect = utils.isValidEmail(tfSubscriberEmail.getText());

		tflist.clear();

		if (tfSubscriberFirstName.getText().isEmpty())
			tflist.add(tfSubscriberFirstName);
		if (tfSubscriberLastName.getText().isEmpty())
			tflist.add(tfSubscriberLastName);
		if (tfSubscriberUsrName.getText().isEmpty())
			tflist.add(tfSubscriberUsrName);
		if (tfSubscriberPassword.getText().isEmpty())
			tflist.add(tfSubscriberPassword);
		if (!isPhoneCorrect) {
			tflist.add(tfSubscruberPhone);
			txtAddNewSubscriberPhoneError.setVisible(true);
		}
		if (!isEmailCoorect) {
			tflist.add(tfSubscriberEmail);
			txtAddNewSubscriberEmailError.setVisible(true);
		}
		if (dpGraduationDateNewSub.getValue() == null)
			tflist.add(dpGraduationDateNewSub);

		utils.setStyleToList(tflist, "-fx-border-color: red ; -fx-border-width: 2px ;-fx-border-radius: 5px;");

		if (dpGraduationDateNewSub.getValue() != null && isEmailCoorect && isPhoneCorrect
				&& !tfSubscriberPassword.getText().isEmpty() && !tfSubscriberUsrName.getText().isEmpty()
				&& !tfSubscriberLastName.getText().isEmpty() && !tfSubscriberFirstName.getText().isEmpty()) {
			String createNewSubscriberQueryUserTable = "INSERT INTO obl.users (usrName, usrPassword,usrFirstName, usrLastName,usrEmail,usrType) VALUES ('"
					+ tfSubscriberUsrName.getText() + "', '" + tfSubscriberPassword.getText() + "', '"
					+ tfSubscriberFirstName.getText() + "','" + tfSubscriberLastName.getText() + "','"
					+ tfSubscriberEmail.getText() + "', 'Subscriber'); ";
			String createNewSubscriberQuerySubscriberTable = "INSERT INTO obl.subscribers (subNum, subPhoneNum, subGraduationDate) VALUES (LAST_INSERT_ID(), '"
					+ tfSubscruberPhone.getText() + "', '" + Date.valueOf(dpGraduationDateNewSub.getValue()) + "');";
			String checkEmailAndPhoneQuery = "SELECT b.subNum, a.usrName, a.usrPassword, a.usrFirstName, a.usrLastName, a.usrEmail, b.subPhoneNum, a.usrType, b.subStatus FROM obl.users as a right join obl.subscribers as b on a.usrId=b.subNum WHERE a.usrEmail='"
					+ tfSubscriberEmail.getText() + "' or b.subPhoneNum='" + tfSubscruberPhone.getText()
					+ "' or usrName='" + tfSubscriberUsrName.getText() + "';";
			String[] queryArr = new String[3];
			queryArr[0] = createNewSubscriberQueryUserTable;
			queryArr[1] = createNewSubscriberQuerySubscriberTable;
			queryArr[2] = checkEmailAndPhoneQuery;

			ViewStarter.client
					.handleMessageFromClientUI(new Message(OperationType.AddNewSubscriberByLibrarian, queryArr));
		}
	}

	/**
	 * onReturnBookBtn is the returning book by librarian procedure event an action
	 * event
	 */
	@FXML
	void onReturnBookBtn(ActionEvent event) {

		tfReturnBookSubscriberNumber.clear();
		tfReturnBookBorrowDate.setValue(null);
		tfReturnBookEndBorrowDate.setValue(null);
		tfReturnBookReturningDate.setValue(null);
		tfReturnBookCatalogNumber.setStyle(null);

		if (tfReturnBookCatalogNumber.getText().isEmpty())
			tfReturnBookCatalogNumber
					.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;-fx-border-radius: 5px;");
		else {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Confirmation");
			alert.setHeaderText("Are you sure you want to return this book?");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				LocalDate actualReturnDate = LocalDate.now();
				Date date = Date.valueOf(actualReturnDate);
				BorrowCopy borrowCopy = new BorrowCopy(tfReturnBookCatalogNumber.getText(), date);
				ViewStarter.client.handleMessageFromClientUI(new Message(OperationType.ReturnBookByLibrarian, borrowCopy));
			}
		}
		
	}

	/**
	 * onSearchSubscriberBtn is searching a subscriber by librarian procedure event
	 * an action event
	 */
	@FXML
	void onSearchSubscriberBtn(ActionEvent event) {

		cleanAndDisableSearchSubscriberFields();

		String searchSubscriberUsrId = tfSearchSubscriberNumber.getText();
		if (!searchSubscriberUsrId.isEmpty()) {
			ssTfFirstName.setDisable(false);
			ssTfLastName.setDisable(false);
			ssTfPhone.setDisable(false);
			ssTfUserName.setDisable(false);
			// ssTfPassword.setDisable(false);
			ssTfEmail.setDisable(false);
			ssPdGraduation.setDisable(false);
			ssbtnUpdate.setDisable(false);

			ViewStarter.client
					.handleMessageFromClientUI(new Message(OperationType.SearchSubscriber, searchSubscriberUsrId));
		}

	}

	/**
	 * updateDetailsOnBorrow is update the detail on borrow by librarian procedure
	 * objects is an Object array
	 */
	public void updateDetailsOnBorrow(Object[] objects) {
		BorrowCopy bCopy = (BorrowCopy) objects[0];
		Boolean isPopular = (Boolean) objects[1];
		tfBorrowBookBorrowDate.setValue(bCopy.getBorrowDate().toLocalDate());
		tfBorrowBookEndBorrowDate.setValue(bCopy.getReturnDueDate().toLocalDate());
		if (isPopular) {
			txtBorrowBookNotice.setVisible(true);
		}
	}

	/**
	 * updateSearchSubscriberUI is updating the subscriber's history subscriber
	 * contains the relevant details of subscriber
	 */
	public void updateSearchSubscriberUI(Subscriber subscriber) {

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				switch (subscriber.getReaderCard().getStatus()) {
				case Hold:
					sslblStatus.setText("Hold");
					sslblStatus.setTextFill(Color.web("#FFBE0B"));
					ssCxbHoldSubscriber.setSelected(false);
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
					ssCxbHoldSubscriber.setDisable(false);
					ssCxbHoldSubscriber.setSelected(true);
					break;
				}

				if (mIsLibrarianManager)
					ssCxbHoldSubscriber.setDisable(false);
				else
					ssCxbHoldSubscriber.setDisable(true);

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

//				items = FXCollections.observableArrayList(history.get(SubscriberHistoryType.BookExtension));
//				ssLVChangeStatus.setItems(items);

			}

		});
	}

	/**
	 * cleanAndDisableSearchSubscriberFields clean search subscriber field
	 */
	public void cleanAndDisableSearchSubscriberFields() {
		ssTfFirstName.setDisable(true);
		ssTfLastName.setDisable(true);
		ssTfPhone.setDisable(true);
		ssTfUserName.setDisable(true);
		// ssTfPassword.setDisable(true);
		ssTfEmail.setDisable(true);
		ssPdGraduation.setDisable(true);
		ssbtnUpdate.setDisable(true);

		ssTfFirstName.clear();
		ssTfLastName.clear();
		ssTfPhone.clear();
		ssTfUserName.clear();
		ssTfPassword.clear();
		ssTfEmail.clear();
		ssPdGraduation.setValue(null);
	}

	/**
	 * cleanNewSubscriberFields clean subscriber fields
	 */
	public void cleanNewSubscriberFields() {
		tfSubscriberFirstName.clear();
		tfSubscriberLastName.clear();
		tfSubscriberUsrName.clear();
		tfSubscriberPassword.clear();
		tfSubscruberPhone.clear();
		tfSubscriberEmail.clear();
		dpGraduationDateNewSub.getEditor().clear();
	}

	/**
	 * onExtensionBookBtn executing manual book extension by the librarian
	 */
	@FXML
	void onExtensionBookBtn(ActionEvent event) {
		tfReturnBookSubscriberNumber.clear();
		tfReturnBookBorrowDate.setValue(null);
		tfReturnBookEndBorrowDate.setValue(null);
		tfReturnBookReturningDate.setValue(null);
		tfReturnBookCatalogNumber.setStyle(null);

		if (tfReturnBookCatalogNumber.getText().isEmpty())
			tfReturnBookCatalogNumber
					.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;-fx-border-radius: 5px;");
		else {
			
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Confirmation");
			alert.setHeaderText("Are you sure you want to get extantion for this book?");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				LocalDate currentDate = LocalDate.now();
				Date date = Date.valueOf(currentDate);
				BorrowCopy borrowCopy = new BorrowCopy(tfReturnBookCatalogNumber.getText(), date);

				ViewStarter.client
						.handleMessageFromClientUI(new Message(OperationType.ExtensionBookByLibrarian, borrowCopy));

			}
		}

	}

	public void updateManualExtensionUI(BorrowCopy borrowCopy) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				tfReturnBookSubscriberNumber.setText(Integer.toString(borrowCopy.getSubNum()));
				tfReturnBookBorrowDate.setValue(borrowCopy.getBorrowDate().toLocalDate());
				tfReturnBookEndBorrowDate.setValue(borrowCopy.getReturnDueDate().toLocalDate());
			}
		});

	}

	public void setLibrarianManager(boolean isLibrarianManager) {
		mIsLibrarianManager = isLibrarianManager;
	}

}
