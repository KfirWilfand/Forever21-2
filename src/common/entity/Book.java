package common.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Book implements Serializable {
	
	private int catalogNum;
	private String bookName;
	private String description;
	private List <String> author;
	private List <String> genre;
	private int copiesNum;
	private Date purchaseDate;
	private String shelfLocation;
	private String edition;
	private Date printDate;
	
	public Book(int catalogNum, String bookName, String description, List<String> author, List<String> genre,
			int copiesNum, Date purchaseDate, String shelfLocation, String edition, Date printDate) {
		this.catalogNum = catalogNum;
		this.bookName = bookName;
		this.description = description;
		this.author = author;
		this.genre = genre;
		this.copiesNum = copiesNum;
		this.purchaseDate = purchaseDate;
		this.shelfLocation = shelfLocation;
		this.edition = edition;
		this.printDate = printDate;
	}
	
	
	public int getCatalogNum() {
		return catalogNum;
	}

	public void setCatalogNum(int catalogNum) {
		this.catalogNum = catalogNum;
	}

	
}
