package server.gui;

import java.io.IOException;
import java.util.logging.Logger;

import client.ViewStarter;
import client.controllers.Client;
import client.controllers.Utils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import server.ServerConsole;
import server.ServerStarter;
import server.controllers.DBcontroller;

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
	public void initialize() {
		btnServerOnOff.setText("ON");
		btnServerOnOff.setStyle("-fx-background-color: #4CAF50;color: white;");
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

}
