package client.controllers;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import client.ViewStarter;
import common.controllers.Message;
import common.controllers.enums.OperationType;
import common.entity.Book;
import common.entity.TransferFile;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import server.controllers.ManageStockController;

/**
 * The UpdateOrAddBookController class represent the update or add new book controller on the client's side
 * @author  Kfir Wilfand
 * @author Bar Korkos
 * @author Zehavit Otmazgin
 * @author Noam Drori
 * @author Sapir Hochma
 */
public class UpdateOrAddBookController {
	
	/** tfCatalogNumber is the catalog number text */
    @FXML
    private TextField tfCatalogNumber;

    /** tfBookName is the book name */
    @FXML
    private TextField tfBookName;

    /** tfAuthorName is the author name */
    @FXML
    private TextField tfAuthorName;

    /** tfEditionNumber is the edition number text */
    @FXML
    private TextField tfEditionNumber;

    /** tfLocationOnShelf is the location on sherlf text*/
    @FXML
    private TextField tfLocationOnShelf;

    /** txteDescription is the description of the book */
    @FXML
    private TextArea txteDescription;
    
    /** tfGenre is the genre of the book */
    @FXML
    private TextField tfGenre;

    /** dpPrintingDate is the date of printing */
    @FXML
    private DatePicker dpPrintingDate;

    /** dpPurchaseDate is the date of purchase date */
    @FXML
    private DatePicker dpPurchaseDate;

    /** tfCopiesNumber is the copies number text */
    @FXML
    private TextField tfCopiesNumber;

    /** cbIsPopular is the popularity check box */
	@FXML
    private CheckBox cbIsPopular;

	/** btnUpdate is the update button */
    @FXML
    private Button btnUpdate;

    /** btnUploadTableOfContent is the upload table of content button */
   	@FXML
    private Button btnUploadTableOfContent;

   	/** btnAddBook is the add book button */
    @FXML
    private Button btnAddBook;

    /** btnBack is the back button */
	@FXML
    private Button btnBack;

	/** tfTableOfContent is the table of content text*/
    @FXML
    private TextField tfTableOfContent;

    /** bookImage is the book image */
    @FXML
    private ImageView bookImage;
    
    /** tfBookImagePath is the book image path */
    @FXML
    private TextField tfBookImagePath;
    
    private byte[] imgByteArr;

