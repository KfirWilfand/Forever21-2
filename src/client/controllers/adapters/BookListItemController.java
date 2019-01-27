package client.controllers.adapters;

import java.io.IOException;
import java.sql.Date;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import client.ViewStarter;
import client.controllers.BookDetailsController;
import common.controllers.Message;
import common.controllers.enums.OperationType;
import common.entity.Book;
import common.entity.BorrowBook;
import common.entity.BorrowCopy;
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
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

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



    
    
    @Override
	protected void updateItem(BorrowCopy bookItem, boolean empty) 
    {
		super.updateItem(bookItem, empty);
		System.out.println(bookItem);
		if (empty) 
		{
			setText(null);
			setContentDisplay(ContentDisplay.TEXT_ONLY);
		} 
		else 
		{
	
			txtBookName.setText(bookItem.getBookName());
			txtBorrowDate.setText(bookItem.getBorrowDate().toString());
			txtReturnDate.setText(bookItem.getReturnDueDate().toString());

			LocalDate todaylocaldate = LocalDate.now();
			Date todaydate = Date.valueOf(todaylocaldate);	
			//Date returnDate = Date.valueOf(bookItem.getReturnDueDate().toString());
			//txtTimeToReturn.set(Long.ChronoUnit.DAYS.between(returnDate, r));//לעשות את החישוב של הימים 
			// Image image = new Image(item.getBookImagePath());
			// ivBook.setImage(image)
			//Long diff = bookItem.getReturnDueDate().getTime() - todaydate.getTime();
			//txtTimeToReturn.setText((Math.toIntExact(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS))).toString());
			
			btnLossReporting.setOnAction(new EventHandler<ActionEvent>()
			{
			    @Override public void handle(ActionEvent e)
			    {
			    	Object[] queryArr=new Object[2];
			    	queryArr[0]="UPDATE obl.books as a left join obl.copeis as b"
			    			+ " on a.bCatalogNum=b.bCatalogNum SET a.bCopiesNum=a.bCopiesNum-1"
			    			+ " WHERE b.copyID='"+bookItem.getCopyID()+"'";
			    	queryArr[1]="update obl.borrows set actualReturnDate=0000-00-00 where copyID='"+bookItem.getCopyID()+"' and subNum="+bookItem.getSubNum()+" and borrowDate='"+bookItem.getBorrowDate()+"'";
			    	ViewStarter.client.handleMessageFromClientUI(new Message(OperationType.LossReporting, queryArr));
			    		
			    }
			});
			setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
			setGraphic(tableRowBorrowBook);
			}
    }
}



