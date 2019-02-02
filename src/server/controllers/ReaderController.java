package server.controllers;

import java.net.URISyntaxException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import client.ViewStarter;
import client.controllers.adapters.QueryBuilder;
import common.controllers.Message;
import common.controllers.enums.OperationType;
import common.controllers.enums.ReturnMessageType;
import common.entity.Book;
import common.entity.InboxMsgItem;
import common.entity.Librarian;
import common.entity.LibraryManager;
import common.entity.Subscriber;
import common.entity.TransferFile;
import common.entity.User;
import common.entity.enums.InboxMsgType;
import common.entity.enums.UserType;
import server.ServerConsole;
/**
 * The ReaderController class represent the reader card controller on the server side
 * @author  Kfir Wilfand
 * @author Bar Korkos
 * @author Zehavit Otmazgin
 * @author Noam Drori
 * @author Sapir Hochma
 */
public class ReaderController {
	/**instance is a singleton of the class */
	private static ReaderController instance;
    
    private ReaderController(){}
    /**
	 * getInstance is creating the singleton object of the class
	 * @return  instance    instance of ReaderController
	 */
    public static ReaderController getInstance(){
        if(instance == null){
            instance = new ReaderController();
        }
        return instance;
        
    }
  
    /**
	 * login method
	 * @param msg contains the message from the client
	 * @throws SQLException when occurs
	 */
    public Message login(Object msg) throws SQLException
    {
    	String loginQuery=(String)((Message)msg).getObj();
    	DBcontroller dbControllerObj= DBcontroller.getInstance();
    	ResultSet user_res= dbControllerObj.query(loginQuery);
   
    	if(user_res.next()) {
    		if(ServerConsole.connectedClients.contains(user_res.getInt("usrId")))
    		{
    			return new Message(OperationType.Login, null, ReturnMessageType.ClientIsAlreadyLogin);
    		}
    		ServerConsole.connectedClients.add(user_res.getInt("usrId"));
    		if (user_res.getString("usrType").equals("Subscriber")) 
    		{
    			String query= "SELECT b.subStatus FROM obl.users as a right join obl.subscribers as b on a.usrId=b.subNum WHERE a.usrId = " + user_res.getString("usrId");
    			ResultSet result= dbControllerObj.query(query);
    			if(result.next())
    			{ 
    				if(result.getString("subStatus").equals("Lock"))
    				 return new Message(OperationType.Login, null, ReturnMessageType.SubscriberIsLocked);
    			}
    			
    			User user = SubscriberController.getSubscriberById(user_res.getString("usrId"));
    			List<InboxMsgItem> msgList=getInboxMessagesByID(user.getId());
    			Object[] msgObj=new Object[2];
    			msgObj[0]=user;
    			msgObj[1]=msgList;
    			
    			return new Message(OperationType.Login, msgObj, ReturnMessageType.Successful);	
    		}
		
    		if (user_res.getString("usrType").equals("Librarian")) {
    			User user = new Librarian(user_res.getInt("usrId"), user_res.getString("usrName"),  user_res.getString("usrPassword"), user_res.getString("usrFirstName"), user_res.getString("usrLastName"), user_res.getString("usrEmail"), UserType.stringToEnum(user_res.getString("usrType")));
    			List<InboxMsgItem> msgList=getInboxMessagesByID(user.getId());
    			Object[] msgObj=new Object[2];
    			msgObj[0]=user;
    			msgObj[1]=msgList;
    			
    			return new Message(OperationType.Login, msgObj, ReturnMessageType.Successful);
    		}

    		if (user_res.getString("usrType").equals("LibraryManager")) {
    			User user = new LibraryManager(user_res.getInt("usrId"), user_res.getString("usrName"), user_res.getString("usrPassword"),
    					user_res.getString("usrFirstName"), user_res.getString("usrLastName"), user_res.getString("usrEmail"), UserType.stringToEnum(user_res.getString("usrType")));
    			List<InboxMsgItem> msgList=getInboxMessagesByID(user.getId());
    			Object[] msgObj=new Object[2];
    			msgObj[0]=user;
    			msgObj[1]=msgList;
    			
    			return new Message(OperationType.Login, msgObj, ReturnMessageType.Successful);
    		}	
    	}else {
    		return new Message(OperationType.Login, null, ReturnMessageType.Unsuccessful);	
    	} 
    	return (Message) msg;
    }
    
