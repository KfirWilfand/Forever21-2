package common.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Queue;
import java.util.concurrent.SynchronousQueue;
/**
 * The BookInOrder class represent a book in order with all of his details and implementing Serializable
 * @author  Kfir Wilfand
 * @author Bar Korkos
 * @author Zehavit Otmazgin
 * @author Noam Drori
 * @author Sapir Hochma
 */
public class BookInOrder implements Serializable
{
/** bCatalogNum is the catalog number of the book */
private int bCatalogNum;
/** subNum is the subscriber number that order the book */
private int subNum;
/** dateOfOrder of date of order book */
private Timestamp dateOfOrder;
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
 * getDateOfOrder getter of order date
 * @return dateOfOrder
 */
public Timestamp getDateOfOrder() {
	return dateOfOrder;
}
/**
 * setDateOfOrder setter of order date
 * @param dateOfOrder
 */
public void setDateOfOrder(Timestamp dateOfOrder) {
	this.dateOfOrder = dateOfOrder;
}
/**
 * BookInOrder class constructor
 * @param subNum
 * @param bCatalogNum
 * @param dateOfOrder
 */
public BookInOrder(int subNum, int bCatalogNum , Timestamp dateOfOrder) {
	super();
	this.bCatalogNum = bCatalogNum;
	this.subNum = subNum;
	this.dateOfOrder = dateOfOrder;
}




}

