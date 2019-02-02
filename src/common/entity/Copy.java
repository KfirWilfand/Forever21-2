package common.entity;

import java.io.Serializable;
/**
 * The Copy class represent a copy with all of his details and implementing Serializable
 * @author  Kfir Wilfand
 * @author Bar Korkos
 * @author Zehavit Otmazgin
 * @author Noam Drori
 * @author Sapir Hochma
 */
public class Copy implements Serializable 
{
	/** copyID is the copy id */
	private String copyID;
	/** bCatalogNum is the book catalog number */
	private int bCatalogNum;
	/** isAvilabale tells if the copy is aviable */
	private Boolean isAvilabale;
	
	/**
	 * Copy class constructor
	 * @param copyID
	 * @param bCatalogNum
	 * @param isAvilabale
	 */
	public Copy(String copyID, int bCatalogNum, Boolean isAvilabale) {
		super();
		this.copyID = copyID;
		this.bCatalogNum = bCatalogNum;
		this.isAvilabale = isAvilabale;
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
	 * getbCatalogNum getter of book catalog number
	 * @return bCatalogNum
	 */
	public int getbCatalogNum() {
		return bCatalogNum;
	}
	/**
	 * setbCatalogNum setter of book catalog number
	 * @param bCatalogNum
	 */
	public void setbCatalogNum(int bCatalogNum) {
		this.bCatalogNum = bCatalogNum;
	}
	/**
	 * isAvilabale tells if the book is aviable
	 * @return isAvilabale boolean
	 */
	public Boolean isAvilabale() {
		return isAvilabale;
	}
	/**
	 * setAvilabale setter of aviability of book
	 * @param isAvilabale
	 */
	public void setAvilabale(Boolean isAvilabale) {
		this.isAvilabale = isAvilabale;
	}
}
