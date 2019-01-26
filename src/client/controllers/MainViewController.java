package client.controllers;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

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

	/** mainView is a anchor pane */
	@FXML
	private AnchorPane mainView;

	@FXML
	private ImageView welcomeImg;

	/** mainPane is a pane */
	@FXML
	private Pane mainPane;

	/** btnProfile is a button to profile */
	@FXML
	private Button btnProfile;

	/** btnSearchBook is the search book button */
	@FXML
	private Button btnSearchBook;

	/** lblSubTitle is a subtitle*/
	@FXML
	private Label lblSubTitle;

	/** btnLogin is the login button */
	@FXML
	private Button btnLogin;
	// log in variables
	/** tfUserName is the username */
	@FXML
	private TextField tfUserName;
	
	/** tfPassword is the user password */
	@FXML
	private TextField tfPassword;

	/** lblLoginAs is the name of the logged user */
	@FXML
	private Label lblLoginAs;

	/** dialogBoxLogin is the login anchor pance */
	@FXML
	private AnchorPane dialogBoxLogin;

	/** btnDialogBoxLogin is the login dialog box button */
	@FXML
	private Button btnDialogBoxLogin;

	/** btnCloseLogIN is the close log in button */
	@FXML
	private Button btnCloseLogIN;

	/** comboboxLogAs is a combo box */
	@FXML
	private ComboBox<String> comboboxLogAs;

	/** btnLogout is the logout button */
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
		ViewStarter.client.handleMessageFromClientUI(new Message(OperationType.Logout, user));
		btnLogout.setVisible(false);
		mainView.getChildren().add(dialogBoxLogin);
		dialogBoxLogin.setVisible(false);
		btnLogin.setText("Login");
		lblLoginAs.setText("");
		utils.setBtnPressed(true, false, false);
		utils.layoutSwitcher(mainPane, "homepage.fxml", "Library management system");
		user=null;
		tfUserName.clear();
		tfPassword.clear();		
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
		tfPassword.setStyle(null);
		tfUserName.setStyle(null);
		if(tfPassword.getText().isEmpty())
			tfPassword.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;-fx-border-radius: 5px;");
		if(tfUserName.getText().isEmpty())
			tfUserName.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;-fx-border-radius: 5px;");
		if(!tfUserName.getText().isEmpty() && !tfPassword.getText().isEmpty())
		{	String loginQuery = "Select * FROM obl.`users` where usrName = '" + this.tfUserName.getText()
					+ "' AND `usrPassword` = '" + this.tfPassword.getText() + "'";
			ViewStarter.client.handleMessageFromClientUI(new Message(OperationType.Login, loginQuery));
		}	
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
				try {
	
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
				} catch (Exception e) {
					System.out.println(e);
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
