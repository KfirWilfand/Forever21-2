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
import common.entity.InboxMsgItem;
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
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ComboBox;

/**
 * The MainViewController class represent the main view controller on the
 * client's side
 * 
 * @author Kfir Wilfand
 * @author Bar Korkos
 * @author Zehavit Otmazgin
 * @author Noam Drori
 * @author Sapir Hochma
 */
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

	/** lblSubTitle is a subtitle */
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

	@FXML
	private Button btnMailBox;

	/** utils object declaration */
	private Utils utils;

	/** user object declaration */
	private User user;

	/** btnHomePage homepage button */
	@FXML
	private Button btnHomePage;
	
    @FXML
    private Circle redCircel;

    @FXML
    private Text txtNumberOfMsg;
    
    @FXML
    private Label lblInbox;

    @FXML
    private Label lblProfile;
    
    public Circle getRedCircel() {
		return redCircel;
	}

	public void setRedCircel(Circle redCircel) {
		this.redCircel = redCircel;
	}

	public Text getTxtNumberOfMsg() {
		return txtNumberOfMsg;
	}

	public void setTxtNumberOfMsg(Text txtNumberOfMsg) {
		this.txtNumberOfMsg = txtNumberOfMsg;
	}

	private List<InboxMsgItem> msgList;

	/**
	 * getUser returns the current user
	 * 
	 * @return User
	 */
	public User getUser() {
		return user;
	}

	/**
	 * openLogInForm open the login form
	 * 
	 * @param event action event
	 */
	@FXML
	void openLogInForm(ActionEvent event) {
		dialogBoxLogin.setVisible(true);
	}

	/**
	 * onHomePageBtn display the homepage
	 * 
	 * @param event action event
	 */
	@FXML
	void onHomePageBtn(ActionEvent event) {
		utils.setBtnPressed(true, false, false, false);
		utils.layoutSwitcher(mainPane, "homepage.fxml", "Library management system");
	}

	/**
	 * initialize the main view controller
	 */
	@FXML
	public void initialize() {
		ViewStarter.client.mainViewController = this;
		this.utils = new Utils(this);
		ViewStarter.client.utilsControllers = this.utils;

		utils.layoutSwitcher(mainPane, "homepage.fxml", "Library management system");
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				utils.setBtnPressed(true, false, false, false);
			}
		});
	}

	/**
	 * onClickMailBox open user MailBox
	 * 
	 * @param event action of current event
	 * @throws IOException
	 */
	@FXML
	void onClickMailBox(ActionEvent event) {
//TODO
		utils.setBtnPressed(false, false, false, true);
		
		
		
		
		utils.layoutSwitcher(mainPane, "inbox.fxml", "My Inbox");
		ViewStarter.client.inboxControllerObj.getTxtBody().setVisible(false);
		ViewStarter.client.inboxControllerObj.getTxtTitle().setVisible(false);
		if(Integer.parseInt(getTxtNumberOfMsg().getText())==0)
		{
			getRedCircel().setVisible(false);
			getTxtNumberOfMsg().setVisible(false);
		}
		
		ViewStarter.client.handleMessageFromClientUI(new Message(OperationType.GetInboxMsg, this.user));
		
	}

	/**
	 * openProfileView open user profile
	 * 
	 * @param event action of current event
	 * @throws IOException
	 */
	@FXML
	void openProfileView(ActionEvent event) throws IOException {
		if (this.user != null)
			{
				Object[] objMsg= new Object[2];
				objMsg[0]=user;
				objMsg[1]=msgList;
				onLogin(objMsg);
			}
	}

	/**
	 * onLogoutBtn log out the current user
	 * 
	 * @param event action of current event
	 */
	@FXML
	void onLogoutBtn(ActionEvent event) {
		ViewStarter.client.handleMessageFromClientUI(new Message(OperationType.Logout, user));
		btnLogout.setVisible(false);
		mainView.getChildren().add(dialogBoxLogin);
		dialogBoxLogin.setVisible(false);
		btnLogin.setText("Login");
		lblLoginAs.setText("");
		utils.setBtnPressed(true, false, false, false);
		utils.layoutSwitcher(mainPane, "homepage.fxml", "Library management system");
		user = null;
		tfUserName.clear();
		tfPassword.clear();
		btnMailBox.setVisible(false);
		lblInbox.setVisible(false);
		lblProfile.setVisible(false);
		redCircel.setVisible(false);
		txtNumberOfMsg.setVisible(false);
		
	}

	/**
	 * getLblSubTitle get the user title
	 * 
	 * @return user label
	 */
	public Label getLblSubTitle() {
		return lblSubTitle;
	}

	/**
	 * getBtnMailBox get MailBox Button
	 * 
	 * @return Button
	 */
	public Button getBtnMailBox() {
		return btnMailBox;
	}

	/**
	 * openSearchView open the search view
	 * 
	 * @param event action of current event
	 */
	@FXML
	void openSearchView(ActionEvent event) {
		utils.setBtnPressed(false, true, false, false);
		utils.layoutSwitcher(mainPane, "search.fxml", "Search Book");
	}

	/**
	 * onCloseLogIn close after login the login dialog box
	 * 
	 * @param event action of current event
	 */
	@FXML
	void onCloseLogIn(ActionEvent event) {
		dialogBoxLogin.setVisible(false);
	}

	/**
	 * onBtnDialogBoxLogin open login dialog box
	 * 
	 * @param event action of current event
	 */
	@FXML
	void onBtnDialogBoxLogin(ActionEvent event) {
		tfPassword.setStyle(null);
		tfUserName.setStyle(null);
		if (tfPassword.getText().isEmpty())
			tfPassword.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;-fx-border-radius: 5px;");
		if (tfUserName.getText().isEmpty())
			tfUserName.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;-fx-border-radius: 5px;");
		if (!tfUserName.getText().isEmpty() && !tfPassword.getText().isEmpty()) {
			String loginQuery = "Select * FROM obl.`users` where usrName = '" + this.tfUserName.getText()
					+ "' AND `usrPassword` = '" + this.tfPassword.getText() + "'";
			ViewStarter.client.handleMessageFromClientUI(new Message(OperationType.Login, loginQuery));
		}
	}

	/**
	 * getBtnProfile presents profile
	 * 
	 * @return button
	 */
	public Button getBtnProfile() {
		return btnProfile;
	}

	/**
	 * getBtnSearchBook get the search book button
	 * 
	 * @return button
	 */
	public Button getBtnSearchBook() {
		return btnSearchBook;
	}

	/**
	 * onLogin make the log in procedure
	 * 
	 * @param objMsg the current logged user
	 */
	public void onLogin(Object[] objMsg) {
		this.user=(User) objMsg[0];
		this.msgList = (List<InboxMsgItem>) objMsg[1];

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				try {
					
					int counter=0;
					if(!msgList.isEmpty())
						{	txtNumberOfMsg.setVisible(true);
							for(InboxMsgItem msg: msgList )
							{
								if(!msg.isIs_read())
									counter++;
							}
							if(counter!=0)
							{
								txtNumberOfMsg.setText(String.valueOf(counter));
								redCircel.setVisible(true);
							}
						}
								
					btnMailBox.setVisible(true);
					lblInbox.setVisible(true);
					lblProfile.setVisible(true);
					btnLogout.setVisible(true);
					mainView.getChildren().remove(dialogBoxLogin);
					btnLogin.setText(user.getFirstName() + " " + user.getLastName());

					utils.setBtnPressed(false, false, true, false);

					if (user instanceof Subscriber) {
						utils.layoutSwitcher(mainPane, "subscriber.fxml", "Subscriber Profile");
						lblProfile.setText("Profile");
						lblLoginAs.setText("Log as Subscriber");
						ViewStarter.client.subscriberClientControllerObj.initializeDetailsAtLogin((Subscriber) user);
					}

					if (user instanceof Librarian) {
					
						utils.layoutSwitcher(mainPane, "librarian.fxml", "Librarian Profile");
						lblLoginAs.setText("Log as Librarian");
						
						ViewStarter.client.librarianClientControllerObj.setLibrarianManager(false);
						ViewStarter.client.librarianClientControllerObj.initializeDetailsAtLogin();
					}

					if (user instanceof LibraryManager) {
						utils.layoutSwitcher(mainPane, "librarian.fxml", "Library Manager Profile");
						lblLoginAs.setText("Log as Manager");
						lblProfile.setText("Operstions");
						ViewStarter.client.librarianClientControllerObj.setLibrarianManager(true);
						ViewStarter.client.librarianClientControllerObj.initializeDetailsAtLogin();
					}
				} catch (Exception e) {
					System.out.println(e);
				}

			}
		});
	}

	/**
	 * getBtnHomePage get the homepage button
	 * 
	 * @return button
	 */
	public Button getBtnHomePage() {
		return btnHomePage;
	}

	/**
	 * getMainView get the main viewbutton
	 * 
	 * @return anchor pane main view
	 */
	public AnchorPane getMainView() {
		return mainView;
	}

	/**
	 * getLblLoginAs write login as label
	 * 
	 * @return login as label
	 */
	public Label getLblLoginAs() {
		return lblLoginAs;
	}

	public void autolog(String usrName, String usrPass) {
		tfPassword.setText(usrPass);
		tfUserName.setText(usrName);
		onBtnDialogBoxLogin(null);
	}

}