    /**
     * initialize is initializing the update or add controller
	 */
	@FXML
	public void initialize() {
		ViewStarter.client.updateOrAddBookControllerObj = this;
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
//				if (btnAddCopy != null)
//					btnAddCopy.setVisible(false);
//				if (btnAddBook != null)
//					btnAddBook.setVisible(true);
//				if (btnUpdate != null)
//					btnUpdate.setVisible(false);
			
			}
		});
	
	}
    
	/**
	 * onClickAddBook is adding a new book to inventory
	 * @param event is an action event
	 */
    @FXML
    void onClickAddBook(ActionEvent event) {
    	
    	ViewStarter.client.manageStockClientControllerObj.getTvCopies().getItems().clear();
    	tfBookName.setStyle(null);
    	tfAuthorName.setStyle(null);
    	tfEditionNumber.setStyle(null);
    	tfLocationOnShelf.setStyle(null);
    	tfGenre.setStyle(null);
    	txteDescription.setStyle(null);
    	dpPrintingDate.setStyle(null);
    	dpPurchaseDate.setStyle(null);

		if(tfBookName.getText().isEmpty())
			tfBookName.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;-fx-border-radius: 5px;");
		if(tfAuthorName.getText().isEmpty())
			tfAuthorName.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;-fx-border-radius: 5px;");
		if(tfEditionNumber.getText().isEmpty())
			tfEditionNumber.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;-fx-border-radius: 5px;");
		if(tfLocationOnShelf.getText().isEmpty())
			tfLocationOnShelf.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;-fx-border-radius: 5px;");
		if(tfGenre.getText().isEmpty())
			tfGenre.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;-fx-border-radius: 5px;");
		if(txteDescription.getText().isEmpty())
			txteDescription.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;-fx-border-radius: 5px;");
		if(dpPrintingDate.getValue()==null)
			dpPrintingDate.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;-fx-border-radius: 5px;");
		if(dpPurchaseDate.getValue()==null)
			dpPurchaseDate.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;-fx-border-radius: 5px;");
		if(!tfBookName.getText().isEmpty() && !tfAuthorName.getText().isEmpty() && !tfEditionNumber.getText().isEmpty() && !tfLocationOnShelf.getText().isEmpty() && !tfGenre.getText().isEmpty() && !txteDescription.getText().isEmpty() && !(dpPrintingDate.getValue()==null) && !(dpPurchaseDate.getValue()==null))
		{
			
			String addBookQuery = "insert  into obl.books (bName,bDescription,bEdition,bPrintDate,bCopiesNum,bShelfLocation,bGenre,bAuthor,bPurchaseDate,bAvilableCopiesNum,bIsPopular) "
					+ "values ('"+ tfBookName.getText() +"','"+ txteDescription.getText() +"','"+ tfEditionNumber.getText() +"','"+ dpPrintingDate.getValue()+"'"
						+ ",0,'"+tfLocationOnShelf.getText()+"','"+tfGenre.getText()+"','"+tfAuthorName.getText()+"'"
							+ ",'"+dpPurchaseDate.getValue()+"',0,"+cbIsPopular.isSelected()+")";
			
			TransferFile tf =TransferFile.createFileToTransfer(tfTableOfContent.getText());
			TransferFile photo=TransferFile.createFileToTransfer(tfBookImagePath.getText());
			
			Object[] msg=new Object[4];
			msg[0]=addBookQuery;
			msg[1]=tf;
			msg[2]=tfBookName.getText();
			msg[3]=photo;
			
			ViewStarter.client.handleMessageFromClientUI(new Message(OperationType.AddNewBook, msg));
		}
    }


    /**
	 * onClickBack going back to the previous page
	 * @param event is action event
	 * @exception IOException
	 */
    @FXML
    void onClickBack(ActionEvent event)
    {

    	try 
    	{
        	ViewStarter.client.manageStockClientControllerObj.getBtnAddNewBook().setVisible(true);
        	ViewStarter.client.manageStockClientControllerObj.getTvCopies().setVisible(true);
        	ViewStarter.client.manageStockClientControllerObj.getBtnAddNewCopy().setVisible(true);
        	ViewStarter.client.manageStockClientControllerObj.getBtnDeleteCopy().setVisible(true);
        	ViewStarter.client.manageStockClientControllerObj.getTfEnterNewCopyID().setVisible(true);
			ViewStarter.client.manageStockClientControllerObj.getLabel1().setVisible(true);
			ViewStarter.client.manageStockClientControllerObj.getLabel2().setVisible(true);
    		ViewStarter.client.manageStockClientControllerObj.getTvCopies().getItems().clear();
			Parent newPane = FXMLLoader.load(getClass().getResource("/client/boundery/layouts/searchBook_by_number_at_manageStock.fxml"));
			if(ViewStarter.client.manageStockClientControllerObj.getInnerPaneInManageStock() != null)
			{
				ViewStarter.client.manageStockClientControllerObj.getInnerPaneInManageStock().getChildren().setAll(newPane);
			}
    	}
		 catch (IOException e) 
    	{
			e.printStackTrace();
		}
    }
    /**
	 * onClickUpdate updates the book details
	 * @param event is action event
	 */
    @FXML
    void onClickUpdate(ActionEvent event) 
    {
    	tfCatalogNumber.setStyle(null);
    	tfBookName.setStyle(null);
    	tfAuthorName.setStyle(null);
    	tfEditionNumber.setStyle(null);
    	tfLocationOnShelf.setStyle(null);
    	tfGenre.setStyle(null);
    	txteDescription.setStyle(null);
    	dpPrintingDate.setStyle(null);
    	dpPurchaseDate.setStyle(null);
    	
    	
    	if(tfCatalogNumber.getText().isEmpty())
    		tfBookName.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;-fx-border-radius: 5px;");
		if(tfBookName.getText().isEmpty())
			tfBookName.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;-fx-border-radius: 5px;");
		if(tfAuthorName.getText().isEmpty())
			tfAuthorName.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;-fx-border-radius: 5px;");
		if(tfEditionNumber.getText().isEmpty())
			tfEditionNumber.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;-fx-border-radius: 5px;");
		if(tfLocationOnShelf.getText().isEmpty())
			tfLocationOnShelf.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;-fx-border-radius: 5px;");
		if(tfGenre.getText().isEmpty())
			tfGenre.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;-fx-border-radius: 5px;");
		if(txteDescription.getText().isEmpty())
			txteDescription.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;-fx-border-radius: 5px;");
		if(dpPrintingDate.getValue()==null)
			dpPrintingDate.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;-fx-border-radius: 5px;");
		if(dpPurchaseDate.getValue()==null)
			dpPurchaseDate.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;-fx-border-radius: 5px;");
    	
		if(!tfCatalogNumber.getText().isEmpty()&&!tfBookName.getText().isEmpty() && !tfAuthorName.getText().isEmpty() && !tfEditionNumber.getText().isEmpty() && !tfLocationOnShelf.getText().isEmpty() && !tfGenre.getText().isEmpty() && !txteDescription.getText().isEmpty() && !(dpPrintingDate.getValue()==null) && !(dpPurchaseDate.getValue()==null))
    	{	String query = "UPDATE obl.books SET bName='"+tfBookName.getText()+"',bAuthor='"+tfAuthorName.getText()+"',bGenre='"+tfGenre.getText()+"',bIsPopular="+cbIsPopular.isSelected()+",bEdition='"+tfEditionNumber.getText()+"',bPrintDate='"+dpPrintingDate.getValue()+"',bDescription='"+txteDescription.getText()+"',bPurchaseDate='"+dpPurchaseDate.getValue()+"',bShelfLocation='"+tfLocationOnShelf.getText()+"' WHERE bCatalogNum="+tfCatalogNumber.getText()+";";
    	
			TransferFile tf =TransferFile.createFileToTransfer(tfTableOfContent.getText());
			TransferFile photo=TransferFile.createFileToTransfer(tfBookImagePath.getText());
		
			Object[] msg=new Object[4];
			msg[0]=query;
			msg[1]=tf;
			msg[2]=tfBookName.getText();
			msg[3]=photo;
		
			System.out.println(query);
			ViewStarter.client.handleMessageFromClientUI(new Message(OperationType.UpdateBookDetails,msg ));
    	}
    	
    }
    /**
	 * showSelectedBookDetails is displaying the details of selected book
	 * @param book contains the details of the book
	 */
    public void showSelectedBookDetails(Book book)
    {
    	Platform.runLater(new Runnable() 
    	{
		@Override
		public void run() 
			{
				tfCatalogNumber.setText(String.valueOf(book.getCatalogNum()));
		    	tfBookName.setText(book.getBookName());
		    	tfAuthorName.setText(String.join(", ",book.getAuthor()));
		    	tfGenre.setText(String.join(", ",book.getGenre()));
		    	cbIsPopular.setSelected(book.isPopular());
		    	tfEditionNumber.setText(book.getEdition());
		    	dpPrintingDate.setValue(book.getPrintDate().toLocalDate());
		       	dpPurchaseDate.setValue(book.getPurchaseDate().toLocalDate());
		       	txteDescription.setText(book.getDescription());
		       	tfLocationOnShelf.setText(book.getShelfLocation());
		    	tfCopiesNumber.setText(String.valueOf(book.getCopiesNum()));
		    	
		    	ViewStarter.client.handleMessageFromClientUI(new Message(OperationType.ShowBookPhoto, book.getBookName()));
		    
			}
    	});
    }
    /**
	 * btnAddBook is getting the add new book button
	 * @return the add new book button
	 */
    public Button getBtnAddBook() {
		return btnAddBook;
	}
    /**
	 * getBtnUpdate get the update inventory button
	 * @return btnUpdate the upload button
	 */
    public Button getBtnUpdate() {
		return btnUpdate;
	}
    /**
	 * getTfCopiesNumber get the number of copies
	 * @return the copies number
	 */
    public TextField getTfCopiesNumber() {
		return tfCopiesNumber;
	}

    /**
	 * setTfCopiesNumber set the number of cpies per book
	 * @param tfCopiesNumber contains the number of copies
	 */
	public void setTfCopiesNumber(TextField tfCopiesNumber) {
		this.tfCopiesNumber = tfCopiesNumber;
	}

	/**
	 * onUploadImageBtn uploads an image of a book
	 *@param event action event
	 */
    @FXML
    void onUploadImageBtn(ActionEvent event) throws IOException {
    	FileChooser fc= new FileChooser();
    	File selectedFile =fc.showOpenDialog(null);
    	if (selectedFile != null)
    		{
    			tfBookImagePath.setText(selectedFile.getCanonicalPath());
    			bookImage.setImage(new Image(selectedFile.toURI().toString()));
    		}
    	
  	
    }
    /**
	 * onUploadTableOfContent upload table of content of added book 
	 * @param event action event
	 */
    @FXML
    void onUploadTableOfContent(ActionEvent event) {
    	FileChooser fc= new FileChooser();
    	File selectedFile =fc.showOpenDialog(null);
    	if (selectedFile != null)
    			tfTableOfContent.setText(selectedFile.getAbsolutePath());		
    
    	
    }
    
    public void showPhoto(String fileName) throws URISyntaxException
    {
    	URL url = getClass().getResource("/BooksImages/");
    	
    	String path =(UpdateOrAddBookController.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
  		path = path.substring(0, path.lastIndexOf("/"))+"/BooksImages/";
		String str=path+fileName.replace(" ","_")+".png";
		System.out.println(str);
    	bookImage.setImage(new Image(new File(str).toURI().toString()));
    }
	


    
}
