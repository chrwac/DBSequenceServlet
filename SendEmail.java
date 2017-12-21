
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.*;

public class SendEmail {
	public SendEmail(String to,String subject,String message_text)
	{
		String host="smtp.gmail.com";
		final String user="scaffoldseqencewebserver@gmail.com";
		final String pass = "tunasandwich";
		String from="scaffoldsequencewebserver@gmail.com";
		
		Properties properties = new Properties();
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable","true");
		properties.put("mail.smtp.host","smtp.gmail.com");
		properties.put("mail.smtp.port","587");
		properties.put("mail.smtp.user",user);
		properties.put("mail.smtp.password",pass);
		
		Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
		    protected PasswordAuthentication getPasswordAuthentication() {
		        return new PasswordAuthentication(user, pass);
		    }
		});
		
		
		MimeMessage message =  new MimeMessage(session);
		try
		{
			message.setFrom(new InternetAddress(from));
			message.setRecipients(Message.RecipientType.CC,InternetAddress.parse(to,true));
			message.setSubject(subject);
			message.setText(message_text);
			Transport transport = session.getTransport("smtp");
			transport.connect(host,from,pass);
			transport.sendMessage(message,message.getAllRecipients());
			transport.close();
		}
		catch(AddressException ae)
		{
			ae.printStackTrace();
		}
		catch(MessagingException me)
		{
			me.printStackTrace();
		}
	}
}
