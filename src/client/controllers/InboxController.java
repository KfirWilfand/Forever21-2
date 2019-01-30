package client.controllers;

import java.io.IOException;
import java.util.List;

import client.ViewStarter;
import common.controllers.Message;
import common.controllers.enums.OperationType;
import common.entity.InboxMsgItem;
import common.entity.enums.InboxMsgType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class InboxController {

    @FXML
    private ListView<InboxMsgItem> lvMessages;

    @FXML
    private Pane messagePane;

    @FXML
    private Text txtTitle;

    @FXML
    private Text txtBody;

    @FXML
    private Pane loadedPane;
    

    
	@FXML
	public void initialize() {
		ViewStarter.client.inboxControllerObj = this;
	}
	

    @FXML
    void onLockReaderBtn(ActionEvent event) {

    }

    @FXML
    void onUnlockReaderBtn(ActionEvent event) {

    }

	
    @FXML
    void onClickMsg(MouseEvent event) {
    	InboxMsgItem msgItem=lvMessages.getSelectionModel().getSelectedItem();
    	messagePane.setVisible(true);
		txtTitle.setText(msgItem.getTitle());
		txtBody.setText(msgItem.getBody());
		lvMessages.getSelectionModel().getSelectedItem().setIs_read(true);
		int numberOfUnreadMsg=Integer.parseInt(ViewStarter.client.mainViewController.getTxtNumberOfMsg().getText())-1;
		ViewStarter.client.mainViewController.getTxtNumberOfMsg().setText(String.valueOf(numberOfUnreadMsg));
		if(numberOfUnreadMsg==0)
		{
			ViewStarter.client.mainViewController.getRedCircel().setVisible(false);
			ViewStarter.client.mainViewController.getTxtNumberOfMsg().setVisible(false);	
		}
		
		if(msgItem.getType()==InboxMsgType.LockReader)
			loadedPane.setVisible(true);
		else
			loadedPane.setVisible(false);
		
		ViewStarter.client.handleMessageFromClientUI(new Message(OperationType.makeAsRead, msgItem ));

    }


	public void showInboxMessages(List<InboxMsgItem> messages) {
		// TODO Auto-generated method stub
		
		for(InboxMsgItem msgItem : messages )
		{
			lvMessages.getItems().add(msgItem);
			//if(msgItem.isIs_read())	
		}	
	}

}