    /**
   	 * searchBook is searching a book 
   	 * @param msg contains the message from the client
   	 * @throws SQLException when occurs
   	 */
    public Message searchBook(Object msg) throws SQLException
    {
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
    
    /**
   	 * sendTableOfContantToClient is send the table of contant to the client 
   	 * @param msg contains the message from the client
   	 */ 
  	public Message sendTableOfContantToClient(Message msg)
  	{
  		String bookName=(String)msg.getObj();
  		

    	String path="";
		try {
			path = (ReaderController.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
  		path = path.substring(0, path.lastIndexOf("/"))+"/TableOfContent/";
  		path=path+bookName.replace(" ","_")+".pdf";
  	
  		TransferFile tf=TransferFile.createFileToTransfer(path);
  		Object[] message=new Object[2];
  		message[0]=tf;
  		message[1]=bookName;
  		if(tf!=null)
  			return new Message(OperationType.DownloadTableOfContent, message , ReturnMessageType.Successful);
  		else
  			return new Message(OperationType.DownloadTableOfContent, null , ReturnMessageType.Unsuccessful);
  	}

    /**
   	 * getInboxMessagesByID get all the inbox messages of a particular user 
   	 * @param id              the id of the user 
   	 * @throws SQLException   SQLException
   	 * @return msgList        the list of the inbox messages
   	 */
  	public List<InboxMsgItem> getInboxMessagesByID(int id) throws SQLException
  	{
  		String query="select * from obl.inbox_msg where usrID="+String.valueOf(id);
  		DBcontroller dbControllerObj= DBcontroller.getInstance();
    	ResultSet msg_res= dbControllerObj.query(query);
    	List<InboxMsgItem> msgList=new ArrayList<InboxMsgItem>();
    	while(msg_res.next())
    	{
    		msgList.add(new InboxMsgItem(msg_res.getInt("usrID"), msg_res.getString("Title"), msg_res.getString("body"), InboxMsgType.stringToEnum(msg_res.getString("type")) , msg_res.getBoolean("is_read"), msg_res.getTimestamp("date")));
    	}
    	return msgList;
  	}
  	
  	
    /**
   	 * getInboxMessages use the 'getInboxMessageById' function and send to client the messages 
   	 * @param msg              contains the message from the client
   	 * @throws SQLException    SQLException
   	 */
  	public Message getInboxMessage(Message msg) throws SQLException
  	{
    	List<InboxMsgItem> msgList=getInboxMessagesByID(((User)msg.getObj()).getId());

    	if (msgList.isEmpty())
    		return new Message(OperationType.GetInboxMsg, null , ReturnMessageType.Unsuccessful);
    	else
    		return new Message(OperationType.GetInboxMsg, msgList , ReturnMessageType.Successful);
  		
  	}
  	
  	/**
  	 * makeAsRead change the inbox message to be show as message that already been read
  	 * @param msg       message from the client
  	 */
	public void makeAsRead(Message msg) {
		String query= "Update obl.inbox_msg set is_read=1 where usrID='"+((InboxMsgItem)msg.getObj()).getUserID()+"'and date='"+((InboxMsgItem)msg.getObj()).getTime()+"'";
		System.out.println(query);
		DBcontroller dbControllerObj= DBcontroller.getInstance();
		Boolean isUpdate=dbControllerObj.update(query);
		if(!isUpdate)
			System.out.println("ERROR: makeAsRead function");
	}

}
