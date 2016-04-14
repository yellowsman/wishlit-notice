package mail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.util.Properties;
import java.util.UUID;

// These are from the JavaMail API, which you can download at https://java.net/projects/javamail/pages/Home. 
// Be sure to include the mail.jar library in your project. In the build order, mail.jar should precede the AWS SDK for Java library.
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

// These are from the AWS SDK for Java, which you can download at https://aws.amazon.com/sdk-for-java.
// Be sure to include the AWS SDK for Java library in your project.
import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.RawMessage;
import com.amazonaws.services.simpleemail.model.SendRawEmailRequest;

public class ComposeAndSendMIMEEmail {

	// IMPORTANT: To successfully send an email, you must replace the values of the strings below with your own values.
	private static String EMAIL_FROM = "sendmail_wishlist_notice@yahoo.co.jp"; // Replace with the sender's address. This address must be verified with Amazon SES.
	private static String EMAIL_RECIPIENT = ""; // Replace with a recipient address. If your account is still in the sandbox,

	// IMPORTANT: Ensure that the region selected below is the one in which your identities are verified.
	private static Regions AWS_REGION = Regions.US_EAST_1; // Choose the AWS region of the Amazon SES endpoint you want to connect to. Note that your
															// sandbox
															// status, sending limits, and Amazon SES identity-related settings are specific to a
															// given AWS
															// region, so be sure to select an AWS region in which you set up Amazon SES. Here, we are
															// using
															// the US West (Oregon) region. Examples of other regions that Amazon SES supports are
															// US_EAST_1
															// and EU_WEST_1. For a complete list, see
															// http://docs.aws.amazon.com/ses/latest/DeveloperGuide/regions.html

	private static String EMAIL_SUBJECT = "Price down Items in your Wishlist.";

	public ComposeAndSendMIMEEmail(String to_address, String subject) {
		EMAIL_RECIPIENT = to_address;
		EMAIL_SUBJECT = subject;
	}

	public static void main(String[] args) throws AddressException, MessagingException, IOException {
		ComposeAndSendMIMEEmail casmm = new ComposeAndSendMIMEEmail("keroro966orz@hotmail.co.jp", "method test");
		casmm.sendMail("this mail sent for sendMail() in ComposeAndSendMIMEEmail.java"); 
	}

	public void sendMail(String text) throws AddressException, MessagingException, IOException {

		Session session = Session.getDefaultInstance(new Properties());
		MimeMessage message = new MimeMessage(session);
		message.setSubject(EMAIL_SUBJECT, "UTF-8");

		message.setFrom(new InternetAddress(EMAIL_FROM));
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(EMAIL_RECIPIENT));

		// Cover wrap
		MimeBodyPart wrap = new MimeBodyPart();

		// Alternative TEXT/HTML content
		MimeMultipart cover = new MimeMultipart("alternative");
		MimeBodyPart html = new MimeBodyPart();
		cover.addBodyPart(html);

		wrap.setContent(cover);

		MimeMultipart content = new MimeMultipart("related");
		message.setContent(content);
		content.addBodyPart(wrap);

		html.setContent(text, "text/html");

		try {
			System.out.println("Attempting to send an email through Amazon SES by using the AWS SDK for Java...");

			/*
			 * The ProfileCredentialsProvider will return your [default] credential profile by reading from the credentials file located at
			 * (~/.aws/credentials).
			 * 
			 * TransferManager manages a pool of threads, so we create a single instance and share it throughout our application.
			 */
			AWSCredentials credentials = null;
			try {
				credentials = new ProfileCredentialsProvider().getCredentials();
			} catch (Exception e) {
				throw new AmazonClientException(
						"Cannot load the credentials from the credential profiles file. "
								+ "Please make sure that your credentials file is at the correct "
								+ "location (~/.aws/credentials), and is in valid format.", e);
			}

			// Instantiate an Amazon SES client, which will make the service call with the supplied AWS credentials.
			AmazonSimpleEmailServiceClient client = new AmazonSimpleEmailServiceClient(credentials);
			Region REGION = Region.getRegion(AWS_REGION);
			client.setRegion(REGION);

			// Print the raw email content on the console
			PrintStream out = System.out;
			message.writeTo(out);

			// Send the email.
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			message.writeTo(outputStream);
			RawMessage rawMessage = new RawMessage(ByteBuffer.wrap(outputStream.toByteArray()));

			SendRawEmailRequest rawEmailRequest = new SendRawEmailRequest(rawMessage);
			client.sendRawEmail(rawEmailRequest);
			System.out.println("Email sent!");

		} catch (Exception ex) {
			System.out.println("Email Failed");
			System.err.println("Error message: " + ex.getMessage());
			ex.printStackTrace();
		}
	}
}
