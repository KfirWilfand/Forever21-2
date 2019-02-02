
package client.controllers.adapters;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

import client.ViewStarter;
import client.controllers.BookDetailsController;
import client.controllers.Utils;
import common.controllers.Message;
import common.controllers.enums.OperationType;
import common.entity.Book;
import common.entity.BorrowBook;
import common.entity.BorrowCopy;
import common.entity.Subscriber;
import common.entity.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.effect.DropShadow;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import server.controllers.ManageStockController;
/**
 * The BookListItemController class represent the row at the table view of the borrowed books
 * @author  Kfir Wilfand
 * @author Bar Korkos
 * @author Zehavit Otmazgin
 * @author Noam Drori
 * @author Sapir Hochma
 */
public class BookListItemController extends ListCell<BorrowCopy>
{
    @FXML
    private AnchorPane tableRowBorrowBook;

	@FXML
    private ImageView imgBookImage;

    @FXML
    private Label txtBookName;

    @FXML
    private Button btnAskBorrowExtenation;

   


	@FXML
    private Label txtBorrowDate;

    @FXML
    private Label txtReturnDate;

    @FXML
    private Label txtTimeToReturn;

	@FXML
    private Button btnLossReporting;
    
    static Utils utils = new Utils(ViewStarter.client.mainViewController);

    public Button getBtnAskBorrowExtenation() {
		return btnAskBorrowExtenation;
	}
    
    @Override
	protected void updateItem(BorrowCopy bookItem, boolean empty) 
    {
    	super.updateItem(bookItem, empty);
    	//btnAskBorrowExtenation.setDisable(true);

		
		if (empty) 
		{
			setText(null);
			setContentDisplay(ContentDisplay.TEXT_ONLY);
		} 
		else 
		{
			btnAskBorrowExtenation.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
	            @Override
	            public void handle(MouseEvent e) {
	            	DropShadow shadow3 = new DropShadow();
	            	btnAskBorrowExtenation.setEffect(shadow3);
	            }
	        });

			btnLossReporting.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
	            @Override
	            public void handle(MouseEvent e) {
	            	DropShadow shadow4 = new DropShadow();
	            	btnLossReporting.setEffect(shadow4);
	            }
	        });
			txtBookName.setText(bookItem.getBookName());
			txtBorrowDate.setText(bookItem.getBorrowDate().toString());
			txtReturnDate.setText(bookItem.getReturnDueDate().toString());
			LocalDate todaylocaldate = LocalDate.now();
			Date todaydate = Date.valueOf(todaylocaldate);	
		// Image image = new Image(item.getBookImagePath());
			// ivBook.setImage(image)v
			Long diff = bookItem.getReturnDueDate().getTime() - todaydate.getTime();
			int dayVar=Math.toIntExact(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
			txtTimeToReturn.setText(Integer.toString(dayVar));
			//User usr=ViewStarter.client.mainViewController.getUser();
			if(dayVar<=7&&dayVar>=0)
	    		btnAskBorrowExtenation.setDisable(false);
			
/**
 * on click loss report message have been send to the server to update that the book is loss
 */
			btnLossReporting.setOnAction(new EventHandler<ActionEvent>()
			{
			    @Override public void handle(ActionEvent e)
			    {
			    	DropShadow shadow = new DropShadow();
			    	btnLossReporting.setEffect(shadow);
			    	Object[] queryArr=new Object[4];
			    	queryArr[0]="UPDATE obl.books as a left join obl.copeis as b"
			    			+ " on a.bCatalogNum=b.bCatalogNum SET a.bCopiesNum=a.bCopiesNum-1"
			    			+ " WHERE b.copyID='"+bookItem.getCopyID()+"'";
			    	queryArr[1]="update obl.borrows set actualReturnDate=0000-00-00 where copyID='"+bookItem.getCopyID()+"' and subNum="+bookItem.getSubNum()+" and borrowDate='"+bookItem.getBorrowDate()+"'";
			    	queryArr[2]=bookItem.getSubNum();
			    	queryArr[3]=bookItem.getBookName();
			    	btnLossReporting.setDisable(true);
			    	ViewStarter.client.handleMessageFromClientUI(new Message(OperationType.LossReporting, queryArr));
			    		
			    }
			});
			
			/**
			 * on click borrow extenuation - message have been send to the server about extenuation request
			 */
							
			btnAskBorrowExtenation.setOnAction(new EventHandler<ActionEvent>()
			{
			    @Override public void handle(ActionEvent e)
			    {
			    	DropShadow shadow1 = new DropShadow();
			    	btnAskBorrowExtenation.setEffect(shadow1);
			    	Object[] query=new Object[4];
			    	if(bookItem.isPopular()==true)
			   			utils.showAlertWithHeaderText(AlertType.ERROR, "", "This book is popular! Extenation is not permitted.");
			    	else 
			    	{
						query[0]= "UPDATE obl.borrows SET returnDueDate=DATE_ADD('"+bookItem.getReturnDueDate()+"', INTERVAL 7 DAY) where subNum="+bookItem.getSubNum()+" and copyID='"+bookItem.getCopyID()+"' and returnDueDate='"+bookItem.getReturnDueDate()+"'";					    	
						query[1]=bookItem.getCatalogNumber();
						query[2]=bookItem.getSubNum();
						query[3]=bookItem.getBookName();
						btnAskBorrowExtenation.setDisable(true);
						ViewStarter.client.handleMessageFromClientUI(new Message(OperationType.AutomaticBorrowExtenation, query));
			    	}					    	
			    }
			});
			btnAskBorrowExtenation.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
	            @Override
	            public void handle(MouseEvent e) {
	            	btnAskBorrowExtenation.setEffect(null);
	            }
	        });
			btnLossReporting.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
	            @Override
	            public void handle(MouseEvent e) {
	            	btnLossReporting.setEffect(null);
	            }
	        });
			setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
			setGraphic(tableRowBorrowBook);
			}
    

    }
    
    

}

