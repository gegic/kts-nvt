package rs.ac.uns.ftn.ktsnvt.kultura.service;

public interface SMTPServer {
    void sendEmail(String toEmail, String subject, String body) throws Exception;
}
