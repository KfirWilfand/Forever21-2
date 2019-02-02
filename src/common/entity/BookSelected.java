package common.entity;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;
import java.util.Map;

import common.entity.Book;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
/**
 * The BookSelected class represent the statistic details about specific book
 * @author  Kfir Wilfand
 * @author Bar Korkos
 * @author Zehavit Otmazgin
 * @author Noam Drori
 * @author Sapir Hochma
 */
public class BookSelected implements Serializable {

	private Integer bCatalogNum;
	private String bName;
	private Date returnDueDate;
	private Date actualReturnDate;
	private Boolean isSelected;
	private Date borrowDate;
	private float average;
	private int median;
	private Map<Integer, List<Integer>> distribution;

	public BookSelected(Integer bCatalogNum, String bName, Date borrowDate, Date returnDueDate, Date actualReturnDate,
			Boolean isSelected) {
		super();
		this.bCatalogNum = bCatalogNum;
		this.bName = bName;
		this.borrowDate = borrowDate;
		this.returnDueDate = returnDueDate;
		this.actualReturnDate = actualReturnDate;
		this.isSelected = isSelected;
	}

	public BookSelected(Integer bCatalogNum, String bName, Date borrowDate, Date returnDueDate, Date actualReturnDate,
			float average, int median, Map<Integer, List<Integer>> distribution) {
		super();
		this.bCatalogNum = bCatalogNum;
		this.bName = bName;
		this.returnDueDate = returnDueDate;
		this.actualReturnDate = actualReturnDate;
		this.isSelected = false;
	}

	public Date getBorrowDate() {
		return borrowDate;
	}

	public void setBorrowDate(Date borrowDate) {
		this.borrowDate = borrowDate;
	}

	public Integer getbCatalogNum() {
		return bCatalogNum;
	}

	public void setbCatalogNum(Integer bCatalogNum) {
		this.bCatalogNum = bCatalogNum;
	}

	public String getbName() {
		return bName;
	}

	public void setbName(String bName) {
		this.bName = bName;
	}

	public Date getReturnDueDate() {
		return returnDueDate;
	}

	public void setReturnDueDate(Date returnDueDate) {
		this.returnDueDate = returnDueDate;
	}

	public Date getActualReturnDate() {
		return actualReturnDate;
	}

	public void setActualReturnDate(Date actualReturnDate) {
		this.actualReturnDate = actualReturnDate;
	}

	public Boolean getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(Boolean isSelected) {
		this.isSelected = isSelected;
	}

}
