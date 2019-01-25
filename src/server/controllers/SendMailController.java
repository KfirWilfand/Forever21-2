package server.controllers;

public class SendMailController {
	
	private  String OblMail;
	private  String OblPassword;
	private  String host;
	private static SendMailController instance;

	 private SendMailController(){
		 this.OblMail="obl.group21@gmail.com";
		 this.OblPassword="obl123456!";
		 this.host = "localhost";
	 }
	 
	 public static SendMailController getInstance(){
	        if(instance == null){
	            instance = new SendMailController();
	        }
	        return instance;
	 }

	
	
}
