package client;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

//import com.itextpdf.layout.element.Image;
import javafx.scene.image.Image;
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

/**
 * The ViewStarter class that extends application represent the start view
 * @author  Kfir Wilfand
 * @author Bar Korkos
 * @author Zehavit Otmazgin
 * @author Noam Drori
 * @author Sapir Hochma
 */
public class ViewStarter extends Application {
	private static final Logger LOGGER = Logger.getLogger(Client.class.getName());
	final public static int DEFAULT_PORT = 5555;
	public static Client client;
	/**
     * main method
	 */
	public static void main(String[] args) {
		String host = "";
		int port = 0; // The port number

		try {
			if (args[0].equals("autolog"))
				throw new Exception();
			
			port = Integer.parseInt(args[0]);
		} catch (Exception e) {
			port = DEFAULT_PORT;
		}

		try {
			if (args[0].equals("autolog"))
				throw new Exception();

			host = args[1];
		} catch (Exception e) {
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
	/**
     * start method
     * @param primaryStage       primary Stage
	 */
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader();
			Parent root = loader.load(getClass().getResource("/client/boundery/layouts/main_view.fxml"));

			Scene scene = new Scene(root);
			primaryStage.setTitle("OBL");
			primaryStage.setResizable(false);
			primaryStage.getIcons().add(new Image(getClass().getResource("boundery/resources/logo.png").toString()));
			List<String> args = getParameters().getRaw();

			if (args.contains("autolog")) {
				client.mainViewController.autolog(args.get(1), args.get(2));
			}

			primaryStage.show();
			primaryStage.setScene(scene);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	/**
     * stop method
	 */
	@Override
	public void stop() {
		User usr = ViewStarter.client.mainViewController.getUser();
		ViewStarter.client.handleMessageFromClientUI(new Message(OperationType.Logout, usr));
	}
}