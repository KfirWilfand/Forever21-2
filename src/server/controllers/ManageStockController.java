package server.controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import common.controllers.Message;
import common.controllers.enums.OperationType;
import common.controllers.enums.ReturnMessageType;
import common.entity.Book;

public class ManageStockController {
	private static ManageStockController instance;

	 private ManageStockController(){}
	 
	 public static ManageStockController getInstance(){
	        if(instance == null){
	            instance = new ManageStockController();
	        }
	        return instance;
	    }
	 public Message searchBook(Object msg) throws SQLException
	 {
		 String query = (String)((Message)msg).getObj();
		 DBcontroller dbControllerObj = DBcontroller.getInstance();
		 ResultSet books_res = dbControllerObj.query(query);
		if(books_res.next())
		{
			List<String> authors= Arrays.asList(books_res.getString("bAuthor").split(","));
			List<String> genres= Arrays.asList(books_res.getString("bGenre").split(","));
			Book book= new Book(books_res.getInt("bCatalogNum"), books_res.getString("bName"),  books_res.getString("bDescription"), authors, genres,
					books_res.getInt("bCopiesNum"), books_res.getDate("bPurchaseDate"), books_res.getString("bShelfLocation"), books_res.getString("bEdition"), books_res.getDate("bPrintDate"));
			return new Message(OperationType.SearchBookOnManageStock , book, ReturnMessageType.BooksFoundOnManageStock);
		}
		else
			return new Message(OperationType.SearchBookOnManageStock , null, ReturnMessageType.BookWasNotFoundOnManageStock);
	
	 }

}
