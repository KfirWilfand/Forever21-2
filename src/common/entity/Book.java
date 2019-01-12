package common.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Book implements Serializable {

	private int catalogNum;
	private String bookName;
	private String description;
	private List<String> author;
	private List<String> genre;
	private int copiesNum;
	private Date purchaseDate;
	private String shelfLocation;
	private String edition;
	private Date printDate;
	private String bookImagePath;
	private static final String Defualt_Image_Path = "/client/boundery/resources/book-no-image.png";

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

	public String getBookName() {
		return bookName;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getAuthor() {
		return author;
	}

	public void setAuthor(List<String> author) {
		this.author = author;
	}

	public List<String> getGenre() {
		return genre;
	}

	public void setGenre(List<String> genre) {
		this.genre = genre;
	}

	public int getCopiesNum() {
		return copiesNum;
	}

	public void setCopiesNum(int copiesNum) {
		this.copiesNum = copiesNum;
	}

	public Date getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public String getShelfLocation() {
		return shelfLocation;
	}

	public void setShelfLocation(String shelfLocation) {
		this.shelfLocation = shelfLocation;
	}

	public String getEdition() {
		return edition;
	}

	public void setEdition(String edition) {
		this.edition = edition;
	}

	public Date getPrintDate() {
		return printDate;
	}

	public void setPrintDate(Date printDate) {
		this.printDate = printDate;
	}

	public String getBookImagePath( ) {
		return (bookImagePath != null) ? Defualt_Image_Path : bookImagePath;
	}



}
