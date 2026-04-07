package com.digiqual.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final ObjectProvider<JavaMailSender> mailSenderProvider;

    public NotificationService(ObjectProvider<JavaMailSender> mailSenderProvider) {
        this.mailSenderProvider = mailSenderProvider;
    }

    public void sendStudentLoginCredentials(String email, String fullName, String studentId, String tempPassword) {
        String subject = "DIGIQUAL Student Portal Access";
        String body = "Hello " + safe(fullName) + ",\n\n"
                + "Your student account has been approved.\n"
                + "Student ID: " + safe(studentId) + "\n"
                + "Email: " + safe(email) + "\n"
                + "Temporary Password: " + safe(tempPassword) + "\n\n"
                + "Please sign in and change your password after first login.\n\n"
                + "Regards,\nDIGIQUAL";

        JavaMailSender sender = mailSenderProvider.getIfAvailable();
        if (sender == null) {
            log.info("Email sender not configured. Credentials fallback log -> email={}, studentId={}, tempPassword={}", email, studentId, tempPassword);
            return;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(subject);
        message.setText(body);
        sender.send(message);
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }
}
