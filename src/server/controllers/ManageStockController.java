package server.controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import common.controllers.Message;
import common.controllers.enums.OperationType;
import common.controllers.enums.ReturnMessageType;
import common.entity.Book;
import common.entity.Copy;

public class ManageStockController {
	private static ManageStockController instance;

	 private ManageStockController(){}
	 
	 public static ManageStockController getInstance(){
	        if(instance == null){
	            instance = new ManageStockController();
	        }
	        return instance;
	    }
	 
	 public Message addNewBook(Object msg) throws SQLException 
	 {
		 String query=(String)((Message)msg).getObj();
		 DBcontroller dbControllerObj=DBcontroller.getInstance();
	   	 Boolean res = dbControllerObj.insert(query);
	   	 if(res)
	   	 {
	   		return new Message(OperationType.AddNewBook, null , ReturnMessageType.Successful); 
	   	 }
	   	 else return new Message(OperationType.AddNewBook, null , ReturnMessageType.Unsuccessful); 
	 }
	 
	 public Message getCopiesbyCatalogNumber(Object msg) throws SQLException 
	 {
		 List<Copy> copies_list= new ArrayList<Copy>();
		 
		 String query=(String)((Message)msg).getObj();
		 DBcontroller dbControllerObj=DBcontroller.getInstance();
		 ResultSet result = dbControllerObj.query(query);
		 if(result != null) 
		 {
			 while(result.next()) 
			 {
				 copies_list.add(new Copy(result.getString("copyID"),result.getInt("bCatalogNum"),result.getBoolean("isAvilable")));	 
			 }
			 return new Message(OperationType.GetCopiesOfSelectedBook, copies_list , ReturnMessageType.Successful); 
		 }
		 return new Message(OperationType.GetCopiesOfSelectedBook, copies_list , ReturnMessageType.Unsuccessful); 
	 }
	 
	 public Message addNewCopy(Object msg) throws SQLException 
	 {
		 	Object[] m=(Object[])((Message)msg).getObj();
	    	DBcontroller dbControllerObj=DBcontroller.getInstance();
	       	Boolean res1=dbControllerObj.insert((String)m[0]);
	    	Boolean res2=dbControllerObj.update((String)m[1]);	
	    	
	    	if(res1 && res2)
    			return new Message(OperationType.AddNewCopy, (Copy)m[2] , ReturnMessageType.Successful);
    		else
    			return new Message(OperationType.AddNewCopy, null , ReturnMessageType.Unsuccessful);
	 }
	 
	 public Message deleteCopy(Object msg) throws SQLException 
	 {
		 	String[] query=(String[])((Message)msg).getObj();
	    	DBcontroller dbControllerObj=DBcontroller.getInstance();
	       	Boolean res1=dbControllerObj.insert(query[0]);
	    	Boolean res2=dbControllerObj.update(query[1]);	
	    	
	    	if(res1 && res2)
    			return new Message(OperationType.DeleteCopy, null , ReturnMessageType.Successful);
    		else
    			return new Message(OperationType.DeleteCopy, null , ReturnMessageType.Unsuccessful);
	 }
	 
	 public Message updateBookDetails(Object msg) throws SQLException 
	 {
		 String query=(String)((Message)msg).getObj();
		 DBcontroller dbControllerObj=DBcontroller.getInstance();
	   	 Boolean res = dbControllerObj.update(query);
	   	 if(res)
	   	 {
	   		return new Message(OperationType.UpdateBookDetails, null , ReturnMessageType.Successful); 
	   	 }
	   	 else return new Message(OperationType.UpdateBookDetails, null , ReturnMessageType.Unsuccessful);  
	 }
}

