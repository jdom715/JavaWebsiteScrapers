package org.websitescrapers.nordstrom.util;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.websitescrapers.nordstrom.model.Product;

@Service
public class EmailClient {
  private static final String FROM_EMAIL_ADDRESS = "FROM_EMAIL_ADDRESS";
  private static final String FROM_EMAIL_PASSWORD = "FROM_EMAIL_PASSWORD";
  private final Logger logger = LogManager.getLogger();
  private final String fromEmail;

  private final Session session;

  public EmailClient() {
    fromEmail = System.getenv(FROM_EMAIL_ADDRESS);
    final Authenticator auth = getAuthenticator();
    final Properties props = setAndGetEmailProperties();
    session = Session.getInstance(props, auth);
  }

  private Authenticator getAuthenticator() {
    final String password = System.getenv(FROM_EMAIL_PASSWORD);

    return new Authenticator() {
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(fromEmail, password);
      }
    };
  }

  private Properties setAndGetEmailProperties() {
    final Properties props = new Properties();
    props.put("mail.smtp.host", "smtp.gmail.com");
    props.put("mail.smtp.port", "587");
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    return props;
  }

  public void sendProductAvailableEmail(final Product product) throws MessagingException {
    final MimeMessage message = new MimeMessage(session);
    message.setSubject("Your Product is Available");
    final Map<String, String> requesters = product.getRequesters();
    final Set<Entry<String, String>> emailToNameSet = requesters.entrySet();
    for (final Entry<String, String> entry : emailToNameSet) {
      message.setRecipient(Message.RecipientType.TO, new InternetAddress(entry.getKey()));
      final String msg =
          String.format(
              "Hello %s,\n\nYour %s is available in style [%s,%s].\n\n%s",
              entry.getValue(),
              product.getDescription(),
              product.getColor(),
              product.getSize(),
              product.getUrl());
      sendEmail(message, msg);
    }
  }

  private void sendEmail(final MimeMessage message, final String msg) throws MessagingException {
    final MimeBodyPart mimeBodyPart = new MimeBodyPart();
    mimeBodyPart.setContent(msg, "text/plain");
    Multipart multipart = new MimeMultipart();
    multipart.addBodyPart(mimeBodyPart);
    message.setContent(multipart);
    Transport.send(message);
  }

  public void sendErrorEmail(final Exception e) throws MessagingException {
    logger.error(e);
    final MimeMessage message = new MimeMessage(session);
    message.setRecipient(Message.RecipientType.TO, new InternetAddress(fromEmail));
    message.setSubject("There Was an Error with the Nordstrom Product Availability Checker");
    final String msg = ExceptionUtils.getStackTrace(e);
    sendEmail(message, msg);
  }
}
