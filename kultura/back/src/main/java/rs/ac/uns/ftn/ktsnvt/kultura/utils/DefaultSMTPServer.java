package rs.ac.uns.ftn.ktsnvt.kultura.utils;

import rs.ac.uns.ftn.ktsnvt.kultura.service.SMTPServer;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;


public class DefaultSMTPServer implements SMTPServer {
    private final Session session;

    public DefaultSMTPServer(Session session) {
        this.session = session;
    }

    public void sendEmail(String toEmail, String subject, String body) throws Exception {
        MimeMessage msg = new MimeMessage(session);
        //set message headers
        msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
        msg.addHeader("format", "flowed");
        msg.addHeader("Content-Transfer-Encoding", "8bit");

        msg.setFrom(new InternetAddress("no-replay@kultura.rs", "NoReply-JD"));
        msg.setReplyTo(InternetAddress.parse("no-replay@kultura.rs", false));
        msg.setSubject(subject, "UTF-8");
        msg.setText(body, "UTF-8");
        msg.setSentDate(new Date());
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));

        Transport.send(msg);
    }
}