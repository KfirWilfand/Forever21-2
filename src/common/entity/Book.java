package common.entity;

import java.io.Serializable;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.List;
/**
 * The Book class represent a book with all of his details and implementing Serializable
 * @author  Kfir Wilfand
 * @author Bar Korkos
 * @author Zehavit Otmazgin
 * @author Noam Drori
 * @author Sapir Hochma
 */
public class Book implements Serializable {
	/** catalogNum is the catalog number of the book */
	private int catalogNum;
	/** bookName is the name of the book */
	private String bookName;
	/** description is the description of the book */
	private String description;
	/** author is the author/s of the book */
	private List<String> author;
	/** genre is the genre of the book */
	private List<String> genre;
	/** copiesNum is the number of copies of the book */
	private int copiesNum;
	/** purchaseDate is the date of purchase of the book */
	private Date purchaseDate;
	/** shelfLocation is the location on shelf of the book */
	private String shelfLocation;
	/** edition is the edition of the book */
	private String edition;
	/** printDate is the printing date of the book */
	private Date printDate;
	/** bookImagePath is the book image path of the book */
	private String bookImagePath;
	/** isPopular tells if the book is popular */
	private boolean isPopular;
	/** avilableCopiesNum tells the number of aviable copies num of the book */
	private int avilableCopiesNum;
	
	private static final String Defualt_Image_Path = "/client/boundery/resources/book-no-image.png";
	/**
   	 * Book is the constructor of the class
   	 * @param catalogNum
   	 * @param bookName
   	 * @param description
   	 * @param author
   	 * @param genre
   	 * @param copiesNum
   	 * @param purchaseDate
   	 * @param shelfLocation
   	 * @param edition
   	 * @param printDate
   	 * @param isPopular
   	 * @param avilableCopiesNum
   	 */
	public Book(int catalogNum, String bookName, String description, List<String> author, List<String> genre,
			int copiesNum, Date purchaseDate, String shelfLocation, String edition, Date printDate, boolean isPopular,int avilableCopiesNum) 
	{
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
	/**
   	 * isPopular tells if the book is popular
   	 * @return boolean
   	 */
	public boolean isPopular() {
		return isPopular;
	}
	/**
   	 * setPopular set of is popular var
   	 * @param isPopular
   	 */
	public void setPopular(boolean isPopular) {
		this.isPopular = isPopular;
	}
	/**
   	 * getCatalogNum getter of catalog number
   	 * @return catalog number
   	 */
	public int getCatalogNum() {
		return catalogNum;
	}
	/**
   	 * setCatalogNum setter of catalog number
   	 */
	public void setCatalogNum(int catalogNum) {
		this.catalogNum = catalogNum;
	}
	/**
   	 * getBookName getter of book name
   	 * @return book name
   	 */
	public String getBookName() {
		return bookName;
	}
	/**
   	 * setBookName setter of book name
   	 * @param bookName
   	 */
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}
	/**
   	 * getDescription getter of book description
   	 * @return book description
   	 */
	public String getDescription() {
		return description;
	}
	/**
   	 * setDescription setterof book description
   	 * @param description
   	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
   	 * getAuthor getter of book author
   	 * @return book author
   	 */
	public List<String> getAuthor() {
		return author;
	}
	/**
   	 * setAuthor getter of book author
   	 * @param author book author
   	 */
	public void setAuthor(List<String> author) {
		this.author = author;
	}
	/**
   	 * getGenre getter of book genre
   	 * @return book genre
   	 */
	public List<String> getGenre() {
		return genre;
	}
	/**
   	 * setGenre setter of book genre
   	 * @param genre book genre
   	 */
	public void setGenre(List<String> genre) {
		this.genre = genre;
	}
	/**
   	 * getCopiesNum getter of book copies num
   	 * @return copies number of book 
   	 */
	public int getCopiesNum() {
		return copiesNum;
	}
	/**
   	 * setCopiesNum setter of book copies num
   	 * @param copiesNum of book 
   	 */
	public void setCopiesNum(int copiesNum) {
		this.copiesNum = copiesNum;
	}
	/**
   	 * getPurchaseDate getter of book purchase date
   	 * @return purchaseDate book purchase date
   	 */
	public Date getPurchaseDate() {
		return purchaseDate;
	}
	/**
   	 * setPurchaseDate setter of book purchase date
   	 * @param purchaseDate book purchase date
   	 */
	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}
	/**
   	 * getShelfLocation getter of book location on shelf
   	 * @return shelf location of book
   	 */
	public String getShelfLocation() {
		return shelfLocation;
	}
	/**
   	 * setShelfLocation setter of book location on shelf
   	 * @param shelfLocation of book
   	 */
	public void setShelfLocation(String shelfLocation) {
		this.shelfLocation = shelfLocation;
	}
	/**
   	 * getEdition getter of book edition
   	 * @return book edition
   	 */
	public String getEdition() {
		return edition;
	}
	/**
   	 * setEdition setter of book edition
   	 * @param edition   edition number
   	 */
	public void setEdition(String edition) {
		this.edition = edition;
	}
	/**
   	 * getPrintDate getter of book printing date
   	 * @return printing date
   	 */
	public Date getPrintDate() {
		return printDate;
	}
	/**
   	 * setPrintDate setter of book printing date
   	 * @param printDate book printing date
   	 */
	public void setPrintDate(Date printDate) {
		this.printDate = printDate;
	}
	/**
   	 * getBookImagePath getter of book image path
   	 * @return string of book image path
   	 */
	public String getBookImagePath( ) {
		return (bookImagePath != null) ? Defualt_Image_Path : bookImagePath;
	}
	/**
   	 * resultSetToList is taking results and set to list
   	 * @param rs ResultSet
   	 * @return list of books
   	 */
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
	/**
   	 * getAvilableCopiesNum getter of aviable copies number of book
   	 * @return number of aviable copies
   	 */
	public int getAvilableCopiesNum() {
		return avilableCopiesNum;
	}
	/**
   	 * getAvilableCopiesNum setter of aviable copies number of book
   	 * @param avilableCopiesNum of aviable copies
   	 */
	public void setAvilableCopiesNum(int avilableCopiesNum) {
		this.avilableCopiesNum = avilableCopiesNum;
	}
}
