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


public class DBcontroller {
	
	private static final Logger LOGGER = Logger.getLogger(DBcontroller.class.getName());
	private Connection connection;
	
	private static DBcontroller instance;
    
    private DBcontroller(){}
    
    public static DBcontroller getInstance(){
        if(instance == null){
            instance = new DBcontroller();
        }
        return instance;
    }

	
	public  void connectDB() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (Exception ex) {
			/* handle the error */}
		try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost/obl", "root", "0508386875");
			LOGGER.info("MySql Server connected");
		} catch (SQLException ex) {/* handle any errors */
			LOGGER.severe("SQLException: " + ex.getMessage() + "\nSQLState: " + ex.getSQLState() + "\nVendorError: "
					+ ex.getErrorCode());
		}
	}

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

	public Boolean insert(String query) 
	{//for now, this looks exactly like update function, maby in the future we'll merge them both
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
