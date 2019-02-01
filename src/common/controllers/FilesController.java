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

public class FilesController {
	
	private static FilesController instance;

	private FilesController(){}

	public static FilesController getInstance(){
		if(instance == null){
			instance = new FilesController();
		}
		return instance;
	}
	
	public void SaveTableOfContent (TransferFile tf, String bookName,String path)
	{
		int fileSize = tf.getSize();
		URL url = getClass().getResource(path);
		String str=url.getPath().toString()+bookName.replace(" ","_")+".pdf";
		
		//str=str.replace('/', '\\');
		str=str.replaceAll("bin", "src");
		File file = new File(str);
 
	      try {
	  		FileOutputStream fis = new FileOutputStream(file);
	  		try {
	  			fis.write(tf.getMybytearray(),0,tf.getSize());
	  		} catch (IOException e) {
	  			// TODO Auto-generated catch block
	  			e.printStackTrace();
	  		}
	  	} catch (FileNotFoundException e) {
	  		// TODO Auto-generated catch block
	  		e.printStackTrace();
	  	}
	}
	      
	  	public void SavePhoto(TransferFile tf, String bookName,String path)
		{
			int fileSize = tf.getSize();
			URL url = getClass().getResource(path);
			String str=url.getPath().toString()+bookName.replace(" ","_")+".png";
			str=str.replace('/', '\\');
			str=str.replaceAll("bin", "src");
			File file = new File(str);
			try {
		  		FileOutputStream fis = new FileOutputStream(file);
		  		try {
		  			
		  			fis.write(tf.getMybytearray(),0,tf.getSize());
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
