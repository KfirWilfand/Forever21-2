package client.controllers;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import client.ViewStarter;
import common.controllers.Message;
import common.controllers.enums.OperationType;
import common.entity.InboxMsgItem;
import common.entity.enums.InboxMsgType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
/**
 * The InboxController class that operate the inbox
 * @author  Kfir Wilfand
 * @author Bar Korkos
 * @author Zehavit Otmazgin
 * @author Noam Drori
 * @author Sapir Hochma
 */
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
    private Button btnLock;

    @FXML
    private Button btnActive;

    
	@FXML
	public void initialize() {
		ViewStarter.client.inboxControllerObj = this;
	}
	
/**
 * onLockReaderBtn send to the server request to lock reader card
 * @param event
 */
    @FXML
    void onLockReaderBtn(ActionEvent event)
    {
    	String mydata = txtBody.getText();
    	Pattern pattern = Pattern.compile("'(.*?)'");
    	Matcher matcher = pattern.matcher(mydata);
    	if (matcher.find())
    	{
    		String subscriberNum=matcher.group(1).replaceAll("'", "");
    		ViewStarter.client.handleMessageFromClientUI(new Message(OperationType.LockReaderCard, subscriberNum ));
    	}
    }

    /**
     * onUnlockReaderBtn send to the server request to change the reader card to Active
     * @param event
     */   
    @FXML
    void onUnlockReaderBtn(ActionEvent event) 
    {
    	String mydata = txtBody.getText();
    	Pattern pattern = Pattern.compile("'(.*?)'");
    	Matcher matcher = pattern.matcher(mydata);
    	if (matcher.find())
    	{
    		String subscriberNum=matcher.group(1).replaceAll("'", "");
    		ViewStarter.client.handleMessageFromClientUI(new Message(OperationType.ChangeToActiveReaderCard, subscriberNum ));
    	}
    }

    /**
     * onClickMsg display the selected message
     * @param event
     */   
    @FXML
    void onClickMsg(MouseEvent event) {
    	InboxMsgItem msgItem=lvMessages.getSelectionModel().getSelectedItem();
		getTxtBody().setVisible(true);
		getTxtTitle().setVisible(true);
    	messagePane.setVisible(true);
		txtTitle.setText(msgItem.getTitle());
		txtBody.setText(msgItem.getBody());
		lvMessages.getSelectionModel().getSelectedItem().setIs_read(true);
		if(Integer.parseInt(ViewStarter.client.mainViewController.getTxtNumberOfMsg().getText())!=0)
		{
			int numberOfUnreadMsg=Integer.parseInt(ViewStarter.client.mainViewController.getTxtNumberOfMsg().getText())-1;
			ViewStarter.client.mainViewController.getTxtNumberOfMsg().setText(String.valueOf(numberOfUnreadMsg));
			if(numberOfUnreadMsg<=0)
			{
				ViewStarter.client.mainViewController.getRedCircel().setVisible(false);
				ViewStarter.client.mainViewController.getTxtNumberOfMsg().setVisible(false);	
			}
		}
		
		if(msgItem.getType().equals(InboxMsgType.LockReader))
			loadedPane.setVisible(true);
		else
			loadedPane.setVisible(false);
		
		ViewStarter.client.handleMessageFromClientUI(new Message(OperationType.makeAsRead, msgItem ));

    }

    /**
     * show inbox messages
     * @param messages
     */   
	public void showInboxMessages(List<InboxMsgItem> messages) {
		// TODO Auto-generated method stub
		
		for(InboxMsgItem msgItem : messages )
		{
			lvMessages.getItems().add(msgItem);
			//if(msgItem.isIs_read())	
		}	
	}
	
	 public Button getBtnLock() {
			return btnLock;
		}


		public Button getBtnActive() {
			return btnActive;
		}
		
	    public Text getTxtTitle() {
			return txtTitle;
		}


		public Text getTxtBody() {
			return txtBody;
		}

}

