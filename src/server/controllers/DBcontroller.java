package server.controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
			connection = DriverManager.getConnection("jdbc:mysql://localhost/obl", "root", "root");
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

	public  void insert(String table, List<String> params) {
//		INSERT INTO `student` VALUES ('1234', 'Kfir', 'Active', 'ReturnBookRequest,LendingReqest', '0');

	}

	public  void update(List<String> params) {
//		UPDATE obl.student SET StatusMembership = 'Freeze' WHERE StudentID = 'k12345';

	}

}
