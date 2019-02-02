package server.controllers;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

import common.entity.Subscriber;
/**
 * The SendMailController class represent the send mail to subscriber controller on the servers side
 * @author  Kfir Wilfand
 * @author Bar Korkos
 * @author Zehavit Otmazgin
 * @author Noam Drori
 * @author Sapir Hochma
 */
public class SendMailController {
	 /** USER_NAME is cGMail user name*/
    private static String USER_NAME = "obl.group21";  // GMail user name (just the part before "@gmail.com")
    /** PASSWORD is GMail password*/
    private static String PASSWORD = "obl12345!!"; // GMail password
    /** RECIPIENT string*/
    private static String RECIPIENT = "bar1160@gmail.com";

    /**
	 * sendMailToSubscriber is sending email to the subscriber email 
	 * @param to who to send
	 * @param subject of message
	 * @param body of the mail
	 */
    public static void  sendMailToSubscriber(Subscriber to,String subject, String body )
    { 	
    	String[] sendTo=new String[1];
    	sendTo[0]=to.getEmail();
    	sendFromGMail(sendTo,subject,body);
    }
    
    /**
  	 * sendFromGMail is sending from gmail
  	 * @param to who to send
  	 * @param subject of message
  	 * @param body of the mail
  	 * @exception AddressException
  	 * @exception MessagingException
  	 */
    private static void sendFromGMail(String[] to, String subject, String body) {
        Properties props = System.getProperties();
        String host = "smtp.gmail.com";
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", USER_NAME);
        props.put("mail.smtp.password", PASSWORD);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(USER_NAME));
            InternetAddress[] toAddress = new InternetAddress[to.length];

            // To get the array of addresses
            for( int i = 0; i < to.length; i++ ) {
                toAddress[i] = new InternetAddress(to[i]);
            }

            for( int i = 0; i < toAddress.length; i++) {
                message.addRecipient(Message.RecipientType.TO, toAddress[i]);
            }

            message.setSubject(subject);
            message.setText(body);
            Transport transport = session.getTransport("smtp");
            transport.connect(host, USER_NAME, PASSWORD);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        }
        catch (AddressException ae) {
            ae.printStackTrace();
        }
        catch (MessagingException me) {
            me.printStackTrace();
        }
    }
    
    /**
  	 * sendLockInboxToLibraryManager is sending an inbox message to the library manager to lock subscriber that lates 3 times at return books
  	 * @param subscriberNumber     subscriber number to lock
  	 */

    public static void sendLockInboxToLibraryManager(int subscriberNumber) throws SQLException

    {
    	DBcontroller DBcObj=DBcontroller.getInstance();
    	Timestamp today= new Timestamp(System.currentTimeMillis());
    	String getLibraryManagers="SELECT usrID FROM obl.users where usrType='LibraryManager'";
    	ResultSet libraryManagers=DBcObj.query(getLibraryManagers);
    	while(libraryManagers.next())
    	{
    		String title="Lock subscriber request";
    		String body="Subscriber number '"+subscriberNumber+"' late in returning 3 times.You have the option to lock his reader card";
    		String query="insert into obl.inbox_msg (usrID,Title,body,type,is_read,date) values("+libraryManagers.getString("usrID")+",'"+title+"',\""+body+"\",'LockReader',0,'"+today+"')";
    		System.out.println(query);
    		DBcObj.update(query);
    	}
    }
    
    /**
  	 * sendReminderInbox is sending an inbox message to user to remind something
  	 * @param usrID         user id
  	 * @param msgTitle      the title of the inbox message
  	 * @param msgBody       the body of the inbox message 
  	 */
    public static void sendReminderInbox(int usrID,String msgTitle, String msgBody)
    {
    	DBcontroller DBcObj=DBcontroller.getInstance();
    	Timestamp today= new Timestamp(System.currentTimeMillis());
    	//java.sql.Date today=java.sql.Date.valueOf(LocalDate.now());
    	String title="Reminder: "+msgTitle;
    	String body=msgBody;
    	String query="insert into obl.inbox_msg (usrID,Title,body,type,is_read,date) values("+usrID+",'"+title+"','"+body+"','Reminder',0,'"+today+"')";
    	DBcObj.update(query);
    }
    
    /**
  	 * sendAlertInbox is sending an inbox Alert message to user 
  	 * @param usrID          user id
  	 * @param msgTitle       the title of the inbox message
  	 * @param msgBody        the body of the inbox message 
  	 */
    public static void sendAlertInbox(int usrID,String msgTitle, String msgBody)
    {
    	DBcontroller DBcObj=DBcontroller.getInstance();
    	Timestamp today= new Timestamp(System.currentTimeMillis());
    	//java.sql.Date today=java.sql.Date.valueOf(LocalDate.now());
    	String title="Alert: "+msgTitle;
    	String body=msgBody;
    	String query="insert into obl.inbox_msg (usrID,Title,body,type,is_read,date) values("+usrID+",'"+title+"','"+body+"','Alert',0,'"+today+"')";
    	DBcObj.update(query);
    }
    
    
}
