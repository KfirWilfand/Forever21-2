package common.controllers;

import java.util.List;

public class Book {
	private Integer catalogNum;
	private String name;
	private String description;
	private String edition;
	private String printDate;
	private String copiesNum;
	private String shelfLocation;
	private List<String> genre;
	private List<String> author;
	
	public Book(Integer catalogNum, String name, String description, String edition, String printDate,
			String copiesNum, String shelfLocation, List<String> genre, List<String> author) {
		super();
		this.catalogNum = catalogNum;
		this.name = name;
		this.description = description;
		this.edition = edition;
		this.printDate = printDate;
		this.copiesNum = copiesNum;
		this.shelfLocation = shelfLocation;
		this.genre = genre;
		this.author = author;
	}
}
