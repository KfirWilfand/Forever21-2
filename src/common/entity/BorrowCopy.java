package common.entity;

import java.io.Serializable;
import java.sql.Date;

public class BorrowCopy implements Serializable {
	private String copyID;
	private int subNum;
	private Date borrowDate;
	private Date returnDueDate;
		
	public BorrowCopy(String copyID, int subNum, Date borrowDate, Date returnDueDate) {
		super();
		this.copyID = copyID;
		this.subNum = subNum;
		this.borrowDate = borrowDate;
		this.returnDueDate = returnDueDate;
	}

	public String getCopyID() {
		return copyID;
	}

	public void setCopyID(String copyID) {
		this.copyID = copyID;
	}

	public int getSubNum() {
		return subNum;
	}

	public void setSubNum(int subNum) {
		this.subNum = subNum;
	}

	public Date getBorrowDate() {
		return borrowDate;
	}

	public void setBorrowDate(Date borrowDate) {
		this.borrowDate = borrowDate;
	}

	public Date getReturnDueDate() {
		return returnDueDate;
	}

	public void setReturnDueDate(Date returnDueDate) {
		this.returnDueDate = returnDueDate;
	}

	
}
