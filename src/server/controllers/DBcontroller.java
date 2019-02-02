package server.controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;



import client.controllers.adapters.QueryBuilder;

/**
 * The DBcontroller class represent the Data base controller on the server side
 * @author  Kfir Wilfand
 * @author Bar Korkos
 * @author Zehavit Otmazgin
 * @author Noam Drori
 * @author Sapir Hochma
 */
public class DBcontroller {
	
	private static final Logger LOGGER = Logger.getLogger(DBcontroller.class.getName());
	private Connection connection;
	/**instance is a singleton of the class */
	private static DBcontroller instance;
    
    private DBcontroller(){}
    /**
	 * getInstance is creating the singleton object of the class
	 * @return DBcontroller
	 */
    public static DBcontroller getInstance(){
        if(instance == null){
            instance = new DBcontroller();
        }
        return instance;
    }

    /**
	 * connectDB is connecting to the data base
	 * @param schema          schema name
	 * @param userName        user name
	 * @param password        password 
	 */
	public  void connectDB(String schema,String userName, String password) {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (Exception ex) {
			/* handle the error */}
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost/"+schema, userName, password);
			LOGGER.info("MySql Server connected");
		} catch (SQLException ex) {/* handle any errors */
			LOGGER.severe("SQLException: " + ex.getMessage() + "\nSQLState: " + ex.getSQLState() + "\nVendorError: "
					+ ex.getErrorCode());
		}
	}
	/**
	 * query is executing the SELECT sql queries
	 * @param query      sql query
	 * @return result set of the fitting tuples
	 */
	public ResultSet query(String query)
	{
		ResultSet rs = null;
	   try {
		   Statement stmt = (Statement) connection.createStatement() ;
		   rs= stmt.executeQuery(query);
		   //System.out.println(rs);
		   
	   }catch (SQLException e) {
			e.printStackTrace();
		}
	   return rs;
	}
	/**
	 * insert is executing the UPDATE/INSERT sql queries
	 * @param query     sql qery
	 * @return boolean  if succeeded or not
	 */
	public Boolean insert(String query) 
	{
		 try {
			   Statement stmt = (Statement) connection.createStatement();
			   int res=stmt.executeUpdate(query);
			   if( res==1)
				   return true;
			   else
				   return false;
	
		   }catch (SQLException e) {
				e.printStackTrace();
			}
		   return false;
	}
	/**
	 * update is executing the UPDATE/INSERT sql queries
	 * @param query              sql query
	 * @return boolean           if succeeded or not
	 */
	public  Boolean update(String query) {//receive update query as a string and execute the query in the DB
	
		   try {
			   Statement stmt = (Statement) connection.createStatement() ;
			   int res= stmt.executeUpdate(query);
			   if( res==1)
				   return true;
			   else
				   return false;
	
		   }catch (SQLException e) {
				e.printStackTrace();
			}
		   return false;

	}
	

}
