package client.controllers.adapters;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class QueryBuilder {
    
	private static QueryBuilder instance;
    
    private QueryBuilder(){}
    
    public static QueryBuilder getInstance(){
        if(instance == null){
            instance = new QueryBuilder();
        }
        return instance;
    }
	
	
	public String searchQuery(String bookName, String authorName, String bookGenre, String freeText) 
	{
    	String query="SELECT * FROM obl.books WHERE 1=1";
    	if (!bookName.isEmpty())
    		query=query+" AND books.bName REGEXP '^"+bookName+"'";
    	
    	if (!bookGenre.isEmpty()) {
    		bookGenre=bookGenre.replaceAll(" ", "");
    		String[] geners=bookGenre.split(",");
    		for(String genre : geners)
    		{
    			query=query+" AND books.bGenre REGEXP '"+genre+"'";
    		}
    	}
    	
    	if (!authorName.isEmpty()) {
    		authorName=authorName.replaceAll(" ", "");
           	String[] authors=authorName.split(",");
    		for(String author : authors)
    		{
    			query=query+" AND books.bGenre REGEXP '"+author+"'";
    		}
    	}
    	if(!freeText.isEmpty()) {
    		String[] words = freeText.split("\\W+");
    		for(String word: words)
    		{
    			query=query+" AND books.bDescription REGEXP '"+word+"'";
    		}
    	}
    	return query;
	}
}
