package ch.uzh.ifi.feedback.library.mail;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@Singleton
public class MailClient {

	private static final Log LOGGER = LogFactory.getLog(MailClient.class);
	private String host;
	private String port;
	private String user;
	private String password;
	private Session session;
	private String orchestratorUrl;
	private String feedbackMailKeyName;
	private String repositoryUrl;
	private boolean isMailFeedbackEnabled;

	@Inject
	public MailClient(IMailConfiguration configuration)
	{
		this.host = configuration.getHost();
		this.port = configuration.getPort();
		this.user = configuration.getUser();
		this.password = configuration.getPassword();
		this.session = createSession();
		this.orchestratorUrl = configuration.getOrchestratorUrl();
		this.repositoryUrl = configuration.getRepositoryUrl();
		this.feedbackMailKeyName = configuration.getFeedbackMailKeyName();
		this.isMailFeedbackEnabled = configuration.isMailFeedbackEnabled();
	}

	public void sendEmail(String toAddress,
            String subject, String message) throws AddressException,
            MessagingException
    {
		this.sendEmail(toAddress, subject, message, null);
    }

    public void sendEmail(String toAddress,
            String subject, String message, List<Attachment> attachments) throws AddressException,
            MessagingException
    {
        // creates a new e-mail message
    	MimeMessage msg = new MimeMessage(session);

        msg.setFrom(new InternetAddress(user));
        InternetAddress[] toAddresses = { new InternetAddress(toAddress) };
        msg.setRecipients(Message.RecipientType.TO, toAddresses);
        msg.setSubject(subject);
        msg.setSentDate(new Date());

        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(message, "text/html; charset=utf-8");
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);

        if(attachments != null) {
	        for(Attachment attachment : attachments) {
	        	// 	Part two is attachment
		        if(attachment.getFilePath() != null && !attachment.getFilePath().equals("")) {
					try {
						String rootPath = System.getProperty("catalina.home");
						if(rootPath == null) {
							break;
						}
						String relativePath = "webapps" + File.separator + attachment.getFilePath();
						String filePath = rootPath + File.separator + relativePath;

						File file = new File(filePath);
						if (file.exists()) {
							messageBodyPart = new MimeBodyPart();
							DataSource source = new FileDataSource(filePath);
							messageBodyPart.setDataHandler(new DataHandler(source));
							messageBodyPart.setFileName(attachment.getFileNameAndExtension());
							multipart.addBodyPart(messageBodyPart);
						}
					} catch (Exception e) {
						LOGGER.error(e.getMessage());
					}
		        }
	        }
        }

        msg.setContent(multipart);

        // sends the e-mail
        Transport.send(msg);
    }

	private Session createSession()
	{
        // sets SMTP server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        // creates a new session with an authenticator
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        };

        Session session = Session.getInstance(properties, auth);
        return session;
	}

	public String getOrchestratorUrl() {
		return orchestratorUrl;
	}

	public String getRepositoryUrl() {
		return repositoryUrl;
	}

	public String getFeedbackMailKeyName() {
		return feedbackMailKeyName;
	}

	public boolean isMailFeedbackEnabled() {
		return isMailFeedbackEnabled;
	}
}
