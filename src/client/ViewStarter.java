package client;

import java.io.IOException;
import java.util.logging.Logger;

import client.controllers.Client;
import client.controllers.MainViewController;
import common.controllers.Message;
import common.controllers.enums.OperationType;
import common.entity.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ViewStarter extends Application {
	private static final Logger LOGGER = Logger.getLogger(Client.class.getName());
	final public static int DEFAULT_PORT = 5555;
	public static Client client;

	public static void main(String[] args) {
		String host = "";
		int port = 0; // The port number

		try {
			port = Integer.parseInt(args[0]);
		} catch (ArrayIndexOutOfBoundsException e) {
			port = DEFAULT_PORT;
		}

		try {
			host = args[1];
		} catch (ArrayIndexOutOfBoundsException e) {
			host = "localhost";
		}

		try {
			client = new Client(host, port);
			LOGGER.info("Client setup connection! " + host + ":" + port);
		} catch (IOException exception) {
			LOGGER.severe("Error: Can't setup connection!" + " Terminating client." + exception);
			System.exit(1);
		}

		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader();
			Parent root = loader.load(getClass().getResource("/client/boundery/layouts/main_view.fxml"));
			
			Scene scene = new Scene(root);
			primaryStage.setTitle("OBL");
			primaryStage.setResizable(false);

			primaryStage.show();
			primaryStage.setScene(scene);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	@Override
	public void stop(){
		User usr=ViewStarter.client.mainViewController.getUser();
		ViewStarter.client.handleMessageFromClientUI(new Message(OperationType.Logout, usr));  
	}
}
