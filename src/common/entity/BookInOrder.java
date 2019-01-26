package common.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Queue;
import java.util.concurrent.SynchronousQueue;

public class BookInOrder implements Serializable
{
private int bCatalogNum;
private int subNum;
private Timestamp dateOfOrder;

public int getbCatalogNum() {
	return bCatalogNum;
}

public void setbCatalogNum(int bCatalogNum) {
	this.bCatalogNum = bCatalogNum;
}

public int getSubNum() {
	return subNum;
}

public void setSubNum(int subNum) {
	this.subNum = subNum;
}

public Timestamp getDateOfOrder() {
	return dateOfOrder;
}

public void setDateOfOrder(Timestamp dateOfOrder) {
	this.dateOfOrder = dateOfOrder;
}

public BookInOrder(int subNum, int bCatalogNum , Timestamp dateOfOrder) {
	super();
	this.bCatalogNum = bCatalogNum;
	this.subNum = subNum;
	this.dateOfOrder = dateOfOrder;
}




}

