package server.controllers;
import java.sql.SQLException;
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

	
	
}
