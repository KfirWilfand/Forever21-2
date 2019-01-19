package common.entity;

import java.io.Serializable;

public class Copy implements Serializable 
{

	private String copyID;
	private int bCatalogNum;
	private Boolean isAvilabale;
	
	public Copy(String copyID, int bCatalogNum, Boolean isAvilabale) {
		super();
		this.copyID = copyID;
		this.bCatalogNum = bCatalogNum;
		this.isAvilabale = isAvilabale;
	}

	public String getCopyID() {
		return copyID;
	}

	public void setCopyID(String copyID) {
		this.copyID = copyID;
	}

	public int getbCatalogNum() {
		return bCatalogNum;
	}

	public void setbCatalogNum(int bCatalogNum) {
		this.bCatalogNum = bCatalogNum;
	}

	public Boolean isAvilabale() {
		return isAvilabale;
	}

	public void setAvilabale(Boolean isAvilabale) {
		this.isAvilabale = isAvilabale;
	}
}
