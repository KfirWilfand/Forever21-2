package server;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import client.ViewStarter;
import client.controllers.Client;
import common.controllers.Message;
import common.controllers.enums.OperationType;
import common.entity.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import server.controllers.DBcontroller;
import server.gui.serverGuiController;
/**
 * The ServerStarter class that extends application represent the server started connection
 * @author  Kfir Wilfand
 * @author Bar Korkos
 * @author Zehavit Otmazgin
 * @author Noam Drori
 * @author Sapir Hochma
 */
public class ServerStarter extends Application {
	private static final Logger LOGGER = Logger.getLogger(Client.class.getName());
	//final public static int DEFAULT_PORT = 5555;
	public static ServerConsole server;
	public static serverGuiController severGui;
	/**
     * main method
	 */
	public static void main(String[] args) {

		launch(args);
	}
	/**
     * start server method
     * @param primaryStage
	 */
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader();
			Parent root = loader.load(getClass().getResource("/server/gui/severGui.fxml"));
			
			Scene scene = new Scene(root);
			primaryStage.setTitle("OBL Server");
			primaryStage.setResizable(false);

			List<String> args =getParameters().getRaw();
		
			if (args.contains("autolog")) {
				severGui.autolog(args.get(1));
			}

			primaryStage.show();
			primaryStage.setScene(scene);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	/**
     * stop server method
	 */
	@Override
	public void stop(){
		server.connectedClients.clear();
		try {
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
