package client.controllers.adapters;

import java.io.IOException;

import client.ViewStarter;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AlertController {

    @FXML
    private Pane paneAlert;

    @FXML
    private ImageView ivIcon;

    @FXML
    private Label lblTitle;

    @FXML
    private Button btnCloseBtn;

    @FXML
    private Text lblSubTitle;

    @FXML
    private Label lblType;

    @FXML
    private Button btnOK;



	@FXML
    void onCloseBtn(ActionEvent event) {
		ViewStarter.client.mainViewController.getMainView().getChildren().remove(ViewStarter.client.alertClientControllerObj.getPaneAlert() );

    }

    @FXML
    void onOKbtn(ActionEvent event) {
		ViewStarter.client.mainViewController.getMainView().getChildren().remove(ViewStarter.client.alertClientControllerObj.getPaneAlert() );
    }


	
	@FXML
	public void initialize() {
		ViewStarter.client.alertClientControllerObj = this;
		//this.paneAlert.setLayoutX(210);
		//this.paneAlert.setLayoutY(120);
	}

	public void info(String lblTitleToSet, String lblSubTitleToSet) throws IOException {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Stage primaryStage = new Stage();
				FXMLLoader loader = new FXMLLoader();
				Pane root;
				try {
					root = loader.load(getClass().getResource("/client/boundery/layouts/alert.fxml").openStream());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
				lblType.setText("Information");
				ivIcon.setImage(new Image("/client/boundery/resources/information.png"));
				lblTitle.setText(lblTitleToSet);
				lblSubTitle.setText(lblSubTitleToSet);	
				//ViewStarter.client.mainViewController.getMainView().getChildren().add(paneAlert);
			}
		
		});

	}
	
	public void error(String lblTitle, String lblSubTitle) {
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Stage primaryStage = new Stage();
				FXMLLoader loader = new FXMLLoader();
				try {
					Pane root = loader.load(getClass().getResource("/client/boundery/layouts/alert.fxml").openStream());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ViewStarter.client.alertClientControllerObj.getLblType().setText("Error");
				
				ViewStarter.client.alertClientControllerObj.getIvIcon().setImage(new Image(getClass().getResource("/client/boundery/resources/error.png").toString()));
				ViewStarter.client.alertClientControllerObj.getLblTitle().setText(lblTitle);
				ViewStarter.client.alertClientControllerObj.getLblSubTitle().setText(lblSubTitle);
				
				ViewStarter.client.mainViewController.getMainView().getChildren().add(paneAlert);
			}
		});

	}
	
	public void warning(String lblTitle, String lblSubTitle) {
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Stage primaryStage = new Stage();
				FXMLLoader loader = new FXMLLoader();
				try {
					Pane root = loader.load(getClass().getResource("/client/boundery/layouts/alert.fxml").openStream());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ViewStarter.client.alertClientControllerObj.getLblType().setText("Warning");
				
				ViewStarter.client.alertClientControllerObj.getIvIcon().setImage(new Image(getClass().getResource("/client/boundery/resources/warning.png").toString()));
				ViewStarter.client.alertClientControllerObj.getLblTitle().setText(lblTitle);
				ViewStarter.client.alertClientControllerObj.getLblSubTitle().setText(lblSubTitle);
				
				ViewStarter.client.mainViewController.getMainView().getChildren().add(paneAlert);
			}
		});

	}
    public Pane getPaneAlert() {
		return paneAlert;
	}

	public void setPaneAlert(Pane paneAlert) {
		this.paneAlert = paneAlert;
	}

	public ImageView getIvIcon() {
		return ivIcon;
	}

	public void setIvIcon(ImageView ivIcon) {
		this.ivIcon = ivIcon;
	}

	public Label getLblTitle() {
		return lblTitle;
	}

	public void setLblTitle(Label lblTitle) {
		this.lblTitle = lblTitle;
	}

	public Button getBtnCloseBtn() {
		return btnCloseBtn;
	}

	public void setBtnCloseBtn(Button btnCloseBtn) {
		this.btnCloseBtn = btnCloseBtn;
	}

	public Text getLblSubTitle() {
		return lblSubTitle;
	}

	public void setLblSubTitle(Text lblSubTitle) {
		this.lblSubTitle = lblSubTitle;
	}

	public Label getLblType() {
		return lblType;
	}

	public void setLblType(Label lblType) {
		this.lblType = lblType;
	}

	public Button getBtnOK() {
		return btnOK;
	}

	public void setBtnOK(Button btnOK) {
		this.btnOK = btnOK;
	}
}
