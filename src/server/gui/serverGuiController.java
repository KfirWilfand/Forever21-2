package server.gui;

import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.logging.Logger;

import client.ViewStarter;
import client.controllers.Client;
import client.controllers.Utils;
import common.entity.Subscriber;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.text.Text;
import server.ServerConsole;
import server.ServerStarter;
import server.controllers.AutomaticFunctionsController;
import server.controllers.DBcontroller;
import server.controllers.SendMailController;
import server.controllers.SubscriberController;

public class serverGuiController {
	private static final Logger LOGGER = Logger.getLogger(Client.class.getName());

	/** tfSchema is the name of the schema*/
    @FXML
    private TextField tfSchema;

    /** tfUsrName is the username */
    @FXML
    private TextField tfUsrName;

    /** tfPassword is the password */
    @FXML
    private TextField tfPassword;

    /** tfServerPort is the server's port */
    @FXML
    private TextField tfServerPort;

    /** btnServerOnOff is a toggle button of on and off */
    @FXML
    private ToggleButton btnServerOnOff;

    /** btnDisconnectClients is the disconnect all clients button */
    @FXML
    private Button btnDisconnectClients;
    
    /** initialize all the server's gui controller */
    

    @FXML
    private TextField tfSubscriberNumber;
    
    @FXML
    private Text tfGraduateResult;

    @FXML
    private Button btnSend;
    
    @FXML
    private Button btnSetTime;

    @FXML
    private Text txtAutoFunc;

	/**
     * onClickSendBtn a subscriber id that finish graduate is send to the "OBL" and update at the DB the date of graduate
     * Check if there are still books he not return and change his status .
     * @param event is action event
     * @exception IOException
	 */
    @FXML
    void onClickSendBtn(ActionEvent event) throws SQLException 
    {
    	tfGraduateResult.setText(" ");
    	tfSubscriberNumber.setStyle(null);
    	if(tfSubscriberNumber.getText().isEmpty())
    	{
    		tfSubscriberNumber.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;-fx-border-radius: 5px;");
    		return;
    	}
    	
		Date today = Date.valueOf(LocalDate.now());
		String query1 = "update obl.subscribers set subGraduationDate ='"+today+"' where subNum="+tfSubscriberNumber.getText();
		DBcontroller dbControllerObj = DBcontroller.getInstance();
	    Boolean res1 = dbControllerObj.update(query1);
	    if(res1)
	    {
	    	String query2 = "select * from obl.borrows where subNum ="+tfSubscriberNumber.getText()+" and actualReturnDate is null";
	    	ResultSet res2 = dbControllerObj.query(query2);
	    	if(!res2.next())
	    	{
	    		String updateStatusToLock= "update obl.subscribers set subStatus='Lock' where subNum="+tfSubscriberNumber.getText();
	    		boolean isLock= dbControllerObj.update(updateStatusToLock);
	    		if(isLock)
	    			tfGraduateResult.setText("Graduate student return all Books, Reader Card is LOCK!");
	    		else 
	    			tfGraduateResult.setText("Graduate student return all Books, but FAIL to Lock reader card");
	    	}
	    	else
	    	{
	    		String updateStatusToHold= "update obl.subscribers set subStatus='Hold' where subNum="+tfSubscriberNumber.getText();
	    		boolean isHold= dbControllerObj.update(updateStatusToHold);
	    		if(isHold)
	    		{
	    			tfGraduateResult.setText("Graduate student but still has not returned all the books he borrowed, Reader Card is HOLD!");
	    			Subscriber graduateSub=SubscriberController.getSubscriberById(tfSubscriberNumber.getText());
	    			String mailSubject="Your need to return your books";
	    			String mailBody="Dear Student congrats on graduation,\nYou have books you should return to the library!";
	    			SendMailController.sendMailToSubscriber(graduateSub, mailSubject, mailBody);
	    			System.out.println("EMAIL send to subscriber "+graduateSub.toString()); 	
	    		}
	    		else
	    			tfGraduateResult.setText("Graduate student still have books to return, but FAIL to HOLD reader card");
	    	}
	    }
	    else
	    	tfGraduateResult.setText("Fail to update graduate date at OBL");
	    
    }
    
	@FXML
	public void initialize() {
		btnServerOnOff.setText("ON");
		btnServerOnOff.setStyle("-fx-background-color: #4CAF50;color: white;");
		ServerStarter.severGui = this;
	}
	/**
     * onDisconnectClientBtn disconnect all the clients
     * @param event is action event
	 */
    @FXML
    void onDisconnectClientBtn(ActionEvent event) {
    	Thread[] clients=ServerStarter.server.getClientConnections();
    	for(Thread client :clients)
    		{
    			client.stop();
    		}
    	ServerStarter.server.connectedClients.clear();
    }
	/**
     * onServerOnOffbtn turn off the server
     * @param event is action event
     * @exception IOException
	 */
    @FXML
    void onServerOnOffbtn(ActionEvent event) {
    	if(btnServerOnOff.isSelected())
    	{
    		btnSetTime.setDisable(false);
    		tfSubscriberNumber.setDisable(false);
    		btnSend.setDisable(false);
    		ServerStarter.server = new ServerConsole(Integer.parseInt(tfServerPort.getText()));
			try {
				ServerStarter.server.listen();
			} catch (IOException e) {
				LOGGER.severe("ERROR - Could not listen for clients!");
				e.printStackTrace();
			}	
			DBcontroller dbControllerObj=DBcontroller.getInstance();
			dbControllerObj.connectDB(tfSchema.getText(), tfUsrName.getText(), tfPassword.getText());
			btnServerOnOff.setText("OFF");
			btnServerOnOff.setStyle("-fx-background-color: #f44336;-fx-color: white;");
    	}
    	else
    	{
    		txtAutoFunc.setVisible(false);
    		btnSetTime.setDisable(true);
    		tfSubscriberNumber.setDisable(true);
    		btnSend.setDisable(true);
    		//ServerStarter.server.serverStopped();
    		btnServerOnOff.setText("ON");
    		btnServerOnOff.setStyle("-fx-background-color: #4CAF50;color: white;");
    		ServerStarter.server.connectedClients.clear();
    		try {
				ServerStarter.server.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

    	}

    }
	public void autolog(String password) {
		btnServerOnOff.setSelected(true);
		tfPassword.setText(password);
		
		onServerOnOffbtn(null);
		
	}
	
	/**
     * onClickSetTimeToNow change the execute time of the automatic functions to - now
     * @param event is action event
	 */
    @FXML
    void onClickSetTimeToNow(ActionEvent event) {
    	txtAutoFunc.setVisible(false);
    	Timestamp now =new Timestamp(System.currentTimeMillis());
    	AutomaticFunctionsController afObj = AutomaticFunctionsController.getInstance();
    	txtAutoFunc.setVisible(true);
		afObj.startExecutionAt(now.getHours(),now.getMinutes() ,now.getSeconds());
    }


}
