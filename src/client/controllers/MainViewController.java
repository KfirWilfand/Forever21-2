package client.controllers;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import client.ViewStarter;
import client.controllers.Utils;
import common.controllers.Message;
import common.controllers.enums.OperationType;
import common.entity.Librarian;
import common.entity.LibraryManager;
import common.entity.Subscriber;
import common.entity.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ComboBox;

public class MainViewController {

	@FXML
	private AnchorPane mainView;

	@FXML
	private ImageView welcomeImg;

	@FXML
	private Pane mainPane;

	@FXML
	private Button btnProfile;

	@FXML
	private Button btnSearchBook;

	@FXML
	private Label lblSubTitle;

	@FXML
	private Button btnLogin;
	// log in variables
	@FXML
	private TextField tfUserName;
	@FXML
	private TextField tfPassword;

	@FXML
	private Label lblLoginAs;


	@FXML
	private AnchorPane dialogBoxLogin;

	@FXML
	private Button btnDialogBoxLogin;

	@FXML
	private Button btnCloseLogIN;

	@FXML
	private ComboBox<String> comboboxLogAs;

	@FXML
	private Button btnLogout;

	private Utils utils;

	private User user;

	public User getUser() {
		return user;
	}

	@FXML
	private Button btnHomePage;

	@FXML
	void openLogInForm(ActionEvent event) {
		dialogBoxLogin.setVisible(true);
	}

	@FXML
	void onHomePageBtn(ActionEvent event) {
		utils.setBtnPressed(true, false, false);
		utils.layoutSwitcher(mainPane, "homepage.fxml", "Library management system");
	}

	@FXML
	public void initialize() {
		ViewStarter.client.mainViewController = this;
		this.utils = new Utils(this);
		ViewStarter.client.utilsControllers = this.utils;

		utils.layoutSwitcher(mainPane, "homepage.fxml", "Library management system");
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				utils.setBtnPressed(true, false, false);
			}
		});
	}

	@FXML
	void openProfileView(ActionEvent event) throws IOException {
		if (this.user != null)
			onLogin(user);
	}

	@FXML
	void onLogoutBtn(ActionEvent event) {
		btnLogout.setVisible(false);
		mainView.getChildren().add(dialogBoxLogin);
		dialogBoxLogin.setVisible(false);
		btnLogin.setText("Login");
		lblLoginAs.setText("");
		utils.setBtnPressed(false, false, false);
	}

	public Label getLblSubTitle() {
		return lblSubTitle;
	}

	@FXML
	void openSearchView(ActionEvent event) {
		utils.setBtnPressed(false, true, false);
		utils.layoutSwitcher(mainPane, "search.fxml", "Search Book");
	}

	@FXML
	void onCloseLogIn(ActionEvent event) {
		dialogBoxLogin.setVisible(false);
	}

	@FXML
	void onBtnDialogBoxLogin(ActionEvent event) {
		String loginQuery = "Select * FROM obl.`users` where usrName = '" + this.tfUserName.getText()
				+ "' AND `usrPassword` = '" + this.tfPassword.getText() + "'";
		ViewStarter.client.handleMessageFromClientUI(new Message(OperationType.Login, loginQuery));
	}

	public Button getBtnProfile() {
		return btnProfile;
	}

	public Button getBtnSearchBook() {
		return btnSearchBook;
	}

	public void onLogin(User user) {
		this.user = user;

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				btnLogout.setVisible(true);
				mainView.getChildren().remove(dialogBoxLogin);
				btnLogin.setText(user.getFirstName() + " " + user.getLastName());

				utils.setBtnPressed(false, false, true);

				if (user instanceof Subscriber) {
					utils.layoutSwitcher(mainPane, "subscriber.fxml", "Subscriber Profile");

					lblLoginAs.setText("Log as Subscriber");
					ViewStarter.client.subscriberClientControllerObj.initializeDetailsAtLogin((Subscriber) user);
				}

				if (user instanceof Librarian) {
					utils.layoutSwitcher(mainPane, "librarian.fxml", "Librarian Profile");
					lblLoginAs.setText("Log as Librarian");
				}

				if (user instanceof LibraryManager) {
					utils.layoutSwitcher(mainPane, "librarian.fxml", "Library Manager Profile");
					lblLoginAs.setText("Log as LibraryManager");
				}
			}
		});
	}

	public Button getBtnHomePage() {
		return btnHomePage;
	}

	public AnchorPane getMainView() {
		return mainView;
	}
	
	public Label getLblLoginAs() {
		return lblLoginAs;
	}

}
