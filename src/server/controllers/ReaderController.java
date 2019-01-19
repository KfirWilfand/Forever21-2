package server.controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import client.controllers.adapters.QueryBuilder;
import common.controllers.Message;
import common.controllers.enums.OperationType;
import common.controllers.enums.ReturnMessageType;
import common.entity.Book;
import common.entity.Librarian;
import common.entity.LibraryManager;
import common.entity.Subscriber;
import common.entity.User;
import common.entity.enums.UserType;

public class ReaderController {

	private static ReaderController instance;
    
    private ReaderController(){}
    
    public static ReaderController getInstance(){
        if(instance == null){
            instance = new ReaderController();
        }
        return instance;
        
    }
  
    
    public Message login(Object msg) throws SQLException
    {
    	String loginQuery=(String)((Message)msg).getObj();
    	DBcontroller dbControllerObj= DBcontroller.getInstance();
    	ResultSet user_res= dbControllerObj.query(loginQuery);
    	if(user_res.next()) {
    		if (user_res.getString("usrType").equals("Subscriber")) {

    			String subscriberQuery="SELECT b.subNum, a.usrName, a.usrPassword, a.usrFirstName, a.usrLastName, a.usrEmail, b.subPhoneNum, a.usrType, b.subStatus FROM obl.users as a right join obl.subscribers as b on a.usrId=b.subNum WHERE a.usrId = "+user_res.getString("usrId");
    			ResultSet subscriber_res= dbControllerObj.query(subscriberQuery);
    			if(subscriber_res.next()) {
    				User user = new Subscriber(subscriber_res.getInt("subNum"), subscriber_res.getString("usrName"), subscriber_res.getString("usrPassword"),
    					subscriber_res.getString("usrFirstName"), subscriber_res.getString("usrLastName"), subscriber_res.getString("usrEmail"), UserType.stringToEnum(subscriber_res.getString("usrType")), "Subscriber", subscriber_res.getString("subPhoneNum"));

    				return new Message(OperationType.Login, user, ReturnMessageType.UserSuccessLogin);
    			}
    		}
		
    		if (user_res.getString("usrType").equals("Librarian")) {
    			User user = new Librarian(user_res.getInt("usrId"), user_res.getString("usrName"),  user_res.getString("usrPassword"), user_res.getString("usrFirstName"), user_res.getString("usrLastName"), user_res.getString("usrEmail"), UserType.stringToEnum(user_res.getString("usrType")));
    			return new Message(OperationType.Login, user, ReturnMessageType.UserSuccessLogin);
    		}

    		if (user_res.getString("usrType").equals("LibraryManager")) {
    			User user = new LibraryManager(user_res.getInt("usrId"), user_res.getString("usrName"), user_res.getString("usrPassword"),
    					user_res.getString("usrFirstName"), user_res.getString("usrLastName"), user_res.getString("usrEmail"), UserType.stringToEnum(user_res.getString("usrType")));
    			return new Message(OperationType.Login, user, ReturnMessageType.UserSuccessLogin);
    		}	
    	}else {
    		return new Message(OperationType.Login, null, ReturnMessageType.UserFailedLogin);	
    	} 
    	return (Message) msg;
    }
    
    public Message searchBook(Object msg) throws SQLException
    {//TODO :μαγεχ 
    	String searchQuery= (String)((Message)msg).getObj();
    	DBcontroller dbControllerObj= DBcontroller.getInstance();
    	ResultSet books_res= dbControllerObj.query(searchQuery);
    	List<Book> books_list = Book.resultSetToList(books_res);
    	if(!books_list.isEmpty())
    	{
    		if (((Message)msg).getOperationType() ==  OperationType.SearchBook )
    			return new Message(OperationType.SearchBook, books_list, ReturnMessageType.BooksFound);
    		else //if the operation type is 'SearchBookOnManageStock'
    			return new Message(OperationType.SearchBookOnManageStock, books_list, ReturnMessageType.BooksFoundOnManageStock);
    	}
    	else
    	{
    		return new Message(OperationType.SearchBook, null, ReturnMessageType.BooksNotFound);
    	}
    }


}
