package common.entity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.Serializable;
/**
 * The TransferFile class represent file that sent between the server and the client 
 * @author  Kfir Wilfand
 * @author Bar Korkos
 * @author Zehavit Otmazgin
 * @author Noam Drori
 * @author Sapir Hochma
 */
public class TransferFile implements Serializable{

	private String Description=null;
	private String fileName=null;	
	private int size=0;
	public  byte[] mybytearray;
	
	
	public void initArray(int size)
	{
		mybytearray = new byte [size];	
	}
	
	public TransferFile(String fileName) {
		this.fileName = fileName;
	}
	
	public String getFileName() {
 		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public byte[] getMybytearray() {
		return mybytearray;
	}
	
	public byte getMybytearray(int i) {
		return mybytearray[i];
	}

	public void setMybytearray(byte[] mybytearray) {
		
		for(int i=0;i<mybytearray.length;i++)
		this.mybytearray[i] = mybytearray[i];
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}	
	/**
	 * create a file to transfer
	 * @param Path
	 */
    public static TransferFile createFileToTransfer(String Path)
    {
    	if(Path.isEmpty())
    		return null;
        TransferFile msg=new TransferFile(Path);
        try {
        	File newFile = new File(Path);
        	if(!newFile.exists())
        		return null;
        	byte[] mybytearray  = new byte [(int)newFile.length()];
		    FileInputStream fis = new FileInputStream(newFile);
		    BufferedInputStream bis = new BufferedInputStream(fis);
		    msg.initArray(mybytearray.length);
		    msg.setSize(mybytearray.length);
		    bis.read(msg.getMybytearray(),0,mybytearray.length);
		  
        }
		catch (Exception e) {
			System.out.println("Error send (Files)msg) to Server");
		}
        return msg;
    }	
	
	
	
	
}
