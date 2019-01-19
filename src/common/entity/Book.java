package common.entity;

import java.io.Serializable;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

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
	private boolean isPopular;
	private int avilableCopiesNum;
	
	private static final String Defualt_Image_Path = "/client/boundery/resources/book-no-image.png";

	public Book(int catalogNum, String bookName, String description, List<String> author, List<String> genre,
			int copiesNum, Date purchaseDate, String shelfLocation, String edition, Date printDate, boolean isPopular, int avilableCopiesNum) {
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
		this.isPopular = isPopular;
		this.avilableCopiesNum=avilableCopiesNum;
	}

	public boolean isPopular() {
		return isPopular;
	}

	public void setPopular(boolean isPopular) {
		this.isPopular = isPopular;
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

	public static List<Book> resultSetToList(ResultSet rs) throws SQLException
	{
		List<Book> books_list= new ArrayList<Book>();
		if(rs != null) 
		{
       		while (rs.next())
    		{
    			List<String> authors= Arrays.asList(rs.getString("bAuthor").split(","));
    			List<String> genres= Arrays.asList(rs.getString("bGenre").split(","));
    			books_list.add(new Book(rs.getInt("bCatalogNum"), rs.getString("bName"),  rs.getString("bDescription"), 
    					authors, genres, rs.getInt("bCopiesNum"), rs.getDate("bPurchaseDate"), 
    					rs.getString("bShelfLocation"), rs.getString("bEdition"), rs.getDate("bPrintDate"), rs.getBoolean("bIsPopular"),rs.getInt("bAvilableCopiesNum")));
    			
    		}
	     }
		return books_list;

	}

	public int getAvilableCopiesNum() {
		return avilableCopiesNum;
	}

	public void setAvilableCopiesNum(int avilableCopiesNum) {
		this.avilableCopiesNum = avilableCopiesNum;
	}
}
