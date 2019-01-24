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

    @FXML
    private TextField tfSchema;

    @FXML
    private TextField tfUsrName;

    @FXML
    private TextField tfPassword;

    @FXML
    private TextField tfServerPort;

    @FXML
    private ToggleButton btnServerOnOff;

    @FXML
    private Button btnDisconnectClients;
    
	@FXML
	public void initialize() {
		btnServerOnOff.setText("ON");
		btnServerOnOff.setStyle("-fx-background-color: #4CAF50;color: white;");
	}

    @FXML
    void onDisconnectClientBtn(ActionEvent event) {
    	Thread[] clients=ServerStarter.server.getClientConnections();
    	for(Thread client :clients)
    		{
    			client.stop();
    		}
    	ServerStarter.server.connectedClients.clear();
    }

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
