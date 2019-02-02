package common.controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;

import common.controllers.Message;
import common.controllers.enums.OperationType;
import common.controllers.enums.ReturnMessageType;
import common.entity.TransferFile;
/**
 * The FilesController class represent the Files controller
 * @author  Kfir Wilfand
 * @author Bar Korkos
 * @author Zehavit Otmazgin
 * @author Noam Drori
 * @author Sapir Hochma
 */
public class FilesController {
	/**instance is a singleton of the class */
	private static FilesController instance;

	private FilesController(){}
	 /**
	 * getInstance is creating the singleton object of the class
	 * @return FilesController
	 */
	public static FilesController getInstance(){
		if(instance == null){
			instance = new FilesController();
		}
		return instance;
	}
	
	/**
	 * SaveTableOfContent method save that table of content at a path
	 * @param tf             object of transferFile
	 * @param bookName		 name of book
	 * @param path           path of destination
	 */
	public void SaveTableOfContent (TransferFile tf, String bookName,String path)
	{
		int fileSize = tf.getSize();

		String str=path+bookName.replace(" ","_")+".pdf";
		
		File file = new File(str);
 
	      try {
	  		FileOutputStream fis = new FileOutputStream(file);
	  		try {
	  			fis.write(tf.getMybytearray(),0,tf.getSize());
	  			fis.close();
	  		} catch (IOException e) {
	  			// TODO Auto-generated catch block
	  			e.printStackTrace();
	  		}
	  	} catch (FileNotFoundException e) {
	  		// TODO Auto-generated catch block
	  		e.printStackTrace();
	  	}
	}
	   
	/**
	 * SavePhoto method save that photo at a path
	 * @param tf                file that include the photo
	 * @param bookName          the name of the book
	 * @param path              the path
	 */
	  	public void SavePhoto(TransferFile tf, String bookName,String path)
		{
			int fileSize = tf.getSize();

			String str=path+bookName.replace(" ","_")+".png";
		

			File file = new File(str);
			try {
		  		FileOutputStream fis = new FileOutputStream(file);
		  		try {

		  			fis.write(tf.getMybytearray(),0,tf.getSize());
		  			fis.close();
		  		} catch (IOException e) {
		  			// TODO Auto-generated catch block
		  			e.printStackTrace();
		  		}
		  	} catch (FileNotFoundException e) {
		  		// TODO Auto-generated catch block
		  		e.printStackTrace();
		}
	         
	}


}
