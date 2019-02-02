package common.entity;

import java.io.Serializable;
import java.sql.Date;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * The BorrowCopy class represent a borrow copy with all of his details and implementing Serializable
 * @author  Kfir Wilfand
 * @author Bar Korkos
 * @author Zehavit Otmazgin
 * @author Noam Drori
 * @author Sapir Hochma
 */

public class BorrowCopy implements Serializable {

	private String bookName;



	private int catalogNumber;
	private boolean isPopular; 
	/** copyID is the copy id */
	private String copyID;
	/** subNum is the subscriber number */
	private int subNum;
	/** borrowDate is the borrow date */
	private Date borrowDate;
	/** returnDueDate is the return due date */
	private Date returnDueDate;
	/** actualReturnDate is the  actual return date */
	private Date actualReturnDate;

	/**
	 * BorrowCopy class constructor
	 * @param copyID
	 * @param subNum
	 * @param borrowDate
	 * @param returnDueDate
	 */

	public BorrowCopy(String copyID, int subNum, Date borrowDate, Date returnDueDate) {
		super();
		this.copyID = copyID;
		this.subNum = subNum;
		this.borrowDate = borrowDate;
		this.returnDueDate = returnDueDate;
	}

	//this constructor  is to the borrows books. you cant use in the book name
	public BorrowCopy(String bookName,String copyID, int subNum, Date borrowDate, Date returnDueDate,int catalogNumber,boolean isPopular) {
		super();
		this.bookName = bookName;
		System.out.println(bookName);
		this.copyID = copyID;
		this.subNum = subNum;
		this.borrowDate = borrowDate;
		this.returnDueDate = returnDueDate;
		this.catalogNumber=catalogNumber;
		this.isPopular=isPopular;
	}

	/**
	 * BorrowCopy class constructor
	 * @param copyID
	 * @param actualReturnDate
	 */

	public BorrowCopy(String copyID, Date actualReturnDate)//constructor for return date functionality 
	{
		this.copyID = copyID;
		this.setActualReturnDate(actualReturnDate);
	}
	
	/**
	 * getCopyID getter of book copy id
	 * @return copy id
	 */
	public String getCopyID() {
		return copyID;
	}
	/**
	 * setCopyID setter of book copy id
	 * @param copyID
	 */
	public void setCopyID(String copyID) {
		this.copyID = copyID;
	}
	/**
	 * getSubNum getter of subscriber number
	 * @return subNum
	 */
	public int getSubNum() {
		return subNum;
	}
	/**
	 * setSubNum setter of subscriber number
	 * @param subNum
	 */
	public void setSubNum(int subNum) {
		this.subNum = subNum;
	}
	/**
	 * getBorrowDate getter of borrow date
	 * @return borrowDate
	 */
	public Date getBorrowDate() {
		return borrowDate;
	}
	/**
	 * setBorrowDate setter of borrow date
	 * @param borrowDate
	 */
	public void setBorrowDate(Date borrowDate) {
		this.borrowDate = borrowDate;
	}
	/**
	 * getReturnDueDate getter of return due date
	 * @return returnDueDate
	 */
	public Date getReturnDueDate() {
		return returnDueDate;
	}
	/**
	 * setReturnDueDate setter of return due date
	 * @param returnDueDate
	 */
	public void setReturnDueDate(Date returnDueDate) {
		this.returnDueDate = returnDueDate;
	}
	/**
	 * getActualReturnDate getter of actual return date
	 * @return actualReturnDate
	 */
	public Date getActualReturnDate() {
		return actualReturnDate;
	}
	/**
	 * setActualReturnDate setter of actual return date
	 * @param actualReturnDate
	 */
	public void setActualReturnDate(Date actualReturnDate) {
		this.actualReturnDate = actualReturnDate;
	}
	public String getBookName() {
		return bookName;
	}
	
	public int getCatalogNumber() {
		return catalogNumber;
	}

	public boolean isPopular() {
		return isPopular;
	}

	/**
	 * resultSetToList create a list with the books that borrowed by the subscriber 
	 * @param rs                   result set from data base
	 * @return borrowBooks_list    list of borrow book
	 * @exception SQLException SQLException
	 */
	public static List<BorrowCopy> resultSetToList(ResultSet rs) throws SQLException
	{
		List<BorrowCopy> borrowBooks_list= new ArrayList<BorrowCopy>();
		if(rs != null) 
		{
       		while (rs.next())
    		{
       			borrowBooks_list.add(new BorrowCopy(rs.getString("bName"),rs.getString("copyID"),rs.getInt("subNum"),rs.getDate("borrowDate"),rs.getDate("returnDueDate"),rs.getInt("bCatalogNum"),rs.getBoolean("bIsPopular")));
    		}
		}
		return borrowBooks_list;
	}

	
}
