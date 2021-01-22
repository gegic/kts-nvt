package rs.ac.uns.ftn.ktsnvt.kultura.service;

import org.springframework.scheduling.annotation.Async;

public interface SMTPServer {
    @Async
    void sendEmail(String toEmail, String subject, String body) throws Exception;
}
