/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import model.SessionData;
/**
 *
 * @author Dell
 */
public class EmailService {

    public static void sendOTP(String receiverEmail, String otp) {

        final String senderEmail = "pawsclawsfinder@gmail.com";
        final String senderPassword = "fcev uxpm feer esro";

        Properties props = new Properties();

        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {

            @Override
            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {

            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress(senderEmail));

            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(receiverEmail)
            );

            message.setSubject("Paws & Claws Finder OTP Verification");

            if (SessionData.role.equalsIgnoreCase("Provider")) {
                message.setText(
                        """
                        Hi Provider,
                        
                        Thank you for joining Paws & Claws Finder.
                        We appreciate your support in helping pets find loving homes.
                        
                        Your OTP is: """ + otp
                    );

            } else if (SessionData.role.equalsIgnoreCase("Adopter")) {

                message.setText(
                    """
                    Hi Adopter,
                    
                    Thank you for using Paws & Claws Finder.
                    We look forward to helping you find a reliable pet companion.
                    
                    Your OTP is: """ + otp
                );
            }

            Transport.send(message);

            System.out.println("OTP sent successfully to: " + receiverEmail);

        } catch (MessagingException e) {

            System.out.println("Error while sending OTP email: " + e.getMessage());
        }
    }
}
